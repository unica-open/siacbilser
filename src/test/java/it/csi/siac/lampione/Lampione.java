/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.lampione;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import it.csi.siac.lampione.http.SingleThreadCookieManager;
import it.csi.siac.mail.Parallelizer;


public class Lampione {
	
	private Integer userId;
	private String remoteUser;
	private String folder;
	private EnvEnum env;
	private List<File> files;
	private boolean parallelize;
	
	public static void main(String[] args) {
		Lampione lampione = new Lampione();
		try {
			lampione.init(args); // <environment> <remote-user> 
			lampione.uploadFiles();
			
			System.exit(0);
		} catch (LampioneException t) {
			lampione.printErrorAndExit(t);
		}
	}

	/**
	 * Initialization
	 * @param environment the environment
	 * @throws LampioneException if an exception occured in initialization
	 */
	public void init(String[] args) throws LampioneException {
		Properties ownProperties = readProperties();
		
		userId = Integer.valueOf(ownProperties.getProperty("lampione.webmin.user.id"));
		
		folder = readFolder(ownProperties);
		// TODO
		parallelize = true;
		
		String environment = getArg(args, 0);
		env = environment != null ? EnvEnum.valueOf(environment) : EnvEnum.CONSIP;
		
		String remoteUser = getArg(args, 1);
		this.remoteUser = remoteUser != null ? remoteUser : "ajb640"; 
		
		// Sets the CookieHandler. Handles the cookies thread-by-thread
		CookieHandler.setDefault(new SingleThreadCookieManager());
	}

	private String getArg(String[] args, int i) {
		if (args.length < i + 1) {
			return null;
		}
		
		return args[i];
	}
	
	private String readFolder(Properties ownProperties) {
		
		String f = ownProperties.getProperty("lampione.source.dir");
		
		if (f != null) {
			return f;
		}
		
		return ownProperties.getProperty("consip_dir");
	}

	/**
	 * Reads the properties from a file
	 * @throws LampioneException if an exception occured in the reading
	 */
	private Properties readProperties() throws LampioneException {
		Properties ownProperties = new Properties();
		InputStream is = null;
		try {
			is = new FileInputStream("buildfiles/own.properties");
			ownProperties.load(is);
		} catch (IOException e) {
			throw new LampioneException("Errore nel caricamento delle properties", e);
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new LampioneException("Errore nella chiusura dell'InputStream delle properties", e);
				}
			}
		}
		return ownProperties;
	}
	
	/**
	 * File upload
	 * @throws in case an exception occurred
	 */
	public void uploadFiles() throws LampioneException { 
		files = readFolderFiles();

		if (!files.isEmpty()) {
			doUploadFiles();
			deleteFiles();
		}
		
		log("\nBye");
	}
	
	/**
	 * Prints the error stack and exits
	 * @param t the throwable to print
	 */
	public void printErrorAndExit(Throwable t) {
		t.printStackTrace(System.err);
		System.exit(1);
	}

	
	private void doUploadFiles() throws LampioneException {
		this.parallelize = env.getHosts().size() == 1 ? false : !"N".equalsIgnoreCase(ask("Parallelize upload? ([Y]es/[n]o) "));
		
		Parallelizer<Entry<String, String>> parallelizer = 
				new Parallelizer<Entry<String, String>>(env.getHosts(), new LampioneCallable(files, remoteUser, userId), true);
		try {
			parallelizer.doWork(parallelize ? env.getHosts().size() : 1);
		} catch(IllegalStateException ise) {

			log("cause: " + ise.getCause()); 
			log("cause message: " + ise.getCause() != null ? ise.getCause().getMessage() : "");
			
			throw new LampioneException("Exception in parallelizer work", ise.getCause());
		}
	}
	
	private void deleteFiles() throws LampioneException {
		String ans = ask("Delete local files? ([Y]es/[n]o) ");
		
		if (StringUtils.isNotBlank(ans) && !"y".equalsIgnoreCase(ans)){
			return;
		}
		
		for (File file : files) {
			if(file.delete()) {
				log(file.getName() + " deleted");
			} else {
				log("Error deleting " + file.getName());
			}
		}
	}

	private List<File> readFolderFiles() throws LampioneException {
		File folderFile = new File(folder);
		log("Source folder is " + canonicalize(folderFile));
		List<File> files = new ArrayList<File>(FileUtils.listFiles(folderFile, null, true));

		if (files.isEmpty()) {
			log("Nothing to do");
			return files;
		}
		
		for (File file : files) {
			log(canonicalize(file));
		}
		
		log("\nConfirm files:");

		List<File> confirmedFiles = new ArrayList<File>();
		
		for (File file : files) {
			String answer = ask(String.format("%s? ([y]es/[n]o/[A]ll) ", canonicalize(file)));
			
			if (StringUtils.isBlank(answer) || "A".equalsIgnoreCase(answer)) {
				return files;
			}
			
			if ("Y".equalsIgnoreCase(answer)) {
				confirmedFiles.add(file);
			}
		}
		
		return confirmedFiles;
	}
	
	private String canonicalize(File path) throws LampioneException {
		try {
			return path.getCanonicalPath();
		} catch (IOException e) {
			throw new LampioneException("IOException in retrieving canonical path for file " + path, e);
		}
	}

	private void log(String message) {
		System.out.println(message);
	}

	private String ask(String text) throws LampioneException {
		System.out.print(text);
		try {
			return new BufferedReader(new InputStreamReader(System.in)).readLine();
		}
		catch (IOException e) {
			throw new LampioneException("IOException in user ask", e);
		}
	}
	
}

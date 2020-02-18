/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.lampione;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.csi.siac.lampione.http.MultipartUtility;
import it.csi.siac.mail.Parallelizer.ParallelizerCallable;

public class LampioneCallable extends ParallelizerCallable<Entry<String, String>> {
	
	private static final String FARO_HOST = "faro-bilancio.ecosis.csi.it";
	private static final String TARGET_DIR = "/tmp/lampione";
	
	private static final Pattern PATTERN = Pattern.compile("upload\\.cgi\\?id\\&#61;(\\d+)");
	
	private final List<File> files;
	private final String remoteUser;
	private final Integer userId;
	private final Charset charset;
	
	LampioneCallable(List<File> files, String remoteUser, Integer userId) {
		this.files = files;
		this.remoteUser = remoteUser;
		this.userId = userId;
		this.charset = Charset.forName("UTF-8");
	}
	
	private void log(String message) {
		System.out.println("[Thread " + Thread.currentThread().getName() + "] :: " + message);
	}

	@Override
	protected void doCall(Entry<String, String> host) {
		
		log(String.format("Preparing upload to %s as user %s", host.getValue(), remoteUser));
		for (int i = 0; i < (files.size() - 1) / 4 + 1; i++) {
			List<File> chunk = files.subList(4 * i, Math.min(4 * (i + 1), files.size()));
			try {
				uploadToHost(host.getKey(), host.getValue(), chunk);
			} catch(Exception e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	private void uploadToHost(String targetHostId, String targetHost, List<File> files) throws Exception {
		
		Thread.sleep(500);

		login(targetHostId);
		
		// Thread.sleep(1000);
		String uploadId = retrieveUploadId(targetHost);
		Thread.sleep(1000);
		uploadFiles(targetHost, files, uploadId);
			
		log(String.format("Upload to %s completed", targetHost));
	}

	private void uploadFiles(String targetHost, List<File> files, String uploadId) throws IOException {
		MultipartUtility mu = new MultipartUtility(String.format("http://%s/%s/updown/upload.cgi?id=%s", FARO_HOST, targetHost, uploadId), charset);
		
		mu.addHeaderField("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		mu.addHeaderField("Accept-Encoding", "gzip, deflate");
		mu.addHeaderField("Accept-Language", "en-US,en;q=0.5");
		mu.addHeaderField("Cache-Control", "no-cache");
		mu.addHeaderField("Connection", "keep-alive");
		mu.addHeaderField("Content-Language", "en-US");
		mu.addHeaderField("Host", FARO_HOST);
		mu.addHeaderField("Pragma", "no-cache");
		mu.addHeaderField("Upgrade-Insecure-Requests", "1");
		mu.addHeaderField("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0");
		mu.addHeaderField("Referer", String.format("http://%s/%s/updown/index.cgi", FARO_HOST, targetHost));
		mu.addHeaderField("Origin", String.format("http://%s", FARO_HOST));
		
		mu.addFormField("dir", TARGET_DIR);
		mu.addFormField("mkdir", "1");
//		mu.addFormField("user", "ajb640");
		mu.addFormField("user", remoteUser);
		mu.addFormField("group_def", "1");
		mu.addFormField("group", "");
		mu.addFormField("zip", "0");
		mu.addFormField("email_def", "1");
		mu.addFormField("ok", "Inizia l'upload");
		
		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			log(file.getCanonicalPath());
			mu.addFilePart("upload" + i, file);
		}
		mu.finish();
	}

	private String retrieveUploadId(String targetHost) throws MalformedURLException, IOException, ProtocolException, LampioneException {
		HttpURLConnection conn = null;
		URL url = new URL(String.format("http://%s/%s/updown/index.cgi", FARO_HOST, targetHost));
		String page = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			setDefaultHeaders(conn);
			conn.addRequestProperty("Referer", String.format("http://%s/%s/", FARO_HOST, targetHost));
			
			// Get response
			evaluateResponseCode(conn.getResponseCode());
			page = getResponseString(conn.getInputStream());
		} finally {
			if(conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		Matcher matcher = PATTERN.matcher(page);
		String uploadId = matcher.find() ? matcher.group(1) : null;
		log("Read Upload ID " + uploadId);
		if (uploadId == null) {
			log("=====================\n" + page + "\n=====================");
			throw new LampioneException("Cannot read Upload ID");
		}
		return uploadId;
	}

	private void login(String targetHostId) throws MalformedURLException, IOException, ProtocolException, LampioneException {
		HttpURLConnection conn = null;
		URL url = new URL(String.format("http://%s/webmin_%s_ID%d/", FARO_HOST, targetHostId, userId));
		// Login
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			setDefaultHeaders(conn);
			
			// Get response
			evaluateResponseCode(conn.getResponseCode());
		} finally {
			if(conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
	}

	/**
	 * Controlla lo status code della response
	 * @param responseCode lo status code
	 */
	private void evaluateResponseCode(int responseCode) throws LampioneException {
		if(responseCode < 200 || responseCode > 301) {
			System.out.println(responseCode);
			throw new LampioneException("Errore nell'acquisizione della risorsa: ottenuto statusCode " + responseCode);
		}
	}
	
	/**
	 * Impostazione degli header di default
	 * @param conn la connessione
	 */
	private void setDefaultHeaders(HttpURLConnection conn) {
		conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		conn.setRequestProperty("Cache-Control", "no-cache");
		conn.setRequestProperty("Connection", "keep-alive");
		conn.setRequestProperty("Content-Language", "en-US");
		conn.setRequestProperty("Host", FARO_HOST);
		conn.setRequestProperty("Pragma", "no-cache");
		conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:54.0) Gecko/20100101 Firefox/54.0");
		
		conn.setDoOutput(true);
		conn.setUseCaches(false);
	}
	
	/**
	 * Ottiene la response come stringa
	 * @param is lo stream della response
	 * @return la response come stringa
	 * @throws IOException
	 */
	private String getResponseString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		String line;
		BufferedReader br = null;
		InputStreamReader isr = null;
		
		try {
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			while((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch(IOException ioe) {
			throw new IllegalStateException("Impossibile leggere la response del servizio");
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch(IOException ioe2){}
			}
			if(isr != null) {
				try {
					isr.close();
				} catch(IOException ioe2){}
			}
		}
		return sb.toString();
	}

}

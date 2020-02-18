/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentospesa;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.appjwebsrv.business.EsitoServizio;
import it.csi.appjwebsrv.business.Evasioni;
import it.csi.appjwebsrv.business.WSInterface;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

public class VerificaEvasioniTest extends BaseJunit4TestCase {
	
	@Autowired
	private WSInterface wWSInterface;
	
	@Test
	public void  checkOrdiniEvasi(){
		String methodName = "checkOrdiniEvasi";
		
		Evasioni evasioni = new Evasioni();
		evasioni.setAnnoDocumento("2015");
		evasioni.setCodiceFornitore(12);
		evasioni.setCodiceUtente("323");
		evasioni.setNumeroDocumento("11");
		evasioni.setTipoDocumento("DS");
		evasioni.getImpegni(); //.add();
		evasioni.getOrdini(); //adAll()
		
		long currentTimeMillis = System.currentTimeMillis();
		EsitoServizio esitoServizio = wWSInterface.verificaEvasione(evasioni);
		long elapsed = System.currentTimeMillis()-currentTimeMillis;
		log.info(methodName, "Servizio verificaEvasioni: elapsed time: "+elapsed +" ms. esitoServizio:" +  esitoServizio.getEsito());
		
		
		
	}
	
	public static void main(String[] args) {
		try {
			printClasspath();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void printClasspath() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		
		
		ClassLoader c = Thread.currentThread().getContextClassLoader();
		printClassLoader(c);
		
		System.out.println("################################");
		
		ClassLoader cl = ClassLoader.getSystemClassLoader();
        printClassLoader(cl);
        
	}

	private static void printClassLoader(ClassLoader cl) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
         System.out.println(url.getFile());
        }
        
        Field f = ClassLoader.class.getDeclaredField("classes");
		f.setAccessible(true);
        
        @SuppressWarnings("unchecked")
		Vector<Class<?>> classes =  (Vector<Class<?>>) f.get(cl);
        
        for (Class<?> c : classes) {
			System.out.println(c.getName());
		}
	}
	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.excel.base;

import java.io.IOException;

import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * Handler di base per la creazione di un Report.
 * 
 * @author Domenico Lisi
 *
 */
public abstract class BaseByteArrayHandler {
	
	protected LogUtil log = new LogUtil(this.getClass());
		
	protected byte[] bytes;

	/**
	 * Elaborazione del report.
	 * 
	 */
	public void elaborate() {
		final String methodName = "elaborate";
		
		try {
			
			//Elabora i dati dei bytes
			elaborateDataBase();
			
			// Pre-generazione dei bytes
			preGeneraBytesBase();
			
			//Invoca il servizio di generazione dei bytes
			generaBytesBase();

//		} catch(BytesElaborationException bee) {
//			throw bee
		} catch (Exception e) {
			String msg = "Errore durante l'elaborazione del report ";
			log.error(methodName, msg, e);
			throw new RuntimeException(msg, e);
		} finally {
			try {
				elaborationEnd();
			} catch (Exception e){
				log.error(methodName, "Errore nella gestione del termine dell'elaborazione. Metodo elaborationEnd del BytesHandler.", e);
			}
		}
	}

	/**
	 * Richiama l'implementazione di elaborateData
	 */
	protected void elaborateDataBase() {
		final String methodName = "elaborateDataBase";
		log.info(methodName, "Inizio elaborazione dati del report.");
		try {
			elaborateData();
		} catch (Exception e) {
			String msg = "Errore durante l'elaborazione dei dati del report.";
			log.error(methodName, msg, e);
			throw new RuntimeException(msg, e);
		}
		log.info(methodName, "Fine elaborazione dati del report.");
	}
	
	/**
	 * Pre-generazione del report
	 */
	protected void preGeneraBytesBase() {
		final String methodName = "preGeneraBytesBase";
		log.info(methodName, "Inizio pre-elaborazione dei bytes.");
		try {
			preGeneraBytes();
		} catch (Exception e) {
			String msg = "Errore durante la pre-generazione del report.";
			log.error(methodName, msg, e);
			throw new RuntimeException(msg, e);
		}
		log.info(methodName, "Fine pre-elaborazione dei bytes.");
	}
	
	protected void generaBytesBase() {
		final String methodName = "generaBytesBase";
		log.info(methodName, "Inizio generazione deui bytes.");
		try {
			bytes = generaBytes();
		} catch (IOException e) {
			String msg = "Errore durante la generazione dei bytes";
			log.error(methodName, msg, e);
			throw new RuntimeException(msg, e);
		}
		log.debug(methodName, "Fine generazione deui bytes.");
	}
	
	/**
	 * Pre-generazione del report.
	 * <br/>
	 * Fornisce un aggancio alle funzionalit&agrave; che devono richiamare servizi vari prima
	 * della creazione del report
	 */
	protected abstract void preGeneraBytes();


	/**
	 * Elabora i dati necessari a costruire il Bytes
	 */	
	protected abstract void elaborateData();
	
	
	protected abstract void elaborationEnd();
	

	protected abstract byte[] generaBytes() throws IOException;

}

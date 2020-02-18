/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.base;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccommon.util.JAXBUtility;

/**
 * Specializza BaseReportHandler per l'utilizzo di JAXB come motore di generazione dell'xml.
 *
 * @author Domenico Lisi
 * @param <T> the generic type of result
 */
public abstract class JAXBBaseReportHandler<T> extends BaseReportHandler {
	
	protected T result;
	
	@Override
	protected String getReportXml() {
		final String methodName = "getReport";

		try {
			String xml = JAXBUtility.marshall(result);
			log.debug(methodName, "xml: " + xml);
			return xml;
		} catch (Exception e) {
			String msg = "Errore durante il marshall dell'oggetto result.";
			log.error(methodName, msg, e);
			throw new ReportElaborationException(msg, e);
		}

	}
	
	@Override
	protected void elaborateDataBase() {
		final String methodName = "elaborateDataBase";
		
		result = instantiateNewResult();
		super.elaborateDataBase();
		if(result==null) {
			String msg = "Errore durante l'elaborazione dei dati del report. Nessun result ottenuto.";
			log.error(methodName, msg);
			throw new ReportElaborationException(msg);
		}
	}
	
	
	/**
	 * Instanzia l'oggetto result vuoto.
	 * Questo metodo prevede che l'oggetto result abbia il costruttore vuoto.
	 * Per esigenze più complesse &egrave; necessario fare override.
	 * 
	 * @return
	 */
	protected T instantiateNewResult() {
		return Utility.instantiateGenericType(this.getClass(), JAXBBaseReportHandler.class, 0);
	}

}

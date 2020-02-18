/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.base;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dad.FileDad;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.frontend.webservice.ReportService;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReport;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.StatoFile.CodiceStatoFile;
import it.csi.siac.siaccorser.model.file.TipoFile;

/**
 * Handler di base per la creazione di un Report.
 * 
 * @author Domenico Lisi
 *
 */
public abstract class BaseReportHandler {
	
	protected LogUtil log = new LogUtil(this.getClass());
	protected Ente ente;
	protected Richiedente richiedente;
	
	@Autowired
	private ReportService reportService;
	@Autowired
	protected ServiceExecutor serviceExecutor;
	@Autowired
	private FileDad fileDad;
	
	protected GeneraReportResponse generaReportResponse;

	// SIAC-6110
	protected boolean cleanReportContent = false;
	// SIAC-6232
	protected boolean prepersistReportXml = false;


	/**
	 * Elaborazione del report.
	 * 
	 */
	public void elaborate() {
		final String methodName = "elaborate";
		
		serviceExecutor.setServiceName(this.getClass().getSimpleName());

		try {
			
			//Elabora i dati del report
			elaborateDataBase();
			
			// Pre-generazione del report
			preGeneraReportBase();
			
			//Invoca il servizio di generazione del report
			generaReportBase();

			//Gestisce la respose del servizio di generazione del report
			handleResponseBase();			

		} catch (Exception e) {
			
			gestisciErroreElaborazione(e);
		} finally {
			try {
				elaborationEnd();
			} catch (Exception e){
				log.error(methodName, "Errore nella gestione del termine dell'elaborazione. Metodo elaborationEnd del ReportHandler.", e);
			}
		}
	}

	protected void gestisciErroreElaborazione(Exception e) {
		final String methodName ="gestisciErroreElaborazione";
		//			if(e instanceof ReportElaborationException){
		//				throw (ReportElaborationException)e;
		//			}
		String msg = "Errore durante l'elaborazione del report ";
		log.error(methodName, msg, e);
		throw new ReportElaborationException(msg, e);
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
			throw new ReportElaborationException(msg, e);
		}
		log.info(methodName, "Fine elaborazione dati del report.");
	}
	

	/**
	 * Elabora i dati necessari a costruire il Report
	 */	
	protected abstract void elaborateData();
	
	/**
	 * Ottiene i dati del report in formato xml
	 * 
	 * @return xml contenente i dati il report
	 */
	protected abstract String getReportXml();
	
	/**
	 * Ottiene il codice del template
	 * 
	 * @return codice del template
	 */
	public abstract String getCodiceTemplate();
	
	
	/**
	 * Pre-generazione del report
	 */
	protected void preGeneraReportBase() {
		final String methodName = "preGeneraReportBase";
		try {
			preGeneraReport();
		} catch (Exception e) {
			String msg = "Errore durante la pre-generazione del report.";
			log.error(methodName, msg, e);
			throw new ReportElaborationException(msg, e);
		}
	}
	
	/**
	 * Pre-generazione del report.
	 * <br/>
	 * Fornisce un aggancio alle funzionalit&agrave; che devono richiamare servizi vari prima
	 * della creazione del report
	 */
	protected void preGeneraReport() {
		// Da implementare eventualmente nelle sottoclassi
	}


	protected void generaReportBase() {
		final String methodName = "generaReportBase";
		log.info(methodName, "Inizio generazione report... ");

		String codiceTemplate = getCodiceTemplate();
		String reportXml = getReportXml();
		Integer xmlFileUid = null;
		
		if(prepersistReportXml) {
			xmlFileUid = persistXml(codiceTemplate, reportXml);
		}
		
		generaReportResponse = generaReport(codiceTemplate, reportXml, xmlFileUid);

		log.debug(methodName, "Fine generazione report.");
	}

	/**
	 * Persistenza dell'XML e restituzione dell'uid dello stesso
	 * @param codiceTemplate il codice del template
	 * @param reportXml il report da persistere
	 * @return l'uid
	 */
	protected Integer persistXml(String codiceTemplate, String reportXml) {
		fileDad.setEnte(ente);
		fileDad.setLoginOperazione(richiedente.getOperatore().getCodiceFiscale());
		
		TipoFile tipoFile = fileDad.ricercaTipoFileByCodice("REPORT_XML");
		
		File file = new File();
		file.setCodice(String.format("XML-%s", codiceTemplate));
		file.setNome(String.format("%s.xml", codiceTemplate));
		file.setMimeType("text/xml");
		file.setContenuto(reportXml);
		file.setStatoFile(CodiceStatoFile.CARICATO);
		file.setTipo(tipoFile);
		
		fileDad.inserisciFile(file);
		
		return Integer.valueOf(file.getUid());
	}


	/**
	 * Invoca il servizio di generazione del report.
	 * 
	 * @return
	 */
	protected GeneraReportResponse generaReport(String codiceTemplate, String objectXml, Integer xmlFileUid) {
		final String methodName = "generaReport";
			
		
		GeneraReport req = new GeneraReport();
		req.setRichiedente(richiedente);
		req.setEnte(ente);
		
		req.setCodiceTemplate(codiceTemplate);
		if(prepersistReportXml) {
			req.setXmlFileUid(xmlFileUid);
		} else {
			req.setObjectXml(objectXml);
		}
		
		req.setCleanReportContent(cleanReportContent);
		
		GeneraReportResponse res = null;
		try {
			res = reportService.generaReport(req);
			return res;
		} catch (Throwable e) {			
			String msg = "Errore durante la generazione del report effettuata dal servizio GeneraReport.";
			log.error(methodName, msg, e);
			throw new ReportElaborationException(msg, e);
		} finally {
			log.logXmlTypeObject(res, "GeneraReportResponse");
		}
	}
	
	/**
	 * Richiama l'implementazione di handleResponse
	 * 
	 * @param res
	 */
	protected void handleResponseBase() {
		final String methodName = "handleResponseBase";
		log.debug(methodName, "Inizio gestione risposta... ");
		
		if(generaReportResponse==null){
			String msg = "Risposta del servizo generaReportResponse null.";
			log.error(methodName, msg);
			throw new ReportElaborationException(msg);
		}
		
		if(generaReportResponse.isFallimento()){
			String msg = "Esito del servizo generaReportResponse FALLIMENTO.";
			log.error(methodName, msg);
			throw new ReportElaborationException(msg);
		}
		
		try{
			handleResponse(generaReportResponse);
		} catch (Exception e) {
			String msg = "Errore durante la gestione dei dati di ritorno del report.";
			log.error(methodName, msg, e);
			throw new ReportElaborationException(msg, e);
		}
		log.debug(methodName, "Fine gestione risposta.");
	}

	
	
	/**
	 * Gestisce la risposta di creazione del report.
	 * Viene invocato quando il report è stato creato.
	 *  
	 * @param res rsponse del servizio di creazione del report.
	 */
	protected abstract void handleResponse(GeneraReportResponse res);


	/**
	 * @return the ente
	 */
	public Ente getEnte() {
		return ente;
	}


	/**
	 * @param ente the ente to set
	 */
	public void setEnte(Ente ente) {
		this.ente = ente;
	}


	/**
	 * @return the richiedente
	 */
	public Richiedente getRichiedente() {
		return richiedente;
	}


	/**
	 * @param richiedente the richiedente to set
	 */
	public void setRichiedente(Richiedente richiedente) {
		this.richiedente = richiedente;
	}



	/**
	 * @return the generaReportResponse
	 */
	public GeneraReportResponse getGeneraReportResponse() {
		return generaReportResponse;
	}	
	
	
	/**
	 * Richiamato sempre una volta terminata l'elaborazione.
	 */
	protected void elaborationEnd() {
		//implementazione di default vuota.
	}
	
	
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.base;

import it.csi.siac.siaccorser.frontend.webservice.msg.report.ReportServiceRequest;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.ReportServiceResponse;



/**
 * Classe base dei servizi che generano dei report.
 * Delega la creazione del report al ReportHandler specificato.
 * Specializza la gestione sincrona della generazione del report: la response viene restuita al termine dell'elaborazione.
 * 
 *
 * @author Domenico Lisi
 * 
 * @param <REQ> the ReportServiceRequest generic type
 * @param <RES> the ReportServiceResponse generic type
 * @param <RH> the BaseReportHandler generic type
 */
public abstract class 
	SyncReportBaseService<REQ extends ReportServiceRequest,RES extends ReportServiceResponse,RH extends BaseReportHandler> 
	extends ReportBaseService<REQ, RES, RH> {
	
	protected final void startElaboration() {
		String methodName = "startElaboration";
		try{
			log.info(methodName,"inizio elaborazione...");
			reportHandler.elaborate();
			log.info(methodName,"elaborazione terminata con successo.");
			postElaborationSuccess();
		} catch (ReportElaborationException ree) {
			log.info(methodName,"elaborazione terminata con errori.");
			elaborationError(ree);
		}
		
	}
	
	
	protected void elaborationError(ReportElaborationException e) {
		postElaborationError(e);
		throw e;
	}
	
	protected abstract void postElaborationSuccess();
	
	protected void postElaborationError(ReportElaborationException e) {
		final String methodName = "postElaborationError";
		log.info(methodName, "post start elaborazione avvenuta con errori: " + e.getMessage());
		throw e;
	}
	
}

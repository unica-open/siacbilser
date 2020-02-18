/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.base;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.ReportServiceRequest;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ReportServiceResponse;



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
public abstract class SyncReportBaseService<REQ extends ReportServiceRequest,RES extends ReportServiceResponse,RH extends BaseReportHandler> extends ReportBaseService<REQ, RES, RH> {
	
	
	/**
	 * Invoca l'elaborazione del reportHandler in modo sincrono.
	 */	
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
	
	/**
	 * Viene invocato se l'elaborazione è terminata con successo.
	 */
	protected abstract void postElaborationSuccess();
	
	/**
	 * Viene invocato se l'elaborazione è terminata con errori.
	 */
	protected abstract void postElaborationError(ReportElaborationException e);
	
}

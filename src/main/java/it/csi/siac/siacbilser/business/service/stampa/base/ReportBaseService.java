/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.base;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ReportServiceRequest;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ReportServiceResponse;



/**
 * Classe base dei servizi che generano dei report.
 * Delega la creazione del report al ReportHandler specificato.
 * 
 *
 * @author Domenico Lisi
 * 
 * @param <REQ> the ReportServiceRequest generic type
 * @param <RES> the ReportServiceResponse generic type
 * @param <RH> the BaseReportHandler generic type
 */
public abstract class ReportBaseService<REQ extends ReportServiceRequest,RES extends ReportServiceResponse,RH extends BaseReportHandler> extends ExtendedBaseService<REQ, RES> {

	@Autowired
	protected ApplicationContext appCtx;
	
	protected RH reportHandler;
	
		
	@Override
	protected final void execute() {
		preStartElaboration();	
		initReportHandlerBase();
		startElaboration();	
	}	
	

	


	/**
	 * Inizializza la logica di controllo necessaria per inizializzare l'elaborazione.
	 * 
	 */
	protected abstract void preStartElaboration();
		
	/**
	 * Invoca l'elaborazione del reportHandler
	 */	
	protected abstract void startElaboration();
	

	/**
	 * Estende il check del Richiedente aggiungendo il check dell'Ente.
	 */
	@Override
	protected void checkRichiedente() throws ServiceParamError {
		super.checkRichiedente();
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
	}
	
	/**
	 * Metodo di base per inizializzare il reportHandler.
	 */
	protected void initReportHandlerBase() {
		final String methodName = "initReportHandlerBase";
		log.debug(methodName, "invoked");
		Class<RH> reportHandlerClass = getReportHandlerClass();
		try{
			reportHandler = appCtx.getBean(Utility.toDefaultBeanName(reportHandlerClass), reportHandlerClass);
		} catch(BeansException be) {
			log.error(methodName, "Impossibile ottenere il reportHandler! Errore nell'ottenimento del component:  "+reportHandlerClass.getName() + " :"+be.getMessage(),be);
			throw be;
		}
		if(reportHandler==null){
			log.error(methodName, "Impossibile ottenere il reportHandler! Nessun component tovato per "+reportHandlerClass.getName());
		}
		reportHandler.setEnte(req.getEnte());
		reportHandler.setRichiedente(req.getRichiedente());
		initReportHandler();
	}
	
	/**
	 * Inizializza i parametri di input del reportHandler.
	 */
	protected abstract void initReportHandler();




	/**
	 * Specifica la classe finale del reportHandler.
	 * Di default viene istanziata quella espressa nel GenericType, ma sovrascrivendo il metodo &egrave; 
	 * possibile specificare una classe.
	 * 
	 * @return
	 */
	protected Class<RH> getReportHandlerClass() {
		return Utility.findGenericType(this.getClass(), ReportBaseService.class, 2);
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;

import org.springframework.context.ApplicationContext;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.BaseService;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Esecutore centralizzato delle classi che estendono BaseService.
 * Individua ed applica il modo standard per eseguire un qualunque servizio che estende BaseService.
 * 
 * @author Domenico Lisi
 * @see BaseService
 */
public class BaseServiceExecutor {
	
	private static LogUtil log = new LogUtil(BaseServiceExecutor.class);
	
	/** Prevent instantiation */
	private BaseServiceExecutor() {
		// Prevent instantiation
	}
	
	
	public static <SREQ extends ServiceRequest,SRES extends ServiceResponse, SER extends ExtendedBaseService<SREQ, SRES>> SRES execute(ApplicationContext appCtx, Class<SER> serviceClass, SREQ serviceRequest) {
		SRES serviceResponse;
		String serviceBeanName; 
		
		try {
			serviceBeanName = Utility.toDefaultBeanName(serviceClass);
		} catch (Throwable t) {
			serviceResponse = instantiateNewRes(serviceClass);
			serviceResponse.addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile ottenere il nome di default per il bean del Servizio " + serviceClass.getSimpleName()));
			serviceResponse.setEsito(Esito.FALLIMENTO);
			serviceResponse.addErroreDiSistema(t);
			return serviceResponse;
		}
		
		serviceResponse = execute(appCtx, serviceBeanName, serviceClass, serviceRequest);
		
		return serviceResponse;
	}
	
	

	public static <SREQ extends ServiceRequest,SRES extends ServiceResponse, SER extends ExtendedBaseService<SREQ, SRES>> SRES execute(ApplicationContext appCtx, String serviceBeanName, Class<SER> serviceClass, SREQ serviceRequest) {
		SRES serviceResponse;
		SER service;
		try {
			service = appCtx.getBean(serviceBeanName, serviceClass);  //Utility.getBeanViaDefaultName(appCtx, serviceClass);
		} catch (Throwable t){
			serviceResponse = instantiateNewRes(serviceClass);
			serviceResponse.addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile ottenere il bean del Servizio " + serviceClass.getSimpleName()));
			serviceResponse.addErroreDiSistema(t);
			serviceResponse.setEsito(Esito.FALLIMENTO);
			return serviceResponse;
		}
		
		serviceResponse = execute(serviceClass, service, serviceRequest);
			
		
		return serviceResponse;
	}
	
	
	
	public static <SREQ extends ServiceRequest,SRES extends ServiceResponse, SER extends ExtendedBaseService<SREQ, SRES>> SRES execute(Class<SER> serviceClass, SER service, SREQ serviceRequest) {
		final String methodName = "execute";
		SRES serviceResponse;
		try {
			serviceResponse = service.executeService(serviceRequest);
		} catch (Throwable t){
			log.error(methodName, "Errore di sistema sollevato da executeService. Servizio: "+ serviceClass.getSimpleName(),t);
			serviceResponse = instantiateNewRes(serviceClass);
			serviceResponse.addErroreDiSistema(t);
			serviceResponse.setEsito(Esito.FALLIMENTO);
			service.setServiceResponse(serviceResponse);
			if(t instanceof RuntimeException){
				service.handleRuntimeException((RuntimeException) t);
			} else {
				service.handleThrowable(t);
			}
		}
		log.debug(methodName, "Servizio "+serviceClass.getSimpleName()+" terminato con esito: " + serviceResponse.getEsito() + ". " + serviceResponse.getDescrizioneErrori());
		return serviceResponse;
	}
	
	
	private static <SREQ extends ServiceRequest,SRES extends ServiceResponse, SER extends BaseService<SREQ, SRES>> SRES instantiateNewRes(Class<SER> serviceClass) {		
		return Utility.instantiateGenericType(serviceClass, BaseService.class, 1);
	}

}

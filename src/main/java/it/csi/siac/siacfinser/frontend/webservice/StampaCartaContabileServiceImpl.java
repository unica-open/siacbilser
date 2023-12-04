/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacfinser.business.service.stampa.cartacontabile.StampaRiepilogoCartaContabileService;
import it.csi.siac.siacfinser.frontend.webservice.msg.StampaRiepilogoCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.StampaRiepilogoCartaContabileResponse;


@WebService(serviceName = "StampaCartaContabileService", 
portName = "StampaCartaContabileServicePort", 
targetNamespace = FINSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.StampaCartaContabileService")
public class StampaCartaContabileServiceImpl extends AbstractService implements StampaCartaContabileService {
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	

	
	@Override
	@WebMethod
	public  @WebResult StampaRiepilogoCartaContabileResponse stampaRiepilogoCartaContabile(@WebParam  StampaRiepilogoCartaContabile request) {
		return new ServiceExecutor<StampaRiepilogoCartaContabileResponse, StampaRiepilogoCartaContabile>("stampaRiepilogoCartaContabile") {
			@Override
			StampaRiepilogoCartaContabileResponse executeService(StampaRiepilogoCartaContabile request) {
				return appCtx.getBean(StampaRiepilogoCartaContabileService.class).executeService(request);
			}
		}.execute(request);
	}
	
}

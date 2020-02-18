/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.pagopa.frontend.webservice.PagoPAService;
import it.csi.siac.pagopa.frontend.webservice.PagoPASvcDictionary;
import it.csi.siac.pagopa.frontend.webservice.msg.ElaboraFlussoRiconciliazione;
import it.csi.siac.pagopa.frontend.webservice.msg.ElaboraFlussoRiconciliazioneResponse;
import it.csi.siac.siacbilser.business.service.pagopa.ElaboraFlussoRiconciliazioneService;
import it.csi.siac.siacbilser.business.utility.Utility;

@WebService(serviceName = "PagoPAService", 
			portName = "PagoPAServicePort", 
			targetNamespace = PagoPASvcDictionary.NAMESPACE,
			endpointInterface = "it.csi.siac.pagopa.frontend.webservice.PagoPAService")

public class PagoPAServiceImpl implements PagoPAService {
    
	@Autowired
	private ApplicationContext appCtx;
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public ElaboraFlussoRiconciliazioneResponse elaboraFlussoRiconciliazione(ElaboraFlussoRiconciliazione req) {
		return Utility.getBeanViaDefaultName(appCtx, ElaboraFlussoRiconciliazioneService.class).executeService(req);
	}
}

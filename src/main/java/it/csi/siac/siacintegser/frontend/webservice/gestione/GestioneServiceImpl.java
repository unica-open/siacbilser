/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.frontend.webservice.gestione;
  

import javax.annotation.PostConstruct;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.custom.oopp.business.service.gestione.AggiornaProgettoOOPPService;
import it.csi.siac.custom.oopp.business.service.gestione.InserisciProgettoOOPPService;
import it.csi.siac.siacintegser.business.service.test.TestService;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.gestione.AggiornaProgettoOOPP;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.gestione.AggiornaProgettoOOPPResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.gestione.InserisciProgettoOOPP;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.gestione.InserisciProgettoOOPPResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.test.Test;
import it.csi.siac.siacintegser.frontend.webservice.msg.test.TestResponse;

@WebService(serviceName = "GestioneService", 
portName = "GestioneServicePort", 
targetNamespace = GestioneSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacintegser.frontend.webservice.gestione.GestioneService")
public class GestioneServiceImpl implements GestioneService {

	@Autowired
	private ApplicationContext appCtx;

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public TestResponse test(@WebParam Test request) {
		return appCtx.getBean(TestService.class).executeService(request);
	}

	@Override
	public InserisciProgettoOOPPResponse inserisciProgettoOOPP(InserisciProgettoOOPP request) {
		return appCtx.getBean(InserisciProgettoOOPPService.class).executeService(request);
	}

	@Override
	public AggiornaProgettoOOPPResponse aggiornaProgettoOOPP(AggiornaProgettoOOPP request) {
		return appCtx.getBean(AggiornaProgettoOOPPService.class).executeService(request);
	}
}

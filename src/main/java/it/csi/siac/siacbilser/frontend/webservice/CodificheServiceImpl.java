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

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.common.RicercaCodificaService;
import it.csi.siac.siacbilser.business.service.common.RicercaCodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifica;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;

/**
 * Implementazione del servizio CodificheService, i metodi esposti sono
 * propri del modulo BIL.
 *
 * @author Domenico
 */
@WebService(serviceName = "CodificheService", portName = "CodificheServicePort", targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.CodificheService")
public class CodificheServiceImpl implements CodificheService {

	/** The app ctx. */
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
	public RicercaCodificheResponse ricercaCodifiche(RicercaCodifiche request) {
		return BaseServiceExecutor.execute(appCtx, RicercaCodificheService.class, request);
	}

	@Override
	public RicercaCodificaResponse ricercaCodifica(RicercaCodifica request) {
		return BaseServiceExecutor.execute(appCtx, RicercaCodificaService.class, request);
	}
	
}

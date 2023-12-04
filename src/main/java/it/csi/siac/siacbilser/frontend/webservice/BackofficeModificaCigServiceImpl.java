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

import it.csi.siac.siacbilser.business.service.backoffice.ModificaCigBackofficeService;
import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.msg.ModificaCigBackoffice;
import it.csi.siac.siacbilser.frontend.webservice.msg.ModificaCigBackofficeResponse;

@WebService(serviceName = "BackofficeModificaCigService", portName = "BackofficeModificaCigServicePort", targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.BackofficeModificaCigService")
public class BackofficeModificaCigServiceImpl implements BackofficeModificaCigService {

	/** The app ctx. */
	@Autowired
	private ApplicationContext appCtx;

	/**
	 * Inits the ctx.
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public ModificaCigBackofficeResponse modificaCigBackoffice(ModificaCigBackoffice request) {
		return BaseServiceExecutor.execute(appCtx, ModificaCigBackofficeService.class, request);
	}

}

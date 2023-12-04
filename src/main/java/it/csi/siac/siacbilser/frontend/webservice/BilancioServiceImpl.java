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
import it.csi.siac.siacbilser.business.service.bilancio.AggiornaAttributiBilancioService;
import it.csi.siac.siacbilser.business.service.bilancio.RicercaAttributiBilancioService;
import it.csi.siac.siacbilser.business.service.bilancio.RicercaDettaglioBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAttributiBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;

/**
 * Implementazione del servizio BilancioService.
 *
 * @author Alessandro Marchino
 * @version 1.0.0 - 09/01/2013
 */
@WebService(serviceName = "BilancioService",
portName = "BilancioServicePort",
targetNamespace = BILSvcDictionary.NAMESPACE,
endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.BilancioService")
public class BilancioServiceImpl implements BilancioService {
	
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
	public RicercaDettaglioBilancioResponse ricercaDettaglioBilancio(RicercaDettaglioBilancio parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioBilancioService.class, parameters);
	}

	@Override
	public RicercaAttributiBilancioResponse ricercaAttributiBilancio(RicercaAttributiBilancio parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaAttributiBilancioService.class, parameters);
	}

	@Override
	public AggiornaAttributiBilancioResponse aggiornaAttributiBilancio(AggiornaAttributiBilancio parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaAttributiBilancioService.class, parameters);
	}

}

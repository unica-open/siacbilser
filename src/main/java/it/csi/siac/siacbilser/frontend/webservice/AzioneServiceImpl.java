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

import it.csi.siac.siacbilser.business.service.azione.RicercaAzionePerChiaveService;
import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAzionePerChiave;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaAzionePerChiaveResponse;

/**
 * Implementazione del servizio AzioneService.
 *
 * @author Alessandro Marchino
 */
@WebService(serviceName = "AzioneService",
portName = "AzioneServicePort",
targetNamespace = BILSvcDictionary.NAMESPACE,
endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.AzioneService")
public class AzioneServiceImpl implements AzioneService {
	
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
	public RicercaAzionePerChiaveResponse ricercaAzionePerChiave(RicercaAzionePerChiave parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaAzionePerChiaveService.class, parameters);
	}

}

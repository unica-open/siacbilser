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
import it.csi.siac.siacbilser.business.service.common.EsisteElaborazioneAttivaService;
import it.csi.siac.siacbilser.frontend.webservice.msg.EsisteElaborazioneAttiva;
import it.csi.siac.siacbilser.frontend.webservice.msg.EsisteElaborazioneAttivaResponse;

/**
 *
 * @author AR
 */
@WebService(serviceName = "ElaborazioniService",
portName = "ElaborazioniServicePort",
targetNamespace = BILSvcDictionary.NAMESPACE,
endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.ElaborazioniService")
public class ElaborazioniServiceImpl implements ElaborazioniService {

	@Autowired
	private ApplicationContext appCtx;
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public EsisteElaborazioneAttivaResponse esisteElaborazioneAttiva(EsisteElaborazioneAttiva request) {
		return BaseServiceExecutor.execute(appCtx, EsisteElaborazioneAttivaService.class, request);
	}
}

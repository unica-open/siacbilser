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
import it.csi.siac.siacbilser.business.service.elencodocumentiallegato.RicercaTotaliPredocumentiPerElencoService;
import it.csi.siac.siacfin2ser.frontend.webservice.ElencoDocumentiAllegatoService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPredocumentiPerElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPredocumentiPerElencoResponse;

@WebService(serviceName = "ElencoDocumentiAllegatoService", portName = "ElencoDocumentiAllegatoServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.ElencoDocumentiAllegatoService")

public class ElencoDocumentiAllegatoServiceImpl implements ElencoDocumentiAllegatoService {

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
	public RicercaTotaliPredocumentiPerElencoResponse ricercaTotaliPredocumentiPerElenco(RicercaTotaliPredocumentiPerElenco parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTotaliPredocumentiPerElencoService.class, parameters);
	}
}

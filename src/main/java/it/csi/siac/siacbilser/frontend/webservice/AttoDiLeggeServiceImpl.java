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

import it.csi.siac.siacattser.frontend.webservice.ATTSvcDictionary;
import it.csi.siac.siacattser.frontend.webservice.AttoDiLeggeService;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.CancellaAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaAttoDiLeggeResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLegge;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaTipiAttoDiLeggeResponse;
import it.csi.siac.siacbilser.business.service.attodilegge.AggiornaAttoDiLeggeService;
import it.csi.siac.siacbilser.business.service.attodilegge.CancellaAttoDiLeggeService;
import it.csi.siac.siacbilser.business.service.attodilegge.InserisceAttoDiLeggeService;
import it.csi.siac.siacbilser.business.service.attodilegge.RicercaAttoDiLeggeService;
import it.csi.siac.siacbilser.business.service.attodilegge.RicercaPuntualeAttoDiLeggeService;
import it.csi.siac.siacbilser.business.service.attodilegge.RicercaTipiAttoDiLeggeService;
import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;

/**
 *  Servizi sugli atti di legge.
 */
@WebService(serviceName = "AttoDiLeggeService", portName = "AttoDiLeggeServicePort", 
targetNamespace = ATTSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacattser.frontend.webservice.AttoDiLeggeService")
public class AttoDiLeggeServiceImpl implements AttoDiLeggeService {
	

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
	public InserisceAttoDiLeggeResponse inserisceAttoDiLegge(InserisceAttoDiLegge parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceAttoDiLeggeService.class, parameters);
	}

	@Override
	public AggiornaAttoDiLeggeResponse aggiornaAttoDiLegge(AggiornaAttoDiLegge parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaAttoDiLeggeService.class, parameters);
	}

	@Override
	public RicercaAttoDiLeggeResponse ricercaAttoDiLegge(RicercaAttoDiLegge parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaAttoDiLeggeService.class, parameters);
	}

	@Override
	public CancellaAttoDiLeggeResponse cancellaAttoDiLegge(CancellaAttoDiLegge parameters) {
		return BaseServiceExecutor.execute(appCtx, CancellaAttoDiLeggeService.class, parameters);
	}

	@Override
	public RicercaTipiAttoDiLeggeResponse getTipiAttoLegge(RicercaTipiAttoDiLegge parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipiAttoDiLeggeService.class, parameters);
	}

	@Override
	public RicercaAttoDiLeggeResponse ricercaPuntualeAttoDiLegge(RicercaAttoDiLegge parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeAttoDiLeggeService.class, parameters);
	}

}

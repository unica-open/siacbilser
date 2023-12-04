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
import it.csi.siac.siacbilser.business.service.quadroeconomico.AggiornaQuadroEconomicoService;
import it.csi.siac.siacbilser.business.service.quadroeconomico.AnnullaQuadroEconomicoService;
import it.csi.siac.siacbilser.business.service.quadroeconomico.InserisceQuadroEconomicoService;
import it.csi.siac.siacbilser.business.service.quadroeconomico.RicercaQuadroEconomicoValidoService;
import it.csi.siac.siacbilser.business.service.quadroeconomico.RicercaSinteticaQuadroEconomicoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AggiornaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AggiornaQuadroEconomicoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AnnullaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.AnnullaQuadroEconomicoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.InserisceQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.InserisceQuadroEconomicoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaQuadroEconomicoValido;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaQuadroEconomicoValidoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomicoResponse;


@WebService(serviceName = "QuadroEconomicoService", portName = "QuadroEconomicoServicePort", 
targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.QuadroEconomicoService")

public class QuadroEconomicoServiceImpl implements QuadroEconomicoService {

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
	public InserisceQuadroEconomicoResponse inserisceQuadroEconomico(InserisceQuadroEconomico parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceQuadroEconomicoService.class, parameters);
	}

	@Override
	public AggiornaQuadroEconomicoResponse aggiornaQuadroEconomico(AggiornaQuadroEconomico parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaQuadroEconomicoService.class, parameters);
	}

	@Override
	public AnnullaQuadroEconomicoResponse annullaQuadroEconomico(AnnullaQuadroEconomico parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaQuadroEconomicoService.class, parameters);
	}

	@Override
	public RicercaSinteticaQuadroEconomicoResponse ricercaSinteticaQuadroEconomico(RicercaSinteticaQuadroEconomico parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaQuadroEconomicoService.class, parameters);
	}

	@Override
	public RicercaQuadroEconomicoValidoResponse ricercaQuadroEconomicoValido(RicercaQuadroEconomicoValido parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaQuadroEconomicoValidoService.class, parameters);
	}
}

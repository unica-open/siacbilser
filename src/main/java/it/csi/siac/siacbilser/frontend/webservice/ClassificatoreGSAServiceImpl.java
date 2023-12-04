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
import it.csi.siac.siacbilser.business.service.classifgsa.AggiornaClassificatoreGSAService;
import it.csi.siac.siacbilser.business.service.classifgsa.AnnullaClassificatoreGSAService;
import it.csi.siac.siacbilser.business.service.classifgsa.InserisceClassificatoreGSAService;
import it.csi.siac.siacbilser.business.service.classifgsa.RicercaClassificatoreGSAValidoService;
import it.csi.siac.siacbilser.business.service.classifgsa.RicercaSinteticaClassificatoreGSAService;
import it.csi.siac.siacgenser.frontend.webservice.ClassificatoreGSAService;
import it.csi.siac.siacgenser.frontend.webservice.GENSvcDictionary;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaClassificatoreGSAResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaClassificatoreGSAResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceClassificatoreGSAResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassificatoreGSAValido;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassificatoreGSAValidoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaClassificatoreGSAResponse;

@WebService(serviceName = "ClassificatoreGSAService", portName = "ClassificatoreGSAServicePort", 
targetNamespace = GENSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacgenser.frontend.webservice.ClassificatoreGSAService")

public class ClassificatoreGSAServiceImpl implements ClassificatoreGSAService {

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
	public InserisceClassificatoreGSAResponse inserisceClassificatoreGSA(InserisceClassificatoreGSA parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceClassificatoreGSAService.class, parameters);
	}

	@Override
	public AggiornaClassificatoreGSAResponse aggiornaClassificatoreGSA(AggiornaClassificatoreGSA parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaClassificatoreGSAService.class, parameters);
	}

	@Override
	public AnnullaClassificatoreGSAResponse annullaClassificatoreGSA(AnnullaClassificatoreGSA parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaClassificatoreGSAService.class, parameters);
	}

	@Override
	public RicercaSinteticaClassificatoreGSAResponse ricercaSinteticaClassificatoreGSA(RicercaSinteticaClassificatoreGSA parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaClassificatoreGSAService.class, parameters);
	}

	@Override
	public RicercaClassificatoreGSAValidoResponse ricercaClassificatoreGSAValido(RicercaClassificatoreGSAValido parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaClassificatoreGSAValidoService.class, parameters);
	}
}

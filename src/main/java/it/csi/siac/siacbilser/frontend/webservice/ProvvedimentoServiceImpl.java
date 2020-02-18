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
import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.AggiornaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaSinteticaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaSinteticaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.VerificaAnnullabilitaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.VerificaAnnullabilitaProvvedimentoResponse;
import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.provvedimento.AggiornaProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.InserisceProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.RicercaProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.RicercaSinteticaProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.TipiProvvedimentoService;
import it.csi.siac.siacbilser.business.service.provvedimento.VerificaAnnullabilitaProvvedimentoService;

/**
 * Servizi sul Provvedimento ed atti amministrativi.
 */
@WebService(serviceName = "ProvvedimentoService", portName = "ProvvedimentoServicePort", 
targetNamespace =  ATTSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService")
public class ProvvedimentoServiceImpl implements ProvvedimentoService {
	

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
	public InserisceProvvedimentoResponse inserisceProvvedimento(InserisceProvvedimento parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceProvvedimentoService.class, parameters);
	}

	@Override
	public AggiornaProvvedimentoResponse aggiornaProvvedimento(AggiornaProvvedimento parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaProvvedimentoService.class, parameters);
	}

	@Override
	public RicercaProvvedimentoResponse ricercaProvvedimento(RicercaProvvedimento parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaProvvedimentoService.class, parameters);
	}

	@Override
	public VerificaAnnullabilitaProvvedimentoResponse verificaAnnullabilitaProvvedimento(VerificaAnnullabilitaProvvedimento parameters) {
		return BaseServiceExecutor.execute(appCtx, VerificaAnnullabilitaProvvedimentoService.class, parameters);
	}
	
	@Override
	public TipiProvvedimentoResponse getTipiProvvedimento(TipiProvvedimento parameters) {
		return BaseServiceExecutor.execute(appCtx, TipiProvvedimentoService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaProvvedimentoResponse ricercaSinteticaProvvedimento(RicercaSinteticaProvvedimento parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaProvvedimentoService.class, parameters);
	}
}

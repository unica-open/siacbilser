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
import it.csi.siac.siacbilser.business.service.fatturaelettronica.RicercaDettaglioFatturaElettronicaService;
import it.csi.siac.siacbilser.business.service.fatturaelettronica.RicercaSinteticaFatturaElettronicaService;
import it.csi.siac.siacbilser.business.service.fatturaelettronica.SospendiFatturaElettronicaService;
import it.csi.siac.sirfelser.frontend.webservice.FELSvcDictionary;
import it.csi.siac.sirfelser.frontend.webservice.FatturaElettronicaService;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronicaResponse;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronicaResponse;
import it.csi.siac.sirfelser.frontend.webservice.msg.SospendiFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.SospendiFatturaElettronicaResponse;


/**
 * The Class FatturaElettronicaServiceImpl.
 */
@WebService(serviceName = "FatturaElettronicaService", portName = "FatturaElettronicaServicePort", 
targetNamespace = FELSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.sirfelser.frontend.webservice.FatturaElettronicaService")
public class FatturaElettronicaServiceImpl implements FatturaElettronicaService {
	
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
	public RicercaDettaglioFatturaElettronicaResponse ricercaDettaglioFatturaElettronica(RicercaDettaglioFatturaElettronica parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioFatturaElettronicaService.class, parameters);
	}

	@Override
	public RicercaSinteticaFatturaElettronicaResponse ricercaSinteticaFatturaElettronica(RicercaSinteticaFatturaElettronica parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaFatturaElettronicaService.class, parameters);
	}

	@Override
	public SospendiFatturaElettronicaResponse sospendiFatturaElettronica(SospendiFatturaElettronica parameters) {
		return BaseServiceExecutor.execute(appCtx, SospendiFatturaElettronicaService.class, parameters);
	}

}

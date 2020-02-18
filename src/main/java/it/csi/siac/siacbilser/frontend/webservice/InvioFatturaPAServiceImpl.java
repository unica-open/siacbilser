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

import it.csi.siac.sirfelser.frontend.webservice.InvioFatturaPAService;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.InvioFatturaPA;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.InvioFatturaPAResponse;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.DatiPortaleFattureType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.EmbeddedXMLType;
import it.csi.siac.sirfelser.frontend.webservice.msg.inviofatturapa.services.sirfelservice.InformazioniAggiuntiveType;

/**
 * Implementazione del servizio BilancioService.
 *
 * @author Alessandro Marchino
 * @version 1.0.0 - 09/01/2013
 */
@WebService(serviceName = "InvioFatturaPA",
targetNamespace = "http://www.csi.it/sirfel/InvioFatturaPA-1.0",
endpointInterface = "it.csi.siac.sirfelser.frontend.webservice.InvioFatturaPAService")
public class InvioFatturaPAServiceImpl implements InvioFatturaPAService {
	
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
	public InvioFatturaPAResponse.Result invioFatturaPA(DatiPortaleFattureType datiPortaleFatture, EmbeddedXMLType fatturaElettronica, String codiceEnte, InformazioniAggiuntiveType informazioniAggiuntive) {
		InvioFatturaPA parameters = new InvioFatturaPA();
		
		parameters.setDatiPortaleFatture(datiPortaleFatture);
		parameters.setFatturaElettronica(fatturaElettronica);
		parameters.setCodiceEnte(codiceEnte);
		parameters.setInformazioniAggiuntive(informazioniAggiuntive);
		
		return appCtx.getBean(it.csi.siac.siacbilser.business.service.inviofatturapa.InvioFatturaPAService.class).executeService(parameters).getResult();
	}

}

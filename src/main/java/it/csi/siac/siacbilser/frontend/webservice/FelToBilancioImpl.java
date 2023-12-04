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

import it.csi.fel.eis.feltobilancio.EsitoFatturaAttivaRequest;
import it.csi.fel.eis.feltobilancio.FelToBilancio;
import it.csi.fel.eis.types.ResponseType;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoSdiDocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;


/**
 * The Class SdiDocumentoServiceImpl.

@WebService(serviceName = "SdiDocumentoService", portName = "SdiDocumentoServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.SdiDocumentoService")
public class SdiDocumentoServiceImpl implements SdiDocumentoService {
 */
@WebService(serviceName = "FelToBilancio", portName = "FelToBilancioPort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.fel.eis.feltobilancio.FelToBilancio")
public class FelToBilancioImpl implements FelToBilancio {
	
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
	public ResponseType esitoFatturaAttiva(EsitoFatturaAttivaRequest parameters) {
		return appCtx.getBean(AggiornaStatoSdiDocumentoService.class).executeService(parameters);
	}

	/*@Override
	public AggiornaStatoSdiDocumentoResponse aggiornaStatoSdiDocumento(AggiornaStatoSdiDocumento parameters) {
		return appCtx.getBean(AggiornaStatoSdiDocumentoService.class).executeService(parameters);
	}*/
	
	

}

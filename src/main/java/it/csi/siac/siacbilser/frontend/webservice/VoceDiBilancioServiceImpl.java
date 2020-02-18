/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.GestioneVoceDiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.UpdateVdB;
import it.csi.siac.siacbilser.frontend.webservice.msg.UpdateVdBResponse;
import it.csi.siac.siacbilser.model.VoceDiBilancio;

/**
 * Implementazione del servizio VoceDiBilancioService.
 *
 * @author alagna
 * @version $Id: $
 */
@WebService(serviceName = "VoceDiBilancioService", portName = "VoceDiBilancioServicePort", targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.VoceDiBilancioService")
public class VoceDiBilancioServiceImpl implements VoceDiBilancioService {

	/** The gestione voce di bilancio. */
	@Autowired
	private GestioneVoceDiBilancio gestioneVoceDiBilancio;

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.frontend.webservice.VoceDiBilancioService#updateVdB(it.csi.siac.siacbilser.frontend.webservice.msg.UpdateVdB)
	 */
	@Override
	@WebMethod
	@WebResult
	public UpdateVdBResponse updateVdB(@WebParam UpdateVdB parameters) {

		VoceDiBilancio vdb = gestioneVoceDiBilancio.getAndSave(parameters
				.getVoceDibilancio());

		UpdateVdBResponse res = new UpdateVdBResponse();
		res.setVoceDiBilancio(vdb);

		return res;
	}

	/**
	 * Gets the gestione voce di bilancio.
	 *
	 * @return the gestione voce di bilancio
	 */
	public GestioneVoceDiBilancio getGestioneVoceDiBilancio() {
		return gestioneVoceDiBilancio;
	}

	/**
	 * Sets the gestione voce di bilancio.
	 *
	 * @param gestore the new gestione voce di bilancio
	 */
	public void setGestioneVoceDiBilancio(GestioneVoceDiBilancio gestore) {
		this.gestioneVoceDiBilancio = gestore;
	}

	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

}

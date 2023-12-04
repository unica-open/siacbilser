/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.bilancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAttributiBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.model.AttributiBilancio;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Servizio per la ricerca degli attributi del bilancio.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 13/10/2016
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAttributiBilancioService extends CheckedAccountBaseService<AggiornaAttributiBilancio, AggiornaAttributiBilancioResponse> {
	
	@Autowired
	private BilancioDad bilancioDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio", false);
		checkNotNull(req.getAttributiBilancio(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("attributi bilancio"), false);
	}
	
	@Transactional()
	@Override
	public AggiornaAttributiBilancioResponse executeService(AggiornaAttributiBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		AttributiBilancio attributiBilancio = bilancioDad.updateAttributiDettaglioByBilancio(req.getBilancio(), req.getAttributiBilancio());
		res.setAttributiBilancio(attributiBilancio);
	}

}

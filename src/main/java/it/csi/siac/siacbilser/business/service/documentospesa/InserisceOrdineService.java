/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.OrdineDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceOrdineResponse;
import it.csi.siac.siacfin2ser.model.Ordine;

/**
 * Inserimento dell'ordine.
 *
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class InserisceOrdineService extends CheckedAccountBaseService<InserisceOrdine, InserisceOrdineResponse> {
	
	@Autowired 
	private OrdineDad ordineDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getOrdine(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ordine"));
		checkNotBlank(req.getOrdine().getNumeroOrdine(), "numero ordine");
		
	}
	
	@Override
	protected void init() {
		ordineDad.setEnte(ente);
		ordineDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	@Transactional
	public InserisceOrdineResponse executeService(InserisceOrdine serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		Ordine ordine = ordineDad.inserisceOrdine(req.getOrdine());
		res.setOrdine(ordine);
	}

	
}

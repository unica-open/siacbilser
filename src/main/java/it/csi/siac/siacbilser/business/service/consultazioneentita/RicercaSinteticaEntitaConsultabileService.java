/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.EntitaConsultabileDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaSinteticaEntitaConsultabileResponse;
import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * Ricerca sintetica delle enita consultabili.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaEntitaConsultabileService extends CheckedAccountBaseService<RicercaSinteticaEntitaConsultabile, RicercaSinteticaEntitaConsultabileResponse> {

	
	@Autowired
	private EntitaConsultabileDad entitaConsultabileDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getParametriRicercaEntitaConsultabile(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri ricerca entita consultabile"));
	}
	
	@Transactional(readOnly=true)
	public RicercaSinteticaEntitaConsultabileResponse executeService(RicercaSinteticaEntitaConsultabile serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		
		entitaConsultabileDad.setEnte(ente);
	}
	
	
	@Override
	protected void execute() {
		ListaPaginata<EntitaConsultabile> elencoEntitaConsultabili = entitaConsultabileDad.ricercaSinteticaEntitaConsultabile(req.getParametriRicercaEntitaConsultabile(), req.getParametriPaginazione());
		res.setEntitaConsultabili(elencoEntitaConsultabili);
	}

}

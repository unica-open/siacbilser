/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.EntitaConsultabileDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaFigliEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.RicercaFigliEntitaConsultabileResponse;
import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * Ricerca i figli di un entita consultabile
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaFigliEntitaConsultabileService extends CheckedAccountBaseService<RicercaFigliEntitaConsultabile, RicercaFigliEntitaConsultabileResponse> {

	@Autowired
	private EntitaConsultabileDad entitaConsultabileDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getEntitaDaCercare(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("entita da cercare"));
		checkNotNull(req.getEntitaPadre(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("entita da cercare"));
		checkCondition(req.getEntitaPadre().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid entita da cercare"));
	}
	
	@Transactional(readOnly=true)
	public RicercaFigliEntitaConsultabileResponse executeService(RicercaFigliEntitaConsultabile serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		
		entitaConsultabileDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		ListaPaginata<EntitaConsultabile> elencoEntitaConsultabili = entitaConsultabileDad.ricercaFigliEntitaConsultabile(
				req.getEntitaPadre(),
				req.getEntitaDaCercare(),
				req.getAnnoEsercizio(),
				req.getListaParametriGenerici(),
				req.getParametriPaginazione());
		res.setEntitaConsultabili(elencoEntitaConsultabili);
		if(req.isRequestImporto()) {
			BigDecimal importo = entitaConsultabileDad.calcolaImportoFigliEntitaConsultabile(
					req.getEntitaPadre(),
					req.getEntitaDaCercare(),
					req.getAnnoEsercizio(),
					req.getListaParametriGenerici());
			res.setImportoEntitaConsultabili(importo);
		}
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.rateirisconti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNotaResponse;

/**
 * Ottiene le entit&agrave; colelgate a una prima nota.
 * @author Alessandro Marchino
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OttieniEntitaCollegatePrimaNotaService extends CheckedAccountBaseService<OttieniEntitaCollegatePrimaNota, OttieniEntitaCollegatePrimaNotaResponse> {

	@Autowired
	private PrimaNotaDad primaNotaDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getPrimaNota(), "prima nota", false);
		checkNotNull(req.getTipoCollegamento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo collegamento"), false);
		checkNotNull(req.getModelDetails(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("model details"), false);
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public OttieniEntitaCollegatePrimaNotaResponse executeService(OttieniEntitaCollegatePrimaNota serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	
	@Override
	protected void execute() {
		ListaPaginata<Entita> entitaCollegate = primaNotaDad.ottieniEntitaCollegatePrimaNota(req.getPrimaNota(), req.getTipoCollegamento(), req.getModelDetails(), req.getParametriPaginazione());
		res.setEntitaCollegate(entitaCollegate);
	}

}

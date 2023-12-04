/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.business.service.ricerca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.custom.oopp.integration.dad.MovimentoGestioneOOPPDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.base.IntegBaseService;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaMovimentiGestioneOOPP;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaMovimentiGestioneOOPPResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaMovimentiGestioneOOPPService extends
		IntegBaseService<RicercaMovimentiGestioneOOPP, RicercaMovimentiGestioneOOPPResponse>
{
	@Autowired
	private MovimentoGestioneOOPPDad movimentoGestioneOOPPDad;
	
	@Override
	protected RicercaMovimentiGestioneOOPPResponse execute(RicercaMovimentiGestioneOOPP ireq) {
		RicercaMovimentiGestioneOOPPResponse ires = instantiateNewIRes();
		movimentoGestioneOOPPDad.setEnte(ente);
		ires.setElencoMovimentoGestione(movimentoGestioneOOPPDad.ricercaMovimentiGestione(ireq.getAnnoBilancio(), ireq.getCup(), ireq.getCodiceProgetto(), ireq.getCig()));
		return ires;
	}

	@Override
	protected void checkServiceParameters(RicercaMovimentiGestioneOOPP ireq) throws ServiceParamError {		
		checkParamNotNull(ireq.getCodiceProgetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codiceProgetto"));
	}
}

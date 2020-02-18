/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStoricoVariazioniCodificheCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStoricoVariazioniCodificheCapitoloResponse;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.DettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * Ricerca dello storico delle varizioni codifiche afferenti ad un capitolo.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaStoricoVariazioniCodificheCapitoloService extends CheckedAccountBaseService<RicercaStoricoVariazioniCodificheCapitolo, RicercaStoricoVariazioniCodificheCapitoloResponse> {

	@Autowired
	private VariazioniDad variazioniDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitolo(), "capitolo", false);
		checkParametriPaginazione(req.getParametriPaginazione());
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaStoricoVariazioniCodificheCapitoloResponse executeService(
			RicercaStoricoVariazioniCodificheCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		ListaPaginata<DettaglioVariazioneCodificaCapitolo> variazioniCodificheCapitolo = variazioniDad.ricercaStoricoVariazioniCodificheCapitolo(req.getCapitolo(), req.getParametriPaginazione());
		res.setDatiStoricoVariazioniCodificheCapitolo(variazioniCodificheCapitolo);
	}

}

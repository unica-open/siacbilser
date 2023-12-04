/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceDettaglioResiduoVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceDettaglioResiduoVariazioneBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

/**
 * Inserisce l'anagrafica di una Variazione Bilancio. Viene esclusa la lista di {@link DettaglioVariazioneImportoCapitolo}.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceDettaglioResiduoVariazioneBilancioService extends CheckedAccountBaseService<InserisceDettaglioResiduoVariazioneBilancio, InserisceDettaglioResiduoVariazioneBilancioResponse> {

	@Autowired
	private VariazioniDad variazioniDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getVariazioneImportoCapitolo(), "variazione importi capitolo");
	}

	@Override
	protected void init() {
		variazioniDad.setEnte(ente);
		variazioniDad.setLoginOperazione(loginOperazione);
	}

	@Override
	@Transactional(timeout=600)
	public InserisceDettaglioResiduoVariazioneBilancioResponse executeService(InserisceDettaglioResiduoVariazioneBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		variazioniDad.caricaResidui(req.getVariazioneImportoCapitolo());
	}


}

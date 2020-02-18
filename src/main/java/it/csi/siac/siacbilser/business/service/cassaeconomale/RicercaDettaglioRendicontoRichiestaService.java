/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RendicontoRichiestaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.RichiestaEconomaleDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class InserisceRendicontoRichiestaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioRendicontoRichiestaService extends CheckedAccountBaseService<RicercaDettaglioRendicontoRichiesta, RicercaDettaglioRendicontoRichiestaResponse> {
		
	
	@Autowired
	private RendicontoRichiestaEconomaleDad rendicontoRichiestaEconomaleDad;
	@Autowired
	private RichiestaEconomaleDad richiestaEconomaleDad;
	
	private RendicontoRichiesta rendicontoRichiesta;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		this.rendicontoRichiesta = req.getRendicontoRichiesta();
		checkEntita(rendicontoRichiesta, "rendiconto richiesta economale");
		rendicontoRichiesta.setEnte(ente);
		
	}
	

	@Override
	@Transactional
	public RicercaDettaglioRendicontoRichiestaResponse executeService(RicercaDettaglioRendicontoRichiesta serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		RendicontoRichiesta rendiconto = rendicontoRichiestaEconomaleDad.ricercaDettaglioRendicontoRichiesta(rendicontoRichiesta);
		if(rendiconto == null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Rendiconto richiesta economale con uid", rendicontoRichiesta.getUid()));
			res.setEsito(Esito.FALLIMENTO);
		}
		res.setRendicontoRichiesta(rendiconto);
	}

	
	
}

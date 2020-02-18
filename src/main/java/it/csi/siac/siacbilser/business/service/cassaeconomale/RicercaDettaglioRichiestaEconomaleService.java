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
import it.csi.siac.siacbilser.integration.dad.RichiestaEconomaleDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioRichiestaEconomaleService extends CheckedAccountBaseService<RicercaDettaglioRichiestaEconomale, RicercaDettaglioRichiestaEconomaleResponse> {
	
	@Autowired
	private RichiestaEconomaleDad richiestaEconomaleDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getRichiestaEconomale(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("richiesta economale"));
		checkCondition(req.getRichiestaEconomale().getUid() != 0 ||
			(req.getRichiestaEconomale().getNumeroRichiesta() != null && req.getRichiestaEconomale().getCassaEconomale() != null && req.getRichiestaEconomale().getCassaEconomale().getUid() != 0 && req.getRichiestaEconomale().getBilancio()!=null),
			ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave richiesta economale"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaDettaglioRichiestaEconomaleResponse executeService(RicercaDettaglioRichiestaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		if(req.getRichiestaEconomale().getUid() != 0) {
			ricercaByUid();
		} else {
			ricercaByNumeroAndCassa();
		}
	}

	private void ricercaByUid() {
		RichiestaEconomale richiestaEconomale = richiestaEconomaleDad.findRichiestaEconomaleByUid(req.getRichiestaEconomale().getUid());
		
		if(richiestaEconomale == null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("richiesta economale", "uid: " + req.getRichiestaEconomale().getUid()));
			res.setEsito(Esito.FALLIMENTO);	
			return;
		}
		
		res.setRichiestaEconomale(richiestaEconomale);
	}
	
	private void ricercaByNumeroAndCassa() {
		RichiestaEconomale richiestaEconomale = richiestaEconomaleDad.findRichiestaEconomaleByNumeroAndUidCassa(req.getRichiestaEconomale().getNumeroRichiesta(),
				req.getRichiestaEconomale().getCassaEconomale().getUid(),req.getRichiestaEconomale().getBilancio().getUid());

		if(richiestaEconomale == null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("richiesta economale", "numero: " + req.getRichiestaEconomale().getNumeroRichiesta()));
			res.setEsito(Esito.FALLIMENTO);	
			return;
		}
		
		res.setRichiestaEconomale(richiestaEconomale);
	}

}

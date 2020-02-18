/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RichiestaEconomaleDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.ControllaAggiornabilitaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.ControllaAggiornabilitaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ControllaAggiornabilitaRichiestaEconomaleService extends CheckedAccountBaseService<ControllaAggiornabilitaRichiestaEconomale, ControllaAggiornabilitaRichiestaEconomaleResponse> {
	
	@Autowired
	private RichiestaEconomaleDad richiestaEconomaleDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getRichiestaEconomale(), "Richiesta economale");
	}
	
	@Override
	protected void init() {
		richiestaEconomaleDad.setEnte(ente);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ControllaAggiornabilitaRichiestaEconomaleResponse executeService(ControllaAggiornabilitaRichiestaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkStatoNonAnnullato();
		checkHasNoStampaRendiconto();
		
		res.setAggiornabile(Boolean.TRUE);
	}

	private void checkStatoNonAnnullato() {
		StatoOperativoRichiestaEconomale statoOperativoRichiestaEconomale = richiestaEconomaleDad.findStatoOperativo(req.getRichiestaEconomale().getUid());
		if(StatoOperativoRichiestaEconomale.ANNULLATA.equals(statoOperativoRichiestaEconomale)) {
			throw new BusinessException(ErroreCEC.CEC_ERR_0006.getErrore());
		}
	}

	private void checkHasNoStampaRendiconto() {
		Integer numeroRendicontoStampato = richiestaEconomaleDad.findNumeroRendicontoStampato(req.getRichiestaEconomale().getUid());
		if(numeroRendicontoStampato != null) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("la richiesta e' presente nel rendiconto numero " + numeroRendicontoStampato));
		}
	}

}

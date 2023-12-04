/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestione;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestioneResponse;
import it.csi.siac.siacfinser.model.StrutturaAmmContabileFlatStoricizzato;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestioneService extends AbstractBaseService<LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestione, LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestioneResponse> {

	
	final String methodName = "LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestioneService";
	
	@Override
	protected void init() {
		log.debug(methodName, ": init - Begin");
	}

	
	@Override
	@Transactional(readOnly=true)
	public LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestioneResponse executeService(LeggiStoricoAggiornamentoStrutturaCompetenteMovimentoGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
		
	@Override
	public void execute() {
				
		log.debug(methodName, ": execute - Begin");
		
		List<StrutturaAmmContabileFlatStoricizzato> storico = commonDad.leggiStoricoAggiornamentoStrutturaCompetente(req.getMovimento().getUid(), req.getMovimento().getTipoMovimento());
		
		res.setStoricoStrutturaCompetente(storico);
		
		
		log.debug(methodName, ": execute - End");
	}
	

}

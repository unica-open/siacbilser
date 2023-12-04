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
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoProvvedimentoMovimentoGestione;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneResponse;
import it.csi.siac.siacfinser.model.AttoAmministrativoStoricizzato;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneService extends AbstractBaseService<LeggiStoricoAggiornamentoProvvedimentoMovimentoGestione, LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneResponse> {

	
	final String methodName = "LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneService";
	
	@Override
	protected void init() {
		log.debug(methodName, ": init - Begin");
	}

	
	@Override
	@Transactional(readOnly=true)
	public LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneResponse executeService(LeggiStoricoAggiornamentoProvvedimentoMovimentoGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
		
	@Override
	public void execute() {
				
		log.debug(methodName, ": execute - Begin");
		
		List<AttoAmministrativoStoricizzato> storico = commonDad.leggiStoricoAggiornamentoProvvedimento(req.getMovimento().getUid(), req.getMovimento().getTipoMovimento());
		
		res.setStoricoAttoAmministrativi(storico);
		
		
		log.debug(methodName, ": execute - End");
	}
	

}

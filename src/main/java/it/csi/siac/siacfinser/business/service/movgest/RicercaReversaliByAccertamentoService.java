/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaReversaliByAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaReversaliByAccertamentoResponse;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaReversaliByAccertamentoService extends AbstractBaseService<RicercaReversaliByAccertamento, RicercaReversaliByAccertamentoResponse> {

	
	final String methodName = "RicercaReversaliByAccertamentoService";
	
	@Override
	protected void init() {
		log.debug(methodName, ": init - Begin");
	}

	
	@Override
	@Transactional(readOnly=true)
	public RicercaReversaliByAccertamentoResponse executeService(RicercaReversaliByAccertamento serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
		
	@Override
	public void execute() {
				
		log.debug(methodName, ": execute - Begin");
		
		Boolean reversaliCollegateAlMovimento = accertamentoOttimizzatoDad.ricercaReversaliByAccertamento(req.getUidMovimento(), req.getEnte().getUid());
		
		res.setReversaliCollegateAlMovimento(reversaliCollegateAlMovimento);
		
		
		log.debug(methodName, ": execute - End");
	}
	

}

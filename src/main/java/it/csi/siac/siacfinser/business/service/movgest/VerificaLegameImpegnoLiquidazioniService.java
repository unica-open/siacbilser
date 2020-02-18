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
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaLegameImpegnoLiquidazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaLegameImpegnoLiquidazioniResponse;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaLegameImpegnoLiquidazioniService extends AbstractBaseService<VerificaLegameImpegnoLiquidazioni, VerificaLegameImpegnoLiquidazioniResponse> {

	
	final String methodName = "VerificaLegameImpegnoLiquidazioniService";
	
	@Override
	protected void init() {
		log.debug(methodName, ": init - Begin");
	}

	
	@Override
	@Transactional(readOnly=true)
	public VerificaLegameImpegnoLiquidazioniResponse executeService(VerificaLegameImpegnoLiquidazioni serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
		
	@Override
	public void execute() {
				
		log.debug(methodName, ": execute - Begin");
		
		Boolean liquidazioniCollegateAlMovimento = impegnoOttimizzatoDad.ricercaLiquidazioniByMovimentoGestione(req.getUidMovimento(), req.getEnte().getUid());
		
		res.setLiquidazioniCollegateAlMovimento(liquidazioniCollegateAlMovimento);
		
		
		log.debug(methodName, ": execute - End");
	}
	

}

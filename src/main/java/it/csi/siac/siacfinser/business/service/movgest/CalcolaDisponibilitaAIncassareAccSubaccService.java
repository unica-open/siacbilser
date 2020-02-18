/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaDisponibilitaAIncassareAccSubacc;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaDisponibilitaAIncassareAccSubaccResponse;
import it.csi.siac.siacfinser.integration.dad.AccertamentoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcolaDisponibilitaAIncassareAccSubaccService extends AbstractBaseService<CalcolaDisponibilitaAIncassareAccSubacc, CalcolaDisponibilitaAIncassareAccSubaccResponse> {

	@Autowired
	AccertamentoOttimizzatoDad accertamentoDad;
	
		
	@Override
	protected void init() {
		final String methodName="CalcolaDisponibilitaAIncassareAccSubacc : init()";
		log.debug(methodName, " - Begin");
		
	}	

	
	@Override
	@Transactional
	public CalcolaDisponibilitaAIncassareAccSubaccResponse executeService(CalcolaDisponibilitaAIncassareAccSubacc serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		String methodName = "CalcolaDisponibilitaAIncassareAccSubacc - execute()";
		log.debug(methodName, " - Begin");
			
		DisponibilitaMovimentoGestioneContainer disponibilitaAIncassare = accertamentoDad.calcolaDisponibiltaAIncassareAccertamentoOSub(req.getUidMovimentoGestione());
				
		res.setDisponibilitaAIncassare(disponibilitaAIncassare.getDisponibilita());
		res.setMotivazioneDisponibilitaAIncassare(disponibilitaAIncassare.getMotivazione());
		res.setEsito(Esito.SUCCESSO);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="CalcolaDisponibilitaAIncassareAccSubacc : checkServiceParam()";
		
		log.debug(methodName, " - Begin");
		
		checkNotNull(req.getUidMovimentoGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid accertamento/subaccertamento"));
		
		log.debug(methodName, " - End");
		
	}	
}

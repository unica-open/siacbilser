/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.liquidazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaDisponibilitaALiquidare;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaDisponibilitaALiquidareResponse;
import it.csi.siac.siacfinser.integration.dad.LiquidazioneDad;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcolaDisponibilitaALiquidareService extends AbstractBaseService<CalcolaDisponibilitaALiquidare,CalcolaDisponibilitaALiquidareResponse> {

	@Autowired
	LiquidazioneDad liquidazioneDad;
	
		
	@Override
	protected void init() {
		final String methodName="CalcolaDisponibilitaALiquidareService : init()";
		log.debug(methodName, " - Begin");
		
	}	

	
	@Override
	@Transactional
	public CalcolaDisponibilitaALiquidareResponse executeService(CalcolaDisponibilitaALiquidare serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		String methodName = "CalcolaDisponibilitaALiquidareService - execute()";
		log.debug(methodName, " - Begin");
			
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		setBilancio(req.getBilancio());
		
		Liquidazione liquidazione = req.getLiquidazione();
			
		
		DisponibilitaMovimentoGestioneContainer disponibilitaALiquidareContainer = liquidazioneDad.calcolaDisponibilitaALiquidare(liquidazione, ente.getUid(), bilancio.getAnno(), richiedente.getAccount().getId());
				
		res.setDisponibilitaALiquidare(disponibilitaALiquidareContainer.getDisponibilita());
		res.setMotivazioneDisponibilitaLiquidare(disponibilitaALiquidareContainer.getMotivazione());
		res.setEsito(Esito.SUCCESSO);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="AggiornaLiquidazioneModulareService : checkServiceParam()";
		
		log.debug(methodName, " - Begin");
		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		
		checkNotNull(req.getLiquidazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("liquidazione"));
		
		log.debug(methodName, " - End");
		
	}	
}

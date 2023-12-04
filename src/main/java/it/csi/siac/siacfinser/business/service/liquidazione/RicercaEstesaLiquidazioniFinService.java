/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.liquidazione;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaEstesaLiquidazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaEstesaLiquidazioniResponse;
import it.csi.siac.siacfinser.integration.dad.LiquidazioneDad;
import it.csi.siac.siacfinser.model.liquidazione.LiquidazioneAtti;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaEstesaLiquidazioniFinService extends AbstractBaseService<RicercaEstesaLiquidazioni, RicercaEstesaLiquidazioniResponse> {

	@Autowired
	LiquidazioneDad liquidazioneDad;
	

	@Override
	protected void init() {
		final String methodName="RicercaEstesaLiquidazioniService : init()";
		log.debug(methodName, " - Begin");
		
	}	
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaEstesaLiquidazioniResponse executeService(RicercaEstesaLiquidazioni serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		String methodName = "RicercaEstesaLiquidazioniService - execute()";
		log.debug(methodName, " - Begin");

		ente = req.getEnte();
		
		RicercaProvvedimentoResponse resAtto = ricercaProvvedimento(req.getAtto(), req.getRichiedente());
		
		List<LiquidazioneAtti> liquidazioni = liquidazioneDad.ricercaEstesaLiquidazioni(req.getAnnoEsercizio(),resAtto.getListaAttiAmministrativi().get(0), ente.getUid());
				
		
		
		if(liquidazioni==null || liquidazioni.isEmpty()){
			throw new BusinessException(ErroreCore.NESSUN_DATO_REPERITO.getErrore(), Esito.FALLIMENTO);
		}

		// Response per esito KO
		res.setEsito(Esito.SUCCESSO);
		res.setNumRisultati(liquidazioni.size());
		res.setNumPagina(req.getNumPagina());
		res.setElencoLiquidazioni(liquidazioni);

		

	}


	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName="RicercaLiquidazioniService : checkServiceParam()";
		log.debug(methodName, " - Begin");
		

	}	
}

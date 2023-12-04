/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAvanzovincolo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAvanzovincoloResponse;
import it.csi.siac.siacfinser.integration.dao.common.dto.DatiOperazioneDto;
import it.csi.siac.siacfinser.integration.util.Operazione;
import it.csi.siac.siacfinser.model.Avanzovincolo;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAvanzovincoloService extends AbstractBaseService<RicercaAvanzovincolo, RicercaAvanzovincoloResponse> {

	
	@Override
	protected void init() {
		final String methodName = "RicercaAvanzovincoloService : init()";
		log.debug(methodName, "- Begin");		
	}	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaAvanzovincoloResponse executeService(RicercaAvanzovincolo serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	public void execute() {
		final String methodName = "RicercaAvanzovincoloService : execute()";
		//1. Leggiamo i dati ricevuti dalla request:
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Integer idEnte = ente.getUid();
		List<Avanzovincolo> avanzovincoliList = new ArrayList<Avanzovincolo>();
		
		setBilancio(req.getBilancio());
		Integer annoBilancio = getBilancio().getAnno();
		
		DatiOperazioneDto datiOperazione = commonDad.inizializzaDatiOperazione(ente, req.getRichiedente(), Operazione.RICERCA, CostantiFin.AMBITO_FIN, annoBilancio);
		avanzovincoliList = accertamentoOttimizzatoDad.ricercaAvanzovincolo(datiOperazione);
		
		List<Avanzovincolo> paginata = getPaginata(avanzovincoliList, req.getNumPagina(), req.getNumRisultatiPerPagina());
		
		//RESP:
		res.setEsito(Esito.SUCCESSO);
		res.setElencoAvanzovincolo(paginata);
		res.setNumRisultati(avanzovincoliList.size());
		res.setNumPagina(req.getNumPagina());

	}


	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "RicercaAvanzovincoloService : checkServiceParam()";
		log.debug(methodName, "- Begin");

		Bilancio bilancioInput = req.getBilancio();
		Ente ente = req.getRichiedente().getAccount().getEnte();
		
		if(ente == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		} else if(bilancioInput == null){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("BILANCIO"));
		}
		
	}	

}

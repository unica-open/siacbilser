/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;

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
import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.business.service.common.RicercaAttributiMovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.util.ObjectStreamerHandler;
import it.csi.siac.siacfinser.model.Accertamento;

@Deprecated
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAccertamentoPerChiaveService extends RicercaAttributiMovimentoGestioneService<RicercaAccertamentoPerChiave, RicercaAccertamentoPerChiaveResponse> {
	
	@Autowired
	ObjectStreamerHandler objectStreamerHandler;
	
	@Override
	protected void init() {
		final String methodName = "RicercaAccertamentoPerChiaveService : init()";
		log.debug(methodName, "- Begin");
	}	
	
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaAccertamentoPerChiaveResponse executeService(RicercaAccertamentoPerChiave serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		final String methodName = "RicercaAccertamentoPerChiaveService : execute()";
		log.debug(methodName, "- Begin");
		log.debug(methodName, "- req.getpRicercaImpegnoK() " + req.getpRicercaAccertamentoK());
		
		//1. Vengono letti i valori ricevuti in input dalla request
		String annoEsercizio = req.getpRicercaAccertamentoK().getAnnoEsercizio().toString();
		Integer annoAccertamento = req.getpRicercaAccertamentoK().getAnnoAccertamento();
		BigDecimal numeroAccertamento = req.getpRicercaAccertamentoK().getNumeroAccertamento();
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();

		//2. Si richiama il metodo interno di ricerca per chiave per impegni o accertamenti:
		Accertamento accertamento = (Accertamento) accertamentoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio, annoAccertamento, numeroAccertamento, Constanti.MOVGEST_TIPO_ACCERTAMENTO, true);
		
		if(null!=accertamento){
			//si invoca il metodo completaDatiRicercaAccertamentoPk che si occupa di vestire i dati ottenuti:
			accertamento = completaDatiRicercaAccertamentoPk(richiedente, accertamento);
			//componiamo la respose esito positivo:
			res.setAccertamento(accertamento);
			res.setBilancio(accertamento.getCapitoloEntrataGestione().getBilancio());
			res.setCapitoloEntrataGestione(accertamento.getCapitoloEntrataGestione());
			res.setEsito(Esito.SUCCESSO);
		} else {
			//componiamo la respose esito negativo:
			res.setBilancio(null);
			res.setCapitoloEntrataGestione(null);
			res.setAccertamento(null);
			res.setEsito(Esito.FALLIMENTO);
		}
		
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "RicercaAccertamentoPerChiaveService : checkServiceParam()";
	
		//dati di input presi da request:
		Integer annoEsercizio = req.getpRicercaAccertamentoK().getAnnoEsercizio();
		Integer annoAccertamento = req.getpRicercaAccertamentoK().getAnnoAccertamento();
		BigDecimal numeroAccertamento = req.getpRicercaAccertamentoK().getNumeroAccertamento();
		Ente ente = req.getRichiedente().getAccount().getEnte();

		if(null==req.getpRicercaAccertamentoK()){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("RICERCA_ACCERTAMENTO_K"));			
		} else if(null==annoEsercizio && null==annoAccertamento && null==numeroAccertamento && null==ente){
			checkCondition(false, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN PARAMETRO INIZIALIZZATO"));
		}  else if(null==annoEsercizio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_ESERCIZIO"));
		} else if(null==annoAccertamento){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_IMPEGNO"));
		} else if(null==numeroAccertamento){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO_IMPEGNO"));
		} else if(null==ente){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		}
	}	
	

}

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
import it.csi.siac.siacfinser.business.service.common.RicercaAttributiMovimentoGestioneOttimizzatoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubAccertamentiDiUnAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubAccertamentiDiUnAccertamentoResponse;
import it.csi.siac.siacfinser.integration.dad.AccertamentoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSubAccertamentiDiUnAccertamentoService extends RicercaAttributiMovimentoGestioneOttimizzatoService<RicercaSubAccertamentiDiUnAccertamento, RicercaSubAccertamentiDiUnAccertamentoResponse> {

	@Autowired
	AccertamentoOttimizzatoDad accertamentoOttimizzatoDad;
	
	@Override
	protected void init() {
		final String methodName = "RicercaSubAccertamentiDiUnAccertamentoService : init()";
		log.debug(methodName, "- Begin");		
	}	
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaSubAccertamentiDiUnAccertamentoResponse executeService(RicercaSubAccertamentiDiUnAccertamento serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	public void execute() {
		final String methodName = "RicercaSubAccertamentiDiUnAccertamentoService : execute()";
		log.debug(methodName, "- Begin");
		log.debug(methodName, "- req.getParametroRicercaAccertamento() " + req.getpRicercaAccertamentoK());
		
		
		//1. Leggiamo i dati ricevuti dalla request:
		RicercaAccertamentoK ricercaAccertamentoK = req.getpRicercaAccertamentoK();
		Ente ente = req.getRichiedente().getAccount().getEnte();
		Integer idEnte = ente.getUid();
		
		String annoEsercizio = req.getpRicercaAccertamentoK().getAnnoEsercizio().toString();
		Integer annoAccertamento = req.getpRicercaAccertamentoK().getAnnoAccertamento();
		BigDecimal numeroAccertamento = req.getpRicercaAccertamentoK().getNumeroAccertamento();
		Richiedente richiedente = req.getRichiedente();
		Accertamento accertamento = null;

		//2. Si invoca il metodo ricercaAccertamenti che ci restituisce il numero di risultati attesi dalla query composta
		//secondo i parametri di ricerca
		
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = new PaginazioneSubMovimentiDto();
		paginazioneSubMovimentiDto.setDimensionePagina(req.getNumRisultatiPerPagina());
		paginazioneSubMovimentiDto.setNumeroPagina(req.getNumPagina());
		paginazioneSubMovimentiDto.setPaginazione(true);
		paginazioneSubMovimentiDto.setNoSub(false);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = req.getDatiOpzionaliElencoSubTuttiConSoloGliIds();
		
		EsitoRicercaMovimentoPkDto esito = accertamentoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, annoEsercizio, annoAccertamento, numeroAccertamento, paginazioneSubMovimentiDto,caricaDatiOpzionaliDto, Constanti.MOVGEST_TIPO_ACCERTAMENTO, true);
	
		if(esito!=null && esito.getMovimentoGestione()!=null){
			accertamento = ((Accertamento) esito.getMovimentoGestione());
			if(accertamento!=null){
				
				//set dei dati di paginazione sub:
				impostaDatiRespPaginazioneSub(esito);
				//
				
				//Imposto i sub trovati:
				res.setElencoSubAccertamenti(accertamento.getElencoSubAccertamenti());
				//
				//si invoca il metodo completaDatiRicercaAccertamentoPk che si occupa di vestire i dati ottenuti:
				accertamento = completaDatiRicercaAccertamentoPk(richiedente, accertamento);
				
				//setto i dati dell testata:
				res.setAccertamento(accertamento);
				
				res.setEsito(Esito.SUCCESSO);
			}
		}
			
	}


	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		final String methodName = "RicercaSubAccertamentiDiUnAccertamentoService : checkServiceParam()";

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
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_ACCERTAMENTO"));
		} else if(null==numeroAccertamento){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO_ACCERTAMENTO"));
		} else if(null==ente){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		}
		
		if(req.getNumPagina()<=0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO PAGINA DEVE ESSERE MAGGIORE DI ZERO"));
		} if(req.getNumRisultatiPerPagina()<=0){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO RISULTATI PER PAGINA DEVE ESSERE MAGGIORE DI ZERO"));
		}
	}	
	
	
}


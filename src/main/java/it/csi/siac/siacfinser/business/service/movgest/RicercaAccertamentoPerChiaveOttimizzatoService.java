/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.common.RicercaAttributiMovimentoGestioneOttimizzatoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.integration.dad.AccertamentoOttimizzatoDad;
import it.csi.siac.siacfinser.integration.dad.StoricoImpegnoAccertamentoDad;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
//import it.csi.siac.siacfinser.integration.util.ObjectStreamerHandler;
import it.csi.siac.siacfinser.model.Accertamento;
;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaAccertamentoPerChiaveOttimizzatoService extends RicercaAttributiMovimentoGestioneOttimizzatoService<RicercaAccertamentoPerChiaveOttimizzato, RicercaAccertamentoPerChiaveOttimizzatoResponse> {
	
	@Autowired
	private AccertamentoOttimizzatoDad accertamentoOttimizzatoDad;
	
	@Autowired
	private StoricoImpegnoAccertamentoDad storicoImpegnoAccertamentoDad;

//	@Autowired
//	private ObjectStreamerHandler objectStreamerHandler;
	
	@Override
	protected void init() {
		final String methodName = "RicercaAccertamentoPerChiaveOttimizzatoService : init()";
		log.debug(methodName, "- Begin");
	}	
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaAccertamentoPerChiaveOttimizzatoResponse executeService(RicercaAccertamentoPerChiaveOttimizzato serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	public void execute() {
		final String methodName = "RicercaAccertamentoPerChiaveOttimizzatoService : execute()";
		log.debug(methodName, "- Begin");
		log.debug(methodName, "- req.getpRicercaImpegnoK() " + req.getpRicercaAccertamentoK());
		
		//1. Vengono letti i valori ricevuti in input dalla request
		String annoEsercizio = req.getpRicercaAccertamentoK().getAnnoEsercizio().toString();
		Integer annoAccertamento = req.getpRicercaAccertamentoK().getAnnoAccertamento();
		BigDecimal numeroAccertamento = req.getpRicercaAccertamentoK().getNumeroAccertamento();
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Accertamento accertamento = null;
		
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = analizzaRichiestaPaginazioneSub();
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = req.getDatiOpzionaliElencoSubTuttiConSoloGliIds();
		
		long startUno = System.currentTimeMillis();

		//2. Si richiama il metodo interno di ricerca per chiave per impegni o accertamenti:
		EsitoRicercaMovimentoPkDto esitoRicerca = accertamentoOttimizzatoDad.ricercaMovimentoPk(
				richiedente, ente, annoEsercizio, annoAccertamento, numeroAccertamento, paginazioneSubMovimentiDto, caricaDatiOpzionaliDto,
				CostantiFin.MOVGEST_TIPO_ACCERTAMENTO, true, req.isCaricalistaModificheCollegate());
		
		long endUno = System.currentTimeMillis();
		long startDue = System.currentTimeMillis();
		
		// RM: Commento per la siac-4188, la presenza di troppe reversali collegate all'accertamento fa andare in timeout il 
		//		caricamento del movimento, spostata la chiamata a un servizio dedicato nell'app		
		if(esitoRicerca!=null && esitoRicerca.getMovimentoGestione()!=null){
			accertamento = (Accertamento) esitoRicerca.getMovimentoGestione();
			//accertamento.setCollegatoAOrdinativi(esitoRicerca.isMovimentoConOrdinativi());
		}
		
		if(null!=accertamento){
			
			DatiOpzionaliCapitoli datiOpzionaliCapitoli = req.getDatiOpzionaliCapitoli();
			
			//set dei dati di paginazione sub:
			impostaDatiRespPaginazioneSub(esitoRicerca);
			//
			
			//si invoca il metodo completaDatiRicercaAccertamentoPk che si occupa di vestire i dati ottenuti:
			accertamento = completaDatiRicercaAccertamentoPk(richiedente, accertamento,datiOpzionaliCapitoli,null,null);
			
			Bilancio bil = accertamento.getCapitoloEntrataGestione().getBilancio();
			
			if(req.isCaricaFlagPresenteStoricizzazioneNelBilancio()) {
				Integer count = storicoImpegnoAccertamentoDad.countLegamiStoricizzazione(null, accertamento, null, null, Boolean.FALSE, bil, ente);
				res.setHasStoricizzazioneNellBilancio(count != null && count.intValue() != 0);
			}
			
			//SIAC-8142
			if(req.isCaricaAnnoAccertamentiConStessoNumeroNelBilancio()) {
				List<Integer> anniAccertamentiConStessoNumeroNelBilancio = accertamentoOttimizzatoDad.caricaAnniAccertamentiConStessoNumeroNelBilancio(accertamento, annoEsercizio, ente);
				res.setAnniAccertamentiConStessoNumeroNelBilancio(anniAccertamentiConStessoNumeroNelBilancio);
			}
			
			
			//componiamo la respose esito positivo:
			res.setAccertamento(accertamento);
			res.setBilancio(bil);
			res.setCapitoloEntrataGestione(accertamento.getCapitoloEntrataGestione());
			res.setEsito(Esito.SUCCESSO);
			
			
			long endDue = System.currentTimeMillis();
			
			long totUno = endUno - startUno;
			long totDue = endDue - startDue;
			
			CommonUtil.println("totUno: " + totUno + " - totDue: " + totDue);
			
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
//		final String methodName = "RicercaAccertamentoPerChiaveOttimizzatoService : checkServiceParam()";
	
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

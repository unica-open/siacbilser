/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.movgest;

import java.math.BigDecimal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CommonUtil;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.common.RicercaAttributiMovimentoGestioneOttimizzatoService;
import it.csi.siac.siacfinser.business.service.util.Utility;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoROR;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.integration.dao.common.dto.EsitoRicercaMovimentoPkDto;
import it.csi.siac.siacfinser.integration.dao.common.dto.PaginazioneSubMovimentiDto;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;



@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaImpegnoPerChiaveOttimizzatoRORService extends RicercaAttributiMovimentoGestioneOttimizzatoService<RicercaImpegnoPerChiaveOttimizzatoROR, RicercaImpegnoPerChiaveOttimizzatoResponse> {
	
	@Override
	protected void init() {
		final String methodName = "RicercaImpegnoPerChiaveOttimizzatoService : init()";
		log.debug(methodName, "- Begin");
	}	
	
	@Override
	public void execute() {

		final String methodName = "RicercaImpegnoPerChiaveOttimizzatoService : execute()";
		log.debug(methodName, "- Begin");
		log.debug(methodName, "- req.getpRicercaImpegnoK() " + req.getpRicercaImpegnoK());
		
		//1. Vengono letti i valori ricevuti in input dalla request
		String annoEsercizio = req.getpRicercaImpegnoK().getAnnoEsercizio().toString();
		Integer annoImpegno = req.getpRicercaImpegnoK().getAnnoImpegno();
		BigDecimal numeroImpegno = req.getpRicercaImpegnoK().getNumeroImpegno();
		Richiedente richiedente = req.getRichiedente();
		Ente ente = req.getEnte();
		Impegno impegno = null;
		
		PaginazioneSubMovimentiDto paginazioneSubMovimentiDto = analizzaRichiestaPaginazioneSub();
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds caricaDatiOpzionaliDto = req.getDatiOpzionaliElencoSubTuttiConSoloGliIds();
		
		
		long startUno = System.currentTimeMillis();
		
		EsitoRicercaMovimentoPkDto esitoRicerca = impegnoOttimizzatoDad.ricercaMovimentoPk(richiedente, ente, 
				annoEsercizio, annoImpegno, numeroImpegno, paginazioneSubMovimentiDto, caricaDatiOpzionaliDto,
				CostantiFin.MOVGEST_TIPO_IMPEGNO, true, req.isCaricalistaModificheCollegate());
		
		//SETTING IMPORTI PER MOVGEST
		SubImpegno subimpegno = impegnoOttimizzatoDad.campiRiepilogoROR(annoEsercizio, req.getUidMovGest(),ente.getUid());
		if(subimpegno != null && esitoRicerca.getMovimentoGestione()!= null){
			esitoRicerca.getMovimentoGestione().setImportoDaRiaccertare(subimpegno.getImportoDaRiaccertare());
			esitoRicerca.getMovimentoGestione().setImportoMaxDaRiaccertare(subimpegno.getImportoMaxDaRiaccertare());
			esitoRicerca.getMovimentoGestione().setImportoModifiche(subimpegno.getImportoModifiche());
			esitoRicerca.getMovimentoGestione().setDifferitoN(subimpegno.getDifferitoN());
			esitoRicerca.getMovimentoGestione().setDifferitoN1(subimpegno.getDifferitoN1());
			esitoRicerca.getMovimentoGestione().setDifferitoN2(subimpegno.getDifferitoN2());
			esitoRicerca.getMovimentoGestione().setDifferitoNP(subimpegno.getDifferitoNP());
			esitoRicerca.getMovimentoGestione().setDaCancellareInesigibilita(subimpegno.getDaCancellareInesigibilita());
			esitoRicerca.getMovimentoGestione().setDaCancellareInsussistenza(subimpegno.getDaCancellareInsussistenza());
			esitoRicerca.getMovimentoGestione().setResiduoDaMantenere(subimpegno.getResiduoDaMantenere());
			esitoRicerca.getMovimentoGestione().setNumeroTotaleModifcheMovimento(subimpegno.getNumeroTotaleModifcheMovimento());
			esitoRicerca.getMovimentoGestione().setLiquidatoAnnoSuccessivo(subimpegno.getLiquidatoAnnoSuccessivo());
			esitoRicerca.getMovimentoGestione().setDocumentiNoLiqAnnoSuccessivo(subimpegno.getDocumentiNoLiqAnnoSuccessivo());
		}
		
		
		
		
		long endUno = System.currentTimeMillis();
		
		long startDue = System.currentTimeMillis();
		
		// Jira 4189  FIN - Impegni: ricerca per chiave con tante liquidazioni
		if(esitoRicerca!=null && esitoRicerca.getMovimentoGestione()!=null){
			impegno = (Impegno) esitoRicerca.getMovimentoGestione();
			//impegno.setCollegatoALiquidazioni(esitoRicerca.isMovimentoConLiquidazioni()); 
		}
		

		
		if(null!=impegno){
			
			DatiOpzionaliCapitoli datiOpzionaliCapitoli = req.getDatiOpzionaliCapitoli();
			
			//set dei dati di paginazione sub:
			impostaDatiRespPaginazioneSub(esitoRicerca);
			//
			
			impegno = completaDatiImpegnoESubDopoRicercaMovimentoPk(impegno, richiedente, annoEsercizio, req.getEnte().getUid(),datiOpzionaliCapitoli);
			
			//componiamo la respose esito positivo:
			res.setImpegno(impegno);
			res.setBilancio(impegno.getCapitoloUscitaGestione().getBilancio());
			res.setCapitoloUscitaGestione(impegno.getCapitoloUscitaGestione());
			res.setEsito(Esito.SUCCESSO);
			Utility.logXmlTypeObject(res, "WAWA"); // https://en.wikipedia.org/wiki/Wawa
			
			long endDue = System.currentTimeMillis();
			
			long totUno = endUno - startUno;
			long totDue = endDue - startDue;
			
			CommonUtil.println("totUno: " + totUno + " - totDue: " + totDue);
			
		} else {
			//componiamo la respose esito negativo:
			res.setBilancio(null);
			res.setCapitoloUscitaGestione(null);
			res.setImpegno(null);
			res.setEsito(Esito.FALLIMENTO);
		}
	}
	
	
	
	@Override
	@Transactional(readOnly=true)
	public RicercaImpegnoPerChiaveOttimizzatoResponse executeService(RicercaImpegnoPerChiaveOttimizzatoROR serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		//dati di input presi da request:
		Integer annoEsercizio = req.getpRicercaImpegnoK().getAnnoEsercizio();
		Integer annoImpegno = req.getpRicercaImpegnoK().getAnnoImpegno();
		BigDecimal numeroImpegno = req.getpRicercaImpegnoK().getNumeroImpegno();
		Ente ente = req.getRichiedente().getAccount().getEnte();

		if(null==req.getpRicercaImpegnoK()){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("RICERCA_IMPEGNO_K"));			
		} else if(null==annoEsercizio && null==annoImpegno && null==numeroImpegno && null==ente){
			checkCondition(false, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore("NESSUN PARAMETRO INIZIALIZZATO"));
		}  else if(null==annoEsercizio){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_ESERCIZIO"));
		} else if(null==annoImpegno){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ANNO_IMPEGNO"));
		} else if(null==numeroImpegno){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("NUMERO_IMPEGNO"));
		} else if(null==ente){
			checkCondition(false, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ENTE"));
		}
	}	
	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;


@Deprecated
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AccertamentoDad extends MovimentoGestioneDad<Accertamento, SubAccertamento>{

	
	
	/**
	 * wrapper di retro compatibilita'
	 */
	@Override
	protected Accertamento convertiMovimentoGestione(SiacTMovgestFin siacTMovgest){
		return convertiMovimentoGestione(siacTMovgest);
	}

	@Override
	protected Accertamento convertiMovimentoGestione(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
		Accertamento accertamentoTrovato = map(siacTMovgest, Accertamento.class, FinMapId.SiacTMovgest_Accertamento);	
		accertamentoTrovato = EntityToModelConverter.siacTMovgestEntityToAccertamentoModel(siacTMovgest, accertamentoTrovato,ottimizzazioneDto);	
		//Termino restituendo l'oggetto di ritorno: 
		return accertamentoTrovato;
	}
	
	@Override
	protected Accertamento convertiMovimentoGestioneOPT(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
		Accertamento accertamentoTrovato = map(siacTMovgest, Accertamento.class, FinMapId.SiacTMovgest_Accertamento);	
		accertamentoTrovato = EntityToModelConverter.siacTMovgestEntityToAccertamentoModelOPT(siacTMovgest, accertamentoTrovato,ottimizzazioneDto);	
		//Termino restituendo l'oggetto di ritorno: 
		return accertamentoTrovato;
	}

	/**
	 * wrapper di retro compatibilita'
	 */
	@Override
	protected SubAccertamento convertiSubMovimento(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest, boolean caricaDatiUlteriori){
		return convertiSubMovimento(siacTMovgestTs, siacTMovgest, caricaDatiUlteriori,null);
	}

	@Override
	protected SubAccertamento convertiSubMovimento(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest, boolean caricaDatiUlteriori,OttimizzazioneMovGestDto ottimizzazioneDto){
		SubAccertamento subAccertamentoTrovato = map(siacTMovgestTs, SubAccertamento.class, FinMapId.SiacTMovgestTs_SubAccertamento);
		subAccertamentoTrovato = EntityToModelConverter.siacTMovgestTsEntityToSubAccertamentoModel(siacTMovgestTs, subAccertamentoTrovato, siacTMovgest,ottimizzazioneDto);
		
		//Termino restituendo l'oggetto di ritorno: 
		return subAccertamentoTrovato;
	}
	
	@Override
	protected SubAccertamento convertiSubMovimentoOPT(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
		SubAccertamento subAccertamentoTrovato = map(siacTMovgestTs, SubAccertamento.class, FinMapId.SiacTMovgestTs_SubAccertamento);
		subAccertamentoTrovato = EntityToModelConverter.siacTMovgestTsEntityToSubAccertamentoModelOPT(toList(siacTMovgestTs), 
				toList(subAccertamentoTrovato), siacTMovgest,ottimizzazioneDto).get(0);
		//Termino restituendo l'oggetto di ritorno: 
		return subAccertamentoTrovato;
	}


		@Override
		protected boolean checkStato(String stato) {
			return CostantiFin.MOVGEST_STATO_DEFINITIVO.equals(stato) || CostantiFin.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(stato);
		}


		/**
		 * Operazione interna di doppia gestione
		 * 
		 * @param richiedente
		 * @param ente
		 * @param bilancio
		 * @param primoAccertamentoDaInserire
		 * @param datiOperazione
		 * @return EsitoOperazioneInserimentoMovgestDoppiaGestioneDto
		 */
		/*@Override
		public EsitoOperazioneInserimentoMovgestDoppiaGestioneDto operazioneInternaInserisceMovimento(Richiedente richiedente, Ente ente, Bilancio bilancio, MovimentoGestione primoImpegnoDaInserire, DatiOperazioneDto datiOperazione,Integer forzaMaxCodePerDoppia){
			
			EsitoOperazioneInserimentoMovgestDoppiaGestioneDto esito = new EsitoOperazioneInserimentoMovgestDoppiaGestioneDto();
			
			List<Errore> listaErrori = this.controlliDiMeritoInserimentoAccertamentoOperazioneInterna(richiedente,  ente, bilancio, (Accertamento) primoImpegnoDaInserire, datiOperazione);
			
			// setto eventuali errori
			esito.setListaErrori(listaErrori);
			
			// se non ci sono errori procedo con inserimento
			if(listaErrori==null || listaErrori.size()==0){
				Accertamento accertamento  = (Accertamento) this.inserisceImpegno(richiedente, ente, bilancio, primoImpegnoDaInserire, datiOperazione,forzaMaxCodePerDoppia);
				// setto accertamento inserito
				esito.setMovimentoGestione(accertamento);
			}
			
			
			//Termino restituendo l'oggetto di ritorno: 
			return esito;
		}	*/
		
		/*@Override	
		public EsitoAggiornamentoMovimentoGestioneDto operazioneInternaAggiornaMovGest(Richiedente richiedente, Ente ente, Bilancio bilancio, MovimentoGestione impegnoDaAggiornare, Soggetto soggettoCreditore, UnitaElementareGestione unitaElemDiGest, 
				DatiOperazioneDto datiOperazione, SubMovgestInModificaInfoDto subMovgestInModificaValutati,boolean fromDoppiaGestione,CapitoliInfoDto capitoliInfo){
			EsitoAggiornamentoMovimentoGestioneDto esito=new EsitoAggiornamentoMovimentoGestioneDto();
			ImpegnoInModificaInfoDto impegnoInModificaInfoDto = getDatiGeneraliImpegnoInAggiornamento((Accertamento)impegnoDaAggiornare, datiOperazione, bilancio);
		
			impegnoInModificaInfoDto.setDoppiaGestione(fromDoppiaGestione);
		
			if(subMovgestInModificaValutati==null){
				impegnoInModificaInfoDto = valutaSubImp(impegnoDaAggiornare, impegnoInModificaInfoDto, datiOperazione, bilancio);
			} else {
				impegnoInModificaInfoDto.setInfoSubValutati(subMovgestInModificaValutati);
			}
			
			HashMap<Integer, CapitoloEntrataGestione> capitoliEntrata = capitoliInfo.getCapitoliDaServizioEntrata();
			
			List<Errore> listaErrori = this.controlliDiMeritoAggiornamentoAccertamentoOperazioneInterna(richiedente,  ente, bilancio, (Accertamento) impegnoDaAggiornare,
					datiOperazione, impegnoInModificaInfoDto,capitoliEntrata);
			if(listaErrori!=null && listaErrori.size()>0){
				esito.setListaErrori(listaErrori);
				return esito;
			}
	
			
			esito = this.aggiornaImpegno(richiedente, ente, bilancio, impegnoDaAggiornare, soggettoCreditore, unitaElemDiGest,datiOperazione,
					impegnoInModificaInfoDto,capitoliInfo);
				
			//Termino restituendo l'oggetto di ritorno: 
			return esito;
		}*/
		
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

@Deprecated
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ImpegnoDad extends MovimentoGestioneDad<Impegno, SubImpegno> {

	
	
	/**
	 * Wrapper di retro compatibilita'
	 */
	@Override
	protected Impegno convertiMovimentoGestione(SiacTMovgestFin siacTMovgest){
		return convertiMovimentoGestione(siacTMovgest,null);
	}
	
	@Override
	protected Impegno convertiMovimentoGestione(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
		// mappo solo i dati minimi dell'impegno, anno, codice e descrizione
		Impegno impegnoTrovato = map(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno);	
		impegnoTrovato = EntityToModelConverter.siacTMovgestEntityToImpegnoModel(siacTMovgest, impegnoTrovato,ottimizzazioneDto);	
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoTrovato;
	}
	
	@Override
	protected Impegno convertiMovimentoGestioneOPT(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
		Impegno impegnoTrovato = map(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno);	
		impegnoTrovato = EntityToModelConverter.siacTMovgestEntityToImpegnoModel(siacTMovgest, impegnoTrovato,ottimizzazioneDto);	
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoTrovato;
	}
	
	/**
	 * wrapper di retro compatibilita'
	 */
	@Override
	protected SubImpegno convertiSubMovimento(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest, boolean caricaDatiUlteriori){
		return convertiSubMovimento(siacTMovgestTs, siacTMovgest, caricaDatiUlteriori,null);
	}
	
	@Override
	protected SubImpegno convertiSubMovimento(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest, boolean caricaDatiUlteriori,OttimizzazioneMovGestDto ottimizzazioneDto){
		SubImpegno subImpegnoTrovato = map(siacTMovgestTs, SubImpegno.class, FinMapId.SiacTMovgestTs_SubImpegno);
		subImpegnoTrovato = EntityToModelConverter.siacTMovgestTsEntityToSubImpegnoModel(siacTMovgestTs, subImpegnoTrovato,siacTMovgest, caricaDatiUlteriori,ottimizzazioneDto);
		//Termino restituendo l'oggetto di ritorno: 
        return subImpegnoTrovato;
	}

	@Override
	protected SubImpegno convertiSubMovimentoOPT(SiacTMovgestTsFin siacTMovgestTs, SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto){
		SubImpegno subImpegnoTrovato = map(siacTMovgestTs, SubImpegno.class, FinMapId.SiacTMovgestTs_SubImpegno);
		subImpegnoTrovato = EntityToModelConverter.siacTMovgestTsEntityToSubImpegnoModel(siacTMovgestTs, subImpegnoTrovato,siacTMovgest, false,ottimizzazioneDto);
		//Termino restituendo l'oggetto di ritorno: 
        return subImpegnoTrovato;
	}

	@Override
	protected boolean checkStato(String stato) {
		return Constanti.MOVGEST_STATO_DEFINITIVO.equals(stato) || Constanti.MOVGEST_STATO_DEFINITIVO_NON_LIQUIDABILE.equals(stato);
	}
	
	
	/**
	 * Operazione interna di doppia gestione
	 * 
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param primoImpegnoDaInserire
	 * @param datiOperazione
	 * @return EsitoOperazioneInserimentoMovgestDoppiaGestioneDto
	 */
	/*@Override
	public EsitoOperazioneInserimentoMovgestDoppiaGestioneDto operazioneInternaInserisceMovimento(Richiedente richiedente, Ente ente, Bilancio bilancio, MovimentoGestione primoImpegnoDaInserire, DatiOperazioneDto datiOperazione,Integer forzaMaxCodePerDoppia){
		
		EsitoOperazioneInserimentoMovgestDoppiaGestioneDto esito = new EsitoOperazioneInserimentoMovgestDoppiaGestioneDto();
		
		EsitoControlliInserimentoMovimentoDto esitoControlli = 	this.controlliDiMeritoInserimentoImpegnoOperazioneInterna(richiedente,  ente, bilancio, (Impegno) primoImpegnoDaInserire, datiOperazione);
		// setto eventuali errori
		List<Errore> listaErrori = esitoControlli.getListaErrori();
		esito.setListaErrori(listaErrori);
		esito.addWarning(esitoControlli.getListaWarning());
		
		
		// se non ci sono errori procedo con inserimento
		if(listaErrori==null || listaErrori.size()==0){
		
			Impegno impegno = (Impegno) this.inserisceImpegno(richiedente, ente, bilancio, primoImpegnoDaInserire,datiOperazione,forzaMaxCodePerDoppia);
			
			// setto impegno inserito
			esito.setMovimentoGestione(impegno);
		}
		//Termino restituendo l'oggetto di ritorno: 
        return esito;
		
	}*/
	
	/**
	 * Operazione interna aggiorna impegno
	 * 
	 * @param richiedente
	 * @param ente
	 * @param bilancio
	 * @param impegnoDaAggiornare
	 * @param soggettoCreditore
	 * @param unitaElemDiGest
	 * @param datiOperazione
	 * @param impegnoInModificaInfoDto
	 * @return EsitoAggiornamentoMovimentoGestioneDto
	 */
	/*@Override	
	public EsitoAggiornamentoMovimentoGestioneDto operazioneInternaAggiornaMovGest(Richiedente richiedente, Ente ente, Bilancio bilancio,
			MovimentoGestione impegnoDaAggiornare, Soggetto soggettoCreditore, UnitaElementareGestione unitaElemDiGest,
			DatiOperazioneDto datiOperazione,SubMovgestInModificaInfoDto subMovgestInModificaValutati,boolean fromDoppiaGestione,CapitoliInfoDto capitoliInfo){
		
		EsitoAggiornamentoMovimentoGestioneDto esito=new EsitoAggiornamentoMovimentoGestioneDto();
		
		//carica i dati dell'impegno
		ImpegnoInModificaInfoDto impegnoInModificaInfoDto = getDatiGeneraliImpegnoInAggiornamento(impegnoDaAggiornare, datiOperazione, bilancio);
		
		impegnoInModificaInfoDto.setDoppiaGestione(fromDoppiaGestione);
		
		if(subMovgestInModificaValutati==null || impegnoInModificaInfoDto==null){
			impegnoInModificaInfoDto = valutaSubImp(impegnoDaAggiornare, impegnoInModificaInfoDto, datiOperazione, bilancio);
		} else {
			impegnoInModificaInfoDto.setInfoSubValutati(subMovgestInModificaValutati);
		}
		
		HashMap<Integer, CapitoloUscitaGestione> capitoliUscita = capitoliInfo.getCapitoliDaServizioUscita();
		// controlli per aggiornamento
		EsitoControlliAggiornamentoMovimentoDto esitoControlli = this.controlliDiMeritoAggiornamentoImpegnoOperazioneInterna(richiedente,  ente, bilancio, (Impegno) impegnoDaAggiornare,
				datiOperazione, impegnoInModificaInfoDto,capitoliUscita);
		// setto eventuali errori o warning:
		List<Errore> listaErrori = esitoControlli.getListaErrori();
		esito.setListaErrori(listaErrori);
		esito.addWarning(esitoControlli.getListaWarning());
		
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

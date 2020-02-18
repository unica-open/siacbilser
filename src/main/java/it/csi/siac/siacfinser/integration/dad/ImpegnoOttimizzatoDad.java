/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.integration.dao.common.dto.OttimizzazioneMovGestDto;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacRLiquidazioneStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTLiquidazioneFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.util.EntityToModelConverter;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ImpegnoOttimizzatoDad extends MovimentoGestioneOttimizzatoDad<Impegno, SubImpegno> {

	/**
	 * Wrapper di retro compatibilita'
	 */
	@Override
	protected Impegno convertiMovimentoGestione(SiacTMovgestFin siacTMovgest){
		return convertiMovimentoGestione(siacTMovgest,null);
	}
	
	
	@Override
	protected Impegno convertiMovimentoGestione(SiacTMovgestFin siacTMovgest,OttimizzazioneMovGestDto ottimizzazioneDto,boolean ottimizzatoCompletamenteDaChiamante){
		// mappo solo i dati minimi dell'impegno, anno, codice e descrizione
		Impegno impegnoTrovato = map(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno);	
		impegnoTrovato = EntityToModelConverter.siacTMovgestEntityToImpegnoModel(siacTMovgest, impegnoTrovato,ottimizzazioneDto, ottimizzatoCompletamenteDaChiamante);	
		//Termino restituendo l'oggetto di ritorno: 
        return impegnoTrovato;
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
	 * Ricerco le eventuali liquidazioni collegate al movimento
	 * @param uidMovimetno
	 * @return
	 */
	public Boolean ricercaLiquidazioniByMovimentoGestione(Integer idMovimento, Integer idEnte){
		
		Boolean movimentoConLiquidazioni = Boolean.FALSE;
		
		// Devo cercare prima il movimento, perche' non e' detto sia di tipo Testata
		Timestamp now = new Timestamp(System.currentTimeMillis());
		List<SiacTMovgestTsFin> siacTMovgestTs = siacTMovgestTsRepository.findMovgestTsByMovgest(idEnte, now, idMovimento);
		if(siacTMovgestTs==null || siacTMovgestTs.isEmpty()){
			//l'uid potrebbe essere di un sub, ricerco
			siacTMovgestTs = siacTMovgestTsRepository.findSubMovgestTsByMovgest(idEnte, now, idMovimento);
		}
		
		// Qui screma gia' sulle R valide, basta anche un solo legame valido 
		
		List<Integer> idsRLiqMovgest = movimentoGestioneDao.getIdsSiacRLiquidazioneMovgestFinByMovGestTsMassive(siacTMovgestTs);
		
		//List<SiacRLiquidazioneMovgestFin> listaSiacRLiquidazioneMovgest = movimentoGestioneDao.ricercaByMovGestTsMassive(siacTMovgestTs,"SiacRLiquidazioneMovgestFin");
		
		if(idsRLiqMovgest!=null && idsRLiqMovgest.size()>0){
			
			for (Integer idRIt : idsRLiqMovgest) {
				
				SiacRLiquidazioneMovgestFin rLiquidazioneMovgest = siacRLiquidazioneMovgestRepository.findOne(idRIt);
				
				SiacTLiquidazioneFin tLiquidazione = rLiquidazioneMovgest.getSiacTLiquidazione();
				
				List<SiacRLiquidazioneStatoFin> siacRLiquidazioneStatos= tLiquidazione.getSiacRLiquidazioneStatos();
				
				for (SiacRLiquidazioneStatoFin rliquidazioneStato : siacRLiquidazioneStatos) {
					if(rliquidazioneStato.getDataFineValidita() == null && !rliquidazioneStato.getSiacDLiquidazioneStato().getLiqStatoCode().equalsIgnoreCase(Constanti.D_LIQUIDAZIONE_STATO_ANNULLATO)){
						movimentoConLiquidazioni = true;
						break;
					}
				}
				if(movimentoConLiquidazioni){
					break;
				}
				
			}
		}	
		
		return movimentoConLiquidazioni; 
	}
	
	
}

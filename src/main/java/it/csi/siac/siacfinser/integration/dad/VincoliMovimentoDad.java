/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.VincoliAccertamentiDao;
import it.csi.siac.siacfinser.integration.dao.movgest.VincoliImpegniDao;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;

public abstract class VincoliMovimentoDad extends ExtendedBaseFinDad {
	
	@Autowired
	protected SiacTMovgestRepository siacTMovgestRepository;
	
	@Autowired 
	protected VincoliImpegniDao vincoliImpegni; 

	@Autowired 
	protected VincoliAccertamentiDao vincoliAccertamenti;
	

	// -------------------------------------------------------------- COMMON -------------------------------------------------------------- 
	
	protected Object[] caricaMovimentoAnnualitaDiversaDaBilancio(Integer annoEsercizio, Integer uidEnte, Integer annoMovimento, 
			Integer numeroMovimento, String tipoMovimento, String numeroCapitolo, String numeroArticolo, Object[] result){
		
		Object[] estremi = vincoliAccertamenti.extractEstremiMovimentoPadre(
				annoEsercizio, uidEnte, annoMovimento, numeroMovimento, tipoMovimento,
				numeroCapitolo, numeroArticolo);
		
		if(estremi == null || estremi.length == 0) {
			return result;
		}
		
		annoEsercizio--;
		
		return caricaMovimentoAnnualitaDiversaDaBilancio(annoEsercizio, uidEnte, 
				new Integer(estremi[3].toString()), 
				new Integer(estremi[4].toString()), 
				tipoMovimento,
				numeroCapitolo,
				numeroArticolo,
				estremi
			);
	}

	public Object[] caricaMovimentoCatenaRiacceramentoReanno(Integer annoEsercizio, Integer uidEnte, Integer annoMovimento, Integer numeroMovimento, String tipoMovimento, Object[] result){
		Object[] estremi = vincoliAccertamenti.extractEstremiRiaccertamento(annoEsercizio, uidEnte, annoMovimento, numeroMovimento, tipoMovimento);
		
		if(estremi == null || estremi.length == 0) {
			return result;
		}
		
		if(!"S".equals(estremi[1].toString())) {
			return estremi;
		}
		
		//task-110
		return caricaMovimentoCatenaRiacceramentoReanno(
				new Integer(estremi[4].toString()), 
				uidEnte, 
				new Integer(estremi[2].toString()), 
				new Integer(estremi[3].toString()), 
				tipoMovimento,
				estremi
			);
	}
	
	protected SiacTMovgestFin findSiacTMovgestFromEstremi(Integer uidEnteProprietario, String tipoMovimentoGestione, String annoBilancio, Integer annoMovimento, BigDecimal numeroMovimento) {
		//task-110
		return siacTMovgestRepository.ricercaSiacTMovgestPkWithCap(uidEnteProprietario, 
				annoBilancio,
				annoMovimento,
				numeroMovimento,
				tipoMovimentoGestione
			);
	}
	
}

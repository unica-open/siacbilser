/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDModificaStatoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDModificaTipoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacRMovgestAggiudicazioneFinRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTModificaRepository;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;

@Component
@Transactional
public class ModificaMovimentoGestioneDad extends AbstractFinDad {

	@Autowired
	private SiacDModificaStatoRepository siacDModificaStatoRepository;
	
	@Autowired
	private SiacDModificaTipoRepository siacDModificaTipoRepository;
	
	@Autowired
	private SiacTModificaRepository siacTModificaRepository;
	

	@Autowired
	private SiacRMovgestAggiudicazioneFinRepository siacRMovgestAggiudicazioneRepository;
	
	/**
	 * Gets the importo attuale modifica.
	 *
	 * @param modifica the modifica
	 * @return the importo attuale modifica
	 */
	public BigDecimal getImportoAttualeModifica(ModificaMovimentoGestione modifica) {;
		return siacTModificaRepository.findMovgestTsDetImportoModByModIdAndMovgestTsDetTipoCode(modifica.getUid(), CostantiFin.MOVGEST_TS_DET_TIPO_ATTUALE);
	}

	/**
	 * Gets the tipo modifica.
	 *
	 * @param modificaMovimentoGestioneSpesa the modifica movimento gestione spesa
	 * @return the tipo modifica
	 */
	public String getTipoModifica(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa) {
		return siacTModificaRepository.findMovgestTsDetImportoModByModIdAndMovgestTsDetTipoCode(modificaMovimentoGestioneSpesa.getUid());
	}

	/**
	 * Determina stato operativo provvedimento collegato.
	 *
	 * @param modificaMovimentoGestioneSpesa the modifica movimento gestione spesa
	 * @return the string
	 */
	public String determinaStatoOperativoProvvedimentoCollegato(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa) {
		return siacTModificaRepository.findProvvedimentoStatoCode(modificaMovimentoGestioneSpesa.getUid());
	}

	public List<String> getChiaviLogicheMovimentoGestioneAggiudicazioneDaModifica(ModificaMovimentoGestioneSpesa mod) {
		
		List<SiacTMovgestFin> siacTMovgestFinCollegati = siacRMovgestAggiudicazioneRepository.findSiacTMovgestAValidiByModId(mod.getUid());
		if(siacTMovgestFinCollegati == null || siacTMovgestFinCollegati.isEmpty()) {
			return null;
		}
		
		List<String> chiaviMovgestCollegati = new ArrayList<String>();
		for (SiacTMovgestFin siacTMovgestFin : siacTMovgestFinCollegati) {
			String anno = siacTMovgestFin.getMovgestAnno() != null? siacTMovgestFin.getMovgestAnno().toString() : "null";
			String numero = siacTMovgestFin.getMovgestNumero() != null? Integer.valueOf(siacTMovgestFin.getMovgestNumero().intValue()).toString() : "null";
			String chiaveMovgest =  anno + " / " + numero;
			chiaviMovgestCollegati.add(chiaveMovgest);
		}
		return chiaviMovgestCollegati;
	}
	
	
}
			

/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.entitymapping.converter.base.ConvertersFin;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class VincoliAccertamentoDad extends VincoliMovimentoDad {

	// -------------------------------------------------------------- ACCERTAMENTO -------------------------------------------------------------- 
	
	public List<VincoloAccertamento> cercaVincoliAccertamento(Accertamento accertamento, Ente ente, ModelDetailEnum[] modelDetails){
		List<SiacRMovgestTsFin> listSiacRMovgestTsFin = vincoliAccertamenti.findSiacRMovgestTsA(accertamento.getUid(), ente.getUid());

		return convertiLista(listSiacRMovgestTsFin, VincoloAccertamento.class, FinMapId.VincoloAccertamento_SiacRMovgestTsFin_Base_ModelDetail, ConvertersFin.byModelDetails(modelDetails));
	}

	public List<VincoloAccertamento> cercaVincoliAccertamentoByAnnoBilancio(Accertamento accertamento, Integer annobilancio, Ente ente, ModelDetailEnum[] modelDetails){
		List<SiacRMovgestTsFin> listSiacRMovgestTsFin = vincoliAccertamenti.findSiacRMovgestTsAByAnno(accertamento.getUid(), annobilancio, ente.getUid());
		
		return convertiLista(listSiacRMovgestTsFin, VincoloAccertamento.class, FinMapId.VincoloAccertamento_SiacRMovgestTsFin_Base_ModelDetail, ConvertersFin.byModelDetails(modelDetails));
	}
	
	public BigDecimal calcolaSommaModificheReimpReannoAssociateAdAccertamento(Accertamento accertamento, Ente ente) {
		return vincoliAccertamenti.sumImportoDeltaSiacRModificaVincoloByUidAccertamento(accertamento.getUid(), ente.getUid());
	}

	public BigDecimal calcolaSommaModificheReimpReannoAssociateAdAccertamentoFunction(Accertamento accertamento, Ente ente) {
		return vincoliAccertamenti.extractImportoModificheReimpReanno(accertamento.getUid(), ente.getUid());
	}
	
	public Accertamento caricaPrimoAccertamentoCatenaRiaccertamentoReanno(Integer annoEsercizio, Integer uidEnteProprietario, Accertamento acc) {
		Accertamento accertamento = null;
		
		if(annoEsercizio != null && acc != null && acc.getAnnoMovimento() != 0 && acc.getNumeroBigDecimal() != null && uidEnteProprietario != null 
				&& StringUtils.isNotBlank(acc.getTipoMovimento())) {
			
			Object[] estremi = caricaMovimentoCatenaRiacceramentoReanno(annoEsercizio, uidEnteProprietario, 
						acc.getAnnoMovimento(), 
						acc.getNumeroBigDecimal().intValue(), 
						acc.getTipoMovimento(), 
						null
					);
			
			if(estremi == null || estremi.length == 0) return acc;
			
			SiacTMovgestFin siacTMovgest = findSiacTMovgestFromEstremi(uidEnteProprietario, acc.getTipoMovimento(), 
					estremi[4].toString(), new Integer(estremi[2].toString()), new BigDecimal(estremi[3].toString()));
			accertamento = mapNotNull(siacTMovgest, Accertamento.class, FinMapId.SiacTMovgest_Accertamento_Capitolo_Bilancio);
		}
		
		return accertamento == null ? acc : accertamento;
	}
	
	public Accertamento caricaAccertamentoPadreResiduo(Accertamento acc, Integer annoEsercizio, Integer uidEnteProprietario) {
		Accertamento accertamento = null;
		
		if(annoEsercizio != null && uidEnteProprietario != null && acc != null && acc.getAnnoMovimento() != 0 && acc.getNumeroBigDecimal() != null 
				&& StringUtils.isNotBlank(acc.getTipoMovimento()) && acc.getCapitoloEntrataGestione() != null) {

			Object[] estremi = caricaMovimentoAnnualitaDiversaDaBilancio(
						annoEsercizio, 
						uidEnteProprietario, 
						acc.getAnnoMovimento(), 
						acc.getNumeroBigDecimal().intValue(), 
						acc.getTipoMovimento(), 
						acc.getCapitoloEntrataGestione().getNumeroCapitolo().toString(), 
						acc.getCapitoloEntrataGestione().getNumeroArticolo().toString(), 
						null
					);
			
			if(estremi == null || estremi.length == 0) return acc;

			SiacTMovgestFin siacTMovgest = findSiacTMovgestFromEstremi(uidEnteProprietario, acc.getTipoMovimento(), 
					estremi[2].toString(), new Integer(estremi[3].toString()), new BigDecimal(estremi[4].toString()));
			accertamento = mapNotNull(siacTMovgest, Accertamento.class, FinMapId.SiacTMovgest_Accertamento_Capitolo_Bilancio);
		}
		
		return accertamento == null ? acc : accertamento;
	}

	public Accertamento caricaAccertamentoPadrePluriennale(Accertamento acc, Integer annoEsercizio, Integer uidEnteProprietario) {
		Accertamento accertamento = null;
		
		if(annoEsercizio != null && uidEnteProprietario != null && acc != null && acc.getAnnoMovimento() != 0 
				&& acc.getNumeroBigDecimal() != null && StringUtils.isNotBlank(acc.getTipoMovimento()) && acc.getCapitoloEntrataGestione() != null) {

			Object[] estremi = caricaMovimentoAnnualitaDiversaDaBilancio(
						annoEsercizio, 
						uidEnteProprietario, 
						acc.getAnnoMovimento(), 
						acc.getNumeroBigDecimal().intValue(), 
						acc.getTipoMovimento(), 
						acc.getCapitoloEntrataGestione().getNumeroCapitolo().toString(), 
						acc.getCapitoloEntrataGestione().getNumeroArticolo().toString(), 
						null
					);
			
			if(estremi == null || estremi.length == 0) return acc;

			SiacTMovgestFin siacTMovgest = findSiacTMovgestFromEstremi(uidEnteProprietario, acc.getTipoMovimento(), 
					estremi[2].toString(), new Integer(estremi[3].toString()), new BigDecimal(estremi[4].toString()));
			accertamento = mapNotNull(siacTMovgest, Accertamento.class, FinMapId.SiacTMovgest_Accertamento_Capitolo_Bilancio);
		}
		
		return accertamento == null ? acc : accertamento;
	}	
	
}

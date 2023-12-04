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
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class VincoliImpegnoDad extends VincoliMovimentoDad {

	// -------------------------------------------------------------- IMPEGNO -------------------------------------------------------------- 
	
	public List<VincoloImpegno> cercaVincoliImpegno(Impegno impegno, Ente ente, ModelDetailEnum[] modelDetails){
		List<SiacRMovgestTsFin> listSiacRMovgestTsFin = vincoliImpegni.findSiacRMovgestTsB(impegno.getUid(), ente.getUid());
		
		return convertiLista(listSiacRMovgestTsFin, VincoloImpegno.class, FinMapId.VincoloImpegno_SiacRMovgestTsFin_Base_ModelDetail, ConvertersFin.byModelDetails(modelDetails));
	}

	public List<VincoloImpegno> cercaVincoliImpegnoByAnnoBilancio(Impegno impegno, Integer annobilancio, Ente ente, ModelDetailEnum[] modelDetails){
		List<SiacRMovgestTsFin> listSiacRMovgestTsFin = vincoliImpegni.findSiacRMovgestTsBByAnno(impegno.getUid(), annobilancio, ente.getUid());
		
		return convertiLista(listSiacRMovgestTsFin, VincoloImpegno.class, FinMapId.VincoloImpegno_SiacRMovgestTsFin_Base_ModelDetail, ConvertersFin.byModelDetails(modelDetails));
	}

	/*public BigDecimal calcolaSommaModificheReimpReannoAssociateAdImpegno(Impegno impegno, Ente ente) {
		return vincoliImpegni.sumImportoDeltaSiacRModificaVincoloByUidImpegno(impegno.getUid(), ente.getUid());
	}*/

	public BigDecimal calcolaSommaModificheReimpReannoAssociateAdImpegnoFunction(Impegno impegno, Ente ente) {
		return vincoliImpegni.extractImportoModificheReimpReanno(impegno.getUid(), ente.getUid());
	}
	
	public Impegno caricaPrimoImpegnoCatenaRiaccertamentoReanno(Integer annoEsercizio, Integer uidEnteProprietario, Impegno imp) {
		Impegno impegno = null;
		
		if(annoEsercizio != null && imp != null && imp.getAnnoMovimento() != 0 && imp.getNumeroBigDecimal() != null && uidEnteProprietario != null 
				&& StringUtils.isNotBlank(imp.getTipoMovimento())) {
			
			Object[] estremi = caricaMovimentoCatenaRiacceramentoReanno(annoEsercizio, uidEnteProprietario, 
						imp.getAnnoMovimento(), 
						imp.getNumeroBigDecimal().intValue(), 
						imp.getTipoMovimento(), 
						null
					);
			
			if(estremi == null || estremi.length == 0) return imp;
			
			SiacTMovgestFin siacTMovgest = findSiacTMovgestFromEstremi(uidEnteProprietario, imp.getTipoMovimento(), 
					estremi[4].toString(), new Integer(estremi[2].toString()), new BigDecimal(estremi[3].toString()));
			impegno = mapNotNull(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno_Capitolo_Bilancio);
		}
		
		return impegno == null ? imp : impegno;
	}

	public Impegno caricaImpegnoPadreResiduo(Impegno imp, Integer annoEsercizio, Integer uidEnteProprietario) {
		Impegno impegno = null;
		
		if(annoEsercizio != null && uidEnteProprietario != null && imp != null && imp.getAnnoMovimento() != 0 && imp.getNumeroBigDecimal() != null 
				&& StringUtils.isNotBlank(imp.getTipoMovimento()) && imp.getCapitoloUscitaGestione() != null) {

			Object[] estremi = caricaMovimentoAnnualitaDiversaDaBilancio(
						annoEsercizio, 
						uidEnteProprietario, 
						imp.getAnnoMovimento(), 
						imp.getNumeroBigDecimal().intValue(), 
						imp.getTipoMovimento(), 
						imp.getCapitoloUscitaGestione().getNumeroCapitolo().toString(), 
						imp.getCapitoloUscitaGestione().getNumeroArticolo().toString(), 
						null
					);
			
			if(estremi == null || estremi.length == 0) return imp;
			
			SiacTMovgestFin siacTMovgest = findSiacTMovgestFromEstremi(uidEnteProprietario, imp.getTipoMovimento(), 
					estremi[2].toString(), new Integer(estremi[3].toString()), new BigDecimal(estremi[4].toString()));
			impegno = mapNotNull(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno_Capitolo_Bilancio);
		}
		
		return impegno == null ? imp : impegno;
	}

	public Impegno caricaImpegnoPadrePluriennale(Impegno imp, Integer annoEsercizio, Integer uidEnteProprietario) {
		Impegno impegno = null;
		
		if(annoEsercizio != null && uidEnteProprietario != null && imp != null && imp.getAnnoMovimento() != 0 && imp.getNumeroBigDecimal() != null 
				&& StringUtils.isNotBlank(imp.getTipoMovimento()) && imp.getCapitoloUscitaGestione() != null) {
			
			Object[] estremi = caricaMovimentoAnnualitaDiversaDaBilancio(
						annoEsercizio, 
						uidEnteProprietario, 
						imp.getAnnoMovimento(), 
						imp.getNumeroBigDecimal().intValue(), 
						imp.getTipoMovimento(), 
						imp.getCapitoloUscitaGestione().getNumeroCapitolo().toString(), 
						imp.getCapitoloUscitaGestione().getNumeroArticolo().toString(), 
						null
					);
			
			if(estremi == null || estremi.length == 0) return imp;

			SiacTMovgestFin siacTMovgest = findSiacTMovgestFromEstremi(uidEnteProprietario, imp.getTipoMovimento(), 
					estremi[2].toString(), new Integer(estremi[3].toString()), new BigDecimal(estremi[4].toString()));
			impegno = mapNotNull(siacTMovgest, Impegno.class, FinMapId.SiacTMovgest_Impegno_Capitolo_Bilancio);
		}
		
		return impegno == null ? imp : impegno;
	}
	
}

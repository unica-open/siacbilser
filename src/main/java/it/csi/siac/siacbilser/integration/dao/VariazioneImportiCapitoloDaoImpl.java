/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClassVar;
import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetVar;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemVar;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneStatoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTBilElemDetVarElemDetFlagEnum;

/**
 * The Class VariazioneImportiCapitoloDaoImpl.
 */
@Component
@Transactional
public class VariazioneImportiCapitoloDaoImpl extends ExtendedJpaDao<SiacTVariazione, Integer> implements VariazioneImportiCapitoloDao{

	/**
	 * Instantiates a new variazione importi capitolo dao impl.
	 */
	public VariazioneImportiCapitoloDaoImpl() {
		
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.VariazioneImportiCapitoloDao#create(it.csi.siac.siacbilser.integration.entity.SiacTVariazione)
	 */
	@Override
	public SiacTVariazione create(SiacTVariazione var) {
		Date now = new Date();
		var.setVariazioneId(null);
		var.setDataModificaInserimento(now);		
		
		if(var.getSiacRVariazioneStatos()!=null){
			for(SiacRVariazioneStato stato : var.getSiacRVariazioneStatos()){
				stato.setDataModificaInserimento(now);
				
				if(stato.getSiacTBilElemDetVars()!=null){
					for (SiacTBilElemDetVar dettVar : stato.getSiacTBilElemDetVars()) {
						dettVar.setDataModificaInserimento(now);
					}
				}
				
				if(stato.getSiacRBilElemClassVars()!=null){
					for (SiacRBilElemClassVar dettVar : stato.getSiacRBilElemClassVars()) {
						dettVar.setDataModificaInserimento(now);
					}
				}
				
				if(stato.getSiacTBilElemVars()!=null){
					for (SiacTBilElemVar dettVar : stato.getSiacTBilElemVars()) {
						dettVar.setDataModificaInserimento(now);
					}
				}
				
			}
			
		}
		
		if(var.getSiacRVariazioneAttrs()!=null){
			for(SiacRVariazioneAttr attr: var.getSiacRVariazioneAttrs()){
				attr.setDataModificaInserimento(now);
			}
		}
				
		var.setUid(null);
		
		super.save(var);
		entityManager.flush();
		return var;
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	@Override
	public SiacTVariazione update(SiacTVariazione var) {
		SiacTVariazione varAttuale = this.findById(var.getUid());
		
		Date now = new Date();
		var.setDataModificaAggiornamento(now);
		
		//Cancello il vecchio stato e i vecchi importi di variazione associati
		if(varAttuale.getSiacRVariazioneStatos()!=null){
			for(SiacRVariazioneStato stato : varAttuale.getSiacRVariazioneStatos()){
				stato.setDataCancellazioneIfNotSet(now);
				
				if(stato.getSiacTBilElemDetVars()!=null){
					for (SiacTBilElemDetVar dettVar : stato.getSiacTBilElemDetVars()) {
						dettVar.setDataCancellazioneIfNotSet(now);						
					}
				}
				
				if(stato.getSiacRBilElemClassVars()!=null){
					for (SiacRBilElemClassVar dettVar : stato.getSiacRBilElemClassVars()) {
						dettVar.setDataCancellazioneIfNotSet(now);
					}
				}
				
				if(stato.getSiacTBilElemVars()!=null){
					for (SiacTBilElemVar dettVar : stato.getSiacTBilElemVars()) {
						dettVar.setDataCancellazioneIfNotSet(now);
					}
				}
				
				
				
			}
		}
		//Cancello i vecchi attributi associati
		if(varAttuale.getSiacRVariazioneAttrs()!=null){
			for(SiacRVariazioneAttr attr: varAttuale.getSiacRVariazioneAttrs()){
				attr.setDataCancellazioneIfNotSet(now);
			}
		}
		
		
		//Inserisco il nuovo stato e i nuovi importi di variazione associati
		if(var.getSiacRVariazioneStatos()!=null){
			for(SiacRVariazioneStato stato : var.getSiacRVariazioneStatos()){
				stato.setDataModificaInserimento(now);
				
				if(stato.getSiacTBilElemDetVars()!=null){
					for (SiacTBilElemDetVar dettVar : stato.getSiacTBilElemDetVars()) {
						dettVar.setDataModificaInserimento(now);						
					}
				}
				
				if(stato.getSiacRBilElemClassVars()!=null){
					for (SiacRBilElemClassVar dettVar : stato.getSiacRBilElemClassVars()) {
						dettVar.setDataModificaInserimento(now);
					}
				}
				
				if(stato.getSiacTBilElemVars()!=null){
					for (SiacTBilElemVar dettVar : stato.getSiacTBilElemVars()) {
						dettVar.setDataModificaInserimento(now);
					}
				}
				
			}
		}
		
		//Inserisco i nuovi attributi
		if(var.getSiacRVariazioneAttrs()!=null){
			for(SiacRVariazioneAttr attr: var.getSiacRVariazioneAttrs()){
				attr.setDataModificaInserimento(now);
			}
		}
		
		super.update(var);
		
		return var;	
		
	}
	
	
	@Override
	public SiacTVariazione updateAnagrafica(SiacTVariazione var) {
		SiacTVariazione varAttuale = this.findById(var.getUid());
		
		Date now = new Date();
		var.setDataModificaAggiornamento(now);
		
		SiacRVariazioneStato statoAttuale = null;
		
		//Cancello il vecchio stato e i vecchi importi di variazione associati
		if(varAttuale.getSiacRVariazioneStatos()!=null){
			for(SiacRVariazioneStato stato : varAttuale.getSiacRVariazioneStatos()){
				if(stato.getDataCancellazione()==null){
					statoAttuale = stato;
				}
				stato.setDataCancellazioneIfNotSet(now);
				
				//Lascio invariati i capitoli attuali con relativi importi e le codifiche.
			}
		}
		//Cancello i vecchi attributi associati
		if(varAttuale.getSiacRVariazioneAttrs()!=null){
			for(SiacRVariazioneAttr attr: varAttuale.getSiacRVariazioneAttrs()){
				attr.setDataCancellazioneIfNotSet(now);
			}
		}
		
		
		//Inserisco il nuovo stato e re-imposto gli elementi della variazione precedente.
		if(var.getSiacRVariazioneStatos()!=null){
			for(SiacRVariazioneStato statoNuovo : var.getSiacRVariazioneStatos()){
				statoNuovo.setDataModificaInserimento(now);
				
				
				//Collego i dettagli importi capitolo attuali al nuovo stato
				statoNuovo.setSiacTBilElemDetVars(new ArrayList<SiacTBilElemDetVar>());
				if(statoAttuale.getSiacTBilElemDetVars()!=null){
					for (SiacTBilElemDetVar dettVarAttuale : statoAttuale.getSiacTBilElemDetVars()) {
						statoNuovo.addSiacTBilElemDetVar(dettVarAttuale);
					}
				}
				
				//Collego i classificatori attuali al nuovo stato (Non necessario per la variazione importi)
				statoNuovo.setSiacRBilElemClassVars(new ArrayList<SiacRBilElemClassVar>());
				if(statoAttuale.getSiacRBilElemClassVars()!=null){
					for (SiacRBilElemClassVar dettVarAttuale : statoAttuale.getSiacRBilElemClassVars()) {
						statoNuovo.addSiacRBilElemClassVar(dettVarAttuale);
					}
				}
				
				//Collego i capitoli attuali al nuovo stato (Non necessario per la variazione importi)
				statoNuovo.setSiacTBilElemVars(new ArrayList<SiacTBilElemVar>());
				if(statoAttuale.getSiacTBilElemVars()!=null){
					for (SiacTBilElemVar dettVarAttuale : statoAttuale.getSiacTBilElemVars()) {
						statoNuovo.addSiacTBilElemVar(dettVarAttuale);
					}
				}
				
			}
		}
		
		//Inserisco i nuovi attributi
		if(var.getSiacRVariazioneAttrs()!=null){
			for(SiacRVariazioneAttr attr: var.getSiacRVariazioneAttrs()){
				attr.setDataModificaInserimento(now);
			}
		}
		
		super.update(var);
		
		return var;	
		
	}
	
	


	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.VariazioneImportiCapitoloDao#ricercaSinteticaVariazioneDiBilancio(it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneTipoEnum, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneStatoEnum, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.util.List, java.util.List, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SiacTVariazione> ricercaSinteticaVariazioneDiBilancio(Collection<SiacDVariazioneTipoEnum> variazioneTipo, Integer enteProprietarioId, 
			String annoBilancio, Integer variazioneNum, String variazioneDesc, Date dataAperturaProposta, Date dataChiusuraProposta, Integer direzioneProponenteId, SiacDVariazioneStatoEnum variazioneStato,
			Integer attoAmmId,	Integer capitoloId, Integer capitoloCodificaId, Integer capitoloSorgenteId, Integer capitoloDestinazioneId, 
			List<SiacDBilElemTipoEnum> bilElemsTipo, List<SiacDBilElemTipoEnum> bilElemsCodificaTipo, String operatoreImporti, Integer attoAmmVarBilId, Integer attoAmmIdMultiple, boolean limitaRisultatiDefinitiveODecentrate, Pageable pageable) {
		
		StringBuilder  jpql = new StringBuilder();
//		jpql.append("SELECT var ");
		jpql.append("FROM   SiacTVariazione var ");
		jpql.append("WHERE  siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append("       AND siacTBil.siacTPeriodo.anno = :annoBilancio ");
		jpql.append("       AND var.dataCancellazione is NULL ");
		
				
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("annoBilancio", annoBilancio);
		
		
		if(variazioneTipo!=null && !variazioneTipo.isEmpty()){
			Collection<String> variazioneTipoCodes = new ArrayList<String>();
			for(SiacDVariazioneTipoEnum sdvte : variazioneTipo) {
				variazioneTipoCodes.add(sdvte.getCodice());
			}
			
			jpql.append("       AND siacDVariazioneTipo.variazioneTipoCode IN (:variazioneTipoCodes) ");
			param.put("variazioneTipoCodes", variazioneTipoCodes);
		}
		
		
		if(bilElemsTipo!=null && !bilElemsTipo.isEmpty()){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE ");
			jpql.append("       			  rvarstato.dataCancellazione IS NULL ");
			jpql.append("                     AND EXISTS (FROM rvarstato.siacTBilElemDetVars rvarstatoelemdet ");
			jpql.append("       			 			 WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("                                AND rvarstatoelemdet.siacTBilElem.siacDBilElemTipo.elemTipoCode IN ("+SiacDBilElemTipoEnum.getCodiciSingleQuoteCommaSeparated(bilElemsTipo)+") ");
			jpql.append(" 							  ) ) ) ");	
		}
		
		if(bilElemsCodificaTipo!=null && !bilElemsCodificaTipo.isEmpty()){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE ");
			jpql.append("       			  rvarstato.dataCancellazione IS NULL ");
			jpql.append("                     AND EXISTS (FROM rvarstato.siacRBilElemClassVars rvarstatoelemdet ");
			jpql.append("       			 			 WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("                                AND rvarstatoelemdet.siacTBilElem.siacDBilElemTipo.elemTipoCode IN ("+SiacDBilElemTipoEnum.getCodiciSingleQuoteCommaSeparated(bilElemsCodificaTipo)+") ");
			jpql.append(" 							  ) ) ) ");	
		}
			
		
		if(variazioneNum!=null && variazioneNum!=0){
			jpql.append("       AND variazioneNum = :variazioneNum ");
			param.put("variazioneNum", variazioneNum);
		}
		
		if(StringUtils.isNotBlank(variazioneDesc)){
			//jpql.append("       AND variazioneDesc = :variazioneDesc ");
			jpql.append("       AND ( "+Utility.toJpqlSearchLike("variazioneDesc", "CONCAT('%',:variazioneDesc,'%')" )+" ) ");
			param.put("variazioneDesc", variazioneDesc);
		}
		
		if(dataAperturaProposta!=null){
			
			jpql.append(" 		AND var.dataAperturaProposta  >= :dataAperturaProposta ");
			param.put("dataAperturaProposta", dataAperturaProposta);

			Calendar dataAperturaPropostaRange = Calendar.getInstance();
			dataAperturaPropostaRange.setTime(dataAperturaProposta);
			
			dataAperturaPropostaRange.set(Calendar.HOUR_OF_DAY,23);
			dataAperturaPropostaRange.set(Calendar.MINUTE,59);
			dataAperturaPropostaRange.set(Calendar.SECOND,59);
			dataAperturaPropostaRange.set(Calendar.MILLISECOND,0);

			jpql.append(" 		AND var.dataAperturaProposta  <= :dataAperturaPropostaRange ");
			param.put("dataAperturaPropostaRange", dataAperturaPropostaRange.getTime());
			
			
		}
		
		if(dataChiusuraProposta!=null){
			jpql.append(" 		AND var.dataChiusuraProposta  >= :dataChiusuraProposta ");
			param.put("dataChiusuraProposta", dataChiusuraProposta);

			Calendar dataChiusuraPropostaRange = Calendar.getInstance();
			dataChiusuraPropostaRange.setTime(dataChiusuraProposta);
			
			dataChiusuraPropostaRange.set(Calendar.HOUR_OF_DAY,23);
			dataChiusuraPropostaRange.set(Calendar.MINUTE,59);
			dataChiusuraPropostaRange.set(Calendar.SECOND,59);
			dataChiusuraPropostaRange.set(Calendar.MILLISECOND,0);

			jpql.append(" 		AND var.dataChiusuraProposta  <= :dataChiusuraPropostaRange ");
			param.put("dataChiusuraPropostaRange", dataChiusuraPropostaRange.getTime());
		}
		
		if(direzioneProponenteId!=null){
			jpql.append("       AND siacTClass.classifId IN (:direzioneProponenteId) ");
			param.put("direzioneProponenteId", direzioneProponenteId);
		}
		
//		if(direzioneProponenteId!=null){
//			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE rvarstato.siacDVariazioneStato.variazioneStatoTipoCode = :variazioneStatoTipoCode ");
//			jpql.append(" 					AND rvarstato.dataCancellazione is NULL )) ");
//			param.put("variazioneStatoTipoCode", variazioneStato.getCodice());
//		}
		
		if(variazioneStato!=null){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE rvarstato.siacDVariazioneStato.variazioneStatoTipoCode = :variazioneStatoTipoCode ");
			jpql.append(" 					AND rvarstato.dataCancellazione is NULL )) ");
			param.put("variazioneStatoTipoCode", variazioneStato.getCodice());
		}
		
		if(attoAmmId!=null && attoAmmId!=0){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE rvarstato.siacTAttoAmm.attoammId = :attoAmmId ");
			jpql.append(" 					AND rvarstato.dataCancellazione is NULL )) ");
			param.put("attoAmmId", attoAmmId);
		}
		
		if(attoAmmVarBilId!=null && attoAmmVarBilId!=0){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE rvarstato.siacTAttoAmmVarbil.attoammId = :attoAmmId ");
			jpql.append(" 					AND rvarstato.dataCancellazione is NULL )) ");
			param.put("attoAmmId", attoAmmVarBilId);
		}
		
		//SIAC-8771-REGP
		if(limitaRisultatiDefinitiveODecentrate) {
			jpql.append("       AND EXISTS ( ");
			jpql.append("             FROM var.siacRVariazioneStatos rvarstato ");
			jpql.append("             WHERE rvarstato.siacDVariazioneStato.variazioneStatoTipoCode IN ");
			jpql.append(String.format("('%s', '%s')", SiacDVariazioneStatoEnum.CODICE_DEFINITIVA, SiacDVariazioneStatoEnum.CODICE_PRE_BOZZA));
			jpql.append(" 			  AND rvarstato.dataCancellazione is NULL ) ");
		}
		
		
		if(capitoloCodificaId!=null && capitoloCodificaId!=0){
			
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE ");
			jpql.append("       			  rvarstato.dataCancellazione IS NULL ");
			jpql.append("                     AND EXISTS (FROM rvarstato.siacRBilElemClassVars rvarstatoelemdet ");
			jpql.append("       			  		  WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("                             AND rvarstatoelemdet.siacTBilElem.elemId = :capitoloCodificaId  ) )) ");
				
			param.put("capitoloCodificaId", capitoloCodificaId);
		}
		
		
		if(capitoloId!=null && capitoloId!=0){
			
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM var.siacRVariazioneStatos rvarstato ");
			jpql.append("     WHERE rvarstato.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM rvarstato.siacTBilElemDetVars rvarstatoelemdet ");
			jpql.append("         WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("         AND rvarstatoelemdet.siacTBilElem.elemId = :capitoloId ");
			
			if(StringUtils.isNotBlank(operatoreImporti)) {
				jpql.append("         AND rvarstatoelemdet.elemDetImporto ").append(operatoreImporti).append(" 0 ");
			}
			
			jpql.append("     ) ");
			jpql.append(" ) ");
			
			param.put("capitoloId", capitoloId);
		}


		
		if(capitoloSorgenteId!=null && capitoloSorgenteId!=0){
			
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE ");
			jpql.append("       			  rvarstato.dataCancellazione IS NULL ");
			jpql.append("                     AND EXISTS (FROM rvarstato.siacTBilElemDetVars rvarstatoelemdet ");
			jpql.append("       			  		  WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("                             AND rvarstatoelemdet.siacTBilElem.elemId = :capitoloSorgenteId ");
			jpql.append("							  AND rvarstatoelemdet.elemDetFlag = '"+SiacTBilElemDetVarElemDetFlagEnum.Sorgente.getCodice()+"' ) )) ");
				
			param.put("capitoloSorgenteId", capitoloSorgenteId);
		}
		
		if(capitoloDestinazioneId!=null && capitoloDestinazioneId!=0){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE ");
			jpql.append("       			  rvarstato.dataCancellazione IS NULL ");
			jpql.append("                     AND EXISTS (FROM rvarstato.siacTBilElemDetVars rvarstatoelemdet ");
			jpql.append("       			  		  WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("                             AND rvarstatoelemdet.siacTBilElem.elemId = :capitoloDestinazioneId ");
			jpql.append(" 							  AND rvarstatoelemdet.elemDetFlag = '"+SiacTBilElemDetVarElemDetFlagEnum.Destinazione.getCodice()+"') )) ");
			
			param.put("capitoloDestinazioneId", capitoloDestinazioneId);
		}
		
		// SIAC-4815
		if(attoAmmIdMultiple != null && attoAmmIdMultiple.intValue() != 0) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM var.siacRVariazioneStatos rvarstato ")
				.append("     WHERE rvarstato.dataCancellazione is NULL ")
				.append("     AND ( ")
				.append("         rvarstato.siacTAttoAmm.attoammId = :attoAmmIdMult ")
				.append("         OR rvarstato.siacTAttoAmmVarbil.attoammId = :attoAmmIdMult ")
				.append("     ) ")
				.append(" ) ");
			param.put("attoAmmIdMult", attoAmmIdMultiple);
		}
		
		jpql.append(" ORDER BY variazioneNum ");
		
		
		return getPagedList(jpql.toString(), param, pageable);		
	}


	@Override
	public Page<SiacTBilElem> findCapitoliNellaVariazioneByUid(Integer variazioneId, Pageable pageable) {
		
		StringBuilder  jpql = new StringBuilder();
		Map<String,Object> param = new HashMap<String, Object>();
		jpql.append(" SELECT tbe ");
		jpql.append(" FROM  SiacTBilElem tbe, SiacDBilElemTipo dbet");
		jpql.append(" WHERE tbe.elemId IN ( SELECT DISTINCT bedv.siacTBilElem.elemId ");
		jpql.append(" 					FROM  SiacTBilElemDetVar bedv, SiacRVariazioneStato rvs");
		jpql.append(" 					WHERE bedv.dataCancellazione is NULL ");
		jpql.append("				    AND rvs.dataCancellazione is NULL ");
		jpql.append("					AND rvs = bedv.siacRVariazioneStato ");
		jpql.append("					AND rvs.siacTVariazione.variazioneId = :variazioneId )");
		jpql.append(" AND dbet = tbe.siacDBilElemTipo ");
		jpql.append(" ORDER BY dbet.elemTipoId, ");
		jpql.append(Utility.castToType("tbe.elemCode", "INTEGER")).append(", ");
		jpql.append(Utility.castToType("tbe.elemCode2", "INTEGER")).append(", ");
		jpql.append(Utility.castToType("tbe.elemCode3", "INTEGER"));
		
		param.put("variazioneId", variazioneId);
		
		return getPagedList(jpql.toString(), param, pageable);	
	}
	
	@Override
	public Long countDistinctClassifPadre(Integer variazioneId, String classifTipoCodePadre, String classifTipoCodeFiglio) {
		final String methodName = "countDistinctClassifMissione";
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		sb.append(" SELECT COALESCE(COUNT(DISTINCT (tc.classif_id)), 0) ")
			.append(" FROM siac_t_class tc ")
			.append(" JOIN siac_d_class_tipo dct ON dct.classif_tipo_id = tc.classif_tipo_id ")
			.append(" JOIN siac_r_class_fam_tree rcft ON (tc.classif_id = rcft.classif_id_padre AND rcft.data_cancellazione IS NULL) ")
			.append(" WHERE dct.classif_tipo_code = :classifTipoCodePadre ")
			.append(" AND rcft.classif_id IN ( ")
			.append("     SELECT tc1.classif_id ")
			.append("     FROM siac_t_variazione tv ")
			.append("     JOIN siac_r_variazione_stato rvs ON (rvs.variazione_id = tv.variazione_id AND rvs.data_cancellazione IS NULL) ")
			.append("     JOIN siac_t_bil_elem_det_var tbedv ON tbedv.variazione_stato_id = rvs.variazione_stato_id ")
			.append("     JOIN siac_r_bil_elem_class rbec ON (rbec.elem_id = tbedv.elem_id AND rbec.data_cancellazione IS NULL) ")
			.append("     JOIN siac_t_class tc1 ON tc1.classif_id = rbec.classif_id ")
			.append("     JOIN siac_d_class_tipo dct1 ON dct1.classif_tipo_id = tc1.classif_tipo_id ")
			.append("     WHERE tv.variazione_id = :variazioneId ")
			.append("     AND dct1.classif_tipo_code = :classifTipoCodeFiglio ")
			.append(" ) ");
		
		params.put("variazioneId", variazioneId);
		params.put("classifTipoCodePadre", classifTipoCodePadre);
		params.put("classifTipoCodeFiglio", classifTipoCodeFiglio);
		
		log.debug(methodName, "NATIVE SQL: " + sb.toString());
		
		Query query = createNativeQuery(sb.toString(), params);
		long res = ((Number) query.getSingleResult()).longValue();
		
		return Long.valueOf(res);
	}
	
	@Override
	public Long countDistinctClassifNonno(Integer variazioneId, String classifTipoCodeNonno, String classifTipoCodePadre, String classifTipoCodeFiglio) {
		final String methodName = "countDistinctClassifMissione";
		StringBuilder sb = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		sb.append(" SELECT COALESCE(COUNT(DISTINCT (tc.classif_id)), 0) ")
			.append(" FROM siac_t_class tc ")
			.append(" JOIN siac_d_class_tipo dct ON dct.classif_tipo_id = tc.classif_tipo_id ")
			.append(" JOIN siac_r_class_fam_tree rcft ON (tc.classif_id = rcft.classif_id_padre AND rcft.data_cancellazione IS NULL) ")
			.append(" WHERE dct.classif_tipo_code = :classifTipoCodeNonno ")
			.append(" AND rcft.classif_id IN ( ")
			.append("     SELECT tc2.classif_id ")
			.append("     FROM siac_t_variazione tv ")
			.append("     JOIN siac_r_variazione_stato rvs ON (rvs.variazione_id = tv.variazione_id AND rvs.data_cancellazione IS NULL) ")
			.append("     JOIN siac_t_bil_elem_det_var tbedv ON tbedv.variazione_stato_id = rvs.variazione_stato_id ")
			.append("     JOIN siac_r_bil_elem_class rbec ON (rbec.elem_id = tbedv.elem_id AND rbec.data_cancellazione IS NULL) ")
			.append("     JOIN siac_t_class tc1 ON tc1.classif_id = rbec.classif_id ")
			.append("     JOIN siac_d_class_tipo dct1 ON dct1.classif_tipo_id = tc1.classif_tipo_id ")
			.append("     JOIN siac_r_class_fam_tree rcft1 ON (rcft1.classif_id = tc1.classif_id AND rcft1.data_cancellazione IS NULL) ")
			.append("     JOIN siac_t_class tc2 ON tc2.classif_id = rcft1.classif_id_padre ")
			.append("     JOIN siac_d_class_tipo dct2 ON dct2.classif_tipo_id = tc2.classif_tipo_id ")
			.append("     WHERE tv.variazione_id = :variazioneId ")
			.append("     AND dct1.classif_tipo_code = :classifTipoCodeFiglio ")
			.append("     AND dct2.classif_tipo_code = :classifTipoCodePadre ")
			.append(" ) ");
		
		params.put("variazioneId", variazioneId);
		params.put("classifTipoCodeNonno", classifTipoCodeNonno);
		params.put("classifTipoCodePadre", classifTipoCodePadre);
		params.put("classifTipoCodeFiglio", classifTipoCodeFiglio);
		
		log.debug(methodName, "NATIVE SQL: " + sb.toString());
		
		Query query = createNativeQuery(sb.toString(), params);
		long res = ((Number) query.getSingleResult()).longValue();
		
		return Long.valueOf(res);
	}


	@Override
	public List<Object[]> findAllDettagliVariazioneImportoCapitoloByUidVariazione(int uidVariazione) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Page<SiacTBilElem> findPrimoCapitoloNellaVariazioneByUid(int uidVariazione, Pageable pageable, Integer capitoloId) {
		StringBuilder  jpql = new StringBuilder();
		Map<String,Object> param = new HashMap<String, Object>();
		//Integer capitoloId = getFirstCapitoloIdByUidVariazioneTipoCap(uidVariazione, tipoCapitolo);
//		if(capitoloId > 0){
//			
//		}
		jpql.append(" SELECT tbe ");
		jpql.append(" FROM  SiacTBilElem tbe, SiacDBilElemTipo dbet ");
		jpql.append(" WHERE tbe.elemId = :idCapitolo " );
		jpql.append(" AND dbet = tbe.siacDBilElemTipo ");
		//jpql.append(" AND dbet = tbe.siacDBilElemTipo ");
		
		param.put("idCapitolo", capitoloId);
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	//SIAC-6884
	@Override
	public Integer getFirstCapitoloIdByUidVariazioneTipoCap(int uidVariazione, String tipoCapitolo, String annoBilancio) {
		
		String methodName = "getFirstCapitoloAddedToVariazione";
		StringBuilder  sb = new StringBuilder();
		Map<String,Object> param = new HashMap<String, Object>();

		sb.append(" SELECT COALESCE( NULLIF(sub.elem_id ,0) , 0 ) FROM ( ");
		sb.append(" 	SELECT DISTINCT  MIN(bedv.data_creazione), bedv.elem_id ");
		sb.append(" 	FROM  siac_t_bil_elem_det_var bedv, siac_r_variazione_stato rvs  ");
		sb.append(" 	WHERE bedv.data_cancellazione is NULL ");
		sb.append(" 	AND rvs.data_cancellazione  is NULL ");
		sb.append(" 	AND rvs.variazione_stato_id = bedv.variazione_stato_id ");
		sb.append(" 	AND rvs.variazione_id = :variazioneId ");
		sb.append(" 	AND bedv.elem_id IN ( ");
		sb.append(" 		SELECT bilelem.elem_id  ");
		sb.append(" 		FROM siac_t_bil_elem bilelem, siac_d_bil_elem_tipo bilelemtipo, siac_t_bil tb, siac_t_periodo tp ");
		sb.append(" 		WHERE bilelem.elem_tipo_id = bilelemtipo.elem_tipo_id ");		
		sb.append(" 		AND tp.periodo_id = tb.periodo_id AND tp.data_cancellazione IS NULL ");
		sb.append(" 		AND tp.anno = :annoBilancio ");
		sb.append(" 		AND tb.bil_id=bilelem.bil_id ");
		sb.append(" 		AND bilelem.data_cancellazione IS NULL ");
		sb.append(" 		AND bilelemtipo.data_cancellazione IS NULL ");
		sb.append(" 		AND bilelemtipo.elem_tipo_code= :tipoCapitolo ");
		sb.append(" 	) ");
		sb.append(" GROUP BY bedv.elem_id ORDER BY 2) AS sub LIMIT 1");
		
		param.put("variazioneId", uidVariazione);
		param.put("tipoCapitolo", tipoCapitolo);
		param.put("annoBilancio", annoBilancio);
		
		log.debug(methodName, "NATIVE SQL: " + sb.toString());
		
		Query query = createNativeQuery(sb.toString(), param);
		int res = 0;
		try{
			res = (int) ((Number) query.getSingleResult()).longValue();
		}catch(NoResultException e){
			return 0;
		}
		return Integer.valueOf(res);
	}

	@Override
	public	Long countCapitoliComuni(Integer uidVariazione, List<Integer> uidVariaziones) {
		String methodName = "getFirstCapitoloAddedToVariazione";
		StringBuilder  jpql = new StringBuilder();
		Map<String,Object> param = new HashMap<String, Object>();
		jpql.append(" SELECT COALESCE(COUNT(DISTINCT bedv.siacTBilElem.elemId), 0) ");
		jpql.append(" FROM  SiacTBilElemDetVar bedv, SiacRVariazioneStato rvs");
		jpql.append(" WHERE bedv.dataCancellazione is NULL ");
		jpql.append(" AND rvs.dataCancellazione is NULL ");
		jpql.append(" AND rvs = bedv.siacRVariazioneStato ");
		jpql.append(" AND rvs.siacTVariazione.variazioneId = :variazioneId ");
		
		jpql.append(" AND bedv.siacTBilElem.elemId in (SELECT DISTINCT bedv2.siacTBilElem.elemId ");
		jpql.append(" FROM  SiacTBilElemDetVar bedv2, SiacRVariazioneStato rvs2");
		jpql.append(" WHERE bedv2.dataCancellazione is NULL ");
		jpql.append(" AND rvs2.dataCancellazione is NULL ");
		jpql.append(" AND rvs2 = bedv2.siacRVariazioneStato ");
		jpql.append(" AND rvs2.siacTVariazione.variazioneId IN (:variazioneIds) ) ");
		
		param.put("variazioneId", uidVariazione);
		param.put("variazioneIds", uidVariaziones);
		Query query = createQuery(jpql.toString(), param);
		return (Long) query.getSingleResult();
//		Query query = createNativeQuery(sql.toString(), param);
//		long res = 0;
//		try{
//			res = ((Number) query.getSingleResult()).longValue();
//		}catch(NoResultException e){
//			return Long.valueOf(0L);
//		}
//		return Long.valueOf(res);
	}


	@Override
	public Page<SiacTVariazione> ricercaSinteticaVariazioneNeutreDiBilancio(Collection<SiacDVariazioneTipoEnum> variazioneTipo, Integer enteProprietarioId, 
			String annoBilancio, Integer variazioneNum, String variazioneDesc, Date dataAperturaProposta, Date dataChiusuraProposta, Integer direzioneProponenteId, SiacDVariazioneStatoEnum variazioneStato,
			Integer attoAmmId,	Integer capitoloId, Integer capitoloCodificaId, Integer capitoloSorgenteId, Integer capitoloDestinazioneId, 
			List<SiacDBilElemTipoEnum> bilElemsTipo, List<SiacDBilElemTipoEnum> bilElemsCodificaTipo, String operatoreImporti, Integer attoAmmVarBilId, Integer attoAmmIdMultiple, Pageable pageable) {
		
		StringBuilder  jpql = new StringBuilder();
//		jpql.append("SELECT var ");
		jpql.append("FROM   SiacTVariazione var ");
		jpql.append("WHERE  siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append("       AND siacTBil.siacTPeriodo.anno = :annoBilancio ");
		jpql.append("       AND var.dataCancellazione is NULL ");
		
				
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("annoBilancio", annoBilancio);
		
		
		if(variazioneTipo!=null && !variazioneTipo.isEmpty()){
			Collection<String> variazioneTipoCodes = new ArrayList<String>();
			for(SiacDVariazioneTipoEnum sdvte : variazioneTipo) {
				variazioneTipoCodes.add(sdvte.getCodice());
			}
			
			jpql.append("       AND siacDVariazioneTipo.variazioneTipoCode IN (:variazioneTipoCodes) ");
			param.put("variazioneTipoCodes", variazioneTipoCodes);
		}
		
		
		if(bilElemsTipo!=null && !bilElemsTipo.isEmpty()){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE ");
			jpql.append("       			  rvarstato.dataCancellazione IS NULL ");
			jpql.append("                     AND EXISTS (FROM rvarstato.siacTBilElemDetVars rvarstatoelemdet ");
			jpql.append("       			 			 WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("                                AND rvarstatoelemdet.siacTBilElem.siacDBilElemTipo.elemTipoCode IN ("+SiacDBilElemTipoEnum.getCodiciSingleQuoteCommaSeparated(bilElemsTipo)+") ");
			jpql.append(" 							  ) ) ) ");	
		}
		
		if(bilElemsCodificaTipo!=null && !bilElemsCodificaTipo.isEmpty()){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE ");
			jpql.append("       			  rvarstato.dataCancellazione IS NULL ");
			jpql.append("                     AND EXISTS (FROM rvarstato.siacRBilElemClassVars rvarstatoelemdet ");
			jpql.append("       			 			 WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("                                AND rvarstatoelemdet.siacTBilElem.siacDBilElemTipo.elemTipoCode IN ("+SiacDBilElemTipoEnum.getCodiciSingleQuoteCommaSeparated(bilElemsCodificaTipo)+") ");
			jpql.append(" 							  ) ) ) ");	
		}
			
		
		if(variazioneNum!=null && variazioneNum!=0){
			jpql.append("       AND variazioneNum = :variazioneNum ");
			param.put("variazioneNum", variazioneNum);
		}
		
		if(StringUtils.isNotBlank(variazioneDesc)){
			//jpql.append("       AND variazioneDesc = :variazioneDesc ");
			jpql.append("       AND ( "+Utility.toJpqlSearchLike("variazioneDesc", "CONCAT('%',:variazioneDesc,'%')" )+" ) ");
			param.put("variazioneDesc", variazioneDesc);
		}
		
		if(dataAperturaProposta!=null){
			
			jpql.append(" 		AND var.dataAperturaProposta  >= :dataAperturaProposta ");
			param.put("dataAperturaProposta", dataAperturaProposta);

			Calendar dataAperturaPropostaRange = Calendar.getInstance();
			dataAperturaPropostaRange.setTime(dataAperturaProposta);
			
			dataAperturaPropostaRange.set(Calendar.HOUR_OF_DAY,23);
			dataAperturaPropostaRange.set(Calendar.MINUTE,59);
			dataAperturaPropostaRange.set(Calendar.SECOND,59);
			dataAperturaPropostaRange.set(Calendar.MILLISECOND,0);

			jpql.append(" 		AND var.dataAperturaProposta  <= :dataAperturaPropostaRange ");
			param.put("dataAperturaPropostaRange", dataAperturaPropostaRange.getTime());
			
			
		}
		
		if(dataChiusuraProposta!=null){
			jpql.append(" 		AND var.dataChiusuraProposta  >= :dataChiusuraProposta ");
			param.put("dataChiusuraProposta", dataChiusuraProposta);

			Calendar dataChiusuraPropostaRange = Calendar.getInstance();
			dataChiusuraPropostaRange.setTime(dataChiusuraProposta);
			
			dataChiusuraPropostaRange.set(Calendar.HOUR_OF_DAY,23);
			dataChiusuraPropostaRange.set(Calendar.MINUTE,59);
			dataChiusuraPropostaRange.set(Calendar.SECOND,59);
			dataChiusuraPropostaRange.set(Calendar.MILLISECOND,0);

			jpql.append(" 		AND var.dataChiusuraProposta  <= :dataChiusuraPropostaRange ");
			param.put("dataChiusuraPropostaRange", dataChiusuraPropostaRange.getTime());
		}
		
		if(direzioneProponenteId!=null){
			jpql.append("       AND siacTClass.classifId IN (:direzioneProponenteId) ");
			param.put("direzioneProponenteId", direzioneProponenteId);
		}
		
//		if(direzioneProponenteId!=null){
//			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE rvarstato.siacDVariazioneStato.variazioneStatoTipoCode = :variazioneStatoTipoCode ");
//			jpql.append(" 					AND rvarstato.dataCancellazione is NULL )) ");
//			param.put("variazioneStatoTipoCode", variazioneStato.getCodice());
//		}
		
		if(variazioneStato!=null){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE rvarstato.siacDVariazioneStato.variazioneStatoTipoCode = :variazioneStatoTipoCode ");
			jpql.append(" 					AND rvarstato.dataCancellazione is NULL )) ");
			param.put("variazioneStatoTipoCode", variazioneStato.getCodice());
		}
		
		if(attoAmmId!=null && attoAmmId!=0){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE rvarstato.siacTAttoAmm.attoammId = :attoAmmId ");
			jpql.append(" 					AND rvarstato.dataCancellazione is NULL )) ");
			param.put("attoAmmId", attoAmmId);
		}
		
		if(attoAmmVarBilId!=null && attoAmmVarBilId!=0){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE rvarstato.siacTAttoAmmVarbil.attoammId = :attoAmmId ");
			jpql.append(" 					AND rvarstato.dataCancellazione is NULL )) ");
			param.put("attoAmmId", attoAmmVarBilId);
		}
		
		
		if(capitoloCodificaId!=null && capitoloCodificaId!=0){
			
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE ");
			jpql.append("       			  rvarstato.dataCancellazione IS NULL ");
			jpql.append("                     AND EXISTS (FROM rvarstato.siacRBilElemClassVars rvarstatoelemdet ");
			jpql.append("       			  		  WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("                             AND rvarstatoelemdet.siacTBilElem.elemId = :capitoloCodificaId  ) )) ");
				
			param.put("capitoloCodificaId", capitoloCodificaId);
		}
		
		
		if(capitoloId!=null && capitoloId!=0){
			
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM var.siacRVariazioneStatos rvarstato ");
			jpql.append("     WHERE rvarstato.dataCancellazione IS NULL ");
			jpql.append("     AND EXISTS ( ");
			jpql.append("         FROM rvarstato.siacTBilElemDetVars rvarstatoelemdet ");
			jpql.append("         WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("         AND rvarstatoelemdet.siacTBilElem.elemId = :capitoloId ");
			
			if(StringUtils.isNotBlank(operatoreImporti)) {
				jpql.append("         AND rvarstatoelemdet.elemDetImporto ").append(operatoreImporti).append(" 0 ");
				//SIAC-7735-VG-GM
				jpql.append(" AND EXISTS (FROM rvarstatoelemdet.siacTBilElemDetVarComps comp WHERE comp.elemDetImporto != 0) ");
				
			}
			
			jpql.append("     ) ");
			jpql.append(" ) ");
				
			
			//CONTABILIA-285
//			jpql.append("         AND (( SELECT SUM(COALESCE(itbedv.elemDetImporto, 0))");
//			jpql.append("         FROM SiacTVariazione siacTVariazione");
//			jpql.append(" 		  LEFT JOIN siacTVariazione.siacRVariazioneStatos siacRVariazioneStato  ");
//			jpql.append(" 		  LEFT JOIN siacRVariazioneStato.siacTBilElemDetVars itbedv  ");
//			jpql.append("         WHERE siacTVariazione.dataCancellazione IS NULL ");
//			jpql.append("         AND  siacTVariazione.variazioneId = var.variazioneId");
//			jpql.append("         AND  itbedv.siacTBilElem.elemId = :capitoloId");
//			jpql.append(") = 0)");
			
		
//			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE ");
//			jpql.append("       			  rvarstato.dataCancellazione IS NULL ");
//			jpql.append("                     AND EXISTS (FROM rvarstato.siacTBilElemDetVars rvarstatoelemdet ");
//			jpql.append("       			  		  WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
//			jpql.append(" AND EXISTS (FROM rvarstatoelemdet.siacTBilElemDetVarComps comp WHERE comp.elemDetImporto != 0) ");
//			jpql.append(" 							  ) )) ");
			
			
			//
			param.put("capitoloId", capitoloId);
		}
		if(capitoloSorgenteId!=null && capitoloSorgenteId!=0){
			
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE ");
			jpql.append("       			  rvarstato.dataCancellazione IS NULL ");
			jpql.append("                     AND EXISTS (FROM rvarstato.siacTBilElemDetVars rvarstatoelemdet ");
			jpql.append("       			  		  WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("                             AND rvarstatoelemdet.siacTBilElem.elemId = :capitoloSorgenteId ");
			jpql.append("							  AND rvarstatoelemdet.elemDetFlag = '"+SiacTBilElemDetVarElemDetFlagEnum.Sorgente.getCodice()+"' ) )) ");
				
			param.put("capitoloSorgenteId", capitoloSorgenteId);
		}
		
		if(capitoloDestinazioneId!=null && capitoloDestinazioneId!=0){
			jpql.append("       AND (EXISTS ( FROM var.siacRVariazioneStatos rvarstato WHERE ");
			jpql.append("       			  rvarstato.dataCancellazione IS NULL ");
			jpql.append("                     AND EXISTS (FROM rvarstato.siacTBilElemDetVars rvarstatoelemdet ");
			jpql.append("       			  		  WHERE rvarstatoelemdet.dataCancellazione IS NULL ");
			jpql.append("                             AND rvarstatoelemdet.siacTBilElem.elemId = :capitoloDestinazioneId ");
			jpql.append(" 							  AND rvarstatoelemdet.elemDetFlag = '"+SiacTBilElemDetVarElemDetFlagEnum.Destinazione.getCodice()+"') )) ");
			
			param.put("capitoloDestinazioneId", capitoloDestinazioneId);
		}
		
		// SIAC-4815
		if(attoAmmIdMultiple != null && attoAmmIdMultiple.intValue() != 0) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM var.siacRVariazioneStatos rvarstato ")
				.append("     WHERE rvarstato.dataCancellazione is NULL ")
				.append("     AND ( ")
				.append("         rvarstato.siacTAttoAmm.attoammId = :attoAmmIdMult ")
				.append("         OR rvarstato.siacTAttoAmmVarbil.attoammId = :attoAmmIdMult ")
				.append("     ) ")
				.append(" ) ");
			param.put("attoAmmIdMult", attoAmmIdMultiple);
		}
		
		jpql.append(" ORDER BY variazioneNum ");
		
		
		return getPagedList(jpql.toString(), param, pageable);	

	}


	@Override
	public List<Object[]> findStanziamentoCapitoloInVariazioneInDiminuzione(Integer idVariazione, Collection<Integer> idCapitoliDaEscludere) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		jpql.append(" SELECT dv.siacTBilElem.elemId, dv.siacTBilElem.siacDBilElemTipo.elemTipoCode, dv.siacTPeriodo.anno, dv.siacDBilElemDetTipo.elemDetTipoCode, dv.elemDetImporto ");
		jpql.append(" FROM SiacTBilElemDetVar dv");
		jpql.append(" WHERE dv.dataCancellazione IS NULL");
		jpql.append(" AND dv.siacTBilElem.dataCancellazione IS NULL ");
		
		if(idCapitoliDaEscludere != null && !idCapitoliDaEscludere.isEmpty() ) {
			jpql.append(" AND dv.siacTBilElem.elemId NOT IN (:elemIdsDaEscludere)");
			params.put("elemIdsDaEscludere", idCapitoliDaEscludere);
		}
		
//		if(uidCapitoloDaIncludere != null && uidCapitoloDaIncludere.intValue() != 0 ) {
//			jpql.append(" AND dv.siacTBilElem.elemId  = :elemIdDaIncludere");
//			params.put("elemIdDaIncludere", uidCapitoloDaIncludere);
//		}
		//SIAC-8736
		jpql.append(" AND dv.siacDBilElemDetTipo.elemDetTipoCode IN ('STA', 'SCA', 'STR')");
		jpql.append(" AND dv.elemDetImporto < 0 ");
		jpql.append(" AND EXISTS ( ");
		jpql.append("   FROM dv.siacRVariazioneStato rvs ");
		jpql.append("   WHERE rvs.dataCancellazione IS NULL ");
		jpql.append("   AND rvs.siacTVariazione.variazioneId = :variazioneId");
		jpql.append(" ) ");
		jpql.append(" ORDER BY dv.siacTBilElem.siacDBilElemTipo.elemTipoCode, dv.siacTBilElem.elemId");
		params.put("variazioneId", idVariazione);
		
		return getList(jpql.toString(), params);
	}


	@Override
	public List<Object[]> findStanziamentoComponenteCapitoloInVariazioneInDiminuzione(Integer idVariazione, List<Integer> idCapitolispesa) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		jpql.append(" SELECT dv.siacTBilElem.elemId, dv.siacTBilElem.siacDBilElemTipo.elemTipoCode, dvc.siacTBilElemDetComp.siacDBilElemDetCompTipo.elemDetCompTipoId, dvc.siacTBilElemDetComp.siacDBilElemDetCompTipo.elemDetCompTipoDesc, dv.siacTPeriodo.anno, dv.siacDBilElemDetTipo.elemDetTipoCode, dvc.elemDetImporto ");
		jpql.append(" FROM SiacTBilElemDetVarComp dvc, SiacTBilElemDetVar dv");
		jpql.append(" WHERE dvc.siacTBilElemDetVar = dv ");
		jpql.append(" AND dvc.dataCancellazione IS NULL ");
		jpql.append(" AND dv.dataCancellazione IS NULL ");
		jpql.append(" AND dv.siacTBilElem.dataCancellazione IS NULL ");
		
		if(idCapitolispesa != null && !idCapitolispesa.isEmpty() ) {
			jpql.append(" AND dv.siacTBilElem.elemId NOT IN (:elemIdsDaEscludere)");
			params.put("elemIdsDaEscludere", idCapitolispesa);
		}
		
//		if(uidCapitoloDaIncludere != null && uidCapitoloDaIncludere.intValue() != 0 ) {
//			jpql.append(" AND dv.siacTBilElem.elemId  = :elemIdDaIncludere");
//			params.put("elemIdDaIncludere", uidCapitoloDaIncludere);
//		}
		
		jpql.append(" AND dv.siacDBilElemDetTipo.elemDetTipoCode IN ('STA', 'SCA')");
		jpql.append(" AND dvc.elemDetImporto < 0 ");
		jpql.append(" AND EXISTS ( ");
		jpql.append("   FROM dv.siacRVariazioneStato rvs ");
		jpql.append("   WHERE rvs.dataCancellazione IS NULL ");
		jpql.append("   AND rvs.siacTVariazione.variazioneId = :variazioneId");
		jpql.append(" ) ");
		jpql.append(" ORDER BY dv.siacTBilElem.siacDBilElemTipo.elemTipoCode, dv.siacTBilElem.elemId");
		params.put("variazioneId", idVariazione);
		
		return getList(jpql.toString(), params);
	}
	
}
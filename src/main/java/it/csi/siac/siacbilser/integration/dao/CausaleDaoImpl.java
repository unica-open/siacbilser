/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleClass;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleModpag;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;
import it.csi.siac.siacbilser.integration.entity.SiacROnereCausale;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDModelloEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class CausaleDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CausaleDaoImpl extends JpaDao<SiacDCausale, Integer> implements CausaleDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.CausaleDao#create(it.csi.siac.siacbilser.integration.entity.SiacDCausale)
	 */
	public SiacDCausale create(SiacDCausale e){
		
		Date now = new Date();
		
		e.setDataModificaInserimento(now);
		
		setDataModificaInserimento(e, now);
		
		e.setUid(null);		
		super.save(e);
		return e;
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	public SiacDCausale update(SiacDCausale e){
		SiacDCausale eAttuale = this.findById(e.getUid());
		
		Date now = new Date();
		
		e.setDataModificaAggiornamento(now);
			
		
		//cancellazione elementi collegati
		setDataCancellazioneIfNotSet(eAttuale, now);
		
		
		setDataModificaInserimento(e, now);
		
		
		super.update(e);
		return e;
	}
	

	public static void setDataModificaInserimento(SiacDCausale e, Date now) {
		
		//e.setDataModificaInserimento(now);
		
		
		if(e.getSiacRCausaleAttoAmms()!=null){
			for(SiacRCausaleAttoAmm r : e.getSiacRCausaleAttoAmms()){
				r.setDataModificaInserimento(now);
			}
		}

		if(e.getSiacRCausaleBilElems()!=null){
			for(SiacRCausaleBilElem r : e.getSiacRCausaleBilElems()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRCausaleClasses()!=null){
			for(SiacRCausaleClass r : e.getSiacRCausaleClasses()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRCausaleModpags()!=null){
			for(SiacRCausaleModpag r : e.getSiacRCausaleModpags()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRCausaleMovgestTs()!=null){
			for(SiacRCausaleMovgestT r : e.getSiacRCausaleMovgestTs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRCausaleSoggettos()!=null){
			for(SiacRCausaleSoggetto r : e.getSiacRCausaleSoggettos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRCausaleTipos()!=null){
			for(SiacRCausaleTipo r : e.getSiacRCausaleTipos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacRDocOneres()!=null){
			for(SiacRDocOnere  r : e.getSiacRDocOneres()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(e.getSiacROnereCausales()!=null){
			for(SiacROnereCausale r : e.getSiacROnereCausales()){
				r.setDataModificaInserimento(now);
				r.setUid(null);
			}
		}
	}
	

	public static void setDataCancellazioneIfNotSet(SiacDCausale e, Date now) {
		
		//e.setDataCancellazioneIfNotSet(now); //NON cancelliamo la e ma solo le sue r..
		
		
		if(e.getSiacRCausaleAttoAmms()!=null){
			for(SiacRCausaleAttoAmm r : e.getSiacRCausaleAttoAmms()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}

		if(e.getSiacRCausaleBilElems()!=null){
			for(SiacRCausaleBilElem r : e.getSiacRCausaleBilElems()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacRCausaleClasses()!=null){
			for(SiacRCausaleClass r : e.getSiacRCausaleClasses()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacRCausaleModpags()!=null){
			for(SiacRCausaleModpag r : e.getSiacRCausaleModpags()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacRCausaleMovgestTs()!=null){
			for(SiacRCausaleMovgestT r : e.getSiacRCausaleMovgestTs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacRCausaleSoggettos()!=null){
			for(SiacRCausaleSoggetto r : e.getSiacRCausaleSoggettos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacRCausaleTipos()!=null){
			for(SiacRCausaleTipo r : e.getSiacRCausaleTipos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacRDocOneres()!=null){
			for(SiacRDocOnere  r : e.getSiacRDocOneres()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(e.getSiacROnereCausales()!=null){
			for(SiacROnereCausale r : e.getSiacROnereCausales()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
	}
	
	public void delete(SiacDCausale siacDCausale){
		SiacDCausale e = this.findById(siacDCausale.getUid());		
		Date now = new Date();
		setDataCancellazioneIfNotSet(e, now);		
		super.update(e);
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.CausaleDao#ricercaSinteticaCausale(int, it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCausaleFamTipoEnum, java.lang.Integer, java.lang.Integer, java.lang.Boolean, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<SiacDCausale> ricercaSinteticaCausale(int enteProprietarioId,
			SiacDCausaleFamTipoEnum causFamTipoCode,
			String causCode,
			String causDesc,
			Integer struttId,
			Integer causTipoId,
			Boolean statoOperativoCausale,
			Integer elemId,
			Integer movgestId,
			Integer movgestTsId,
			Integer soggettoId,
			Integer attoammId,
			Integer modpagId,
			Integer sedeSecondariaId,
		    Pageable pageable) {
		
		final String methodName = "ricercaSinteticaCausale";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaCausale( jpql,  param, enteProprietarioId, causFamTipoCode, causCode, causDesc, struttId,
				  causTipoId,  statoOperativoCausale, elemId, movgestId, movgestTsId, soggettoId,  attoammId, modpagId, sedeSecondariaId);
		
		jpql.append(" ORDER BY d.causCode ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	
/*	
	public BigDecimal ricercaSinteticaCausaleImportoTotale(Integer enteProprietarioId, SiacDDocFamTipoEnum docFamTipoCode,
			Integer docAnno, 
			String docNumero, 
			Date docDataEmissione,
			Integer docTipoId, 
			SiacDDocStatoEnum docStato,
			SiacDDocStatoEnum docStatoToExclude, 
			Boolean flagRilevanteIva,
			
			Impegno impegno, // movimento
			Accertamento accertamento,
			AttoAmministrativo attoAmministrativo, // provvedimento
			Soggetto soggetto,
						
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaCausaleImportoTotale";
		
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT SUM(d.docImporto) ");
		
		componiQueryRicercaSinteticaCausale( jpql, param, enteProprietarioId, docFamTipoCode, docAnno, docNumero, docDataEmissione, docTipoId, docStato, docStatoToExclude,
				flagRilevanteIva, impegno, accertamento, attoAmministrativo, soggetto);
		
		Query query = createQuery(jpql.toString(), param);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		log.info(methodName, "returning: "+result);
		
		return result;
	}
	
*/	

	/**
 * Componi query ricerca sintetica causale.
 *
 * @param jpql the jpql
 * @param param the param
 * @param enteProprietarioId the ente proprietario id
 * @param causFamTipoCode the caus fam tipo code
 * @param struttId the strutt id
 * @param causTipoId the caus tipo id
 * @param statoOperativoCausale the stato operativo causale
 * @param elemId the elem id
 * @param movgestTsId the movgest ts id
 * @param soggettoId the soggetto id
 * @param attoammId the attoamm id
 * @param modpagId the modpag id
 * @param sedeSecondariaId the sede secondaria id
 */
private void componiQueryRicercaSinteticaCausale(StringBuilder jpql, Map<String, Object> param, 
			Integer enteProprietarioId,SiacDCausaleFamTipoEnum causFamTipoCode, String causCode,
			String causDesc, Integer struttId,
			Integer causTipoId, Boolean statoOperativoCausale,Integer elemId, Integer movgestId, Integer movgestTsId,
			Integer soggettoId, Integer attoammId ,Integer modpagId, Integer sedeSecondariaId ) {
		
		jpql.append("FROM SiacDCausale d ");
		jpql.append(" WHERE ");
		jpql.append(" d.dataCancellazione IS NULL ");
		jpql.append(" AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND EXISTS ( FROM  d.siacRCausaleTipos dt");
		jpql.append(" 	 	WHERE dt.siacDCausaleTipo.siacDCausaleFamTipo.causFamTipoCode = :causFamTipoCode ");
		jpql.append(" 	  ) ");
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("causFamTipoCode", causFamTipoCode.getCodice());
		
		if(StringUtils.isNotBlank(causCode)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.causCode", "CONCAT('%', :causCode, '%')") + " ");
			param.put("causCode", causCode);	
		}
		
		if(StringUtils.isNotBlank(causDesc)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("d.causDesc", "CONCAT('%', :causDesc, '%')") + " ");
			param.put("causDesc", causDesc);	
		}
		
		if (causTipoId != null) {
			jpql.append(" AND EXISTS ( FROM d.siacRCausaleTipos dt ");
			jpql.append(" 	WHERE dt.siacDCausaleTipo.causTipoId= :causTipoId 	 " );
			jpql.append(" 	AND dt.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("causTipoId", causTipoId);
		}
		
		if(elemId != null){
			jpql.append(" AND EXISTS ( FROM d.siacRCausaleBilElems db "  );
			jpql.append(" 	WHERE db.siacTBilElem.elemId  = :elemId ");
			jpql.append(" 	AND db.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("elemId",elemId);			
		}
		
		if(movgestId != null){
			jpql.append(" AND EXISTS ( FROM d.siacRCausaleMovgestTs dm "  );
			jpql.append(" 	WHERE dm.siacTMovgestT.siacTMovgest.movgestId  = :movgestId ");
			jpql.append(" 	AND dm.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("movgestId",movgestId);			
		}
		
		if(movgestTsId != null){
			jpql.append(" AND EXISTS ( FROM d.siacRCausaleMovgestTs dm "  );
			jpql.append(" 	WHERE dm.siacTMovgestT.movgestTsId  = :movgestTsId ");
			jpql.append(" 	AND dm.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("movgestTsId",movgestTsId);			
		}
		
		if(soggettoId != null){
			jpql.append(" AND EXISTS ( FROM d.siacRCausaleSoggettos ds "  );
			jpql.append(" 	WHERE ds.siacTSoggetto.soggettoId  = :soggettoId ");
			jpql.append(" 	AND ds.dataCancellazione IS NULL ");
			jpql.append(" 	AND NOT EXISTS ( FROM ds.siacTSoggetto.siacRSoggettoRelazs2 sr ");
			jpql.append(" 		WHERE sr.dataCancellazione IS NULL ");
			jpql.append("	) ");
			jpql.append(" ) ");
			param.put("soggettoId",soggettoId);			
		}else if(attoammId != null){
			jpql.append(" AND EXISTS ( FROM d.siacRCausaleAttoAmms da "  );
			jpql.append(" 	WHERE da.siacTAttoAmm.attoammId  = :attoammId ");
			jpql.append(" 	AND da.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("attoammId",attoammId);			
		}
		
		if(struttId != null){
			jpql.append(" AND EXISTS ( FROM d.siacRCausaleClasses da "  );
			jpql.append(" 	WHERE da.siacTClass.classifId  = :struttId ");
			jpql.append(" 	AND da.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("struttId",struttId);			
		}
		
		if(modpagId != null){
			jpql.append(" AND EXISTS ( FROM d.siacRCausaleModpags dp "  );
			jpql.append(" 	WHERE dp.siacTModpag.modpagId  = :modpagId ");
			jpql.append(" 	AND dp.dataCancellazione IS NULL ");
			jpql.append(" ) ");
			param.put("modpagId",modpagId);			
		}
		
		if(sedeSecondariaId != null){
			jpql.append(" AND EXISTS ( FROM d.siacRCausaleSoggettos dss "  );
			jpql.append(" 	WHERE dss.siacTSoggetto.soggettoId  = :sedeSecondariaId ");
			jpql.append(" 	AND dss.dataCancellazione IS NULL ");
			jpql.append(" 	AND EXISTS ( FROM dss.siacTSoggetto.siacRSoggettoRelazs2 sr ");
			jpql.append(" 		WHERE sr.dataCancellazione IS NULL ");
			jpql.append("	) ");
			jpql.append(" ) ");
			param.put("sedeSecondariaId",sedeSecondariaId);
		}
		
		if(statoOperativoCausale != null){
			if(statoOperativoCausale){
				jpql.append(" AND ( d.dataFineValidita IS NULL OR d.dataFineValidita > CURRENT_TIMESTAMP ) "  );
			}else {
				jpql.append(" AND d.dataFineValidita < CURRENT_TIMESTAMP "  );
			}
			
		}
		

	}
	
	/**
	 * Find sedi secondarie.
	 *
	 * @return the list
	 */
	public List<SiacTSoggetto> findSediSecondarie() {
		return new ArrayList<SiacTSoggetto>();
	}

	@Override
	public List<SiacDCausale> ricercaCausaliByEnteProprietarioAndTipoCausaleAndModello(Integer enteProprietarioId, Integer onereId, SiacDModelloEnum siacDModelloEnum) {
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacDCausale c ")
			.append(" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		params.put("enteProprietarioId", enteProprietarioId);
		
		if(onereId != null) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM c.siacROnereCausales r ")
				.append("     WHERE r.siacDOnere.onereId = :onereId ")
				.append("     AND r.dataCancellazione IS NULL ")
				.append("     AND r.siacDOnere.dataCancellazione IS NULL ")
				.append(" ) ");
			params.put("onereId", onereId);
		}
		
		if(siacDModelloEnum != null) {
			jpql.append(" AND c.siacDModello.modelCode = :modelCode ")
				.append(" AND c.siacDModello.dataCancellazione IS NULL ");
			params.put("modelCode", siacDModelloEnum.getCodice());
		}
		
		jpql.append(" AND c.dataCancellazione IS NULL ")
			.append(" AND c.dataInizioValidita < CURRENT_TIMESTAMP ")
			.append(" AND (c.dataFineValidita IS NULL OR c.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append(" ORDER BY c.causCode ");
		
		Query query = createQuery(jpql.toString(), params);
		
		@SuppressWarnings("unchecked")
		List<SiacDCausale> result = query.getResultList();
			
		return result;
	}
	

}

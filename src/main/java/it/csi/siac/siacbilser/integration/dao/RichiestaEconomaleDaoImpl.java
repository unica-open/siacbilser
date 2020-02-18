/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
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
import it.csi.siac.siacbilser.integration.entity.SiacRGiustificativoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRMovimentoModpag;
import it.csi.siac.siacbilser.integration.entity.SiacRRichiestaEconClass;
import it.csi.siac.siacbilser.integration.entity.SiacRRichiestaEconMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacRRichiestaEconSog;
import it.csi.siac.siacbilser.integration.entity.SiacRRichiestaEconStato;
import it.csi.siac.siacbilser.integration.entity.SiacRRichiestaEconSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativoDet;
import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEconSospesa;
import it.csi.siac.siacbilser.integration.entity.SiacTTrasfMiss;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRichiestaEconStatoEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

/**
 * The Class CassaEconomaleDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RichiestaEconomaleDaoImpl extends JpaDao<SiacTRichiestaEcon, Integer> implements RichiestaEconomaleDao {
	
	public SiacTRichiestaEcon create(SiacTRichiestaEcon r){
		Date now = new Date();
		r.setDataModificaInserimento(now);
		
		if(r.getSiacRRichiestaEconClasses() != null){
			for(SiacRRichiestaEconClass c: r.getSiacRRichiestaEconClasses()){
				c.setDataModificaInserimento(now);
			}
			
		}
		
		if(r.getSiacRRichiestaEconSubdocs() != null) {
			for(SiacRRichiestaEconSubdoc s : r.getSiacRRichiestaEconSubdocs()) {
				s.setDataModificaInserimento(now);
			}
		}
		
		if(r.getSiacRRichiestaEconSogs() != null){
			for(SiacRRichiestaEconSog s: r.getSiacRRichiestaEconSogs()){
				s.setDataModificaInserimento(now);
			}
			
		}
		
		if(r.getSiacRRichiestaEconStatos()!= null){
			for(SiacRRichiestaEconStato s: r.getSiacRRichiestaEconStatos()){
				s.setDataModificaInserimento(now);
			}
			
		}
		
		if(r.getSiacRRichiestaEconMovgests()!= null){
			for(SiacRRichiestaEconMovgest s: r.getSiacRRichiestaEconMovgests()){
				s.setDataModificaInserimento(now);
			}
			
		}
		
		if(r.getSiacTTrasfMisses()!=null ){
			for(SiacTTrasfMiss t : r.getSiacTTrasfMisses()){
				t.setDataModificaInserimento(now);
				t.setUid(null);
			}
		}
		
		if(r.getSiacTRichiestaEconSospesas()!=null ){
			for(SiacTRichiestaEconSospesa t : r.getSiacTRichiestaEconSospesas()){
				t.setDataModificaInserimento(now);
				t.setUid(null);
			}
		}
		
		if(r.getSiacTMovimentos()!=null ){
			for(SiacTMovimento t : r.getSiacTMovimentos()){
				t.setDataModificaInserimento(now);
				t.setUid(null);
				
				// Relazione del movimento
				if(t.getSiacRMovimentoModpags()!=null){
					for(SiacRMovimentoModpag srmm : t.getSiacRMovimentoModpags()) {
						srmm.setDataModificaInserimento(now);
						srmm.setUid(null);
					}
				}
			}
		}
		
		if(r.getSiacTGiustificativoDets()!=null ){
			for(SiacTGiustificativoDet gd : r.getSiacTGiustificativoDets()){
				gd.setDataModificaInserimento(now);
				gd.setUid(null);
				
				if(gd.getSiacRGiustificativoStatos()!=null){
					for(SiacRGiustificativoStato rgs : gd.getSiacRGiustificativoStatos()){
						rgs.setDataModificaInserimento(now);
					}
				}
			}
		}
		
		if(r.getSiacTGiustificativos()!=null ){
			for(SiacTGiustificativo t : r.getSiacTGiustificativos()){
				t.setDataModificaInserimento(now);
				t.setUid(null);
				
			}
		}
		
		r.setUid(null);		
		super.save(r);
		return r;
	}
	

	public SiacTRichiestaEcon update(SiacTRichiestaEcon r){
		
		SiacTRichiestaEcon rAttuale = this.findById(r.getUid());
		
		Date now = new Date();
		r.setDataModificaAggiornamento(now);	

		
		//cancellazione elementi collegati	
		if(rAttuale.getSiacRRichiestaEconClasses() != null){
			for(SiacRRichiestaEconClass c: rAttuale.getSiacRRichiestaEconClasses()){
				c.setDataCancellazioneIfNotSet(now);
			}
			
		}
		
		if(rAttuale.getSiacRRichiestaEconSubdocs() != null) {
			for(SiacRRichiestaEconSubdoc s : rAttuale.getSiacRRichiestaEconSubdocs()) {
				s.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(rAttuale.getSiacRRichiestaEconSogs() != null){
			for(SiacRRichiestaEconSog s: rAttuale.getSiacRRichiestaEconSogs()){
				s.setDataCancellazioneIfNotSet(now);
			}
			
		}
		
		if(rAttuale.getSiacRRichiestaEconStatos()!= null){
			for(SiacRRichiestaEconStato s: rAttuale.getSiacRRichiestaEconStatos()){
				s.setDataCancellazioneIfNotSet(now);
			}
			
		}
		
		if(rAttuale.getSiacRRichiestaEconMovgests()!= null){
			for(SiacRRichiestaEconMovgest s: rAttuale.getSiacRRichiestaEconMovgests()){
				s.setDataCancellazioneIfNotSet(now);
			}
			
		}
		
		if(rAttuale.getSiacTTrasfMisses()!=null ){
			for(SiacTTrasfMiss t : rAttuale.getSiacTTrasfMisses()){
				t.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(rAttuale.getSiacTRichiestaEconSospesas()!=null ){
			for(SiacTRichiestaEconSospesa t : rAttuale.getSiacTRichiestaEconSospesas()){
				t.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(rAttuale.getSiacTMovimentos()!=null ){
			for(SiacTMovimento t : rAttuale.getSiacTMovimentos()){
				if(t.getSiacTGiustificativo() == null){
					//devo cancellare solo quelli legati alla richiesta, non quelli legati al rendiconto
					t.setDataCancellazioneIfNotSet(now);
					// Relazione del movimento
					if(t.getSiacRMovimentoModpags()!=null){
						for(SiacRMovimentoModpag srmm : t.getSiacRMovimentoModpags()) {
							srmm.setDataCancellazioneIfNotSet(now);
						}
					}
				}
			}
		}
		
		if(rAttuale.getSiacTGiustificativoDets()!=null ){
			for(SiacTGiustificativoDet gd : rAttuale.getSiacTGiustificativoDets()){
				if(gd.getSiacTGiustificativo() == null){
					//devo cancellare solo quelli legati alla richiesta, non quelli legati al rendiconto
					gd.setDataCancellazioneIfNotSet(now);
					
					if(gd.getSiacRGiustificativoStatos()!=null){
						for(SiacRGiustificativoStato rgs : gd.getSiacRGiustificativoStatos()){
							rgs.setDataCancellazioneIfNotSet(now);
						}
					}
				}
			}
		}
		
		if(rAttuale.getSiacTGiustificativos()!=null ){
			for(SiacTGiustificativo t : rAttuale.getSiacTGiustificativos()){
				t.setDataCancellazioneIfNotSet(now);
			}
		}
		
		entityManager.flush();
		
		
		//inserisco nuovi elementi
		if(r.getSiacRRichiestaEconClasses() != null){
			for(SiacRRichiestaEconClass c: r.getSiacRRichiestaEconClasses()){
				c.setDataModificaInserimento(now);
			}
			
		}
		
		if(r.getSiacRRichiestaEconSubdocs() != null) {
			for(SiacRRichiestaEconSubdoc s : r.getSiacRRichiestaEconSubdocs()) {
				s.setDataModificaInserimento(now);
			}
		}
		
		if(r.getSiacRRichiestaEconSogs() != null){
			for(SiacRRichiestaEconSog s: r.getSiacRRichiestaEconSogs()){
				s.setDataModificaInserimento(now);
			}
			
		}
		
		if(r.getSiacRRichiestaEconStatos()!= null){
			for(SiacRRichiestaEconStato s: r.getSiacRRichiestaEconStatos()){
				s.setDataModificaInserimento(now);
			}
			
		}
		
		if(r.getSiacRRichiestaEconMovgests()!= null){
			for(SiacRRichiestaEconMovgest s: r.getSiacRRichiestaEconMovgests()){
				s.setDataModificaInserimento(now);
			}
			
		}
		
		if(r.getSiacTTrasfMisses()!=null ){
			for(SiacTTrasfMiss t : r.getSiacTTrasfMisses()){
				t.setDataModificaInserimento(now);
				t.setUid(null);
			}
		}
		
		if(r.getSiacTRichiestaEconSospesas()!=null ){
			for(SiacTRichiestaEconSospesa t : r.getSiacTRichiestaEconSospesas()){
				t.setDataModificaInserimento(now);
				t.setUid(null);
			}
		}
		
		if(r.getSiacTMovimentos()!=null ){
			for(SiacTMovimento t : r.getSiacTMovimentos()){
				t.setDataModificaInserimento(now);
				t.setUid(null);
				
				// Relazione del movimento
				if( t.getSiacRMovimentoModpags()!=null) {
					for(SiacRMovimentoModpag srmm : t.getSiacRMovimentoModpags()) {
						srmm.setDataModificaInserimento(now);
						srmm.setUid(null);
					}
				}
			}
		}
		
		if(r.getSiacTGiustificativoDets()!=null ){
			for(SiacTGiustificativoDet gd : r.getSiacTGiustificativoDets()){
				gd.setDataModificaInserimento(now);
				gd.setUid(null);
				
				if(gd.getSiacRGiustificativoStatos()!=null){
					for(SiacRGiustificativoStato rgs : gd.getSiacRGiustificativoStatos()){
						rgs.setDataModificaInserimento(now);
					}
				}
			}
		}
		
		if(r.getSiacTGiustificativos()!=null ){
			for(SiacTGiustificativo t : r.getSiacTGiustificativos()){
				t.setDataModificaInserimento(now);
				t.setUid(null);
			}
		}
		
		super.update(r);
		return r;
	}


	@Override
	public Page<SiacTRichiestaEcon> ricercaSinteticaRichiestaEconomale(
			Integer enteProprietarioId, Integer bilId, Integer riceconTipoId, Integer cassaeconId ,
			Integer riceconNumero, Date dataCreazioneDa, Date dataCreazioneA,
			Date dataMovimentoDa, Date dataMovimentoA, Integer riceconsNumero,
			Integer movtNumero, Integer soggettoId, String riceconMatricola, String riceconDesc,
			SiacDRichiestaEconStatoEnum siacDRichiestaEconStatoEnum,
			List<ClassificatoreGenerico> classificatoriGenerici, Pageable pageable) {
		
		final String methodName = "ricercaSinteticaRichiestaEconomale";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaRichiesta(jpql, param, enteProprietarioId, bilId, riceconTipoId, cassaeconId, riceconNumero,
				dataCreazioneDa, dataCreazioneA, dataMovimentoDa, dataMovimentoA, riceconsNumero, movtNumero, soggettoId, riceconMatricola,  riceconDesc,
				siacDRichiestaEconStatoEnum, classificatoriGenerici);
		jpql.append(" ORDER BY r.riceconNumero ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	@Override
	public BigDecimal ricercaSinteticaRichiestaEconomaleTotale(
			Integer enteProprietarioId, Integer bilId, Integer riceconTipoId,
			Integer cassaeconId, Integer riceconNumero,
			Date dataCreazioneRichiestaEconomaleDa, Date dataCreazioneRichiestaEconomaleA,
			Date dataMovimentoDa, Date dataMovimentoA, Integer riceconsNumero,
			Integer movtNumero, Integer soggettoId, String riceconMatricola, String riceconDesc,
			SiacDRichiestaEconStatoEnum siacDRichiestaEconStatoEnum,
			List<ClassificatoreGenerico> classificatoriGenerici) {
		
		final String methodName = "ricercaSinteticaRichiestaEconomaleTotale";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" SELECT COALESCE(SUM(r.riceconImporto), 0) ");
		
		componiQueryRicercaSinteticaRichiesta(jpql, param, enteProprietarioId, bilId, riceconTipoId, cassaeconId, riceconNumero,
				dataCreazioneRichiestaEconomaleDa, dataCreazioneRichiestaEconomaleA, dataMovimentoDa, dataMovimentoA, riceconsNumero, movtNumero, soggettoId, riceconMatricola, riceconDesc,
				siacDRichiestaEconStatoEnum, classificatoriGenerici);
		
		Query query = createQuery(jpql.toString(), param);
		BigDecimal result = (BigDecimal) query.getSingleResult();
		
		log.info(methodName, "returning: "+result);
		
		return result;
	}


	private void componiQueryRicercaSinteticaRichiesta(StringBuilder jpql,
			Map<String, Object> param, Integer enteProprietarioId, Integer bilId,
			Integer riceconTipoId, Integer cassaeconId, Integer riceconNumero,
			Date dataCreazioneDa, Date dataCreazioneA,
			Date dataMovimentoDa, Date dataMovimentoA, Integer riceconsNumero,
			Integer movtNumero, Integer soggettoId, String riceconMatricola, String riceconDesc,
			SiacDRichiestaEconStatoEnum siacDRichiestaEconStatoEnum,
			List<ClassificatoreGenerico> classificatoriGenerici) {
		
		
		jpql.append("FROM SiacTRichiestaEcon r ");
		jpql.append(" WHERE ");
		jpql.append(" r.dataCancellazione IS NULL ");
		jpql.append(" AND r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND r.siacTCassaEcon.cassaeconId = :cassaeconId ");
		jpql.append(" AND r.siacTBil.bilId = :bilId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		param.put("cassaeconId", cassaeconId);
		param.put("bilId", bilId);
		
		if(riceconTipoId != null && riceconTipoId != 0){
			jpql.append(" AND r.siacDRichiestaEconTipo.riceconTipoId = :riceconTipoId ");
			
			param.put("riceconTipoId", riceconTipoId);
		}
		
		if(riceconNumero != null && riceconNumero != 0){
			jpql.append(" AND r.riceconNumero = :riceconNumero ");
			
			param.put("riceconNumero", riceconNumero);
		}
		
		// SIAC-4497
		if(dataCreazioneDa != null){
			jpql.append(" AND " + Utility.toJpqlDateParamGreaterOrEquals("r.dataCreazione", ":dataCreazioneDa"));
			param.put("dataCreazioneDa", dataCreazioneDa);
		}
		if(dataCreazioneA != null){
			jpql.append(" AND " + Utility.toJpqlDateParamLesserOrEquals("r.dataCreazione", ":dataCreazioneA"));
			param.put("dataCreazioneA", dataCreazioneA);
		}
		
		if(riceconsNumero != null && riceconsNumero != 0){
			jpql.append(" AND EXISTS (FROM r.siacTRichiestaEconSospesas rs ");
			jpql.append("			    WHERE rs.dataCancellazione IS NULL ");
			jpql.append("				AND rs.riceconsNumero = :riceconsNumero ");
			jpql.append("			  	) ");
			
			param.put("riceconsNumero", riceconsNumero);
		}
		
		if(movtNumero != null && movtNumero != 0){
			jpql.append(" AND EXISTS (FROM r.siacTMovimentos rm ");
			jpql.append("			    WHERE rm.dataCancellazione IS NULL ");
			jpql.append("				AND rm.movtNumero = :movtNumero ");
			jpql.append("			  	) ");
			
			param.put("movtNumero", movtNumero);
		}
		
		// FIXME: controllare se passare a ExtendedJpaDao
		// SIAC-4552
		if(dataMovimentoDa != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM r.siacTMovimentos rm ");
			jpql.append("     WHERE rm.dataCancellazione IS NULL ");
			jpql.append("     AND rm.movtData >= :movtDataDa ");
			jpql.append(" ) ");
			
			param.put("movtDataDa", dataMovimentoDa);
		}
		if(dataMovimentoA != null) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM r.siacTMovimentos rm ");
			jpql.append("     WHERE rm.dataCancellazione IS NULL ");
			jpql.append("     AND rm.movtData <= :movtDataA ");
			jpql.append(" ) ");
			
			param.put("movtDataA", dataMovimentoA);
		}
		
		
		if(soggettoId!= null && soggettoId != 0){
			jpql.append(" AND EXISTS (FROM r.siacRRichiestaEconSogs rs ");
			jpql.append("			    WHERE rs.dataCancellazione IS NULL ");
			jpql.append("				AND rs.siacTSoggetto.soggettoId = :soggettoId ");
			jpql.append("			  	) ");
			
			param.put("soggettoId", soggettoId);
		}
		
		if(!StringUtils.isEmpty(riceconMatricola)){
			jpql.append(" AND r.riceconMatricola = :riceconMatricola ");
			
			param.put("riceconMatricola", riceconMatricola);
		}
		
		if(!StringUtils.isEmpty(riceconDesc)){
			jpql.append(" AND " + Utility.toJpqlSearchLike("r.riceconDesc", "CONCAT('%', :riceconDesc, '%')") + " ");
			
			param.put("riceconDesc", riceconDesc);
		}
		
		if(siacDRichiestaEconStatoEnum != null && siacDRichiestaEconStatoEnum.getCodice() != null){
			jpql.append(" AND EXISTS (FROM r.siacRRichiestaEconStatos rs ");
			jpql.append("			    WHERE rs.dataCancellazione IS NULL ");
			jpql.append("				AND rs.siacDRichiestaEconStato.riceconStatoCode = :riceconStatoCode ");
			jpql.append("			  	) ");
			
			param.put("riceconStatoCode", siacDRichiestaEconStatoEnum.getCodice());
		}
		
		if(classificatoriGenerici != null){
			for(ClassificatoreGenerico classif : classificatoriGenerici){
				if(classif.getUid() != 0){
					jpql.append(" AND EXISTS (FROM r.siacRRichiestaEconClasses rc ");
					jpql.append("			    WHERE rc.dataCancellazione IS NULL ");
					jpql.append("				AND rc.siacTClass.classifId = :classifId ");
					jpql.append("			  	) ");
					
					param.put("classifId", classif.getUid());
				}
			}
			
		}
		
	}


	
	
	
	

}

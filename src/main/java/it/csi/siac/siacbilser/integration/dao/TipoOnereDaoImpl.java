/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacDOnere;
import it.csi.siac.siacbilser.integration.entity.SiacROnereAttivita;
import it.csi.siac.siacbilser.integration.entity.SiacROnereAttr;
import it.csi.siac.siacbilser.integration.entity.SiacROnereCausale;
import it.csi.siac.siacbilser.integration.entity.SiacROnereSommaNonSoggettaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacROnereSplitreverseIvaTipo;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class DocumentoDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TipoOnereDaoImpl extends JpaDao<SiacDOnere, Integer> implements TipoOnereDao {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.dao.DocumentoDao#create(it.csi.siac.siacbilser.integration.entity.SiacTDoc)
	 */
	public SiacDOnere create(SiacDOnere o){
		
		Date now = new Date();
		o.setDataModificaInserimento(now);
		
		if(o.getSiacROnereAttivitas()!=null){
			for(SiacROnereAttivita r : o.getSiacROnereAttivitas()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(o.getSiacROnereAttrs()!=null){
			for(SiacROnereAttr r : o.getSiacROnereAttrs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(o.getSiacROnereSommaNonSoggettaTipos()!=null){
			for(SiacROnereSommaNonSoggettaTipo r : o.getSiacROnereSommaNonSoggettaTipos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(o.getSiacROnereCausales()!=null){
			for(SiacROnereCausale r : o.getSiacROnereCausales()){
				r.setDataModificaInserimento(now);
				
				//Gestisco l'inserimento delle causali collegate.
				SiacDCausale siacDCausale = r.getSiacDCausale();
				if(siacDCausale.isOnCascade()){
					siacDCausale.setDataModificaInserimento(now);
					CausaleDaoImpl.setDataModificaInserimento(siacDCausale, now);
					siacDCausale.setUid(null);
				}
			}
		}
		
		if(o.getSiacROnereSplitreverseIvaTipos()!=null){
			for(SiacROnereSplitreverseIvaTipo r : o.getSiacROnereSplitreverseIvaTipos()){
				r.setDataModificaInserimento(now);
			}
		}

		o.setUid(null);		
		super.save(o);
		return o;
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.JpaDao#update(java.lang.Object)
	 */
	public SiacDOnere update(SiacDOnere o){
		
		SiacDOnere oAttuale = this.findById(o.getUid());
		
		Date now = new Date();
		o.setDataModificaAggiornamento(now);		
		
		
		//cancellazione elementi collegati	
		if(oAttuale.getSiacROnereAttivitas()!=null){
			for(SiacROnereAttivita r : oAttuale.getSiacROnereAttivitas()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(oAttuale.getSiacROnereAttrs()!=null){
			for(SiacROnereAttr r : oAttuale.getSiacROnereAttrs()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(oAttuale.getSiacROnereSommaNonSoggettaTipos()!=null){
			for(SiacROnereSommaNonSoggettaTipo r : oAttuale.getSiacROnereSommaNonSoggettaTipos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
		
		if(oAttuale.getSiacROnereCausales()!=null){
			for(SiacROnereCausale r : oAttuale.getSiacROnereCausales()){
				r.setDataCancellazioneIfNotSet(now);
				
				
				//Gestisco la cancellazione delle causali collegate.
				SiacDCausale siacDCausale = r.getSiacDCausale();
				
				if(!siacDCausale.isModello770()){
					siacDCausale.setDataCancellazioneIfNotSet(now);
					CausaleDaoImpl.setDataCancellazioneIfNotSet(siacDCausale, now);
				}
				
			}
		}
		
		if(oAttuale.getSiacROnereSplitreverseIvaTipos()!=null){
			for(SiacROnereSplitreverseIvaTipo r : oAttuale.getSiacROnereSplitreverseIvaTipos()){
				r.setDataCancellazioneIfNotSet(now);
			}
		}
	
		//inserimento elementi nuovi	
		if(o.getSiacROnereAttivitas()!=null){
			for(SiacROnereAttivita r : o.getSiacROnereAttivitas()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(o.getSiacROnereAttrs()!=null){
			for(SiacROnereAttr r : o.getSiacROnereAttrs()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(o.getSiacROnereSommaNonSoggettaTipos()!=null){
			for(SiacROnereSommaNonSoggettaTipo r : o.getSiacROnereSommaNonSoggettaTipos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		if(o.getSiacROnereCausales()!=null){
			for(SiacROnereCausale r : o.getSiacROnereCausales()){
				r.setDataModificaInserimento(now);
				
				//Gestisco l'inserimento delle causali collegate.
				SiacDCausale siacDCausale = r.getSiacDCausale();
				
				if(siacDCausale.isOnCascade()) {
						if(siacDCausale.getUid()==0){ //Sto inserendo la causale
							siacDCausale.setDataModificaInserimento(now);
							siacDCausale.setUid(null);
						} else { // Sto aggiornando la causale
							siacDCausale.setDataModificaAggiornamento(now);
						}
						CausaleDaoImpl.setDataModificaInserimento(siacDCausale, now);
				}
				
			}
		}
		
		if(o.getSiacROnereSplitreverseIvaTipos()!=null){
			for(SiacROnereSplitreverseIvaTipo r : o.getSiacROnereSplitreverseIvaTipos()){
				r.setDataModificaInserimento(now);
			}
		}
		
		super.update(o);
		return o;
	}

	
	public Page<SiacDOnere> ricercaSinteticaTipiOnere(Integer onereTipoId, Integer enteProprietrioId,  Boolean corsoDiValidita, Pageable pageable){
		final String methodName = "ricercaSinteticaTipiOnere";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		Date inizioDataAttuale = new Date();
		inizioDataAttuale = DateUtils.setHours(inizioDataAttuale, 0);
		inizioDataAttuale = DateUtils.setMinutes(inizioDataAttuale, 0);
		inizioDataAttuale = DateUtils.setSeconds(inizioDataAttuale, 0);
		inizioDataAttuale = DateUtils.setMilliseconds(inizioDataAttuale, 0);
		
		componiQueryRicercaSinteticaTipoOnere( jpql, param, onereTipoId , enteProprietrioId, corsoDiValidita, inizioDataAttuale);
		
		jpql.append(" ORDER BY c.onereCode  ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	
	private void componiQueryRicercaSinteticaTipoOnere(StringBuilder jpql, Map<String, Object> param, Integer onereTipoId, 
			Integer enteProprietarioId, Boolean corsoDiValidita, Date now) {
		
		jpql.append(" FROM SiacDOnere c ");
		jpql.append(" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND c.dataCancellazione IS NULL ");
		param.put("enteProprietarioId", enteProprietarioId);
		
		if(onereTipoId != null && onereTipoId != 0){
			jpql.append(" AND c.siacDOnereTipo.onereTipoId = :onereTipoId ");
			param.put("onereTipoId", onereTipoId);
		}
	
		if(Boolean.TRUE.equals(corsoDiValidita)){
			jpql.append(" AND (dataFineValidita IS NULL OR dataFineValidita >= :now) ");
			param.put("now", now);
		}
		
	}
	
}

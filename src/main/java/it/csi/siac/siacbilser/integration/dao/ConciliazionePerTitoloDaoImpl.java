/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneTitolo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDConciliazioneClasseEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class ConciliazionePerTitoloDaoImpl.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/10/2015
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConciliazionePerTitoloDaoImpl extends JpaDao<SiacRConciliazioneTitolo, Integer> implements ConciliazionePerTitoloDao {
	
	@Override
	public SiacRConciliazioneTitolo create(SiacRConciliazioneTitolo r) {
		Date now = new Date();
		r.setDataModificaInserimento(now);
		r.setUid(null);
		super.save(r);
		return r;
	}

	@Override
	public SiacRConciliazioneTitolo update(SiacRConciliazioneTitolo r) {
		Date now = new Date();
		r.setDataModificaAggiornamento(now);
		super.update(r);
		return r;
	}

	@Override
	public void elimina(SiacRConciliazioneTitolo entity) {
		SiacRConciliazioneTitolo e = this.findById(entity.getUid());
		Date now = new Date();
		e.setDataCancellazioneIfNotSet(now);
	}

	@Override
	public Page<SiacRConciliazioneTitolo> ricercaSinteticaConciliazioniPerTitolo(
			int enteProprietarioId,
			SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum,
			Integer classifId,
			Integer titoloId,
			Integer tipologiaId,
			Pageable pageable) {
		
		final String methodName = "ricercaSinteticaConciliazioniPerTitolo";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaConciliazioniPerTitolo( jpql, param, enteProprietarioId, siacDConciliazioneClasseEnum, classifId, titoloId, tipologiaId);
		
//		jpql.append(" ORDER BY  ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}

	private void componiQueryRicercaSinteticaConciliazioniPerTitolo( StringBuilder jpql, Map<String, Object> param, int enteProprietarioId, SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum,
			Integer classifId, Integer titoloId, Integer tipologiaId) {
		
		jpql.append("FROM SiacRConciliazioneTitolo ct ");
		jpql.append(" WHERE ct.dataCancellazione IS NULL");
		jpql.append(" AND ct.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND ct.siacDConciliazioneClasse.concclaCode = :concclaCode ");
		
		param.put("concclaCode", siacDConciliazioneClasseEnum.getCodice());
		param.put("enteProprietarioId", enteProprietarioId);
		
		if( classifId != null && classifId != 0){
			jpql.append(" AND ct.siacTClass.classifId = :classifId ");
			param.put("classifId", classifId);			
		}
		
		if(tipologiaId != null &&  tipologiaId != 0){
			
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM ct.siacTClass.siacRClassFamTreesFiglio ft ");
			jpql.append("     WHERE ft.siacTClassPadre.classifId = :tipologiaId ");
			jpql.append("     AND ft.dataCancellazione IS NULL ");
			jpql.append("     AND ft.siacTClassPadre.siacDClassTipo.classifTipoCode = :tipologiaCode ");
			jpql.append(" ) ");
			
			param.put("tipologiaId", tipologiaId);
			param.put("tipologiaCode", SiacDClassTipoEnum.Tipologia.getCodice());
		}
		
		if(titoloId != null &&  titoloId != 0){
			
			jpql.append(" AND ( ");
			jpql.append("     EXISTS ( ");
			jpql.append("         FROM ct.siacTClass.siacRClassFamTreesFiglio ft");
			jpql.append("         WHERE ft.siacTClassPadre.classifId = :titoloId ");
			jpql.append("         AND ft.dataCancellazione IS NULL ");
			jpql.append("         AND ft.siacTClassPadre.siacDClassTipo.classifTipoCode = :titoloSpesaCode ");
			jpql.append("     ) ");
			
			jpql.append("     OR EXISTS ( ");
			jpql.append("         FROM ct.siacTClass.siacRClassFamTreesFiglio ft ");
			jpql.append("         WHERE ft.dataCancellazione IS NULL ");
			jpql.append("         AND EXISTS ( ");
			jpql.append("             FROM ft.siacTClassPadre.siacRClassFamTreesFiglio ftt ");
			jpql.append("             WHERE ftt.siacTClassPadre.classifId = :titoloId ");
			jpql.append("             AND ftt.dataCancellazione IS NULL ");
			jpql.append("             AND ftt.siacTClassPadre.siacDClassTipo.classifTipoCode = :titoloEntrataCode ");
			jpql.append("         ) ");
			jpql.append("     ) ");
			jpql.append(" ) ");
			
			
			param.put("titoloId", titoloId);
			param.put("titoloSpesaCode", SiacDClassTipoEnum.TitoloSpesa.getCodice());
			param.put("titoloEntrataCode", SiacDClassTipoEnum.TitoloEntrata.getCodice());
		}
		
	}
}

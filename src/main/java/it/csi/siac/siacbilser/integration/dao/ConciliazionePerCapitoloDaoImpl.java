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

import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneCapitolo;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class ConciliazionePerCapitoloDaoImpl.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/10/2015
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConciliazionePerCapitoloDaoImpl extends JpaDao<SiacRConciliazioneCapitolo, Integer> implements ConciliazionePerCapitoloDao {
	
	@Override
	public SiacRConciliazioneCapitolo create(SiacRConciliazioneCapitolo r) {
		Date now = new Date();
		r.setDataModificaInserimento(now);
		r.setUid(null);
		super.save(r);
		return r;
	}

	@Override
	public SiacRConciliazioneCapitolo update(SiacRConciliazioneCapitolo r) {
		Date now = new Date();
		r.setDataModificaAggiornamento(now);
		super.update(r);
		return r;
	}

	@Override
	public void elimina(SiacRConciliazioneCapitolo entity) {
		SiacRConciliazioneCapitolo e = this.findById(entity.getUid());
		Date now = new Date();
		e.setDataCancellazioneIfNotSet(now);
	}

	@Override
	public Page<SiacRConciliazioneCapitolo> ricercaSinteticaConciliazioniPerCapitolo(int enteProprietarioId, Integer bilElemId, Pageable pageable) {
		final String methodName = "ricercaSinteticaConciliazioniPerCapitolo";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		componiQueryRicercaSinteticaConciliazioniPerCapitolo(jpql, param, enteProprietarioId, bilElemId);
		
//		jpql.append(" ORDER BY  ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		
		return getPagedList(jpql.toString(), param, pageable);
	}
	
	
	private void componiQueryRicercaSinteticaConciliazioniPerCapitolo(StringBuilder jpql, Map<String, Object> param, int enteProprietarioId, Integer bilElemId) {
		
		jpql.append(" FROM SiacRConciliazioneCapitolo cc ");
		jpql.append(" WHERE cc.dataCancellazione IS NULL");
		jpql.append(" AND cc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		
		param.put("enteProprietarioId", enteProprietarioId);
		
		if( bilElemId != null && bilElemId != 0){
			jpql.append(" AND cc.siacTBilElem.elemId = :elemId ");
			
			param.put("elemId", bilElemId);			
		}
	}		
}

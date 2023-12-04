/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDOnereAttivita;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class CausaleDaoImpl.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AttivitaOnereDaoImpl extends JpaDao<SiacDOnereAttivita, Integer> implements AttivitaOnereDao {
	
	@Override
	public List<SiacDOnereAttivita> ricercaAttivitaOnereByEnteProprietarioAndTipoOnere(Integer enteProprietarioId, Integer onereId) {
		final StringBuilder jpql = new StringBuilder();
		final Map<String, Object> params = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacDOnereAttivita a ")
			.append(" WHERE a.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		params.put("enteProprietarioId", enteProprietarioId);
		
		if(onereId != null) {
			jpql.append(" AND EXISTS ( ")
				.append("     FROM a.siacROnereAttivitas r ")
				.append("     WHERE r.siacDOnere.onereId = :onereId ")
				.append("     AND r.dataCancellazione IS NULL ")
				.append("     AND r.siacDOnere.dataCancellazione IS NULL ")
				.append(" ) ");
			params.put("onereId", onereId);
		}
		
		jpql.append(" AND a.dataCancellazione IS NULL ")
			.append(" AND a.dataInizioValidita < CURRENT_TIMESTAMP ")
			.append(" AND (a.dataFineValidita IS NULL OR a.dataFineValidita > CURRENT_TIMESTAMP) ")
			.append(" ORDER BY a.onereAttCode ");
		
		Query query = createQuery(jpql.toString(), params);
		
		@SuppressWarnings("unchecked")
		List<SiacDOnereAttivita> result = query.getResultList();
		return result;
	}
	
}

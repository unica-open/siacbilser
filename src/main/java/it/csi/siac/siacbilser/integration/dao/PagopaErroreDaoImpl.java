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

import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.PagopaDRiconciliazioneErrore;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PagopaErroreDaoImpl extends ExtendedJpaDao<PagopaDRiconciliazioneErrore, Integer> implements PagopaErroreDao{
	
	@Override
	public List<PagopaDRiconciliazioneErrore> findPagopaErrore(Integer idEnte, Integer idErrore){
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuilder jpql = new StringBuilder();
		
		jpql.append("FROM PagopaDRiconciliazioneErrore err ");
		jpql.append(" WHERE ");
		jpql.append(" err.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		jpql.append(" AND err.pagopa_ric_errore_id = :idErrore ");

		param.put("enteProprietarioId", idEnte);
		param.put("idErrore", idErrore);
		
		Query query = createQuery(jpql.toString(), param);
		
		@SuppressWarnings("unchecked")
		List<PagopaDRiconciliazioneErrore> list = query.getResultList();
		return list;
	}
}

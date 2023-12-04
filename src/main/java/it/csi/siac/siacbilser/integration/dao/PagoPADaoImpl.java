/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PagoPADaoImpl extends ExtendedJpaDao<SiacTProvCassa, Integer> implements PagoPADao {

	@Override
	public List<SiacTProvCassa> findProvvisoriCassa(Integer idEnte, Integer annoEsercizio, List<String> causali, BigDecimal numeroDa, 
			BigDecimal numeroA, Date dataEmissioneDa, Date dataEmissioneA, Boolean isStatoValido) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder jpql = new StringBuilder()
				.append("SELECT DISTINCT pc FROM SiacTProvCassa pc ")
				.append(" WHERE pc.siacTEnteProprietario.enteProprietarioId=:idEnte ");
		
		param.put("idEnte", idEnte);
		
		if (annoEsercizio != null) {
			jpql.append(" AND pc.provcAnno=:annoEsercizio ");
			param.put("annoEsercizio", annoEsercizio);
		} else {
			jpql.append(" AND pc.provcAnno BETWEEN :annoEsercizio-1 AND :annoEsercizio ");
			param.put("annoEsercizio", Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
		}

		if (CollectionUtils.isNotEmpty(causali)) {
			jpql.append(" AND (");
			
			for (String causale : causali) {
				jpql.append(" UPPER(pc.provcCausale) LIKE UPPER('%").append(StringEscapeUtils.escapeSql(causale)).append("%') OR ");
			}
			jpql.append(" 0=1) ");
		}
		
		if (numeroDa != null) {
			jpql.append(" AND pc.provcNumero>=:numeroDa ");
			param.put("numeroDa", numeroDa);
		}

		if (numeroA != null) {
			jpql.append(" AND pc.provcNumero<=:numeroA ");
			param.put("numeroA", numeroA);
		}
		
		if (dataEmissioneDa != null) {
			jpql.append(" AND DATE_TRUNC('day', pc.provcDataEmissione)>=:dataEmissioneDa ");
			param.put("dataEmissioneDa", dataEmissioneDa);
		}

		if (dataEmissioneA != null) {
			jpql.append(" AND DATE_TRUNC('day', pc.provcDataEmissione)<=:dataEmissioneA ");
			param.put("dataEmissioneA", dataEmissioneA);
		}

		if (isStatoValido != null) {
			jpql.append(" AND pc.provcDataAnnullamento IS ")
				.append(isStatoValido ? "" : "NOT")
				.append(" NULL ");
		}

		Query query = createQuery(jpql.toString(), param);
		
		@SuppressWarnings("unchecked")
		List<SiacTProvCassa> list = query.getResultList();
		return list;
	}
	
}

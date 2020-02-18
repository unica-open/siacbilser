/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.base.ExtendedJpaDao;
import it.csi.siac.siacbilser.integration.entity.SiacDPccUfficio;

/**
 * The Class CodicePCCDaoImpl.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/06/2015
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CodiceUfficioDestinatarioPCCDaoImpl extends ExtendedJpaDao<SiacDPccUfficio, Integer> implements CodiceUfficioDestinatarioPCCDao {
	
	@Override
	public List<SiacDPccUfficio> findByEnteProprietarioAndStruttureAmministrativoContabili(Integer enteProprietarioId, Collection<Integer> classifIds) {
		final String methodName = "findByEnteProprietarioAndStruttureAmministrativoContabili";
		
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacDPccUfficio sdpu ");
		jpql.append(" WHERE sdpu.dataCancellazione IS NULL ");
		jpql.append(" AND sdpu.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		parameters.put("enteProprietarioId", enteProprietarioId);
		
		if(classifIds != null && !classifIds.isEmpty()) {
			// Filtro anche per i classificatori
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM sdpu.siacRPccUfficioClasses srpuc ");
			jpql.append("     WHERE srpuc.dataCancellazione IS NULL ");
			jpql.append("     AND srpuc.siacTClass.classifId IN (:classifIds) ");
			jpql.append(" ) ");
			
			parameters.put("classifIds", classifIds);
		}
		jpql.append(" ORDER BY sdpu.pccuffCode ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		TypedQuery<SiacDPccUfficio> query = createQuery(jpql.toString(), parameters, SiacDPccUfficio.class);
		return query.getResultList();
	}
	
}

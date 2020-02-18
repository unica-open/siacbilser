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
import it.csi.siac.siacbilser.integration.entity.SiacDPccCodice;

/**
 * The Class CodicePCCDaoImpl.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/06/2015
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CodicePCCDaoImpl extends ExtendedJpaDao<SiacDPccCodice, Integer> implements CodicePCCDao {
	
	@Override
	public List<SiacDPccCodice> findByEnteProprietarioAndStruttureAmministrativoContabiliAndCodiceUfficioPCC(Integer enteProprietarioId, Collection<Integer> classifIds, Integer codiceUfficioId) {
		final String methodName = "findByEnteProprietarioAndStruttureAmministrativoContabili";
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		jpql.append(" FROM SiacDPccCodice sdpc ");
		jpql.append(" WHERE sdpc.dataCancellazione IS NULL ");
		jpql.append(" AND sdpc.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ");
		parameters.put("enteProprietarioId", enteProprietarioId);
		
		if(classifIds != null && !classifIds.isEmpty()) {
			// Filtro anche per i classificatori
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM sdpc.siacRPccCodiceClasses srpcc ");
			jpql.append("     WHERE srpcc.dataCancellazione IS NULL ");
			jpql.append("     AND srpcc.siacTClass.classifId IN (:classifIds) ");
			jpql.append(" ) ");
			
			parameters.put("classifIds", classifIds);
		}
		if(codiceUfficioId!= null && codiceUfficioId != 0) {
			jpql.append(" AND EXISTS ( ");
			jpql.append("     FROM sdpc.siacRPccUfficioCodices srpuc ");
			jpql.append("     WHERE srpuc.dataCancellazione IS NULL ");
			jpql.append("     AND srpuc.siacDPccUfficio.pccuffId = :pccuffId ");
			jpql.append(" ) ");
			
			parameters.put("pccuffId", codiceUfficioId);
		}
		
		jpql.append(" ORDER BY sdpc.pcccodCode ");
		
		log.debug(methodName, "JPQL to execute: " + jpql.toString());
		TypedQuery<SiacDPccCodice> query = createQuery(jpql.toString(), parameters, SiacDPccCodice.class);
		return query.getResultList();
	}

}

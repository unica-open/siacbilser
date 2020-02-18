/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccorser.integration.dao;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import it.csi.siac.siaccommonser.integration.dao.base.BaseDao;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * Superclasse di tutti i dao modulo core.
 *
 * @author AR
 */
public abstract class BaseDaoImpl implements BaseDao{

	/** The entity manager. */
	@PersistenceContext
	protected EntityManager entityManager;

	/** The log. */
	protected static final Logger log = Logger.getLogger(BaseDaoImpl.class.getName());

	/**
	 * Find by id and lock.
	 *
	 * @param <T> the generic type
	 * @param cls the cls
	 * @param uid the uid
	 * @return the t
	 */
	protected <T> T findByIdAndLock(Class<T> cls, int uid){
		return entityManager.find(cls, uid, LockModeType.PESSIMISTIC_WRITE);
	}

	/**
	 * Gets the paged list.
	 *
	 * @param <T> the generic type
	 * @param jpql the jpql
	 * @param parameters the parameters
	 * @param pageParams the page params
	 * @return the paged list
	 */
	protected <T> ListaPaginata<T> getPagedList(String jpql, Map<String, Object> parameters,
			ParametriPaginazione pageParams){
		Query qn = createQuery(String.format("SELECT COUNT(*) FROM (%s)", jpql), parameters);
		Number count = (Number) qn.getSingleResult();

		Query query = createQuery(jpql, parameters);
		
		query.setFirstResult(pageParams.getPrimoElemento());
		query.setMaxResults(pageParams.getElementiPerPagina());

		ListaPaginataImpl<T> pagedList = new ListaPaginataImpl<T>(query.getResultList());
		
		pagedList.setTotaleElementi(count.intValue());

		return pagedList;
	}

	/**
	 * Creates the query.
	 *
	 * @param jpql the jpql
	 * @param parameters the parameters
	 * @return the query
	 */
	protected Query createQuery(String jpql, Map<String, Object> parameters){
		Query query = entityManager.createQuery(jpql);

		for (Map.Entry<String, Object> param : parameters.entrySet()){
			query.setParameter(param.getKey(), param.getValue());
		}
			

		return query;
	}

}

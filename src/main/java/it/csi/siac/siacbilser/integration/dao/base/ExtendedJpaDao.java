/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.enumeration.OrderByEnum;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;

/**
 * Estende le funzionalità di base di JpaDao.
 * 
 * Aggiunto supporto alla paginazione per le Native Query.
 * 
 * @author Domenico
 *
 * @param <E>
 * @param <PK>
 */
public class ExtendedJpaDao<E, PK> extends JpaDao<E, PK> {

	protected String idField;
	
	protected ExtendedJpaDao() {
		super();
		this.idField = getIdField(entityClass);
	}
	
	private String getIdField(Class<E> entityClass) {
		Entity entity = entityClass.getAnnotation(Entity.class);
		if(entity == null) {
			return null;
		}
		Field[] fields = entityClass.getDeclaredFields();
		for(Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if(id != null) {
				return field.getName();
			}
		}
		return null;
	}
	
	private String getIdColumn(Class<E> entityClass) {
		Entity entity = entityClass.getAnnotation(Entity.class);
		if(entity == null) {
			return null;
		}
		Field[] fields = entityClass.getDeclaredFields();
		for(Field field : fields) {
			Id id = field.getAnnotation(Id.class);
			if(id != null) {
				Column column = field.getAnnotation(Column.class); 
				return column != null ? column.name() : null;
			}
		}
		return null;
	}
	
	protected Page<E> getPageByPks(Collection<PK> uids, Pageable pageable, long totalElements) {
		if(idField == null) {
			throw new IllegalStateException("Impossibile effettuare una ricerca per PKs implicita: non e' stato possibile determinare la colonna della PK");
		}
		List<E> resultList = new ArrayList<E>();
		
		if(totalElements > 0) {
			StringBuilder jpql = new StringBuilder();
			Map<String, Object> param = new HashMap<String, Object>();
			
			jpql.append(" FROM " + entityClass.getSimpleName() + " e WHERE e." + idField + " IN :uids ");
			param.put("uids", uids);
			
			TypedQuery<E> query = entityManager.createQuery(jpql.toString(), entityClass);
			setQueryParameters(query, param);
			resultList = query.getResultList();
		}
		return new PageImpl<E>(resultList, pageable, totalElements);
	}
	
	protected List<E> getByPks(Collection<PK> uids) {
		if(idField == null) {
			throw new IllegalStateException("Impossibile effettuare una ricerca per PKs implicita: non e' stato possibile determinare la colonna della PK");
		}
		StringBuilder jpql = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		
		jpql.append(" FROM " + entityClass.getSimpleName() + " e WHERE e." + idField + " IN :uids ");
		param.put("uids", uids);
		
		TypedQuery<E> query = entityManager.createQuery(jpql.toString(), entityClass);
		setQueryParameters(query, param);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	protected <T> Page<T> getNativePagedList(String jpql, Map<String, Object> parameters, Pageable pageable, Class<T> entityClass) {
		String jpqlCount = getNativeCountQuery(jpql);
		Query qn = createNativeQuery(jpqlCount, parameters);

		long count = ((Number) qn.getSingleResult()).longValue();

		List<T> resultList = new ArrayList<T>();
		if (count > 0L) {
			Query query = createNativeQuery(jpql, parameters, entityClass);
			query.setFirstResult(pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
			resultList = query.getResultList();
		}

		Page<T> pagedList = new PageImpl<T>(resultList, pageable, count);
		return pagedList;
	}
	
	protected <T> Page<T> getNativePagedList(String jpql, Map<String, Object> parameters, Pageable pageable) {
		return getNativePagedList(jpql, parameters, pageable, false);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> Page<T> getNativePagedList(String jpql, Map<String, Object> parameters, Pageable pageable, boolean keepOriginalQuerySyntax) {
		String jpqlCount;
		if(!keepOriginalQuerySyntax) {
			jpqlCount = getNativeCountQuery(jpql);
		} else {
			jpqlCount = getNativeCountQueryKeepSyntax(jpql);
		}
		Query qn = createNativeQuery(jpqlCount, parameters);

		long count = ((Number) qn.getSingleResult()).longValue();

		List<T> resultList = new ArrayList<T>();
		if (count > 0L) {
			Query query = createNativeQuery(jpql, parameters);
			query.setFirstResult(pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
			resultList = query.getResultList();
		}

		Page<T> pagedList = new PageImpl<T>(resultList, pageable, count);
		return pagedList;
	}

	protected Query createNativeQuery(String sql, Map<String, Object> parameters, Class<?> entityClass) {
		Query query = this.entityManager.createNativeQuery(sql, entityClass);

		if (parameters != null) {
			setQueryParameters(query, parameters);
		}
		return query;
	}

	protected void setQueryParameters(Query query, Map<String, Object> parameters) {
		for (Map.Entry<String, Object> param : parameters.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}
	}
	
	protected static String getNativeCountQuery(String jpql) {
		String jpqlCountContent = "*";
		String jpqlCount = "";

		String jpqlUpperCase = jpql.toUpperCase(Locale.ITALIAN);
		
		int fromIndex = jpqlUpperCase.indexOf("FROM");
		jpqlCount = jpql.substring(fromIndex);

		int toIndex = jpqlCount.toUpperCase(Locale.ITALIAN).lastIndexOf("ORDER BY");
		if (toIndex != -1) {
			jpqlCount = jpqlCount.substring(0, toIndex);
		}
		
		int distinctIndex = jpqlUpperCase.indexOf("DISTINCT");
		// Se ho la distinct ed e' PRIMA del primo from e non ho virgole nella distinct
		if(distinctIndex != -1 && distinctIndex < fromIndex) {
			
			int commaIndex = jpqlUpperCase.indexOf(",");
			if(commaIndex != -1 && commaIndex < fromIndex){
				throw new UnsupportedOperationException("COUNT Query per una DISTINCT a parametri multipli non supportata da PostgreSQL.");
			}
			
			// Injetto il dato in distinct
			jpqlCountContent = jpql.substring(distinctIndex, fromIndex);
		}

		jpqlCount = String.format("SELECT COUNT(%s) %s", jpqlCountContent, jpqlCount);
		return jpqlCount;
	}
	
	protected static String getNativeCountQueryKeepSyntax(String jpql) {
		return String.format("SELECT COUNT(*) FROM (%s) AS original_query", jpql);
	}
	
//	@Override
//	protected <T> Page<T> getPagedList(String jpql, Map<String, Object> parameters, Pageable pageable) {
//		int distinctIndex = jpql.toUpperCase().indexOf("DISTINCT");
//		if(distinctIndex != -1) {
//			return getDistinctPagedList(jpql, parameters, pageable);
//		}
//		return super.getPagedList(jpql, parameters, pageable);
//	}

	/**
	 * Ottiene la lista paginata nel caso in cui vi siano dei distinct nella select pi&ugrave; esterna
	 * @param jpql il JPQL da eseguire
	 * @param parameters i parametri
	 * @param pageable i dati di paginazione
	 * @return la pagina corrispondente alla query, i parametri e la paginazione
	 */
	@SuppressWarnings("unchecked")
	protected <T> Page<T> getDistinctPagedList(final String jpql, final Map<String, Object> parameters, final Pageable pageable) {
		final String jpqlCount = this.getDistinctCountQuery(jpql);
		final Query qn = this.createQuery(jpqlCount, parameters);
		
		final long count = ((Number)qn.getSingleResult()).longValue();
		List<T> resultList = new ArrayList<T>();
		if (count > 0L) {
			final Query query = this.createQuery(jpql, parameters);
			query.setFirstResult(pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
			resultList = query.getResultList();
		}
		return new PageImpl<T>(resultList, pageable, count);
	}
	
	private String getDistinctCountQuery(final String jpql) {
		String jpqlCountContent = "*";
		final String upperCaseJpql = jpql.toUpperCase(Locale.ITALIAN);
		
		final int distinctIndex = upperCaseJpql.indexOf("DISTINCT");
		final int fromIndex = upperCaseJpql.indexOf("FROM");
		final int commaIndex = upperCaseJpql.indexOf(",");
		
		// Se ho la distinct, la distinct e' PRIMA del primo from e ho virgole nella distinct
		if(distinctIndex != -1 && distinctIndex < fromIndex && commaIndex != -1 && commaIndex < fromIndex) {
			// La versione migliore sarebbe quella di racchiudere la query originaria in una sotto-query. Ma Hibernate non lo supporta
			// Cfr. https://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html/ch16.html#queryhql-subqueries
			throw new UnsupportedOperationException("COUNT Query per una distinct a parametri multipli non supportata da Hibernate.");
		}
		
		String jpqlCount = jpql.substring(fromIndex);
		
		// Se ho la distinct, la distinct e' PRIMA del primo from e non ho virgole nella distinct
		if(distinctIndex != -1 && distinctIndex < fromIndex) {
			// Injetto il dato in distinct
			jpqlCountContent = jpql.substring(distinctIndex, fromIndex);
		}
		
		final int toIndex = jpqlCount.toUpperCase(Locale.ITALIAN).lastIndexOf("ORDER BY");
		if (toIndex != -1) {
			jpqlCount = jpqlCount.substring(0, toIndex);
		}
		return String.format("SELECT COUNT(%s) %s", jpqlCountContent, jpqlCount);
	}
	
	/**
	 * Creazione di una query tipizzata
	 * @param jpql il jpql per cui creare la query
	 * @param parameters i parametri della query
	 * @param clazz la classe di riferimento
	 * @return la query tipizzata
	 */
	protected <T> TypedQuery<T> createQuery(final String jpql, final Map<String, Object> parameters, Class<T> clazz) {
		TypedQuery<T> query = entityManager.createQuery(jpql, clazz);

		if (parameters != null) {
			setQueryParameters(query, parameters);
		}

		return query;
	}
	
	/**
	 * Aggiunge alla query la clausola di ORDER BY
	 * @param jpql lo string builder contenente la query JPQL
	 * @param fallback l'order by di fallback nel caso in cui le condizioni non siano presenti
	 * @param aliases gli alias di sostituzione
	 * @param orderByEnums le condizioni di order by
	 */
	protected <O extends OrderByEnum> void orderQuery(StringBuilder jpql, String fallback, Collection<String> aliases, Collection<O> orderByEnums) {
		StringBuilder sb = new StringBuilder();
		sb.append(" ORDER BY ");
		if(orderByEnums == null) {
			// Fallback
			if(StringUtils.isNotBlank(fallback)) {
				sb.append(fallback);
				jpql.append(sb.toString());
			}
			return;
		}
		
		String[] aliasesAsArray = aliases.toArray(new String[aliases.size()]);
		
		// TODO: gestire alias multipli
		for(Iterator<O> it = orderByEnums.iterator(); it.hasNext();) {
			O stpobe = it.next();
			sb.append(stpobe.getOrderByClause(aliasesAsArray));
			if(it.hasNext()) {
				sb.append(", ");
			}
		}
		jpql.append(sb.toString());
	}
	
	protected <A extends SiacTBase> void setDataModificaInserimento(Iterable<A> entities, Date now) {
		if(entities != null){
			for(SiacTBase siacTBase : entities){
				siacTBase.setDataModificaInserimento(now);
			}
		}
	}
	
	protected <A extends SiacTBase> void setDataCancellazione(Iterable<A> entities, Date now) {
		if(entities != null){
			for(SiacTBase siacTBase : entities){
				siacTBase.setDataCancellazioneIfNotSet(now);
			}
		}
	}
	
	protected void forceUpdateDataInizioValidita(Integer uid, Date dataInizioValidita) {

		Table table = entityClass.getAnnotation(Table.class);
		
		if (table == null)
			throw new IllegalStateException("@Table non definita per l'entity " + entityClass.getName());
		
		String idColumnName = getIdColumn(entityClass);

		if (idColumnName == null)
			throw new IllegalStateException("@Id non definita per l'entity " + entityClass.getName());
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put(idColumnName, uid);
		params.put("validita_inizio", dataInizioValidita);
		
		Query query = createNativeQuery(String.format("UPDATE %s SET validita_inizio=:validita_inizio WHERE %s=:%s", table.name(), idColumnName, idColumnName), params);
		
		query.executeUpdate();
	}

	
}

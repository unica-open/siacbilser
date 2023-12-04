/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.function;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

/**
 * Utility per l'invocazione di una function con dei parametri.
 * 
 * @author Domenico Lisi
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SimpleJPAFunctionInvoker {
	
	//log
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	//entity Managers
	@PersistenceContext
	protected EntityManager entityManager;

	
	/**
	 * Invoke function with result list.
	 *
	 * @param <T> the generic type
	 * @param functionName the function name
	 * @param params the params
	 * @return the list
	 */
	public <T> List<T> invokeFunctionWithResultList(String functionName, Object... params) {
		//creo una query nativa del tipo select * from function
		Query q = createFunctionQuery(functionName, params);
		
		//invoco la query
		@SuppressWarnings("unchecked")
		List<T> results = q.getResultList();
		//ritorno la lista restituita
		return results;
	}
	
	/**
	 * Invoke function with single result.
	 *
	 * @param <T> the generic type
	 * @param functionName the function name
	 * @param params the params
	 * @return the t
	 */
	public <T> T invokeFunctionWithSingleResult(String functionName, Object... params) {
		//creo una query nativa del tipo select * from function
		Query q = createFunctionQuery(functionName, params);
		
		//invoco la query
		@SuppressWarnings("unchecked")
		T result = (T)q.getSingleResult();
		//restituisco il risultato
		return result;
	}

	/**
	 * Creates the function query.
	 *
	 * @param functionName the function name
	 * @param params the params
	 * @return the query
	 */
	public Query createFunctionQuery(String functionName, Object... params) {
		//creo la stringa che mi rappresenta l'sql del tipo select * from function
		String sql = createNativeSQL(functionName, params);
		//creo la query
		Query q = entityManager.createNativeQuery(sql);
		//setto i parametri
		setQueryParameters(q, params);
		return q;
	}
	
	/**
	 * Creates the function query with result class.
	 *
	 * @param functionName the function name
	 * @param resultClass the result class
	 * @param params the params
	 * @return the query
	 */
	public Query createFunctionQueryWithResultClass(String functionName, Class<?> resultClass, Object... params) {
		//creo la stringa che mi rappresenta l'sql del tipo select * from function
		String sql = createNativeSQL(functionName, params);
		//creo la query impostando la classe del risultato
		Query q = entityManager.createNativeQuery(sql, resultClass);
		//setto i parametri
		setQueryParameters(q, params);
		return q;
	}
	
	/**
	 * Creates the function query with result set mapping.
	 *
	 * @param functionName the function name
	 * @param resultSetMapping the result set mapping
	 * @param params the params
	 * @return the query
	 */
	public Query createFunctionQueryWithResultSetMapping(String functionName, String resultSetMapping,  Object... params) {
		//per prima cosa, mi prendo la stringa dell'sql
		String sql = createNativeSQL(functionName, params);
		//creo la query impostando il result set
		Query q = entityManager.createNativeQuery(sql, resultSetMapping);
		//imposto i parametri
		setQueryParameters(q, params);
		return q;
	}
	

	private void setQueryParameters(Query q, Object... params) {
		final String methodName = "setQueryParameters";
		//imposto i parametri
		for (int i = 0; i < params.length; i++) {
			String p = "p"+i;
			Object value = params[i];
			//imposto i parametri
			q.setParameter(p, value);
			log.debug(methodName, p + ": " + value);
		}
	}
	

	private String createNativeSQL(String functionName, Object... params) {
		final String methodName = "createNativeSQL";
		
		StringBuilder sb = new StringBuilder();
		//scheletro base dell'sql
		sb.append("SELECT * FROM "+functionName+"(");
		
		//ho dei parametri, li imposto
		for (int i = 0; i < params.length; i++) {
			sb.append(":p"+i);
			//i parametri sono separti da virgole
			if((i+1) < params.length){
				//non e' l'ultimo parametro, imposto la virgola
				sb.append(", ");
			}
		}
		sb.append(")");
		
		String sql = sb.toString();
		//loggo l'sql che vado ad eseguire
		log.debug(methodName, "Native SQL: " + sql);
		return sql;
	}
}

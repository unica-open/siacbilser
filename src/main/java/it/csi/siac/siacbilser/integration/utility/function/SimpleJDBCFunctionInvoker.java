/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.function;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.utility.function.jdbc.resultsetextractor.MapResultSetExtractor;
import it.csi.siac.siacbilser.integration.utility.function.jdbc.resultsetextractor.ObjectArrayResultSetExtractor;
import it.csi.siac.siacbilser.integration.utility.function.jdbc.resultsetextractor.ResultSetExtractor;
import it.csi.siac.siacbilser.integration.utility.function.jdbc.resultsetextractor.SingleResultResultSetExtractor;
import it.csi.siac.siacbilser.integration.utility.function.jdbc.resultsetextractor.VoidResultSetExtractor;
import it.csi.siac.siacbilser.integration.utility.function.jdbc.statementsetter.CallableStatementSetter;
import it.csi.siac.siacbilser.integration.utility.function.jdbc.statementsetter.DefaultCallableStatementSetter;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

/**
 * Utility per l'invocazione di una function con dei parametri.
 * 
 * @author Domenico Lisi
 *
 */
@Component
public class SimpleJDBCFunctionInvoker {
	
	//il log
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	//ENTITY MANAGERS
	@PersistenceContext
	protected EntityManager entityManager;
	
	
	/**
	 * Invoke function void.
	 *
	 * @param functionName the function name
	 * @param functionParams the function params
	 */
	public void invokeFunctionVoid(String functionName, Object... functionParams){
		//chiamo il servizio centralizzato di invocazione
		invokeFunction(functionName, new VoidResultSetExtractor(), functionParams);
	}
	
	/**
	 * Invoke function single result.
	 *
	 * @param <T> the generic type
	 * @param functionName the function name
	 * @param functionParams the function params
	 * @return the t
	 */
	public <T> T invokeFunctionSingleResult(String functionName, Object... functionParams){
		//chiamo la function: devo avere un solo risultato
		return invokeFunctionSingleResult(functionName, null, functionParams);
	}
	
	/**
	 * Invoke function single result.
	 *
	 * @param <T> the generic type
	 * @param functionName the function name
	 * @param resultClass the result class
	 * @param functionParams the function params
	 * @return the t
	 */
	public <T> T invokeFunctionSingleResult(String functionName, Class<T> resultClass, Object... functionParams){
		//chiamo il metodo centralizzato impostando un risultato singolo
		return invokeFunction(functionName, new SingleResultResultSetExtractor<T>(resultClass), functionParams);
	}
	
	/**
	 * Invoke function to map.
	 *
	 * @param functionName the function name
	 * @param functionParams the function params
	 * @return the list
	 */
	public List<Map<String, Object>> invokeFunctionToMap(String functionName, Object... functionParams){
		//chiamo il metodo centralizzato impostando una mappa come risultato
		return invokeFunction(functionName, new MapResultSetExtractor(), functionParams);
	}
	
	/**
	 * Invoke function to object array.
	 *
	 * @param functionName the function name
	 * @param functionParams the function params
	 * @return the list
	 */
	public List<Object[]> invokeFunctionToObjectArray(String functionName, Object... functionParams){
		//chiamo il metodo centralizzato impostando il risultato come Obìectarray
		return invokeFunction(functionName, new ObjectArrayResultSetExtractor(), functionParams);
	}
	
	/**
	 * Invoke function.
	 *
	 * @param <T> the generic type
	 * @param functionName the function name
	 * @param resultSetExtractor the result set extractor
	 * @param functionParams the function params
	 * @return the t
	 */
	public <T> T invokeFunction(String functionName, ResultSetExtractor<T> resultSetExtractor, Object... functionParams){
		//creo l'sql a partire dal nome della function
		String sql = createSQL(functionName, functionParams.length);
		//chiamo il metodo centralizzato
		return invokeFunction(sql, resultSetExtractor, new DefaultCallableStatementSetter(functionParams));
	}

	/**
	 * Invoke function.
	 *
	 * @param <T> the generic type
	 * @param sql the sql
	 * @param resultSetExtractor the result set extractor
	 * @param callableStatementSetter the callable statement setter
	 * @return the t
	 */
	public <T> T invokeFunction(String sql, ResultSetExtractor<T> resultSetExtractor, CallableStatementSetter callableStatementSetter) {
		final String methodName = "invokeFunction";
		
		final ObjectHolder<T> resultHolder = new ObjectHolder<T>();
		
		//prendo la sessione
		Session session = entityManager.unwrap(Session.class);
		//invoco la function
		session.doWork(new JDBCFunctionWork<T>(sql, resultHolder, resultSetExtractor, callableStatementSetter));
		
		log.debug(methodName, "Returning result.");
		return resultHolder.getObj();
	
	}
	
	private String createSQL(String functionName, int numeroParametri) {
		final String methodName = "createSQL";
		StringBuilder sb = new StringBuilder();
		//sql.append("{ ? = call "+functionName+"(");
		sb.append("{ call "+functionName+"("); //TODO per ora NON supporto il parametro di ritorno della call. Utilizzare i ResultSetExtractor!
		
		//imposto i parametri
		for (int i = 0; i < numeroParametri; i++) {
			sb.append("?");
			if((i+1) < numeroParametri){
				sb.append(", ");
			}
		}
		sb.append(") }");
		String sql = sb.toString();
		//loggo l'sql ottenuto
		log.debug(methodName, "SQL: " + sql);
		return sql;
	}
	
	/**
	 * Work per l'esecuzione della function JDBC
	 * @author Marchino Alessandro
	 *
	 * @param <T> la tipizzazine del risultato
	 */
	private static class JDBCFunctionWork<T> implements Work {
		// il log
		private final LogSrvUtil log = new LogSrvUtil(getClass());
		
		//campi final
		private final String invokeFunctionSql;
		private final ObjectHolder<T> resultHolder;
		private final ResultSetExtractor<T> rse;
		private final CallableStatementSetter css;
		
		//costruttore
		JDBCFunctionWork(String invokeFunctionSql, ObjectHolder<T> resultHolder, ResultSetExtractor<T> rse, CallableStatementSetter css) {
			this.invokeFunctionSql = invokeFunctionSql;
			this.resultHolder = resultHolder;
			this.rse = rse;
			this.css = css;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void execute(Connection conn) throws SQLException {
			final String methodName = "execute";
			//inizializzo i campi a null
			CallableStatement stmt = null;
			ResultSet rs = null;
			try{
				log.debug(methodName, "Preparing call...");
				stmt = conn.prepareCall(invokeFunctionSql);
				
				//Imposto i valori nello statement
				log.debug(methodName, "Setting statement values...");
				css.setValues(stmt);
				
				//Eseguo lo statement.
				log.debug(methodName, "Exececuting statement...");
				boolean thereIsResul = stmt.execute();
				log.debug(methodName, "Statement executed. There is result? "+thereIsResul);
				
				//Prelevo i valori dallo statement (per gli OUT parameters)
				log.debug(methodName, "Getting statement values...");
				css.saveOutValues(stmt);

				rs = stmt.getResultSet();
				Object o = null;
				if(rs!=null /*&& thereIsResul*/){
					//ho un result set, posso estrarne i dati
					log.debug(methodName, "Extracting data from resultSet...");
					o = rse.extractData(rs);
					log.debug(methodName, "Data extracted from resultSet.");
				}
				resultHolder.setObj((T)o);
			
			} finally {
				if(rs != null) {
					//il result set e' diverso da null
					try {
						//chiudo il result set
						rs.close();
						log.debug(methodName, "Result set closed.");
					} catch(SQLException se) {
						//si e' verificata una eccezione: la loggo
						log.warn(methodName, "Errore chiusura result set.", se);
					}
				}
				if (stmt != null) {
					try {
						// chiudo lo statement
						stmt.close();
						log.debug(methodName, "Statement closed.");
					} catch (SQLException se) {
						//si e' verificata una eccezione: la loggo
						log.warn(methodName, "Errore chiusura statement.", se);
					}
				}
				// la Connection verra' chiusa dall'esecutore del Work.
			}
		}
	}


	private static class ObjectHolder<T>{
		private T obj;

		/**
		 * @return the result
		 */
		T getObj() {
			return obj;
		}

		/**
		 * @param result the result to set
		 */
		void setObj(T result) {
			this.obj = result;
		}
		
	}
	
}

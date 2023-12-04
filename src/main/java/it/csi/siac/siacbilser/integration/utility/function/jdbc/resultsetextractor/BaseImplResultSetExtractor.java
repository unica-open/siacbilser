/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.function.jdbc.resultsetextractor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;

/**
 * Impl di base per i {@link ResultSetExtractor}.
 * 
 * @author Domenico
 *
 * @param <T> Tipo dell'Object gestito.
 */
public abstract class BaseImplResultSetExtractor<T> implements ResultSetExtractor<T> {

	protected LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	public BaseImplResultSetExtractor() {
		super();
	}
	
	/**
	 * Ottiene la colonna i-esima di un result set con il tipo Java predefinito.
	 * 
	 * @param rs il {@link ResultSet}
	 * @param i indice della colonna
	 * @return valore nel tipo Java predefinito
	 * @throws SQLException
	 */
	protected Object getObject(ResultSet rs, int i) throws SQLException {
		Object value = rs.getObject(i);
		
		if(value instanceof java.sql.Date){
			value = rs.getTimestamp(i); //altrimenti avrei solo la data (senza ora, secondi e millisecondi)
		}	
		if (value instanceof java.sql.Timestamp){
			 //Se è un java.sql.Timestamp sono già apposto perchè estende java.util.Date. Ma meglio comunque reincapsularlo!
			value = new java.util.Date(((java.sql.Timestamp) value).getTime()); 
		}
		return value;
	}
	
	/**
	 * Ottiene la colonna i-esima di un result set con il Tipo Java specificato nel parametro typeClass.
	 * 
	 * @param rs il {@link ResultSet}
	 * @param i indice della colonna
	 * @param typeClass 
	 * @return valore nel tipo Java specificato.
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	protected <O> O getObject(ResultSet rs, int i, Class<O> typeClass) throws SQLException {
		Object value;
		if(typeClass == null){
			value = getObject(rs, i);
		} else if(typeClass.isAssignableFrom(String.class)){
			value = rs.getString(i);
		} else if(typeClass.isAssignableFrom(Integer.class)){
			value = rs.getInt(i);
			if(rs.wasNull()){
				value = null;
			}
		} else if(typeClass.isAssignableFrom(Long.class)){
			value = rs.getLong(i);
			if(rs.wasNull()){
				value = null;
			}
		} else if(typeClass.isAssignableFrom(BigDecimal.class)){
			value = rs.getBigDecimal(i);
		} else if(typeClass.isAssignableFrom(java.util.Date.class)){
			value = rs.getTimestamp(i); //altrimenti avrei solo la data (senza ora, secondi e millisecondi)  
			if(value!=null){
				value = new java.util.Date(((java.sql.Timestamp)value).getTime());//Se  presente un valore viene reincapsularlo in un java.util.Date
			}
		} else {
			//TODO throw UnsupportedTypeException? oppure lascio il mapping di default con il rischio del ClassCastException che in effetti ci sta...
			value = getObject(rs, i);
		}
		return (O)value;
	}

}
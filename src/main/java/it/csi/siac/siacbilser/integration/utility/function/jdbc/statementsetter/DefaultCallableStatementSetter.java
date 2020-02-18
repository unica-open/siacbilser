/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.function.jdbc.statementsetter;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.integration.utility.function.jdbc.SQLParam;
import it.csi.siac.siaccommon.util.log.LogUtil;

/**
 * CallableStatementSetter di default.
 * 
 * @author Domenico
 *
 */
public class DefaultCallableStatementSetter implements CallableStatementSetter {
	
	private LogUtil log = new LogUtil(this.getClass());

	private SQLParam[] functionParams;
	
	
	public DefaultCallableStatementSetter(SQLParam[] functionParams) {
		if(functionParams==null){
			throw new IllegalArgumentException("La lista dei parametri non puo' essere nulla.");
		}
		this.functionParams = functionParams;
	}
	
	public DefaultCallableStatementSetter(Object... functionParams) {
		this(toSQLParam(functionParams));
	}
	
	public static SQLParam[] toSQLParam(Object... params){
		List<SQLParam> l = new ArrayList<SQLParam>();
		for (int i = 0; i < params.length; i++) {
			Object value = params[i];
			if(value==null){
				l.add(new SQLParam(null, Types.NULL));
			} else if(value instanceof SQLParam){ //Posso passare anche direttamente un SQLParam! :) 
				                                  //Da utilizzare se si voule impostare ad esempio un Out Parameter!
				l.add((SQLParam)value); 	
			} else if(value instanceof String){
				l.add(new SQLParam(value, Types.VARCHAR)); //CHAR, LONGVARCHAR
			} else if(value instanceof Integer){
				l.add(new SQLParam(value, Types.INTEGER)); 
			} else if(value instanceof Long){
				l.add(new SQLParam(value, Types.BIGINT)); 
			} else if(value instanceof BigDecimal){
				l.add(new SQLParam(value, Types.NUMERIC)); 
			} else if(value instanceof Date){
				l.add(new SQLParam(value, Types.TIMESTAMP)); 
			} else {
				throw new IllegalArgumentException("Il parametro non è tra i tipi supportati.");
			}
		}
		SQLParam[] result = l.toArray(new SQLParam[l.size()]);
		return result;
	}


	@Override
	public void setValues(CallableStatement stmt) throws SQLException {
		final String methodName = "setValues";

		//register Out parameters
		for (int i = 0; i < functionParams.length; i++) {
			SQLParam sQLParam = functionParams[i];
			int j = i+1;
			
			if(sQLParam.isOut()){
				stmt.registerOutParameter(j, sQLParam.getSqlType());
			}
		}
		
		//set parameters value
		for (int i = 0; i < functionParams.length; i++) {
			SQLParam sQLParam = functionParams[i];
			Object value = sQLParam.getValue();
			int j = i+1;
			
			if(sQLParam.isOutOnly()){ //salto gli OUT parameter
				continue;
			}
			
			log.debug(methodName, i+". "+value);
			
			if(value == null){
				stmt.setNull(j, sQLParam.getSqlType());
			} else if(value instanceof String){
				stmt.setString(j, (String)value);
			} else if(value instanceof Integer){
				stmt.setInt(j, ((Integer)value).intValue());
			} else if(value instanceof Long){
				stmt.setLong(j, ((Long)value).longValue());
			} else if(value instanceof BigDecimal){
				stmt.setBigDecimal(j, (BigDecimal)value);
			} else if(value instanceof Date){
				stmt.setTimestamp(j, new Timestamp(((Date) value).getTime()));
			} else {
				stmt.setObject(j, value, sQLParam.getSqlType());
			}
		}
				
	}





	@Override
	public void saveOutValues(CallableStatement stmt) throws SQLException {
		//Salvo il valore degli output parameters
		for (int i = 0; i < functionParams.length; i++) {
			SQLParam sQLParam = functionParams[i];
			int j = i+1;
			
			if(!sQLParam.isOut()){
				continue;
			}
			
			sQLParam.setValue(stmt.getObject(j));
			
		}
		
	}
	
	
//		@Override
//		public int getNumberOfParams() {
//			return functionParams.length;
//		}
		
}
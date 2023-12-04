/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.function.jdbc.resultsetextractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Estrae il resultSet in una generica mappa con chiave = nome della colonna e valore il valore della colonna.
 * 
 * @author Domenico
 *
 */
public class MapResultSetExtractor extends BaseImplResultSetExtractor<List<Map<String, Object>>> {
		
	@Override
	public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException {
		final String methodName = "extractData";
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		while(rs.next()){
			Map<String,Object> m = new HashMap<String, Object>();
			int columnCount = rs.getMetaData().getColumnCount();
			for(int i = 1; i<=columnCount; i++){
				//1-based!
				String columnName = rs.getMetaData().getColumnName(i);
//				int columnType = rs.getMetaData().getColumnType(i);
				
				Object value = getObject(rs, i);
				m.put(columnName, value);
			}
			result.add(m);
		}
		if(log.isDebugEnabled()){
			log.debug(methodName, "Returning map: "+result);
		}
		return result;
	}


}

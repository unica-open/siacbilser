/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.function.jdbc.resultsetextractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Estrae il resultSet in un array di Object, con il valore della colonna.
 * @author Marchino Alessandro
 */
public class ObjectArrayResultSetExtractor extends BaseImplResultSetExtractor<List<Object[]>> {

	@Override
	public List<Object[]> extractData(ResultSet rs) throws SQLException {
		List<Object[]> result = new ArrayList<Object[]>();
		while(rs.next()){
			int columnCount = rs.getMetaData().getColumnCount();
			Object[] arr = new Object[columnCount];
			
			for(int i = 1; i <= columnCount; i++){
				Object value = getObject(rs, i);
				arr[i - 1] = value;
			}
			result.add(arr);
		}
		return result;
	}

}
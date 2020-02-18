/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.function.jdbc.resultsetextractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Estrae un singolo risultato a partire dal resulSet.
 * 
 * @author Domenico
 *
 * @param <T>  Il tipo associato al risultato.
 */
public class SingleResultResultSetExtractor<T> extends BaseImplResultSetExtractor<T>{
	
	private Class<T> typeClass;
	
	public SingleResultResultSetExtractor(Class<T> typeClass) {
		this.typeClass = typeClass;
	}
	
	public SingleResultResultSetExtractor() {
		this(null);
	}

	@Override
	public T extractData(ResultSet rs) throws SQLException {
		if(!rs.next()){
			return null;
		}
		
		T value = getObject(rs, 1 , this.typeClass);
		
		return value;
	}

	
	
}

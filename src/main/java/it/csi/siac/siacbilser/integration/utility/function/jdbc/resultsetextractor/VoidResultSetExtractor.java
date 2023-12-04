/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.function.jdbc.resultsetextractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utilizzato per le function Void.
 * 
 * @author Domenico
 *
 */
public class VoidResultSetExtractor implements ResultSetExtractor<Void>{

	@Override
	public Void extractData(ResultSet rs) throws SQLException {
		return null;
	}
	
}
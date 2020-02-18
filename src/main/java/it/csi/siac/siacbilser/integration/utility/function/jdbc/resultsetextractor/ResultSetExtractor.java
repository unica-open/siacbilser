/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.function.jdbc.resultsetextractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Estre un Object a partire dal {@link ResultSet}.
 * @author Domenico
 *
 * @param <T> Tipo dell'Object gestito.
 */
public interface ResultSetExtractor<T> {
	
	/**
	 * Estrae i dati dal {@link ResultSet}.
	 * 
	 * @param rs
	 * @return l'Objet popolato con i dati del {@link ResultSet}.
	 * @throws SQLException
	 */
	public T extractData(ResultSet rs) throws SQLException;
}
/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.function.jdbc.statementsetter;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * Interfaccia per la gestione del CallableStatement utilizzato per invocare la function.
 * 
 * @author Domenico
 *
 */
public interface CallableStatementSetter {
	/**
	 * Imposta i valori nello Statement
	 * 
	 * @param stmt
	 * @throws SQLException
	 */
	public abstract void setValues(CallableStatement stmt) throws SQLException;
	
	/**
	 * Salva gli OUT o INOUT params dallo statement se previsti.
	 * 
	 * @param stmt
	 * @throws SQLException
	 */
	public void saveOutValues(CallableStatement stmt) throws SQLException;
	
	
}
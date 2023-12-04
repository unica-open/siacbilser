/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility.function.jdbc;

import java.sql.Types;

/**
 * Rappresenta un parametro SQL di una Function.
 * 
 * @author Domenico
 *
 */
public class SQLParam {
	/**
	 * @see Types
	 */
	private final int sqlType;
	private Object value;
	private final Mode mode;
	
	public SQLParam(Object value, int sqlType) {
		this(value, sqlType, Mode.IN);
	}
	
	public SQLParam(Object value, int sqlType, Mode mode) {
		this.value = value;
		this.sqlType = sqlType;
		this.mode = mode;
	}
	
	/**
	 * @return the sqlType
	 */
	public int getSqlType() {
		return sqlType;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the mode
	 */
	public Mode getMode() {
		return mode;
	}

	public boolean isOut(){
		return Mode.OUT.equals(this.mode) || Mode.INOUT.equals(this.mode);
	}
	
	public boolean isOutOnly(){
		return Mode.OUT.equals(this.mode);
	}
	
	/**
	 * Modalita del parametro
	 * IN -> Input
	 * OUT -> Output
	 * INOUT -> Input e Output.
	 * 
	 * @author Domenico
	 *
	 */
	public enum Mode {
		IN,
		OUT,
		INOUT
	}
}
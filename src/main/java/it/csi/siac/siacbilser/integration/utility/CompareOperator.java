/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility;

/**
 * Operatore di comparazione.
 * 
 * @author Marchino Alessandro
 *
 */
public enum CompareOperator {

	EQUALS(" = "),
	NOT_EQUALS(" <> "),
	LESS(" < "),
	LESS_OR_EQUAL(" <= "),
	GREATER_OR_EQUAL(" >= "),
	GREATER(" > "),
	;
	private final String jpql;
	
	private CompareOperator(String jpql) {
		this.jpql = jpql;
	}

	/**
	 * @return the jpql
	 */
	public String getJpql() {
		return jpql;
	}
}

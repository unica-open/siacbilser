/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.utility;

/**
 * Costanti varie e centralizzate
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2019
 *
 */
public enum Constants {

	CATEGORIA_CAPITOLO_STANDARD("STD"),
	CATEGORIA_CAPITOLO_FPV("FPV"),
	CATEGORIA_CAPITOLO_BUDGET("BDG"),
	;
	
	private final String value;

	/**
	 * Costruttore di wrap per la costante
	 * @param value la costante
	 */
	private Constants(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}
}

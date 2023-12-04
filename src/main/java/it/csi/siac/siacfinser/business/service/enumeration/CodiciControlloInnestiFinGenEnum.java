/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.enumeration;



/**
 * Descrive il mapping dell codifiche.
 * 
 * @author Valentina
 */
public enum CodiciControlloInnestiFinGenEnum {

	TITOLO_1("1"), 
	TITOLO_2("2"),
	TITOLO_3("3"), 
	TITOLO_4("4"), 
	TITOLO_5("5"), 
	TITOLO_7("7"),
	MACROAGGREGATO_1030000("1030000"),
	MACROAGGREGATO_1040000("1040000"), 
	MACROAGGREGATO_2030000("2030000"),
	MACROAGGREGATO_2040000("2040000"), 
	MACROAGGREGATO_2050000("2050000"),
	
	MACROAGGREGATO_4010000("4010000"),
	MACROAGGREGATO_4020000("4020000"),
	MACROAGGREGATO_4030000("4030000"),
	MACROAGGREGATO_4040000("4040000"),
	
	MACROAGGREGATO_7020000("7020000"),
	MACROAGGREGATO_7010000("7010000"), 
	
	MACROAGGREGATO_1100000("1100000"),
	
	;

	private String codice;

	private CodiciControlloInnestiFinGenEnum(String codice) {

		setCodice(codice);

	}

	public static CodiciControlloInnestiFinGenEnum byCodice(String codice) {
		for (CodiciControlloInnestiFinGenEnum ce : CodiciControlloInnestiFinGenEnum
				.values()) {
			if (ce.getCodice().equals(codice)) {
				return ce;
			}
		}
		throw new IllegalArgumentException(
				"Impossibile trovare un mapping per il codice: " + codice
						+ " in CodiciControlloInnestiFinGenEnum");
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @param collegamentoTipoCode
	 *            the collegamentoTipoCode to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

}

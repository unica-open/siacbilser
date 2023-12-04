/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacTNumeroAttoEnum.
 * @deprecated 
 */
@Deprecated
public enum SiacTNumeroAttoEnum {
	
	/** The atto di legge. */
	ATTO_DI_LEGGE("A", "Atto di legge"),
	
	/** The provvedimento. */
	PROVVEDIMENTO("P", "Provvedimento");

	/** The codice. */
	private final String codice;
	
	/** The descrizione. */
	private final String descrizione;
	
	/**
	 * Instantiates a new siac t numero atto enum.
	 *
	 * @param codice the codice
	 * @param descrizione the descrizione
	 */
	private SiacTNumeroAttoEnum (String codice, String descrizione) {
		this.codice = codice;
		this.descrizione = descrizione;
	}
	
	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * Gets the descrizione.
	 *
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}
}

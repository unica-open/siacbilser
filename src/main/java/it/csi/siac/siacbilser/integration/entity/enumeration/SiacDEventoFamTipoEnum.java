/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;


/**
 * Descrive il mapping dell codifiche.
 * 
 * @author Valentina
 */
public enum SiacDEventoFamTipoEnum {
	
	Entrata("E", "Entrata"),
	Spesa("S", "Spesa"),

	;
	
	private final String codice;
	private final String descrizione;
	
	private SiacDEventoFamTipoEnum(String codice, String descrizione){
		this.codice = codice;
		this.descrizione = descrizione;
	}
	
	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}
	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

}

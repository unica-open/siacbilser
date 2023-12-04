/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * The Enum SiacDDocFamTipoEnum.
 */
@EnumEntity(entityName="SiacDMovgestTsDetTipo", idPropertyName="movgestTsDetTipoId", codePropertyName="movgestTsDetTipoCode")
public enum SiacDMovgestTsDetTipoEnum {
	
	Iniziale("I"),
	Attuale("A");

	private final String codice;
	
	/**
	 * Instantiates a new siac d movgest ts det tipo enum
	 *
	 * @param codice the codice
	 */
	private SiacDMovgestTsDetTipoEnum(String codice){
		this.codice = codice;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc fam tipo enum
	 */
	public static SiacDMovgestTsDetTipoEnum byCodice(String codice){
		for(SiacDMovgestTsDetTipoEnum e : SiacDMovgestTsDetTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Tipo dettaglio "+ codice + " non ha un mapping corrispondente in SiacDMovgestTsDetTipoEnum");
	}
	
	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}
	
	public static List<String> getCodices() {
		List<String> result = new ArrayList<String>();
		for(SiacDMovgestTsDetTipoEnum sddfte : values()) {
			result.add(sddfte.getCodice());
		}
		return result;
	}
	
}
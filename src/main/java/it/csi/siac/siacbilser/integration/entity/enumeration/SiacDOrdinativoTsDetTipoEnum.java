/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

/**
 * select replace(initcap(attoamm_stato_desc),' ','') || '("' || attoamm_stato_code || '"),' from siac_d_atto_amm_stato where ente_proprietario_id = 1
 */
@EnumEntity(entityName="SiacDOrdinativoTsDetTipo", idPropertyName="ordTsDetTipoCode", codePropertyName="ordTsDetTipoDesc")
public enum SiacDOrdinativoTsDetTipoEnum {
	
	ATTUALE("A"),
	INIZIALE("I"),
	;

	/** The codice. */
	private final String codice;
	

	
	SiacDOrdinativoTsDetTipoEnum(String codice){
		this.codice = codice;
	}
	
	/**
	 * @param codice
	 * @return
	 */
	public static SiacDOrdinativoTsDetTipoEnum byCodiceEvenNull(String codice){
		if(codice==null){
			return null;
		}
		return byCodice(codice);
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d atto amm tipo enum
	 */
	public static SiacDOrdinativoTsDetTipoEnum byCodice(String codice){
		for(SiacDOrdinativoTsDetTipoEnum e : SiacDOrdinativoTsDetTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDOrdinativoTsDetTipoEnum");
	}
	

	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	
}
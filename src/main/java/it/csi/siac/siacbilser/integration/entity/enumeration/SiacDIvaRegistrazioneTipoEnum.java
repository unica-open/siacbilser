/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;


// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDIvaRegistrazioneTipoEnum.
 */
@EnumEntity(entityName="SiacDIvaRegistrazioneTipo", idPropertyName="regTipoId", codePropertyName="regTipoCode")
public enum SiacDIvaRegistrazioneTipoEnum {
	
	Normale   ("01"),
	Intrastat ("02"),
	;
	
	
	/** The codice. */
	private final String codice;
	
	
	/**
	 * Instantiates a new siac d iva registro tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoRegistroIva the tipo registro iva
	 */
	private SiacDIvaRegistrazioneTipoEnum(String codice){
		this.codice = codice;
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
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d iva registro tipo enum
	 */
	public static SiacDIvaRegistrazioneTipoEnum byCodice(String codice){
		for(SiacDIvaRegistrazioneTipoEnum e : SiacDIvaRegistrazioneTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDIvaRegistrazioneTipoEnum");
	}
	

	

}

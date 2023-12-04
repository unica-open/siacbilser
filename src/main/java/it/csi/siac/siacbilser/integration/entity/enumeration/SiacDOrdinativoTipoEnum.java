/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoOrdinativo;

/**
 * Enum per l'entity SiacDIvaStampaTipo
 *
 */
@EnumEntity(entityName="SiacDOrdinativoTipo", idPropertyName="ordTipoId", codePropertyName="ordTipoCode")
public enum SiacDOrdinativoTipoEnum {
	
	
	Pagamento    ("P", TipoOrdinativo.PAGAMENTO),
	
	Incasso    ("I", TipoOrdinativo.INCASSO),
	
	;
	
	
	
	/** The codice. */
	private final String codice;
	
	/** The tipo ordinativo. */
	private final TipoOrdinativo tipoOrdinativo;
	
	
	/**
	 * Instantiates a new siac d iva registro tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoRegistroIva the tipo registro iva
	 */
	private SiacDOrdinativoTipoEnum(String codice, TipoOrdinativo tipoOrdinativo){
		this.codice = codice;
		this.tipoOrdinativo = tipoOrdinativo;
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
	 * Gets the tipo registro iva.
	 *
	 * @return the tipo registro iva
	 */
	public TipoOrdinativo getTipoOrdinativo() {
		return tipoOrdinativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d iva registro tipo enum
	 */
	public static SiacDOrdinativoTipoEnum byCodice(String codice){
		for(SiacDOrdinativoTipoEnum e : SiacDOrdinativoTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDOrdinativoTipoEnum");
	}
	
	/**
	 * By tipo registro iva.
	 *
	 * @param tipoStampaIva the tipo
	 * @return the siac d iva registro tipo enum
	 */
	public static SiacDOrdinativoTipoEnum byTipoOrdinativo(TipoOrdinativo tipoOrdinativo){
		for(SiacDOrdinativoTipoEnum e : SiacDOrdinativoTipoEnum.values()){
			if(e.getTipoOrdinativo().equals(tipoOrdinativo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo ordinativo "+ tipoOrdinativo + " non ha un mapping corrispondente in SiacDOrdinativoTipoEnum");
	}
	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.ModelloCausale;



/**
 * select replace(initcap(elem_det_tipo_desc),' ','') || '("' || elem_det_tipo_code || '"),' from Siac_D_Atto_Allegato_Stato.
 */
@EnumEntity(entityName="SiacDModello", idPropertyName="modelId", codePropertyName="modelCode")
public enum SiacDModelloEnum {
	CAUSALE_770("01", ModelloCausale.CAUSALE_770),
	PREDOCUMENTO_ENTRATA("PREDOC_E", ModelloCausale.PREDOCUMENTO_ENTRATA),
	PREDOCUMENTO_SPESA("PREDOC_S", ModelloCausale.PREDOCUMENTO_SPESA),
	ONERI("ONERI", ModelloCausale.ONERI),
	;

	/** The codice. */
	private final String codice;
	private final ModelloCausale modello;
	
	
	private SiacDModelloEnum(String codice, ModelloCausale modello){
		this.codice = codice;
		this.modello = modello;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d atto amm tipo enum
	 */
	public static SiacDModelloEnum byCodice(String codice){
		for(SiacDModelloEnum e : SiacDModelloEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDModelloEnum");
	}
	
	/**
	 * By codice even null.
	 *
	 * @param codice the codice
	 * @return the siac d atto amm tipo enum
	 */
	public static SiacDModelloEnum byCodiceEvenNull(String codice){
		if(codice == null) {
			return null;
		}
		return byCodice(codice);
	}
	
	/**
	 * By modello.
	 *
	 * @param modello the modello
	 * @return the siac d atto amm tipo enum
	 */
	public static SiacDModelloEnum byModello(ModelloCausale modello){
		for(SiacDModelloEnum e : SiacDModelloEnum.values()){
			if(e.getModello().equals(modello)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il modello "+ modello + " non ha un mapping corrispondente in SiacDModelloEnum");
	}
	
	/**
	 * By modello even null.
	 *
	 * @param modello the modello
	 * @return the siac d atto amm tipo enum
	 */
	public static SiacDModelloEnum byModelloEvenNull(ModelloCausale modello){
		if(modello == null) {
			return null;
		}
		return byModello(modello);
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
	 * @return the modello
	 */
	public ModelloCausale getModello() {
		return modello;
	}

}
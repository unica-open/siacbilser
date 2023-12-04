/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;

/**
 * The Enum SiacDBilElemDetCompMacroTipoEnum.
 */
@EnumEntity(entityName="SiacDBilElemDetCompMacroTipo", idPropertyName="elemDetCompMacroTipoId", codePropertyName="elemDetCompMacroTipoCode")
public enum SiacDBilElemDetCompMacroTipoEnum {

	FRESCO("01", MacrotipoComponenteImportiCapitolo.FRESCO),
	FPV("02", MacrotipoComponenteImportiCapitolo.FPV),
	AVANZO("03", MacrotipoComponenteImportiCapitolo.AVANZO),
	DA_ATTRIBUIRE("04", MacrotipoComponenteImportiCapitolo.DA_ATTRIBUIRE),
	;
	
	/** The codice. */
	private final String codice;
	
	/** The macrotipo componente importi capitolo. */
	private final MacrotipoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo;
	
	
	
	/**
	 * Instantiates a new siac d bil elem det comp macro tipo enum.
	 *
	 * @param codice the codice
	 * @param macrotipoComponenteImportiCapitolo the macrotipo componente importi capitolo
	 */
	private SiacDBilElemDetCompMacroTipoEnum(String codice, MacrotipoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo){
		this.codice = codice;
		this.macrotipoComponenteImportiCapitolo = macrotipoComponenteImportiCapitolo;
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
	 * @return the macrotipoComponenteImportiCapitolo
	 */
	public MacrotipoComponenteImportiCapitolo getMacrotipoComponenteImportiCapitolo() {
		return this.macrotipoComponenteImportiCapitolo;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDBilElemDetCompMacroTipoEnum byCodice(String codice){
		for(SiacDBilElemDetCompMacroTipoEnum e : SiacDBilElemDetCompMacroTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemDetCompMacroTipoEnum");
	}
	
	

	/**
	 * By macrotipo componente importi capitolo.
	 *
	 * @param macrotipoComponenteImportiCapitolo the macrotipoComponenteImportiCapitolo
	 * @return the siac d bil elem det compo macro tipo enum
	 */
	public static SiacDBilElemDetCompMacroTipoEnum byMacrotipoComponenteImportiCapitolo(MacrotipoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo){
		for(SiacDBilElemDetCompMacroTipoEnum e : SiacDBilElemDetCompMacroTipoEnum.values()){
			if(e.getMacrotipoComponenteImportiCapitolo().equals(macrotipoComponenteImportiCapitolo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il macrotipo "+ macrotipoComponenteImportiCapitolo + " non ha un mapping corrispondente in SiacDBilElemDetCompMacroTIpoEnum");
	}
	
	public static SiacDBilElemDetCompMacroTipoEnum byMacrotipoComponenteImportiCapitoloEvenNull(MacrotipoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo){
		if(macrotipoComponenteImportiCapitolo == null){
			return null;
		}
		return byMacrotipoComponenteImportiCapitolo(macrotipoComponenteImportiCapitolo);
	}
	
}

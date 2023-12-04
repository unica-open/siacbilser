/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.SottotipoComponenteImportiCapitolo;

/**
 * The Enum SiacDBilElemDetCompSottoTipoEnum.
 */
@EnumEntity(entityName="SiacDBilElemDetCompSottoTipo", idPropertyName="elemDetCompSottoTipoId", codePropertyName="elemDetCompSottoTipoCode")
public enum SiacDBilElemDetCompSottoTipoEnum {

	PROGRAMMATO_NON_IMPEGNATO("01", SottotipoComponenteImportiCapitolo.PROGRAMMATO_NON_IMPEGNATO),
	CUMULATO("02", SottotipoComponenteImportiCapitolo.CUMULATO),
	APPLICATO("03", SottotipoComponenteImportiCapitolo.APPLICATO),
	;

	/** The codice. */
	private final String codice;
	/** The macrotipo componente importi capitolo. */
	private final SottotipoComponenteImportiCapitolo sottotipoComponenteImportiCapitolo;
	
	/**
	 * Instantiates a new siac d bil elem det comp sotto tipo enum.
	 *
	 * @param codice the codice
	 * @param sottotipoComponenteImportiCapitolo the sottotipo componente importi capitolo
	 */
	private SiacDBilElemDetCompSottoTipoEnum(String codice, SottotipoComponenteImportiCapitolo sottotipoComponenteImportiCapitolo){
		this.codice = codice;
		this.sottotipoComponenteImportiCapitolo = sottotipoComponenteImportiCapitolo;
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
	 * @return the sottotipoComponenteImportiCapitolo
	 */
	public SottotipoComponenteImportiCapitolo getSottotipoComponenteImportiCapitolo() {
		return this.sottotipoComponenteImportiCapitolo;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil elem det comp sotto tipo enum
	 */
	public static SiacDBilElemDetCompSottoTipoEnum byCodice(String codice){
		for(SiacDBilElemDetCompSottoTipoEnum e : SiacDBilElemDetCompSottoTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemDetCompSottoTipoEnum");
	}
	
	

	/**
	 * By sottotipo componente importi capitolo.
	 *
	 * @param sottotipoComponenteImportiCapitolo the sottotipoComponenteImportiCapitolo
	 * @return the siac d bil elem det comp sotto tipo enum
	 */
	public static SiacDBilElemDetCompSottoTipoEnum bySottotipoComponenteImportiCapitolo(SottotipoComponenteImportiCapitolo sottotipoComponenteImportiCapitolo){
		for(SiacDBilElemDetCompSottoTipoEnum e : SiacDBilElemDetCompSottoTipoEnum.values()){
			if(e.getSottotipoComponenteImportiCapitolo().equals(sottotipoComponenteImportiCapitolo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il sottotipo "+ sottotipoComponenteImportiCapitolo + " non ha un mapping corrispondente in SiacDBilElemDetCompSottoTIpoEnum");
	}
	/**
	 * By sottotipo componente importi capitolo, even null.
	 *
	 * @param sottotipoComponenteImportiCapitolo the sottotipoComponenteImportiCapitolo
	 * @return the siac d bil elem det comp sotto tipo enum
	 */
	public static SiacDBilElemDetCompSottoTipoEnum bySottotipoComponenteImportiCapitoloEvenNull(SottotipoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo){
		if(macrotipoComponenteImportiCapitolo == null){
			return null;
		}
		return bySottotipoComponenteImportiCapitolo(macrotipoComponenteImportiCapitolo);
	}
	
}

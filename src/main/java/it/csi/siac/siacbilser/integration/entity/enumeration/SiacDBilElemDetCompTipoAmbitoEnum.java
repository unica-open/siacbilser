/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.AmbitoComponenteImportiCapitolo;

/**
 * The Enum SiacDBilElemDetCompTipoAmbitoEnum.
 */
@EnumEntity(entityName="SiacDBilElemDetCompTipoAmbito", idPropertyName="elemDetCompTipoAmbitoId", codePropertyName="elemDetCompTipoAmbitoCode")
public enum SiacDBilElemDetCompTipoAmbitoEnum {

	AUTONOMO("01", AmbitoComponenteImportiCapitolo.AUTONOMO),
	VINCOLATO("02", AmbitoComponenteImportiCapitolo.VINCOLATO),
	DA_DEFINIRE("03", AmbitoComponenteImportiCapitolo.DA_DEFINIRE),
	;

	/** The codice. */
	private final String codice;
	/** The tipo ambito componente importi capitolo. */
	private final AmbitoComponenteImportiCapitolo tipoAmbitoComponenteImportiCapitolo;
	
	/**
	 * Instantiates a new siac d bil elem det comp tipo ambito enum.
	 *
	 * @param codice the codice
	 * @param tipoAmbitoComponenteImportiCapitolo the tipo ambito componente importi capitolo
	 */
	private SiacDBilElemDetCompTipoAmbitoEnum(String codice, AmbitoComponenteImportiCapitolo tipoAmbitoComponenteImportiCapitolo){
		this.codice = codice;
		this.tipoAmbitoComponenteImportiCapitolo = tipoAmbitoComponenteImportiCapitolo;
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
	 * @return the tipoAmbitoComponenteImportiCapitolo
	 */
	public AmbitoComponenteImportiCapitolo getAmbitoComponenteImportiCapitolo() {
		return this.tipoAmbitoComponenteImportiCapitolo;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil elem det comp tipo ambito enum
	 */
	public static SiacDBilElemDetCompTipoAmbitoEnum byCodice(String codice){
		for(SiacDBilElemDetCompTipoAmbitoEnum e : SiacDBilElemDetCompTipoAmbitoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemDetCompTIpoAmbitoEnum");
	}
	
	

	/**
	 * By tipo ambito componente importi capitolo.
	 *
	 * @param tipoAmbitoComponenteImportiCapitolo the tipoAmbitoComponenteImportiCapitolo
	 * @return the siac d bil elem det comp tipo ambito enum
	 */
	public static SiacDBilElemDetCompTipoAmbitoEnum byAmbitoComponenteImportiCapitolo(AmbitoComponenteImportiCapitolo tipoAmbitoComponenteImportiCapitolo){
		for(SiacDBilElemDetCompTipoAmbitoEnum e : SiacDBilElemDetCompTipoAmbitoEnum.values()){
			if(e.getAmbitoComponenteImportiCapitolo().equals(tipoAmbitoComponenteImportiCapitolo)){
				return e;
			}
		}
		throw new IllegalArgumentException("L'ambito "+ tipoAmbitoComponenteImportiCapitolo + " non ha un mapping corrispondente in SiacDBilElemDetCompTIpoAmbitoEnum");
	}
	/**
	 * By tipo ambito componente importi capitolo, even null.
	 *
	 * @param tipoAmbitoComponenteImportiCapitolo the tipoAmbitoComponenteImportiCapitolo
	 * @return the siac d bil elem det comp tipo ambito enum
	 */
	public static SiacDBilElemDetCompTipoAmbitoEnum byAmbitoComponenteImportiCapitoloEvenNull(AmbitoComponenteImportiCapitolo macrotipoComponenteImportiCapitolo){
		if(macrotipoComponenteImportiCapitolo == null){
			return null;
		}
		return byAmbitoComponenteImportiCapitolo(macrotipoComponenteImportiCapitolo);
	}
	
}

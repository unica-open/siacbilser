/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.ImpegnabileComponenteImportiCapitolo;

/**
 * The Enum SiacDBilElemDetCompTipoImpEnum.
 */
@EnumEntity(entityName="SiacDBilElemDetCompTipoImp", idPropertyName="elemDetCompTipoImpId", codePropertyName="elemDetCompTipoImpCode")
public enum SiacDBilElemDetCompTipoImpEnum {

	SI("01", ImpegnabileComponenteImportiCapitolo.SI),
	NO("02", ImpegnabileComponenteImportiCapitolo.NO),
	AUTO("03", ImpegnabileComponenteImportiCapitolo.AUTO),
	;
	
	/** The codice. */
	private final String codice;
	/** The impegnabile componente importi capitolo. */
	private final ImpegnabileComponenteImportiCapitolo impegnabileComponenteImportiCapitolo;
	
	/**
	 * Instantiates a new siac d bil elem det comp fonte finanziaria enum.
	 *
	 * @param codice the codice
	 * @param impegnabile ComponenteImportiCapitolo theimp'impegnabile  componente importi capitolo
	 */
	private SiacDBilElemDetCompTipoImpEnum(String codice, ImpegnabileComponenteImportiCapitolo impegnabileComponenteImportiCapitolo){
		this.codice = codice;
		this.impegnabileComponenteImportiCapitolo = impegnabileComponenteImportiCapitolo;
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
	 * @return the impegnabileComponenteImportiCapitolo
	 */
	public ImpegnabileComponenteImportiCapitolo getImpegnabileComponenteImportiCapitolo() {
		return impegnabileComponenteImportiCapitolo;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil elem det comp tipo def enum
	 */
	public static SiacDBilElemDetCompTipoImpEnum byCodice(String codice){
		for(SiacDBilElemDetCompTipoImpEnum e : SiacDBilElemDetCompTipoImpEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemDetCompTipoImpEnum");
	}
	
	

	/**
	 * By impegnabile componente importi capitolo.
	 *
	 * @param Impegnabile ComponenteImportiCapitolo the   impegnabileComponenteImportiCapitolo
	 * @return the siac d bil elem det comp tipo imp enum
	 */
	public static SiacDBilElemDetCompTipoImpEnum byImpegnabileComponenteImportiCapitolo(ImpegnabileComponenteImportiCapitolo impegnabileComponenteImportiCapitolo){
		for(SiacDBilElemDetCompTipoImpEnum e : SiacDBilElemDetCompTipoImpEnum.values()){
			if(e.getImpegnabileComponenteImportiCapitolo().equals(impegnabileComponenteImportiCapitolo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Impegnabile " + impegnabileComponenteImportiCapitolo + " non ha un mapping corrispondente in SiacDBilElemDetTipoImpEnum");
	}
	/**
	 * By impegnabile componente importi capitolo, even null.
	 *
	 * @param impegnabileComponenteImportiCapitolo the impegnabileComponenteImportiCapitolo
	 * @return the siac d bil elem det comp tipo imp enum
	 */
	public static SiacDBilElemDetCompTipoImpEnum byImpegnabileComponenteImportiCapitoloEvenNull(ImpegnabileComponenteImportiCapitolo impegnabileComponenteImportiCapitolo){
		if(impegnabileComponenteImportiCapitolo == null){
			return null;
		}
		return byImpegnabileComponenteImportiCapitolo(impegnabileComponenteImportiCapitolo);
	}
	
}

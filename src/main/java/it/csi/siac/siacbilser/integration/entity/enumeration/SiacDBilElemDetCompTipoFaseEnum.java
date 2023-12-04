/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.MomentoComponenteImportiCapitolo;

/**
 * The Enum SiacDBilElemDetCompTipoFaseEnum.
 */
@EnumEntity(entityName="SiacDBilElemDetCompTipoFase", idPropertyName="elemDetCompTipoFaseId", codePropertyName="elemDetCompTipoFaseCode")
public enum SiacDBilElemDetCompTipoFaseEnum {

	GESTIONE("01", MomentoComponenteImportiCapitolo.GESTIONE),
	PREVISIONE("02", MomentoComponenteImportiCapitolo.PREVISIONE),
	ROR_EFFETTIVO("03", MomentoComponenteImportiCapitolo.ROR_EFFETTIVO),
	ROR_PREVISIONE("04", MomentoComponenteImportiCapitolo.ROR_PREVISIONE),
	;
	
	/** The codice. */
	private final String codice;
	/** The momento componente importi capitolo. */
	private final MomentoComponenteImportiCapitolo momentoComponenteImportiCapitolo;
	
	/**
	 * Instantiates a new siac d bil elem det comp fonte finanziaria enum.
	 *
	 * @param codice the codice
	 * @param momentoComponenteImportiCapitolo the fonte finanziaria componente importi capitolo
	 */
	private SiacDBilElemDetCompTipoFaseEnum(String codice, MomentoComponenteImportiCapitolo momentoComponenteImportiCapitolo){
		this.codice = codice;
		this.momentoComponenteImportiCapitolo = momentoComponenteImportiCapitolo;
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
	 * @return the momentoComponenteImportiCapitolo
	 */
	public MomentoComponenteImportiCapitolo getMomentoComponenteImportiCapitolo() {
		return this.momentoComponenteImportiCapitolo;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil elem det comp tipo fase enum
	 */
	public static SiacDBilElemDetCompTipoFaseEnum byCodice(String codice){
		for(SiacDBilElemDetCompTipoFaseEnum e : SiacDBilElemDetCompTipoFaseEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemDetCompTipoFaseEnum");
	}
	
	

	/**
	 * By momento componente importi capitolo.
	 *
	 * @param momentoComponenteImportiCapitolo the momentoComponenteImportiCapitolo
	 * @return the siac d bil elem det comp tipo fase enum
	 */
	public static SiacDBilElemDetCompTipoFaseEnum byMomentoComponenteImportiCapitolo(MomentoComponenteImportiCapitolo momentoComponenteImportiCapitolo){
		for(SiacDBilElemDetCompTipoFaseEnum e : SiacDBilElemDetCompTipoFaseEnum.values()){
			if(e.getMomentoComponenteImportiCapitolo().equals(momentoComponenteImportiCapitolo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il momento " + momentoComponenteImportiCapitolo + " non ha un mapping corrispondente in SiacDBilElemDetTipoFaseEnum");
	}
	/**
	 * By fonte finanziaria componente importi capitolo, even null.
	 *
	 * @param momentoComponenteImportiCapitolo the momentoComponenteImportiCapitolo
	 * @return the siac d bil elem det comp tipo fase enum
	 */
	public static SiacDBilElemDetCompTipoFaseEnum byMomentoComponenteImportiCapitoloEvenNull(MomentoComponenteImportiCapitolo momentoComponenteImportiCapitolo){
		if(momentoComponenteImportiCapitolo == null){
			return null;
		}
		return byMomentoComponenteImportiCapitolo(momentoComponenteImportiCapitolo);
	}
	
}

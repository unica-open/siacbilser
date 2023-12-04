/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.StatoTipoComponenteImportiCapitolo;

/**
 * The Enum SiacDBilElemDetCompTipoStatoEnum.
 */
@EnumEntity(entityName="SiacDBilElemDetCompTipoStato", idPropertyName="elemDetCompTipoStatoId", codePropertyName="elemDetCompTipoStatoCode")
public enum SiacDBilElemDetCompTipoStatoEnum {

	VALIDO("V", StatoTipoComponenteImportiCapitolo.VALIDO),
	ANNULLATO("A", StatoTipoComponenteImportiCapitolo.ANNULLATO),
	;
	
	/** The codice. */
	private final String codice;
	
	/** The stato componente importi capitolo. */
	private final StatoTipoComponenteImportiCapitolo statoTipoComponenteImportiCapitolo;
	
	
	
	/**
	 * Instantiates a new siac d bil elem det comp tipo stato enum.
	 *
	 * @param codice the codice
	 * @param statoTipoComponenteImportiCapitolo the stato componente importi capitolo
	 */
	private SiacDBilElemDetCompTipoStatoEnum(String codice, StatoTipoComponenteImportiCapitolo statoTipoComponenteImportiCapitolo){
		this.codice = codice;
		this.statoTipoComponenteImportiCapitolo = statoTipoComponenteImportiCapitolo;
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
	 * @return the statoTipoComponenteImportiCapitolo
	 */
	public StatoTipoComponenteImportiCapitolo getStatoTipoComponenteImportiCapitolo() {
		return this.statoTipoComponenteImportiCapitolo;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil elem det comp tipo stato enum
	 */
	public static SiacDBilElemDetCompTipoStatoEnum byCodice(String codice){
		for(SiacDBilElemDetCompTipoStatoEnum e : SiacDBilElemDetCompTipoStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemDetCompTipoStatoEnum");
	}
	
	

	/**
	 * By stato componente importi capitolo.
	 *
	 * @param statoTipoComponenteImportiCapitolo the statoTipoComponenteImportiCapitolo
	 * @return the siac d bil elem det compo tipo stato enum
	 */
	public static SiacDBilElemDetCompTipoStatoEnum byStatoTipoComponenteImportiCapitolo(StatoTipoComponenteImportiCapitolo statoTipoComponenteImportiCapitolo){
		for(SiacDBilElemDetCompTipoStatoEnum e : SiacDBilElemDetCompTipoStatoEnum.values()){
			if(e.getStatoTipoComponenteImportiCapitolo().equals(statoTipoComponenteImportiCapitolo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato "+ statoTipoComponenteImportiCapitolo + " non ha un mapping corrispondente in SiacDBilElemDetCompTipoStatoEnum");
	}
	
	public static SiacDBilElemDetCompTipoStatoEnum byStatoTipoComponenteImportiCapitoloEvenNull(StatoTipoComponenteImportiCapitolo statoTipoComponenteImportiCapitolo){
		if(statoTipoComponenteImportiCapitolo == null){
			return null;
		}
		return byStatoTipoComponenteImportiCapitolo(statoTipoComponenteImportiCapitolo);
	}
	
}

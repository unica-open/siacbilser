/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.ParteQuadroEconomico;

/**
 * The Enum SiacDVariazioneParteEnum.
 */
@EnumEntity(entityName="SiacDQuadroEconomicoParte", idPropertyName="parteId", codePropertyName="parteCode")
public enum SiacDQuadroEconomicoParteEnum {
	
	A("A", ParteQuadroEconomico.A),	
	B("B", ParteQuadroEconomico.B),
	C("C", ParteQuadroEconomico.C),
	;	
	
	
	private final String codice;
	private final ParteQuadroEconomico parteQuadroEconomico;
	
	/**
	 * Instantiates a new siac d variazione stato enum.
	 *
	 * @param codice the codice
	 * @param soeb the soeb
	 */
	SiacDQuadroEconomicoParteEnum(String codice, ParteQuadroEconomico soeb){
		this.codice = codice;
		this.parteQuadroEconomico = soeb;
	}
	
	/**
	 * By stato operativo classificatore gsa.
	 *
	 * @param soeb the soeb
	 * @return the siac d variazione stato enum
	 */
	public static SiacDQuadroEconomicoParteEnum byParte(ParteQuadroEconomico soeb){
		for(SiacDQuadroEconomicoParteEnum e : SiacDQuadroEconomicoParteEnum.values()){
			if(e.getParteQuadroEconomico().equals(soeb)){
				return e;
			}
		}
		throw new IllegalArgumentException("Parte operativo "+ soeb + " non ha un mapping corrispondente in SiacDQuadroEconomicoParteEnum");
	}
	
	/**
	 * By stato operativo classificatore gsa even null.
	 *
	 * @param soeb the soeb
	 * @return the siac d variazione stato enum
	 */
	public static SiacDQuadroEconomicoParteEnum byParteQuadroEconomicoEvenNull(ParteQuadroEconomico soeb){
		if(soeb==null){
			return null;
		}		
		return byParte(soeb);
		
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d variazione stato enum
	 */
	public static SiacDQuadroEconomicoParteEnum byCodice(String codice){
		for(SiacDQuadroEconomicoParteEnum e : SiacDQuadroEconomicoParteEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDQuadroEconomicoParteEnum");
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
	 * Gets the stato operativo ParteQuadroEconomico.
	 *
	 * @return the ParteQuadroEconomico
	 */
	public ParteQuadroEconomico getParteQuadroEconomico() {
		return parteQuadroEconomico;
	}
	

}

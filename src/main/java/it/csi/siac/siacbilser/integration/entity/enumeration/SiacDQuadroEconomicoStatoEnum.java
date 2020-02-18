/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.StatoOperativoQuadroEconomico;

/**
 * The Enum SiacDVariazioneStatoEnum.
 */
@EnumEntity(entityName="SiacDQuadroEconomicoStato", idPropertyName="quadroEconomicoStatoId", codePropertyName="quadroEconomicoStatoCode")
public enum SiacDQuadroEconomicoStatoEnum {
	
	/** The valido. */
	VALIDO("V", StatoOperativoQuadroEconomico.VALIDO),
	
	/** The annullato. */
	ANNULLATO("A", StatoOperativoQuadroEconomico.ANNULLATO),
	;	
	
	
	private final String codice;
	private final StatoOperativoQuadroEconomico statoOperativoQuadroEconomico;
	
	/**
	 * Instantiates a new siac d variazione stato enum.
	 *
	 * @param codice the codice
	 * @param soeb the soeb
	 */
	SiacDQuadroEconomicoStatoEnum(String codice, StatoOperativoQuadroEconomico soeb){
		this.codice = codice;
		this.statoOperativoQuadroEconomico = soeb;
	}
	
	/**
	 * By stato operativo classificatore gsa.
	 *
	 * @param soeb the soeb
	 * @return the siac d variazione stato enum
	 */
	public static SiacDQuadroEconomicoStatoEnum byStatoOperativo(StatoOperativoQuadroEconomico soeb){
		for(SiacDQuadroEconomicoStatoEnum e : SiacDQuadroEconomicoStatoEnum.values()){
			if(e.getStatoOperativoQuadroEconomico().equals(soeb)){
				return e;
			}
		}
		throw new IllegalArgumentException("Stato operativo "+ soeb + " non ha un mapping corrispondente in SiacDQuadroEconomicoStatoEnum");
	}
	
	/**
	 * By stato operativo classificatore gsa even null.
	 *
	 * @param soeb the soeb
	 * @return the siac d variazione stato enum
	 */
	public static SiacDQuadroEconomicoStatoEnum byStatoOperativoQuadroEconomicoEvenNull(StatoOperativoQuadroEconomico soeb){
		if(soeb==null){
			return null;
		}		
		return byStatoOperativo(soeb);
		
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d variazione stato enum
	 */
	public static SiacDQuadroEconomicoStatoEnum byCodice(String codice){
		for(SiacDQuadroEconomicoStatoEnum e : SiacDQuadroEconomicoStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDVariazioneStatoEnum");
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
	 * Gets the stato operativo classificatore gsa.
	 *
	 * @return the stato operativo classificatore gsa
	 */
	public StatoOperativoQuadroEconomico getStatoOperativoQuadroEconomico() {
		return statoOperativoQuadroEconomico;
	}
	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;


// TODO: Auto-generated Javadoc
/**
 * select replace(initcap(elem_stato_desc),' ','') || '("' || elem_stato_code || '")' from siac_d_bil_elem_stato.
 *
 * @author Domenico
 */
@EnumEntity(entityName="SiacDBilElemStato", idPropertyName="elemStatoId", codePropertyName="elemStatoCode")
public enum SiacDBilElemStatoEnum {
	
	/** The Annullato. */
	Annullato("AN", StatoOperativoElementoDiBilancio.ANNULLATO),
	
	/** The Valido. */
	Valido("VA",  StatoOperativoElementoDiBilancio.VALIDO),
	
	/** The Provvisorio. */
	Provvisorio("PR",  StatoOperativoElementoDiBilancio.PROVVISORIO);
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo elemento di bilancio. */
	private final StatoOperativoElementoDiBilancio statoOperativoElementoDiBilancio;
	
	/**
	 * Instantiates a new siac d bil elem stato enum.
	 *
	 * @param codice the codice
	 * @param soeb the soeb
	 */
	SiacDBilElemStatoEnum(String codice, StatoOperativoElementoDiBilancio soeb){		
		this.codice = codice;
		this.statoOperativoElementoDiBilancio = soeb;
	}
	
	/**
	 * By stato operativo elemento di bilancio.
	 *
	 * @param soeb the soeb
	 * @return the siac d bil elem stato enum
	 */
	public static SiacDBilElemStatoEnum byStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio soeb){
		for(SiacDBilElemStatoEnum e : SiacDBilElemStatoEnum.values()){
			if(e.getStatoOperativoElementoDiBilancio().equals(soeb)){
				return e;
			}
		}
		throw new IllegalArgumentException("Stato operativo "+ soeb + " non ha un mapping corrispondente in SiacDBilElemStatoEnum");
	}
	
	
	/**
	 * By stato operativo elemento di bilancio even null.
	 *
	 * @param soeb the soeb
	 * @return the siac d bil elem stato enum
	 */
	public static SiacDBilElemStatoEnum byStatoOperativoElementoDiBilancioEvenNull(StatoOperativoElementoDiBilancio soeb){
		if(soeb==null){
			return null;
		}
		return byStatoOperativoElementoDiBilancio(soeb);
	}
	
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil elem stato enum
	 */
	public static SiacDBilElemStatoEnum byCodice(String codice){
		for(SiacDBilElemStatoEnum e : SiacDBilElemStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilElemStatoEnum");
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
	 * Gets the stato operativo elemento di bilancio.
	 *
	 * @return the stato operativo elemento di bilancio
	 */
	public StatoOperativoElementoDiBilancio getStatoOperativoElementoDiBilancio() {
		return statoOperativoElementoDiBilancio;
	}
	
	


	


}

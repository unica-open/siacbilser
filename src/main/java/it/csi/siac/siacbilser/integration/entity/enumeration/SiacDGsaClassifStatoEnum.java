/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacgenser.model.StatoOperativoClassificatoreGSA;

/**
 * The Enum SiacDVariazioneStatoEnum.
 */
@EnumEntity(entityName="SiacDGsaClassifStato", idPropertyName="gsaClassifStatoId", codePropertyName="gsaClassifStatoCode")
public enum SiacDGsaClassifStatoEnum {
	
	/** The valido. */
	VALIDO("V", StatoOperativoClassificatoreGSA.VALIDO),
	
	/** The annullato. */
	ANNULLATO("A", StatoOperativoClassificatoreGSA.ANNULLATO),
	;	
	
	
	private final String codice;
	private final StatoOperativoClassificatoreGSA statoOperativoClassificatoreGSA;
	
	/**
	 * Instantiates a new siac d variazione stato enum.
	 *
	 * @param codice the codice
	 * @param soeb the soeb
	 */
	SiacDGsaClassifStatoEnum(String codice, StatoOperativoClassificatoreGSA soeb){
		this.codice = codice;
		this.statoOperativoClassificatoreGSA = soeb;
	}
	
	/**
	 * By stato operativo classificatore gsa.
	 *
	 * @param soeb the soeb
	 * @return the siac d variazione stato enum
	 */
	public static SiacDGsaClassifStatoEnum byStatoOperativo(StatoOperativoClassificatoreGSA soeb){
		for(SiacDGsaClassifStatoEnum e : SiacDGsaClassifStatoEnum.values()){
			if(e.getStatoOperativoClassificatoreGSA().equals(soeb)){
				return e;
			}
		}
		throw new IllegalArgumentException("Stato operativo "+ soeb + " non ha un mapping corrispondente in SiacDVariazioneStatoEnum");
	}
	
	/**
	 * By stato operativo classificatore gsa even null.
	 *
	 * @param soeb the soeb
	 * @return the siac d variazione stato enum
	 */
	public static SiacDGsaClassifStatoEnum byStatoOperativoClassificatoreGSAEvenNull(StatoOperativoClassificatoreGSA soeb){
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
	public static SiacDGsaClassifStatoEnum byCodice(String codice){
		for(SiacDGsaClassifStatoEnum e : SiacDGsaClassifStatoEnum.values()){
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
	public StatoOperativoClassificatoreGSA getStatoOperativoClassificatoreGSA() {
		return statoOperativoClassificatoreGSA;
	}
	

}

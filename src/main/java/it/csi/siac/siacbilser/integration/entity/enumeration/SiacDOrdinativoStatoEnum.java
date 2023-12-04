/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;

/**
 * The Enum SiacDOrdinativoStatoEnum.
 */
@EnumEntity(entityName="SiacDOrdinativoStato", idPropertyName="ordinativoStatoId", codePropertyName="ordinativoStatoCode")
public enum SiacDOrdinativoStatoEnum {
		
	Inserito("I", StatoOperativoOrdinativo.INSERITO),
	Trasmesso("T", StatoOperativoOrdinativo.TRASMESSO),
	Firmato("F", StatoOperativoOrdinativo.FIRMATO),
	Quietanzato("Q", StatoOperativoOrdinativo.QUIETANZATO),
	Annullato("A", StatoOperativoOrdinativo.ANNULLATO);

	
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoOperativoOrdinativo statoOperativo;
	
	/**
	 * Instantiates a new siac d subdoc iva stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDOrdinativoStatoEnum(String codice, StatoOperativoOrdinativo statoOperativo){
		this.codice = codice;
		this.statoOperativo = statoOperativo;
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
	 * Gets the stato operativo.
	 *
	 * @return the stato operativo
	 */
	public StatoOperativoOrdinativo getStatoOperativo() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d subdoc iva stato enum
	 */
	public static SiacDOrdinativoStatoEnum byCodice(String codice){
		for(SiacDOrdinativoStatoEnum e : SiacDOrdinativoStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDOrdinativoStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d subdoc iva stato enum
	 */
	public static SiacDOrdinativoStatoEnum byStatoOperativo(StatoOperativoOrdinativo stato){
		for(SiacDOrdinativoStatoEnum e : SiacDOrdinativoStatoEnum.values()){
			if(e.getStatoOperativo().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ stato + " non ha un mapping corrispondente in SiacDOrdinativoStatoEnum");
	}
	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.StatoOperativo;


// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDVincoloStatoEnum.
 */
@EnumEntity(entityName="SiacDVincoloStato", idPropertyName="vincoloStatoId", codePropertyName="vincoloStatoCode")
public enum SiacDVincoloStatoEnum {
	
	/** The Valido. */
	Valido("V", StatoOperativo.VALIDO),
	
	/** The Annullato. */
	Annullato("A", StatoOperativo.ANNULLATO);
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoOperativo statoOperativo;
	
	/**
	 * Instantiates a new siac d vincolo stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDVincoloStatoEnum(String codice, StatoOperativo statoOperativo){
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
	public StatoOperativo getStatoOperativo() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d vincolo stato enum
	 */
	public static SiacDVincoloStatoEnum byCodice(String codice){
		for(SiacDVincoloStatoEnum e : SiacDVincoloStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDVincoloStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param tipo the tipo
	 * @return the siac d vincolo stato enum
	 */
	public static SiacDVincoloStatoEnum byStatoOperativo(StatoOperativo tipo){
		for(SiacDVincoloStatoEnum e : SiacDVincoloStatoEnum.values()){
			if(e.getStatoOperativo().equals(tipo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ tipo + " non ha un mapping corrispondente in SiacDVincoloStatoEnum");
	}
	

}

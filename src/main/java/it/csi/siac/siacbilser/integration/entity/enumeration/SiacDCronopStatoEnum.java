/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.StatoOperativoCronoprogramma;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDCronopStatoEnum.
 */
@EnumEntity(entityName="SiacDCronopStato", idPropertyName="cronopStatoId", codePropertyName="cronopStatoCode")
public enum SiacDCronopStatoEnum {
	
	/** The Valido. */
	Valido("VA", StatoOperativoCronoprogramma.VALIDO),
	
	/** The Annullato. */
	Annullato("AN", StatoOperativoCronoprogramma.ANNULLATO),
	
	/** The Provvisorio. */
	Provvisorio("PR", StatoOperativoCronoprogramma.PROVVISORIO),
	;
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoOperativoCronoprogramma statoOperativo;
	
	/**
	 * Instantiates a new siac d cronop stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDCronopStatoEnum(String codice, StatoOperativoCronoprogramma statoOperativo){
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
	public StatoOperativoCronoprogramma getStatoOperativo() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d cronop stato enum
	 */
	public static SiacDCronopStatoEnum byCodice(String codice){
		for(SiacDCronopStatoEnum e : SiacDCronopStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDCronopStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d cronop stato enum
	 */
	public static SiacDCronopStatoEnum byStatoOperativo(StatoOperativoCronoprogramma stato){
		for(SiacDCronopStatoEnum e : SiacDCronopStatoEnum.values()){
			if(e.getStatoOperativo().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ stato + " non ha un mapping corrispondente in SiacDCronopStatoEnum");
	}
	

}

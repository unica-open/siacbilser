/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siaccecser.model.StatoOperativoOperazioneCassa;

/**
 * The Enum SiacDDocStatoEnum.
 */
@EnumEntity(entityName="SiacDCassaEconOperazStato", idPropertyName="cassaeconopStatoId", codePropertyName="cassaeconopStatoCode")
public enum SiacDCassaEconOperazStatoEnum {
		
	Provvisorio("P", StatoOperativoOperazioneCassa.PROVVISORIO),
	Definitivo("D", StatoOperativoOperazioneCassa.DEFINITIVO), 
	Annullato("A", StatoOperativoOperazioneCassa.ANNULLATO);
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoOperativoOperazioneCassa statoOperativo;
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDCassaEconOperazStatoEnum(String codice, StatoOperativoOperazioneCassa statoOperativo){
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
	public StatoOperativoOperazioneCassa getStatoOperativo() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDCassaEconOperazStatoEnum byCodice(String codice){
		for(SiacDCassaEconOperazStatoEnum e : SiacDCassaEconOperazStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDCassaEconOperazStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d cassa econ operaz stato enum
	 */
	public static SiacDCassaEconOperazStatoEnum byStatoOperativo(StatoOperativoOperazioneCassa stato){
		for(SiacDCassaEconOperazStatoEnum e : SiacDCassaEconOperazStatoEnum.values()){
			if(e.getStatoOperativo().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ stato + " non ha un mapping corrispondente in SiacDCassaEconOperazStatoEnum");
	}
	


}

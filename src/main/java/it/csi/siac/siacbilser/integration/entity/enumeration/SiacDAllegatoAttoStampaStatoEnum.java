/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.StatoOperativoStampaAllegatoAtto;

@EnumEntity(entityName="SiacDAllegatoAttoStampaStato", idPropertyName="attoalstStatoId", codePropertyName="attoalstStatoCode")
public enum SiacDAllegatoAttoStampaStatoEnum {

	VALIDO ("V",StatoOperativoStampaAllegatoAtto.VALIDO),
	;
	/** The codice. */
	private final String codice;
	
	/** The stato stampa. */
	private final StatoOperativoStampaAllegatoAtto statoOperativoStampaAllegatoAtto;
	
	/**
	 * Instantiates a new siac d allegato atto stampa stato tipo enum.
	 *
	 * @param codice the codice
	 * @param StatoOperativoStampaAllegatoAtto the stato operativo stampa allegato atto
	 */
	private SiacDAllegatoAttoStampaStatoEnum(String codice, StatoOperativoStampaAllegatoAtto statoOperativoStampaAllegatoAtto){
		this.codice= codice;
		this.statoOperativoStampaAllegatoAtto = statoOperativoStampaAllegatoAtto;
	}

	public String getCodice() {
		return codice;
	}
	
	public StatoOperativoStampaAllegatoAtto getStatoOperativoStampaAllegatoAtto(){
		return statoOperativoStampaAllegatoAtto;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the SiacDIvaStampaStatoEnum
	 */
	public static SiacDAllegatoAttoStampaStatoEnum byCodice(String codice){
		for(SiacDAllegatoAttoStampaStatoEnum e : SiacDAllegatoAttoStampaStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDAllegatoAttoStampaStatoEnum");
	}
	
	/**
	 * By stato stampa.
	 *
	 * @param stato the statooperativo
	 * @return the SiacDAllegatoAttoStampaStatoEnum
	 */
	public static SiacDAllegatoAttoStampaStatoEnum byStatoStampa(StatoOperativoStampaAllegatoAtto stato){
		for(SiacDAllegatoAttoStampaStatoEnum e : SiacDAllegatoAttoStampaStatoEnum.values()){
			if(e.getStatoOperativoStampaAllegatoAtto().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo stampa allegato atto "+ stato + " non ha un mapping corrispondente in SiacDAllegatoAttoStampaStatoEnum");
	}
}

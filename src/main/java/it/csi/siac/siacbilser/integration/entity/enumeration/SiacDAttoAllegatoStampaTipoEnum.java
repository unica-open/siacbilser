/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoStampaAllegatoAtto;

@EnumEntity(entityName="SiacDAttoAllegatoStampaTipo", idPropertyName="attoalstTipoId", codePropertyName="attoalstTipoCode")
public enum SiacDAttoAllegatoStampaTipoEnum {
	ALLEGATO("AL", TipoStampaAllegatoAtto.ALLEGATO),
	;
	/** The codice. */
	private final String codice;
	
	/** The stato stampa. */
	private final TipoStampaAllegatoAtto tipoStampaAllegatoAtto;

	/**
	 * Instantiates a new siac d allegato atto stampa tipo enum.
	 *
	 * @param codice the codice
	 * @param TipoStampaAllegatoAtto the stato operativo stampa allegato atto
	 */
	private SiacDAttoAllegatoStampaTipoEnum(String codice, TipoStampaAllegatoAtto tipoStampaAllegatoAtto){
		this.codice= codice;
		this.tipoStampaAllegatoAtto =tipoStampaAllegatoAtto;
	}
	
	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	
	/**
	 * @return the tipoStampaAllegatoAtto
	 */
	public TipoStampaAllegatoAtto getTipoStampaAllegatoAtto() {
		return tipoStampaAllegatoAtto;
	}

	/**
	 * By stato stampa.
	 *
	 * @param tipo the tipo stampa
	 * @return the SiacDAttoAllegatoStampaTipoEnum
	 */
	public static SiacDAttoAllegatoStampaTipoEnum byTipoStampa(TipoStampaAllegatoAtto stato) {
		for(SiacDAttoAllegatoStampaTipoEnum e : SiacDAttoAllegatoStampaTipoEnum.values()){
			if(e.getTipoStampaAllegatoAtto().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo stampa allegato atto "+ stato + " non ha un mapping corrispondente in SiacDAttoAllegatoStampaTipoEnum");
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the SiacDAttoAllegatoStampaStatoEnum
	 */
	
	public static SiacDAttoAllegatoStampaTipoEnum byCodice(String codice) {
		for (SiacDAttoAllegatoStampaTipoEnum e : SiacDAttoAllegatoStampaTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDAttoAllegatoStampaTipoEnum");
	}


}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoStampa;


@EnumEntity(entityName="SiacDIvaStampaStato", idPropertyName="ivastStatoId", codePropertyName="ivastStatoCode")
public enum SiacDIvaStampaStatoEnum {
	
	
	Bozza      ("B", TipoStampa.BOZZA),
	
	
	Definitiva("D", TipoStampa.DEFINITIVA),
	
	;
	
	
	
	/** The codice. */
	private final String codice;
	
	/** The tipo stampa. */
	private final TipoStampa tipoStampa;
	
	
	/**
	 * Instantiates a new siac d iva registro tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoRegistroIva the tipo registro iva
	 */
	private SiacDIvaStampaStatoEnum(String codice, TipoStampa tipoStampa){
		this.codice = codice;
		this.tipoStampa = tipoStampa;
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
	 * Gets the tipo stampa.
	 *
	 * @return the tipo stampa
	 */
	public TipoStampa getTipoStampa() {
		return tipoStampa;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the SiacDIvaStampaStatoEnum
	 */
	public static SiacDIvaStampaStatoEnum byCodice(String codice){
		for(SiacDIvaStampaStatoEnum e : SiacDIvaStampaStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDIvaStampaStatoEnum");
	}
	
	/**
	 * By tipo stampa.
	 *
	 * @param tipo the tipo
	 * @return the SiacDIvaStampaStatoEnum
	 */
	public static SiacDIvaStampaStatoEnum byTipoStampa(TipoStampa tipo){
		for(SiacDIvaStampaStatoEnum e : SiacDIvaStampaStatoEnum.values()){
			if(e.getTipoStampa().equals(tipo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo stampa iva "+ tipo + " non ha un mapping corrispondente in SiacDIvaRegistroTipoEnum");
	}
	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoStampaAllegatoAtto;

@EnumEntity(entityName="SiacTReptjTemplate", idPropertyName="repjt_id", codePropertyName="repjtCode")
public enum SiacTRepjTemplateEnum {
	ALLEGATO("AllegatoAtto",TipoStampaAllegatoAtto.ALLEGATO),
	;
	/** the codice  */
	private final String codice;
	/** the descrizione*/
	private final TipoStampaAllegatoAtto tipoStampaAllegatoAtto;
	
	/**
	 * Instantiates a new SiacTReptjTemplateEnum
	 * @param codice the codice
	 * @param TipoStampaAllegatoAtto the tipoStampaAllegatoAtto
	 * 
	 * */ 
	private SiacTRepjTemplateEnum(String codice, TipoStampaAllegatoAtto tipoStampa){
		this.codice = codice;
		this.tipoStampaAllegatoAtto = tipoStampa;
		
	}
	
	/**
	 * @returns the codice
	 * */
	private String getCodice(){
		return codice;
	}
	
	/**
	 * @returns the TipoStampaAllegatoAtto
	 * */
	private TipoStampaAllegatoAtto getTipoStampaAllegatoAtto(){
		return tipoStampaAllegatoAtto;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the SiacTReptjTemplateEnum
	 */
	public static SiacTRepjTemplateEnum byCodice(String codice) {
		
		for(SiacTRepjTemplateEnum e : SiacTRepjTemplateEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacTReptjTemplateEnum");
	}
	
	/**
	 * By tipo stampa.
	 *
	 * @param codice the tipo stampa allegato atto
	 * @return the SiacTReptjTemplateEnum
	 */
	public static SiacTRepjTemplateEnum byTipoStampa(TipoStampaAllegatoAtto tipoStampa){

		for(SiacTRepjTemplateEnum e : SiacTRepjTemplateEnum.values() ){
			if(e.getTipoStampaAllegatoAtto().equals(tipoStampa)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ tipoStampa + " non ha un mapping corrispondente in SiacTReptjTemplateEnum");
	}
}

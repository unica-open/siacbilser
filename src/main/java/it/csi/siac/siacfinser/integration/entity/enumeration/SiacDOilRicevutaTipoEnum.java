/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.enumeration;

import it.csi.siac.siacfinser.integration.entity.converter.EnumEntityFin;

@EnumEntityFin(entityName="SiacDOilRicevutaTipo", idPropertyName="oilRicevutaTipoId", codePropertyName="oilRicevutaTipoCode")
public enum SiacDOilRicevutaTipoEnum {

	FIRMA("F"),
	QUIETANZA("Q"),
	STORNO_QUIETANZA("S"),
	PROVVISORIO_DI_CASSA("P"),
	STORNO_PROVVISORIO_DI_CASSA("PS");
	
	
	private String codice;
	

	/**
	 * @param codice
	 */
	SiacDOilRicevutaTipoEnum(String codice){
		this.codice = codice;
	}
	
	public String getCodice() {
		return codice;
	}
	

	public static SiacDOilRicevutaTipoEnum byCodice(String codice){
		for(SiacDOilRicevutaTipoEnum e : SiacDOilRicevutaTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDOilRicevutaTipo");
	}
	

	
}

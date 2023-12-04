/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.enumeration;

import it.csi.siac.siacfinser.integration.entity.converter.EnumEntityFin;
import it.csi.siac.siacfinser.model.carta.CartaContabile.StatoOperativoCartaContabile;


@EnumEntityFin(entityName="SiacDCartacontStatoFin", idPropertyName="cartacStatoId", codePropertyName="cartacStatoCode")
public enum SiacDCartaContabileStatoOpEnum {


	Provvisorio("P", StatoOperativoCartaContabile.PROVVISORIO),
	Annullato("A", StatoOperativoCartaContabile.ANNULLATO),
	Completato("C", StatoOperativoCartaContabile.COMPLETATO),
	Trasmesso("T", StatoOperativoCartaContabile.TRASMESSO);
	

	private String codice;
	private StatoOperativoCartaContabile statoCarta;
	
	
	
	/**
	 * @param codice
	 */
	SiacDCartaContabileStatoOpEnum(String codice, StatoOperativoCartaContabile statoCarta){
		this.codice = codice;
		this.statoCarta = statoCarta;
	}
	
	public String getCodice() {
		return codice;
	}
	
	public StatoOperativoCartaContabile getStatoCartaContabile() {
		return statoCarta;
	}

	public static SiacDCartaContabileStatoOpEnum byCodice(String codice){
		for(SiacDCartaContabileStatoOpEnum e : SiacDCartaContabileStatoOpEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDCartaContabileStatoOpEnum");
	}
	
	public static SiacDCartaContabileStatoOpEnum byStatoCarta(StatoOperativoCartaContabile statoCarta){
		for(SiacDCartaContabileStatoOpEnum e : SiacDCartaContabileStatoOpEnum.values()){
			if(e.getStatoCartaContabile().equals(statoCarta)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato di carta "+ statoCarta + " non ha un mapping corrispondente in SiacDCartaContabileStatoOpEnum");
	}
	
}

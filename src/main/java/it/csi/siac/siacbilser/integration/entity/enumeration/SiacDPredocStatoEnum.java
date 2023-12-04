/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDPredocStatoEnum.
 */
@EnumEntity(entityName="SiacDPredocStato", idPropertyName="predocStatoId", codePropertyName="predocStatoCode")
public enum SiacDPredocStatoEnum {
		
	/** The Incompleto. */
	Incompleto("I", StatoOperativoPreDocumento.INCOMPLETO),
	
	/** The Completo. */
	Completo("C", StatoOperativoPreDocumento.COMPLETO), 
	
	/** The Annullato. */
	Annullato("A", StatoOperativoPreDocumento.ANNULLATO), 
	
	/** The Definito. */
	Definito("D", StatoOperativoPreDocumento.DEFINITO);
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoOperativoPreDocumento statoOperativo;
	
	/**
	 * Instantiates a new siac d predoc stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDPredocStatoEnum(String codice, StatoOperativoPreDocumento statoOperativo){
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
	public StatoOperativoPreDocumento getStatoOperativo() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d predoc stato enum
	 */
	public static SiacDPredocStatoEnum byCodice(String codice){
		for(SiacDPredocStatoEnum e : SiacDPredocStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDPredocStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d predoc stato enum
	 */
	public static SiacDPredocStatoEnum byStatoOperativo(StatoOperativoPreDocumento stato){
		for(SiacDPredocStatoEnum e : SiacDPredocStatoEnum.values()){
			if(e.getStatoOperativo().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ stato + " non ha un mapping corrispondente in SiacDPredocStatoEnum");
	}
	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.StatoOperativoProgetto;

// TODO: Auto-generated Javadoc
/**
 * Enum per gli stati del dell'entity SiacTProgramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/02/2014
 *
 */
@EnumEntity(entityName="SiacDProgrammaStato", idPropertyName="programmaStatoId", codePropertyName="programmaStatoCode")
public enum SiacDProgrammaStatoEnum {
	
	/** The Valido. */
	Valido("VA", StatoOperativoProgetto.VALIDO),
	
	/** The Annullato. */
	Annullato("AN", StatoOperativoProgetto.ANNULLATO);
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo progetto. */
	private final StatoOperativoProgetto statoOperativoProgetto;
	
	/**
	 * Instantiates a new siac d programma stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativoProgetto the stato operativo progetto
	 */
	private SiacDProgrammaStatoEnum(String codice, StatoOperativoProgetto statoOperativoProgetto){
		this.codice = codice;
		this.statoOperativoProgetto = statoOperativoProgetto;
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
	 * Gets the stato operativo progetto.
	 *
	 * @return the stato operativo progetto
	 */
	public StatoOperativoProgetto getStatoOperativoProgetto() {
		return statoOperativoProgetto;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d programma stato enum
	 */
	public static SiacDProgrammaStatoEnum byCodice(String codice){
		for(SiacDProgrammaStatoEnum e : SiacDProgrammaStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice " + codice + " non ha un mapping corrispondente in SiacDProgrammaStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param tipo the tipo
	 * @return the siac d programma stato enum
	 */
	public static SiacDProgrammaStatoEnum byStatoOperativo(StatoOperativoProgetto tipo){
		for(SiacDProgrammaStatoEnum e : SiacDProgrammaStatoEnum.values()){
			if(e.getStatoOperativoProgetto().equals(tipo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo " + tipo + " non ha un mapping corrispondente in SiacDProgrammaStatoEnum");
	}
	
	/**
	 * By stato operativo even null.
	 *
	 * @param tipo the tipo
	 * @return the siac d programma stato enum
	 */
	public static SiacDProgrammaStatoEnum byStatoOperativoEvenNull(StatoOperativoProgetto tipo){
		if(tipo == null) {
			return null;
		}
		return byStatoOperativo(tipo);
	}
	
}

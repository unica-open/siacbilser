/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.TipoProgetto;

// TODO: Auto-generated Javadoc
/**
 * Enum per gli stati del dell'entity SiacTProgramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/02/2014
 *
 */
@EnumEntity(entityName="SiacDProgrammaTipo", idPropertyName="programmaTipoId", codePropertyName="programmaTipoCode")
public enum SiacDProgrammaTipoEnum {
	
	/** The Valido. */
	Previsione("P", TipoProgetto.PREVISIONE),
	
	/** The Annullato. */
	Gestione("G", TipoProgetto.GESTIONE);
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo progetto. */
	private final TipoProgetto sipoProgetto;
	
	/**
	 * Instantiates a new siac d programma stato enum.
	 *
	 * @param codice the codice
	 * @param sipoProgetto the stato operativo progetto
	 */
	private SiacDProgrammaTipoEnum(String codice, TipoProgetto sipoProgetto){
		this.codice = codice;
		this.sipoProgetto = sipoProgetto;
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
	public TipoProgetto getTipoProgetto() {
		return sipoProgetto;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d programma stato enum
	 */
	public static SiacDProgrammaTipoEnum byCodice(String codice){
		for(SiacDProgrammaTipoEnum e : SiacDProgrammaTipoEnum.values()){
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
	public static SiacDProgrammaTipoEnum byTipoProgetto(TipoProgetto tipo){
		for(SiacDProgrammaTipoEnum e : SiacDProgrammaTipoEnum.values()){
			if(e.getTipoProgetto().equals(tipo)){
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
	public static SiacDProgrammaTipoEnum byTipoProgettoEvenNull(TipoProgetto tipo){
		if(tipo == null) {
			return null;
		}
		return byTipoProgetto(tipo);
	}
	
}

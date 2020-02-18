/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDSoggettoStatoEnum.
 */
@EnumEntity(entityName="SiacDSoggettoStato", idPropertyName="soggettoStatoId", codePropertyName="soggettoStatoCode")
public enum SiacDSoggettoStatoEnum {
	
	/** The Annullato. */
	Annullato("ANNULLATO", StatoOperativoAnagrafica.ANNULLATO),
	
	/** The Bloccato. */
	Bloccato("BLOCCATO", StatoOperativoAnagrafica.BLOCCATO),
	
	/** The In modifica. */
	InModifica("IN_MODIFICA", StatoOperativoAnagrafica.IN_MODIFICA),
	
	/** The Provvisorio. */
	Provvisorio("PROVVISORIO", StatoOperativoAnagrafica.PROVVISORIO),
	
	/** The Sospeso. */
	Sospeso("SOSPESO", StatoOperativoAnagrafica.SOSPESO),
	
	/** The Valido. */
	Valido("VALIDO", StatoOperativoAnagrafica.VALIDO);

	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo anagrafica. */
	private final StatoOperativoAnagrafica statoOperativoAnagrafica;
	
	/**
	 * Instantiates a new siac d soggetto stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativoAnagrafica the stato operativo anagrafica
	 */
	SiacDSoggettoStatoEnum(String codice, StatoOperativoAnagrafica statoOperativoAnagrafica){
		this.codice = codice;
		this.statoOperativoAnagrafica = statoOperativoAnagrafica;
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
	 * Gets the stato operativo anagrafica.
	 *
	 * @return the stato operativo anagrafica
	 */
	public StatoOperativoAnagrafica getStatoOperativoAnagrafica() {
		return statoOperativoAnagrafica;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d soggetto stato enum
	 */
	public static SiacDSoggettoStatoEnum byCodice(String codice){
		for(SiacDSoggettoStatoEnum e : SiacDSoggettoStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDSoggettoStato");
	}
	
	/**
	 * By stato operativo anagrafica.
	 *
	 * @param statoOperativoAnagrafica the stato operativo anagrafica
	 * @return the siac d soggetto stato enum
	 */
	public static SiacDSoggettoStatoEnum byStatoOperativoAnagrafica(StatoOperativoAnagrafica statoOperativoAnagrafica){
		for(SiacDSoggettoStatoEnum e : SiacDSoggettoStatoEnum.values()){
			if(e.getStatoOperativoAnagrafica().equals(statoOperativoAnagrafica)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato anagrafico "+ statoOperativoAnagrafica + " non ha un mapping corrispondente in SiacDSoggettoStatoEnum");
	}
	
}
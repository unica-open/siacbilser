/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDSubdocIvaStatoEnum.
 */
@EnumEntity(entityName="SiacDSubdocIvaStato", idPropertyName="subdocivaStatoId", codePropertyName="subdocivaStatoCode")
public enum SiacDSubdocIvaStatoEnum {
		
	/** The Definitivo. */
	Definitivo("DE", StatoSubdocumentoIva.DEFINITIVO),
	
	/** The Provvisorio. */
	Provvisorio("PR", StatoSubdocumentoIva.PROVVISORIO), 
	
	/** The Provvisorio definitivo. */
	ProvvisorioDefinitivo("PD", StatoSubdocumentoIva.PROVVISORIO_DEFINITIVO); 
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoSubdocumentoIva statoOperativo;
	
	/**
	 * Instantiates a new siac d subdoc iva stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDSubdocIvaStatoEnum(String codice, StatoSubdocumentoIva statoOperativo){
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
	public StatoSubdocumentoIva getStatoOperativo() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d subdoc iva stato enum
	 */
	public static SiacDSubdocIvaStatoEnum byCodice(String codice){
		for(SiacDSubdocIvaStatoEnum e : SiacDSubdocIvaStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDSubdocIvaStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d subdoc iva stato enum
	 */
	public static SiacDSubdocIvaStatoEnum byStatoOperativo(StatoSubdocumentoIva stato){
		for(SiacDSubdocIvaStatoEnum e : SiacDSubdocIvaStatoEnum.values()){
			if(e.getStatoOperativo().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ stato + " non ha un mapping corrispondente in SiacDSubdocIvaStatoEnum");
	}
	

}

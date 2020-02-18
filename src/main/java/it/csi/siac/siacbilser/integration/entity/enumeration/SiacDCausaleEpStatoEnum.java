/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;

/**
 * The Enum SiacDCausaleEpStatoEnum.
 */
@EnumEntity(entityName = "SiacDCausaleEpStato", idPropertyName = "causaleEpStatoId", codePropertyName = "causaleEpStatoCode")
public enum SiacDCausaleEpStatoEnum {

	Annullato("A", "Annullato", StatoOperativoCausaleEP.ANNULLATO), 
	Provvisorio("P", "Provvisiorio", StatoOperativoCausaleEP.PROVVISORIO), 
	Valido("V", "Valido", StatoOperativoCausaleEP.VALIDO), 
	;
	
	
	public static final String codiceAnnullato = "A";

	private final String codice;
	private final String descrizione;
	private final StatoOperativoCausaleEP statoOperativoCausaleEP;

	/**
	 * Instantiates a new siac d causale ep stato enum.
	 *
	 * @param codice the codice
	 * @param descrizione the descrizione
	 */
	private SiacDCausaleEpStatoEnum(String codice, String descrizione, StatoOperativoCausaleEP statoOperativo) {
		this.codice = codice;
		this.descrizione = descrizione;
		this.statoOperativoCausaleEP = statoOperativo;
	}
	
	

	public static SiacDCausaleEpStatoEnum byStatoOperativo(StatoOperativoCausaleEP statoOperativo) {
		for (SiacDCausaleEpStatoEnum e : SiacDCausaleEpStatoEnum.values()) {
			if (e.getStatoOperativoCausaleEP().equals(statoOperativo)) {
				return e;
			}
		}
		throw new IllegalArgumentException("statoOperativo " + statoOperativo + " non ha un mapping corrispondente in SiacDCausaleEpStatoEnum");
	}
	
	/**
	 * By tipo causale even null.
	 *
	 * @param tipoCausale the tipo causale
	 * @return the siac d causale ep tipo enum
	 */
	public static SiacDCausaleEpStatoEnum byStatoOperativoEvenNull(StatoOperativoCausaleEP statoOperativo) {
		if(statoOperativo==null){
			return null;
		}
		return byStatoOperativo(statoOperativo);
		
	}
	

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d atto legge tipo enum
	 */
	public static SiacDCausaleEpStatoEnum byCodice(String codice) {
		for (SiacDCausaleEpStatoEnum e : SiacDCausaleEpStatoEnum.values()) {
			if (e.getCodice().equals(codice)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Codice " + codice + " non ha un mapping corrispondente in SiacDCausaleEpStatoEnum");
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
	 * Gets the descrizione.
	 *
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}



	/**
	 * @return the statoOperativoCausaleEP
	 */
	public StatoOperativoCausaleEP getStatoOperativoCausaleEP() {
		return statoOperativoCausaleEP;
	}
	
	


}
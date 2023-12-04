/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * The Enum SiacDCausaleEpTipoEnum.
 */
@EnumEntity(entityName = "SiacDCausaleEpTipo", idPropertyName = "causaleEpTipoId", codePropertyName = "causaleEpTipoCode")
public enum SiacDCausaleEpTipoEnum {

	Integrata("INT", "Integrata", TipoCausale.Integrata),
	Libera("LIB", "Libera", TipoCausale.Libera),
	;
	
	private final String codice;
	private final String descrizione;
	private final TipoCausale tipoCausale;

	

	/**
	 * Instantiates a new siac d causale ep tipo enum.
	 *
	 * @param codice the codice
	 * @param descrizione the descrizione
	 */
	private SiacDCausaleEpTipoEnum(String codice, String descrizione, TipoCausale tipoCausale) {
		this.codice = codice;
		this.descrizione = descrizione;
		this.tipoCausale = tipoCausale;
	}
	
	/**
	 * By tipo causale.
	 *
	 * @param tipoCausale the tipo causale
	 * @return the siac d causale ep tipo enum
	 */
	public static SiacDCausaleEpTipoEnum byTipoCausale(TipoCausale tipoCausale) {
		for (SiacDCausaleEpTipoEnum e : SiacDCausaleEpTipoEnum.values()) {
			if (e.getTipoCausale().equals(tipoCausale)) {
				return e;
			}
		}
		throw new IllegalArgumentException("TipoCausale " + tipoCausale + " non ha un mapping corrispondente in SiacDCausaleEpTipoEnum");
	}
	
	/**
	 * By tipo causale even null.
	 *
	 * @param tipoCausale the tipo causale
	 * @return the siac d causale ep tipo enum
	 */
	public static SiacDCausaleEpTipoEnum byTipoCausaleEvenNull(TipoCausale tipoCausale) {
		if(tipoCausale==null){
			return null;
		}
		return byTipoCausale(tipoCausale);
		
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d atto legge tipo enum
	 */
	public static SiacDCausaleEpTipoEnum byCodice(String codice) {
		for (SiacDCausaleEpTipoEnum e : SiacDCausaleEpTipoEnum.values()) {
			if (e.getCodice().equals(codice)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Codice " + codice + " non ha un mapping corrispondente in SiacDCausaleEpTipoEnum");
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
	 * @return the tipoCausale
	 */
	public TipoCausale getTipoCausale() {
		return tipoCausale;
	}



}
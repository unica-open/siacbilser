/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siaccecser.model.TipologiaGiustificativo;

/**
 * The Enum SiacDDocStatoEnum.
 */
@EnumEntity(entityName="SiacDGiustificativoTipo", idPropertyName="giustTipoId", codePropertyName="giustTipoCode")
public enum SiacDGiustificativoTipoEnum {
		
	Rimborso("R", TipologiaGiustificativo.RIMBORSO),
	Anticipo("A", TipologiaGiustificativo.ANTICIPO),
	AnticipoMissione("M", TipologiaGiustificativo.ANTICIPO_MISSIONE),
	Pagamento("P", TipologiaGiustificativo.PAGAMENTO),
	;
	
	/** The codice. */
	private final String codice;
	
	
	private final TipologiaGiustificativo tipologiaGiustificativo;
	
	
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param tipologiaGiustificativo the tipologia giustificativo
	 */
	private SiacDGiustificativoTipoEnum(String codice, TipologiaGiustificativo tipologiaGiustificativo){
		this.codice = codice;
		this.tipologiaGiustificativo = tipologiaGiustificativo;
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
	 * @return the tipologiaGiustificativo
	 */
	public TipologiaGiustificativo getTipologiaGiustificativo() {
		return tipologiaGiustificativo;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d giustificativo tipo enum
	 */
	public static SiacDGiustificativoTipoEnum byCodice(String codice){
		for(SiacDGiustificativoTipoEnum e : SiacDGiustificativoTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDGiustificativoTipoEnum");
	}
	
	/**
	 * By tipologia giustificativo
	 *
	 * @param stato the stato
	 * @return the siac d doc stato enum
	 */
	public static SiacDGiustificativoTipoEnum byTipologiaGiustificativo(TipologiaGiustificativo tipologia){
		for(SiacDGiustificativoTipoEnum e : SiacDGiustificativoTipoEnum.values()){
			if(e.getTipologiaGiustificativo().equals(tipologia)){
				return e;
			}
		}
		throw new IllegalArgumentException("La tipologia di giustificativo "+ tipologia + " non ha un mapping corrispondente in SiacDGiustificativoTipoEnum");
	}
	
	
	

}

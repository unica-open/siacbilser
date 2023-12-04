/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siaccecser.model.TipoDiCassa;

/**
 * The Enum SiacDDocStatoEnum.
 */
@EnumEntity(entityName="SiacDCassaEconTipo", idPropertyName="cassaeconTipoId", codePropertyName="cassaeconTipoCode")
public enum SiacDCassaEconTipoEnum {
		
	ContoCorrenteBancario("CC", TipoDiCassa.CONTO_CORRENTE_BANCARIO),
	Contanti("CO", TipoDiCassa.CONTANTI), 
	Mista("MI", TipoDiCassa.MISTA);
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final TipoDiCassa tipoDiCassa;
	
	
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDCassaEconTipoEnum(String codice, TipoDiCassa tipoDiCassa){
		this.codice = codice;
		this.tipoDiCassa = tipoDiCassa;
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
	public TipoDiCassa getTipoDiCassa() {
		return tipoDiCassa;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDCassaEconTipoEnum byCodice(String codice){
		for(SiacDCassaEconTipoEnum e : SiacDCassaEconTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDCassaEconTipoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d doc stato enum
	 */
	public static SiacDCassaEconTipoEnum byTipoDiCassa(TipoDiCassa tipo){
		for(SiacDCassaEconTipoEnum e : SiacDCassaEconTipoEnum.values()){
			if(e.getTipoDiCassa().equals(tipo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo di cassa "+ tipo + " non ha un mapping corrispondente in SiacDCassaEconTipoEnum");
	}
	
	
	

}

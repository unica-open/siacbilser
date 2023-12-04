/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.visibilita.TipoCampo;

/**
 * The Enum SiacDTipoCampoEnum.
 */
@EnumEntity(entityName="SiacDTipoCampo", idPropertyName="tcId", codePropertyName="tcCode")
public enum SiacDTipoCampoEnum {
		
	NUMERIC("NUMERIC", TipoCampo.NUMERIC),
	INTEGER("INTEGER", TipoCampo.INTEGER),
	TEXT("TEXT", TipoCampo.TEXT),
	BOOLEAN("BOOLEAN", TipoCampo.BOOLEAN),
	;
	
	/** The codice. */
	private final String codice;
	
	/** The tipo campo. */
	private final TipoCampo tipoCampo;
	
	/**
	 * Instantiates a new siac d tipo campo enum.
	 *
	 * @param codice the codice
	 * @param tipoCampo the tipo campo
	 */
	private SiacDTipoCampoEnum(String codice, TipoCampo tipoCampo){
		this.codice = codice;
		this.tipoCampo = tipoCampo;
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
	 * Gets the tipo campo.
	 *
	 * @return the tipoCampo
	 */
	public TipoCampo getTipoCampo() {
		return this.tipoCampo;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d tipo campo enum
	 */
	public static SiacDTipoCampoEnum byCodice(String codice){
		for(SiacDTipoCampoEnum e : SiacDTipoCampoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDTipoCampoEnum");
	}
	
	

	/**
	 * By tipo campo.
	 *
	 * @param tipoCampo the tipo campo
	 * @return the siac d tipo campo enum
	 */
	public static SiacDTipoCampoEnum byTipoCampo(TipoCampo tipoCampo){
		for(SiacDTipoCampoEnum e : SiacDTipoCampoEnum.values()){
			if(e.getTipoCampo().equals(tipoCampo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo  "+ tipoCampo + " non ha un mapping corrispondente in SiacDTipoCampoEnum");
	}
	
	/**
	 * By tipo campo even null.
	 *
	 * @param tipoCampo the tipo campo
	 * @return the siac d tipo campo enum
	 */
	public static SiacDTipoCampoEnum byTipoCampoEvenNull(TipoCampo tipoCampo){
		if(tipoCampo == null){
			return null;
		}
		return byTipoCampo(tipoCampo);
	}
	

}

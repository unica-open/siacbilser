/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;

/**
 * The Enum SiacDSplitreverseIvaTipoEnum.
 */
@EnumEntity(entityName="SiacDSplitreverseIvaTipo", idPropertyName="srivaTipoId", codePropertyName="srivaTipoCode")
public enum SiacDSplitreverseIvaTipoEnum {
	
	SplitIstituzionale("SI", TipoIvaSplitReverse.SPLIT_ISTITUZIONALE),
	SplitCommerciale  ("SC", TipoIvaSplitReverse.SPLIT_COMMERCIALE),
	ReverseChange     ("RC", TipoIvaSplitReverse.REVERSE_CHANGE),
	Esenzione         ("ES", TipoIvaSplitReverse.ESENZIONE),
	;
	
	
	/** The codice. */
	private final String codice;
	
	/** The tipo registro iva. */
	private final TipoIvaSplitReverse tipoIvaSplitReverse;
	
	
	/**
	 * Instantiates a new siac d iva registro tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoRegistroIva the tipo registro iva
	 */
	private SiacDSplitreverseIvaTipoEnum(String codice, TipoIvaSplitReverse tipoIvaSplitReverse){
		this.codice = codice;
		this.tipoIvaSplitReverse = tipoIvaSplitReverse;
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
	 * @return the tipoIvaSplitReverse
	 */
	public TipoIvaSplitReverse getTipoIvaSplitReverse() {
		return tipoIvaSplitReverse;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d splitreverse iva tipo enum
	 */
	public static SiacDSplitreverseIvaTipoEnum byCodice(String codice){
		for(SiacDSplitreverseIvaTipoEnum e : values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDSplitreverseIvaTipoEnum");
	}
	
	/**
	 * By tipo registro iva.
	 *
	 * @param tipo the tipo
	 * @return the siac d splitreverse iva tipo enum
	 */
	public static SiacDSplitreverseIvaTipoEnum byTipoIvaSplitReverse(TipoIvaSplitReverse tipo){
		for(SiacDSplitreverseIvaTipoEnum e : SiacDSplitreverseIvaTipoEnum.values()){
			if(e.getTipoIvaSplitReverse().equals(tipo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo iva split / reverse "+ tipo + " non ha un mapping corrispondente in SiacDSplitreverseIvaTipoEnum");
	}
	

}

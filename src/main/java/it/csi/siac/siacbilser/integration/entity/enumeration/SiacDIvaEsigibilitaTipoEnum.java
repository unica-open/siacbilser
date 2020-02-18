/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoEsigibilitaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;


// TODO: Auto-generated Javadoc
/**
 * Enum per il tipo di registroIva.
 * 
 * 
 *
 */
@EnumEntity(entityName="SiacDIvaEsigibilitaTipo", idPropertyName="ivaesTipoId", codePropertyName="ivaesTipoCode")
public enum SiacDIvaEsigibilitaTipoEnum {
	
	/** The immediata. */
	IMMEDIATA("I", TipoEsigibilitaIva.IMMEDIATA),
	
	/** The differita. */
	DIFFERITA("D", TipoEsigibilitaIva.DIFFERITA);
	
	
	/** The codice. */
	private final String codice;
	
	/** The tipo esigibilita iva. */
	private final TipoEsigibilitaIva tipoEsigibilitaIva;
	
	/**
	 * Instantiates a new siac d iva esigibilita tipo enum.
	 *
	 * @param codice the codice
	 * @param tei the tei
	 */
	private SiacDIvaEsigibilitaTipoEnum(String codice, TipoEsigibilitaIva tei){		
		this.codice = codice;
		this.tipoEsigibilitaIva = tei;
	}
	
	/**
	 * Ottiene l'enum a partire dal tipo esigibilita iva.
	 *
	 * @param tei the tei
	 * @return l'enum corrispondente al tipo, se esistente
	 */
	public static SiacDIvaEsigibilitaTipoEnum byTipoEsigibilitaIva(TipoEsigibilitaIva tei){
		for(SiacDIvaEsigibilitaTipoEnum e : SiacDIvaEsigibilitaTipoEnum.values()){
			if(e.getTipoEsigibilitaIva().equals(tei)){
				return e;
			}
		}
		throw new IllegalArgumentException("Tipo esigibilita iva " + tei + " non ha un mapping corrispondente in SiacDIvaRegistroTipoEnum");
	}
	
	/**
	 * Ottiene l'enum a partire dal tipo di eigibilita iva, anche nel caso in cui essa sia <code>null</code>.
	 *
	 * @param tei the tei
	 * @return l'enum corrispondente al tipo, se esistente
	 * @see SiacDIvaEsigibilitaTipoEnum#byTipoRegistroIva(TipoRegistroIva)
	 */
	public static SiacDIvaEsigibilitaTipoEnum byTipoEsigibilitaIvaEvenNull(TipoEsigibilitaIva tei){
		if(tei == null){
			return null;
		}
		return byTipoEsigibilitaIva(tei);
	}
	
	/**
	 * Ottiene l'enum a partire dal codice del tipo.
	 *
	 * @param codice il codice da cui ricavare l'enum
	 * @return l'enum corrispondente al tipo, se esistente
	 */
	public static SiacDIvaEsigibilitaTipoEnum byCodice(String codice){
		for(SiacDIvaEsigibilitaTipoEnum e : SiacDIvaEsigibilitaTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDIvaEsigbilitaTipoEnum");
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
	 * Gets the tipo esigibilita iva.
	 *
	 * @return the tipoRegistroIva
	 */
	public TipoEsigibilitaIva getTipoEsigibilitaIva() {
		return tipoEsigibilitaIva;
	}

	
	
}

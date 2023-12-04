/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoChiusura;


// TODO: Auto-generated Javadoc
/**
 * Enum per il tipo di operazione Iva.
 * 
 * 
 *
 */
@EnumEntity(entityName="SiacDIvaChiusuraTipo", idPropertyName="ivachiTipoId", codePropertyName="ivachiTipoCode")
public enum SiacDIvaChiusuraTipoEnum {
	
	/** The mensile. */
	MENSILE("M", TipoChiusura.MENSILE),
	
	/** The trimestrale. */
	TRIMESTRALE("T", TipoChiusura.TRIMESTRALE),
	
	/** The annuale. */
	ANNUALE("A", TipoChiusura.ANNUALE);
	
	/** The codice. */
	private final String codice;
	
	/** The tipo chiusura. */
	private final TipoChiusura tipoChiusura;
	
	/**
	 * Instantiates a new siac d iva chiusura tipo enum.
	 *
	 * @param codice the codice
	 * @param tc the tc
	 */
	private SiacDIvaChiusuraTipoEnum(String codice, TipoChiusura tc){		
		this.codice = codice;
		this.tipoChiusura = tc;
	}
	
	/**
	 * Ottiene l'enum a partire dal tipo chiusura iva.
	 *
	 * @param tc il tipo da cui ricavare l'enum
	 * @return l'enum corrispondente al tipo, se esistente
	 */
	public static SiacDIvaChiusuraTipoEnum byTipoChiusura(TipoChiusura tc){
		for(SiacDIvaChiusuraTipoEnum e : SiacDIvaChiusuraTipoEnum.values()){
			if(e.getTipoChiusura().equals(tc)){
				return e;
			}
		}
		throw new IllegalArgumentException("Tipo chiusura " + tc + " non ha un mapping corrispondente in SiacDIvaChiusuraTipoEnum");
	}
	
	/**
	 * Ottiene l'enum a partire dal tipo di chiusura iva, anche nel caso in cui essa sia <code>null</code>.
	 *
	 * @param tc il tipo da cui ricavare l'enum
	 * @return l'enum corrispondente al tipo, se esistente
	 * @see SiacDIvaChiusuraTipoEnum#byTipoChiusura(TipoChiusura)
	 */
	public static SiacDIvaChiusuraTipoEnum byTipoChiusuraEvenNull(TipoChiusura tc){
		if(tc == null){
			return null;
		}
		return byTipoChiusura(tc);
	}
	
	/**
	 * Ottiene l'enum a partire dal codice del tipo.
	 *
	 * @param codice il codice da cui ricavare l'enum
	 * @return l'enum corrispondente al tipo, se esistente
	 */
	public static SiacDIvaChiusuraTipoEnum byCodice(String codice){
		for(SiacDIvaChiusuraTipoEnum e : SiacDIvaChiusuraTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDIvaChiusuraTipoEnum");
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
	 * Gets the tipo chiusura.
	 *
	 * @return the tipoOperazioneIva
	 */
	public TipoChiusura getTipoChiusura() {
		return tipoChiusura;
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoOperazioneIva;


// TODO: Auto-generated Javadoc
/**
 * Enum per il tipo di operazione Iva.
 * 
 * @author Alessandro Marchino - 27/05/2014
 *
 */
@EnumEntity(entityName="SiacDIvaOperazioneTipo", idPropertyName="ivaopTipoId", codePropertyName="ivaopTipoCode")
public enum SiacDIvaOperazioneTipoEnum {
	
	/** The imponibile. */
	IMPONIBILE("I", TipoOperazioneIva.IMPONIBILE),
	
	/** The non imponibile. */
	NON_IMPONIBILE("NI",  TipoOperazioneIva.NON_IMPONIBILE),
	
	/** The esente. */
	ESENTE("ES",  TipoOperazioneIva.ESENTE),
	
	/** The escluso fci. */
	ESCLUSO_FCI("FCI",  TipoOperazioneIva.ESCLUSO_FCI);
	
	/** The codice. */
	private final String codice;
	
	/** The tipo operazione iva. */
	private final TipoOperazioneIva tipoOperazioneIva;
	
	/**
	 * Instantiates a new siac d iva operazione tipo enum.
	 *
	 * @param codice the codice
	 * @param toi the toi
	 */
	private SiacDIvaOperazioneTipoEnum(String codice, TipoOperazioneIva toi){		
		this.codice = codice;
		this.tipoOperazioneIva = toi;
	}
	
	/**
	 * Ottiene l'enum a partire dal tipo di operazione iva.
	 *
	 * @param toi il tipo da cui ricavare l'enum
	 * @return l'enum corrispondente al tipo, se esistente
	 */
	public static SiacDIvaOperazioneTipoEnum byTipoOperazioneIva(TipoOperazioneIva toi){
		for(SiacDIvaOperazioneTipoEnum e : SiacDIvaOperazioneTipoEnum.values()){
			if(e.getTipoOperazioneIva().equals(toi)){
				return e;
			}
		}
		throw new IllegalArgumentException("Tipo operazione Iva " + toi + " non ha un mapping corrispondente in SiacDIvaOperazioneTipoEnum");
	}
	
	/**
	 * Ottiene l'enum a partire dal tipo di operazione iva, anche nel caso in cui essa sia <code>null</code>.
	 *
	 * @param toi il tipo da cui ricavare l'enum
	 * @return l'enum corrispondente al tipo, se esistente
	 * @see SiacDIvaOperazioneTipoEnum#byTipoOperazioneIva(TipoOperazioneIva)
	 */
	public static SiacDIvaOperazioneTipoEnum byTipoOperazioneIvaEvenNull(TipoOperazioneIva toi){
		if(toi == null){
			return null;
		}
		return byTipoOperazioneIva(toi);
	}
	
	/**
	 * Ottiene l'enum a partire dal codice del tipo.
	 *
	 * @param codice il codice da cui ricavare l'enum
	 * @return l'enum corrispondente al tipo, se esistente
	 */
	public static SiacDIvaOperazioneTipoEnum byCodice(String codice){
		for(SiacDIvaOperazioneTipoEnum e : SiacDIvaOperazioneTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDIvaOperazioneTipoEnum");
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
	 * Gets the tipo operazione iva.
	 *
	 * @return the tipoOperazioneIva
	 */
	public TipoOperazioneIva getTipoOperazioneIva() {
		return tipoOperazioneIva;
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaProvvisoria;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDSubdocIvaStatoEnum.
 */
@EnumEntity(entityName="SiacDPnProvAccettazioneStato", idPropertyName="pnStaAccProvId", codePropertyName="pnStaAccProvCode")
public enum SiacDPnProvAccettazioneStatoEnum {
		
	
	/** The Definitivo. */
	Definitivo("1", StatoAccettazionePrimaNotaProvvisoria.DEFINITIVO),
	
	/** The Provvisorio. */
	Provvisorio("2", StatoAccettazionePrimaNotaProvvisoria.RIFIUTATO), 
	
	/** The Provvisorio definitivo. */
	Annullato("3", StatoAccettazionePrimaNotaProvvisoria.PROVVISORIO); 
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoAccettazionePrimaNotaProvvisoria statoAccettazioneProv;
	
	/**
	 * Instantiates a new siac d subdoc iva stato enum.
	 *
	 * @param codice the codice
	 * @param statoAccettazioneProv the stato operativo
	 */
	private SiacDPnProvAccettazioneStatoEnum(String codice, StatoAccettazionePrimaNotaProvvisoria statoAccettazioneProv){
		this.codice = codice;
		this.statoAccettazioneProv = statoAccettazioneProv;
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
	public StatoAccettazionePrimaNotaProvvisoria getStatoAccettazioneProv() {
		return statoAccettazioneProv;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d subdoc iva stato enum
	 */
	public static SiacDPnProvAccettazioneStatoEnum byCodice(String codice){
		for(SiacDPnProvAccettazioneStatoEnum e : SiacDPnProvAccettazioneStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDPrimaNotaStatoEnum");
	}
	
	/**
	 * By codice even null.
	 *
	 * @param codice the codice
	 * @return the siac d prima nota stato enum
	 */
	public static SiacDPnProvAccettazioneStatoEnum byCodiceEvenNull(String codice){
		if(codice == null){
			return null;
		}
		return byCodice(codice);
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d prima nota stato enum
	 */
	public static SiacDPnProvAccettazioneStatoEnum byStatoAccettazioneProv(StatoAccettazionePrimaNotaProvvisoria stato){
		for(SiacDPnProvAccettazioneStatoEnum e : SiacDPnProvAccettazioneStatoEnum.values()){
			if(e.getStatoAccettazioneProv().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ stato + " non ha un mapping corrispondente in SiacDPrimaNotaStatoEnum");
	}
	

}

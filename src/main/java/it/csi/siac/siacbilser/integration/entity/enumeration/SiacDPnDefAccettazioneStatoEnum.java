/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.model.DecodificaEnum;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;

/**
 * The Enum SiacDSubdocIvaStatoEnum.
 */
@EnumEntity(entityName="SiacDPnDefAccettazioneStato", idPropertyName="pnStaAccDefId", codePropertyName="pnStaAccDefCode")
public enum SiacDPnDefAccettazioneStatoEnum implements DecodificaEnum {
		
	
	/** The Definitivo. */
	Integrato("1", StatoAccettazionePrimaNotaDefinitiva.INTEGRATO),
	
	/** The Defvisorio. */
	Rifiutato("2", StatoAccettazionePrimaNotaDefinitiva.RIFIUTATO), 
	
	/** The Defvisorio definitivo. */
	DaAccettare("3", StatoAccettazionePrimaNotaDefinitiva.DA_ACCETTARE); 
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoAccettazionePrimaNotaDefinitiva statoAccettazioneDef;
	
	/**
	 * Instantiates a new siac d subdoc iva stato enum.
	 *
	 * @param codice the codice
	 * @param statoAccettazioneDef the stato operativo
	 */
	private SiacDPnDefAccettazioneStatoEnum(String codice, StatoAccettazionePrimaNotaDefinitiva statoAccettazioneDef){
		this.codice = codice;
		this.statoAccettazioneDef = statoAccettazioneDef;
	}
	
	@Override
	public String getCodice() {
		return codice;
	}

	/**
	 * Gets the stato operativo.
	 *
	 * @return the stato operativo
	 */
	public StatoAccettazionePrimaNotaDefinitiva getStatoAccettazioneDef() {
		return statoAccettazioneDef;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d subdoc iva stato enum
	 */
	public static SiacDPnDefAccettazioneStatoEnum byCodice(String codice){
		for(SiacDPnDefAccettazioneStatoEnum e : SiacDPnDefAccettazioneStatoEnum.values()){
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
	public static SiacDPnDefAccettazioneStatoEnum byCodiceEvenNull(String codice){
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
	public static SiacDPnDefAccettazioneStatoEnum byStatoAccettazioneDef(StatoAccettazionePrimaNotaDefinitiva stato){
		for(SiacDPnDefAccettazioneStatoEnum e : SiacDPnDefAccettazioneStatoEnum.values()){
			if(e.getStatoAccettazioneDef().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ stato + " non ha un mapping corrispondente in SiacDPrimaNotaStatoEnum");
	}
	
	/**
	 * By stato operativo even null.
	 *
	 * @param stato the stato
	 * @return the siac d prima nota stato enum
	 */
	public static SiacDPnDefAccettazioneStatoEnum byStatoAccettazioneDefEvenNull(StatoAccettazionePrimaNotaDefinitiva stato){
		if(stato == null) {
			return null;
		}
		return byStatoAccettazioneDef(stato);
	}
	
	/**
	 * By stato operativo even null.
	 *
	 * @param statiAccettazioneDefinitivo the stato
	 * @return the siac d prima nota stato enum
	 */
	public static List<SiacDPnDefAccettazioneStatoEnum> byStatiAccettazioneDef(Iterable<StatoAccettazionePrimaNotaDefinitiva> statiAccettazioneDefinitivo){
		List<SiacDPnDefAccettazioneStatoEnum> res = new ArrayList<SiacDPnDefAccettazioneStatoEnum>();
		if(statiAccettazioneDefinitivo == null) {
			return res;
		}
		for(StatoAccettazionePrimaNotaDefinitiva sapnd : statiAccettazioneDefinitivo) {
			SiacDPnDefAccettazioneStatoEnum sdpdase = byStatoAccettazioneDefEvenNull(sapnd);
			if(sdpdase != null) {
				res.add(sdpdase);
			}
		}
		return res;
	}
	

}

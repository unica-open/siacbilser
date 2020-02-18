/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.model.DecodificaEnum;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDSubdocIvaStatoEnum.
 */
@EnumEntity(entityName="SiacDPrimaNotaStato", idPropertyName="pnotaStatoId", codePropertyName="pnotaStatoCode")
public enum SiacDPrimaNotaStatoEnum implements DecodificaEnum {
		
	/** The Definitivo. */
	Definitivo("D", StatoOperativoPrimaNota.DEFINITIVO),
	
	/** The Provvisorio. */
	Provvisorio("P", StatoOperativoPrimaNota.PROVVISORIO), 
	
	/** The Provvisorio definitivo. */
	Annullato("A", StatoOperativoPrimaNota.ANNULLATO); 
	
	public static final String codiceAnnullato = "A";
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoOperativoPrimaNota statoOperativo;
	
	/**
	 * Instantiates a new siac d subdoc iva stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDPrimaNotaStatoEnum(String codice, StatoOperativoPrimaNota statoOperativo){
		this.codice = codice;
		this.statoOperativo = statoOperativo;
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
	public StatoOperativoPrimaNota getStatoOperativo() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d subdoc iva stato enum
	 */
	public static SiacDPrimaNotaStatoEnum byCodice(String codice){
		for(SiacDPrimaNotaStatoEnum e : SiacDPrimaNotaStatoEnum.values()){
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
	public static SiacDPrimaNotaStatoEnum byCodiceEvenNull(String codice){
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
	public static SiacDPrimaNotaStatoEnum byStatoOperativo(StatoOperativoPrimaNota stato){
		for(SiacDPrimaNotaStatoEnum e : SiacDPrimaNotaStatoEnum.values()){
			if(e.getStatoOperativo().equals(stato)){
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
	public static SiacDPrimaNotaStatoEnum byStatoOperativoEvenNull(StatoOperativoPrimaNota stato){
		if(stato == null) {
			return null;
		}
		return byStatoOperativo(stato);
	}

	/**
	 * By stato operativo even null.
	 *
	 * @param stato the stato
	 * @return the siac d prima nota stato enum
	 */
	public static List<SiacDPrimaNotaStatoEnum> byStatiOperativi(Iterable<StatoOperativoPrimaNota> stato){
		List<SiacDPrimaNotaStatoEnum> res = new ArrayList<SiacDPrimaNotaStatoEnum>();
		if(stato == null) {
			return res;
		}
		for(StatoOperativoPrimaNota sopn : stato) {
			SiacDPrimaNotaStatoEnum sdpnse = byStatoOperativoEvenNull(sopn);
			if(sdpnse != null) {
				res.add(sdpnse);
			}
		}
		return res;
	}
}

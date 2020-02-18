/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDRegMovFinStatoEnum.
 */
@EnumEntity(entityName="SiacDRegMovfinStato", idPropertyName="regmovfinStatoId", codePropertyName="regmovfinStatoCode")
public enum SiacDRegMovFinStatoEnum {
		
	Notificato("N", StatoOperativoRegistrazioneMovFin.NOTIFICATO),
	
	Elaborato("E", StatoOperativoRegistrazioneMovFin.ELABORATO), 
	
	Annullato("A", StatoOperativoRegistrazioneMovFin.ANNULLATO), 
	
	Registrato("R", StatoOperativoRegistrazioneMovFin.REGISTRATO), 
	
	Contabilizzato("C", StatoOperativoRegistrazioneMovFin.CONTABILIZZATO); 
	

	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoOperativoRegistrazioneMovFin statoOperativo;
	
	/**
	 * Instantiates a new siac d subdoc iva stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDRegMovFinStatoEnum(String codice, StatoOperativoRegistrazioneMovFin statoOperativo){
		this.codice = codice;
		this.statoOperativo = statoOperativo;
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
	public StatoOperativoRegistrazioneMovFin getStatoOperativo() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the SiacDRegMovFinStatoEnum
	 */
	public static SiacDRegMovFinStatoEnum byCodice(String codice){
		for(SiacDRegMovFinStatoEnum e : SiacDRegMovFinStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDRegMovFinStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the SiacDRegMovFinStatoEnum
	 */
	public static SiacDRegMovFinStatoEnum byStatoOperativo(StatoOperativoRegistrazioneMovFin stato){
		for(SiacDRegMovFinStatoEnum e : SiacDRegMovFinStatoEnum.values()){
			if(e.getStatoOperativo().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ stato + " non ha un mapping corrispondente in SiacDRegMovFinStatoEnum");
	}
	
	/**
	 * By stato operativo even null.
	 *
	 * @param stato the stato
	 * @return the siac d reg mov fin stato enum
	 */
	public static SiacDRegMovFinStatoEnum byStatoOperativoEvenNull(StatoOperativoRegistrazioneMovFin stato){
		if(stato == null){
			return null;
		}
		return byStatoOperativo(stato);
	}
	
	
	
	
	/**
	 * Gets the codici.
	 *
	 * @param regMovFinStatos the bil elems tipo
	 * @return the codici
	 */
	public static Set<String> getCodici(Set<SiacDRegMovFinStatoEnum> regMovFinStatos){
		Set<String> codici = new HashSet<String>();
		for (SiacDRegMovFinStatoEnum d : regMovFinStatos) {
			codici.add(d.getCodice());
		}
		return codici;
	}
	
	
	/**
	 * Gets the codici single quote.
	 *
	 * @param regMovFinStatos the bil elems tipo
	 * @return the codici single quote
	 */
	public static Set<String> getCodiciSingleQuote(Set<SiacDRegMovFinStatoEnum> regMovFinStatos){
		Set<String> codici = new HashSet<String>();
		for (SiacDRegMovFinStatoEnum d : regMovFinStatos) {
			codici.add(d.getCodiceSingleQuote());
		}
		return codici;
	}
	
	/**
	 * Gets the codice single quote.
	 *
	 * @return the codice single quote
	 */
	public String getCodiceSingleQuote() {
		return "'"+getCodice()+"'";
	}

	/**
	 * Gets the codici single quote comma separated.
	 *
	 * @param regMovFinStatos the bil elems tipo
	 * @return the codici single quote comma separated
	 */
	public static String getCodiciSingleQuoteCommaSeparated(Set<SiacDRegMovFinStatoEnum> regMovFinStatos){
		return StringUtils.join(getCodiciSingleQuote(regMovFinStatos),",");
	}
	
	
	/**
	 * By stato operativo even null.
	 *
	 * @param stati the stati
	 * @return the sets the
	 */
	public static Set<SiacDRegMovFinStatoEnum> byStatoOperativoEvenNull(List<StatoOperativoRegistrazioneMovFin> stati) {
		Set<SiacDRegMovFinStatoEnum> set = EnumSet.noneOf(SiacDRegMovFinStatoEnum.class);
		if(stati!=null){
			for(StatoOperativoRegistrazioneMovFin stato : stati){
				SiacDRegMovFinStatoEnum siacDRegMovFinStatoEnumDaEscludere =  SiacDRegMovFinStatoEnum.byStatoOperativoEvenNull(stato);
				set.add(siacDRegMovFinStatoEnumDaEscludere);
			}
		}
		return set;
	}

}

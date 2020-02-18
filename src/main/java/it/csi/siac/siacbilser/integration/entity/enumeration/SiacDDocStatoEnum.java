/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;

/**
 * The Enum SiacDDocStatoEnum.
 */
@EnumEntity(entityName="SiacDDocStato", idPropertyName="docStatoId", codePropertyName="docStatoCode")
public enum SiacDDocStatoEnum {
		
	Incompleto("I", StatoOperativoDocumento.INCOMPLETO),
	Valido("V", StatoOperativoDocumento.VALIDO), 
	Liquidato("L", StatoOperativoDocumento.LIQUIDATO), 
	ParzialmenteLiquidato("PL", StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO), 
	ParzialmenteEmesso("PE", StatoOperativoDocumento.PARZIALMENTE_EMESSO), 
	Emesso("EM", StatoOperativoDocumento.EMESSO), 
	Annullato("A", StatoOperativoDocumento.ANNULLATO), 
	Stornato("ST", StatoOperativoDocumento.STORNATO);
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoOperativoDocumento statoOperativo;
	
	public static final String CodiceAnnullato = "A";
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDDocStatoEnum(String codice, StatoOperativoDocumento statoOperativo){
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
	public StatoOperativoDocumento getStatoOperativo() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDDocStatoEnum byCodice(String codice){
		for(SiacDDocStatoEnum e : SiacDDocStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDDocStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d doc stato enum
	 */
	public static SiacDDocStatoEnum byStatoOperativo(StatoOperativoDocumento stato){
		for(SiacDDocStatoEnum e : SiacDDocStatoEnum.values()){
			if(e.getStatoOperativo().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ stato + " non ha un mapping corrispondente in SiacDDocStatoEnum");
	}
	
	public static Set<SiacDDocStatoEnum> byStatiOperativi(Collection<StatoOperativoDocumento> stati) {
		return byStatiOperativi(stati!=null && !stati.isEmpty()?EnumSet.copyOf(stati):null);
	}
	
	public static Set<SiacDDocStatoEnum> byStatiOperativi(Set<StatoOperativoDocumento> stati) {
		Set<SiacDDocStatoEnum> enums = EnumSet.noneOf(SiacDDocStatoEnum.class);
		if(stati!=null){
			for(StatoOperativoDocumento stato: stati){
				enums.add(byStatoOperativo(stato));
			}
		}
		
		//return enums.toArray(new SiacDDocStatoEnum[enums.size()]);
		//return new ArrayList<SiacDDocStatoEnum>(enums);
		return enums;
	}
	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;

/**
 * The Enum SiacDDocStatoEnum.
 */
@EnumEntity(entityName="SiacDRichiestaEconStato", idPropertyName="riceconStatoId", codePropertyName="riceconStatoCode")
public enum SiacDRichiestaEconStatoEnum {
	
		
	Prenotata("PR", StatoOperativoRichiestaEconomale.PRENOTATA),
	Evasa("EV", StatoOperativoRichiestaEconomale.EVASA), 
	DaRendicontare("DR", StatoOperativoRichiestaEconomale.DA_RENDICONTARE), 
	Rendicontata("RE", StatoOperativoRichiestaEconomale.RENDICONTATA),
	AgliAtti("AA", StatoOperativoRichiestaEconomale.AGLI_ATTI), 
	Annullata("AN", StatoOperativoRichiestaEconomale.ANNULLATA);
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoOperativoRichiestaEconomale statoOperativo;
	
	/**
	 * Instantiates a new siac d richiesta econ stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDRichiestaEconStatoEnum(String codice, StatoOperativoRichiestaEconomale statoOperativo){
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
	public StatoOperativoRichiestaEconomale getStatoOperativo() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d richiesta econ stato enum
	 */
	public static SiacDRichiestaEconStatoEnum byCodice(String codice){
		for(SiacDRichiestaEconStatoEnum e : SiacDRichiestaEconStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDRichiestaEconStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d richiesta econ stato enum
	 */
	public static SiacDRichiestaEconStatoEnum byStatoOperativo(StatoOperativoRichiestaEconomale stato){
		for(SiacDRichiestaEconStatoEnum e : SiacDRichiestaEconStatoEnum.values()){
			if(e.getStatoOperativo().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ stato + " non ha un mapping corrispondente in SiacDRichiestaEconStatoEnum");
	}
	
	public static Set<SiacDRichiestaEconStatoEnum> byStatiOperativi(List<StatoOperativoRichiestaEconomale> stati) {
		return byStatiOperativi(stati!=null && !stati.isEmpty()?EnumSet.copyOf(stati):null);
	}
	
	public static Set<SiacDRichiestaEconStatoEnum> byStatiOperativi(Set<StatoOperativoRichiestaEconomale> stati) {
		Set<SiacDRichiestaEconStatoEnum> enums = EnumSet.noneOf(SiacDRichiestaEconStatoEnum.class);
		if(stati!=null){
			for(StatoOperativoRichiestaEconomale stato: stati){
				enums.add(byStatoOperativo(stato));
			}
		}
		
		//return enums.toArray(new SiacDDocStatoEnum[enums.size()]);
		//return new ArrayList<SiacDDocStatoEnum>(enums);
		return enums;
	}
	

}

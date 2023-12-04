/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import it.csi.siac.siacattser.model.StatoOperativoAtti;


/**
 * select replace(initcap(attoamm_stato_desc),' ','') || '("' || attoamm_stato_code || '"),' from siac_d_atto_amm_stato where ente_proprietario_id = 1
 */
@EnumEntity(entityName="SiacDAttoAmmStato", idPropertyName="attoammStatoId", codePropertyName="attoammStatoCode")
public enum SiacDAttoAmmStatoEnum {
	
	ANNULLATO("ANNULLATO", StatoOperativoAtti.ANNULLATO),
	DEFINITIVO("DEFINITIVO", StatoOperativoAtti.DEFINITIVO),
	PROVVISORIO("PROVVISORIO", StatoOperativoAtti.PROVVISORIO),
	;

	/** The codice. */
	private final String codice;
	
	/** The descrizione. */
	private final StatoOperativoAtti statoOperativoAtti;

	
	SiacDAttoAmmStatoEnum(String codice, StatoOperativoAtti statoOperativoAtti){
		this.codice = codice;
		this.statoOperativoAtti = statoOperativoAtti;
	}
	
	/**
	 * @param codice
	 * @return
	 */
	public static SiacDAttoAmmStatoEnum byCodiceEvenNull(String codice){
		if(codice==null){
			return null;
		}
		return byCodice(codice);
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d atto amm tipo enum
	 */
	public static SiacDAttoAmmStatoEnum byCodice(String codice){
		for(SiacDAttoAmmStatoEnum e : SiacDAttoAmmStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDAttoAmmStatoEnum");
	}
	
	public static SiacDAttoAmmStatoEnum byStatoOperativo(StatoOperativoAtti statoOperativo){
		for(SiacDAttoAmmStatoEnum e : SiacDAttoAmmStatoEnum.values()){
			if(e.getStatoOperativoAtti().equals(statoOperativo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operavivo "+ statoOperativo + " non ha un mapping corrispondente in SiacDAttoAmmStatoEnum");
	}
	
	public static Set<SiacDAttoAmmStatoEnum> byStatiOperativi(Collection<StatoOperativoAtti> stati) {
		return byStatiOperativi(stati!=null && !stati.isEmpty()?EnumSet.copyOf(stati):null);
	}
	
	public static Set<SiacDAttoAmmStatoEnum> byStatiOperativi(Set<StatoOperativoAtti> stati) {
		Set<SiacDAttoAmmStatoEnum> enums = EnumSet.noneOf(SiacDAttoAmmStatoEnum.class);
		if(stati!=null){
			for(StatoOperativoAtti stato: stati){
				enums.add(byStatoOperativo(stato));
			}
		}
		
		//return enums.toArray(new SiacDDocStatoEnum[enums.size()]);
		//return new ArrayList<SiacDDocStatoEnum>(enums);
		return enums;
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
	 * Gets the descrizione.
	 *
	 * @return the descrizione
	 */
	public StatoOperativoAtti getStatoOperativoAtti() {
		return statoOperativoAtti;
	}
	
}
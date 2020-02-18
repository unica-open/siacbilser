/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import it.csi.siac.siaccespser.model.StatoVariazioneCespite;

/**
 * The Enum SiacDCespitiVariazioneStatoEnum.
 */
@EnumEntity(entityName="SiacDCespitiVariazioneStato", idPropertyName="cesVarStatoId", codePropertyName="cesVarStatoCode")
public enum SiacDCespitiVariazioneStatoEnum {
	
	Provvisorio("P", StatoVariazioneCespite.PROVVISORIO),
	Definitivo("D", StatoVariazioneCespite.DEFINITIVO),
	Annullato("A", StatoVariazioneCespite.ANNULLATO),
	;
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoVariazioneCespite statoVariazioneCespite;
	
	/**
	 * Instantiates a new siac d cespiti variazione stato enum.
	 *
	 * @param codice the codice
	 * @param statoVariazioneCespite the stato variazione cespite
	 */
	private SiacDCespitiVariazioneStatoEnum(String codice, StatoVariazioneCespite statoVariazioneCespite){
		this.codice = codice;
		this.statoVariazioneCespite = statoVariazioneCespite;
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
	 * @return the statoVariazioneCespite
	 */
	public StatoVariazioneCespite getStatoVariazioneCespite() {
		return this.statoVariazioneCespite;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDCespitiVariazioneStatoEnum byCodice(String codice){
		for(SiacDCespitiVariazioneStatoEnum e : SiacDCespitiVariazioneStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice " + codice + " non ha un mapping corrispondente in SiacDCespitiVariazioneStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d doc stato enum
	 */
	public static SiacDCespitiVariazioneStatoEnum byStatoVariazioneCespite(StatoVariazioneCespite stato){
		for(SiacDCespitiVariazioneStatoEnum e : SiacDCespitiVariazioneStatoEnum.values()){
			if(e.getStatoVariazioneCespite().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato variazione cespite " + stato + " non ha un mapping corrispondente in SiacDCespitiVariazioneStatoEnum");
	}
	
	public static Set<SiacDCespitiVariazioneStatoEnum> byStatiOperativi(Collection<StatoVariazioneCespite> stati) {
		return byStatiOperativi(stati != null && !stati.isEmpty() ? EnumSet.copyOf(stati) : null);
	}
	
	public static Set<SiacDCespitiVariazioneStatoEnum> byStatiOperativi(Set<StatoVariazioneCespite> stati) {
		Set<SiacDCespitiVariazioneStatoEnum> enums = EnumSet.noneOf(SiacDCespitiVariazioneStatoEnum.class);
		if(stati!=null){
			for(StatoVariazioneCespite stato : stati){
				enums.add(byStatoVariazioneCespite(stato));
			}
		}
		
		return enums;
	}

}

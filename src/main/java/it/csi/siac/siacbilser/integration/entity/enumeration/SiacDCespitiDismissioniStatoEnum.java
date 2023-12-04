/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import it.csi.siac.siaccespser.model.StatoDismissioneCespite;

/**
 * The Enum SiacDCespitiVariazioneStatoEnum.
 */
@EnumEntity(entityName="SiacDCespitiDismissioniStato", idPropertyName="cesDismissioniStatoId", codePropertyName="cesDismissioniStatoCode")
public enum SiacDCespitiDismissioniStatoEnum {
	
	Provvisorio("P", StatoDismissioneCespite.PROVVISORIO),
	Definitivo("D", StatoDismissioneCespite.DEFINITIVO),
	Non_Definito("N.D.", StatoDismissioneCespite.NON_DEFINITO),
	;
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoDismissioneCespite statoVariazioneCespite;
	
	/**
	 * Instantiates a new siac d cespiti variazione stato enum.
	 *
	 * @param codice the codice
	 * @param statoVariazioneCespite the stato variazione cespite
	 */
	private SiacDCespitiDismissioniStatoEnum(String codice, StatoDismissioneCespite statoVariazioneCespite){
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
	public StatoDismissioneCespite getStatoDismissioneCespite() {
		return this.statoVariazioneCespite;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDCespitiDismissioniStatoEnum byCodice(String codice){
		for(SiacDCespitiDismissioniStatoEnum e : SiacDCespitiDismissioniStatoEnum.values()){
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
	public static SiacDCespitiDismissioniStatoEnum byStatoDismissioneCespite(StatoDismissioneCespite stato){
		for(SiacDCespitiDismissioniStatoEnum e : SiacDCespitiDismissioniStatoEnum.values()){
			if(e.getStatoDismissioneCespite().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato variazione cespite " + stato + " non ha un mapping corrispondente in SiacDCespitiVariazioneStatoEnum");
	}
	
	public static Set<SiacDCespitiDismissioniStatoEnum> byStatiOperativi(Collection<StatoDismissioneCespite> stati) {
		return byStatiOperativi(stati != null && !stati.isEmpty() ? EnumSet.copyOf(stati) : null);
	}
	
	public static Set<SiacDCespitiDismissioniStatoEnum> byStatiOperativi(Set<StatoDismissioneCespite> stati) {
		Set<SiacDCespitiDismissioniStatoEnum> enums = EnumSet.noneOf(SiacDCespitiDismissioniStatoEnum.class);
		if(stati!=null){
			for(StatoDismissioneCespite stato : stati){
				enums.add(byStatoDismissioneCespite(stato));
			}
		}
		
		return enums;
	}

}

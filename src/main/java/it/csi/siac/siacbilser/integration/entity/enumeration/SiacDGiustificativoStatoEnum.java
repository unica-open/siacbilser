/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import it.csi.siac.siaccecser.model.StatoOperativoGiustificativi;

/**
 * The Enum SiacDGiustificativoStatoEnum.
 */
@EnumEntity(entityName="SiacDGiustificativoStato", idPropertyName="giustStatoId", codePropertyName="giustStatoCode")
public enum SiacDGiustificativoStatoEnum {
	
		
	Annullato("ANNULLATO", StatoOperativoGiustificativi.ANNULLATO),
	EsclusoAlPagamento("ESCLUSO", StatoOperativoGiustificativi.ESCLUSO_AL_PAGAMENTO), 
	Valido("VALIDO", StatoOperativoGiustificativi.VALIDO), 
	;
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final StatoOperativoGiustificativi statoOperativo;
	
	/**
	 * Instantiates a new siac d richiesta econ stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDGiustificativoStatoEnum(String codice, StatoOperativoGiustificativi statoOperativo){
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
	public StatoOperativoGiustificativi getStatoOperativo() {
		return statoOperativo;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d richiesta econ stato enum
	 */
	public static SiacDGiustificativoStatoEnum byCodice(String codice){
		for(SiacDGiustificativoStatoEnum e : SiacDGiustificativoStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDGiustificativoStatoEnum");
	}
	
	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d richiesta econ stato enum
	 */
	public static SiacDGiustificativoStatoEnum byStatoOperativo(StatoOperativoGiustificativi stato){
		for(SiacDGiustificativoStatoEnum e : SiacDGiustificativoStatoEnum.values()){
			if(e.getStatoOperativo().equals(stato)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ stato + " non ha un mapping corrispondente in SiacDGiustificativoStatoEnum");
	}
	
	public static Set<SiacDGiustificativoStatoEnum> byStatiOperativi(List<StatoOperativoGiustificativi> stati) {
		return byStatiOperativi(stati!=null && !stati.isEmpty()?EnumSet.copyOf(stati):null);
	}
	
	public static Set<SiacDGiustificativoStatoEnum> byStatiOperativi(Set<StatoOperativoGiustificativi> stati) {
		Set<SiacDGiustificativoStatoEnum> enums = EnumSet.noneOf(SiacDGiustificativoStatoEnum.class);
		if(stati!=null){
			for(StatoOperativoGiustificativi stato: stati){
				enums.add(byStatoOperativo(stato));
			}
		}
		
		//return enums.toArray(new SiacDGiustificativoStatoEnum[enums.size()]);
		//return new ArrayList<SiacDGiustificativoStatoEnum>(enums);
		return enums;
	}
	

}

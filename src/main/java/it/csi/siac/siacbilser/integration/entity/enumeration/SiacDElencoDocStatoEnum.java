/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;


/**
 * select replace(initcap(elem_det_tipo_desc),' ','') || '("' || elem_det_tipo_code || '"),' from Siac_D_Atto_Allegato_Stato.
 */
@EnumEntity(entityName="SiacDElencoDocStato", idPropertyName="eldocStatoId", codePropertyName="eldocStatoCode")
public enum SiacDElencoDocStatoEnum {
	
	BOZZA("B", StatoOperativoElencoDocumenti.BOZZA),
	COMPLETATO("C", StatoOperativoElencoDocumenti.COMPLETATO),
	RIFIUTATO("R", StatoOperativoElencoDocumenti.RIFIUTATO);
	

	/** The codice. */
	private final String codice;
	
	/** The descrizione. */
	private final StatoOperativoElencoDocumenti statoOperativoElencoDocumenti;

	
	SiacDElencoDocStatoEnum(String codice, StatoOperativoElencoDocumenti statoOperativoElencoDocumenti){
		this.codice = codice;
		this.statoOperativoElencoDocumenti = statoOperativoElencoDocumenti;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d atto amm tipo enum
	 */
	public static SiacDElencoDocStatoEnum byCodice(String codice){
		for(SiacDElencoDocStatoEnum e : SiacDElencoDocStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDElencoDocStatoEnum");
	}
	
	public static SiacDElencoDocStatoEnum byStatoOperativo(StatoOperativoElencoDocumenti statoOperativo){
		for(SiacDElencoDocStatoEnum e : SiacDElencoDocStatoEnum.values()){
			if(e.getStatoOperativoElencoDocumenti().equals(statoOperativo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operavivo "+ statoOperativo + " non ha un mapping corrispondente in SiacDElencoDocStatoEnum");
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
	public StatoOperativoElencoDocumenti getStatoOperativoElencoDocumenti() {
		return statoOperativoElencoDocumenti;
	}
	
	public static Set<SiacDElencoDocStatoEnum> byStatiOperativiElencoDocumenti(List<StatoOperativoElencoDocumenti> stati) {
		return byStatiOperativi(stati!=null && !stati.isEmpty()?EnumSet.copyOf(stati):null);
	}
	
	public static Set<SiacDElencoDocStatoEnum> byStatiOperativi(Set<StatoOperativoElencoDocumenti> stati) {
		Set<SiacDElencoDocStatoEnum> enums = EnumSet.noneOf(SiacDElencoDocStatoEnum.class);
		if(stati!=null){
			for(StatoOperativoElencoDocumenti stato: stati){
				enums.add(byStatoOperativo(stato));
			}
		}
		return enums;
	}
	
}
/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;


/**
 * select replace(initcap(elem_det_tipo_desc),' ','') || '("' || elem_det_tipo_code || '"),' from Siac_D_Atto_Allegato_Stato.
 */
@EnumEntity(entityName="SiacDAttoAllegatoStato", idPropertyName="attoalStatoId", codePropertyName="attoalStatoCode")
public enum SiacDAttoAllegatoStatoEnum {
	
	DA_COMPLETARE("D", StatoOperativoAllegatoAtto.DA_COMPLETARE),
	COMPLETATO("C", StatoOperativoAllegatoAtto.COMPLETATO),
	RIFIUTATO("R", StatoOperativoAllegatoAtto.RIFIUTATO),
	PARZIALMENTE_CONVALIDATO("PC", StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO),
	CONVALIDATO("CV", StatoOperativoAllegatoAtto.CONVALIDATO),
	ANNULLATO("A", StatoOperativoAllegatoAtto.ANNULLATO);
	
	
	public static final String CODICE_ANNULLATO = "A"; //ANNULLATO.getCodice();
	public static final String CODICE_RIFIUTATO = "R"; //ANNULLATO.getCodice();
	

	/** The codice. */
	private final String codice;
	
	/** The descrizione. */
	private final StatoOperativoAllegatoAtto statoOperativoAllegatoAtto;

	
	SiacDAttoAllegatoStatoEnum(String codice, StatoOperativoAllegatoAtto statoOperativoAllegatoAtto){
		this.codice = codice;
		this.statoOperativoAllegatoAtto = statoOperativoAllegatoAtto;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d atto amm tipo enum
	 */
	public static SiacDAttoAllegatoStatoEnum byCodice(String codice){
		for(SiacDAttoAllegatoStatoEnum e : SiacDAttoAllegatoStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDAttoAllegatoStatoEnum");
	}
	
	public static SiacDAttoAllegatoStatoEnum byStatoOperativo(StatoOperativoAllegatoAtto statoOperativo){
		for(SiacDAttoAllegatoStatoEnum e : SiacDAttoAllegatoStatoEnum.values()){
			if(e.getStatoOperativoAllegatoAtto().equals(statoOperativo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato operativo "+ statoOperativo + " non ha un mapping corrispondente in SiacDAttoAllegatoStatoEnum");
	}
	
	/**
	 * By lista stati operativi. Ottiene, a partita da una lista di stati operativi dell'allegato att, la corrispondente lista di enum
	 *
	 * @param statoOperativos the stato operativos da convertire
	 * @return the lista, null se gli statioperativos passati in input sono null
	 */
	//SIAC-5584
	public static List<SiacDAttoAllegatoStatoEnum> byListaStatiOperativi(List<StatoOperativoAllegatoAtto> statoOperativos){
		List<SiacDAttoAllegatoStatoEnum> siacDAttoAllegatoStatoEnums = new ArrayList<SiacDAttoAllegatoStatoEnum>();
		if(statoOperativos == null) {
			return siacDAttoAllegatoStatoEnums;
		}
		
		for (StatoOperativoAllegatoAtto statoOperativo : statoOperativos) {
			siacDAttoAllegatoStatoEnums.add(byStatoOperativo(statoOperativo));
		}

		return siacDAttoAllegatoStatoEnums;
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
	public StatoOperativoAllegatoAtto getStatoOperativoAllegatoAtto() {
		return statoOperativoAllegatoAtto;
	}
	
}
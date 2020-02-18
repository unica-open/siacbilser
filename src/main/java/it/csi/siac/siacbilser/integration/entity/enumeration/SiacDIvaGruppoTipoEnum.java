/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacfin2ser.model.TipoAttivita;
import it.csi.siac.siacfin2ser.model.TipoChiusura;


// TODO: Auto-generated Javadoc
/**
 * Enum per il tipo di operazione Iva.
 * 
 * 
 *
 */
@EnumEntity(entityName="SiacDIvaGruppoTipo", idPropertyName="ivagruTipoId", codePropertyName="ivagruTipoCode")
public enum SiacDIvaGruppoTipoEnum {
	
	/** The istituzionale. */
	ISTITUZIONALE("I", TipoAttivita.ISTITUZIONALE),
	
	/** The promiscua. */
	PROMISCUA("P", TipoAttivita.PROMISCUA),
	
	/** The commerciale. */
	COMMERCIALE("C", TipoAttivita.COMMERCIALE);
	
	/** The codice. */
	private final String codice;
	
	/** The tipo attivita. */
	private final TipoAttivita tipoAttivita;
	
	/**
	 * Instantiates a new siac d iva gruppo tipo enum.
	 *
	 * @param codice the codice
	 * @param ta the ta
	 */
	private SiacDIvaGruppoTipoEnum(String codice, TipoAttivita ta){		
		this.codice = codice;
		this.tipoAttivita = ta;
	}
	
	/**
	 * Ottiene l'enum a partire dal tipo attivita iva.
	 *
	 * @param ta il tipo da cui ricavare l'enum
	 * @return l'enum corrispondente al tipo, se esistente
	 */
	public static SiacDIvaGruppoTipoEnum byTipoAttivita(TipoAttivita ta){
		for(SiacDIvaGruppoTipoEnum e : SiacDIvaGruppoTipoEnum.values()){
			if(e.getTipoAttivita().equals(ta)){
				return e;
			}
		}
		throw new IllegalArgumentException("Tipo attivita " + ta + " non ha un mapping corrispondente in SiacDIvaGruppoTipoEnum");
	}
	
	/**
	 * Ottiene l'enum a partire dal tipo di attivita iva, anche nel caso in cui essa sia <code>null</code>.
	 *
	 * @param ta the ta
	 * @return l'enum corrispondente al tipo, se esistente
	 * @see SiacDIvaGruppoTipoEnum#byTipoAttivita(TipoChiusura)
	 */
	public static SiacDIvaGruppoTipoEnum byTipoAttivitaEvenNull(TipoAttivita ta){
		if(ta == null){
			return null;
		}
		return byTipoAttivita(ta);
	}
	
	/**
	 * Ottiene l'enum a partire dal codice del tipo.
	 *
	 * @param codice il codice da cui ricavare l'enum
	 * @return l'enum corrispondente al tipo, se esistente
	 */
	public static SiacDIvaGruppoTipoEnum byCodice(String codice){
		for(SiacDIvaGruppoTipoEnum e : SiacDIvaGruppoTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDIvaGruppoTipoEnum");
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
	 * Gets the tipo attivita.
	 *
	 * @return the tipoOperazioneIva
	 */
	public TipoAttivita getTipoAttivita() {
		return tipoAttivita;
	}
	
}

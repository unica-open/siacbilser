/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.Ambito;

/**
 * The Enum SiacDDocStatoEnum.
 */
@EnumEntity(entityName="SiacDAmbito", idPropertyName="ambitoId", codePropertyName="ambitoCode")
public enum SiacDAmbitoEnum {
		
	AmbitoFin("AMBITO_FIN", Ambito.AMBITO_FIN),
	AmbitoCec("AMBITO_CEC", Ambito.AMBITO_CEC), 
	AmbitoGsa("AMBITO_GSA", Ambito.AMBITO_GSA),
	AmbitoInv("AMBITO_INV", Ambito.AMBITO_INV);
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final Ambito ambito;
	
	
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDAmbitoEnum(String codice, Ambito ambito){
		this.codice = codice;
		this.ambito = ambito;
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
	 * @return the ambito
	 */
	public Ambito getAmbito() {
		return ambito;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDAmbitoEnum byCodice(String codice){
		for(SiacDAmbitoEnum e : SiacDAmbitoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDAmbitoEnum");
	}
	
	

	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d doc stato enum
	 */
	public static SiacDAmbitoEnum byAmbito(Ambito ambito){
		for(SiacDAmbitoEnum e : SiacDAmbitoEnum.values()){
			if(e.getAmbito().equals(ambito)){
				return e;
			}
		}
		throw new IllegalArgumentException("L'ambito "+ ambito + " non ha un mapping corrispondente in SiacDAmbitoEnum");
	}
	
	public static SiacDAmbitoEnum byAmbitoEvenNull(Ambito ambito){
		if(ambito == null){
			return null;
		}
		return byAmbito(ambito);
	}
	
	
	

}

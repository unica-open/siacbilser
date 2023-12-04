/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siaccespser.model.ClassificazioneGiuridicaCespite;

/**
 * The Enum SiacDDocStatoEnum.
 */
@EnumEntity(entityName="SiacDCespitiClassificazioneGiuridica", idPropertyName="cesClassGiuId", codePropertyName="cesClassGiuCode")
public enum SiacDCespitiClassificazioneGiuridicaEnum {
		
	Ccg1("1", ClassificazioneGiuridicaCespite.CES_BENE_DISPONIBILE),
	Ccg2("2", ClassificazioneGiuridicaCespite.CES_BENE_INDISPONIBILE), 
	Ccg3("3", ClassificazioneGiuridicaCespite.CES_BENE_DEMANIALE);
	
	/** The codice. */
	private final String codice;
	
	/** The stato operativo. */
	private final ClassificazioneGiuridicaCespite cgc;
	
	/**
	 * Instantiates a new siac d doc stato enum.
	 *
	 * @param codice the codice
	 * @param statoOperativo the stato operativo
	 */
	private SiacDCespitiClassificazioneGiuridicaEnum(String codice, ClassificazioneGiuridicaCespite cgc){
		this.codice = codice;
		this.cgc = cgc;
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
	public ClassificazioneGiuridicaCespite getClassificazioneGiuridicaCespite() {
		return cgc;
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDCespitiClassificazioneGiuridicaEnum byCodice(String codice){
		for(SiacDCespitiClassificazioneGiuridicaEnum e : SiacDCespitiClassificazioneGiuridicaEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDClassificazioneGiuridicaCespiteEnum");
	}

	/**
	 * By stato operativo.
	 *
	 * @param stato the stato
	 * @return the siac d doc stato enum
	 */
	public static SiacDCespitiClassificazioneGiuridicaEnum byClassificazioneGiuridicaCespite(ClassificazioneGiuridicaCespite cgc){
		for(SiacDCespitiClassificazioneGiuridicaEnum e : SiacDCespitiClassificazioneGiuridicaEnum.values()){
			if(e.getClassificazioneGiuridicaCespite().equals(cgc)){
				return e;
			}
		}
		throw new IllegalArgumentException("La classificazione giuridica "+ cgc + " non ha un mapping corrispondente in SiacDClassificazioneGiuridicaCespiteEnum");
	}
	
	public static SiacDCespitiClassificazioneGiuridicaEnum byClassificazioneGiuridicaCespiteEvenNull(ClassificazioneGiuridicaCespite cgc){
		if(cgc == null){
			return null;
		}
		return byClassificazioneGiuridicaCespite(cgc);
	}

}

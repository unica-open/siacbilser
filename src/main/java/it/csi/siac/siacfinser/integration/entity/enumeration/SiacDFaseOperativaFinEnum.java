/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.enumeration;

import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siacfinser.integration.entity.converter.EnumEntityFin;



@EnumEntityFin(entityName="SiacDFaseOperativaFin", idPropertyName="faseOperativaId", codePropertyName="faseOperativaCode")
public enum SiacDFaseOperativaFinEnum {
	//Pluriennale("L"),
    //	FaseOperativaTest("FOP", FaseBilancio.PREVISIONE), // La considero di previsione
	
	Previsione("P", FaseBilancio.PREVISIONE),
	EsercizioProvvisorio("E", FaseBilancio.ESERCIZIO_PROVVISORIO),
	Gestione("G", FaseBilancio.GESTIONE),
	Assestamento("A", FaseBilancio.ASSESTAMENTO),
	Consuntivo("O", FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO),
	Chiuso("C", FaseBilancio.CHIUSO);

	
	private String codice;
	private FaseBilancio faseBilancio;
	
	/**
	 * @param codice
	 */
	SiacDFaseOperativaFinEnum(String codice, FaseBilancio faseBilancio){
		this.codice = codice;
		this.faseBilancio = faseBilancio;
	}
	
	public String getCodice() {
		return codice;
	}
	
	public FaseBilancio getFaseBilancio() {
		return faseBilancio;
	}

	public static SiacDFaseOperativaFinEnum byCodice(String codice){
		for(SiacDFaseOperativaFinEnum e : SiacDFaseOperativaFinEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDFaseOperativaFinEnum");
	}
	
	public static SiacDFaseOperativaFinEnum byFaseBilancio(FaseBilancio faseBilancio){
		for(SiacDFaseOperativaFinEnum e : SiacDFaseOperativaFinEnum.values()){
			if(e.getFaseBilancio().equals(faseBilancio)){
				return e;
			}
		}
		throw new IllegalArgumentException("La fase di bilancio "+ faseBilancio + " non ha un mapping corrispondente in SiacDFaseOperativaFinEnum");
	}
	
}
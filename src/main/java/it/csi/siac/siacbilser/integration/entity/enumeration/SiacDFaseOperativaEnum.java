/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siaccorser.model.FaseBilancio;

/**
 * The Enum SiacDFaseOperativaEnum.
 */
@EnumEntity(entityName="SiacDFaseOperativa", idPropertyName="faseOperativaId", codePropertyName="faseOperativaCode")
public enum SiacDFaseOperativaEnum {
	
	 // La considero di previsione
    Previsione("P", FaseBilancio.PREVISIONE),
	EsercizioProvvisorio("E", FaseBilancio.ESERCIZIO_PROVVISORIO),
	Gestione("G", FaseBilancio.GESTIONE),
	Assestamento("A", FaseBilancio.ASSESTAMENTO),
	Consuntivo("O", FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO),
	Chiuso("C", FaseBilancio.CHIUSO),
	FaseOperativaTest("FOP", FaseBilancio.PREVISIONE);

	
	/** The codice. */
	private final String codice;
	
	/** The fase bilancio. */
	private final FaseBilancio faseBilancio;
	
	/**
	 * Instantiates a new siac d fase operativa enum.
	 *
	 * @param codice the codice
	 * @param faseBilancio the fase bilancio
	 */
	SiacDFaseOperativaEnum(String codice, FaseBilancio faseBilancio){
		this.codice = codice;
		this.faseBilancio = faseBilancio;
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
	 * Gets the fase bilancio.
	 *
	 * @return the fase bilancio
	 */
	public FaseBilancio getFaseBilancio() {
		return faseBilancio;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d fase operativa enum
	 */
	public static SiacDFaseOperativaEnum byCodice(String codice){
		for(SiacDFaseOperativaEnum e : SiacDFaseOperativaEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDFaseOperativaEnum");
	}
	
	/**
	 * By fase bilancio.
	 *
	 * @param faseBilancio the fase bilancio
	 * @return the siac d fase operativa enum
	 */
	public static SiacDFaseOperativaEnum byFaseBilancio(FaseBilancio faseBilancio){
		for(SiacDFaseOperativaEnum e : SiacDFaseOperativaEnum.values()){
			if(e.getFaseBilancio().equals(faseBilancio)){
				return e;
			}
		}
		throw new IllegalArgumentException("La fase di bilancio "+ faseBilancio + " non ha un mapping corrispondente in SiacDFaseOperativaEnum");
	}
	
}
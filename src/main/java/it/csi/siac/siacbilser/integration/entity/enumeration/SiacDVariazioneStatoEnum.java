/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacfin2ser.model.DecodificaEnum;

/**
 * The Enum SiacDVariazioneStatoEnum.
 */
@EnumEntity(entityName="SiacDVariazioneStato", idPropertyName="variazioneStatoTipoId", codePropertyName="variazioneStatoTipoCode")
public enum SiacDVariazioneStatoEnum implements DecodificaEnum {
	
	/** The Bozza. */
	PreBozza(SiacDVariazioneStatoEnum.CODICE_PRE_BOZZA, StatoOperativoVariazioneBilancio.PRE_BOZZA),
	/** The Bozza. */
	Bozza(SiacDVariazioneStatoEnum.CODICE_BOZZA, StatoOperativoVariazioneBilancio.BOZZA),
	
	/** The Definitiva. */
	Definitiva(SiacDVariazioneStatoEnum.CODICE_DEFINITIVA, StatoOperativoVariazioneBilancio.DEFINITIVA),
	
	/** The Annullata. */
	Annullata(SiacDVariazioneStatoEnum.CODICE_ANNULLATA, StatoOperativoVariazioneBilancio.ANNULLATA),
	
	/** The Giunta. */
	Giunta(SiacDVariazioneStatoEnum.CODICE_GIUNTA, StatoOperativoVariazioneBilancio.GIUNTA),
	
	/** The Consiglio. */
	Consiglio(SiacDVariazioneStatoEnum.CODICE_CONSIGLIO, StatoOperativoVariazioneBilancio.CONSIGLIO),
	
	/** The Pre Definitiva. */
	PreDefinitiva(SiacDVariazioneStatoEnum.CODICE_PRE_DEFINITIVA, StatoOperativoVariazioneBilancio.PRE_DEFINITIVA),
	;	
	
	
	private final String codice;
	private final StatoOperativoVariazioneBilancio statoOperativoVariazioneBilancio;
	//SIAC-8332
	public static final String CODICE_PRE_BOZZA = "BD";
	public static final String CODICE_BOZZA = "B";
	public static final String CODICE_DEFINITIVA = "D";
	public static final String CODICE_ANNULLATA = "A";
	public static final String CODICE_GIUNTA = "G";
	public static final String CODICE_CONSIGLIO = "C";
	public static final String CODICE_PRE_DEFINITIVA = "P";
	
	/**
	 * Instantiates a new siac d variazione stato enum.
	 *
	 * @param codice the codice
	 * @param soeb the soeb
	 */
	SiacDVariazioneStatoEnum(String codice, StatoOperativoVariazioneBilancio soeb){
		this.codice = codice;
		this.statoOperativoVariazioneBilancio = soeb;
	}
	
	/**
	 * By stato operativo variazione di bilancio.
	 *
	 * @param soeb the soeb
	 * @return the siac d variazione stato enum
	 */
	public static SiacDVariazioneStatoEnum byStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneBilancio soeb){
		for(SiacDVariazioneStatoEnum e : SiacDVariazioneStatoEnum.values()){
			if(e.getStatoOperativoVariazioneDiBilancio().equals(soeb)){
				return e;
			}
		}
		throw new IllegalArgumentException("Stato operativo "+ soeb + " non ha un mapping corrispondente in SiacDVariazioneStatoEnum");
	}
	
	/**
	 * By stato operativo variazione di bilancio even null.
	 *
	 * @param soeb the soeb
	 * @return the siac d variazione stato enum
	 */
	public static SiacDVariazioneStatoEnum byStatoOperativoVariazioneDiBilancioEvenNull(StatoOperativoVariazioneBilancio soeb){
		if(soeb==null){
			return null;
		}		
		return byStatoOperativoVariazioneDiBilancio(soeb);
		
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d variazione stato enum
	 */
	public static SiacDVariazioneStatoEnum byCodice(String codice){
		for(SiacDVariazioneStatoEnum e : SiacDVariazioneStatoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDVariazioneStatoEnum");
	}
	
	@Override
	public String getCodice() {
		return codice;
	}

	/**
	 * Gets the stato operativo variazione di bilancio.
	 *
	 * @return the stato operativo variazione di bilancio
	 */
	public StatoOperativoVariazioneBilancio getStatoOperativoVariazioneDiBilancio() {
		return statoOperativoVariazioneBilancio;
	}
	

}

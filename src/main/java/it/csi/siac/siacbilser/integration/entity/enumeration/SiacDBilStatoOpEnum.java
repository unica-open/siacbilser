/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siaccorser.model.StatoBilancio;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDBilStatoOpEnum.
 */
@EnumEntity(entityName="SiacDBilStatoOp", idPropertyName="bilStatoOpId", codePropertyName="bilStatoOpCode")
public enum SiacDBilStatoOpEnum {
	// FIXME: da cancellare
	/** The Stato di test. */
	StatoDiTest("STATO", StatoBilancio.PLURIENNALE),
	
	
	
	/** The Pluriennale. */
	Pluriennale("PL", StatoBilancio.PLURIENNALE),
	
	/** The Caricamento bilancio di previsione. */
	CaricamentoBilancioDiPrevisione("CB", StatoBilancio.CARICAMENTO_BILANCIO_DI_PREVISIONE),
	
	/** The Bilancio di previsione stampato per giunta. */
	BilancioDiPrevisioneStampatoPerGiunta("SP", StatoBilancio.BILANCIO_DI_PREVISIONE_STAMPATO_PER_GIUNTA),
	
	/** The Bilancio di previsione spampato per consiglio. */
	BilancioDiPrevisioneSpampatoPerConsiglio("SC", StatoBilancio.BILANCIO_DI_PREVISIONE_STAMPATO_PER_CONSIGLIO),
	
	/** The Esercizio provvisorio con caricamento bilancio di previsione. */
	EsercizioProvvisorioConCaricamentoBilancioDiPrevisione("EC", StatoBilancio.ESERCIZIO_PROVVISORIO_CON_CARICAMENTO_BILANCIO_DI_PREVISIONE),
	
	/** The Esercizio provvisorio con bilancio di previsione stampato per giunta. */
	EsercizioProvvisorioConBilancioDiPrevisioneStampatoPerGiunta("SE", StatoBilancio.ESERCIZIO_PROVVISORIO_CON_BILANCIO_DI_PREVISIONE_STAMPATO_PER_GIUNTA),
	//BilancioDiPrevisioneApprovatoEntroFineAnnoNonAncoraEsecutivo("PN", StatoBilancio.BILANCIO_DI_PREVISIONE_APPROVATO_NON_ESECUTIVO),
	/** The Bilancio di previsione approvato ma non ancora esecutivo. */
	BilancioDiPrevisioneApprovatoMaNonAncoraEsecutivo("EN", StatoBilancio.BILANCIO_DI_PREVISIONE_APPROVATO_NON_ESECUTIVO),
	
	/** The Bilancio di previsione trasferito in gestione. */
	BilancioDiPrevisioneTrasferitoInGestione("PG", StatoBilancio.BILANCIO_DI_PREVISIONE_TRASFERITO_IN_GESTIONE),
	
	/** The Caricamento assestamento di bilancio. */
	CaricamentoAssestamentoDiBilancio("CA", StatoBilancio.CARICAMENTO_ASSESTAMENTO_DI_BILANCIO),
	
	/** The Assestamento del bilancio stampato per giunta. */
	AssestamentoDelBilancioStampatoPerGiunta("SA", StatoBilancio.ASSESTAMENTO_DEL_BILANCIO_STAMPATO_PER_GIUNTA),
	
	/** The Assestamento approvato ma non ancora esecutivo. */
	AssestamentoApprovatoMaNonAncoraEsecutivo("NA", StatoBilancio.ASSESTAMENTO_APPROVATO_NON_ESECUTIVO),
	
	/** The Assestamento di bilancio trasferito in gestione. */
	AssestamentoDiBilancioTrasferitoInGestione("TA", StatoBilancio.ASSESTAMENTO_DI_BILANCIO_TRASFERITO_IN_GESTIONE),
	
	/** The Predisposizione consuntivo. */
	PredisposizioneConsuntivo("CO", StatoBilancio.PREDISPOSIZIONE_CONSUNTIVO),
	
	/** The Esercizio chiuso. */
	EsercizioChiuso("CH", StatoBilancio.ESERCIZIO_CHIUSO);

	
	/** The codice. */
	private final String codice;
	
	/** The stato bilancio. */
	private final StatoBilancio statoBilancio;
	
	/**
	 * Instantiates a new siac d bil stato op enum.
	 *
	 * @param codice the codice
	 * @param statoBilancio the stato bilancio
	 */
	SiacDBilStatoOpEnum(String codice, StatoBilancio statoBilancio){
		this.codice = codice;
		this.statoBilancio = statoBilancio;
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
	 * Gets the stato bilancio.
	 *
	 * @return the stato bilancio
	 */
	public StatoBilancio getStatoBilancio() {
		return statoBilancio;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d bil stato op enum
	 */
	public static SiacDBilStatoOpEnum byCodice(String codice){
		for(SiacDBilStatoOpEnum e : SiacDBilStatoOpEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilStatoOpEnum");
	}
	
	/**
	 * By stato bilancio.
	 *
	 * @param statoBilancio the stato bilancio
	 * @return the siac d bil stato op enum
	 */
	public static SiacDBilStatoOpEnum byStatoBilancio(StatoBilancio statoBilancio){
		for(SiacDBilStatoOpEnum e : SiacDBilStatoOpEnum.values()){
			if(e.getStatoBilancio().equals(statoBilancio)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato di bilancio "+ statoBilancio + " non ha un mapping corrispondente in SiacDBilStatoOpEnum");
	}
	
}
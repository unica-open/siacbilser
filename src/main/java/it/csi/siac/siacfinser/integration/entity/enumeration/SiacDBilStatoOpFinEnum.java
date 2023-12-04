/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.enumeration;

import it.csi.siac.siaccorser.model.StatoBilancio;
import it.csi.siac.siacfinser.integration.entity.converter.EnumEntityFin;

@EnumEntityFin(entityName="SiacDBilStatoOpFin", idPropertyName="bilStatoOpId", codePropertyName="bilStatoOpCode")
public enum SiacDBilStatoOpFinEnum {
	
	
	Pluriennale("PL", StatoBilancio.PLURIENNALE),
	CaricamentoBilancioDiPrevisione("CB", StatoBilancio.CARICAMENTO_BILANCIO_DI_PREVISIONE),
	BilancioDiPrevisioneStampatoPerGiunta("SP", StatoBilancio.BILANCIO_DI_PREVISIONE_STAMPATO_PER_GIUNTA),
	BilancioDiPrevisioneSpampatoPerConsiglio("SC", StatoBilancio.BILANCIO_DI_PREVISIONE_STAMPATO_PER_CONSIGLIO),
	EsercizioProvvisorioConCaricamentoBilancioDiPrevisione("EC", StatoBilancio.ESERCIZIO_PROVVISORIO_CON_CARICAMENTO_BILANCIO_DI_PREVISIONE),
	EsercizioProvvisorioConBilancioDiPrevisioneStampatoPerGiunta("SE", StatoBilancio.ESERCIZIO_PROVVISORIO_CON_BILANCIO_DI_PREVISIONE_STAMPATO_PER_GIUNTA),
	//BilancioDiPrevisioneApprovatoEntroFineAnnoNonAncoraEsecutivo("PN", StatoBilancio.BILANCIO_DI_PREVISIONE_APPROVATO_NON_ESECUTIVO),
	BilancioDiPrevisioneApprovatoMaNonAncoraEsecutivo("EN", StatoBilancio.BILANCIO_DI_PREVISIONE_APPROVATO_NON_ESECUTIVO),
	BilancioDiPrevisioneTrasferitoInGestione("PG", StatoBilancio.BILANCIO_DI_PREVISIONE_TRASFERITO_IN_GESTIONE),
	CaricamentoAssestamentoDiBilancio("CA", StatoBilancio.CARICAMENTO_ASSESTAMENTO_DI_BILANCIO),
	AssestamentoDelBilancioStampatoPerGiunta("SA", StatoBilancio.ASSESTAMENTO_DEL_BILANCIO_STAMPATO_PER_GIUNTA),
	AssestamentoApprovatoMaNonAncoraEsecutivo("NA", StatoBilancio.ASSESTAMENTO_APPROVATO_NON_ESECUTIVO),
	AssestamentoDiBilancioTrasferitoInGestione("TA", StatoBilancio.ASSESTAMENTO_DI_BILANCIO_TRASFERITO_IN_GESTIONE),
	PredisposizioneConsuntivo("CO", StatoBilancio.PREDISPOSIZIONE_CONSUNTIVO),
	EsercizioChiuso("CH", StatoBilancio.ESERCIZIO_CHIUSO);

	
	private String codice;
	private StatoBilancio statoBilancio;
	
	/**
	 * @param codice
	 */
	SiacDBilStatoOpFinEnum(String codice, StatoBilancio statoBilancio){
		this.codice = codice;
		this.statoBilancio = statoBilancio;
	}
	
	public String getCodice() {
		return codice;
	}
	
	public StatoBilancio getStatoBilancio() {
		return statoBilancio;
	}

	public static SiacDBilStatoOpFinEnum byCodice(String codice){
		for(SiacDBilStatoOpFinEnum e : SiacDBilStatoOpFinEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDBilStatoOpFinEnum");
	}
	
	public static SiacDBilStatoOpFinEnum byStatoBilancio(StatoBilancio statoBilancio){
		for(SiacDBilStatoOpFinEnum e : SiacDBilStatoOpFinEnum.values()){
			if(e.getStatoBilancio().equals(statoBilancio)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo stato di bilancio "+ statoBilancio + " non ha un mapping corrispondente in SiacDBilStatoOpFinEnum");
	}
	
}
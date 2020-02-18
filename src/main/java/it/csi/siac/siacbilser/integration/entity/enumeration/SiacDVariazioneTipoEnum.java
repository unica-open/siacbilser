/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.TipoVariazione;

// TODO: Auto-generated Javadoc
/**
 * The Enum SiacDVariazioneTipoEnum.
 */
@EnumEntity(entityName="SiacDVariazioneTipo", idPropertyName="variazioneTipoId", codePropertyName="variazioneTipoCode")
public enum SiacDVariazioneTipoEnum {
		
	/** The Storno. */
	Storno("ST", TipoVariazione.STORNO),
	
	/** The Variazione importo. */
	VariazioneImporto("VA", TipoVariazione.VARIAZIONE_IMPORTO),
	
	/** The Variazione codifica. */
	VariazioneCodifica("CD", TipoVariazione.VARIAZIONE_CODIFICA),
	
	/** The Prelevamento fondo di riserva. */
	PrelevamentoFondoDiRiserva("PF", TipoVariazione.PRELEVAMENTO_FONDO_DI_RISERVA),
	
	/** The Utilizzo avanzo di amministrazione. */
	UtilizzoAvanzoDiAmministrazione("UA", TipoVariazione.UTILIZZO_AVANZO_DI_AMMINISTRAZIONE),
	
	/** The Prenotazione al pluriennale. */
	PrenotazioneAlPluriennale("PP", TipoVariazione.PRENOTAZIONE_AL_PLURIENNALE),
	
	/** The Prenotazione passata ad assegnazione. */
	PrenotazionePassataAdAssegnazione("XP", TipoVariazione.PRENOTAZIONE_PASSATA_AD_ASSEGNAZIONE),
	/** The Prenotazione passata ad assegnazione. */
	VariazionePerRiaccertamento("VR", TipoVariazione.VARIAZIONE_PER_RIACCERTAMENTO),
	/**the Variazione per assestamento*/
	VariazionePerAssestamento("AS", TipoVariazione.VARIAZIONE_PER_ASSESTAMENTO),
	
	//VariazioneDecentrata("VD", TipoVariazione.VARIAZIONE_DECENTRATA),
	
	//SIAC-4637
	VariazioneCostituzioneIncrementoFPVSpesa("FP", TipoVariazione.COSTITUZIONE_INCREMENTO_FPV_SPESA)
	
	;

	/** The codice. */
	private final String codice;
	
	/** The tipo variazione. */
	private final TipoVariazione tipoVariazione;

	/**
	 * Instantiates a new siac d variazione tipo enum.
	 *
	 * @param codice the codice
	 * @param tipoVariazione the tipo variazione
	 */
	SiacDVariazioneTipoEnum(String codice, TipoVariazione tipoVariazione){
		this.codice = codice;
		this.tipoVariazione = tipoVariazione;
	}

	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}
	
//	public SiacDVariazioneTipo getEntity(){
//		SiacDVariazioneTipo result = new SiacDVariazioneTipo();
//		result.setVariazioneTipoId(getId());
//		result.setVariazioneTipoCode(getCodice());
//		return result;
//	}

	/**
 * Gets the tipo variazione.
 *
 * @return the tipo variazione
 */
public TipoVariazione getTipoVariazione() {
		return tipoVariazione;
	}
	
	
	/**
	 * By tipo variazione.
	 *
	 * @param soeb the soeb
	 * @return the siac d variazione tipo enum
	 */
	public static SiacDVariazioneTipoEnum byTipoVariazione(TipoVariazione soeb){
		for(SiacDVariazioneTipoEnum e : SiacDVariazioneTipoEnum.values()){
			if(e.getTipoVariazione().equals(soeb)){
				return e;
			}
		}
		throw new IllegalArgumentException("Tipo variazione "+ soeb + " non ha un mapping corrispondente in SiacDVariazioneTipoEnum");
	}
	
	/**
	 * By tipo variazione even null.
	 *
	 * @param soeb the soeb
	 * @return the siac d variazione tipo enum
	 */
	public static SiacDVariazioneTipoEnum byTipoVariazioneEvenNull(TipoVariazione soeb){
		if(soeb==null){
			return null;
		}

		return byTipoVariazione(soeb);
	}
	
	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d variazione tipo enum
	 */
	public static SiacDVariazioneTipoEnum byCodice(String codice){
		for(SiacDVariazioneTipoEnum e : SiacDVariazioneTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDVariazioneTipoEnum");
	}
	
	

}

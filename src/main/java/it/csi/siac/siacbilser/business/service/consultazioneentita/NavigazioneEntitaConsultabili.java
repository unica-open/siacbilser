/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlType;

import it.csi.siac.siaccecser.model.CECDataDictionary;
import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;

/**
 * Elenco dei tipi di entita consultabile.
 * 
 * @author Domenico Lisi
 * @version 1.0 - 22/03/2016
 */
@XmlType(namespace = CECDataDictionary.NAMESPACE)
public enum NavigazioneEntitaConsultabili {
	
	NULL(null, TipoEntitaConsultabile.CAPITOLOSPESA, 
			   TipoEntitaConsultabile.CAPITOLOENTRATA, 
			   TipoEntitaConsultabile.PROVVEDIMENTO, 
			   TipoEntitaConsultabile.SOGGETTO),

	CAPITOLOSPESA(TipoEntitaConsultabile.CAPITOLOSPESA, TipoEntitaConsultabile.IMPEGNO, 
			                                            TipoEntitaConsultabile.LIQUIDAZIONE, 
			                                            TipoEntitaConsultabile.MANDATO, 
			                                            TipoEntitaConsultabile.VARIAZIONE,
		                                                TipoEntitaConsultabile.DOCUMENTO),
	IMPEGNO(TipoEntitaConsultabile.IMPEGNO, TipoEntitaConsultabile.LIQUIDAZIONE, 
			                                TipoEntitaConsultabile.MANDATO),
	
	LIQUIDAZIONE(TipoEntitaConsultabile.LIQUIDAZIONE, TipoEntitaConsultabile.MANDATO),
	
	
	CAPITOLOENTRATA(TipoEntitaConsultabile.CAPITOLOENTRATA, TipoEntitaConsultabile.ACCERTAMENTO, 
			                                                TipoEntitaConsultabile.REVERSALE, 
			                                                TipoEntitaConsultabile.VARIAZIONE,
			                                                TipoEntitaConsultabile.DOCUMENTO),
	
	ACCERTAMENTO(TipoEntitaConsultabile.ACCERTAMENTO, TipoEntitaConsultabile.REVERSALE),
	
	
	
	PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.IMPEGNO, 
			                                            TipoEntitaConsultabile.LIQUIDAZIONE, 
			                                            TipoEntitaConsultabile.MANDATO, 
			                                            // SIAC-4588
			                                            TipoEntitaConsultabile.ACCERTAMENTO,
			                                            TipoEntitaConsultabile.REVERSALE,
														// SIAC-4589
			                                            TipoEntitaConsultabile.MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_SPESA,
			                                            TipoEntitaConsultabile.MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_ENTRATA,
			                                            TipoEntitaConsultabile.ALLEGATO, 
			                                            TipoEntitaConsultabile.ELENCO, 
			                                            TipoEntitaConsultabile.DOCUMENTO,
			                                            TipoEntitaConsultabile.VARIAZIONE),
	
	ALLEGATO(TipoEntitaConsultabile.ALLEGATO, TipoEntitaConsultabile.ELENCO, TipoEntitaConsultabile.DOCUMENTO),
	ELENCO(TipoEntitaConsultabile.ELENCO, TipoEntitaConsultabile.DOCUMENTO),
	
	
	SOGGETTO(TipoEntitaConsultabile.SOGGETTO, TipoEntitaConsultabile.IMPEGNO,
											  TipoEntitaConsultabile.LIQUIDAZIONE, 
								              TipoEntitaConsultabile.MANDATO,
								              TipoEntitaConsultabile.ACCERTAMENTO,
								              TipoEntitaConsultabile.REVERSALE,
                                              TipoEntitaConsultabile.DOCUMENTO,
                                              // SIAC-5279
                                              TipoEntitaConsultabile.INDIRIZZO,
                                              TipoEntitaConsultabile.SEDE_SECONDARIA_SOGGETTO,
                                              TipoEntitaConsultabile.MODALITA_PAGAMENTO_SOGGETTO),
	
	//Foglie:
	MANDATO(TipoEntitaConsultabile.MANDATO),
	REVERSALE(TipoEntitaConsultabile.REVERSALE),
	VARIAZIONE(TipoEntitaConsultabile.VARIAZIONE),
	DOCUMENTO(TipoEntitaConsultabile.DOCUMENTO),
	
	MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_SPESA(TipoEntitaConsultabile.MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_SPESA),
	MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_ENTRATA(TipoEntitaConsultabile.MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_ENTRATA),
	
	// SIAC-5279
	INDIRIZZO(TipoEntitaConsultabile.INDIRIZZO),
	SEDE_SECONDARIA_SOGGETTO(TipoEntitaConsultabile.SEDE_SECONDARIA_SOGGETTO),
	MODALITA_PAGAMENTO_SOGGETTO(TipoEntitaConsultabile.MODALITA_PAGAMENTO_SOGGETTO),
	
	;
	
	private final TipoEntitaConsultabile tipoEntitaConsultabile;
	private TipoEntitaConsultabile[] tipoEntitaConsultabili;
	
	private NavigazioneEntitaConsultabili(TipoEntitaConsultabile tipoEntitaConsultabile, TipoEntitaConsultabile... tipoEntitaConsultabili) {
		this.tipoEntitaConsultabile = tipoEntitaConsultabile;
		this.tipoEntitaConsultabili = tipoEntitaConsultabili;
	}
	
	
	/**
	 * @return the tipoEntitaConsultabile
	 */
	public TipoEntitaConsultabile getTipoEntitaConsultabile() {
		return tipoEntitaConsultabile;
	}


	/**
	 * @return the tipoEntitaConsultabili
	 */
	public TipoEntitaConsultabile[] getTipoEntitaConsultabili() {
		return tipoEntitaConsultabili;
	}
	
	public List<TipoEntitaConsultabile> getTipoEntitaConsultabiliList() {
		return Arrays.asList(tipoEntitaConsultabili);
	}
	
	public boolean isParent(){
		return tipoEntitaConsultabili != null && tipoEntitaConsultabili.length != 0;
	}
	
	public boolean isSon(){
		return !isParent();
	}


	public static NavigazioneEntitaConsultabili byTipoEntitaConsultabile(TipoEntitaConsultabile tec) {
		for(NavigazioneEntitaConsultabili nec : NavigazioneEntitaConsultabili.values()) {
			if((nec.getTipoEntitaConsultabile() == null && tec == null) || 
					nec.getTipoEntitaConsultabile() !=null && nec.getTipoEntitaConsultabile().equals(tec)){
				return nec;
			}
		}
		return null;
	}
	
}

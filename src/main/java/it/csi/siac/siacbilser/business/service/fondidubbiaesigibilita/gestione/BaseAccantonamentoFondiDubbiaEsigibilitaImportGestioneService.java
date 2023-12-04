/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.gestione;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.BaseAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoImportazione;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

/**
 * Inserimento dei fondi a dubbia e difficile esazione, base per l'importazione
 * 
 * @author Marchino Alessandro
 */
public abstract class BaseAccantonamentoFondiDubbiaEsigibilitaImportGestioneService<REQ extends ServiceRequest, RES extends BaseAccantonamentoFondiDubbiaEsigibilitaResponse> extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaGestioneService<REQ, RES> {
	
	protected List<AccantonamentoFondiDubbiaEsigibilitaGestione> fondiDubbiaEsigibilita = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaGestione>();
	protected AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiOld;
	
	/**
	 * Caricamento dei capitoli e wrap negli accantonamenti
	 * @param tipoImportazioneRichiesta il tipo di importazione richiesta
	 * @param bilancio il bilancio
	 */
	protected void loadAccantonamenti(TipoImportazione tipoImportazioneRichiesta, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancioVersioneOld, CapitoloEntrataGestioneModelDetail... modelDetails) {
		final String methodName = "loadAccantonamenti";
		List<CapitoloEntrataGestione> capitoli = new ArrayList<CapitoloEntrataGestione>();
		switch(tipoImportazioneRichiesta) {
			case DA_ANAGRAFICA_CAPITOLI:
				log.debug(methodName, "Caricamento capitoli da anagrafica");
				capitoli.addAll(capitoloEntrataGestioneDad.getCapitoliNonCollegatiFCDE(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, CapitoloEntrataGestione.class, modelDetails));
				break;
			case DA_PREVISIONE:
				log.debug(methodName, "Caricamento capitoli da ultima previsione");
				attributiOld = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findPreviousOrCurrent(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
				if(attributiOld != null) {
					capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaGestioneDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiOld, modelDetails));
				}
				break;
			case DA_GESTIONE:
				log.debug(methodName, "Caricamento capitoli da ultima gestione");
				//SIAC-8851
				attributiOld = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findPreviousGestione(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE, StatoAccantonamentoFondiDubbiaEsigibilita.DEFINITIVA, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
				if(attributiOld != null) {
					capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaGestioneDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiOld, modelDetails));
				}
				break;			
			case DA_RENDICONTO:
				log.debug(methodName, "Caricamento capitoli da ultimo rendiconto");
				attributiOld = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findPrevious(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
				if(attributiOld != null) {
					capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaGestioneDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiOld, modelDetails));
				}
				break;
			case DA_VERSIONE_PRECEDENTE:
				log.debug(methodName, "Caricamento capitoli da versione precedente");
				capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaGestioneDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiBilancioVersioneOld, modelDetails));
				break;
			default:
				log.warn(methodName, "Tipo di importazione " + tipoImportazioneRichiesta + " non gestito. Caricamento accantonamenti saltato");
				break;
		}
		log.info(methodName, "Inserimento di " + capitoli.size() + " capitoli. Sorgente dati: " + tipoImportazioneRichiesta);
		for(CapitoloEntrataGestione cep : capitoli) {
			AccantonamentoFondiDubbiaEsigibilitaGestione afde = new AccantonamentoFondiDubbiaEsigibilitaGestione();
			afde.setCapitolo(cep);
			// cerco la media di confronto
//			if(TipoImportazione.DA_VERSIONE_PRECEDENTE.equals(tipoImportazioneRichiesta)) {
//			popolaMediaConfronto(afde);
			cercaMediaConfronto(afde);
//			}
			fondiDubbiaEsigibilita.add(afde);
		}
	}

}

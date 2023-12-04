/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.rendiconto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.BaseAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
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
public abstract class BaseAccantonamentoFondiDubbiaEsigibilitaImportRendicontoService<REQ extends ServiceRequest, RES extends BaseAccantonamentoFondiDubbiaEsigibilitaResponse> extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaRendicontoService<REQ, RES> {
	
	protected List<AccantonamentoFondiDubbiaEsigibilitaRendiconto> fondiDubbiaEsigibilita = new ArrayList<AccantonamentoFondiDubbiaEsigibilitaRendiconto>();
	protected AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiOld;
	
	protected void loadAccantonamenti(TipoImportazione tipoImportazione, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancioVersioneOld, CapitoloEntrataGestioneModelDetail... modelDetails) {
		final String methodName = "loadAccantonamenti";
		List<CapitoloEntrataGestione> capitoli = new ArrayList<CapitoloEntrataGestione>();
		switch(tipoImportazione) {
			case DA_ANAGRAFICA_CAPITOLI:
				log.debug(methodName, "Caricamento capitoli da anagrafica");
				capitoli.addAll(capitoloEntrataGestioneDad.getCapitoliNonCollegatiFCDE(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, CapitoloEntrataGestione.class, modelDetails));
				break;
			case DA_PREVISIONE:
				log.debug(methodName, "Caricamento capitoli da ultima previsione");
				attributiOld = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findPreviousOrCurrent(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
				if(attributiOld != null) {
					capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaRendicontoDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiOld, modelDetails));
				}
				break;
			case DA_GESTIONE:
				log.debug(methodName, "Caricamento capitoli da ultima gestione");
				attributiOld = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findPreviousOrCurrent(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
				if(attributiOld != null) {
					capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaRendicontoDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiOld, modelDetails));
				}
				break;
			case DA_RENDICONTO:
				log.debug(methodName, "Caricamento capitoli da ultimo rendiconto");
				// FIXME: O rendiconto?
				attributiOld = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findPrevious(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
				if(attributiOld != null) {
					capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaRendicontoDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiOld, modelDetails));
				}
				break;
			case DA_VERSIONE_PRECEDENTE:
				log.debug(methodName, "Caricamento capitoli da versione precedente");
				attributiOld = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findById(attributiBilancioVersioneOld.getUid(), AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
				capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaRendicontoDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiBilancioVersioneOld, modelDetails));
				break;
			default:
				log.warn(methodName, "Tipo di importazione " + tipoImportazione + " non gestito. Caricamento accantonamenti saltato");
				break;
		}
		log.info(methodName, "Inserimento di " + capitoli.size() + " capitoli. Sorgente dati: " + tipoImportazione);
		for(CapitoloEntrataGestione cep : capitoli) {
			AccantonamentoFondiDubbiaEsigibilitaRendiconto afde = new AccantonamentoFondiDubbiaEsigibilitaRendiconto();
			afde.setCapitolo(cep);
			fondiDubbiaEsigibilita.add(afde);
		}
	}

}

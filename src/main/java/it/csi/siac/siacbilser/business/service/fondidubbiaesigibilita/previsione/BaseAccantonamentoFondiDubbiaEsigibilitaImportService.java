/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.previsione;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.BaseAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.TipoImportazione;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataPrevisioneModelDetail;

/**
 * Inserimento dei fondi a dubbia e difficile esazione, base per l'importazione
 * 
 * @author Marchino Alessandro
 */
public abstract class BaseAccantonamentoFondiDubbiaEsigibilitaImportService<REQ extends ServiceRequest, RES extends BaseAccantonamentoFondiDubbiaEsigibilitaResponse> extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaService<REQ, RES> {
	
	protected List<AccantonamentoFondiDubbiaEsigibilita> fondiDubbiaEsigibilita = new ArrayList<AccantonamentoFondiDubbiaEsigibilita>();
	protected AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiOld;
	
	protected void loadAccantonamenti(TipoImportazione tipoImportazione, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio attributiBilancioVersioneOld, CapitoloEntrataPrevisioneModelDetail... modelDetails) {
		final String methodName = "loadAccantonamenti";
		List<CapitoloEntrataPrevisione> capitoli = new ArrayList<CapitoloEntrataPrevisione>();
		switch(tipoImportazione) {
			case DA_ANAGRAFICA_CAPITOLI:
				log.debug(methodName, "Caricamento capitoli da anagrafica");
				capitoli.addAll(capitoloEntrataPrevisioneDad.getCapitoliNonCollegatiFCDE(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, CapitoloEntrataPrevisione.class, modelDetails));
				break;
			case DA_PREVISIONE:
				log.debug(methodName, "Caricamento capitoli da ultima previsione");
				attributiOld = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findPrevious(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
				if(attributiOld != null) {
					capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiOld, modelDetails));
				}
				break;
			case DA_GESTIONE:
				log.debug(methodName, "Caricamento capitoli da ultima gestione");
				attributiOld = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findPrevious(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
				if(attributiOld != null) {
					capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiOld, modelDetails));
				}
				break;
			case DA_RENDICONTO:
				log.debug(methodName, "Caricamento capitoli da ultimo rendiconto");
				attributiOld = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findPrevious(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
				if(attributiOld != null) {
					capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiOld, modelDetails));
				}
				break;
			case DA_VERSIONE_PRECEDENTE:
				log.debug(methodName, "Caricamento capitoli da versione precedente");
				attributiOld = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findById(attributiBilancioVersioneOld.getUid(), AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Tipo);
				capitoli.addAll(accantonamentoFondiDubbiaEsigibilitaDad.findCapitoliInBilancioEquivalentiNonCollegati(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio, attributiBilancioVersioneOld, modelDetails));
				break;
			default:
				log.warn(methodName, "Tipo di importazione " + tipoImportazione + " non gestito. Caricamento accantonamenti saltato");
				break;
		}
		log.info(methodName, "Inserimento di " + capitoli.size() + " capitoli. Sorgente dati: " + tipoImportazione);
		for(CapitoloEntrataPrevisione cep : capitoli) {
			AccantonamentoFondiDubbiaEsigibilita afde = new AccantonamentoFondiDubbiaEsigibilita();
			afde.setCapitolo(cep);
			fondiDubbiaEsigibilita.add(afde);
		}
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.gestione;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.InserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.InserisceAccantonamentoFondiDubbiaEsigibilitaImportGestioneResponse;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.model.fcde.TipoImportazione;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

/**
 * Inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceAccantonamentoFondiDubbiaEsigibilitaImportGestioneService extends BaseAccantonamentoFondiDubbiaEsigibilitaImportGestioneService<InserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione, InserisceAccantonamentoFondiDubbiaEsigibilitaImportGestioneResponse> {
	
	@Override
	@Transactional
	public InserisceAccantonamentoFondiDubbiaEsigibilitaImportGestioneResponse executeService(InserisceAccantonamentoFondiDubbiaEsigibilitaImportGestione serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio");
		checkNotNull(req.getTipoImportazione(), "tipo importazione");
		if(TipoImportazione.DA_VERSIONE_PRECEDENTE.equals(req.getTipoImportazione())) {
			checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioOld(), "attributi bilancio old");
		}
	}
	
	@Override
	protected void execute() {
		// Lettura accantonamento
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findById(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid(),
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Bilancio,
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato);
		
		// Caricamento accantonamenti
		loadAccantonamenti(req.getTipoImportazione(), req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioOld());
		
		// Salvataggio accantonamenti
		for(AccantonamentoFondiDubbiaEsigibilitaGestione afde : fondiDubbiaEsigibilita) {
			// Popolamento dati
			popolaDatiAccantonamento(afde);
			// Inserisco il singolo accantonamento
			inserisciAccantonamento(afde);
		}
		
		// popolaResponse
		List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> accantonamentiFondiDubbiaEsigibilita = caricaAccantonamenti(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		res.setListaAccantonamenti(accantonamentiFondiDubbiaEsigibilita);
	}

}

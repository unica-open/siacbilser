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

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.InserisceAccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.gestione.InserisceAccantonamentoFondiDubbiaEsigibilitaGestioneResponse;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceAccantonamentoFondiDubbiaEsigibilitaGestioneService extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaGestioneService<InserisceAccantonamentoFondiDubbiaEsigibilitaGestione, InserisceAccantonamentoFondiDubbiaEsigibilitaGestioneResponse> {
	
	@Override
	@Transactional
	public InserisceAccantonamentoFondiDubbiaEsigibilitaGestioneResponse executeService(InserisceAccantonamentoFondiDubbiaEsigibilitaGestione serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio");
		// Controllo la lista di accantonamenti ottenuta dalla request
		checkNotNull(req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione(), "nessun capitolo selezionato");
		checkCondition(!req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("nessun capitolo selezionato"), false);
		//controllo ogni accantonamento
		int i = 1;
		for(AccantonamentoFondiDubbiaEsigibilitaGestione afde : req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione()) {
			checkNotNull(afde, "accantonamento " + i, false);
			//un accantonamento deve essere necessariamente legato ad un capitolo
			checkCondition(afde == null || (afde.getCapitolo() != null && afde.getCapitolo().getUid() != 0), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("capitolo accantonamento " + i), false);
			i++;
		}
	}
	
	@Override
	protected void execute() {
		// Lettura accantonamento
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findById(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid(),
				AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail.Stato);
		
		Utility.MDTL.addModelDetails(AccantonamentoFondiDubbiaEsigibilitaModelDetail.TipoMedia);
		
		// Caricamento accantonamenti
		for(AccantonamentoFondiDubbiaEsigibilitaGestione afde : req.getListaAccantonamentoFondiDubbiaEsigibilitaGestione()) {
			// Controllo che l'accantonamento non sia gia' presente
			checkAccantonamentoGiaEsistente(afde);
			// Passo l'anno di bilancio agli accontonamenti per la media di confronto
			passaAnnoBilancio(afde);
			// Popolamento dati
			popolaDatiAccantonamento(afde);
			// Inserisco il singolo accantonamento
			inserisciAccantonamento(afde);
		}
		// popolaResponse
		List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> accantonamentiFondiDubbiaEsigibilita = caricaAccantonamenti(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		res.setListaAccantonamenti(accantonamentiFondiDubbiaEsigibilita);
	}
	
	private void passaAnnoBilancio(AccantonamentoFondiDubbiaEsigibilitaGestione afde) {
		if(afde.getCapitolo() != null) {
			afde.getCapitolo().setAnnoCapitolo(req.getAnnoBilancio());
		}
	}

}

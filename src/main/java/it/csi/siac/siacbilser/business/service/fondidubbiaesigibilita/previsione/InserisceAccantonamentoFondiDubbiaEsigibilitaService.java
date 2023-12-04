/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.previsione;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.InserisceAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.previsione.InserisceAccantonamentoFondiDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserimento dei fondi a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceAccantonamentoFondiDubbiaEsigibilitaService extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaService<InserisceAccantonamentoFondiDubbiaEsigibilita, InserisceAccantonamentoFondiDubbiaEsigibilitaResponse> {
	
	@Override
	@Transactional
	public InserisceAccantonamentoFondiDubbiaEsigibilitaResponse executeService(InserisceAccantonamentoFondiDubbiaEsigibilita serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio");
		// Controllo la lista di accantonamenti ottenuta dalla request
		checkNotNull(req.getListaAccantonamentoFondiDubbiaEsigibilita(), "nessun capitolo selezionato");
		checkCondition(!req.getListaAccantonamentoFondiDubbiaEsigibilita().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("nessun capitolo selezionato"), false);
		//controllo ogni accantonamento
		int i = 1;
		for(AccantonamentoFondiDubbiaEsigibilita afde : req.getListaAccantonamentoFondiDubbiaEsigibilita()) {
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
		
		// Caricamento accantonamenti
		for(AccantonamentoFondiDubbiaEsigibilita afde : req.getListaAccantonamentoFondiDubbiaEsigibilita()) {
			// Controllo che l'accantonamento non sia gia' presente
			checkAccantonamentoGiaEsistente(afde);
			// Popolamento dati
			popolaDatiAccantonamento(afde);
			
			//SIAC-8519 non sono ammesse medie ponderate per la gestione
			afde.setMediaPonderataTotali(null);
			afde.setMediaPonderataRapporti(null);
			
			// Inserisco il singolo accantonamento
			inserisciAccantonamento(afde);
		}
		
		// popolaResponse
		List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> accantonamentiFondiDubbiaEsigibilita = caricaAccantonamenti(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		res.setListaAccantonamenti(accantonamentiFondiDubbiaEsigibilita);
	}

}

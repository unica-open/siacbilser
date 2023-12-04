/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.rendiconto;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.rendiconto.InserisceAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
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
public class InserisceAccantonamentoFondiDubbiaEsigibilitaRendicontoService extends BaseCUDAccantonamentoFondiDubbiaEsigibilitaRendicontoService<InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto, InserisceAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse> {
	
	@Override
	@Transactional
	public InserisceAccantonamentoFondiDubbiaEsigibilitaRendicontoResponse executeService(InserisceAccantonamentoFondiDubbiaEsigibilitaRendiconto serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio");
		// Controllo la lista di accantonamenti ottenuta dalla request
		checkNotNull(req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto(), "nessun capitolo selezionato");
		checkCondition(!req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("nessun capitolo selezionato"), false);
		//controllo ogni accantonamento
		int i = 1;
		for(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde : req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto()) {
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
		for(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde : req.getListaAccantonamentoFondiDubbiaEsigibilitaRendiconto()) {
			// Controllo che l'accantonamento non sia gia' presente
			checkAccantonamentoGiaEsistente(afde);
			// Popolamento dati
			popolaDatiAccantonamento(adeguaAccantonamentoPreInserimento(afde));
			// Inserisco il singolo accantonamento
			inserisciAccantonamento(afde);
		}
		
		// popolaResponse
		List<AccantonamentoFondiDubbiaEsigibilitaBase<?>> accantonamentiFondiDubbiaEsigibilita = caricaAccantonamenti(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
		res.setListaAccantonamenti(accantonamentiFondiDubbiaEsigibilita);
	}
	
	/**
	 * SIAC-8540 
	 * 
	 * In caso di inserimento mancano gli importi di accantonamento e residuo finale, a seguito della SIAC-8446
	 * il rendiconto non ricalcola più il dato durante l'estrazione degli excell ma la legge direttamente dal db.
	 *  
	 * @param <AccantonamentoFondiDubbiaEsigibilitaRendiconto> <strong>afde</strong> l'accantonamento di rendiconto
	 * @return l'accantonamento adeguato
	 */
	private AccantonamentoFondiDubbiaEsigibilitaRendiconto adeguaAccantonamentoPreInserimento(AccantonamentoFondiDubbiaEsigibilitaRendiconto afde) {
		if(afde == null || afde.getCapitolo() == null || afde.getCapitolo().getUid() == 0) return afde;
		return accantonamentoFondiDubbiaEsigibilitaRendicontoDad.adeguaAccantonamento(afde, afde.getCapitolo());
	}

}

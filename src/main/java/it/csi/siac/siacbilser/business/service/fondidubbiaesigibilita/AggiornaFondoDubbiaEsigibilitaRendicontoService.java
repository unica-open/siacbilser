/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilitaRendicontoResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaRendicontoDad;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Aggiornamento del fondo a dubbia e difficile esazione, rendiconto
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaFondoDubbiaEsigibilitaRendicontoService extends CheckedAccountBaseService<AggiornaFondoDubbiaEsigibilitaRendiconto, AggiornaFondoDubbiaEsigibilitaRendicontoResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaRendicontoDad accantonamentoFondiDubbiaEsigibilitaRendicontoDad;

	@Override
	@Transactional
	public AggiornaFondoDubbiaEsigibilitaRendicontoResponse executeService(AggiornaFondoDubbiaEsigibilitaRendiconto serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//imposto ente e login operazione nei dad
		accantonamentoFondiDubbiaEsigibilitaRendicontoDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaRendicontoDad.setEnte(ente);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//controllo che mi sia fornito un id dell'accantonamento
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaRendiconto(), "accantonamento");
		//l'accantonamento deve necessariamente avere un capitolo
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaRendiconto().getCapitolo(), "capitolo accantonamento");
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		//effetto dei controlli a partire dalla base dati
		checkUnicoAccantonamentoPerCapitolo(req.getAccantonamentoFondiDubbiaEsigibilitaRendiconto().getCapitolo());
		checkCoerenzaCapitoloAccantonamento(req.getAccantonamentoFondiDubbiaEsigibilitaRendiconto(), req.getAccantonamentoFondiDubbiaEsigibilitaRendiconto().getCapitolo());
		
		//i controlli precedenti sono stati superati, posso aggiornare
		AccantonamentoFondiDubbiaEsigibilitaRendiconto afdeAggiornato = accantonamentoFondiDubbiaEsigibilitaRendicontoDad.update(req.getAccantonamentoFondiDubbiaEsigibilitaRendiconto());
		
		//imposto in request e loggo i dati
		log.debug(methodName, "Aggiornato accantonamento con uid " + afdeAggiornato.getUid());
		res.setAccantonamentoFondiDubbiaEsigibilitaRendiconto(afdeAggiornato);
	}

	/**
	 * Controlla che vi sia un unico accantonamento collegato al capitolo
	 * @param capitoloEntrataGestione il capitolo da controllare
	 */
	private void checkUnicoAccantonamentoPerCapitolo(CapitoloEntrataGestione capitoloEntrataGestione) {
		final String methodName = "checkUnicoAccantonamentoPerCapitolo";
		//calcolo quanti accantonamenti vi siano per il capitolo
		Long countAccantonamentoPerCapitolo = accantonamentoFondiDubbiaEsigibilitaRendicontoDad.countByCapitolo(capitoloEntrataGestione);
		log.debug(methodName, "Numero di accantonamenti: " + countAccantonamentoPerCapitolo + " [capitolo " + capitoloEntrataGestione.getUid() + "]");
		if(countAccantonamentoPerCapitolo == null || countAccantonamentoPerCapitolo.longValue() == 0L) {
			//non esitono accantonamenti per il capitolo: non posso aggiornarlo, lancio un errore 
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Aggiornamento accantonamento: capitolo senza accantonamenti collegati"));
		}
		// Questo e' un errore di dati su db
		if(countAccantonamentoPerCapitolo.longValue() > 1L) {
			//esiste piu' di un accantonamento per il capitolo, non sarebbe dovuto succedere
			throw new IllegalStateException("Capitolo con " + countAccantonamentoPerCapitolo.longValue() + " accantonamenti collegati");
		}
	}
	
	/**
	 * Controlla che il capitolo specificato e quello collegato all'accantonamento siano lo stesso
	 * @param accantonamentoFondiDubbiaEsigibilitaRendiconto l'accantonamento da controllare
	 * @param capitoloEntrataGestione il capitolo da controllare
	 */
	private void checkCoerenzaCapitoloAccantonamento(AccantonamentoFondiDubbiaEsigibilitaRendiconto accantonamentoFondiDubbiaEsigibilitaRendiconto, CapitoloEntrataGestione capitoloEntrataGestione) {
		//ottengo l'accantonamento del caitolo
		AccantonamentoFondiDubbiaEsigibilitaRendiconto afde = accantonamentoFondiDubbiaEsigibilitaRendicontoDad.findByCapitolo(capitoloEntrataGestione);
		if(afde == null) {
			//il capitolo non ha alcun accantonamento collegato, non posso aggiornare, lancio un'eccezione
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accantonamento", "collegata al capitolo con uid " + capitoloEntrataGestione.getUid()));
		}
		if(afde.getUid() != accantonamentoFondiDubbiaEsigibilitaRendiconto.getUid()) {
			//su db l'accantonamento che voglio aggiornare e' legato ad un altro capitolo. Lancio un'eccezione, non posso andare avanti
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Aggiornamento accantonamento: l'accantonamento collegato al capitolo con uid "
					+ capitoloEntrataGestione.getUid() + " non coincide con quello che si e' richiesto di aggiornare [uid collegato: "
					+ afde.getUid()+ ", uid richiesto per l'aggiornamento " +  accantonamentoFondiDubbiaEsigibilitaRendiconto.getUid() + "]"));
		}
	}

}

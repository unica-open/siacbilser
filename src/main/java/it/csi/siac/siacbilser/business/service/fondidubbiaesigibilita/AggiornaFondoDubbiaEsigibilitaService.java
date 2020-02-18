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
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilita;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaFondoDubbiaEsigibilitaResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Aggiornamento del fondo a dubbia e difficile esazione
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaFondoDubbiaEsigibilitaService extends CheckedAccountBaseService<AggiornaFondoDubbiaEsigibilita, AggiornaFondoDubbiaEsigibilitaResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;

	@Override
	@Transactional
	public AggiornaFondoDubbiaEsigibilitaResponse executeService(AggiornaFondoDubbiaEsigibilita serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//imposto ente e login operazione nei dad
		accantonamentoFondiDubbiaEsigibilitaDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaDad.setEnte(ente);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//controllo che mi sia fornito un id dell'accantonamento
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilita(), "accantonamento");
		//l'accantonamento deve necessariamente avere un capitolo
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilita().getCapitolo(), "capitolo accantonamento");
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		//effetto dei controlli a partire dalla base dati
		checkUnicoAccantonamentoPerCapitolo(req.getAccantonamentoFondiDubbiaEsigibilita().getCapitolo());
		checkCoerenzaCapitoloAccantonamento(req.getAccantonamentoFondiDubbiaEsigibilita(), req.getAccantonamentoFondiDubbiaEsigibilita().getCapitolo());
		
		//i controlli precedenti sono stati superati, posso aggiornare
		AccantonamentoFondiDubbiaEsigibilita afdeAggiornato = accantonamentoFondiDubbiaEsigibilitaDad.update(req.getAccantonamentoFondiDubbiaEsigibilita());
		//imposto in response e loggo i dati
		log.debug(methodName, "Aggiornato accantonamento con uid " + afdeAggiornato.getUid());
		res.setAccantonamentoFondiDubbiaEsigibilita(afdeAggiornato);
	}

	/**
	 * Controlla che vi sia un unico accantonamento collegato al capitolo
	 * @param capitoloEntrataPrevisione il capitolo da controllare
	 */
	private void checkUnicoAccantonamentoPerCapitolo(CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		final String methodName = "checkUnicoAccantonamentoPerCapitolo";
		//calcolo quanti accantonamenti vi siano per il capitolo
		Long countAccantonamentoPerCapitolo = accantonamentoFondiDubbiaEsigibilitaDad.countByCapitolo(capitoloEntrataPrevisione);
		log.debug(methodName, "Numero di accantonamenti: " + countAccantonamentoPerCapitolo + " [capitolo " + capitoloEntrataPrevisione.getUid() + "]");
		
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
	 * @param accantonamentoFondiDubbiaEsigibilita l'accantonamento da controllare
	 * @param capitoloEntrataPrevisione il capitolo da controllare
	 */
	private void checkCoerenzaCapitoloAccantonamento(AccantonamentoFondiDubbiaEsigibilita accantonamentoFondiDubbiaEsigibilita, CapitoloEntrataPrevisione capitoloEntrataPrevisione) {
		//ottengo l'accantonamento del caitolo
		AccantonamentoFondiDubbiaEsigibilita afde = accantonamentoFondiDubbiaEsigibilitaDad.findByCapitolo(capitoloEntrataPrevisione);
		if(afde == null) {
			//il capitolo non ha alcun accantonamento collegato, non posso aggiornare, lancio un'eccezione
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accantonamento", "collegata al capitolo con uid " + capitoloEntrataPrevisione.getUid()));
		}
		if(afde.getUid() != accantonamentoFondiDubbiaEsigibilita.getUid()) {
			//su db l'accantonamento che voglio aggiornare e' legato ad un altro capitolo. Lancio un'eccezione, non posso andare avanti
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Aggiornamento accantonamento: l'accantonamento collegato al capitolo con uid "
					+ capitoloEntrataPrevisione.getUid() + " non coincide con quello che si e' richiesto di aggiornare [uid collegato: "
					+ afde.getUid()+ ", uid richiesto per l'aggiornamento " +  accantonamentoFondiDubbiaEsigibilita.getUid() + "]"));
		}
	}

}

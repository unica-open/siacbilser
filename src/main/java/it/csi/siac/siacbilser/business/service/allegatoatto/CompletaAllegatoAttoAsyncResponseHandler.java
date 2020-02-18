/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompletaAllegatoAttoAsyncResponseHandler extends BilAsyncResponseHandler<CompletaAllegatoAttoResponse> {

	@Autowired
	private ElaborazioniManager elaborazioniManager;
	
	@Override
	public void handleResponse(CompletaAllegatoAttoResponse response) {
		final String methodName = "handleResponse";
		//gestione centralizzata delle chiavi di blocco delle elaborazioni attive
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(response.getAllegatoAtto().getUid());
		ElabKeys completaAllegatoAttoKeySelector = ElabKeys.COMPLETA_ALLEGATO_ATTO;
		boolean endElab = elaborazioniManager.endElaborazione(eakh.creaElabServiceFromPattern(completaAllegatoAttoKeySelector), eakh.creaElabKeyFromPattern(completaAllegatoAttoKeySelector));
		if(endElab){
			//l'operazione e' stata correttamente disattivata. l'oggo l'informazione
			log.info(methodName, "Elaborazione segnata come terminata.");
		}
		
		//inserisco gli eventuali messaggi della response su base dati, in modo tale che sia poi possibile mostrarli da cruscotto
		for(Messaggio messaggio : response.getMessaggi()) {
			log.info(methodName, "Messaggio: " + messaggio.getCodice() + " - "+ messaggio.getDescrizione());
			inserisciDettaglioOperazioneAsinc(messaggio.getCodice(), messaggio.getDescrizione(), Esito.SUCCESSO);
		}
		
		//inserisco gli eventuali errori della response su base dati, in modo tale che sia poi possibile mostrarli da cruscotto
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		//inserisco informazioni eventuali riguardanti i subdocumenti scartati, in modo tale che sia poi possibile mostrarli da cruscotto
		for(SubdocumentoSpesa subdoc : response.getSubdocumentiScartati()) {
			String msg = ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("Quota " + subdoc.getNumero()
					+ (subdoc.getDocumento() != null ? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "")
					+ " [uid:" + subdoc.getUid() + "]",
				"subdcumento sospeso").getTesto();
			log.info(methodName, msg);
			inserisciDettaglioOperazioneAsinc("QUOTA_SCARTATA", msg, Esito.FALLIMENTO);
		}
		
		if(!response.hasErrori()){
			//non si sono verificati errori, inserisco l'informazione di successo su base dati in modo tale che venga visualizzata
			inserisciDettaglioSuccesso();
		}
	}

}

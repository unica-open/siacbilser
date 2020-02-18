/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
/**
 * Response handler per il servizio asincrono di popolamento fondi a dubbia e difficile esazione da anno precedente
 * @author Alessandro Marchino
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteAsyncResponseHandler extends BilAsyncResponseHandler<PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteResponse> {

	//MANAGRERS
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	
	@Override
	public void handleResponse(PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteResponse response) {
		final String methodName = "handleResponse";
		//inizializzo i dati per la creazione della coppia chiave-valore che mi identifica univocamente l'elaborazione
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler( response.getBilancio().getUid(), PopolaFondiDubbiaEsigibilitaDaGestioneAnnoPrecedenteAsyncService.class);
		ElabKeys fondiKeySelector = ElabKeys.POPOLA_FONDI_DUBBIA_ESIGIBILITA_GESTIONE;
		//segno su db l'elaborazione come conclusa, cancellando logicamente il record
		boolean endElab = elaborazioniManager.endElaborazione(eakh.creaElabServiceFromPattern(fondiKeySelector), eakh.creaElabKeyFromPattern(fondiKeySelector));
		if(endElab){
			//ho segnato come completata l'elaborazione. loggo questa informazione
			log.info(methodName, "Elaborazione segnata come terminata.");
		}
		
		//inserisco gli eventuali errori della response su base dati, in modo tale che sia poi possibile mostrarli da cruscotto
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		if(!response.hasErrori()){
			//non si sono verificati errori, inserisco l'informazione di successo su base dati in modo tale che venga visualizzata
			inserisciDettaglioSuccesso();
		}
	}

}

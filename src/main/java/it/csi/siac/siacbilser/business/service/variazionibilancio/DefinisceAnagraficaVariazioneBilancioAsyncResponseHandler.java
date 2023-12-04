/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;

/**
 * Handler della response asincrona del servizio {@link DefinisceAnagraficaVariazioneBilancioService}
 * 
 * @author Domenico
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefinisceAnagraficaVariazioneBilancioAsyncResponseHandler extends BilAsyncResponseHandler<DefinisceAnagraficaVariazioneBilancioResponse> {
	
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	
	@Override
	public void handleResponse(DefinisceAnagraficaVariazioneBilancioResponse response) {
		final String methodName = "handleResponse";
		
		//NB: endElaborazione e' volutamente su AggiornaAnagraficaVariazioneBilancioAsyncService non DefinisceAnagraficaVariazioneBilancioAsyncService in modo da 
		//    bloccare l'elaborazione contemporanea di aggiorna e di definisci.
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(response.getVariazioneImportoCapitolo().getUid(), DefinisceAnagraficaVariazioneBilancioAsyncService.class);
		ElabKeys variazioneKeySelector = ElabKeys.DEFINISCI_VARIAZIONE;
		boolean endElab = elaborazioniManager.endElaborazione(eakh.creaElabServiceFromPattern(variazioneKeySelector), eakh.creaElabKeyFromPattern(variazioneKeySelector));
		if(endElab){
			log.info(methodName, "Elaborazione segnata come terminata.");
		}
		
		//Imposto come Messaggio la response del servizio.
		inserisciDettaglioOperazioneAsinc("ServiceResponse","Response del servizio: "
				+DefinisceAnagraficaVariazioneBilancioService.class.getSimpleName()+".", Esito.SUCCESSO, null, JAXBUtility.marshall(response));
		
		
		for(Messaggio messaggio : response.getMessaggi()) {
			log.info(methodName, "Messaggio: " + messaggio.getCodice() + " - "+ messaggio.getDescrizione());
			inserisciDettaglioOperazioneAsinc(messaggio.getCodice(), messaggio.getDescrizione(), Esito.SUCCESSO);
		}
		
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		if(!response.hasErrori()){
			inserisciDettaglioSuccesso();
		}
	}

}

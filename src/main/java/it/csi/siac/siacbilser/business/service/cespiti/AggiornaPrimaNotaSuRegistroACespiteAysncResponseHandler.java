/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPrimaNotaSuRegistroACespiteAysncResponseHandler extends BilAsyncResponseHandler<AggiornaPrimaNotaSuRegistroACespiteResponse> {

	@Override
	public void handleResponse(AggiornaPrimaNotaSuRegistroACespiteResponse response) {
		final String methodName = "handleResponse";
		
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
		
		if(!response.hasErrori()){
			//non si sono verificati errori, inserisco l'informazione di successo su base dati in modo tale che venga visualizzata
			inserisciDettaglioSuccesso();
		}
	}

}

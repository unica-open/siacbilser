/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraMassivaPrimaNotaIntegrataResponse;

/**
 * Response handler per la registrazione massiva della prima nota integrata
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegistraMassivaPrimaNotaIntegrataAsyncResponseHandler extends BilAsyncResponseHandler<RegistraMassivaPrimaNotaIntegrataResponse> {

	@Override
	public void handleResponse(RegistraMassivaPrimaNotaIntegrataResponse response) {
		String methodName = "handleResponse";
		
		for(Messaggio messaggio : response.getMessaggi()) {
			log.info(methodName, "Messaggio: " + messaggio.getCodice() + " - " + messaggio.getDescrizione());
			inserisciDettaglioOperazioneAsinc(messaggio.getCodice(), messaggio.getDescrizione(), Esito.SUCCESSO);
		}
		
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		if(!response.hasErrori()) {
			inserisciDettaglioSuccesso();
		}
		
	}

}

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
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrataResponse;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ValidazioneMassivaPrimaNotaIntegrataAsyncResponseHandler extends BilAsyncResponseHandler<ValidazioneMassivaPrimaNotaIntegrataResponse> {

	@Override
	public void handleResponse(ValidazioneMassivaPrimaNotaIntegrataResponse response) {
		final String methodName = "handleResponse";
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		// Messaggi non bloccanti
		for(Errore errore : response.getMessaggi()) {
			log.info(methodName, "Messaggio dal servizio: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.SUCCESSO);
		}
		
		if(!response.hasErrori()){
			inserisciDettaglioSuccesso();
		}
	}

}

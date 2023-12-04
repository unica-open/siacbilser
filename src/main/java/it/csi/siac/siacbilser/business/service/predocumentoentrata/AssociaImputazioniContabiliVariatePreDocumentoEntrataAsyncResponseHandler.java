/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse;

/**
 * The class AssociaImputazioniContabiliVariatePreDocumentoEntrataAsyncResponseHandler
 * @author Marchino Alessandro
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssociaImputazioniContabiliVariatePreDocumentoEntrataAsyncResponseHandler extends BilAsyncResponseHandler<AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse> {

	@Override
	public void handleResponse(AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse response) {
		final String methodName = "handleResponse";
		
		for(Messaggio messaggio : unNull(response.getMessaggi())) {
			log.info(methodName, "Messaggio: " + messaggio.getCodice() + " - "+ messaggio.getDescrizione());
			inserisciDettaglioOperazioneAsinc(messaggio.getCodice(), messaggio.getDescrizione(), Esito.SUCCESSO);
		}
		
		for(Errore errore : unNull(response.getErrori())) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		if(!response.hasErrori()){
			inserisciDettaglioSuccesso();
		}
	}
	
}

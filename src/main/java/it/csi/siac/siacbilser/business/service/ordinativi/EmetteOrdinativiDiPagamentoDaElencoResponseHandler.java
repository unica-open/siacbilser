/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.ordinativi;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElencoResponse;

/**
 * Handler per la risposnte dell'emissione ordinativi di pagamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/11/2014
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EmetteOrdinativiDiPagamentoDaElencoResponseHandler extends BilAsyncResponseHandler<EmetteOrdinativiDiPagamentoDaElencoResponse> {

	@Override
	public void handleResponse(EmetteOrdinativiDiPagamentoDaElencoResponse response) {
		final String methodName = "handleResponse";
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		for(Messaggio messaggio : response.getMessaggi()) {
			log.info(methodName, "Errore riscontrato: " + messaggio.getDescrizione());
			inserisciDettaglioOperazioneAsinc(messaggio.getCodice(), messaggio.getDescrizione(), Esito.SUCCESSO);
		}
		
		if(!response.hasErrori()) {
			inserisciDettaglioSuccesso();
		}
	}
	
}

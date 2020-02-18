/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registroiva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RecuperaProtocolloRegistroIvaResponse;

/**
 * Handler della response asincrona del servizio {@link RecuperaProtocolloRegistroIvaService}
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RecuperaProtocolloRegistroIvaAsyncResponseHandler extends BilAsyncResponseHandler<RecuperaProtocolloRegistroIvaResponse> {
	
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	
	@Override
	public void handleResponse(RecuperaProtocolloRegistroIvaResponse response) {
		final String methodName = "handleResponse";
		boolean endElab = elaborazioniManager.endElaborazione(RecuperaProtocolloRegistroIvaAsyncService.class, "registroIva.uid:" + response.getRegistroIva().getUid());
		if(endElab){
			log.info(methodName, "Elaborazione segnata come terminata.");
		}
		
		//Imposto come Messaggio la response del servizio.
		inserisciDettaglioOperazioneAsinc("ServiceResponse", "Response del servizio: " + RecuperaProtocolloRegistroIvaService.class.getSimpleName() + ".",
				Esito.SUCCESSO, null, JAXBUtility.marshall(response));
		
		
//		for(Messaggio messaggio : response.getMessaggi()) {
//			log.info(methodName, "Messaggio: " + messaggio.getCodice() + " - "+ messaggio.getDescrizione());
//			inserisciDettaglioOperazioneAsinc(messaggio.getCodice(), messaggio.getDescrizione(), Esito.SUCCESSO);
//		}
		
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		if(!response.hasErrori()){
			inserisciDettaglioSuccesso();
		}
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.documenti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siacbilser.business.service.documento.InserisceElenchiDocumentiService;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ElaboraFileDocumentiAsyncResponseHandler extends BilAsyncResponseHandler<ElaboraFileResponse> {

	@Override
	public void handleResponse(ElaboraFileResponse response) {
		final String methodName = "handleResponse";
		
		List<Messaggio> serviceResponses = findAndRemoveServiceResponse(response);
		for (Messaggio sr : serviceResponses){
			log.info(methodName, "ServiceResponse: " + sr.getCodice());
			inserisciDettaglioOperazioneAsinc(sr.getCodice(), "Response del servizio: "
					+InserisceElenchiDocumentiService.class.getSimpleName()+".",
					Esito.SUCCESSO, null, sr.getDescrizione());
		}
		
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		if(!response.hasErrori()) {
			
			for(Messaggio messaggio : response.getMessaggi()) {
				log.info(methodName, "Messaggio: " + messaggio.getCodice() + " - " + messaggio.getDescrizione());
				inserisciDettaglioOperazioneAsinc(messaggio.getCodice(), messaggio.getDescrizione(), Esito.SUCCESSO);
			}
			
			inserisciDettaglioSuccesso();
		}
		
	}
	
	private static List<Messaggio> findAndRemoveServiceResponse(ElaboraFileResponse response) {
		List<Messaggio> result = new ArrayList<Messaggio>();
		
		for(Iterator<Messaggio> i = response.getMessaggi().iterator() ; i.hasNext();){
			 Messaggio messaggio = i.next();
			 if(ElaboraFileDocumentiService.CODICE_MESSAGGIO_SERVICE_RESPONSE.equals(messaggio.getCodice())) {
				 i.remove();
				 result.add(messaggio);
			 }
		}
		return result;
	}
	

}

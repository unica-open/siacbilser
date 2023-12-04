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
import it.csi.siac.siacbilser.integration.dad.RegistroIvaDad;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AllineaProtocolloRegistroIvaResponse;

/**
 * Handler della response asincrona del servizio {@link AllineaProtocolloRegistroIvaService}
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AllineaProtocolloRegistroIvaAsyncResponseHandler extends BilAsyncResponseHandler<AllineaProtocolloRegistroIvaResponse> {
	
	@Autowired
	private RegistroIvaDad registroIvaDad;
	
	@Override
	public void handleResponse(AllineaProtocolloRegistroIvaResponse response) {
		final String methodName = "handleResponse";
		
		response.getRegistroIva().setFlagBloccato(Boolean.FALSE);
		registroIvaDad.aggiornaFlagBloccato(response.getRegistroIva());
		//Imposto come Messaggio la response del servizio.
		inserisciDettaglioOperazioneAsinc("ServiceResponse", "Response del servizio: " + AllineaProtocolloRegistroIvaService.class.getSimpleName() + ".",
				Esito.SUCCESSO, null, JAXBUtility.marshall(response));
		
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		if(!response.hasErrori()){
			inserisciDettaglioSuccesso();
		}
	}
	
}

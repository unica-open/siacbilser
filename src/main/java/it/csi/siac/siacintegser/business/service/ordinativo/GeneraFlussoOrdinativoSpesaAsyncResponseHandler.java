/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ordinativo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GeneraFlussoOrdinativoSpesaAsyncResponseHandler extends BilAsyncResponseHandler<ElaboraFileResponse> {

	@Override
	public void handleResponse(ElaboraFileResponse response) {
		final String methodName = "handleResponse";
		
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		if(!response.hasErrori()) {
			inserisciDettaglioSuccesso();
		}
	}

}

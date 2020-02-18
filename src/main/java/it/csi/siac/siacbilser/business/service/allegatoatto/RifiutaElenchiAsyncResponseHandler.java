/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchiResponse;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RifiutaElenchiAsyncResponseHandler extends BilAsyncResponseHandler<RifiutaElenchiResponse> {

	@Override
	public void handleResponse(RifiutaElenchiResponse response) {
		final String methodName = "handleResponse";
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		for(String string : response.getListaScarti()) {
			// TODO: mettere a posto
			inserisciDettaglioOperazioneAsinc("ELENCO_SCARTATO", string, Esito.FALLIMENTO);
		}
		
		if(!response.hasErrori()) {
			inserisciDettaglioSuccesso();
		}
	}

}

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
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerProvvisorioResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConvalidaAllegatoAttoPerProvvisorioAsyncResponseHandler extends BilAsyncResponseHandler<ConvalidaAllegatoAttoPerProvvisorioResponse> {

	@Override
	public void handleResponse(ConvalidaAllegatoAttoPerProvvisorioResponse response) {
		final String methodName = "handleResponse";
		
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		for(Messaggio messaggio : response.getMessaggi()) {
			log.info(methodName, "Messaggio: " + messaggio.getCodice() + " - "+ messaggio.getDescrizione());
			inserisciDettaglioOperazioneAsinc(messaggio.getCodice(), messaggio.getDescrizione(), Esito.SUCCESSO);
		}
		
		for(ElencoDocumentiAllegato elenco : response.getElenchiScartati()) {
			String msg = "Elenco scartato. Anno/Numero elenco: " + elenco.getAnno() + "/" + elenco.getNumero();
			log.info(methodName, msg);
			inserisciDettaglioOperazioneAsinc("ELENCO_SCARTATO", msg, Esito.FALLIMENTO);
		}
		
		for(Subdocumento<?, ?> subdoc : response.getSubdocumentiScartati()) {
			String msg = "Quota scartata. Quota numero: " + subdoc.getNumero() + (subdoc.getDocumento()!=null? " del documento: "+ subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "");
			log.info(methodName, msg);
			inserisciDettaglioOperazioneAsinc("QUOTA_SCARTATA", msg, Esito.FALLIMENTO);
		}
		
		if(!response.hasErrori()) {
			inserisciDettaglioSuccesso();
		}
	}

}

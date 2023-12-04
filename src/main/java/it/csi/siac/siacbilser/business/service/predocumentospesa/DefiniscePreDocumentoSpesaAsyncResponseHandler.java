/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;


/**
 * The Class DefiniscePreDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefiniscePreDocumentoSpesaAsyncResponseHandler extends BilAsyncResponseHandler<DefiniscePreDocumentoSpesaResponse> {

	@Override
	public void handleResponse(DefiniscePreDocumentoSpesaResponse response) {
		final String methodName = "handleResponse";
		
		for(PreDocumentoSpesa preDoc : response.getPredocumentiElaborati()) {
			log.info(methodName, "Elaborato predocumento " + preDoc.getUid());
			inserisciDettaglioOperazioneAsinc("AGGIORNATO", " predocumento: " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione(), Esito.SUCCESSO);
		}
		for(PreDocumentoSpesa preDoc : response.getPredocumentiSaltati()) {
			log.info(methodName, "Saltato predocumento " + preDoc.getUid());
			inserisciDettaglioOperazioneAsinc("SALTATO", " predocumento: " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione(), Esito.FALLIMENTO, "Non completo");
		}
		
		for(Messaggio messaggio : response.getMessaggi()) {
			log.info(methodName, "Messaggio: " + messaggio.getCodice() + " - "+ messaggio.getDescrizione());
			inserisciDettaglioOperazioneAsinc(messaggio.getCodice(), messaggio.getDescrizione(), Esito.SUCCESSO);
		}
		
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		inserisciDettaglioSuccesso();
	}

}

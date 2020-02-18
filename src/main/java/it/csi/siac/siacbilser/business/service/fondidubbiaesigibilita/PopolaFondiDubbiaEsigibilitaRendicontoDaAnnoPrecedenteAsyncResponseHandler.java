/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
/**
 * Response handler per il servizio asincrono di popolamento fondi a dubbia e difficile esazione, rendiconto, da anno precedente
 * @author Alessandro Marchino
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteAsyncResponseHandler extends BilAsyncResponseHandler<PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse> {

	//managers
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	
	@Override
	public void handleResponse(PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse response) {
		final String methodName = "handleResponse";
		 
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(response.getBilancio().getUid(), PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteAsyncService.class);
		//chiudo il record
		ElabKeys fondiRendicontoKeySelector = ElabKeys.POPOLA_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO;
		boolean endElab = elaborazioniManager.endElaborazione(eakh.creaElabServiceFromPattern(fondiRendicontoKeySelector), eakh.creaElabKeyFromPattern(fondiRendicontoKeySelector));
		if(endElab){
			//sono riuscito a chiudere il record correttamente
			log.info(methodName, "Elaborazione segnata come terminata.");
		}
		
		//se nel servizio si sono verificati degli errori, scrivo un record su db in modo tale che possano essere visualizzati da cruscotto
		for(Errore errore : response.getErrori()) {
			log.info(methodName, "Errore riscontrato: " + errore.getTesto());
			inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
		}
		
		//non si sono verificati degli errori: inserisco un record in modo tale che possano essere visualizzata tale informazione da cruscotto
		if(!response.hasErrori()){
			inserisciDettaglioSuccesso();
		}
	}

}

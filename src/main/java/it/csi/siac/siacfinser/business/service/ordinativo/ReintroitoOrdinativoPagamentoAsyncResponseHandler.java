/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.ordinativo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfinser.ReintroitoUtils;
import it.csi.siac.siacfinser.StringUtils;
import it.csi.siac.siacfinser.frontend.webservice.msg.ReintroitoOrdinativoPagamentoResponse;

/**
 * Handler della response asincrona del servizio {@link ReintroitoOrdinativoPagamentoService}
 * 
 * @author Domenico
 *
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ReintroitoOrdinativoPagamentoAsyncResponseHandler extends BilAsyncResponseHandler<ReintroitoOrdinativoPagamentoResponse> {
	
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	
	@Override
	public void handleResponse(ReintroitoOrdinativoPagamentoResponse response) {
		final String methodName = "handleResponse";
		
		String elabKey = ReintroitoUtils.buildElabKeyReintroito(response.getEnte(), response.getOrdPagReintroitato());
		
		//NB: endElaborazione e' volutamente su AggiornaAnagraficaVariazioneBilancioAsyncService non DefinisceAnagraficaVariazioneBilancioAsyncService in modo da 
		//    bloccare l'elaborazione contemporanea di aggiorna e di definisci.
		//boolean endElab = elaborazioniManager.endElaborazione(AggiornaAnagraficaVariazioneBilancioAsyncService.class, "variazione.uid:"+response.getVariazioneImportoCapitolo().getUid());
		
		boolean endElab = elaborazioniManager.endElaborazione(ReintroitoOrdinativoPagamentoAsyncService.class, elabKey);
		if(endElab){
			log.info(methodName, "Elaborazione segnata come terminata.");
		}
		
		//Imposto come Messaggio la response del servizio.
		inserisciDettaglioOperazioneAsinc("ServiceResponse","Response del servizio: "
				+ReintroitoOrdinativoPagamentoService.class.getSimpleName()+".", Esito.SUCCESSO, null, JAXBUtility.marshall(response));
		
		if(!StringUtils.isEmpty(response.getErrori())){
			//CONSIDERO FALLIMENTO LA PRESENZA DI ERRORI 
			//PER NON PERDERE INFORMAZIONE SE HO DIMENTICATO DI SETTERE isFallimento
			for(Errore errore : response.getErrori()) {
				log.info(methodName, "Errore riscontrato: " + errore.getTesto());
				inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.FALLIMENTO);
			}
		} else if(!response.isFallimento() && !StringUtils.isEmpty(response.getMessaggi())){
			//SE NON CI SONO ERRORI, E NON E' FALLIMENTO E CI SONO MESSAGGI:
			for(Errore errore : response.getMessaggi()) {
				log.info(methodName, "Messaggio: " + errore.getCodice() + " - "+ errore.getDescrizione());
				inserisciDettaglioOperazioneAsinc(errore.getCodice(), errore.getDescrizione(), Esito.SUCCESSO);
			}
		}
		
		if(!response.hasErrori()){
			inserisciDettaglioSuccesso();
		}
	}

}

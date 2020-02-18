/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;

/**
 * Wrapper async di {@link DefinisceAnagraficaVariazioneBilancioService}.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefinisceAnagraficaVariazioneBilancioAsyncService extends AsyncBaseService<DefinisceAnagraficaVariazioneBilancio,
																						DefinisceAnagraficaVariazioneBilancioResponse,
																						AsyncServiceRequestWrapper<DefinisceAnagraficaVariazioneBilancio>,
																						DefinisceAnagraficaVariazioneBilancioAsyncResponseHandler,
																						DefinisceAnagraficaVariazioneBilancioService> {

	@Autowired
	private ElaborazioniManager elaborazioniManager;
	@Autowired
	private VariazioniDad variazioniDad;
	
	private static final ElabKeys aggiornaVariazioneKeySelector = ElabKeys.AGGIORNA_VARIAZIONE;
	private static final ElabKeys definisciVariazioneKeySelector = ElabKeys.DEFINISCI_VARIAZIONE;

	@Override
	protected void init() {
		super.init();
		elaborazioniManager.init(req.getRichiedente().getAccount().getEnte(), req.getRichiedente().getOperatore().getCodiceFiscale());
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		String methodName = "checkServiceParam";
		service.checkServiceParam();
		log.debug(methodName, "Errori riscontrati: "+ service.getServiceResponse().getErrori());
		res.addErrori(service.getServiceResponse().getErrori());
	}
	
	@Override
	protected void preStartService() {
		//SIAC-7271
		
		checkEsistonoDefinizioniDeiCapitoliAssociatiAllaVariazione();
	}
	
	@Override
	protected void startService() {
		final String methodName = "startService";
		//SIAC-7271: abbastanza improbabile che ci sia un aggiornamento concorrente, lo lascio comunque qui perche' dai commenti sembra essere stata una scelta molto convinta.
		checkAggiornamentoVariazioneConcorrente();
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(originalRequest.getVariazioneImportoCapitolo().getUid(), this.getClass());
		String elabService = eakh.creaElabServiceFromPattern(definisciVariazioneKeySelector);
		checkEsistenzaDefinizioniConcorrentiSugliStessiCapitoli(elabService);
		
		try {								 	 
			elaborazioniManager.startElaborazione(eakh.creaElabServiceFromPattern(definisciVariazioneKeySelector), eakh.creaElabKeyFromPattern(definisciVariazioneKeySelector));
		} catch (ElaborazioneAttivaException eae){
			String msg = "L'elaborazione per la Variazione e' gia' in corso. Attendere il termine dell'elaborazione. [uid: "+ originalRequest.getVariazioneImportoCapitolo().getUid()+"]";
			log.error(methodName, msg, eae);
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg));
		}
		super.startService();
	}

	/**
	 * @param elabService
	 */
	private void checkEsistenzaDefinizioniConcorrentiSugliStessiCapitoli(String elabService) {
		List<String> elabKeysElaborazioniAttive = elaborazioniManager.getElabKeysElaborazioniAttive(elabService);
		if(elabKeysElaborazioniAttive == null || elabKeysElaborazioniAttive.isEmpty()) {
			return;
		}
		List<Integer> uids = new ArrayList<Integer>();
		for (String string : elabKeysElaborazioniAttive) {
			Integer uidByElabKey = ElaborazioniAttiveKeyHandler.getUidByElabKey(definisciVariazioneKeySelector, string);
			if(uidByElabKey != null && uidByElabKey.intValue() != originalRequest.getVariazioneImportoCapitolo().getUid()) {
				uids.add(uidByElabKey);
			}
		}
		if(uids.isEmpty()) {
			return;
		}
		boolean capitoliComuniAdaltreVariazioni = variazioniDad.isCapitoliComuniAdAltreVariazioni(originalRequest.getVariazioneImportoCapitolo().getUid(), uids);
		if(capitoliComuniAdaltreVariazioni) {
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore("Alcuni capitoli afferenti alla variazione sono oggetto di un'altra definizione di variazione attualemnente in corso. Attendere il termine dell'elaborazione."));
		}
	}

	/**
	 * 
	 */
	private void checkAggiornamentoVariazioneConcorrente() {
		//NB: startElaborazione e' volutamente su AggiornaAnagraficaVariazioneBilancioAsyncService non DefinisceAnagraficaVariazioneBilancioAsyncService in modo da 
		//    bloccare l'elaborazione contemporanea di aggiorna e di definisci.
		ElaborazioniAttiveKeyHandler eakhAgg = new ElaborazioniAttiveKeyHandler(originalRequest.getVariazioneImportoCapitolo().getUid(), AggiornaAnagraficaVariazioneBilancioAsyncService.class);
		boolean esisteElaborazioneAttiva = elaborazioniManager.esisteElaborazioneAttiva(eakhAgg.creaElabServiceFromPattern(aggiornaVariazioneKeySelector), eakhAgg.creaElabKeyFromPattern(aggiornaVariazioneKeySelector));
		if(esisteElaborazioneAttiva) {
			String msg = "Esiste un aggiornamento della variazione in corso. Attendere il termine dell'elaborazione. [uid: "+ originalRequest.getVariazioneImportoCapitolo().getUid()+"]";
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg));
		}
	}

	@Override
	protected void postStartService() {
		// Nothing to do
	}

	
	private void checkEsistonoDefinizioniDeiCapitoliAssociatiAllaVariazione() {
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(originalRequest.getVariazioneImportoCapitolo().getUid(), this.getClass());
//		elaborazioniManager.findElabService
	}
	
	public static void main(String[] args) {
//		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(Integer.valueOf(16), AggiornaAnagraficaVariazioneBilancioAsyncService.class);
//		ElabKeys e = ElabKeys.AGGIORNA_VARIAZIONE;
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(Integer.valueOf(16),AggiornaStatoDocumentoDiSpesaService.class,DocumentoEntrata.class, Ambito.AMBITO_FIN.name());
		ElabKeys e = ElabKeys.PRIMA_NOTA;
		String elabKeyRegex = eakh.creaElabServiceFromPattern(e);
////		String elabKeyRegex = eakh.creaElabKeyFromPattern(e);
		Pattern elabPattern = Pattern.compile("%UID%"); // e.getElabServicePattern()
		String baseString = e.getElabServiceBaseString(); 
//		
//
//		System.out.println(elabKeyRegex);
//		System.out.println(e.getElabServiceBaseString());
		
		Matcher matcher = elabPattern.matcher(e.getElabServiceBaseString());
		System.out.println(elabKeyRegex + " size: " + elabKeyRegex.length());
		System.out.println(baseString + " size: " + baseString.length());
		    // Check all occurrences
//	    while (matcher.find()) {
//	        System.out.print("Start index: " + matcher.start());
//	        System.out.print(" \nEnd index of pattern: " + matcher.end());
//	        System.out.println(" Found: " + matcher.group());
//	        int stop = baseString.length() - matcher.end();
//	        System.out.println(" \n stop: " + stop);
//	        String uu = StringUtils.substring(elabKeyRegex, matcher.start());
//	        System.out.println("substring: " + uu + " size: " + uu.length());
//	        String uu2 = StringUtils.substring(uu, -stop);
//	        System.out.println("substring2: " + uu2);
//	    }
	    
	    
	    Matcher matcherInverse = Pattern.compile("variazione.uid:(+\\d)").matcher(elabKeyRegex); 
	    while (matcherInverse.find()) {
//	        System.out.print("Start index: " + matcher.start());
//	        System.out.print(" \nEnd index of pattern: " + matcher.end());
	        System.out.println(" Found: " + matcher.group());
//	        int stop = baseString.length() - matcher.end();
//	        System.out.println(" \n stop: " + stop);
//	        String uu = StringUtils.substring(elabKeyRegex, matcher.start());
//	        System.out.println("substring: " + uu + " size: " + uu.length());
//	        String uu2 = StringUtils.substring(uu, -stop);
//	        System.out.println("substring2: " + uu2);
	    }
		
	}

}

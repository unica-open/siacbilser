/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita;

import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.AttributiBilancio;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Wrapper asincrono per il popolamento dei fondi a dubbia e difficile esazione, rendiconto, dall'anno precedente
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteAsyncService extends AsyncBaseService<
		//request del servizio sincrono
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente,
		//response del servizio sincrono
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteResponse,
		//request asincrona
		AsyncServiceRequestWrapper<PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente>,
		//response handler del servizio sincrono
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteAsyncResponseHandler,
		//servizio sincrono da mandare
		PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteService> {
	
	//DADs
	@Autowired
	private BilancioDad bilancioDad;
	//helper
	@Autowired
	private ElaborazioniManager elaborazioniManager;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//controllo di avere un id per il bilncio
		checkEntita(originalRequest.getBilancio(), "bilancio", false);
	}
	
	@Override
	@Transactional
	public AsyncServiceResponse executeService(AsyncServiceRequestWrapper<PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedente> serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//inizializzazione
		super.init();
		elaborazioniManager.init(req.getEnte(), req.getRichiedente().getOperatore().getCodiceFiscale());
	}
	
	@Override
	protected void preStartService() {
		final String methodName = "preStartService";
		
		//segno il fatto che sia sta partendo l'elaborazione
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(originalRequest.getBilancio().getUid(), this.getClass());
		ElabKeys fondiRendicontoKeySelector = ElabKeys.POPOLA_FONDI_DUBBIA_ESIGIBILITA_RENDICONTO;
		boolean esisteElaborazioneAttiva = elaborazioniManager.esisteElaborazioneAttiva(eakh.creaElabServiceFromPattern(fondiRendicontoKeySelector), eakh.creaElabKeyFromPattern(fondiRendicontoKeySelector));
		
		if(esisteElaborazioneAttiva){
			//l'elaborazione e' stata bloccata da un altro servizio: lancio un'eccezione e rollabacko per non avere dati sporchi
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("L'elaborazione per il bilancio e' gia' in corso. [uid: " + originalRequest.getBilancio().getUid() + "]"));
		}
		
		log.debug(methodName, "Controlli applicativi sincroni");
		// I due bilanci
		Bilancio bilancioAttuale = originalRequest.getBilancio();
		Bilancio bilancioPrecedente = ottieniBilancioPrecedente(bilancioAttuale);
		
		// Gli attributi
		AttributiBilancio attributiBilancioAttuale = ottieniAttributiBilancio(bilancioAttuale);
		AttributiBilancio attributiBilancioPrecedente = ottieniAttributiBilancio(bilancioPrecedente);
		
		//calcolo la distanza dall'ultimo bilancio approvato
		int deltaAnnoApprovato = attributiBilancioAttuale.getUltimoAnnoApprovato().intValue() - attributiBilancioPrecedente.getUltimoAnnoApprovato().intValue();
		precomputeGetters(deltaAnnoApprovato);
		
		log.debug(methodName, "Operazione asincrona in procinto di avviarsi...");
	}
	
	/**
	 * Ottiene il bilancio precedente
	 * @param bilancio il bilancio
	 * @return il bilancio relativo all'anno precedente
	 */
	private Bilancio ottieniBilancioPrecedente(Bilancio bilancio) {
		final String methodName = "ottiniBilancioPrecedente";
		Bilancio bilancioPrecedente = bilancioDad.getBilancioAnnoPrecedente(bilancio);
		if(bilancioPrecedente == null) {
			//non c'è nessun bilancio precedent, lancio un'eccezione
			throw new BusinessException("Nessun bilancio con anno precedente reperito per il bilancio con uid " + bilancio);
		}
		//ho trovato un bilancio: loggo e restituisco quanto trovato
		log.debug(methodName, "Trovato bilancio anno precedente con uid " + bilancioPrecedente.getUid());
		return bilancioPrecedente;
	}
	
	/**
	 * Ottiene gli attributi relativi al bilancio
	 * @param bilancio il bilancio da cui ottenere gli attributi
	 * @return gli attributi del bilancio
	 */
	private AttributiBilancio ottieniAttributiBilancio(Bilancio bilancio) {
		AttributiBilancio attributiBilancio = bilancioDad.getAttributiDettaglioByBilancio(bilancio);
		if(attributiBilancio == null) {
			//non esistono attributi di bilancio, lancio un eccezione
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("popolamento fondi a dubbia e difficile esazione da anno precedente",
					"il bilancio con uid " + bilancio.getUid() + " non ha attributi relativi ai fondi registrati"));
		}
		//restituisco quanto trovato
		return attributiBilancio;
	}
	
	/**
	 * Precomputa i getter da utilizzare
	 */
	private void precomputeGetters(int deltaAnnoApprovato) {
		final String methodName = "precomputeGetters";
		int j = 0;
		Class<AccantonamentoFondiDubbiaEsigibilitaRendiconto> clazz = AccantonamentoFondiDubbiaEsigibilitaRendiconto.class;
		
		for(int i = 0; i <= 4; i++) {
			if(i >= deltaAnnoApprovato) {
				Method attuale = getSetter(clazz, i);
				Method precedente = getGetter(clazz, j++);
				log.debug(methodName, "Calcolata coppia " + attuale.getName() + " <== " + precedente.getName());
			}
		}
	}
	
	/**
	 * Ottiene il getter
	 * @param clazz la classe avente il metodo
	 * @param anno il delta
	 * @return il getter
	 */
	private Method getSetter(Class<AccantonamentoFondiDubbiaEsigibilitaRendiconto> clazz, int anno) {
		//seleziono il nome setter tra setPercentualeAccantonamentoFondi, setPercentualeAccantonamentoFondi1, setPercentualeAccantonamentoFondi2...
		String name = anno == 0 ? "setPercentualeAccantonamentoFondi" : ("setPercentualeAccantonamentoFondi" + anno);
		try {
			//prendo il metodo dalla classe 
			return clazz.getMethod(name, BigDecimal.class);
		} catch (SecurityException e) {
			throw new IllegalStateException("Errore nel recupero del metodo 'setPercentualeAccantonamentoFondi' per l'anno " + anno + ": errore di sicurezza", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Errore nel recupero del metodo 'setPercentualeAccantonamentoFondi' per l'anno " + anno + ": metodo non esistente", e);
		}
	}
	
	/**
	 * Ottiene il setter
	 * @param clazz la classe avente il metodo
	 * @param anno il delta
	 * @return il setter
	 */
	private Method getGetter(Class<AccantonamentoFondiDubbiaEsigibilitaRendiconto> clazz, int anno) {
		//seleziono il  nome del getter tra getPercentualeAccantonamentoFondi, getPercentualeAccantonamentoFondi1, getPercentualeAccantonamentoFondi2...
		String name = anno == 0 ? "getPercentualeAccantonamentoFondi" : ("getPercentualeAccantonamentoFondi" + anno);
		
		try {
			//prendo il metodo
			return clazz.getMethod(name);
		} catch (SecurityException e) {
			throw new IllegalStateException("Errore nel recupero del metodo 'getPercentualeAccantonamentoFondi' per l'anno " + anno + ": errore di sicurezza", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Errore nel recupero del metodo 'getPercentualeAccantonamentoFondi' per l'anno " + anno + ": metodo non esistente", e);
		}
	}
	
	@Override
	protected void startService() {
		final String methodName = "startService";
		//prima di far partire il servizio devo essere sicuro che non ci siano elaborazioni in corso
		try{
			//metto un record per segnare che l'elaborazione
			elaborazioniManager.startElaborazione(PopolaFondiDubbiaEsigibilitaRendicontoDaAnnoPrecedenteAsyncService.class, "bilancio.uid:" + originalRequest.getBilancio().getUid());
		} catch (ElaborazioneAttivaException eae){
			//c'e' gia' un'operazione dello stesso tipo e con gli stessi dati: non posso continuare
			String msg = "L'elaborazione fondi dubbia esigibilita' per il bilancio e' gia' in corso. [uid: " + originalRequest.getBilancio().getUid() + "]";
			log.error(methodName, msg, eae);
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg));
		}
		//non ci sono elaborazioni concorrenti in corso, posso far partire il servizio
		super.startService();
	}

	@Override
	protected void postStartService() {
		final String methodName = "preStartService";
		//per ora non faccio nullo
		log.debug(methodName, "Operazione asincrona avviata");
	}
	
}

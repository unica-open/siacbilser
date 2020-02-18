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
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente;
import it.csi.siac.siacbilser.frontend.webservice.msg.PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
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
 * Wrapper asincrono per il popolamento dei fondi a dubbia e difficile esazione dall'anno precedente
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteAsyncService extends AsyncBaseService<PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente,
		//response del servizio sincrono		
		PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteResponse,
		//request servizio asincrono
		AsyncServiceRequestWrapper<PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente>,
		//gestore della risposta del servizio sincrono
		PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteAsyncResponseHandler,
		//servizio sincrono
		PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteService> {
	
	//DADs
	@Autowired
	private BilancioDad bilancioDad;
	
	//HELPERS o  managers
	@Autowired
	private ElaborazioniManager elaborazioniManager;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//il bilancio e obbligatorio
		checkEntita(originalRequest.getBilancio(), "bilancio", false);
	}
	
	@Override
	@Transactional
	public AsyncServiceResponse executeService(AsyncServiceRequestWrapper<PopolaFondiDubbiaEsigibilitaDaAnnoPrecedente> serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//inizializzazione dei campi
		super.init();
		elaborazioniManager.init(req.getEnte(), req.getRichiedente().getOperatore().getCodiceFiscale());
	}
	
	@Override
	protected void preStartService() {
		final String methodName = "preStartService";
		
		//segno che sto avviando una elaborazione
		ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(originalRequest.getBilancio().getUid(), this.getClass());
		ElabKeys fondiKeySelector = ElabKeys.POPOLA_FONDI_DUBBIA_ESIGIBILITA;
		
		boolean esisteElaborazioneAttiva = elaborazioniManager.esisteElaborazioneAttiva(eakh.creaElabServiceFromPattern(fondiKeySelector), eakh.creaElabKeyFromPattern(fondiKeySelector));
		if(esisteElaborazioneAttiva){
			//c'e' gia' un'elaborazione concorrente: non posso proseguire
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("L'elaborazione per il bilancio e' gia' in corso. [uid: " + originalRequest.getBilancio().getUid() + "]"));
		}
		
		log.debug(methodName, "Controlli applicativi sincroni");
		// I due bilanci
		Bilancio bilancioAttuale = originalRequest.getBilancio();
		Bilancio bilancioPrecedente = ottieniBilancioPrecedente(bilancioAttuale);
		
		// Gli attributi
		AttributiBilancio attributiBilancioAttuale = ottieniAttributiBilancio(bilancioAttuale);
		AttributiBilancio attributiBilancioPrecedente = ottieniAttributiBilancio(bilancioPrecedente);
		
		//calcolo il delta tra l'anno corrente e l'ultimo anno approvato
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
		//carico il bilancio precedente
		Bilancio bilancioPrecedente = bilancioDad.getBilancioAnnoPrecedente(bilancio);
		if(bilancioPrecedente == null) {
			//non esiste uin bilancio precedente a quello attuale, lancio un'eccezione
			throw new BusinessException("Nessun bilancio con anno precedente reperito per il bilancio con uid " + bilancio);
		}
		log.debug(methodName, "Trovato bilancio anno precedente con uid " + bilancioPrecedente.getUid());
		//restituisco il bilancio appena caricato
		return bilancioPrecedente;
	}
	
	/**
	 * Ottiene gli attributi relativi al bilancio
	 * @param bilancio il bilancio da cui ottenere gli attributi
	 * @return gli attributi del bilancio
	 */
	private AttributiBilancio ottieniAttributiBilancio(Bilancio bilancio) {
		//carico gli attributi di bilancio
		AttributiBilancio attributiBilancio = bilancioDad.getAttributiDettaglioByBilancio(bilancio);
		if(attributiBilancio == null) {
			//non vi sono attributi di bilancio
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("popolamento fondi a dubbia e difficile esazione da anno precedente",
					"il bilancio con uid " + bilancio.getUid() + " non ha attributi relativi ai fondi registrati"));
		}
		//restituisco gli attributi appena caricati
		return attributiBilancio;
	}
	
	/**
	 * Precomputa i getter da utilizzare
	 */
	private void precomputeGetters(int deltaAnnoApprovato) {
		final String methodName = "precomputeGetters";
		int j = 0;
		Class<AccantonamentoFondiDubbiaEsigibilita> clazz = AccantonamentoFondiDubbiaEsigibilita.class;
		
		//ma poi uso questo da qualche parte????
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
	private Method getSetter(Class<AccantonamentoFondiDubbiaEsigibilita> clazz, int anno) {
		String name = anno == 0 ? "setPercentualeAccantonamentoFondi" : ("setPercentualeAccantonamentoFondi" + anno);
		try {
			//prendo il metodo dalla classe
			return clazz.getMethod(name, BigDecimal.class);
		} catch (SecurityException e) {
			//trasformo un'eccezione di sicurezza in una IllegalStateException
			throw new IllegalStateException("Errore nel recupero del metodo 'setPercentualeAccantonamentoFondi' per l'anno " + anno + ": errore di sicurezza", e);
		} catch (NoSuchMethodException e) {
			//quando non e' presente il metodo, lancio una IllegalStateException
			throw new IllegalStateException("Errore nel recupero del metodo 'setPercentualeAccantonamentoFondi' per l'anno " + anno + ": metodo non esistente", e);
		}
	}
	
	/**
	 * Ottiene il setter
	 * @param clazz la classe avente il metodo
	 * @param anno il delta
	 * @return il setter
	 */
	private Method getGetter(Class<AccantonamentoFondiDubbiaEsigibilita> clazz, int anno) {
		String name = anno == 0 ? "getPercentualeAccantonamentoFondi" : ("getPercentualeAccantonamentoFondi" + anno);
		try {
			return clazz.getMethod(name);
		} catch (SecurityException e) {
			//trasformo un'eccezione di sicurezza in una IllegalStateException
			throw new IllegalStateException("Errore nel recupero del metodo 'getPercentualeAccantonamentoFondi' per l'anno " + anno + ": errore di sicurezza", e);
		} catch (NoSuchMethodException e) {
			//quando non e' presente il metodo, lancio una IllegalStateException
			throw new IllegalStateException("Errore nel recupero del metodo 'getPercentualeAccantonamentoFondi' per l'anno " + anno + ": metodo non esistente", e);
		}
	}
	
	@Override
	protected void startService() {
		final String methodName = "startService";
		//segno l'elaborazione in corso
		try{
			elaborazioniManager.startElaborazione(PopolaFondiDubbiaEsigibilitaDaAnnoPrecedenteAsyncService.class, "bilancio.uid:" + originalRequest.getBilancio().getUid());
		} catch (ElaborazioneAttivaException eae){
			//esiste un'elaborazione concorrente: devo attendere che finisca
			String msg = "L'elaborazione fundi dubbia esigibilita' per il bilancio e' gia' in corso. [uid: " + originalRequest.getBilancio().getUid() + "]";
			log.error(methodName, msg, eae);
			throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg));
		}
		super.startService();
	}

	@Override
	protected void postStartService() {
		final String methodName = "preStartService";
		log.debug(methodName, "Operazione asincrona avviata");
	}
	
}

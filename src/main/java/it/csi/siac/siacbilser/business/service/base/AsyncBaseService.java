/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;

import java.util.Date;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AzioneDad;
import it.csi.siac.siacbilser.integration.dad.ThresholdDad;
import it.csi.siac.siacbilser.model.ElabThresholdKey;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.OperazioneAsincronaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.AggiornaOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.AggiornaOperazioneAsincResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciDettaglioOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciDettaglioOperazioneAsincResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsinc;
import it.csi.siac.siaccorser.frontend.webservice.msg.InserisciOperazioneAsincResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.StatoOperazioneAsincronaEnum;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe base dei servizi che richiamati in asincrono.
 *
 * @author Domenico Lisi
 * 
 * @param <REQ> the generic Request type
 * @param <RES> the generic Response type
 * @param <S> the generic Service type
 */
public abstract class AsyncBaseService<REQ extends ServiceRequest,
                                       RES extends ServiceResponse,
                                       REQW extends AsyncServiceRequestWrapper<REQ>,
                                       RH extends BilAsyncResponseHandler<RES>,
                                       S extends ExtendedBaseService<REQ,RES>> extends ExtendedBaseService<REQW, AsyncServiceResponse> {

	@Autowired
	protected ApplicationContext appCtx;
	@Autowired
	private AzioneDad azioneDad;
	@Autowired
	private ThresholdDad thresholdDad;
	
	protected S service;
	protected RH responseHandler;
	protected REQ originalRequest;
	
	public static final int TIMEOUT = 3600;
	
	//protected StatoOperazioneAsincronaEnum statoFinaleOperazione = StatoOperazioneAsincronaEnum.STATO_OPASINC_CONCLUSA;	
	
	@Autowired
	protected OperazioneAsincronaService operazioneAsincronaService;
	

	private Integer idOperazioneAsincrona;
	
	private boolean requireElaborationOnDedicatedQueue = false;
	
		
	@Override
	protected final void execute() {
		final String methodName = "execute";
		
		preStartService();
		requireElaborationOnDedicatedQueue = mayRequireElaborationOnDedicatedQueue();
		
		InserisciOperazioneAsincResponse resIOA = inserisciOperazioneAsinc();
		idOperazioneAsincrona = resIOA.getIdOperazione();
		log.info(methodName, "Operazione asincrona inserita nel sistema. [idOperazioneAsincrona: " + idOperazioneAsincrona + "]");
		res.setIdOperazioneAsincrona(idOperazioneAsincrona);
		
		aggiornaOperazioneAsinc(StatoOperazioneAsincronaEnum.STATO_OPASINC_AVVIATA);
		
		initResponseHandler();
		startService();
		postStartService();
	}
	
	/**
	 * Controllo se si possa richiedere che il servizio venga eseguito in un esecutore dedicato
	 * <br/>
	 * Questo serve ad evitare che servizi con dati particolarmente onerosi vengano chiamati sulla stessa coda dei servizi meno onerosi
	 * @return se il servizio possa richiedere di essere eseguito in un esecutore dedicato
	 */
	protected boolean mayRequireElaborationOnDedicatedQueue() {
		return false;
	}

	@Override
	protected void checkServiceParamBase() throws ServiceParamError {
		checkNotNull(req, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ServiceRequest"));
		checkNotNull(req.getRequest(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ServiceRequest"));
		
		originalRequest = req.getRequest();
		req.setRichiedente(originalRequest.getRichiedente());
		originalRequest.setUserSessionInfo(req.getUserSessionInfo());
		
		//Inizializzazione del Service sottostante.
		initServiceBase();
		
		checkRichiedente();
		checkServiceParam();
		if(res.hasErrori()){
			throw new ServiceParamError(null);
		}
	}
	
	/**
	 * Inizializza la logica di controllo necessaria per inizializzare l'elaborazione.
	 * 
	 */
	protected abstract void preStartService();
		
	/**
	 * Invoca l'elaborazione del reportHandler
	 */	
	protected void startService(){
		final String methodName = "startService";
		log.info(methodName, "Sto per avviare l'operazione Asincrona. [idOperazioneAsincrona: " + idOperazioneAsincrona + "]");
		// Se richiedo elaborazioni sequenziali
		if(requireElaborationOnDedicatedQueue) {
			log.info(methodName, "Operazione asincrona in avvio su executor per elaborazioni di lunga durata");
			serviceExecutor.executeServiceLongExecutionTimePoolAsync(service, originalRequest, responseHandler);
		} else {
			log.info(methodName, "Operazione asincrona in avvio su executor standard");
			/*Future<RES> future =*/ serviceExecutor.executeServiceTxRequiresNewAsync(service, originalRequest, responseHandler);
		}
		
		log.info(methodName, "Operazione Asincrona avviata correttamente. [idOperazioneAsincrona: " + idOperazioneAsincrona + "]");
	}
	
	/**
	 * Viene invocato appena dopo aver avviato l'esecuzione del servizio.
	 * 
	 */
	protected abstract void postStartService();
	

	/**
	 * Estende il check del Richiedente aggiungendo il check dell'Ente, dell'Account e dell'azioneRichiesta
	 */
	@Override
	protected void checkRichiedente() throws ServiceParamError {
		super.checkRichiedente();
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(req.getAccount(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("account"));
		checkCondition(req.getAccount().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid account"));
		
		// TODO: cancellarli in ottica di ricavare l'azione su servizio
		checkNotNull(req.getAzioneRichiesta(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("azione richiesta"));
		checkNotNull(req.getAzioneRichiesta().getAzione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("azione azione richiesta"));
		checkCondition(req.getAzioneRichiesta().getAzione().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid azione azione richiesta"));
	}
	
	/**
	 * Metodo di base per inizializzare il Service sottostante.
	 */
	protected void initServiceBase() {
		final String methodName = "initServiceBase";
		log.debug(methodName, "invoked");
		Class<S> serviceClass = getServiceClass();
		try{
			service = appCtx.getBean(Utility.firstLetterToLowerCase(serviceClass.getSimpleName()), serviceClass);
			if(service == null) {
				log.error(methodName, "Impossibile ottenere il Service! Nessun component tovato per "+serviceClass.getName());
				throw new NoSuchBeanDefinitionException(serviceClass, "Bean ottenuto null.");
			}
			RES resService = service.instantiateNewRes();
			service.setServiceResponse(resService);
			service.setServiceRequest(req.getRequest());
		} catch(BeansException be) {
			log.error(methodName, "Impossibile ottenere il Service! Errore nell'ottenimento del component:  "+serviceClass.getName() + " :"+be.getMessage(),be);
			throw be;
		}
		
		initService();
	}
	
	/**
	 * Viene richiamato quando il servizio viene inizializzato.
	 */
	protected void initService(){
		
	}
	
	/**
	 * Metodo di base per inizializzare del Service.
	 */
	protected void initResponseHandler() {
		final String methodName = "initResponseHandler";
		log.debug(methodName, "invoked");
		Class<RH> asyncResponseHandlerClass = getResponseHandlerClass();
		try{
			responseHandler = appCtx.getBean(Utility.toDefaultBeanName(asyncResponseHandlerClass), asyncResponseHandlerClass);
		} catch(BeansException be) {
			log.error(methodName, "Impossibile ottenere l'AsyncResponseHandler! Errore nell'ottenimento del component:  "+asyncResponseHandlerClass.getName() + " :"+be.getMessage(),be);
			throw be;
		}
		if(service==null){
			log.error(methodName, "Impossibile ottenere l'AsyncResponseHandler! Nessun component trovato per "+asyncResponseHandlerClass.getName());
		}
		responseHandler.setEnte(req.getEnte());
		responseHandler.setRichiedente(req.getRichiedente());
		responseHandler.setIdOperazioneAsincrona(this.idOperazioneAsincrona);
		//asyncResponseHandler.setAzioneRichiesta(req.getAzioneRichiesta());
	}




	/**
	 * Specifica la classe finale del Servizio.
	 * Di default viene istanziata quella espressa nel GenericType, ma sovrascrivendo il metodo &egrave; 
	 * possibile specificare una classe.
	 * 
	 * @return
	 */
	protected Class<S> getServiceClass() {
		String methodName = "getServiceClass";
		try {
			return Utility.findGenericType(this.getClass(), AsyncBaseService.class, 4);
		} catch (Exception t) {
			String msg = "Errore risoluzione classe Service.";
			log.error(methodName, msg, t);
			throw new IllegalArgumentException(msg, t);
		}
	}
	/**
	 * Specifica la classe finale dell' async response hadler.
	 * Di default viene istanziata quella espressa nel GenericType, ma sovrascrivendo il metodo &egrave; 
	 * possibile specificare una classe.
	 * 
	 * @return
	 */
	protected Class<RH> getResponseHandlerClass() {	
		String methodName = "getResponseHandlerClass";
		
		try {
			return Utility.findGenericType(this.getClass(), AsyncBaseService.class, 3);
		} catch (Exception t) {
			String msg = "Errore risoluzione classe Service. ";
			log.error(methodName, msg, t);
			throw new IllegalArgumentException(msg, t);
		}
	}

	
	
	
	
	
	protected InserisciOperazioneAsincResponse inserisciOperazioneAsinc(){
		
		InserisciOperazioneAsinc reqIOS = popolaRequestOperazioneAsincrona();
		
		InserisciOperazioneAsincResponse resAgg = operazioneAsincronaService.inserisciOperazioneAsinc(reqIOS);
		checkServiceResponseFallimento(resAgg);
		return resAgg;
	}

	protected InserisciOperazioneAsinc popolaRequestOperazioneAsincrona() {
		InserisciOperazioneAsinc reqIOS = new InserisciOperazioneAsinc();
		
		reqIOS.setAccount(req.getAccount());
		reqIOS.setDataOra(new Date());
		reqIOS.setEnte(req.getEnte());
		reqIOS.setRichiedente(originalRequest.getRichiedente());
		reqIOS.setUserSessionInfo(req.getUserSessionInfo());
		
		if(getNomeAzione() == null) { 
			//XXX fallback temporaneo, in attesa che tutte le classi che estendono AsyncBaseService specifichino il nomeAzione.
			reqIOS.setAzioneRichiesta(req.getAzioneRichiesta());
		} else {
			AzioneRichiesta azioneRichiesta = creaAzioneRichiesta();
			reqIOS.setAzioneRichiesta(azioneRichiesta);
		}
		return reqIOS;
	}
	
	private AzioneRichiesta creaAzioneRichiesta() {
		final String methodName = "creaAzioneRichiesta";
		AzioneRichiesta ar = new AzioneRichiesta();
		
		// TODO: qui solo per sicurezza
		azioneDad.setEnte(req.getEnte());
		String nomeAzione = getNomeAzione();
		log.debug(methodName, "Caricamento azione per nome " + nomeAzione);
		Azione azione = azioneDad.getAzioneByNome(nomeAzione);
		log.debug(methodName, "Azione caricata per nome " + nomeAzione + ": " + (azione != null ? azione.getUid() : "null"));
		
		if(azione == null) {
			throw new BusinessException("Nessuna azione configurata con nome " + nomeAzione);
		}
		
		ar.setAzione(azione);
		
		return ar;
	}

	/**
	 * Aggiorna lo stato dell'operazione asincrona
	 * @param stato
	 */
	protected void aggiornaOperazioneAsinc(StatoOperazioneAsincronaEnum stato) {
		AggiornaOperazioneAsinc reqAgg = new AggiornaOperazioneAsinc();
		reqAgg.setRichiedente(originalRequest.getRichiedente());
		reqAgg.setIdOperazioneAsinc(this.idOperazioneAsincrona);		
		reqAgg.setIdEnte(req.getEnte().getUid());
		reqAgg.setUserSessionInfo(req.getUserSessionInfo());

		reqAgg.setStato(stato);

		AggiornaOperazioneAsincResponse resAgg = operazioneAsincronaService.aggiornaOperazioneAsinc(reqAgg);
		checkServiceResponseFallimento(resAgg);
	}
	
	
	/**
	 * Aggiorna lo stato dell'operazione asincrona
	 * @param stato
	 */
	protected void inserisciDettaglioOperazioneAsinc(String codice, String descrizione, Esito esito) {
		inserisciDettaglioOperazioneAsinc(codice, descrizione, esito, null);
	}
	
	/**
	 * Aggiorna lo stato dell'operazione asincrona e specifica un messaggio di errore.
	 * @param stato
	 */
	protected void inserisciDettaglioOperazioneAsinc(String codice, String descrizione, Esito esito, String msgErrore) {
		InserisciDettaglioOperazioneAsinc reqdett = new InserisciDettaglioOperazioneAsinc();		
		
		reqdett.setIdOperazioneAsincrona(this.idOperazioneAsincrona);
		reqdett.setCodice(codice);
		reqdett.setDescrizione(descrizione);
		reqdett.setRichiedente(req.getRichiedente());
		reqdett.setIdEnte(req.getEnte().getUid());
		reqdett.setEsito(esito.name());
		reqdett.setMsgErrore(msgErrore);
		
		InserisciDettaglioOperazioneAsincResponse resIDOA = operazioneAsincronaService.inserisciDettaglioOperazioneAsinc(reqdett);
		checkServiceResponseFallimento(resIDOA);
	}
	
	
	protected String getNomeAzione() {
		// Implementazione di default
		return null;// req.getAzioneRichiesta().getAzione().getNome();
	}
	
	protected Long getThreshold() {
		final String methodName = "getThreshold";
		ElabThresholdKey key = ElabThresholdKey.byClass(service.getClass());
		log.debug(methodName, "Threshold key: " + key);
		Long threshold = thresholdDad.findThresholdByCode(key);
		log.debug(methodName, "Threshold value: " + threshold);
		return threshold;
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.base.invoker.AsyncServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.invoker.BaseServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.invoker.BeanClassServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.invoker.BeanNameAndClassServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.invoker.LongExecutionTimePoolAsyncServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.invoker.SingleThreadPoolAsyncServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.invoker.TxNotSupportedBaseServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.invoker.TxRequiresNewBaseServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.invoker.TxRequiresNewBeanClassServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.invoker.TxRequiresNewBeanNameAndClassServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.invoker.TxRequiresNewTimeoutBaseServiceInvoker;
import it.csi.siac.siacbilser.business.service.base.responsehandler.NotErroreResponseHandler;
import it.csi.siac.siacbilser.business.service.base.responsehandler.NotNullResponseHandler;
import it.csi.siac.siacbilser.business.service.base.responsehandler.RicercaSuccessResponseHandler;
import it.csi.siac.siacbilser.business.service.base.responsehandler.SuccessResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.BaseService;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.cache.KeyAdapter;
import it.csi.siac.siaccommonser.business.service.base.cache.ServiceResponseCache;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Permette di eseguire un servizio specificando il tipo di propagazione della transazione.
 * 
 * @author Domenico Lisi
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
//@Primary
public class ServiceExecutor {
	
	@Autowired
	protected ApplicationContext appCtx;
	
	protected String serviceName;
	private static final LogSrvUtil log = new LogSrvUtil(ServiceExecutor.class);

	/**
	 * Chache delle response dei servizi relativa ad un singolo thread, ovvero ad una singola invocazione di un servizio.
	 */
	private static final ThreadLocal<Map<String, ServiceResponseCache>> threadLocalCache = new ThreadLocal<Map<String, ServiceResponseCache>>(){
		protected Map<String,ServiceResponseCache> initialValue() {
			return new HashMap<String, ServiceResponseCache>();
		}
	};
	
	/**
	 * Instantiates a new service executor.
	 */
	public ServiceExecutor() {
		// Nothing
	}
	
	/**
	 * Gets the service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets the service name.
	 *
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets the app ctx.
	 *
	 * @return the appCtx
	 */
	public ApplicationContext getAppCtx() {
		return appCtx;
	}

	/**
	 * Sets the app ctx.
	 *
	 * @param appCtx the appCtx to set
	 */
	public void setAppCtx(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}

	
	/**
	 * Web Listener per ripulire {@link ServiceExecutor#threadLocalCache} ad ogni terminazione della request.
	 * @author Domenico
	 */
	@WebListener
	public static class RequestListener implements ServletRequestListener {
		
		private LogSrvUtil log = new LogSrvUtil(getClass());
		
		@Override
		public void requestInitialized(ServletRequestEvent sre) { 
			System.out.println();
		}

		@Override
		public void requestDestroyed(ServletRequestEvent sre) {
			final String methodName = "requestDestroyed";
			log.debug(methodName, "remove cache for thread: "+ Thread.currentThread().getName());
			ServiceExecutor.cleanThreadLocalCache();
		}

	}
	
	/**
	 * Rimuove la cache dei servizi per il thread corrente
	 */
	public static void cleanThreadLocalCache() {
		threadLocalCache.remove();
		log.debug("cleanThreadLocalCache", "threadLocalCache removed for thread: "+ Thread.currentThread().getName());
	}
	
	/**
	 *  
	 * Esegue il check di una risposta di un servizio.
	 * Nel caso venga invocato un servizio di un altro modulo rispetto al chiamante è possibile passare la serviceResponse ottenuta a
	 * questo metodo di check che in caso si sia verificato un errore con il codice passato in input solleva l'eccezione al chiamante.
	 *
	 * @author Domenico Lisi
	 * @param <ERES> the generic type
	 * @param externalServiceResponse the external service response
	 * @param codiciErrore the codici errore
	 */
	public <ERES extends ServiceResponse> void checkServiceResponseErrore(ERES externalServiceResponse, String... codiciErrore) {
		ResponseHandler<ERES> rh = new NotErroreResponseHandler<ERES>(getServiceName(), codiciErrore);
		rh.handleResponseBase(externalServiceResponse);
	}
	
	/**
	 *  
	 * Esegue il check di una risposta di un servizio.
	 * Nel caso esito sia Fallimento e non si sia verificato nessuno degli errori passati nel parametro codiciErroreDaEscludere 
	 * viene sollevata l'eccezione.
	 *
	 * @author Domenico Lisi
	 * @param <ERES> the generic type
	 * @param externalServiceResponse the external service response
	 * @param codiciErroreDaEscludere codici di errori per il quale NON sollevare l'eccezione
	 */
	public <ERES extends ServiceResponse> void checkServiceResponseFallimento(ERES externalServiceResponse, String... codiciErroreDaEscludere) {
		
		ResponseHandler<ERES> rh = new SuccessResponseHandler<ERES>(getServiceName(), codiciErroreDaEscludere);
		rh.handleResponseBase(externalServiceResponse);
	}
	
	/**
	 * Check service response fallimento ricerca.
	 *
	 * @param <ERES> the generic type
	 * @param externalServiceResponse the external service response
	 */
	public <ERES extends ServiceResponse> void checkServiceResponseFallimentoRicerca(ERES externalServiceResponse) {
		ResponseHandler<ERES> rh = new RicercaSuccessResponseHandler<ERES>(getServiceName());
		rh.handleResponseBase(externalServiceResponse);
	}
	
	/**
	 * Gets the service name.
	 *
	 * @param <ERES> the generic type
	 * @param externalServiceResponse the external service response
	 * @return the service name
	 */
	protected <ERES extends ServiceResponse> String getServiceName(ERES externalServiceResponse) {
		return externalServiceResponse.getClass().getSimpleName().replaceAll("(Response)$","")+"Service";
	}
	
	/* **************************************************************************
	 * **************************** BASE ****************************************
	 * **************************************************************************/
	
	/**
	 * Esegue un generico servizio specificando uno specifico handler della response.
	 * 
	 * Questo &egrave; il pi&ugrave; generico tra tutti gli executeService.
	 * 
	 * @param service
	 * @param req
	 * @param responseHandler
	 * @return response del servizio
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeService(ServiceInvoker<EREQ,ERES> service, EREQ req, ResponseHandler<ERES> responseHandler){
		final String methodName = "executeService";
		
		ERES externalServiceResponse = null;
		
		try {
			initLogUserSessionInfo(req);   
			externalServiceResponse = service.invokeService(req);
		} catch(RuntimeException re){
			log.error(methodName, "Errore di runtime nell'esecuzione del servizio.", re);
			throw re;
		} finally {
			//Eseguo sempre e comunque il responseHandler.
			try {
				if(responseHandler == null) {
					responseHandler = new NotNullResponseHandler<ERES>();
				}
				
				responseHandler.handleResponseBase(externalServiceResponse);
			} catch (RuntimeException re) {
				log.error(methodName, "Errore nella gestione della response.", re);
				if(externalServiceResponse!=null) {
					//c'era una response ed ho fallito a gestirla.
					//sollevo l'eccezione.
					throw re;
				}
			}
		}
		return externalServiceResponse;
	}

	/**
	 * Esegue un servizio esterno all'interno di questo servizio.
	 * Nel caso in cui il servizio richiamato risponda con almeno uno dei codici di errore passato come parametro 
	 * viene sollevata una eccezione runtime di tipo ExecuteExternalServiceException.
	 * Così facendo di default anche questo servizio (ovvero il chiamante) viene interrotto (ovviamente a meno che non intercetti l'eccezione e la gestisca).
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param codiciErrore the codici errore
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeService(BaseService<EREQ,ERES> service, EREQ req, String... codiciErrore){
		return executeService(new BaseServiceInvoker<EREQ,ERES>(service), req, codiciErrore);
	}
	
	/**
	 * Execute service.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param codiciErrore the codici errore
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeService(Class<? extends BaseService<EREQ,ERES>> service, EREQ req, String... codiciErrore){
		return executeService(new BeanClassServiceInvoker<EREQ,ERES>(service,appCtx), req, codiciErrore);
	}
	
	/**
	 * Esecuzione di base di un qualunque servizio sincrono.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service invocatore del servizio.
	 * @param req request del servizio
	 * @return response del servizio
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeService(ServiceInvoker<EREQ,ERES> service, EREQ req, String... codiciErrore) {
		
		return this.executeService(service, req, new NotErroreResponseHandler<ERES>(getServiceName(), codiciErrore));
	}
	
	/* **************************************************************************
	 * **************************** SUCCESS *************************************
	 * **************************************************************************/
	
	/**
	 * Esegue un servizio esterno all'interno di questo servizio.
	 * Stesso comportamento di executeService ma in più solleva l'eccezione anche per Esito.Fallimento solo se tra gli errori non è presente nessuno dei 
	 * codiciErroreDaEscludere passato in input
	 *
	 * @author Domenico Lisi
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceSuccess(BaseService<EREQ,ERES> service, EREQ req, String... codiciErroreDaEscludere){
		return executeServiceSuccess(new BaseServiceInvoker<EREQ,ERES>(service), req, codiciErroreDaEscludere);
	}
	
	/**
	 * Execute service success.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceSuccess(ServiceInvoker<EREQ,ERES> service, EREQ req, String... codiciErroreDaEscludere){
		return this.executeService(service, req, new SuccessResponseHandler<ERES>(getServiceName(), codiciErroreDaEscludere));
	}
	
	/**
	 * Esegue un servizio esterno all'interno di questo servizio.
	 * Stesso comportamento di executeService ma in più solleva l'eccezione anche per Esito.Fallimento solo se tra gli errori non è presente nessuno dei 
	 * codiciErroreDaEscludere passato in input
	 *
	 * @author Domenico Lisi
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceSuccess(Class<? extends BaseService<EREQ,ERES>> serviceClass, EREQ req, String... codiciErroreDaEscludere){
		return executeServiceSuccess(new BeanClassServiceInvoker<EREQ,ERES>(serviceClass, appCtx), req, codiciErroreDaEscludere);
	}
	
	/**
	 * Execute service success.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceName the service name
	 * @param serviceClass the service class
	 * @param req the req
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceSuccess(String serviceName, Class<? extends BaseService<EREQ,ERES>> serviceClass, EREQ req, String... codiciErroreDaEscludere){
		return executeServiceSuccess(new BeanNameAndClassServiceInvoker<EREQ,ERES>(serviceName, serviceClass, appCtx), req, codiciErroreDaEscludere);
	}
	
	/**
	 * Esegue un servizio esterno all'interno di questo servizio.
	 * Stesso comportamento di executeExternalServiceSuccess ma solleva l'eccezione per Esito.Fallimento solo se non c'è un errore di tipo ENTITA_NON_TROVATA
	 *
	 * @author Domenico Lisi
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeExternalServiceSuccessRicerca(BaseService<EREQ,ERES> service, EREQ req){
		return executeService(new BaseServiceInvoker<EREQ,ERES>(service), req, new RicercaSuccessResponseHandler<ERES>(getServiceName()));
	}
	
	/* **************************************************************************
	 * *********************** THREAD LOCAL CACHED ******************************
	 * **************************************************************************/
	
	/**
	 * Execute service thread local cached success.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param keyAdapter the key adapter
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceThreadLocalCachedSuccess(Class<? extends BaseService<EREQ,ERES>> serviceClass, EREQ req, KeyAdapter<EREQ> keyAdapter, String... codiciErroreDaEscludere){
		return executeServiceThreadLocalCachedSuccess(new BeanClassServiceInvoker<EREQ,ERES>(serviceClass, appCtx), req, keyAdapter, codiciErroreDaEscludere);
	}
	
	/**
	 * Execute service thread local cached success.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param keyAdapter the key adapter
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceThreadLocalCachedSuccess(BaseService<EREQ,ERES> service, EREQ req, KeyAdapter<EREQ> keyAdapter,  String... codiciErroreDaEscludere){
		return executeServiceThreadLocalCachedSuccess(new BaseServiceInvoker<EREQ,ERES>(service), req, keyAdapter, codiciErroreDaEscludere);
	}
	
	/**
	 * Execute service thread local cached success.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param keyAdapter the key adapter
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceThreadLocalCachedSuccess(ServiceInvoker<EREQ,ERES> service, EREQ req, KeyAdapter<EREQ> keyAdapter,  String... codiciErroreDaEscludere){
		
		String cacheKey = req.getClass().getName() + keyAdapter.computeKey(req);
		
		Map<String, ServiceResponseCache> cache = threadLocalCache.get();
		if(cache.containsKey(cacheKey)){
			ServiceResponseCache serviceResponseCache = cache.get(cacheKey);
			serviceResponseCache.hit();
			return serviceResponseCache.getServiceResponse();
		}
		
		long currentTimeMillis = System.currentTimeMillis();
		
		ERES externalServiceResponse = this.executeService(service, req, new SuccessResponseHandler<ERES>(getServiceName(), codiciErroreDaEscludere));
		
		ServiceResponseCache serviceResponseCache = new ServiceResponseCache(externalServiceResponse, System.currentTimeMillis()-currentTimeMillis);
		cache.put(cacheKey, serviceResponseCache);
		return externalServiceResponse;
	}
	
	/**
	 * Execute service thread local cached.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param keyAdapter the key adapter
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceThreadLocalCached(Class<? extends BaseService<EREQ,ERES>> serviceClass, EREQ req, KeyAdapter<EREQ> keyAdapter, String... codiciErrore){
		return executeServiceThreadLocalCached(serviceClass, req, keyAdapter, new NotErroreResponseHandler<ERES>(getServiceName(), codiciErrore));
	}
	
	/**
	 * Execute service thread local cached.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param keyAdapter the key adapter
	 * @param responseHandler the response handler
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceThreadLocalCached(Class<? extends BaseService<EREQ,ERES>> serviceClass, EREQ req, KeyAdapter<EREQ> keyAdapter, ResponseHandler<ERES> responseHandler){
		return executeServiceThreadLocalCached(new BeanClassServiceInvoker<EREQ,ERES>(serviceClass, appCtx), req, keyAdapter, responseHandler);
	}
	
	/**
	 * Execute service thread local cached.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param keyAdapter the key adapter
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceThreadLocalCached(BaseService<EREQ,ERES> service, EREQ req, KeyAdapter<EREQ> keyAdapter, String... codiciErrore){
		return executeServiceThreadLocalCached(service, req, keyAdapter, new NotErroreResponseHandler<ERES>(getServiceName(), codiciErrore));
	}
	
	/**
	 * Execute service thread local cached.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param keyAdapter the key adapter
	 * @param responseHandler the response handler
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceThreadLocalCached(BaseService<EREQ,ERES> service, EREQ req, KeyAdapter<EREQ> keyAdapter, ResponseHandler<ERES> responseHandler){
		return executeServiceThreadLocalCached(new BaseServiceInvoker<EREQ,ERES>(service), req, keyAdapter, responseHandler);
	}
	
	/**
	 * Execute service thread local cached.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param keyAdapter the key adapter
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceThreadLocalCached(ServiceInvoker<EREQ,ERES> service, EREQ req, KeyAdapter<EREQ> keyAdapter){
		return executeServiceThreadLocalCached(service, req, keyAdapter, null);
	}
	
	/**
	 * Execute service thread local cached .
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param keyAdapter the key adapter
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceThreadLocalCached(ServiceInvoker<EREQ,ERES> service, EREQ req, KeyAdapter<EREQ> keyAdapter, ResponseHandler<ERES> responseHandler){
		
		String cacheKey = req.getClass().getName() + keyAdapter.computeKey(req);
		
		Map<String, ServiceResponseCache> cache = threadLocalCache.get();
		if(cache.containsKey(cacheKey)){
			ServiceResponseCache serviceResponseCache = cache.get(cacheKey);
			serviceResponseCache.hit();
			return serviceResponseCache.getServiceResponse();
		}
		
		long currentTimeMillis = System.currentTimeMillis();
		
		ERES externalServiceResponse = this.executeService(service, req, responseHandler); //new NotErroreResponseHandler<ERES>(getServiceName(), codiciErrore)
		
		ServiceResponseCache serviceResponseCache = new ServiceResponseCache(externalServiceResponse, System.currentTimeMillis()-currentTimeMillis);
		cache.put(cacheKey, serviceResponseCache);
		return externalServiceResponse;
	}
	
	/* **************************************************************************
	 * ****************************** ASYNC *************************************
	 * **************************************************************************/
	
	/**
	 * 
	 * @param service
	 * @param req
	 * @param responseHandler
	 */
	@Async
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceTxRequiresNewAsync(ExtendedBaseService<EREQ,ERES> service, EREQ req, ResponseHandler<ERES> responseHandler){
		executeService(new TxRequiresNewBaseServiceInvoker<EREQ,ERES>(service), req, responseHandler);
	}
	
	/**
	 * 
	 * @param service
	 * @param req
	 * @param responseHandler
	 */
	@Async
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceTxRequiresNewTimeoutAsync(ExtendedBaseService<EREQ,ERES> service, EREQ req, ResponseHandler<ERES> responseHandler){
		executeService(new TxRequiresNewTimeoutBaseServiceInvoker<EREQ,ERES>(service), req, responseHandler);
	}
	
	/**
	 * Execute service tx not supported async.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param asyncResponseHandler the async response handler
	 */
	@Async
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> /*Future<ERES>*/ void executeServiceTxNotSupportedAsync(ExtendedBaseService<EREQ,ERES> service, EREQ req, BilAsyncResponseHandler<ERES> responseHandler){
		executeService(new TxNotSupportedBaseServiceInvoker<EREQ,ERES>(service), req, responseHandler);
	}
	
	/**
	 * Esecuzione di base di un generico servizio in asincrono.
	 * Le modalità di esecuzione asincrona sono impostate dall'{@link AsyncServiceInvoker} passato come paretro. 
	 * 
	 * {@link AsyncServiceInvoker} inoltre, permette di specificare un eventuale l'handler della response.
	 * 
	 * @param service
	 * @param req
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceAsync(AsyncServiceInvoker<EREQ,ERES> service, EREQ req) {
		initLogUserSessionInfo(req);
		service.invokeService(req);
	}
	
	/**
	 * Execute service tx requires new async.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param asyncResponseHandler the async response handler
	 */
	@Async
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceTxRequiresNewAsync(ServiceInvoker<EREQ,ERES> service, EREQ req, ResponseHandler<ERES> asyncResponseHandler){
		initLogUserSessionInfo(req);
		ERES externalServiceResponse = service.invokeService(req);
		asyncResponseHandler.handleResponseBase(externalServiceResponse);
	}
	
	/**
	 * Execute service tx requires new async.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param appCtx the app ctx
	 * @param req the req
	 * @param asyncResponseHandler the async response handler
	 */
	@Async
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceTxRequiresNewAsync(Class<? extends BaseService<EREQ,ERES>> serviceClass, EREQ req, ResponseHandler<ERES> asyncResponseHandler){
		executeServiceTxRequiresNewAsync(new BeanClassServiceInvoker<EREQ,ERES>(serviceClass,appCtx), req, asyncResponseHandler);
	}
	
	/* **************************************************************************
	 * ************************** TX REQUIRES NEW *******************************
	 * **************************************************************************/
	
	/**
	 * Execute service tx requires new.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param codiciErrore the codici errore
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceTxRequiresNew(ExtendedBaseService<EREQ,ERES> service, EREQ req, String... codiciErrore){
		return executeService(new TxRequiresNewBaseServiceInvoker<EREQ,ERES>(service), req, codiciErrore);
	}
	
	/**
	 * Execute service tx not supported.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param codiciErrore the codici errore
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceTxNotSupported(ExtendedBaseService<EREQ,ERES> service, EREQ req, String... codiciErrore){
		return executeService(new TxNotSupportedBaseServiceInvoker<EREQ,ERES>(service), req, codiciErrore);
	}
	
	/**
	 * Execute service success tx requires new.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres 
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceSuccessTxRequiresNew(ExtendedBaseService<EREQ,ERES> service, EREQ req, String... codiciErroreDaEscludere){
		return executeServiceSuccess(new TxRequiresNewBaseServiceInvoker<EREQ,ERES>(service), req, codiciErroreDaEscludere);
	}
	
	/**
	 * Execute service success tx requires new.
	 * Permette di specificare il ResponseHandler.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres 
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceTxRequiresNew(ExtendedBaseService<EREQ,ERES> service, EREQ req,ResponseHandler<ERES> responseHandler){
		return executeService(new TxRequiresNewBaseServiceInvoker<EREQ,ERES>(service), req, responseHandler);
	}
	
	/**
	 * Execute service tx requires new.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param responseHandler the response handler
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceTxRequiresNew(Class<? extends ExtendedBaseService<EREQ,ERES>> serviceClass, EREQ req, ResponseHandler<ERES> responseHandler){
		return executeService(new TxRequiresNewBeanClassServiceInvoker<EREQ,ERES>(serviceClass,appCtx), req, responseHandler);
	}
	
	/**
	 * Execute service success tx requires new.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceSuccessTxRequiresNew(Class<? extends ExtendedBaseService<EREQ,ERES>> serviceClass, EREQ req, String... codiciErroreDaEscludere){
		return executeServiceSuccess(new TxRequiresNewBeanClassServiceInvoker<EREQ,ERES>(serviceClass,appCtx), req, codiciErroreDaEscludere);
	}
	
	/**
	 * Execute service success tx requires new.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceName the service name
	 * @param serviceClass the service class
	 * @param req the req
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceSuccessTxRequiresNew(String serviceName, Class<? extends ExtendedBaseService<EREQ,ERES>> serviceClass, EREQ req, String... codiciErroreDaEscludere){
		return executeServiceSuccess(new TxRequiresNewBeanNameAndClassServiceInvoker<EREQ,ERES>(serviceName, serviceClass, appCtx), req, codiciErroreDaEscludere);
	}
	
	/* **************************************************************************
	 * ************************** TX NOT SUPPORTED ******************************
	 * **************************************************************************/
	
	/**
	 * Execute service success tx not supported.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param service the service
	 * @param req the req
	 * @param codiciErroreDaEscludere the codici errore da escludere
	 * @return the eres
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> ERES executeServiceSuccessTxNotSupported(ExtendedBaseService<EREQ,ERES> service, EREQ req, String... codiciErroreDaEscludere){
		return executeServiceSuccess(new TxNotSupportedBaseServiceInvoker<EREQ,ERES>(service), req, codiciErroreDaEscludere);
	}
	
	/* **************************************************************************
	 * ************************ SINGLE THREAD POOL ******************************
	 * **************************************************************************/
	
	/**
	 * Esegue un servizio in modalit&agrave; asincrona dopo l'intervallo di tempo specificato nel parametro startTimeout.
	 * Viene eseguito un solo thread alla volta. Più esecuzioni vengono accodate.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param asyncResponseHandler gestore della response del servizio
	 * @param startTimeout millisecondi dopo il quale far partire l'esecuzione.
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceSingleThreadPoolAsync(Class<? extends BaseService<EREQ,ERES>> serviceClass, EREQ req, ResponseHandler<ERES> responseHandler, long startTimeout) {
		
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new BeanClassServiceInvoker<EREQ,ERES>(serviceClass,appCtx);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new SingleThreadPoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, responseHandler, startTimeout, appCtx);
		
		executeServiceAsync(asyncServiceInvoker, req);
	}
	
	/**
	 * Esegue il servizio in un pool con un solo thread.
	 * I servizi vengono accodati e ne viene eseguito uno solo alla volta.
	 * E' possibile specificare un gestore della response del servizio per eseguire eventuali operazioni con la response.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param responseHandler gestore della response del servizio.
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceSingleThreadPoolAsync(Class<? extends BaseService<EREQ,ERES>> serviceClass, EREQ req, ResponseHandler<ERES> responseHandler) {
		
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new BeanClassServiceInvoker<EREQ,ERES>(serviceClass,appCtx);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new SingleThreadPoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, responseHandler, appCtx);
		
		executeServiceAsync(asyncServiceInvoker, req);
	}
	
	/**
	 * Esegue il servizio in un pool con un solo thread.
	 * I servizi vengono accodati e ne viene eseguito uno solo alla volta.
	 * E' possibile specificare l'intervallo di tempo dopo il quale far partire il servizio.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass classe del servizio
	 * @param req request del servizio
	 * @param startTimeout tempo in millisecondi dopo il quale far partire serivizio.
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceSingleThreadPoolAsync(Class<? extends BaseService<EREQ,ERES>> serviceClass, EREQ req, long startTimeout) {
		
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new BeanClassServiceInvoker<EREQ,ERES>(serviceClass,appCtx);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new SingleThreadPoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, null, startTimeout, appCtx);
		
		executeServiceAsync(asyncServiceInvoker, req);
	}
	
	/**
	 * Esegue il servizio in un pool con un solo thread.
	 * I servizi vengono accodati e ne viene eseguito uno solo alla volta.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass classe del servizio
	 * @param req request del servizio
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceSingleThreadPoolAsync(Class<? extends BaseService<EREQ,ERES>> serviceClass, EREQ req) {
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new BeanClassServiceInvoker<EREQ,ERES>(serviceClass,appCtx);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new SingleThreadPoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, null, appCtx);
		executeServiceAsync(asyncServiceInvoker, req);
	}
	
	/* **************************************************************************
	 * ************************ LONG EXECUTION TIME POOL ************************
	 * **************************************************************************/
	
	/**
	 * Esegue un servizio in modalit&agrave; asincrona dopo l'intervallo di tempo specificato nel parametro startTimeout.
	 * Viene eseguito un solo thread alla volta. Più esecuzioni vengono accodate.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param asyncResponseHandler gestore della response del servizio
	 * @param startTimeout millisecondi dopo il quale far partire l'esecuzione.
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceLongExecutionTimePoolAsync(Class<? extends ExtendedBaseService<EREQ,ERES>> serviceClass, EREQ req, ResponseHandler<ERES> responseHandler, long startTimeout) {
		
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new TxRequiresNewBeanClassServiceInvoker<EREQ,ERES>(serviceClass,appCtx);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new LongExecutionTimePoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, responseHandler, startTimeout, appCtx);
		
		executeServiceAsync(asyncServiceInvoker, req);
	}
	
	/**
	 * Esegue il servizio in un pool con un solo thread.
	 * I servizi vengono accodati e ne viene eseguito uno solo alla volta.
	 * E' possibile specificare un gestore della response del servizio per eseguire eventuali operazioni con la response.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param responseHandler gestore della response del servizio.
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceLongExecutionTimePoolAsync(Class<? extends ExtendedBaseService<EREQ,ERES>> serviceClass, EREQ req, ResponseHandler<ERES> responseHandler) {
		
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new TxRequiresNewBeanClassServiceInvoker<EREQ,ERES>(serviceClass,appCtx);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new LongExecutionTimePoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, responseHandler, appCtx);
		
		executeServiceAsync(asyncServiceInvoker, req);
	}
	
	/**
	 * Esegue il servizio in un pool con un solo thread.
	 * I servizi vengono accodati e ne viene eseguito uno solo alla volta.
	 * E' possibile specificare l'intervallo di tempo dopo il quale far partire il servizio.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass classe del servizio
	 * @param req request del servizio
	 * @param startTimeout tempo in millisecondi dopo il quale far partire serivizio.
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceLongExecutionTimePoolAsync(Class<? extends ExtendedBaseService<EREQ,ERES>> serviceClass, EREQ req, long startTimeout) {
		
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new TxRequiresNewBeanClassServiceInvoker<EREQ,ERES>(serviceClass,appCtx);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new LongExecutionTimePoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, null, startTimeout, appCtx);
		
		executeServiceAsync(asyncServiceInvoker, req);
	} 
	
	/**
	 * Esegue il servizio in un pool con un solo thread.
	 * I servizi vengono accodati e ne viene eseguito uno solo alla volta.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass classe del servizio
	 * @param req request del servizio
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceLongExecutionTimePoolAsync(Class<? extends ExtendedBaseService<EREQ,ERES>> serviceClass, EREQ req) {
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new TxRequiresNewBeanClassServiceInvoker<EREQ,ERES>(serviceClass,appCtx);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new LongExecutionTimePoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, null, appCtx);
		executeServiceAsync(asyncServiceInvoker, req);
	}
	
	/**
	 * Esegue un servizio in modalit&agrave; asincrona dopo l'intervallo di tempo specificato nel parametro startTimeout.
	 * Viene eseguito un solo thread alla volta. Più esecuzioni vengono accodate.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param asyncResponseHandler gestore della response del servizio
	 * @param startTimeout millisecondi dopo il quale far partire l'esecuzione.
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceLongExecutionTimePoolAsync(ExtendedBaseService<EREQ,ERES> service, EREQ req, ResponseHandler<ERES> responseHandler, long startTimeout) {
		
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new TxRequiresNewBaseServiceInvoker<EREQ,ERES>(service);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new LongExecutionTimePoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, responseHandler, startTimeout, appCtx);
		
		executeServiceAsync(asyncServiceInvoker, req);
	}
	
	/**
	 * Esegue il servizio in un pool con un solo thread.
	 * I servizi vengono accodati e ne viene eseguito uno solo alla volta.
	 * E' possibile specificare un gestore della response del servizio per eseguire eventuali operazioni con la response.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass the service class
	 * @param req the req
	 * @param responseHandler gestore della response del servizio.
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceLongExecutionTimePoolAsync(ExtendedBaseService<EREQ,ERES> service, EREQ req, ResponseHandler<ERES> responseHandler) {
		
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new TxRequiresNewBaseServiceInvoker<EREQ,ERES>(service);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new LongExecutionTimePoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, responseHandler, appCtx);
		
		executeServiceAsync(asyncServiceInvoker, req);
	}
	
	/**
	 * Esegue il servizio in un pool con un solo thread.
	 * I servizi vengono accodati e ne viene eseguito uno solo alla volta.
	 * E' possibile specificare l'intervallo di tempo dopo il quale far partire il servizio.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass classe del servizio
	 * @param req request del servizio
	 * @param startTimeout tempo in millisecondi dopo il quale far partire serivizio.
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceLongExecutionTimePoolAsync(ExtendedBaseService<EREQ,ERES> service, EREQ req, long startTimeout) {
		
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new TxRequiresNewBaseServiceInvoker<EREQ,ERES>(service);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new LongExecutionTimePoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, null, startTimeout, appCtx);
		
		executeServiceAsync(asyncServiceInvoker, req);
	}
	
	/**
	 * Esegue il servizio in un pool con un solo thread.
	 * I servizi vengono accodati e ne viene eseguito uno solo alla volta.
	 *
	 * @param <EREQ> the generic type
	 * @param <ERES> the generic type
	 * @param serviceClass classe del servizio
	 * @param req request del servizio
	 */
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> void executeServiceLongExecutionTimePoolAsync(ExtendedBaseService<EREQ,ERES> service, EREQ req) {
		ServiceInvoker<EREQ, ERES> beanClassServiceInvoker = new TxRequiresNewBaseServiceInvoker<EREQ,ERES>(service);
		AsyncServiceInvoker<EREQ,ERES> asyncServiceInvoker = new LongExecutionTimePoolAsyncServiceInvoker<EREQ, ERES>(beanClassServiceInvoker, null, appCtx);
		executeServiceAsync(asyncServiceInvoker, req);
	}

	
	private <EREQ extends ServiceRequest> void initLogUserSessionInfo(EREQ req) {
		
		if (req.getUserSessionInfo() == null) {
			System.out.println("UserSessionInfo null - service name = " + serviceName);
			System.out.println("trying with log thread local UserSessionInfo = " + log.getUserSessionInfo());

			req.setUserSessionInfo(log.getUserSessionInfo());	
		
			return;
		}
		
		log.initializeUserSessionInfo(req.getUserSessionInfo());
	}
	

}

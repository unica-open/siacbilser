/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.invoker;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Invoca un servizio su un altro thread tramite l'{@link AsyncTaskExecutor} specificato.
 * E' possibile estendere questa classe per utilizzare diversi {@code AsyncTaskExecutor} configurati nel contesto di Spring.
 * Inoltre si può specificare un handler della response: {@link ResponseHandler} (facoltativo).
 * 
 * @author Domenico Lisi
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public class AsyncServiceInvoker<REQ extends ServiceRequest, RES extends ServiceResponse> implements ServiceInvoker<REQ, RES> {
	protected LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	protected AsyncTaskExecutor asyncTaskExecutor;
	
	private ServiceInvoker<REQ, RES> serviceInvoker;
	private ResponseHandler<RES> asyncResponseHandler;
	private long startTimeout;

	public AsyncServiceInvoker(AsyncTaskExecutor asyncTaskExecutor, ServiceInvoker<REQ, RES> serviceInvoker) {
		this(asyncTaskExecutor, serviceInvoker,null);
	}
	
	public AsyncServiceInvoker(AsyncTaskExecutor asyncTaskExecutor, ServiceInvoker<REQ, RES> serviceInvoker, ResponseHandler<RES> asyncResponseHandler) {
		this(asyncTaskExecutor, serviceInvoker,asyncResponseHandler,AsyncTaskExecutor.TIMEOUT_IMMEDIATE);
	}
	
	public AsyncServiceInvoker(AsyncTaskExecutor asyncTaskExecutor, ServiceInvoker<REQ, RES> serviceInvoker, ResponseHandler<RES> asyncResponseHandler, long startTimeout) {
		this.asyncTaskExecutor = asyncTaskExecutor;
		this.serviceInvoker = serviceInvoker;
		this.asyncResponseHandler = asyncResponseHandler;
		this.startTimeout = startTimeout;
	}
	

	@Override
	public RES invokeService(final REQ req) {
		final String methodName = "invokeService";
		Runnable runnable = createRunnable(req);
		
		asyncTaskExecutor.execute(runnable, startTimeout);
		
		log.debug(methodName, "Service invoked in async mode! Returning null. [implementor Class: " + asyncTaskExecutor.getClass().getName() + "]");
		return null;
	}
	
	protected Runnable createRunnable(final REQ req) {
		return new AsyncRunnable<REQ, RES>(req, asyncTaskExecutor, serviceInvoker, asyncResponseHandler, startTimeout);
	}
	
	protected static class AsyncRunnable<REQ extends ServiceRequest, RES extends ServiceResponse> implements Runnable {
		
		private final LogSrvUtil log = new LogSrvUtil(this.getClass());

		private final REQ req;
		private final AsyncTaskExecutor asyncTaskExecutor;
		private final ServiceInvoker<REQ, RES> serviceInvoker;
		private final ResponseHandler<RES> asyncResponseHandler;
		private final long startTimeout;
		
		protected AsyncRunnable(REQ req, AsyncTaskExecutor asyncTaskExecutor, ServiceInvoker<REQ, RES> serviceInvoker,
				ResponseHandler<RES> asyncResponseHandler, long startTimeout) {
			this.req = req;
			this.asyncTaskExecutor = asyncTaskExecutor;
			this.serviceInvoker = serviceInvoker;
			this.asyncResponseHandler = asyncResponseHandler;
			this.startTimeout = startTimeout;
		}
		
		@Override
		public void run() {
			log.initializeUserSessionInfo(req.getUserSessionInfo());
			
			startTimeoutWorkaround();
			
			RES externalServiceResponse = null;
			try {
				externalServiceResponse = invokeService(req);
			} finally {
				try {
					if(asyncResponseHandler != null){
						asyncResponseHandler.handleResponseBase(externalServiceResponse);
					}
				} finally {
					ServiceExecutor.cleanThreadLocalCache();
				}
				
			}
		}
		
		protected RES invokeService(REQ req) {
			return serviceInvoker.invokeService(req);
		}

		private void startTimeoutWorkaround() {
			final String methodName = "startTimeoutWorkaround";
			
			if(startTimeout > 0 && asyncTaskExecutor instanceof ThreadPoolTaskExecutor){
				//ThreadPoolTaskExecutor non supporta lo startTimeout! Faccio un semplice sleep prima di iniziare.
				//In questo sleep tengo impegnato cmq un thread nel pool! Non e' una soluzione ottimale!
				//La soluzione migliore e' utilizzare asyncTaskExecutor che supportano 
				String threadName = Thread.currentThread().getName();
				log.warn(methodName, "Stai utilizzando ThreadPoolTaskExecutor che non supporta lo startTimeout! Per usare correttamente lo startTimeout bisogna configurare un AsyncTaskExecutor che lo supporti.  [thread name:" + threadName+"]");
				
				try {
					log.info(methodName, "AsyncTaskExecutor. Sleeping for " + startTimeout + " ms ... [thread name:" + threadName+"]");
					Thread.sleep(startTimeout);
					log.info(methodName, "AsyncTaskExecutor. Sleep terminato. [thread name:" + threadName+"]");
				} catch (InterruptedException e) {
					log.warn(methodName, "AsyncTaskExecutor. Sleep di " + startTimeout + " ms interrotto in anticipo! [thread name:" + threadName +"]", e);
				}
			}
		}
	}

}

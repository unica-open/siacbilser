/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.invoker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Invoca un BaseService in modo asincrono accodandoli su un thread pool con un solo thread.
 * 
 * @author Domenico Lisi
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public class SingleThreadPoolAsyncServiceInvoker<REQ extends ServiceRequest, RES extends ServiceResponse> extends AsyncServiceInvoker<REQ, RES> {
	
	private static final String SINGLE_THREAD_EXECUTOR_BEAN = "singleThreadExecutor";
	
	
	@Autowired
	@Qualifier(SINGLE_THREAD_EXECUTOR_BEAN)
	private AsyncTaskExecutor asyncSingleThreadTaskExecutor;

	public SingleThreadPoolAsyncServiceInvoker(ServiceInvoker<REQ, RES> serviceInvoker, ResponseHandler<RES> asyncResponseHandler) {
		this(serviceInvoker, asyncResponseHandler, AsyncTaskExecutor.TIMEOUT_IMMEDIATE);
	}
	
	public SingleThreadPoolAsyncServiceInvoker(ServiceInvoker<REQ, RES> serviceInvoker, ResponseHandler<RES> asyncResponseHandler, long startTimeout) {
		super(null, serviceInvoker,asyncResponseHandler, startTimeout);
		
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		super.asyncTaskExecutor = asyncSingleThreadTaskExecutor;
	}
	
	
	public SingleThreadPoolAsyncServiceInvoker(ServiceInvoker<REQ, RES> serviceInvoker, ResponseHandler<RES> asyncResponseHandler, ApplicationContext appCtx) {
		this(serviceInvoker, asyncResponseHandler, AsyncTaskExecutor.TIMEOUT_IMMEDIATE, appCtx);
	}
	
	public SingleThreadPoolAsyncServiceInvoker(ServiceInvoker<REQ, RES> serviceInvoker, ResponseHandler<RES> asyncResponseHandler, long startTimeout, ApplicationContext appCtx) {
		super(null, serviceInvoker,asyncResponseHandler, startTimeout);
		
		super.asyncTaskExecutor = appCtx.getBean(SINGLE_THREAD_EXECUTOR_BEAN, AsyncTaskExecutor.class);
	}
	
	
	


}

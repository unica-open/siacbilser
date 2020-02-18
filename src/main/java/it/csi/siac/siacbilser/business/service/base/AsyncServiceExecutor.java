/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Permette di eseguire un servizio specificando in modo asincrono.
 * 
 * @author Domenico Lisi
 */
@Service
public class AsyncServiceExecutor {
	
	@Autowired
	private ServiceExecutor serviceExecutor;
	
	
	@Async
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> /*Future<ERES>*/ void executeServiceAsync(ExtendedBaseService<EREQ,ERES> service, EREQ req, BilAsyncResponseHandler<ERES> asyncResponseHandler){
		ERES res = serviceExecutor.executeServiceTxNotSupported(service,req);
		asyncResponseHandler.handleResponseBase(res);
		//return new AsyncResult<ERES>(res);
	}
	
	@Async
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> /*Future<ERES>*/ void executeServiceSuccessAsync(ExtendedBaseService<EREQ,ERES> service, EREQ req, BilAsyncResponseHandler<ERES> asyncResponseHandler){
		 ERES res = serviceExecutor.executeServiceSuccessTxNotSupported(service,req);
		 asyncResponseHandler.handleResponseBase(res);
		 //return new AsyncResult<ERES>(res);
	}
	
	
	@Async
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> Future<ERES> executeServiceAsync(ExtendedBaseService<EREQ,ERES> service, EREQ req){
		ERES res = serviceExecutor.executeServiceTxNotSupported(service,req);
		return new AsyncResult<ERES>(res);
	}
	
	@Async
	public <EREQ extends ServiceRequest, ERES extends ServiceResponse> Future<ERES> executeServiceSuccessAsync(ExtendedBaseService<EREQ,ERES> service, EREQ req){
		 ERES res = serviceExecutor.executeServiceSuccessTxNotSupported(service,req);
		 return new AsyncResult<ERES>(res);
	}
	
	
	
}

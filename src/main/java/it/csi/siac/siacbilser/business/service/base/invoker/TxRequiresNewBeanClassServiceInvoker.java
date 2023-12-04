/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.invoker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Invoca un BaseService a partire dalla classe del bean di Spring.
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public class TxRequiresNewBeanClassServiceInvoker<REQ extends ServiceRequest, RES extends ServiceResponse> implements ServiceInvoker<REQ, RES> {
	
	private Class<? extends ExtendedBaseService<REQ,RES>> serviceClass;
	
	@Autowired
	private ApplicationContext appCtx;
	
	
	public TxRequiresNewBeanClassServiceInvoker(Class<? extends ExtendedBaseService<REQ,RES>> serviceClass, ApplicationContext appCtx) {
		this.serviceClass = serviceClass;
		this.appCtx = appCtx;
	}
	
	public TxRequiresNewBeanClassServiceInvoker(Class<? extends ExtendedBaseService<REQ,RES>> serviceClass) {
		this.serviceClass = serviceClass;
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	

	@Override
	public RES invokeService(REQ req) {
		ExtendedBaseService<REQ, RES> service = appCtx.getBean(Utility.toDefaultBeanName(serviceClass), serviceClass);
		return service.executeServiceTxRequiresNew(req);
	}

}

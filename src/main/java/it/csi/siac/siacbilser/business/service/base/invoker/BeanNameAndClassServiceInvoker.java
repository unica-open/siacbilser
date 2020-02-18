/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base.invoker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.ServiceInvoker;
import it.csi.siac.siaccommonser.business.service.base.BaseService;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Invoca un BaseService a partire dalla classe del bean di Spring.
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public class BeanNameAndClassServiceInvoker<REQ extends ServiceRequest, RES extends ServiceResponse> implements ServiceInvoker<REQ, RES> {
	
	private String beanName;
	private Class<? extends BaseService<REQ,RES>> serviceClass;
	
	@Autowired
	private ApplicationContext appCtx;
	
	
	public BeanNameAndClassServiceInvoker(String beanName, Class<? extends BaseService<REQ,RES>> serviceClass, ApplicationContext appCtx) {
		this.beanName = beanName;
		this.serviceClass = serviceClass;
		this.appCtx = appCtx;
	}
	
	public BeanNameAndClassServiceInvoker(String beanName, Class<? extends BaseService<REQ,RES>> serviceClass) {
		this.beanName = beanName;
		this.serviceClass = serviceClass;
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	

	@Override
	public RES invokeService(REQ req) {
		BaseService<REQ, RES> service = appCtx.getBean(beanName,serviceClass);
		return service.executeService(req);
	}

}

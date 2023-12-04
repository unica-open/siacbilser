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
 * Invoca un BaseService a partire dal nome del bean di Spring.
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public class BeanNameServiceInvoker<REQ extends ServiceRequest, RES extends ServiceResponse> implements ServiceInvoker<REQ, RES> {
	
	private String beanName;
	
	@Autowired
	private ApplicationContext appCtx;
	
	
	public BeanNameServiceInvoker(String beanName, ApplicationContext appCtx) {
		this.beanName = beanName;
		this.appCtx = appCtx;
	}
	
	public BeanNameServiceInvoker(String beanName) {
		this.beanName = beanName;
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public RES invokeService(REQ req) {
		Object bean = appCtx.getBean(this.beanName);
		BaseService<REQ, RES> service;
		try {
			service = (BaseService<REQ, RES>) bean;
		} catch (ClassCastException cce) {
			throw new IllegalArgumentException("Il bean " + beanName
					+ " non e' di tipo BaseService oppure i tipi di ServiceRequest e ServiceResponse non sono quelli dichiarati in invocazione.", cce);
		}
		return service.executeService(req);
	}

}

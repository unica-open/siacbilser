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
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * Invoca un BaseService.
 *
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public class TxRequiresNewBeanNameServiceInvoker<REQ extends ServiceRequest, RES extends ServiceResponse> implements ServiceInvoker<REQ, RES> {
	
	private String beanName;
	
	@Autowired
	private ApplicationContext appCtx;
	
	
	public TxRequiresNewBeanNameServiceInvoker(String beanName, ApplicationContext appCtx) {
		this.beanName = beanName;
		this.appCtx = appCtx;
	}
	
	public TxRequiresNewBeanNameServiceInvoker(String beanName) {
		this.beanName = beanName;
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RES invokeService(REQ req) {
		Object bean = appCtx.getBean(this.beanName);
		ExtendedBaseService<REQ, RES> service;
		try {
			
			service = (ExtendedBaseService<REQ, RES>) bean;
		} catch (ClassCastException cce) {
			throw new IllegalArgumentException("Il bean " + beanName
					+ " non e' di tipo BaseService oppure i tipi di ServiceRequest e ServiceResponse non sono quelli dichiarati in invocazione.", cce);
		}
		return service.executeServiceTxRequiresNew(req);
	}


}




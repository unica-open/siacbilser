/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.base;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siaccorser.model.Richiedente;

/**
 * Permette di raggruppare l'invocazione generica ad una serie specifica di servizi.
 * 
 * @author Domenico
 *
 */
public abstract class ServiceCallGroup {
	
	protected ServiceExecutor se;
	protected Richiedente richiedente;
	
	
	public ServiceCallGroup(ServiceExecutor serviceExecutor, Richiedente richiedente) {
		super();
		this.se = serviceExecutor;
		this.richiedente = richiedente;
		
		//processInjectionBasedOnCurrentContext();
	}
	
	/**
	 * Permette di inizializzare eventuali servizi legazti con l'annotation @Autowired.
	 * Se necessario e' possibile sovrascrivere questo metodo
	 */
	protected void processInjectionBasedOnCurrentContext() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	

}

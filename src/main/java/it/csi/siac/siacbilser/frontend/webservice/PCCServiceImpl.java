/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.pcc.frontend.webservice.PCCService;
import it.csi.siac.pcc.frontend.webservice.PCCSvcDictionary;
import it.csi.siac.pcc.frontend.webservice.msg.InviaOperazioniPCC;
import it.csi.siac.pcc.frontend.webservice.msg.InviaOperazioniPCCResponse;
import it.csi.siac.siacbilser.business.service.pcc.InviaOperazioniPCCService;
import it.csi.siac.siacbilser.business.utility.Utility;

/**
 * Implementazione dei servizi di integrazione con MARC.
 * 
 * @author Domenico
 */
@WebService(serviceName = "PCCService", 
			portName = "PCCServicePort", 
			targetNamespace = PCCSvcDictionary.NAMESPACE,
			endpointInterface = "it.csi.siac.pcc.frontend.webservice.PCCService")

public class PCCServiceImpl implements PCCService {
    
	@Autowired
	private ApplicationContext appCtx;
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	

	@Override
	public InviaOperazioniPCCResponse inviaOperazioniPCC(InviaOperazioniPCC req) {
		return Utility.getBeanViaDefaultName(appCtx, InviaOperazioniPCCService.class).executeService(req);
	}

}

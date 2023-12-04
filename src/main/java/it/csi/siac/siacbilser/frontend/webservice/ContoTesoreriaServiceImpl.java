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

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.predocumentospesa.LeggiContiTesoreriaService;
import it.csi.siac.siacfin2ser.frontend.webservice.ContoTesoreriaService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;


// TODO: Auto-generated Javadoc
/**
 * The Class OrdineServiceImpl.
 */
@WebService(serviceName = "ContoTesoreriaService", portName = "ContoTesoreriaServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.ContoTesoreriaService")
public class ContoTesoreriaServiceImpl implements ContoTesoreriaService {
	
	/** The app ctx. */
	@Autowired
	private ApplicationContext appCtx;
	
	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public LeggiContiTesoreriaResponse leggiContiTesoreria(LeggiContiTesoreria parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiContiTesoreriaService.class, parameters);
	}


}

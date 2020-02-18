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
import it.csi.siac.siacbilser.business.service.test.Test1Service;
import it.csi.siac.siacbilser.business.service.test.Test2Service;
import it.csi.siac.siaccorser.frontend.webservice.CORSvcDictionary;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.UploadFile;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.UploadFileResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.TestService;

/**
 * Implementazione del servizio Test.
 *
 */
@WebService(serviceName = "TestService", portName = "TestServicePort", 
targetNamespace = CORSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.TestService")
public class TestServiceImpl implements TestService {
	
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
	public UploadFileResponse test1(UploadFile parameters) {
		return BaseServiceExecutor.execute(appCtx, Test1Service.class, parameters);
	}

	@Override
	public UploadFileResponse test2(UploadFile parameters) {
		return BaseServiceExecutor.execute(appCtx, Test2Service.class, parameters);
	}

}

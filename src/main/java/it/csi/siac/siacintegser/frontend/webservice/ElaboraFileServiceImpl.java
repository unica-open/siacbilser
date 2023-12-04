/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileAsyncService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileService;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

/**
 * The Class ElaboraFileServiceImpl.
 */
@WebService(serviceName = "ElaboraFileService", portName = "ElaboraFileServicePort", targetNamespace = INTEGSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacintegser.frontend.webservice.ElaboraFileService")
public class ElaboraFileServiceImpl implements it.csi.siac.siacintegser.frontend.webservice.ElaboraFileService {

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
	public ElaboraFileResponse elaboraFile(@WebParam ElaboraFile req) {
		return BaseServiceExecutor.execute(appCtx, ElaboraFileService.class, req);
	}

	@Override
	public AsyncServiceResponse elaboraFileAsync(AsyncServiceRequestWrapper<ElaboraFile> req) {
		return BaseServiceExecutor.execute(appCtx, ElaboraFileAsyncService.class, req);
	}
	

}

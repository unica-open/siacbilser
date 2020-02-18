/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse;

/**
 * The class AssociaImputazioniContabiliVariatePreDocumentoEntrataAsyncService
 * @author Marchino Alessandro
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssociaImputazioniContabiliVariatePreDocumentoEntrataAsyncService extends AsyncBaseService<AssociaImputazioniContabiliVariatePreDocumentoEntrata, 
																					AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse, 
																					AsyncServiceRequestWrapper<AssociaImputazioniContabiliVariatePreDocumentoEntrata>, 
																					AssociaImputazioniContabiliVariatePreDocumentoEntrataAsyncResponseHandler, 
																					AssociaImputazioniContabiliVariatePreDocumentoEntrataService> {

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		service.checkServiceParam();
		res.addErrori(service.getServiceResponse().getErrori());
	}
	
	@Override
	protected void preStartService() {
		// Nulla da effettuare
	}

	@Override
	protected void postStartService() {
		// Nulla da effettuare
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoMultiploResponse;

/**
 * @author elisa
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompletaAllegatoAttoMultiploAsyncService extends AsyncBaseService<CompletaAllegatoAttoMultiplo,
																	   CompletaAllegatoAttoMultiploResponse,
																	   AsyncServiceRequestWrapper<CompletaAllegatoAttoMultiplo>,
																	   CompletaAllegatoAttoMultiploAsyncResponseHandler,
																	   CompletaAllegatoAttoMultiploService> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		service.checkServiceParam();
		res.addErrori(service.getServiceResponse().getErrori());
	}
	
	@Override
	@Transactional
	public AsyncServiceResponse executeService(AsyncServiceRequestWrapper<CompletaAllegatoAttoMultiplo> serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void preStartService() {
		final String methodName = "preStartService";
		
		log.debug(methodName, "Operazione asincrona in procinto di avviarsi...");
	}

	@Override
	protected void postStartService() {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	protected boolean mayRequireElaborationOnDedicatedQueue() {
		// TODO verificare
		return false;
	}
}

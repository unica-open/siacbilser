/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerProvvisorio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerProvvisorioResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConvalidaAllegatoAttoPerProvvisorioAsyncService extends AsyncBaseService<ConvalidaAllegatoAttoPerProvvisorio, 
																					  ConvalidaAllegatoAttoPerProvvisorioResponse, 
																					  AsyncServiceRequestWrapper<ConvalidaAllegatoAttoPerProvvisorio>, 
																					  ConvalidaAllegatoAttoPerProvvisorioAsyncResponseHandler, 
																					  ConvalidaAllegatoAttoPerProvvisorioService> {

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		String methodName = "checkServiceParam";
		service.checkServiceParam();
		log.debug(methodName, "Errori riscontrati: "+ service.getServiceResponse().getErrori());
		res.addErrori(service.getServiceResponse().getErrori());
	}
	
	
	@Override
	protected void preStartService() {
		//Nessun check sui dati da effettuare.
	}

	@Override
	protected void postStartService() {
		// Nessuna post-operazione
	}

}

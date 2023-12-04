/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraMassivaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraMassivaPrimaNotaIntegrataResponse;

/**
 * Wrapper asincrono per la registrazione massiva della prima nota integrata
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegistraMassivaPrimaNotaIntegrataAsyncService extends AsyncBaseService<RegistraMassivaPrimaNotaIntegrata,
																					RegistraMassivaPrimaNotaIntegrataResponse,
																					AsyncServiceRequestWrapper<RegistraMassivaPrimaNotaIntegrata>,
																					RegistraMassivaPrimaNotaIntegrataAsyncResponseHandler,
																					RegistraMassivaPrimaNotaIntegrataService> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		String methodName = "checkServiceParam";
		service.checkServiceParam();
		log.debug(methodName, "Errori riscontrati: "+ service.getServiceResponse().getErrori());
		res.addErrori(service.getServiceResponse().getErrori());
	
	}

	@Override
	protected void preStartService() {
		// Nessuna pre-elaborazione
	}

	@Override
	protected void postStartService() {
		// Nessuna post-elaborazione
	}
	
}

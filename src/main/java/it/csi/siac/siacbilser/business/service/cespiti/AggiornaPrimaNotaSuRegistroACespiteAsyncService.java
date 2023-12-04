/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.Inventario;
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaPrimaNotaSuRegistroACespiteAsyncService extends AsyncBaseService<AggiornaPrimaNotaSuRegistroACespite,
                                                                       AggiornaPrimaNotaSuRegistroACespiteResponse,
																	   AsyncServiceRequestWrapper<AggiornaPrimaNotaSuRegistroACespite>,
																	   AggiornaPrimaNotaSuRegistroACespiteAysncResponseHandler,
																	   AggiornaPrimaNotaSuRegistroACespiteService> {
	
	/** The ottieni dati prime note fattura con nota credito service. */
	@Autowired
	private ServiceExecutor  serviceExecutor; 
	@Autowired @Inventario
	private PrimaNotaInvDad primaNotaInvDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		service.checkServiceParam();
	}
	
	@Override
	@Transactional
	public AsyncServiceResponse executeService(AsyncServiceRequestWrapper<AggiornaPrimaNotaSuRegistroACespite> serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void preStartService() {
		service.checkImportoRegistroA(originalRequest.getPrimaNota());
		
	}

	@Override
	protected void postStartService() {
		final String methodName = "preStartService";
		log.debug(methodName, "Operazione asincrona avviata");
	}
	
	@Override
	protected boolean mayRequireElaborationOnDedicatedQueue() {
		return false;
	}
}

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
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciPrimeNoteAmmortamentoAnnuoCespiteAsyncService extends AsyncBaseService<InserisciPrimeNoteAmmortamentoAnnuoCespite,
                                                                       InserisciPrimeNoteAmmortamentoAnnuoCespiteResponse,
																	   AsyncServiceRequestWrapper<InserisciPrimeNoteAmmortamentoAnnuoCespite>,
																	   InserisciPrimeNoteAmmortamentoAnnuoCespiteAysncResponseHandler,
																	   InserisciPrimeNoteAmmortamentoAnnuoCespiteService> {
	
	@Autowired
	private CespiteDad cespiteDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
	}
	
	@Override
	@Transactional
	public AsyncServiceResponse executeService(AsyncServiceRequestWrapper<InserisciPrimeNoteAmmortamentoAnnuoCespite> serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(req.getRichiedente().getAccount().getEnte());
	}
	
	@Override
	protected void preStartService() {
		
	}

	
	@Override
	protected void postStartService() {
		final String methodName = "preStartService";
		log.debug(methodName, "Operazione asincrona avviata");
	}
	
//	@Override
//	protected boolean mayRequireElaborationOnDedicatedQueue() {
//		Long threshold = getThreshold();
//	    Long numeroCespiti = Long.valueOf(originalRequest.getUidsCespiti().size());
//		return threshold != null && numeroCespiti != null && numeroCespiti.compareTo(threshold) > 0;
//	}
}

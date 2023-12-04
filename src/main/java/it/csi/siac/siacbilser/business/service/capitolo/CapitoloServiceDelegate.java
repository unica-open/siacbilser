/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccommonser.business.service.base.BaseService;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;

/**
 * The Class CapitoloServiceDelegate.
 */
@Component
public class CapitoloServiceDelegate {

	
	/** The app ctx. */
	@Autowired
	private ApplicationContext appCtx;
	
	
	/**
	 * Annulla capitolo.
	 *
	 * @param cap the cap
	 * @return the service response
	 */
	public ServiceResponse annullaCapitolo(Capitolo<?, ?> cap) {
				
		
		CapitoloServiceEnum s = CapitoloServiceEnum.byOperazioneETipoCapitolo("annulla", cap.getTipoCapitolo());
	    ServiceRequest req = s.getServiceRequestNewInstance();
		
		s.set(req, "Capitolo", cap);
		s.set(req, "Ente", cap.getEnte());
		s.set(req, "Bilancio", cap.getBilancio());
		
		@SuppressWarnings("unchecked")
		BaseService<ServiceRequest,ServiceResponse> service = (BaseService<ServiceRequest, ServiceResponse>) appCtx.getBean(s.getServiceClass());
		ServiceResponse res = service.executeService(req);
		
		//Object object = s.get(serviceResponse, "Esito");
		return res;
		
		
	}
	
	
	
	
}

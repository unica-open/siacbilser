/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.componenteimpcap;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

/**
 * The Class RicercaComponenteImportiCapitoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaComponenteImportiCapitoloService extends BaseGestioneComponenteImportiCapitoloService<RicercaComponenteImportiCapitolo, RicercaComponenteImportiCapitoloResponse> {

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitolo(), "capitolo");
	}

	@Override
	@Transactional(readOnly = true)
	public RicercaComponenteImportiCapitoloResponse executeService(RicercaComponenteImportiCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		initCapitolo(req.getCapitolo().getUid());
		
		loadComponentiImportiCapitolo();
	}

}

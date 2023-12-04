/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.vincolicapitolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ScollegaCapitoloAlVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ScollegaCapitoloAlVincoloResponse;
import it.csi.siac.siacbilser.integration.dad.VincoloCapitoliDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class ScollegaCapitoloAlVincoloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ScollegaCapitoloAlVincoloService extends CheckedAccountBaseService<ScollegaCapitoloAlVincolo, ScollegaCapitoloAlVincoloResponse> {
	
	
	/** The vincolo capitoli dad. */
	@Autowired
	private VincoloCapitoliDad vincoloCapitoliDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getVincolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Vincolo"));
		checkNotNull(req.getVincolo().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente vincolo"));		
		checkCondition(req.getVincolo().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid vincolo"));
		
		checkNotNull(req.getCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Capitolo"));
//		checkNotNull(req.getCapitolo().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente capitolo"));		
		checkCondition(req.getCapitolo().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid Capitolo"));
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		vincoloCapitoliDad.setEnte(req.getVincolo().getEnte());
		vincoloCapitoliDad.setLoginOperazione(loginOperazione);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	public ScollegaCapitoloAlVincoloResponse executeService(ScollegaCapitoloAlVincolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		vincoloCapitoliDad.scollegaCapitoloAlVincolo(req.getVincolo(), req.getCapitolo());
		
	}

	
	

}

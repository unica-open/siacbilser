/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.gruppoattivitaiva;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.GruppoAttivitaIvaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaGruppoAttivitaIvaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaGruppoAttivitaIvaService extends CheckedAccountBaseService<RicercaGruppoAttivitaIva, RicercaGruppoAttivitaIvaResponse> {
	

	/** The gruppo attivita iva dad. */
	@Autowired
	private GruppoAttivitaIvaDad gruppoAttivitaIvaDad;
	
	/** The gruppo. */
	private GruppoAttivitaIva gruppo;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		gruppo=req.getGruppoAttivitaIva();
		
		
		checkNotNull(gruppo, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("gruppo"));
		
		checkNotNull(gruppo.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(gruppo.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaGruppoAttivitaIvaResponse executeService(RicercaGruppoAttivitaIva serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		//Ricerca dei documenti
		List<GruppoAttivitaIva> listaGruppoAttivitaIva = gruppoAttivitaIvaDad.ricercaGruppoAttivitaIva(gruppo);
		res.setListaGruppoAttivitaIva(listaGruppoAttivitaIva);
				
	}
	
	
}

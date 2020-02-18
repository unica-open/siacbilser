/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.DocumentoDiSpesa;

// TODO: Auto-generated Javadoc
//TODO servizio da implementare

/**
 * The Class RicercaDocumentiCapitoloUscitaGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDocumentiCapitoloUscitaGestioneService 
	extends CheckedAccountBaseService<RicercaDocumentiCapitoloUscitaGestione, RicercaDocumentiCapitoloUscitaGestioneResponse> {

	/** The capitolo uscita gestione dad. */
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"), false);
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"), false);
		checkNotNull(req.getCapitoloUscitaGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo uscita gestione"), false);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {		
		capitoloUscitaGestioneDad.setBilancio(req.getBilancio());
		capitoloUscitaGestioneDad.setEnte(req.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDocumentiCapitoloUscitaGestioneResponse executeService(RicercaDocumentiCapitoloUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {	
		/** INFO In V1 l’operazione dovrà essere implementata restituendo una lista 
		 *       di oggetti del tipo documenti vuota. 
		 */
		List<DocumentoDiSpesa> listaDocumentiDiSpesa = new ArrayList<DocumentoDiSpesa>();
		
		res.setListaDocumenti(listaDocumentiDiSpesa);
	}
}

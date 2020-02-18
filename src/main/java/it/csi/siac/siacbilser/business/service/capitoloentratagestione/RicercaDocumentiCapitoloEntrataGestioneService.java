/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDocumentiCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.DocumentoDiEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDocumentiCapitoloEntrataGestioneService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDocumentiCapitoloEntrataGestioneService 
	extends CheckedAccountBaseService<RicercaDocumentiCapitoloEntrataGestione, RicercaDocumentiCapitoloEntrataGestioneResponse> {

	/** The capitolo entrata gestione dad. */
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
		
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(req.getCapitoloEntrataGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo entrata gestione"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		capitoloEntrataGestioneDad.setBilancio(req.getBilancio());
		capitoloEntrataGestioneDad.setEnte(req.getEnte());	
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDocumentiCapitoloEntrataGestioneResponse executeService(RicercaDocumentiCapitoloEntrataGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {	
		/** INFO In V1 l’operazione dovrà essere implementata restituendo una lista
         *       di oggetti del tipo accertamenti vuota. 
		 */
		List<DocumentoDiEntrata> listaDocumentiDiEntrata = new ArrayList<DocumentoDiEntrata>();
		
		res.setListaDocumentiDiEntrata(listaDocumentiDiEntrata);
	}
}

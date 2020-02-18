/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaspesa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;

/**
 * Implementazione del servizio RicercaDettaglioSubdocumentoIvaSpesaService.
 * 
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioSubdocumentoIvaSpesaService extends CheckedAccountBaseService<RicercaDettaglioSubdocumentoIvaSpesa, RicercaDettaglioSubdocumentoIvaSpesaResponse> {
	
	/** The subdocumento iva spesa dad. */
	@Autowired
	private SubdocumentoIvaSpesaDad subdocumentoIvaSpesaDad;
	
	/** The subdoc. */
	private SubdocumentoIvaSpesa subdoc;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdoc = req.getSubdocumentoIvaSpesa();	
		
		checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva spesa"));		
		checkCondition(subdoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento iva spesa"));
		
		
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		subdocumentoIvaSpesaDad.setLoginOperazione(loginOperazione);
		//subdocumentoIvaSpesaDad.setEnte(subdoc.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	@Override
	public RicercaDettaglioSubdocumentoIvaSpesaResponse executeService(RicercaDettaglioSubdocumentoIvaSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		SubdocumentoIvaSpesa subdocIvaSpesa = subdocumentoIvaSpesaDad.findSubdocumentoIvaSpesaById(subdoc.getUid());		
		
		if(subdocIvaSpesa==null) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("subdocumento iva spesa", "id: "+subdoc.getUid()));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		
		res.setSubdocumentoIvaSpesa(subdocIvaSpesa);
		

	}

}

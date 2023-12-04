/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaentrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoIvaEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDettaglioSubdocumentoIvaEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioSubdocumentoIvaEntrataService extends CheckedAccountBaseService<RicercaDettaglioSubdocumentoIvaEntrata, RicercaDettaglioSubdocumentoIvaEntrataResponse> {
	
	/** The subdocumento iva entrata dad. */
	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaEntrataDad;
	
	/** The subdoc. */
	private SubdocumentoIvaEntrata subdoc;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdoc = req.getSubdocumentoIvaEntrata();	
		
		checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva spesa"));		
		checkCondition(subdoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento iva spesa"));
		
		
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		subdocumentoIvaEntrataDad.setLoginOperazione(loginOperazione);
		//subdocumentoIvaEntrataDad.setEnte(subdoc.getEnte());
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly=true)
	@Override
	public RicercaDettaglioSubdocumentoIvaEntrataResponse executeService(RicercaDettaglioSubdocumentoIvaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		SubdocumentoIvaEntrata subdocIvaEntrata = subdocumentoIvaEntrataDad.findSubdocumentoIvaEntrataById(subdoc.getUid());		
		
		if(subdocIvaEntrata==null) {
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("subdocumento iva spesa", "id: "+subdoc.getUid()));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		
		res.setSubdocumentoIvaEntrata(subdocIvaEntrata);
		

	}

}

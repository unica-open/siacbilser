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
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaPuntualeSubdocumentoIvaEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaPuntualeSubdocumentoIvaEntrataService extends CheckedAccountBaseService<RicercaPuntualeSubdocumentoIvaEntrata, RicercaPuntualeSubdocumentoIvaEntrataResponse> {
	
	/** The subdocumento iva dad. */
	@Autowired
	private SubdocumentoIvaEntrataDad subdocumentoIvaDad;
	
	/** The subdoc iva. */
	private SubdocumentoIvaEntrata subdocIva;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		subdocIva = req.getSubdocumentoIvaEntrata();
		
		checkNotNull(subdocIva, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento iva"));

		checkNotNull(subdocIva.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(subdocIva.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(subdocIva.getAnnoEsercizio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno esercizio"));
		checkNotNull(subdocIva.getProgressivoIVA(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("progressivo IVA"));
		
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaPuntualeSubdocumentoIvaEntrataResponse executeService(RicercaPuntualeSubdocumentoIvaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		SubdocumentoIvaEntrata subdocumentoIvaEntrata = subdocumentoIvaDad.ricercaPuntualeSubdocumentoIvaEntrata(subdocIva);
		res.setSubdocumentoIvaEntrata(subdocumentoIvaEntrata);				
	}

}

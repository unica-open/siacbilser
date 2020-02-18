/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaQuoteByDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaQuoteByDocumentoEntrataService extends CheckedAccountBaseService<RicercaQuoteByDocumentoEntrata, RicercaQuoteByDocumentoEntrataResponse> {
	
	/** The subdocumento entrata dad. */
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getDocumentoEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento entrata"));		
		checkCondition(req.getDocumentoEntrata().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento entrata"));
		
	
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
		
	}
		

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		List<SubdocumentoEntrata> result = subdocumentoEntrataDad.findSubdocumentiEntrataByIdDocumento(req.getDocumentoEntrata().getUid());		
		res.setSubdocumentiEntrata(result);

	}

}

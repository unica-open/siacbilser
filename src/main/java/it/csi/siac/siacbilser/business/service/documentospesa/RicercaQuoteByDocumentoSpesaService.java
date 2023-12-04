/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaQuoteByDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaQuoteByDocumentoSpesaService extends CheckedAccountBaseService<RicercaQuoteByDocumentoSpesa, RicercaQuoteByDocumentoSpesaResponse> {
	
	/** The subdocumento spesa dad. */
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {		
		checkNotNull(req.getDocumentoSpesa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento spesa"));		
		checkCondition(req.getDocumentoSpesa().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento spesa"));
		
	
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		
	}
		

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		List<SubdocumentoSpesa> result = subdocumentoSpesaDad.findSubdocumentiSpesaByIdDocumento(req.getDocumentoSpesa().getUid());		
		res.setSubdocumentiSpesa(result);

	}

}

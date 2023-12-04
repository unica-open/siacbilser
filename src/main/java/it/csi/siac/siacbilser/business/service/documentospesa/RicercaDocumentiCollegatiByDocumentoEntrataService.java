/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDocumentiCollegatiByDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDocumentiCollegatiByDocumentoEntrataService extends CheckedAccountBaseService<RicercaDocumentiCollegatiByDocumentoEntrata, RicercaDocumentiCollegatiByDocumentoEntrataResponse> {

	/** The documento entrata dad. */
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	
	/** The documento entrata. */
	private DocumentoEntrata documentoEntrata;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		documentoEntrata = req.getDocumentoEntrata();
		checkNotNull(documentoEntrata, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento entrata"));
		checkCondition(documentoEntrata.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento entrata"));

	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoEntrataDad.setLoginOperazione(loginOperazione);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaDocumentiCollegatiByDocumentoEntrataResponse executeService(RicercaDocumentiCollegatiByDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		DocumentoEntrata documento = documentoEntrataDad.findDocumentiCollegatiByIdDocumento(documentoEntrata.getUid());
		res.setDocumentoEntrata(documento);
	}

}

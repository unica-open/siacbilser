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
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaDocumentiCollegatiByDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDocumentiCollegatiByDocumentoSpesaService extends CheckedAccountBaseService<RicercaDocumentiCollegatiByDocumentoSpesa, RicercaDocumentiCollegatiByDocumentoSpesaResponse> {

	/** The documento spesa dad. */
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	
	/** The documento spesa. */
	private DocumentoSpesa documentoSpesa;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		documentoSpesa = req.getDocumentoSpesa();
		checkNotNull(documentoSpesa, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento spesa"));
		checkCondition(documentoSpesa.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento spesa"));

	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoSpesaDad.setLoginOperazione(loginOperazione);
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaDocumentiCollegatiByDocumentoSpesaResponse executeService(RicercaDocumentiCollegatiByDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		DocumentoSpesa documento = documentoSpesaDad.findDocumentiCollegatiByIdDocumento(documentoSpesa.getUid());
		res.setDocumentoSpesa(documento);
	}

}

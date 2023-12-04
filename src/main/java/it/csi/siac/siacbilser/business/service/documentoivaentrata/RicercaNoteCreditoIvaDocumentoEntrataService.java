/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaentrata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

/**
 * Ricerca le note credito iva associate ad un documento.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaNoteCreditoIvaDocumentoEntrataService extends CheckedAccountBaseService<RicercaNoteCreditoIvaDocumentoEntrata, RicercaNoteCreditoIvaDocumentoEntrataResponse> {

	
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getDocumentoEntrata(), "documento entrata");
	}
	
	@Transactional(readOnly=true)
	@Override
	public RicercaNoteCreditoIvaDocumentoEntrataResponse executeService(RicercaNoteCreditoIvaDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		List<SubdocumentoIvaEntrata> noteCreditoIvaAssociate = documentoEntrataDad.findNoteCreditoIvaAssociate(req.getDocumentoEntrata().getUid());
		res.setSubdocumentoIvaEntrata(noteCreditoIvaAssociate);
		res.setCardinalitaComplessiva(noteCreditoIvaAssociate!=null?noteCreditoIvaAssociate.size():0);
	}

}

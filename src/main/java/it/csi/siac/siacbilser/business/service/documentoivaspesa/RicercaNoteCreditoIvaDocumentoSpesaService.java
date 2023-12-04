/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoivaspesa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;

/**
 * Ricerca le note credito iva associate ad un documento.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaNoteCreditoIvaDocumentoSpesaService extends CheckedAccountBaseService<RicercaNoteCreditoIvaDocumentoSpesa, RicercaNoteCreditoIvaDocumentoSpesaResponse> {

	
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getDocumentoSpesa(), "documento spesa");
	}
	
	@Transactional(readOnly=true)
	@Override
	public RicercaNoteCreditoIvaDocumentoSpesaResponse executeService(RicercaNoteCreditoIvaDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		List<SubdocumentoIvaSpesa> noteCreditoIvaAssociate = documentoSpesaDad.findNoteCreditoIvaAssociate(req.getDocumentoSpesa().getUid());
		res.setSubdocumentoIvaSpesa(noteCreditoIvaAssociate);
		res.setCardinalitaComplessiva(noteCreditoIvaAssociate!=null?noteCreditoIvaAssociate.size():0);
	}

}

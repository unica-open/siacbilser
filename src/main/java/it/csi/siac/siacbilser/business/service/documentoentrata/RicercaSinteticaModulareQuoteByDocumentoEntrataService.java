/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;

/**
 * The Class RicercaSinteticaQuoteByDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaModulareQuoteByDocumentoEntrataService extends CheckedAccountBaseService<RicercaSinteticaModulareQuoteByDocumentoEntrata, RicercaSinteticaModulareQuoteByDocumentoEntrataResponse> {
	
	/** The subdocumento entrata dad. */
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getDocumentoEntrata(), "documento entrata");
		checkParametriPaginazione(req.getParametriPaginazione());
		
		if(req.getSubdocumentoEntrataModelDetails() == null) {
			// FIXME: da rimettere a posto quando ci sara' un mapping corretto
			Set<SubdocumentoEntrataModelDetail> md = EnumSet.allOf(SubdocumentoEntrataModelDetail.class);
			md.remove(SubdocumentoEntrataModelDetail.Attr);
			req.setSubdocumentoEntrataModelDetails(md.toArray(new SubdocumentoEntrataModelDetail[md.size()]));
//			req.setSubdocumentoSpesaModelDetails(SubdocumentoSpesaModelDetail.values());
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaModulareQuoteByDocumentoEntrataResponse executeService(RicercaSinteticaModulareQuoteByDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		ListaPaginata<SubdocumentoEntrata> result = subdocumentoEntrataDad.findSubdocumentiEntrataByIdDocumento(req.getDocumentoEntrata().getUid(), req.getRilevanteIva(), req.getParametriPaginazione(), req.getSubdocumentoEntrataModelDetails());
		res.setSubdocumentiEntrata(result);
		
		BigDecimal totale = subdocumentoEntrataDad.findTotaleImportoSubdocumentiEntrataByIdDocumento(req.getDocumentoEntrata().getUid());
		res.setTotale(totale);
	}

}

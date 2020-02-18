/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;

/**
 * The Class RicercaSinteticaQuoteByDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaModulareQuoteByDocumentoSpesaService extends CheckedAccountBaseService<RicercaSinteticaModulareQuoteByDocumentoSpesa, RicercaSinteticaModulareQuoteByDocumentoSpesaResponse> {
	
	/** The subdocumento spesa dad. */
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getDocumentoSpesa(), "documento spesa");
		checkParametriPaginazione(req.getParametriPaginazione());
		
		if(req.getSubdocumentoSpesaModelDetails() == null) {
			// FIXME: da rimettere a posto quando ci sara' un mapping corretto
			Set<SubdocumentoSpesaModelDetail> md = EnumSet.allOf(SubdocumentoSpesaModelDetail.class);
			md.remove(SubdocumentoSpesaModelDetail.Attr);
			md.remove(SubdocumentoSpesaModelDetail.DocPadreModelDetail);
			req.setSubdocumentoSpesaModelDetails(md.toArray(new SubdocumentoSpesaModelDetail[md.size()]));
//			req.setSubdocumentoSpesaModelDetails(SubdocumentoSpesaModelDetail.values());
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaModulareQuoteByDocumentoSpesaResponse executeService(RicercaSinteticaModulareQuoteByDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		ListaPaginata<SubdocumentoSpesa> result = subdocumentoSpesaDad.findSubdocumentiSpesaByIdDocumento(req.getDocumentoSpesa().getUid(), req.getRilevanteIva(), req.getParametriPaginazione(), req.getSubdocumentoSpesaModelDetails());
		res.setSubdocumentiSpesa(result);
		
		BigDecimal totale = subdocumentoSpesaDad.findTotaleImportoSubdocumentiSpesaByIdDocumento(req.getDocumentoSpesa().getUid());
		res.setTotale(totale);
		
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class RicercaQuotaEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaQuotePerProvvisorioEntrataService extends CheckedAccountBaseService<RicercaQuotePerProvvisorioEntrata, RicercaQuotePerProvvisorioEntrataResponse> {

	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkParametriPaginazione(req.getParametriPaginazione());
	}
	
	@Override
	protected void init() {
		subdocumentoEntrataDad.setEnte(ente);
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaQuotePerProvvisorioEntrataResponse executeService(RicercaQuotePerProvvisorioEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		ListaPaginata<SubdocumentoEntrata> listaSubdocumentiEntrata = subdocumentoEntrataDad.ricercaSubdocumentiEntrataPerProvvisorio(
				req.getTipoDocumento(),
				req.getAnnoDocumento(),
				req.getNumeroDocumento(),
				req.getDataEmissioneDocumento(),
				req.getNumeroQuota(),
				req.getNumeroMovimento(),
				req.getAnnoMovimento(),
				req.getSoggetto(),
				req.getAnnoElenco(),
				req.getNumeroElenco(),
				req.getFlgEscludiSubDocCollegati(),
				req.getParametriPaginazione()
				
				);
		res.setListaSubdocumenti(listaSubdocumentiEntrata);
		
//		BigDecimal totaleImporti = subdocumentoEntrataDad.ricercaSubdocumentiEntrataPerProvvisorioTotaleImporti(
//				req.getTipoDocumento(),
//				req.getAnnoDocumento(),
//				req.getNumeroDocumento(),
//				req.getDataEmissioneDocumento(),
//				req.getNumeroQuota(),
//				req.getNumeroMovimento(),
//				req.getAnnoMovimento(),
//				req.getSoggetto(),
//				req.getAnnoElenco(),
//				req.getNumeroElenco(),
//				req.getParametriPaginazione()
//				);
		BigDecimal totaleImporti = subdocumentoEntrataDad.ricercaSubdocumentiEntrataPerProvvisorioTotaleImporti(listaSubdocumentiEntrata);
		res.setTotaleImporti(totaleImporti);
	}


}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class RicercaQuotePerProvvisorioSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaQuotePerProvvisorioSpesaService extends CheckedAccountBaseService<RicercaQuotePerProvvisorioSpesa, RicercaQuotePerProvvisorioSpesaResponse> {

	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkParametriPaginazione(req.getParametriPaginazione());
	}
	
	@Override
	protected void init() {
		subdocumentoSpesaDad.setEnte(ente);
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaQuotePerProvvisorioSpesaResponse executeService(RicercaQuotePerProvvisorioSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		ListaPaginata<SubdocumentoSpesa> listaSubdocumentiSpesa = subdocumentoSpesaDad.ricercaSubdocumentiSpesaPerProvvisorio(
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
				req.getParametriPaginazione()
				);
		res.setListaSubdocumenti(listaSubdocumentiSpesa);
		
//		BigDecimal totaleImporti = subdocumentoSpesaDad.ricercaSubdocumentiSpesaPerProvvisorioTotaleImporti(
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
		BigDecimal totaleImporti = subdocumentoSpesaDad.ricercaSubdocumentiSpesaPerProvvisorioTotaleImporti(listaSubdocumentiSpesa);
		res.setTotaleImporti(totaleImporti);
	}


}

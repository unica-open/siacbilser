/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.math.BigDecimal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociarePredocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaAssociarePredocumentoResponse;
import it.csi.siac.siacfin2ser.model.Subdocumento;

/**
 * The Class RicercaQuoteDaAssociareService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaQuoteDaAssociarePredocumentoService extends BaseRicercaQuoteDaAssociareService<RicercaQuoteDaAssociarePredocumento, RicercaQuoteDaAssociarePredocumentoResponse> {

	
	@Override
	@Transactional(readOnly=true)
	public RicercaQuoteDaAssociarePredocumentoResponse executeService(RicercaQuoteDaAssociarePredocumento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		//PREDISPOSIZIONE D'INCASSO
		ListaPaginata<Subdocumento<?,?>> listaSubdocumentiPerCollegaDocumento = subdocumentoDad.ricercaSubdocumentiDaAssociarePerCollegaDocumento(
				req.getEnte(),
				req.getTipoFamigliaDocumento(),
				req.getTipoDocumento(),
				req.getAnnoDocumento(),
				req.getNumeroDocumento(),
				req.getSoggetto(),
				req.getStatiOperativoDocumento(),
				req.getImportoPerRicercaQuote(),
				req.getNumeroProvvisorioCassaRicercaQuote(),
				req.getParametriPaginazione()
				);
		
		res.setListaSubdocumenti(listaSubdocumentiPerCollegaDocumento);
		
		BigDecimal totaleImportiPerCollegaDocumento = subdocumentoDad.ricercaSubdocumentiDaAssociareTotaleImportiPerCollegaDocumento(
				req.getEnte(),
				req.getTipoFamigliaDocumento(),
				req.getTipoDocumento(),
				req.getAnnoDocumento(),
				req.getNumeroDocumento(),
				req.getSoggetto(),
				req.getStatiOperativoDocumento(),
				req.getImportoPerRicercaQuote(),
				req.getNumeroProvvisorioCassaRicercaQuote(),
				req.getParametriPaginazione()
				);
		
		res.setTotaleImporti(totaleImportiPerCollegaDocumento);
	}

}

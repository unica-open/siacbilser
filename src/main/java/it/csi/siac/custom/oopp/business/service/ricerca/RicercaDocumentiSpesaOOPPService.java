/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.business.service.ricerca;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.custom.oopp.integration.dad.DocumentoSpesaOOPPDad;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.base.IntegBaseService;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaDocumentiSpesaOOPP;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.oopp.ricerca.RicercaDocumentiSpesaOOPPResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDocumentiSpesaOOPPService extends
		IntegBaseService<RicercaDocumentiSpesaOOPP, RicercaDocumentiSpesaOOPPResponse>
{
	@Autowired private DocumentoSpesaOOPPDad documentoSpesaOOPPDad;
	
	@Override
	protected RicercaDocumentiSpesaOOPPResponse execute(RicercaDocumentiSpesaOOPP ireq) {
		RicercaDocumentiSpesaOOPPResponse ires = instantiateNewIRes();
		documentoSpesaOOPPDad.setEnte(ente);
		ires.setElencoDocumentiSpesa(
			documentoSpesaOOPPDad.ricercaDocumentiSpesa(
				ireq.getAnnoBilancio(), 
				ireq.getAnnoDocumento(), 
				ireq.getCig(), 
				ireq.getAnnoImpegno(), 
				ireq.getNumeroImpegno(), 
				ireq.getAnnoBilancio(), 
				ireq.getNumeroMandato()
		));
		ires.setNumeroTotaleDocumentiSpesaTrovati(CollectionUtil.getSize(ires.getElencoDocumentiSpesa()));
		return ires;
	}

	@Override
	protected void checkServiceParameters(RicercaDocumentiSpesaOOPP ireq) throws ServiceParamError {
		checkParamNotNull(ireq.getAnnoBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annoBilancio"));
		//checkParamNotNull(ireq.getAnnoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annoDocumento")); 
		checkParamCondition(   
				StringUtils.isNotBlank(ireq.getCig()) ||
				(ireq.getAnnoImpegno() != null && ireq.getNumeroImpegno() != null) ||
				ireq.getNumeroMandato() != null, 
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("cig o impegno"));   
	}
}

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
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAttributiQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAttributiQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class AggiornaAttributiQuotaDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAttributiQuotaDocumentoSpesaService extends CheckedAccountBaseService<AggiornaAttributiQuotaDocumentoSpesa, AggiornaAttributiQuotaDocumentoSpesaResponse> {

	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	/** The subdocumento spesa. */
	private SubdocumentoSpesa subdocumentoSpesa;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		subdocumentoSpesa = req.getSubdocumentoSpesa();
		
		checkEntita(subdocumentoSpesa, "subdocumento spesa", false);
		checkEntita(subdocumentoSpesa.getEnte(), "ente subdocumento spesa", false);
		
		checkNotNull(req.getAttributi(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("attributi"), false);
		if(req.getSiopeAssenzaMotivazione() != null) {
			checkEntita(req.getSiopeAssenzaMotivazione(), "siope assenza motivazione");
		}
	}
	
	@Override
	protected void init() {
		subdocumentoSpesaDad.setEnte(subdocumentoSpesa.getEnte());
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	@Transactional
	public AggiornaAttributiQuotaDocumentoSpesaResponse executeService(AggiornaAttributiQuotaDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		subdocumentoSpesaDad.aggiornaAttributiSubdocumento(subdocumentoSpesa, req.getAttributi());
		subdocumentoSpesaDad.aggiornaSiopeAssenzaMotivazione(subdocumentoSpesa, req.getSiopeAssenzaMotivazione());
		
		res.setSubdocumentoSpesa(subdocumentoSpesa);
	}


}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class RicercaQuotaSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioQuotaEntrataService extends CheckedAccountBaseService<RicercaDettaglioQuotaEntrata, RicercaDettaglioQuotaEntrataResponse> {

	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	private SubdocumentoEntrata subdocumentoEntrata;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		subdocumentoEntrata = req.getSubdocumentoEntrata();
		checkNotNull(subdocumentoEntrata, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento entrata"));
		checkCondition(subdocumentoEntrata.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento entrata"));
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioQuotaEntrataResponse executeService(RicercaDettaglioQuotaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		SubdocumentoEntrata subdoc = subdocumentoEntrataDad.findSubdocumentoEntrataById(subdocumentoEntrata.getUid());
		
		if(subdoc==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("subdocumento entrata", "id: "+subdocumentoEntrata.getUid()));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		
		res.setSubdocumentoEntrata(subdoc);
		
	}

}

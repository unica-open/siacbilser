/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CodiceCommissioneDocumentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceCommissioneDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceCommissioneDocumentoResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class RicercaCodiceBolloService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCodiceCommissioneDocumentoService extends CheckedAccountBaseService<RicercaCodiceCommissioneDocumento, RicercaCodiceCommissioneDocumentoResponse>{
	
	@Autowired
	private CodiceCommissioneDocumentoDad codiceCommissioneDocumentoDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {	
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(req.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
	}
	
	@Transactional
	@Override
	public RicercaCodiceCommissioneDocumentoResponse executeService(RicercaCodiceCommissioneDocumento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		res.setElencoCodiciCommissioneDocumento(codiceCommissioneDocumentoDad.ricercaCodiciCommissioneDocumento(ente));
		res.setDataOra(new Date());
		res.setEsito(Esito.SUCCESSO);
	}
}

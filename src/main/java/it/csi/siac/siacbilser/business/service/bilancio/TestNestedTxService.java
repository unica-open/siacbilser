/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.bilancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * Servizio per la ricerca di dettaglio del bilancio.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 09/01/2014
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TestNestedTxService extends CheckedAccountBaseService<RicercaDettaglioBilancio, RicercaDettaglioBilancioResponse> {
	
	/** The bilancio dad. */
	@Autowired
	private BilancioDad bilancioDad;
	
	@Autowired
	private RicercaDettaglioBilancioService ricercaDettaglioBilancioService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getChiaveBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave bilancio"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional(readOnly= true)
	public RicercaDettaglioBilancioResponse executeService(RicercaDettaglioBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {		
		res = ricercaDettaglioBilancioService.executeServiceTxNested(req);
	}

}

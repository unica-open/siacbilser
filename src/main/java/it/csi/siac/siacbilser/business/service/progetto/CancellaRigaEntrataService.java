/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaEntrata;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRigaEntrataResponse;
import it.csi.siac.siacbilser.integration.dad.CronoprogrammaDad;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

// TODO: Auto-generated Javadoc
/**
 * The Class CancellaRigaEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CancellaRigaEntrataService extends CheckedAccountBaseService<CancellaRigaEntrata, CancellaRigaEntrataResponse> {
		
	/** The dett. */
	private DettaglioEntrataCronoprogramma dett;
	
	/** The cronoprogramma dad. */
	@Autowired
	private CronoprogrammaDad cronoprogrammaDad;

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		dett = req.getDettaglioEntrataCronoprogramma();
		checkNotNull(dett, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettaglio entrata programma"));
		checkCondition(dett.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid dettaglio entrata programma"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public CancellaRigaEntrataResponse executeService(CancellaRigaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		//cronoprogrammaDad.setEnte(dett.getEnte());
		//cronoprogrammaDad.setBilancio(dett.getBilancio());
		cronoprogrammaDad.setLoginOperazione(loginOperazione);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		cronoprogrammaDad.cancellaDettaglioEntrataCronoprogramma(dett);
		res.setDettaglioEntrataCronoprogramma(dett);
	}


}

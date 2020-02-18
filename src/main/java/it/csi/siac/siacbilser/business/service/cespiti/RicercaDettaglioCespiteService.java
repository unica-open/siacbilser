/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioCespiteService extends CheckedAccountBaseService<RicercaDettaglioCespite, RicercaDettaglioCespiteResponse> {

	// DAD
	@Autowired
	private CespiteDad cespiteDad;

	private Cespite cespite;

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		cespite = req.getCespite();
		checkEntita(cespite, "cespite");
		
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
		cespiteDad.setLoginOperazione(loginOperazione);		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public RicercaDettaglioCespiteResponse executeService(RicercaDettaglioCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		Cespite cc = cespiteDad.findCespiteById(cespite, req.getListaCespiteModelDetails());
		if (cc == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("cespite ", "uid:" + cespite.getUid()));
		}		
		res.setCespite(cc);
	}

	

}
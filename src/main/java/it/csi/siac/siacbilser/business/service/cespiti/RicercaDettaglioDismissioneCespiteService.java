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
import it.csi.siac.siacbilser.integration.dad.DismissioneCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioDismissioneCespiteResponse;
import it.csi.siac.siaccespser.model.DismissioneCespite;
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
public class RicercaDettaglioDismissioneCespiteService extends CheckedAccountBaseService<RicercaDettaglioDismissioneCespite, RicercaDettaglioDismissioneCespiteResponse> {

	// DAD
	@Autowired
	private DismissioneCespiteDad dismissioneDismissioneCespiteDad;

	private DismissioneCespite dismissioneDismissioneCespite;

	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		dismissioneDismissioneCespite = req.getDismissioneCespite();
		checkEntita(dismissioneDismissioneCespite, "dismissione cespite");
		
	}
	
	@Override
	protected void init() {
		super.init();
		dismissioneDismissioneCespiteDad.setEnte(ente);
		dismissioneDismissioneCespiteDad.setLoginOperazione(loginOperazione);		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Transactional
	@Override
	public RicercaDettaglioDismissioneCespiteResponse executeService(RicercaDettaglioDismissioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		
		DismissioneCespite cc = dismissioneDismissioneCespiteDad.findDismissioneCespiteById(dismissioneDismissioneCespite, req.getListaDismissioneCespiteModelDetails());
		if (cc == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("dismissioneDismissioneCespite ", "uid:" + dismissioneDismissioneCespite.getUid()));
		}		
		res.setDismissioneCespite(cc);
	}

	

}
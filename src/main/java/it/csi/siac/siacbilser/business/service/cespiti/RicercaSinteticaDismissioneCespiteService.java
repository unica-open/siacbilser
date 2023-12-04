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
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.DismissioneCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.DismissioneCespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;


/**
 * 
 * @author Antonino
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaDismissioneCespiteService extends CheckedAccountBaseService<RicercaSinteticaDismissioneCespite, RicercaSinteticaDismissioneCespiteResponse> {

	//DAD
	@Autowired
	private DismissioneCespiteDad dismissioneCespiteDad;

	private DismissioneCespite dismissioneCespite;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		dismissioneCespite = req.getDismissioneCespite();
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
	}
	
	@Override
	protected void init() {
		super.init();
		dismissioneCespiteDad.setEnte(ente);
		dismissioneCespiteDad.setLoginOperazione(loginOperazione);
	}
	
	@Transactional
	@Override
	public RicercaSinteticaDismissioneCespiteResponse executeService(RicercaSinteticaDismissioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Utility.MDTL.addModelDetails(req.getModelDetails());
		ListaPaginata<DismissioneCespite> listaDismissioneCespite = dismissioneCespiteDad.ricercaSinteticaDismissioneCespite(req.getDismissioneCespite(), 
				req.getAttoAmministrativo(), 
				req.getEvento(), 
				req.getCausaleEP(),
				req.getCespite(),
				Utility.MDTL.byModelDetailClass(DismissioneCespiteModelDetail.class), req.getParametriPaginazione());
		res.setListaDismissioneCespite(listaDismissioneCespite);
	}
	
}
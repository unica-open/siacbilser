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
import it.csi.siac.siacbilser.integration.dad.VariazioneCespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespiteModelDetail;
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
public class RicercaSinteticaVariazioneCespiteService extends CheckedAccountBaseService<RicercaSinteticaVariazioneCespite, RicercaSinteticaVariazioneCespiteResponse> {

	//DAD
	@Autowired
	private VariazioneCespiteDad variazioneCespiteDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametri paginazione"));
		
//		checkCondition(req.getNumeroInventarioDa() == null || req.getNumeroInventarioA() == null
//				|| req.getNumeroInventarioDa().compareTo(req.getNumeroInventarioA()) <= 0,
//				ErroreCore.VALORE_NON_CONSENTITO.getErrore("numero inventario da/a", "il numero da non puo' essere superiore al numero a"));
	}
	
	@Override
	protected void init() {
		super.init();
		variazioneCespiteDad.setEnte(ente);
		variazioneCespiteDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaVariazioneCespiteResponse executeService(RicercaSinteticaVariazioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Utility.MDTL.addModelDetails(req.getModelDetails());
		ListaPaginata<VariazioneCespite> listaVariazioneCespite = variazioneCespiteDad.ricercaSinteticaVariazioneCespite(
				req.getVariazioneCespite(),
				Utility.MDTL.byModelDetailClass(VariazioneCespiteModelDetail.class),
				req.getParametriPaginazione());
		res.setListaVariazioneCespite(listaVariazioneCespite);
	}
	
}
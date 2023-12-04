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
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespiteResponse;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Ricerca di dettaglio della variazione cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/08/2018
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioVariazioneCespiteService extends CheckedAccountBaseService<RicercaDettaglioVariazioneCespite, RicercaDettaglioVariazioneCespiteResponse> {

	// DAD
	@Autowired
	private VariazioneCespiteDad variazioneCespiteDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getVariazioneCespite(), "variazione cespite");
	}

	@Override
	protected void init() {
		super.init();
		variazioneCespiteDad.setEnte(ente);
		variazioneCespiteDad.setLoginOperazione(loginOperazione);
	}

	@Override
	@Transactional(readOnly = true)
	public RicercaDettaglioVariazioneCespiteResponse executeService(RicercaDettaglioVariazioneCespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		Utility.MDTL.addModelDetails(req.getModelDetails());
		VariazioneCespite variazioneCespite = variazioneCespiteDad.findVariazioneCespiteById(req.getVariazioneCespite(),
				Utility.MDTL.byModelDetailClass(VariazioneCespiteModelDetail.class));
		if(variazioneCespite == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Variazione cespite", "uid " + req.getVariazioneCespite().getUid()));
		}
		res.setVariazioneCespite(variazioneCespite);
	}
}

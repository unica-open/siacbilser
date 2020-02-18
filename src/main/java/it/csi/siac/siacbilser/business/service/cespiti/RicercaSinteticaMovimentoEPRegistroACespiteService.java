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
import it.csi.siac.siacbilser.integration.dad.PrimaNotaInvDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoEPRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoEPRegistroACespiteResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.MovimentoEPModelDetail;

/**
 * Ricerca sintatica del registro A per il cespite.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/10/2018
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Deprecated
public class RicercaSinteticaMovimentoEPRegistroACespiteService extends CheckedAccountBaseService<RicercaSinteticaMovimentoEPRegistroACespite, RicercaSinteticaMovimentoEPRegistroACespiteResponse> {

	@Autowired
	private PrimaNotaInvDad primaNotaInvDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkParametriPaginazione(req.getParametriPaginazione(), false);
		checkEntita(req.getPrimaNota(), "prima nota");
	}
	
	@Override
	protected void init() {
		primaNotaInvDad.setEnte(ente);
		Utility.MDTL.addModelDetails(req.getModelDetails());
	}

	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaMovimentoEPRegistroACespiteResponse executeService(RicercaSinteticaMovimentoEPRegistroACespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		ListaPaginata<MovimentoEP> listaMovimentoEP = primaNotaInvDad.ricercaSinteticaMovimentoEPRegistroA(req.getPrimaNota(), req.getParametriPaginazione(), Utility.MDTL.byModelDetailClass(MovimentoEPModelDetail.class));
		res.setMovimentiEP(listaMovimentoEP);
	}
	
}

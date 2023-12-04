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
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoDettaglioRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoDettaglioModelDetail;

/**
 * @author elisa
 * @version 1.0.0 - 14-01-2019
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaMovimentoDettaglioRegistroACespiteService extends CheckedAccountBaseService<RicercaSinteticaMovimentoDettaglioRegistroACespite, RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse> {

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
	public RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse executeService(RicercaSinteticaMovimentoDettaglioRegistroACespite serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		ListaPaginata<MovimentoDettaglio> listaMovimentoDet = primaNotaInvDad.ricercaSinteticaMovimentoDettaglioRegistroA(req.getPrimaNota(), req.getParametriPaginazione(), Utility.MDTL.byModelDetailClass(MovimentoDettaglioModelDetail.class));
		res.setMovimentiDettaglio(listaMovimentoDet);
	}
	
}

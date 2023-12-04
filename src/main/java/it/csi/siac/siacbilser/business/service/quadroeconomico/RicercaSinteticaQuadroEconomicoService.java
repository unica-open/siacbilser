/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.quadroeconomico;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomico;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaSinteticaQuadroEconomicoResponse;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaQuadroEconomicoService extends CrudQuadroEconomicoBaseService<RicercaSinteticaQuadroEconomico, RicercaSinteticaQuadroEconomicoResponse> {
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.quadroEconomico = req.getQuadroEconomico();
		checkNotNull(quadroEconomico, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quadro economico"));
		checkNotNull(req.getParametriPaginazione(), "parametri paginazione");
		
	}
	
	@Override
	@Transactional
	public RicercaSinteticaQuadroEconomicoResponse executeService(RicercaSinteticaQuadroEconomico serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		
		caricaQuadroEconomico();
	}

	private void caricaQuadroEconomico() {
		ListaPaginata<QuadroEconomico> listaquadroEconomico = quadroEconomicoDad.ricercaSinteticaQuadroEconomico(req.getQuadroEconomico(), req.getParametriPaginazione());
		res.setQuadroEconomico(listaquadroEconomico);
		res.setCardinalitaComplessiva(listaquadroEconomico.getTotaleElementi());
	}

}

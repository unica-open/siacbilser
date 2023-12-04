/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.movimentogestione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioAccertamento;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioAccertamentoResponse;
import it.csi.siac.siacbilser.integration.dad.AccertamentoBilDad;
import it.csi.siac.siacfinser.model.Accertamento;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioAccertamentoService extends RicercaDettaglioMovimentoGestioneService<Accertamento, RicercaDettaglioAccertamento, RicercaDettaglioAccertamentoResponse> {
	
	private @Autowired AccertamentoBilDad accertamentoBilDad;
	
	@Override
	@Transactional
	public RicercaDettaglioAccertamentoResponse executeService(RicercaDettaglioAccertamento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		Accertamento accertamento = accertamentoBilDad.ricercaDettaglioAccertamento(req.getAccertamento(), req.getAccertamentoModelDetails());
		res.setAccertamento(accertamento);
	}

}

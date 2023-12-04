/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RichiestaEconomaleDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaMezziDiTrasporto;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaMezziDiTrasportoResponse;
import it.csi.siac.siaccecser.model.MezziDiTrasporto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaMezziDiTrasportoService extends CheckedAccountBaseService<RicercaMezziDiTrasporto, RicercaMezziDiTrasportoResponse> {
	
	@Autowired
	private RichiestaEconomaleDad richiestaEconomaleDad;
	
	@Override
	@Transactional(readOnly = true)
	public RicercaMezziDiTrasportoResponse executeService(RicercaMezziDiTrasporto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		richiestaEconomaleDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		List<MezziDiTrasporto> list = richiestaEconomaleDad.ricercaMezziDiTrasporto();
		res.setMezziDiTrasporto(list);
		res.setCardinalitaComplessiva(list.size());
	}

}

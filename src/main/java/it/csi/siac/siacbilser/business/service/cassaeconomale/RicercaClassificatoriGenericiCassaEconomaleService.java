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
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriGenericiCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriGenericiCassaEconomaleResponse;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaClassificatoriGenericiCassaEconomaleService extends CheckedAccountBaseService<RicercaClassificatoriGenericiCassaEconomale, RicercaClassificatoriGenericiCassaEconomaleResponse> {
	
	@Autowired
	private RichiestaEconomaleDad richiestaEconomaleDad;
	
	@Override
	protected void init() {
		richiestaEconomaleDad.setEnte(ente);
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaClassificatoriGenericiCassaEconomaleResponse executeService(RicercaClassificatoriGenericiCassaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		List<ClassificatoreGenerico> classificatori = richiestaEconomaleDad.ricercaClassificatoriGenerici();
		res.setListaClassificatori(classificatori);
		res.setCardinalitaComplessiva(classificatori.size());
	}

}

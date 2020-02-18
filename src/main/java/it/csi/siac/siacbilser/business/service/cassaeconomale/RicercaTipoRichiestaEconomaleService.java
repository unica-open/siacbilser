/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoRichiestaEconomaleResponse;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaTipoRichiestaEconomaleService extends CheckedAccountBaseService<RicercaTipoRichiestaEconomale, RicercaTipoRichiestaEconomaleResponse> {

	@Override
	@Transactional(readOnly = true)
	public RicercaTipoRichiestaEconomaleResponse executeService(RicercaTipoRichiestaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	
	@Override
	protected void execute() {
		// TODO Auto-generated method stub
	}

}

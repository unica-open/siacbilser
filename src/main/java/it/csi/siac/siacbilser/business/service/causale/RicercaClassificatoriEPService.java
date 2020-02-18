/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.causale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CausaleEPDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriEP;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriEPResponse;
import it.csi.siac.siacgenser.model.ClassificatoreEP;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaClassificatoriEPService extends CheckedAccountBaseService<RicercaClassificatoriEP, RicercaClassificatoriEPResponse> {
	
	@Autowired
	private CausaleEPDad causaleEPDad;
	
	@Override
	protected void init() {
		causaleEPDad.setEnte(ente);
	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaClassificatoriEPResponse executeService(RicercaClassificatoriEP serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		List<ClassificatoreEP> classificatori = causaleEPDad.ricercaClassificatoriEP();
		res.setListaClassificatori(classificatori);
		res.setCardinalitaComplessiva(classificatori.size());
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ContoDad;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassiPianoAmmortamento;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaClassiPianoAmmortamentoResponse;
import it.csi.siac.siacgenser.model.ClassePiano;

/**
 * The Class RicercaDettaglioContoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaClassiPianoAmmortamentoService extends CheckedAccountBaseService<RicercaClassiPianoAmmortamento, RicercaClassiPianoAmmortamentoResponse> {

	//DAD
	@Autowired
	protected ContoDad contoDad;
	

	
	@Override
	protected void init() {
		super.init();
		contoDad.setEnte(ente);
		contoDad.setLoginOperazione(loginOperazione);

	}
	
	@Override
	@Transactional(readOnly=true)
	public RicercaClassiPianoAmmortamentoResponse executeService(RicercaClassiPianoAmmortamento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		List<ClassePiano> listaClassi = contoDad.findClassiPianoAmmortamento();
		res.setClassiPiano(listaClassi);
	}
	

}

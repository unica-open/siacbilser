/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciMutuoService extends BaseInserisciAggiornaMutuoService<InserisciMutuo, InserisciMutuoResponse> {
	
	@Override
	@Transactional
	public InserisciMutuoResponse executeService(InserisciMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		super.checkServiceParam();
		
		checkCondition(req.getGeneraNumeroMutuo() || mutuo.getNumero()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numeroMutuo"), true);
		
	}
	
	@Override
	protected void init() {
		super.init();
		mutuo.setLoginCreazione(loginOperazione);
	}	
	
	@Override
	protected void execute() {
		
		super.execute();
		
		res.setMutuo(inserisciMutuo());
	}
	
	protected Mutuo inserisciMutuo() {
		final String methodName = "inserisciMutuo";

		Mutuo mutuoInserito = mutuoDad.create(mutuo);
		
		log.debug(methodName, "Inserito mutuo con uid " + mutuoInserito.getUid());
		
		return mutuoInserito;
	}
	
	@Override
	protected void checkMutuo() {}
}

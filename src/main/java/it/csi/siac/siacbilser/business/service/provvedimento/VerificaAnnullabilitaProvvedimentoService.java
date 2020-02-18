/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.provvedimento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.frontend.webservice.msg.VerificaAnnullabilitaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.VerificaAnnullabilitaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

/**
 * The Class VerificaAnnullabilitaProvvedimentoService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VerificaAnnullabilitaProvvedimentoService extends CheckedAccountBaseService<VerificaAnnullabilitaProvvedimento, VerificaAnnullabilitaProvvedimentoResponse> {
	
	/** The provvedimento dad. */
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
	
	 
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getAttoAmministrativo(), "atto amministrativo",false);
		req.getAttoAmministrativo().setEnte(ente);
	}
	
	@Transactional
	public VerificaAnnullabilitaProvvedimentoResponse executeService(VerificaAnnullabilitaProvvedimento serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		provvedimentoDad.setLoginOperazione(loginOperazione);
		provvedimentoDad.setEnte(ente);
	}

	@Override
	protected void execute() {
		
		Boolean isAnnullabile = isAnnullabile(req.getAttoAmministrativo());
		res.setAnnullabilitaAttoAmministrativo(isAnnullabile);
		
	}

	protected Boolean isAnnullabile(AttoAmministrativo attoAmministrativo) {
		return provvedimentoDad.isAnnullabile(attoAmministrativo);
	}





	
}

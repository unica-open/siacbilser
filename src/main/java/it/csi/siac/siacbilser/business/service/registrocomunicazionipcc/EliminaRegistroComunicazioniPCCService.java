/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.registrocomunicazionipcc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;

/**
 * The Class AggiornaRegistroComunicazioniPCCService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaRegistroComunicazioniPCCService extends CheckedAccountBaseService<EliminaRegistroComunicazioniPCC, EliminaRegistroComunicazioniPCCResponse> {
	
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	
	private RegistroComunicazioniPCC registroComunicazioniPCC;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getRegistroComunicazioniPCC(), "registro comunicazioni pcc");
		registroComunicazioniPCC = req.getRegistroComunicazioniPCC();
	}
	
	@Override
	@Transactional
	public EliminaRegistroComunicazioniPCCResponse executeService(EliminaRegistroComunicazioniPCC serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		registroComunicazioniPCCDad.eliminaRegistroComunicazioniPCC(registroComunicazioniPCC);
		res.setRegistroComunicazioniPCC(registroComunicazioniPCC);
	}

}

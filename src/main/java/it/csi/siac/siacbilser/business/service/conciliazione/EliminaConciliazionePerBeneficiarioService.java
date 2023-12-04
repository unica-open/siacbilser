/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conciliazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ConciliazioneDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerBeneficiario;

/**
 * Aggiornamento della conciliazione per titolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/10/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EliminaConciliazionePerBeneficiarioService extends CheckedAccountBaseService<EliminaConciliazionePerBeneficiario, EliminaConciliazionePerBeneficiarioResponse> {

	@Autowired
	private ConciliazioneDad conciliazioneDad;

	private ConciliazionePerBeneficiario conciliazionePerBeneficiario;

	@Override
	@Transactional
	public EliminaConciliazionePerBeneficiarioResponse executeService(EliminaConciliazionePerBeneficiario serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getConciliazionePerBeneficiario(), "conciliazione", true);
		conciliazionePerBeneficiario = req.getConciliazionePerBeneficiario();
	}
	
	@Override
	protected void init() {
		conciliazioneDad.setEnte(ente);
		conciliazioneDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		conciliazioneDad.eliminaConciliazionePerBeneficiario(conciliazionePerBeneficiario);
		res.setConciliazionePerBeneficiario(conciliazionePerBeneficiario);
	}

}

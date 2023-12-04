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
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerBeneficiario;

/**
 * Ricerca di dettaglio della conciliazione per titolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/10/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioConciliazionePerBeneficiarioService extends CheckedAccountBaseService<RicercaDettaglioConciliazionePerBeneficiario, RicercaDettaglioConciliazionePerBeneficiarioResponse> {

	@Autowired
	private ConciliazioneDad conciliazioneDad;
	private ConciliazionePerBeneficiario conciliazionePerBeneficiario;

	@Override
	@Transactional(readOnly = true)
	public RicercaDettaglioConciliazionePerBeneficiarioResponse executeService(RicercaDettaglioConciliazionePerBeneficiario serviceRequest) {
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
		conciliazionePerBeneficiario = conciliazioneDad.findConciliazionePerBeneficiarioByUid(conciliazionePerBeneficiario.getUid());
		res.setConciliazionePerBeneficiario(conciliazionePerBeneficiario);
	}

}

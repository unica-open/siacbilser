/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.conciliazione;

import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerBeneficiarioResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerBeneficiario;

/**
 * Inserimento di multiple conciliazioni per beneficiario.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/11/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceConciliazioniPerBeneficiarioService extends CheckedAccountBaseService<InserisceConciliazioniPerBeneficiario, InserisceConciliazioniPerBeneficiarioResponse> {

	@Override
	@Transactional
	public InserisceConciliazioniPerBeneficiarioResponse executeService(InserisceConciliazioniPerBeneficiario serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio", false);
		checkNotNull(req.getConciliazioniPerBeneficiario(), "conciliazioni", true);
		checkCondition(!req.getConciliazioniPerBeneficiario().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("conciliazioni"));
		
		int i = 1;
		for(ConciliazionePerBeneficiario cpb : req.getConciliazioniPerBeneficiario()) {
			if(cpb == null) {
				continue;
			}
			checkEntita(cpb.getSoggetto(), "soggetto conciliazione " + i, false);
			checkEntita(cpb.getCapitolo(), "capitolo conciliazione " + i, false);
			checkEntita(cpb.getConto(), "conto conciliazione " + i, false);
			checkEntita(cpb.getEnte(), "ente conciliazione " + i, false);
			checkNotNull(cpb.getClasseDiConciliazione(), "classe di conciliazione " + i, false);
			
			i++;
		}
	}
	
	@Override
	protected void execute() {
		for(ConciliazionePerBeneficiario conciliazionePerBeneficiario : req.getConciliazioniPerBeneficiario()) {
			if(conciliazionePerBeneficiario != null) {
				elaboraConciliazionePerBeneficiario(conciliazionePerBeneficiario);
			}
		}
	}

	private void elaboraConciliazionePerBeneficiario(ConciliazionePerBeneficiario conciliazionePerBeneficiario) {
		InserisceConciliazionePerBeneficiario reqICPC = new InserisceConciliazionePerBeneficiario();
		
		reqICPC.setBilancio(req.getBilancio());
		reqICPC.setDataOra(new Date());
		reqICPC.setRichiedente(req.getRichiedente());
		reqICPC.setConciliazionePerBeneficiario(conciliazionePerBeneficiario);
		
		InserisceConciliazionePerBeneficiarioResponse resICPC = serviceExecutor.executeServiceSuccess(InserisceConciliazionePerBeneficiarioService.class, reqICPC);
		ConciliazionePerBeneficiario conciliazioneResponse = resICPC.getConciliazionePerBeneficiario();
		res.getConciliazioniPerBeneficiario().add(conciliazioneResponse);
	}

}

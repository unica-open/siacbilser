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
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.integration.dad.ConciliazioneDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.model.ConciliazionePerBeneficiario;

/**
 * Ricerca di sintetica della conciliazione per beneficiario.
 * 
 * @author Valentina
 * @version 1.0.0 - 28/10/2015
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaConciliazionePerBeneficiarioService extends CheckedAccountBaseService<RicercaSinteticaConciliazionePerBeneficiario, RicercaSinteticaConciliazionePerBeneficiarioResponse> {

	@Autowired
	private ConciliazioneDad conciliazioneDad;
	@Autowired
	private CapitoloDad capitoloDad;
	
	private ConciliazionePerBeneficiario conciliazionePerBeneficiario;

	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaConciliazionePerBeneficiarioResponse executeService(RicercaSinteticaConciliazionePerBeneficiario serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getParametriPaginazione(), "parametri di paginazione");
		checkNotNull(req.getConciliazionePerBeneficiario(), "conciliazione", true);
		conciliazionePerBeneficiario = req.getConciliazionePerBeneficiario();
		checkEntita(conciliazionePerBeneficiario.getSoggetto(), "soggetto");
		checkNotNull(conciliazionePerBeneficiario.getCapitolo().getAnnoCapitolo(), "Anno");
	}
	
	@Override
	protected void init() {
		conciliazioneDad.setEnte(ente);
		conciliazioneDad.setLoginOperazione(loginOperazione);
		capitoloDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		ListaPaginata<ConciliazionePerBeneficiario> lista = conciliazioneDad.ricercaSinteticaConciliazioniPerBeneficiario(conciliazionePerBeneficiario, req.getParametriPaginazione());
		
		res.setConciliazioni(lista);
		if(lista == null || lista.isEmpty()){
			res.addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
		}
	}

}

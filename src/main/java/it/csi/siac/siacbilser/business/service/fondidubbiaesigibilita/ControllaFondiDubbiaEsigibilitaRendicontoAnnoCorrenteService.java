/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrenteResponse;
import it.csi.siac.siacbilser.integration.dad.AccantonamentoFondiDubbiaEsigibilitaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

/**
 * Controlla i fondi a dubbia e difficile esazione dall'anno precedente, rendiconto
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrenteService 
	extends CheckedAccountBaseService<ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente, ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrenteResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaDad accantonamentoFondiDubbiaEsigibilitaDad;
	
	@Override
	@Transactional(readOnly = true)
	public ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrenteResponse executeService(ControllaFondiDubbiaEsigibilitaRendicontoAnnoCorrente serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		//controllo di avere l'uid del bilancio
		checkEntita(req.getBilancio(), "bilancio", false);
	}
	
	@Override
	protected void execute() {
		//calcolo il numero di fondi associati al bilancio
		ottieniFondiAnnoCorrente();
	}
	
	private void ottieniFondiAnnoCorrente() {
		final String methodName = "ottieniFondiAnnoCorrente";
		//chiamo il dad per avere il numero di accantonamenti presenti per il bilancio
		Long numeroFondiAnnoCorrente = accantonamentoFondiDubbiaEsigibilitaDad.countByBilancio(req.getBilancio());
		log.debug(methodName, "Numero fondi anno corrente: " + numeroFondiAnnoCorrente);
		//imposto in response il numero di fondi
		res.setNumeroFondiAnnoCorrente(numeroFondiAnnoCorrente);
	}

}

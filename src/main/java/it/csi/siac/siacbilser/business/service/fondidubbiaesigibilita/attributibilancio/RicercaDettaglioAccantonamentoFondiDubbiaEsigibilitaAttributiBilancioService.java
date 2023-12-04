/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

/**
 * Ricerca degli attributi del bilancio
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioService extends CheckedAccountBaseService<RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio, RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;

	@Override
	@Transactional(readOnly = true)
	public RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse executeService(RicercaDettaglioAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio");
	}
	
	@Override
	protected void execute() {
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.findById(
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getUid(),
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails());
		res.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
	}

}

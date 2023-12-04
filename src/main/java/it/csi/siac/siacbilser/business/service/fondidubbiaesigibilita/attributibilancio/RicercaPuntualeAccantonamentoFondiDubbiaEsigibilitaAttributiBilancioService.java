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
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
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
public class RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioService extends CheckedAccountBaseService<RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio, RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;

	@Override
	@Transactional(readOnly = true)
	public RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse executeService(RicercaPuntualeAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio");
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getBilancio(), "bilancio attributi bilancio", false);
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getTipoAccantonamentoFondiDubbiaEsigibilita(), "tipo accantonamento attributi bilancio", false);
		// Versione facoltativa: se non passata, recupera l'ultima versione
	}
	
	@Override
	protected void execute() {
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.getByBilancioAndTipoAndVersione(
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getBilancio(),
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getTipoAccantonamentoFondiDubbiaEsigibilita(),
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getVersione(),
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails());
		res.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
	}

}

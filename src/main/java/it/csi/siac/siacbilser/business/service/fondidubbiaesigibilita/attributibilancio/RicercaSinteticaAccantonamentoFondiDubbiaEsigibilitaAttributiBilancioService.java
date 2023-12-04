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
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * Ricerca degli attributi del bilancio
 * 
 * @author Marchino Alessandro
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioService extends CheckedAccountBaseService<RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio, RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;

	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse executeService(RicercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(), "attributi bilancio");
		checkEntita(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getBilancio(), "bilancio attributi bilancio", false);
		checkNotNull(req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getTipoAccantonamentoFondiDubbiaEsigibilita(), "tipo accantonamento attributi bilancio", false);
		
		checkCondition(!Boolean.TRUE.equals(req.getExcludeCurrent()) || req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio().getVersione() != null,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("versione se richiesto di escludere la versione corrente"), false);
		checkParametriPaginazione(req.getParametriPaginazione(), false);
	}
	
	@Override
	protected void execute() {
		ListaPaginata<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio> listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.ricercaSinteticaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(),
				req.getExcludeCurrent(),
				req.getParametriPaginazione(),
				req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails());
		res.setListaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(listaAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
	}

}

/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.business.service.fondidubbiaesigibilita.attributibilancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.fcde.attributibilancio.ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Ricerca degli attributi del bilancio
 * 
 * @author Alessandro Todesco
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioService extends CheckedAccountBaseService<ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio, ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse> {

	//DADs
	@Autowired
	private AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad;
	
	@Override
	@Transactional()
	public ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioResponse executeService(ImpostaDefaultAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio serviceRequest) {
		//eseguo il servizio
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		//imposto ente e login operazione nei dad
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.setLoginOperazione(loginOperazione);
		accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad.setEnte(ente);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getBilancio(), "bilancio");
		checkNotNull(req.getStatoAccantonamentoFondiDubbiaEsigibilita(), "stato versione");
		checkCondition(StatoAccantonamentoFondiDubbiaEsigibilita.BOZZA.equals(req.getStatoAccantonamentoFondiDubbiaEsigibilita()), ErroreCore.FORMATO_NON_VALIDO.getErrore("stato versione"));
		checkNotNull(req.getTipoAccantonamentoFondiDubbiaEsigibilita(), "tipo versione");
	}
	
	@Override
	protected void execute() {
		// Ricerca di una versione valida della tipologia scelta appartenente al bilancio 
		AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = 
			accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad
				.getByBilancioAndTipoAndVersione(
					req.getBilancio(),
					req.getTipoAccantonamentoFondiDubbiaEsigibilita(),
					null,
					req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails());
		
		// Se non ci sono corrispondenze eseguo l'init
		if(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio == null) {
			accantonamentoFondiDubbiaEsigibilitaAttributiBilancio = accantonamentoFondiDubbiaEsigibilitaAttributiBilancioDad
				.initDefaultVersion(
					req.getBilancio(),
					req.getStatoAccantonamentoFondiDubbiaEsigibilita(),
					req.getTipoAccantonamentoFondiDubbiaEsigibilita(),
					req.getAccantonamentoFondiDubbiaEsigibilitaAttributiBilancioModelDetails());
		}
		
		res.setAccantonamentoFondiDubbiaEsigibilitaAttributiBilancio(accantonamentoFondiDubbiaEsigibilitaAttributiBilancio);
	}

}


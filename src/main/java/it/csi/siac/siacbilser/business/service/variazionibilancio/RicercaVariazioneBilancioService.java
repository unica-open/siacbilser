/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.integration.dad.ImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.dad.VariazioniDad;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * The Class RicercaVariazioneBilancioService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaVariazioneBilancioService extends CheckedAccountBaseService<RicercaVariazioneBilancio, RicercaVariazioneBilancioResponse> {

	/** The variazioni dad. */
	@Autowired
	protected VariazioniDad variazioniDad;
	
	/** The importi capitolo dad. */
	@Autowired
	protected ImportiCapitoloDad importiCapitoloDad;
	
	/** The variazione. */
	protected VariazioneImportoCapitolo variazione;
	
	@Override
	protected void init() {
		variazioniDad.setEnte(variazione.getEnte());
		variazioniDad.setBilancio(variazione.getBilancio());
		variazioniDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(variazione.getEnte());
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		variazione = req.getVariazioneImportoCapitolo();
		
		checkNotNull(variazione, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("variazione"));
		checkEntita(variazione.getEnte(), "ente");
		
		checkNotNull(variazione.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkNotNull(variazione.getBilancio().getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"),false);
	}
	
	@Transactional(readOnly= true)
	public RicercaVariazioneBilancioResponse executeService(RicercaVariazioneBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		ListaPaginata<VariazioneImportoCapitolo> variazioniTrovate =  variazioniDad.ricercaSinteticaVariazioneImportoCapitolo(
				variazione,
				req.getTipiCapitolo(),
				req.getAttoAmministrativo(),
				req.isLimitaRisultatiDefinitiveODecentrate(),
				req.getParametriPaginazione());
		
		res.setVariazioniDiBilancio(variazioniTrovate);
		res.setEsito(Esito.SUCCESSO);
	}
	
}

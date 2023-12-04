/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.math.BigDecimal;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.processi.GestoreProcessiVariazioneBilancio;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserisce l'anagrafica di una Variazione Bilancio. Viene esclusa la lista di {@link DettaglioVariazioneImportoCapitolo}.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceAnagraficaVariazioneBilancioService extends VariazioneDiBilancioBaseService<InserisceAnagraficaVariazioneBilancio, InserisceAnagraficaVariazioneBilancioResponse> {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		variazione = req.getVariazioneImportoCapitolo();
		checkNotNull(variazione, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("variazione importi capitolo"));

		checkEntita(variazione.getEnte(), "ente", false);

		checkEntita(variazione.getBilancio(), "bilancio");
		checkCondition(variazione.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"), false);

		checkNotNull(variazione.getTipoVariazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo variazione"), false);
		checkNotNull(variazione.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("note"), false);
		checkNotNull(variazione.getStatoOperativoVariazioneDiBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo variazione"), false);
		
		checkNotNull(variazione.getApplicazioneVariazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("applicazione variazione"), false);

		// non vengono piu' inseriti i dettagli degli importi.
		variazione.setListaDettaglioVariazioneImporto(null);

		checkNotNull(req.getInvioOrganoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("invio organo amministrativo"), false);
		checkNotNull(req.getInvioOrganoLegislativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("invio organo legislativo"), false);
	}

	@Override
	protected void init() {
		variazioniDad.setEnte(ente);
		variazioniDad.setBilancio(variazione.getBilancio());
		variazioniDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(ente);
	}

	@Override
	@Transactional
	public InserisceAnagraficaVariazioneBilancioResponse executeService(InserisceAnagraficaVariazioneBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		checkProvvedimentoPresenteEDefinitivo();

		Integer numeroVariazione = variazioniDad.staccaNumeroVariazione();
		variazione.setNumero(numeroVariazione);
		
		//SIAC-6884: se la variazione è decentrata non dobbiamo invocare Bonita
		//SIAC-8332-REGP
		variazione.setStatoOperativoVariazioneDiBilancio(GestoreProcessiVariazioneBilancio.getStatoAvvioProcessoVariazioneDiBilancio(variazione.isDecentrata()));
		
		variazioniDad.inserisciAnagraficaVariazioneImportoCapitolo(variazione);
		
		res.setVariazioneImportoCapitolo(variazione);
		
	}

	

	private void checkProvvedimentoPresenteEDefinitivo() {
		boolean isProvvedimentoPresenteDefinitivo = isProvvedimentoPresenteDefinitivo();
		res.setIsProvvedimentoPresenteDefinitivo(Boolean.valueOf(isProvvedimentoPresenteDefinitivo));
	}
	
	@Override
	protected BigDecimal adeguaDisponibilitaVariare(BigDecimal stanziamento, BigDecimal disponibilita, String tipoDisponibilita, Capitolo<?, ?> capitolo, int delta) {
		//In inserimento il calcolo della disponibilitaVariare non va adeguato.
		return disponibilita;
	}

	@Override
	protected boolean isAnnullamentoVariazione() {
		return false;
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.TipoAzione;
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
		
		variazioniDad.inserisciAnagraficaVariazioneImportoCapitolo(variazione);
		
		res.setVariazioneImportoCapitolo(variazione);
		//SIAC-6884: se la variazione è decentrata non dobbiamo invocare Bonita
		if(!variazione.isDecentrata())
			avviaProcessoVariazioneDiBilancio();

	}

	/**
	 * Avvia processo variazione di bilancio.
	 */
	private void avviaProcessoVariazioneDiBilancio() {
		ExecAzioneRichiesta execAzioneRichiesta = new ExecAzioneRichiesta();
		execAzioneRichiesta.setRichiedente(req.getRichiedente());
		execAzioneRichiesta.setDataOra(new Date());

		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		Azione azione = new Azione();
		azioneRichiesta.setAzione(azione);
		azione.setTipo(TipoAzione.AVVIO_PROCESSO);
		azione.setNomeProcesso("VariazioneDiBilancio");
		azione.setNomeTask("VariazioneDiBilancio-InserimentoVariazione");

		setVariabileProcesso(azioneRichiesta, "tipoVariazioneBilancio", "Importo");
		setVariabileProcesso(azioneRichiesta, "descrizione", variazione.getNumero() + " - " + variazione.getDescrizione() + " - "+ variazione.getTipoVariazione().getDescrizione());
		setVariabileProcesso(azioneRichiesta, "descrizioneBreve", variazione.getNumero() + " - " + variazione.getDescrizione() + " - "+ variazione.getTipoVariazione().getDescrizione());
		setVariabileProcesso(azioneRichiesta, "siacAnnoEsercizioProcesso","SIAC-ANNO-ESERCIZIO-" + variazione.getBilancio().getAnno());
		setVariabileProcesso(azioneRichiesta, "siacEnteProprietarioProcesso","SIAC-ENTE-PROPRIETARIO-" + variazione.getEnte().getUid());
		setVariabileProcesso(azioneRichiesta, "variazioneDiBilancioDem", variazione.getUid() +"|"+variazione.getNumero());
		setVariabileProcesso(azioneRichiesta, "siacSacProcesso", "SIAC-SAC-strutture");
		setVariabileProcesso(azioneRichiesta, "invioGiunta", req.getInvioOrganoAmministrativo());
		setVariabileProcesso(azioneRichiesta, "invioConsiglio", req.getInvioOrganoLegislativo());
		setVariabileProcesso(azioneRichiesta, "annullaVariazione", Boolean.FALSE);
		// CR-2304 e 3213
		setVariabileProcesso(azioneRichiesta, "quadraturaVariazioneDiBilancio", (req.isSaltaCheckStanziamentoCassa() || Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamentoCassa())) 
				&& Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamento()) 
				&& Boolean.TRUE.equals(res.getIsProvvedimentoPresenteDefinitivo()));
		setVariabileProcesso(azioneRichiesta, "statoVariazioneDiBilancio", StatoOperativoVariazioneDiBilancio.BOZZA.toString());

		execAzioneRichiesta.setAzioneRichiesta(azioneRichiesta);

		ExecAzioneRichiestaResponse execAzioneRichiestaResponse = coreService.execAzioneRichiesta(execAzioneRichiesta);
		log.logXmlTypeObject(execAzioneRichiestaResponse, "Risposta ottenuta dal servizio ExecAzioneRichiesta.");
		checkServiceResponseFallimento(execAzioneRichiestaResponse);
		// SIAC-6881: clono i dettagli in response
		res.setIdTask(execAzioneRichiestaResponse.getIdTask());
		res.setNomeTask(execAzioneRichiestaResponse.getNomeTask());
		res.setVariabiliDiProcesso(execAzioneRichiestaResponse.getVariabiliDiProcesso());
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
	
}

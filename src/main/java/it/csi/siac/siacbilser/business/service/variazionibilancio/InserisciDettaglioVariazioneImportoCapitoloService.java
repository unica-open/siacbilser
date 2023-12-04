/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Inserisce un singolo dettaglio di una Variazione importi.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisciDettaglioVariazioneImportoCapitoloService extends VariazioneDiBilancioBaseService<InserisciDettaglioVariazioneImportoCapitolo, InserisciDettaglioVariazioneImportoCapitoloResponse> {

	//Fields..
	private DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getDettaglioVariazioneImportoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettaglio variazione importo capitolo"));
		checkEntita(req.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo(), "variazione dettaglio variazione importo capitolo");
		
		checkEntita(req.getDettaglioVariazioneImportoCapitolo().getCapitolo(), "capitolo del dettaglio variazione importo");
	}
	
	@Override
	protected void init() {
		variazioniDad.setEnte(ente);
	}
	
	@Override
	@Transactional
	public InserisciDettaglioVariazioneImportoCapitoloResponse executeService(InserisciDettaglioVariazioneImportoCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		dettaglioVariazioneImportoCapitolo = req.getDettaglioVariazioneImportoCapitolo();
		
		checkCapitoloNonPresenteInVariazione();
		
		if(!req.isSkipLoadVariazione()) {
			popolaDatiDellaVariazione();
		}
		variazione = dettaglioVariazioneImportoCapitolo.getVariazioneImportoCapitolo();
		if(!req.isSkipLoadCapitolo()) {
			popolaDatiCapitolo();
		}
		
		checkCoerenzaTipoCapitoloApplicazioneVariazione();
		//SIAC-7972-revert
//		checkDisponibilitaVariareCapitolo();
		//SIAC-7972
		checkDisponibilitaVariareCapitoloInDiminuzione();
		
		variazioniDad.inserisciDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		res.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);

	}

	private void checkCoerenzaTipoCapitoloApplicazioneVariazione() {
		String methodName = "checkCoerenzaTipoCapitoloApplicazioneVariazione";
		
		TipoCapitolo tipoCapitolo = dettaglioVariazioneImportoCapitolo.getCapitolo().getTipoCapitolo();
		ApplicazioneVariazione applicazioneVariazione = dettaglioVariazioneImportoCapitolo.getVariazioneImportoCapitolo().getApplicazioneVariazione();
		
		boolean isCoerente = (TipoCapitolo.isTipoCapitoloPrevisione(tipoCapitolo) && ApplicazioneVariazione.PREVISIONE.equals(applicazioneVariazione))
				|| (TipoCapitolo.isTipoCapitoloGestione(tipoCapitolo) && ApplicazioneVariazione.GESTIONE.equals(applicazioneVariazione));
		
		log.debug(methodName, "isCoerente: " + isCoerente + " tipoCapitolo: " + tipoCapitolo + " applicazioneVariazione: " + applicazioneVariazione);
		checkBusinessCondition(isCoerente, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il tipo del capitolo non e' coerente con il tipo di applicazione della variazione."));
	}



	private void popolaDatiCapitolo() {
		Capitolo<?,?> capitolo = capitoloDad.findOne(dettaglioVariazioneImportoCapitolo.getCapitolo().getUid());
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
		//SIAC-7658
		caricaDettaglioImportiCapitolo(dettaglioVariazioneImportoCapitolo);
	}



	private void popolaDatiDellaVariazione() {
		VariazioneImportoCapitolo variazioneImportoCapitolo = variazioniDad.findAnagraficaVariazioneImportoCapitoloByUid(dettaglioVariazioneImportoCapitolo.getVariazioneImportoCapitolo().getUid());
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
	}



	/**
	 * Controlla che il capitolo non sia gia' presente nella variazione.
	 */
	private void checkCapitoloNonPresenteInVariazione() {
		boolean capitoloPresenteInVariazione = variazioniDad.checkCapitoloAssociatoAllaVariazione(dettaglioVariazioneImportoCapitolo.getCapitolo().getUid(),
				dettaglioVariazioneImportoCapitolo.getVariazioneImportoCapitolo().getUid());
		
		checkBusinessCondition(!capitoloPresenteInVariazione, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Capitolo gia' associato alla variazione"));
	}
	@Deprecated
	private void checkDisponibilitaVariareCapitolo() {
		// SIAC-6883: verifica per tutti gli anni
		Map<String, BigDecimal> mappaImportiVariazione = variazioniDad.findStanziamentoVariazioneByUidCapitoloAndUidVariazione(Arrays.asList(dettaglioVariazioneImportoCapitolo.getCapitolo().getUid()), variazione.getUid());
		//SIAC-7267: ritengo valido il flag fondino SE  e SOLO SE ho ricevuto in input la richiesta di non caricare i dati del cpaitolo
		boolean gestioneCapitoloFondino = req.isSkipLoadCapitolo() && req.isCapitoloFondino();
		checkVariazioneImporti(dettaglioVariazioneImportoCapitolo, mappaImportiVariazione, gestioneCapitoloFondino);	
	}
	
	//SIAC-7972
	private void checkDisponibilitaVariareCapitoloInDiminuzione() {
		Integer uidCapitolo = dettaglioVariazioneImportoCapitolo.getCapitolo().getUid();
		boolean isCapitoloFondino = capitoloDad.isCapitoloFondino(uidCapitolo);
		if(isCapitoloFondino) {
			return;
		}
		
		controllaStanziamentiEComponenti(dettaglioVariazioneImportoCapitolo);
	}
	
	@Override
	protected boolean isAdeguaImporto(StatoOperativoVariazioneBilancio statoAttuale, Integer uidCapitolo) {
		return false; 
	}

	@Override
	protected boolean isAnnullamentoVariazione() {
		return false;
	}

}

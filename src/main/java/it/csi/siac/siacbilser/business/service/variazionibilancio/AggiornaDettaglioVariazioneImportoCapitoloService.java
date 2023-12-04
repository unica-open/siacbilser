/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.utils.WrapperImportiCapitoloVariatiInVariazione;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaDettaglioVariazioneImportoCapitoloService extends VariazioneDiBilancioBaseService<AggiornaDettaglioVariazioneImportoCapitolo, AggiornaDettaglioVariazioneImportoCapitoloResponse> {

	//DADs..
	//Fields..
	private DettaglioVariazioneImportoCapitolo dettaglioVariazioneImportoCapitolo;
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getDettaglioVariazioneImportoCapitolo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("dettaglio variazione importo capitolo"));
		checkEntita(req.getDettaglioVariazioneImportoCapitolo().getVariazioneImportoCapitolo(), "variazione dettaglio variazione importo capitolo");
		
		checkEntita(req.getDettaglioVariazioneImportoCapitolo().getCapitolo(), "capitolo del dettaglio variazione importo");
	}
	
	@Override
	@Transactional
	public AggiornaDettaglioVariazioneImportoCapitoloResponse executeService(AggiornaDettaglioVariazioneImportoCapitolo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		variazioniDad.setEnte(ente);
	}
	
	@Override
	protected void execute() {
		dettaglioVariazioneImportoCapitolo = req.getDettaglioVariazioneImportoCapitolo();
		checkCapitoloPresenteInVariazione();
		
		if(!req.isSkipLoadVariazione()) {
			popolaDatiDellaVariazione();
		}
		variazione = dettaglioVariazioneImportoCapitolo.getVariazioneImportoCapitolo();
		if(!req.isSkipLoadCapitolo()) {
			popolaDatiCapitolo();
		}
		
		//SIAC-7972-revert
//		loadAndCheckDisponibilitaAVariare();
		//SIAC-7972
		loadAndCheckDisponibilitaVariareCapitoloInDiminuzione();
		
		variazioniDad.aggiornaDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);
		res.setDettaglioVariazioneImportoCapitolo(dettaglioVariazioneImportoCapitolo);

	}

	private void loadAndCheckDisponibilitaAVariare() {
		//SIAC-7267 e SIAC-7658
		if(!req.isSkipLoadCapitolo() || req.isCapitoloFondino()) {
			caricaDettaglioImportiCapitolo(dettaglioVariazioneImportoCapitolo);
		}
		
		checkDisponibilitaVariareCapitolo();
	}

	private void popolaDatiCapitolo() {
		Capitolo<?,?> capitolo = capitoloDad.findOne(dettaglioVariazioneImportoCapitolo.getCapitolo().getUid());
		dettaglioVariazioneImportoCapitolo.setCapitolo(capitolo);
	}

	private void popolaDatiDellaVariazione() {
		VariazioneImportoCapitolo variazioneImportoCapitolo = variazioniDad.findAnagraficaVariazioneImportoCapitoloByUid(dettaglioVariazioneImportoCapitolo.getVariazioneImportoCapitolo().getUid());
		dettaglioVariazioneImportoCapitolo.setVariazioneImportoCapitolo(variazioneImportoCapitolo);
		variazione = dettaglioVariazioneImportoCapitolo.getVariazioneImportoCapitolo();
	}



	/**
	 * Controlla che il capitolo sia gia' presente nella variazione.
	 */
	private void checkCapitoloPresenteInVariazione() {
		boolean capitoloPresenteInVariazione = variazioniDad.checkCapitoloAssociatoAllaVariazione(dettaglioVariazioneImportoCapitolo.getCapitolo().getUid(),
				dettaglioVariazioneImportoCapitolo.getVariazioneImportoCapitolo().getUid());
		
		checkBusinessCondition(capitoloPresenteInVariazione, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Capitolo non associato alla variazione"));
	}
	@Deprecated
	private void checkDisponibilitaVariareCapitolo() {
		// SIAC-6883: check disponibilita per i vari anni
		Map<String, BigDecimal> mappaImportiVariazione = variazioniDad.findStanziamentoVariazioneByUidCapitoloAndUidVariazione(Arrays.asList(dettaglioVariazioneImportoCapitolo.getCapitolo().getUid()), variazione.getUid());
		//SIAC-7267: ritengo valido il flag fondino passato in input SE  e SOLO SE ho ricevuto in input la richiesta di non caricare i dati del cpaitolo
		boolean gestioneCapitoloFondino = req.isSkipLoadCapitolo() && req.isCapitoloFondino();
		checkVariazioneImporti(dettaglioVariazioneImportoCapitolo, mappaImportiVariazione);	
	}
	
	//SIAC-7972
	private void loadAndCheckDisponibilitaVariareCapitoloInDiminuzione() {
		Integer uidCapitolo = dettaglioVariazioneImportoCapitolo.getCapitolo().getUid();
		boolean isCapitoloFondino = capitoloDad.isCapitoloFondino(uidCapitolo);
		if(isCapitoloFondino) {
			return;
		}
		
		controllaStanziamentiEComponenti(dettaglioVariazioneImportoCapitolo);
	}
	
	@Override
	protected boolean isAdeguaImporto(StatoOperativoVariazioneBilancio statoAttuale, Integer uidCapitolo) {
		return super.isAdeguaImporto(statoAttuale, uidCapitolo) && variazioniDad.checkCapitoloAssociatoAllaVariazione(uidCapitolo, variazione.getUid()); 
	}
	
	@Override
	protected BigDecimal getImportoInVariazionePrecedente(WrapperImportiCapitoloVariatiInVariazione wr) {
		BigDecimal result =  variazioniDad.getSingoloDettaglioImporto(dettaglioVariazioneImportoCapitolo.getCapitolo().getUid(), variazione.getUid(), wr.getAnno(), wr.getElemDetTipoCode());
		return result != null? result : BigDecimal.ZERO;
	}

	@Override
	protected boolean isAnnullamentoVariazione() {
		return false;
	}
}

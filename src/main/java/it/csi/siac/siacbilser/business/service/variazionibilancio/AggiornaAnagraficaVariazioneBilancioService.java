/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.AttoAmministrativoModelDetail;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.processi.GestoreProcessiVariazioneBilancio;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.CostantiFin;

/**
 * Aggiorna l'anagrafica di una variazione di Bilancio ed evolve gli stati del processo.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaAnagraficaVariazioneBilancioService extends VariazioneDiBilancioBaseService<AggiornaAnagraficaVariazioneBilancio, AggiornaAnagraficaVariazioneBilancioResponse> {

	@Override
	protected void checkServiceParam() throws ServiceParamError {

		variazione = req.getVariazioneImportoCapitolo();
		// Ho bisogno che sia impostato nella response per l'async responseHandler.
		res.setVariazioneImportoCapitolo(variazione);

		checkEntita(variazione, "variazione importi capitolo");
		checkNotNull(variazione.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero variazione importi capitolo"), false);
		checkEntita(variazione.getEnte(), "ente", false);

		checkEntita(variazione.getBilancio(), "bilancio");
		checkCondition(variazione.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"), false);

		checkNotNull(variazione.getTipoVariazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo variazione"), false);
		checkNotNull(variazione.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("note"), false);
		checkNotNull(variazione.getStatoOperativoVariazioneDiBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo variazione"), false);

		checkNotNull(variazione.getApplicazioneVariazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("applicazione variazione"), false);

		variazione.setListaDettaglioVariazioneImporto(null);

	}

	@Override
	protected void init() {
		variazioniDad.setEnte(ente);
		variazioniDad.setBilancio(variazione.getBilancio());
		variazioniDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(ente);
		attoAmministrativoDad.setEnte(variazione.getEnte());
		attoAmministrativoDad.setLoginOperazione(loginOperazione);
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT*4)//SIAC-7403
	public AggiornaAnagraficaVariazioneBilancioResponse executeServiceTxRequiresNew(AggiornaAnagraficaVariazioneBilancio serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT*4) //SIAC-7403
	public AggiornaAnagraficaVariazioneBilancioResponse executeService(AggiornaAnagraficaVariazioneBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		//SIAC-7316: questo metodo (ed i successivi) NON POSSONO STARE NEL CHECK-SERVICE PARAM!!!!		
		popolaDatiDellaVariazione();
		//SIAC-7972
		loadAndCheckImportiInDiminuzione();

		// CR-2304
		checkQuadratura();
			
		
		//SIAC-6884
		checkCodElenchi(req.isRegionePiemonte());
		
		popolaDatiProvvedimento();

		checkProvvedimentoPresenteDefinitivo();
		// SIAC-4737
		checkNecessarioAttoAmministrativoVariazioneDiBilancio();
		
		//SIAC-8332-REGP eliminata la chiamata a Bonita
		evolviProcessoVariazione();
		
		variazioniDad.aggiornaAnagraficaVariazioneImportoCapitolo(variazione);
		res.setVariazioneImportoCapitolo(variazione);
	}
	/**
	 * Evolvi processo variazione.
	 */
	protected void evolviProcessoVariazione() {
		// TODO: SIAC-6883 - valutare se spostare la chiamata a Bonita al termine, ed effettuare un aggiornamento puntuale dello stato
		//Evolve il processo solo se richiesto!
		//SIAC - 6884
		if(!Boolean.TRUE.equals(req.getEvolviProcesso())) {
			return;
		}
		
		//task-225
		StatoOperativoVariazioneBilancio statoPrecedente = variazione.getStatoOperativoVariazioneDiBilancio();
		String statoCorrente = req.getStatoCorrente();
		if (statoCorrente != null && !(statoPrecedente.getVariableName().equals(statoCorrente))) {
			eseguiOperazioniPerFallimentoPassaggioDiStatoPerModificaContingente();
			return;
		}
		
		StatoOperativoVariazioneBilancio statoSuccessivo = 
				GestoreProcessiVariazioneBilancio.getStatoSuccessivoVariazioneDiBilancio(statoPrecedente, isQuadraturaCorrettaImporti(),isQuadraturaCorrettaProvvedimenti(), variazione.getFlagGiunta(), variazione.getFlagConsiglio(), req.getAnnullaVariazione());
		if(statoSuccessivo == null) {
			eseguiOperazioniPerFallimentoPassaggioDiStato();
			return;
		}
		impostaDatiSuVariazionePerPassaggioDiStato(statoPrecedente, statoSuccessivo);
	}

	protected void impostaDatiSuVariazionePerPassaggioDiStato(StatoOperativoVariazioneBilancio statoPrecedente, StatoOperativoVariazioneBilancio statoSuccessivo) {
		variazione.setStatoOperativoVariazioneDiBilancio(statoSuccessivo);
		if(GestoreProcessiVariazioneBilancio.isChiusuraProposta(statoPrecedente, statoSuccessivo)) {
			variazione.setDataChiusuraProposta(new Date());
		}
		
	}

	private void popolaDatiProvvedimento() {
		AttoAmministrativo provvedimentoPeg = caricaProvvedimento(variazione.getAttoAmministrativo());
		variazione.setAttoAmministrativo(provvedimentoPeg);
		AttoAmministrativo provvedimentoVarBil = caricaProvvedimento(variazione.getAttoAmministrativoVariazioneBilancio());
		variazione.setAttoAmministrativoVariazioneBilancio(provvedimentoVarBil);
	}

	/**
	 * 
	 */
	private AttoAmministrativo caricaProvvedimento(AttoAmministrativo atto) {
		if(atto == null || atto.getUid() == 0){
			return atto;
		}
		return attoAmministrativoDad.findProvvedimentoByIdModelDetail(atto.getUid(), AttoAmministrativoModelDetail.TipoAtto, AttoAmministrativoModelDetail.StrutturaAmmContabile);
	}

	private void popolaDatiDellaVariazione() {
		VariazioneImportoCapitolo variazioneImportoCapitolo = variazioniDad.findVariazioneImportoCapitoloByUid(variazione.getUid());
		variazione.setListaDettaglioVariazioneImporto(variazioneImportoCapitolo.getListaDettaglioVariazioneImporto());
		//SIAC-8332
		variazione.setStatoOperativoVariazioneDiBilancio(variazioneImportoCapitolo.getStatoOperativoVariazioneDiBilancio());
		
		//SIAC-6884
		if(variazioneImportoCapitolo.getDataAperturaProposta() != null){
			variazione.setDataAperturaProposta(variazioneImportoCapitolo.getDataAperturaProposta());
			variazione.setDirezioneProponente(variazioneImportoCapitolo.getDirezioneProponente());
			variazione.setDataChiusuraProposta(variazioneImportoCapitolo.getDataChiusuraProposta());
		}
		variazione.setFlagConsiglio(variazioneImportoCapitolo.getFlagConsiglio());
		variazione.setFlagGiunta(variazioneImportoCapitolo.getFlagGiunta());
		
	}
	
	/**
	 * La variazione risulta essere variazione di bilancio se:
	 * <ul>
	 *     <li>all'interno della variazione ci sono capitoli sia di entrata che di spesa;</li>
	 *     <li>ci sono solo capitoli di spesa e questi capitoli hanno stessa missione, programma e titolo differenti</li>
	 *     <li>ci sono solo capitoli di entrata e questi capitoli hanno stesso titolo e tipologia</li>
	 * </ul>
	 *	Se la variazione &eacute; una variazione di bilancio, l'atto amministrativo variazione di bilancio prima della definizione deve essere presente e definitivo.
	 */
	@Override
	protected void checkNecessarioAttoAmministrativoVariazioneDiBilancio() {
		//SIAC-4737
		String methodName = "checkNecessarioAttoAmministrativoVariazioneDiBilancio";
		
		boolean isVariazioneDiBilancio = isCapitoliEntrataSpesaPresenti()
				|| isCapitoliWithProgrammaDifferenti()
				|| isCapitoliWithMissioneDifferenti()
				|| isCapitoliWithTitoloSpesaDifferenti()
				|| isCapitoliWithTipologieDifferenti()
				|| isCapitoliWithTitoloEntrataDifferenti();
		log.debug(methodName, " isVariazioneDiBilancio = " + isVariazioneDiBilancio);
		
		boolean attoAmmVariazioneDiBilancioPresente = attoAmministrativoVariazioneDiBilancioPresenteDefinitivo();
		boolean attoAmmVariazioneDiBilancioNecessario = !req.isAggiornamentoDaVariazioneDecentrata() && isVariazioneDiBilancio;
		
		res.setIsAttoAmministrativoVariazioneDiBilancioPresenteSeNecessario(Boolean.valueOf(!attoAmmVariazioneDiBilancioNecessario || attoAmmVariazioneDiBilancioPresente));
	}

	private boolean isCapitoliEntrataSpesaPresenti() {
		final String methodName = "isCapitoliEntrataSpesaPresenti";
		List<Capitolo<?, ?>> capitoliUscita = variazione.getCapitoli(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE,TipoCapitolo.CAPITOLO_USCITA_GESTIONE);
		List<Capitolo<?, ?>> capitoliEntrata = variazione.getCapitoli(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE,TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE);
		boolean capitoliPresenti = !capitoliUscita.isEmpty() && !capitoliEntrata.isEmpty();
		log.debug(methodName, "Capitoli uscita: " + capitoliUscita.size() + ", capitoli entrata: " + capitoliEntrata.size() + " => isCapitoliEntrataSpesaPresenti " + capitoliPresenti);
		return capitoliPresenti;
	}

	private boolean attoAmministrativoVariazioneDiBilancioPresenteDefinitivo() {
		AttoAmministrativo attoAmministrativoVariazioneBilancio = variazione.getAttoAmministrativoVariazioneBilancio();
		if(attoAmministrativoVariazioneBilancio == null || attoAmministrativoVariazioneBilancio.getUid() == 0){
			return false;
		}
		
		StatoOperativoAtti statoOperativoAtti = attoAmministrativoDad.findStatoOperativoAttoAmministrativo(attoAmministrativoVariazioneBilancio);
		return StatoOperativoAtti.DEFINITIVO.equals(statoOperativoAtti);
	}
	


	// CR-2304 e 3213
	protected boolean isQuadraturaCorrettaProvvedimenti() {
		//SIAC-4737
		return CostantiFin.AGG_VAR_FROM_CHIUDI_PROPOSTA.equals(req.getAggiornamentoVariazionieDecentrataFromAction())
				|| (
		Boolean.TRUE.equals(res.getIsProvvedimentoPresenteDefinitivo())
		//SIAC-4737 e SIAC-6884 e SIAC-7629
		&& ( req.isSaltaCheckNecessarioAttoAmministrativoVariazioneDiBilancio() 
				|| Boolean.TRUE.equals(res.getIsAttoAmministrativoVariazioneDiBilancioPresenteSeNecessario()))
		);
	}
	// CR-2304 e 3213
	protected boolean isQuadraturaCorrettaImporti() {
		return (req.isSaltaCheckStanziamentoCassa() || Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamentoCassa()))
						&& (req.isSaltaCheckStanziamento() || Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamento()));
	}

	protected void eseguiOperazioniPerFallimentoPassaggioDiStato() {
		res.setEsito(Esito.FALLIMENTO);
		if(!(req.isSaltaCheckStanziamentoCassa() || Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamentoCassa()) && Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamento()))){
			throw new BusinessException(ErroreBil.QUADRATURA_NON_CORRETTA.getErrore("Conclusione attività"));
		}
		if(!Boolean.TRUE.equals(res.getIsProvvedimentoPresenteDefinitivo())) {
			throw new BusinessException(ErroreBil.PROVVEDIMENTO_VARIAZIONE_NON_PRESENTE.getErrore("PEG"));
		}
		if(!Boolean.TRUE.equals(res.getIsAttoAmministrativoVariazioneDiBilancioPresenteSeNecessario())) {
			//questo prevede solo un warning gestitoi da front-end
			return;
		}
		throw new BusinessException(ErroreCore.TRANSAZIONE_DI_STATO_NON_POSSIBILE.getErrore());
		
	}

	// task-225
	protected void eseguiOperazioniPerFallimentoPassaggioDiStatoPerModificaContingente() {
		res.setEsito(Esito.FALLIMENTO);
		throw new BusinessException(ErroreBil.VARIAZIONE_MODIFICATA.getErrore());
	}
	//SIAC-6884
	protected void checkCodElenchi(boolean isRegionePiemonte){
		if(isRegionePiemonte){
			java.util.Vector<ClassificatoreGenerico> list = null;
			for(DettaglioVariazioneImportoCapitolo dettVarImp : variazione.getListaDettaglioVariazioneImporto()){
				try{
					if(dettVarImp.getCapitolo().getTipoCapitolo().equals(TipoCapitolo.CAPITOLO_USCITA_GESTIONE) ||
						dettVarImp.getCapitolo().getTipoCapitolo().equals(TipoCapitolo.CAPITOLO_USCITA_PREVISIONE )){
						if(list == null)
							list = new java.util.Vector<ClassificatoreGenerico>();
						capitoloDad.setEnte(variazione.getEnte());
						ClassificatoreGenerico ci = capitoloDad.ricercaClassificatoreGenerico(dettVarImp.getCapitolo().getUid(), TipologiaClassificatore.CLASSIFICATORE_8);
						list.add(ci != null ? ci : new ClassificatoreGenerico());
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(list != null && !list.isEmpty()){
				boolean sendError = false;
				String codice = ((ClassificatoreGenerico)list.elementAt(0)).getCodice();
				//tutti i capitoli devono avere lo stesso codice elenchi
				for(int j = 0; j < list.size(); j++){
					if((codice != null && !codice.equals(((ClassificatoreGenerico)list.elementAt(j)).getCodice())) || (codice == null && ((ClassificatoreGenerico)list.elementAt(j)).getCodice() != null)){
						sendError = true;
						break;
					}
				}
				if(sendError)
					res.setIsCodiciElenchiCongrui(false);
			}
		}else{
			log.debug("checkCodElenchi", "il controllo non è previsto per utenti non appartenenti a Regione Piemonte.");
		}
	}
	
	private void checkQuadratura() {
		boolean isQuadraturaCorrettaStanziamento = isQuadraturaCorrettaStanziamento();
		res.setIsQuadraturaCorrettaStanziamento(Boolean.valueOf(isQuadraturaCorrettaStanziamento));
		
		boolean isQuadraturaCorrettaStanziamentoCassa = isQuadraturaCorrettaStanziamentoCassa();
		res.setIsQuadraturaCorrettaStanziamentoCassa(Boolean.valueOf(isQuadraturaCorrettaStanziamentoCassa));


		 // SIAC-6884 - CONTROLLI BLOCCANTI SU CHIUDI PROPOSTA DA FARE SOLO SE INSERITA DA DECENTRATO
		// SIAC-7629- INSERITO CONTROLLO SULLA QUADRATURA VL VARIAZIONE DEC PER LEGGE: il controllo vale per tutte le variazioni decentrate, 
		// e viene trasformato in warning lato front-end per la variazioni di tipo VL
		if(!res.getIsQuadraturaCorretta() && !req.isSaltaCheckStanziamentoCassa() && StatoOperativoVariazioneBilancio.PRE_BOZZA.equals(variazione.getStatoOperativoVariazioneDiBilancio()) &&  CostantiFin.AGG_VAR_FROM_CHIUDI_PROPOSTA.equals(req.getAggiornamentoVariazionieDecentrataFromAction())) {
			throw new BusinessException(ErroreBil.QUADRATURA_NON_CORRETTA.getErrore("chiusura proposta"));
		}
	}
	
	private void checkProvvedimentoPresenteDefinitivo() {
		boolean isProvvedimentoPresenteDefinitivo = isProvvedimentoPresenteDefinitivo();
		res.setIsProvvedimentoPresenteDefinitivo(Boolean.valueOf(isProvvedimentoPresenteDefinitivo));
	}
	
	
	/**
	 * SIAC 6884
	 * Nel caso in cui si tratti di una variazione inserita da un operatore 
	 * abilitato alla azione OP-GESC001-insVarDecentrato  e figuri tra i capitoli inseriti un capitolo con flag budget,
	 * fondino viene eseguito un ulteriore controllo atto a verificare che il fondino venga utilizzato dalle direzioni unicamente a
	 * compensazione: |StanziamentoFondinoPostVariazione| ≤ |StanziamentoFondinoPreVariazione|
	 * L utilizzo del valore assoluto garantisce che se lo stanziamento del fondino e' negativo questo possa 
	 * essere unicamente aumentato mentre se lo stanziamento del fondino e' positivo questo possa essere unicamente diminuito.
	 */
	//SIAC-7792 si passa il DettaglioVariazioneImportoCapitolo
	@Override
	protected void checkVariazioniCapitoliFondino(DettaglioVariazioneImportoCapitolo dettVarImp){
		final String methodName="checkVariazioniCapitoliFondino";
		if(!CostantiFin.AGG_VAR_FROM_CHIUDI_PROPOSTA.equals(req.getAggiornamentoVariazionieDecentrataFromAction())) {
			log.debug(methodName, "la variazione non risulta esser stata inserita da decentrato. Esco.");
			return;
		}
		//SIAC-7792 si passa il DettaglioVariazioneImportoCapitolo
		String  checkVariazioniCapitoloFondinoFailedStr = controlloVariazioniCapitoloFondinoFailed(dettVarImp);
		if(checkVariazioniCapitoloFondinoFailedStr== null){
			res.setCheckVariazioniCapitoloFondinoFailed(Boolean.FALSE);
			return;
		}
		res.setCheckVariazioniCapitoloFondinoFailed(Boolean.TRUE);
		throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore(checkVariazioniCapitoloFondinoFailedStr));
	}
	
	
	/**
	 * SIAC 6884
	 * Nel caso in cui si tratti di una variazione inserita da un operatore 
	 * abilitato alla azione OP-GESC001-insVarDecentrato  e figuri tra i capitoli inseriti un capitolo con flag budget,
	 * fondino viene eseguito un ulteriore controllo atto a verificare che il fondino venga utilizzato dalle direzioni unicamente a
	 * compensazione: |StanziamentoFondinoPostVariazione| ≤ |StanziamentoFondinoPreVariazione|
	 * L utilizzo del valore assoluto garantisce che se lo stanziamento del fondino e' negativo questo possa 
	 * essere unicamente aumentato mentre se lo stanziamento del fondino e' positivo questo possa essere unicamente diminuito.
	 */
	//SIAC-7792 si passa il DettaglioVariazioneImportoCapitolo
	//SIAC-7972
	@Override
	protected void checkCapitoliFondinoInVariazione(List<Integer> uidsCapitoliFondino) {
		final String methodName ="checkCapitoliFondinoInVariazione";
		if(!CostantiFin.AGG_VAR_FROM_CHIUDI_PROPOSTA.equals(req.getAggiornamentoVariazionieDecentrataFromAction())) {
			log.debug(methodName, "la variazione non risulta esser stata inserita da decentrato. Esco.");
			return;
		}
		res.setCheckVariazioniCapitoloFondinoFailed(Boolean.FALSE);
		
		int annoBilancio = variazione.getBilancio().getAnno();
		
		for (DettaglioVariazioneImportoCapitolo dett : variazione.getListaDettaglioVariazioneImporto()) {
			if(!uidsCapitoliFondino.contains(dett.getCapitolo().getUid())) {
				continue;
			}
			//issue-13
			int annoStanziamento = annoBilancio;
			checkSingoloImportoFondino(annoStanziamento, dett.getStanziamento(), dett);
			
			annoStanziamento = annoBilancio +1;
			checkSingoloImportoFondino(annoStanziamento, dett.getStanziamento1(), dett);
			
			annoStanziamento = annoBilancio +2;
			checkSingoloImportoFondino(annoStanziamento, dett.getStanziamento2(), dett);
		}
	}
	
	
	private void checkSingoloImportoFondino(Integer annoStanziamento, BigDecimal importoInVariazione, DettaglioVariazioneImportoCapitolo dett) {
		BigDecimal importoCapitolo0 = importiCapitoloDad.caricaSingoloImportoCapitolo(dett.getCapitolo().getUid(),annoStanziamento,"STA");
		//issue-13
		//BigDecimal stanziamentoPostVariazione0 = importoCapitolo0.add(dett.getStanziamento());
		BigDecimal stanziamentoPostVariazione0 = importoCapitolo0.add(importoInVariazione);
		BigDecimal stanziamentoCapitoloIniziale0Abs = importoCapitolo0.abs();
		if(stanziamentoPostVariazione0.abs().compareTo(stanziamentoCapitoloIniziale0Abs) > 0){
			StringBuilder sb = new StringBuilder();
			int anno = annoStanziamento;
			sb.append("Capitolo Budget " + dett.getCapitolo().getDescBilancioAnnoNumeroArticolo() + " Anno " + anno + " importi stanziamenti non congruenti") ;
			throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore(sb.toString()));
		}
	}
	
	
	
	/**
	 * SIAC - 6884 checkVariazioniCapitoliFondino
	 */
	protected String controlloVariazioniCapitoloFondinoFailed(DettaglioVariazioneImportoCapitolo dettVarImp){
		final String methodName = "checkVariazioniCapitoliFondino";
		log.info(methodName, "Controllo variazioni capitoli fondino decentrato");
		try{
			//SIAC-7792 si passa il DettaglioVariazioneImportoCapitolo
			checkVariazioneCapFondinoDec(dettVarImp);
			return null;
		}catch(Exception e){
			return e.getMessage();
		}
	}
	
	
	
	//DOBBIAMO AVERE PER OGNI ANNO GLI IMPORTI SETTATI E QUELLI PRECEDENTE
	//SIAC-7792 REFACTORING 
	//si controlla un singolo dettaglio alla volta
	private void checkVariazioneCapFondinoDec(DettaglioVariazioneImportoCapitolo dettVarImp){
		//ANNO STANZIAMENTO 0
		int annoStanziamento0 = dettVarImp.getCapitolo().getBilancio().getAnno();
		if(dettVarImp.getCapitolo() == null) {
//			continue;
			return;
		}
		//CHECK SE FONDINO SIAC-7658: controllo
//		boolean isFondino = capitoloDad.isCapitoloFondino(dettVarImp.getCapitolo().getUid());
//		if(isFondino) {
		//PRENDIAMO LO STANZIAMENTO PER GLI ANNI DEL CAPITOLO
		BigDecimal stanziamentoCapitoloIniziale0 = null;
		BigDecimal stanziamentoCapitoloIniziale1 = null;
		BigDecimal stanziamentoCapitoloIniziale2 = null;
		if(dettVarImp.getCapitolo().getListaImportiCapitolo()!= null && !dettVarImp.getCapitolo().getListaImportiCapitolo().isEmpty()){
			for( int k=0; k<dettVarImp.getCapitolo().getListaImportiCapitolo().size();k++){
				ImportiCapitolo ic = (ImportiCapitolo) dettVarImp.getCapitolo().getListaImportiCapitolo().get(k);
				if(ic!= null){
					if(ic.getAnnoCompetenza() == annoStanziamento0){
						stanziamentoCapitoloIniziale0 = ic.getStanziamento();
					}else if(ic.getAnnoCompetenza() == annoStanziamento0+1){
						stanziamentoCapitoloIniziale1 = ic.getStanziamento();
					}else if(ic.getAnnoCompetenza() == annoStanziamento0+2){
						stanziamentoCapitoloIniziale2 = ic.getStanziamento();
					}
				}
			}
		}
		//CONFRONTO importo del capitolo con quello della variazione	
		//ANNO 0		
		if(stanziamentoCapitoloIniziale0!= null){
			BigDecimal stanziamentoPost0 = stanziamentoCapitoloIniziale0.add(dettVarImp.getStanziamento());
			BigDecimal stanziamentoPost0Abs = stanziamentoPost0.abs();
			BigDecimal stanziamentoCapitoloIniziale0Abs = stanziamentoCapitoloIniziale0.abs();
			int res = stanziamentoPost0Abs.compareTo(stanziamentoCapitoloIniziale0Abs);
			if(res==1){
				StringBuilder sb = new StringBuilder();
				int anno = annoStanziamento0;
				sb.append("Capitolo Budget " + dettVarImp.getCapitolo().getNumeroCapitolo() + " Anno " + anno + " importi stanziamenti non congruenti") ;
				throw new RuntimeException(sb.toString());
			}
		}
		//ANNO1
		if(stanziamentoCapitoloIniziale1!= null){
			BigDecimal stanziamentoPost1 = stanziamentoCapitoloIniziale1.add(dettVarImp.getStanziamento1());
			BigDecimal stanziamentoPost1Abs = stanziamentoPost1.abs();
			BigDecimal stanziamentoCapitoloIniziale1Abs = stanziamentoCapitoloIniziale1.abs();
			int res = stanziamentoPost1Abs.compareTo(stanziamentoCapitoloIniziale1Abs);
			if(res==1){
				StringBuilder sb = new StringBuilder();
				int anno = annoStanziamento0+1;
				sb.append("Capitolo Budget " + dettVarImp.getCapitolo().getNumeroCapitolo() + " Anno " + anno + " importi stanziamenti non congruenti") ;
				throw new RuntimeException(sb.toString());
			}
		}
		//ANNO2
		if(stanziamentoCapitoloIniziale2!= null){
			BigDecimal stanziamentoPost2 = stanziamentoCapitoloIniziale2.add(dettVarImp.getStanziamento2());
			BigDecimal stanziamentoPost2Abs = stanziamentoPost2.abs();
			BigDecimal stanziamentoCapitoloIniziale2Abs = stanziamentoCapitoloIniziale2.abs();
			int res = stanziamentoPost2Abs.compareTo(stanziamentoCapitoloIniziale2Abs);
			if(res==1){
				StringBuilder sb = new StringBuilder();
				int anno = annoStanziamento0+2;
				sb.append("Capitolo Budget " + dettVarImp.getCapitolo().getNumeroCapitolo() + " Anno " + anno + " importi stanziamenti non congruenti") ;
				throw new RuntimeException(sb.toString());
			}
		}
			
		
	}

	@Override
	protected boolean isAnnullamentoVariazione() {
		return Boolean.TRUE.equals(req.getAnnullaVariazione());
	}

}



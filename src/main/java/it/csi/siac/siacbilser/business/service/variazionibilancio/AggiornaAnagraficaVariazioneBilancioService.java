/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.variazionibilancio;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
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
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiesta;
import it.csi.siac.siaccorser.frontend.webservice.msg.ExecAzioneRichiestaResponse;
import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.TipoAzione;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.VariabileProcesso;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.Constanti;

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

		if(Boolean.TRUE.equals(req.getEvolviProcesso()) || Boolean.TRUE.equals(req.getAnnullaVariazione())){
			checkNotNull(req.getIdAttivita(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("id attivita"));
			checkNotNull(req.getAnnullaVariazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("annulla variazione"));
			checkNotNull(req.getInvioOrganoAmministrativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("invio organo amministrativo"));
			checkNotNull(req.getInvioOrganoLegislativo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("invio organo legislativo"));
		}
	}

	@Override
	protected void init() {
		variazioniDad.setEnte(ente);
		variazioniDad.setBilancio(variazione.getBilancio());
		variazioniDad.setLoginOperazione(loginOperazione);
		importiCapitoloDad.setEnte(ente);
		provvedimentoDad.setEnte(variazione.getEnte());
		provvedimentoDad.setLoginOperazione(loginOperazione);
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public AggiornaAnagraficaVariazioneBilancioResponse executeServiceTxRequiresNew(AggiornaAnagraficaVariazioneBilancio serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public AggiornaAnagraficaVariazioneBilancioResponse executeService(AggiornaAnagraficaVariazioneBilancio serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		// Annullamento della variazione: forzo i dati
		if(Boolean.TRUE.equals(req.getAnnullaVariazione())){
			variazione.setStatoOperativoVariazioneDiBilancio(StatoOperativoVariazioneDiBilancio.ANNULLATA);
			/*
			 * SIAC 6884
			 * SOLO SE NON E' Decentrata va settato l invio a BONITA 
			 */
			if(!req.isAggiornamentoDaVariazioneDecentrata()){
				req.setEvolviProcesso(Boolean.TRUE);
			}
			
		}
		//SIAC-7316: questo metodo (ed i successivi) NON POSSONO STARE NEL CHECK-SERVICE PARAM!!!!		
		popolaDatiDellaVariazione();
		//TODO: questi check sugli importi, sarebbero dovuti essere nel metodo checkImporti: in questo modo sarebbe dovuto essere in checkVariazioneImporti
		//XXX: DA SPOSTARE APPENA POSSIBILE
		checkVariazioniCapitoliFondino();
		
		popolaDatiDellaVariazione();
		//TODO: questi check sugli importi, sarebbero dovuti essere nel metodo checkImporti: in questo modo sarebbe dovuto essere in checkVariazioneImporti
		//XXX: DA SPOSTARE APPENA POSSIBILE
		checkVariazioniCapitoliFondino();
		
		checkImporti();
		
		// CR-2304
		checkQuadratura();
		
		//SIAC-6884
		checkCodElenchi(req.isRegionePiemonte());
		
		
		
		popolaDatiProvvedimento();

		checkProvvedimentoPresenteDefinitivo();
		// SIAC-4737
		checkNecessarioAttoAmministrativoVariazioneDiBilancio();

		// TODO: SIAC-6883 - valutare se spostare la chiamata a Bonita al termine, ed effettuare un aggiornamento puntuale dello stato
		//Evolve il processo solo se richiesto!
		//SIAC - 6884
		if(Boolean.TRUE.equals(req.getEvolviProcesso())){
			if(req.isAggiornamentoDaVariazioneDecentrata()){  
				if(variazione.getDataChiusuraProposta()== null){
					variazione.setDataChiusuraProposta(new Date());
					avviaProcessoVariazioneDiBilancio();
				}
				
			}else{
				ExecAzioneRichiestaResponse execAzioneRichiestaResponse = aggiornaProcessoVariazioneDiBilancio();
				impostaStatoOperativoVariazioneDiBilancio(execAzioneRichiestaResponse);
				checkProcessoEvolutoAlTaskSuccessivo(execAzioneRichiestaResponse);
			}
			
		}
		
		variazioniDad.aggiornaAnagraficaVariazioneImportoCapitolo(variazione);
		res.setVariazioneImportoCapitolo(variazione);
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
		return provvedimentoDad.findProvvedimentoByIdModelDetail(atto.getUid(), AttoAmministrativoModelDetail.TipoAtto, AttoAmministrativoModelDetail.StrutturaAmmContabile);
	}

	private void popolaDatiDellaVariazione() {
		VariazioneImportoCapitolo variazioneImportoCapitolo = variazioniDad.findVariazioneImportoCapitoloByUid(variazione.getUid());
		variazione.setListaDettaglioVariazioneImporto(variazioneImportoCapitolo.getListaDettaglioVariazioneImporto());
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
		
		res.setIsAttoAmministrativoVariazioneDiBilancioPresenteSeNecessario(Boolean.valueOf(!isVariazioneDiBilancio || attoAmmVariazioneDiBilancioPresente));
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
		
		StatoOperativoAtti statoOperativoAtti = provvedimentoDad.findStatoOperativoAttoAmministrativo(attoAmministrativoVariazioneBilancio);
		return StatoOperativoAtti.DEFINITIVO.equals(statoOperativoAtti);
	}
	

	/**
	 * Aggiorna processo variazione di bilancio.
	 *
	 * @return the exec azione richiesta response
	 */
	private ExecAzioneRichiestaResponse aggiornaProcessoVariazioneDiBilancio() {
		ExecAzioneRichiesta execAzioneRichiesta = new ExecAzioneRichiesta();
		execAzioneRichiesta.setRichiedente(req.getRichiedente());
		execAzioneRichiesta.setDataOra(new Date());

		AzioneRichiesta azioneRichiesta = new AzioneRichiesta();
		Azione azione = new Azione();
		azioneRichiesta.setAzione(azione);
		//ID DELL'attivita' ovvero id istanza. Ad esempio: "VariazioneDiBilancio--1.0--12--VariazioneDiBilancio_AggiornamentoVariazione--it1--mainActivityInstance--noLoop"
		azioneRichiesta.setIdAttivita(req.getIdAttivita());
		azione.setTipo(TipoAzione.ATTIVITA_PROCESSO);
		azione.setNomeProcesso("VariazioneDiBilancio");
		//VariazioneDiBilancio-AggiornamentoVariazione (per aggiornamento)	//VariazioneDiBilancio-DefinizioneDellaVariazione
		azione.setNomeTask("VariazioneDiBilancio-AggiornamentoVariazione");

		setVariabileProcesso(azioneRichiesta, "descrizione", variazione.getNumero() + " - " + variazione.getDescrizione() + " - "+ variazione.getTipoVariazione().getDescrizione() );
		setVariabileProcesso(azioneRichiesta, "descrizioneBreve", variazione.getNumero() + " - " + variazione.getDescrizione() + " - "+ variazione.getTipoVariazione().getDescrizione());
		//sac sta per StrutturaAmministravivaContabile
		setVariabileProcesso(azioneRichiesta, "siacSacProcesso", "strutture");
		//Boolean //invioOrganoAmministrativo
		setVariabileProcesso(azioneRichiesta, "invioGiunta", req.getInvioOrganoAmministrativo());
		//Boolean //invioOrganoLegislativo
		setVariabileProcesso(azioneRichiesta, "invioConsiglio", req.getInvioOrganoLegislativo());
		//Boolean
		setVariabileProcesso(azioneRichiesta, "annullaVariazione", req.getAnnullaVariazione());
		// CR-2304
		setVariabileProcesso(azioneRichiesta, "quadraturaVariazioneDiBilancio", (req.isSaltaCheckStanziamentoCassa() || Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamentoCassa()))
				&& Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamento())
				&& Boolean.TRUE.equals(res.getIsProvvedimentoPresenteDefinitivo())
				//SIAC-4737
				&& (req.isSaltaCheckNecessarioAttoAmministrativoVariazioneDiBilancio() || Boolean.TRUE.equals(res.getIsAttoAmministrativoVariazioneDiBilancioPresenteSeNecessario()))
				);

		setVariabileProcesso(azioneRichiesta, "statoVariazioneDiBilancio", StatoOperativoVariazioneDiBilancio.BOZZA.toString());

		execAzioneRichiesta.setAzioneRichiesta(azioneRichiesta);

		ExecAzioneRichiestaResponse execAzioneRichiestaResponse = coreService.execAzioneRichiesta(execAzioneRichiesta);
		log.logXmlTypeObject(execAzioneRichiestaResponse, "Risposta ottenuta dal servizio ExecAzioneRichiesta.");
		checkServiceResponseFallimento(execAzioneRichiestaResponse);

		// SIAC-6881: clono i dettagli in response
		res.setIdTask(execAzioneRichiestaResponse.getIdTask());
		res.setNomeTask(execAzioneRichiestaResponse.getNomeTask());
		res.setVariabiliDiProcesso(execAzioneRichiestaResponse.getVariabiliDiProcesso());

		return execAzioneRichiestaResponse;
	}

	/**
	 * Imposta lo stato operativo della variazione di Bilancio a partire dal valore della di processo <code>statoVariazioneDiBilancio</code>.
	 *
	 * @param execAzioneRichiestaResponse the exec azione richiesta response
	 */
	private void impostaStatoOperativoVariazioneDiBilancio(ExecAzioneRichiestaResponse execAzioneRichiestaResponse) {
		final String methodName = "impostaStatoOperativoVariazioneDiBilancio";

		//statoVariazioneDiBilancio
		VariabileProcesso variabileProcesso = execAzioneRichiestaResponse.findVariabileProcessoByName("statoVariazioneDiBilancio");
		String statoVariazioneDiBilancio = (String) variabileProcesso.getValore();
		
		StatoOperativoVariazioneDiBilancio statoOperativoVariazioneDiBilancio = StatoOperativoVariazioneDiBilancio.byVariableNameEventuallyNull(statoVariazioneDiBilancio);
		checkBusinessCondition(statoOperativoVariazioneDiBilancio != null, ErroreCore.ERRORE_DI_SISTEMA.getErrore("Variabile di processo statoVariazioneDiBilancio non valida: "+ToStringBuilder.reflectionToString(variabileProcesso)));
		log.info(methodName, "Nuovo stato della variazione di Bilancio: "+statoOperativoVariazioneDiBilancio);
		variazione.setStatoOperativoVariazioneDiBilancio(statoOperativoVariazioneDiBilancio);
	}

	/**
	 * Controlla se il processo si &eacute; passato al task successivo o se &eacute; tornato allo stesso stato di partenza.
	 * Se il task &eacute; uguale a quello di partenza viene inserito l'errore nella request.
	 *
	 * @param execAzioneRichiestaResponse response del servizio execAzioneRichiesta
	 */
	private void checkProcessoEvolutoAlTaskSuccessivo(ExecAzioneRichiestaResponse execAzioneRichiestaResponse) {
		final String methodName = "checkProcessoEvolutoAlTaskSuccessivo";
		String nomeTaskPrecedente = getNomeTaskFromAttivitaId();
		String nomeTaskSuccessivo = execAzioneRichiestaResponse.getNomeTask();

		log.info(methodName, "nomeTaskPrecedente: " + nomeTaskPrecedente + " nomeTaskSuccessivo: " + nomeTaskSuccessivo);
		if(nomeTaskPrecedente.equals(nomeTaskSuccessivo)){
			//Se il nome del task precedente e' uguale a quello successivo significa che il processo ha evoluto il suo stato ma e' tornato indietro allo stesso task
			res.setEsito(Esito.FALLIMENTO);
			if(!(req.isSaltaCheckStanziamentoCassa() || Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamentoCassa()) && Boolean.TRUE.equals(res.getIsQuadraturaCorrettaStanziamento()))){
				throw new BusinessException(ErroreBil.QUADRATURA_NON_CORRETTA.getErrore("Conclusione attività"));
			}
			if(!Boolean.TRUE.equals(res.getIsProvvedimentoPresenteDefinitivo())) {
				throw new BusinessException(ErroreBil.PROVVEDIMENTO_VARIAZIONE_NON_PRESENTE.getErrore("PEG"));
			}
		}
	}

	/**
	 * Ottiene VariazioneDiBilancio_AggiornamentoVariazione
	 * a partire da
	 * VariazioneDiBilancio--1.0--12--VariazioneDiBilancio_AggiornamentoVariazione--it1--mainActivityInstance--noLoop
	 *
	 * @return nome del task
	 */
	private String getNomeTaskFromAttivitaId() {
		try {
			return req.getIdAttivita().split("--")[3];
		} catch(RuntimeException re){
			String msg = "Formato idAttività non valido. Impossibile ricavare il nome del task.";
			log.error("getNomeTaskFromAttivitaId",msg + " idAttivita: "+ req.getIdAttivita(),re);
			throw new BusinessException(msg);
		}

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
		if(Constanti.AGG_VAR_FROM_CHIUDI_PROPOSTA.equals(req.getAggiornamentoVariazionieDecentrataFromAction()) && !res.getIsQuadraturaCorretta()) {
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
	private void checkVariazioniCapitoliFondino(){
		final String methodName="checkVariazioniCapitoliFondino";
		if(!Constanti.AGG_VAR_FROM_CHIUDI_PROPOSTA.equals(req.getAggiornamentoVariazionieDecentrataFromAction())) {
			log.debug(methodName, "la variazione non risulta esser stata inserita da decentrato. Esco.");
			return;
		}
		String  checkVariazioniCapitoloFondinoFailedStr = controlloVariazioniCapitoloFondinoFailed();
		if(checkVariazioniCapitoloFondinoFailedStr== null){
			res.setCheckVariazioniCapitoloFondinoFailed(Boolean.FALSE);
			return;
		}
		res.setCheckVariazioniCapitoloFondinoFailed(Boolean.TRUE);
		throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore(checkVariazioniCapitoloFondinoFailedStr));
	}
	
	
	
	/**SIAC 6884
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

}

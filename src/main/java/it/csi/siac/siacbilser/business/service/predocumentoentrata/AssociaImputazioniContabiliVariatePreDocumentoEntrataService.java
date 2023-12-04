/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.AttoAmministrativoModelDetail;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.integration.dad.AccertamentoBilDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabili;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabiliResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.AccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.SubAccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

/**
 * The Class AssociaImputazioniContabiliVariatePreDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssociaImputazioniContabiliVariatePreDocumentoEntrataService extends CheckedAccountBaseService<AssociaImputazioniContabiliVariatePreDocumentoEntrata, AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse> {

	@Autowired
	private AccertamentoBilDad accertamentoBilDad;
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad;
	@Autowired
	private AttoAmministrativoDad attoAmministrativoDad;
	@Autowired
	private SoggettoDad soggettoDad;
	
	@Autowired
	private ServiceExecutor serviceExecutor;
	
	private CapitoloEntrataGestione capitoloEntrataGestione;
	private Accertamento accertamento;
	private SubAccertamento subAccertamento;
	private Soggetto soggetto;
	private AttoAmministrativo attoAmministrativo;
	private Bilancio bilancio;
	//SIAC-7423
	private ProvvisorioDiCassa provvisorioCassa;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		capitoloEntrataGestione = req.getCapitoloEntrataGestione();
		accertamento = req.getAccertamento();
		subAccertamento = req.getSubAccertamento();
		soggetto = req.getSoggetto();
		attoAmministrativo = req.getAttoAmministrativo();
		bilancio = req.getBilancio();
		//SIAC-7423
		provvisorioCassa = req.getProvvisorioCassa();

		checkEntita(bilancio, "bilancio", false);
		
		checkCondition(entitaConUid(capitoloEntrataGestione)
				|| entitaConUid(accertamento)
				|| entitaConUid(subAccertamento)
				|| entitaConUid(soggetto)
				|| entitaConUid(attoAmministrativo)
				|| entitaConUid(provvisorioCassa),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("imputazione contabile"), false);
		
		checkNotNull(req.getPreDocumentiEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumenti entrata"));

		if(req.getRicercaSinteticaPreDocumentoEntrata() == null){
			checkCondition(!req.getPreDocumentiEntrata().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista predocumenti entrata"), false);
			for(PreDocumentoEntrata preDoc : req.getPreDocumentiEntrata()){
				checkEntita(preDoc, "predocumento", true);
			}
		} else {
			checkNotNull(req.getRicercaSinteticaPreDocumentoEntrata().getPreDocumentoEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento ricerca sintetica"));
			checkEntita(req.getRicercaSinteticaPreDocumentoEntrata().getPreDocumentoEntrata().getEnte(), "ente predocumento ricerca sintetica", false);
		}
	}

	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse executeService(AssociaImputazioniContabiliVariatePreDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public AssociaImputazioniContabiliVariatePreDocumentoEntrataResponse executeServiceTxRequiresNew(AssociaImputazioniContabiliVariatePreDocumentoEntrata serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	protected void execute() {
		final String methodName = "execute";
		
		// Check operativi
		checkSoggetto();
		checkCapitolo();
		checkProvvedimento();
		caricaAccertamento();
		caricaSubAccertamento();
		
		checkCongruenzaSoggettoIncasso();
		checkAccertamento();
		checkSubAccertamento();
		
		List<PreDocumentoEntrata> preDocumentiEntrata = getPredocumentiEntrata();

		for (PreDocumentoEntrata preDocInList : preDocumentiEntrata) {
			PreDocumentoEntrata preDoc = getDettaglioPreDocumentoEntrata(preDocInList);
			if(preDoc == null) {
				res.getPreDocumentiEntrataSaltati().add(preDocInList);
				log.debug(methodName, "Saltato predocumento [" + preDocInList.getUid() + "]: non presente su base dati.");
				res.addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il predocumento [" + preDocInList.getUid() + "] non e' presente su base dati."));
				continue;
			}

			if(!isAggiornabile(preDoc)) {
				res.getPreDocumentiEntrataSaltati().add(preDoc);
				log.debug(methodName, "Saltato predocumento " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione() + ": non aggiornabile.");
				res.addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il predocumento " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione() + " non e' aggiornabile."));
				continue;
			}

			associaImputazioniContabili(preDoc);

			try{
				aggiornaPreDocumentoDiEntrata(preDoc);
				log.debug(methodName, "Aggiornato predocumento " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione());
				res.addMessaggio("AGGIORNATO", "Aggiornato predocumento " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione());
			} catch(ExecuteExternalServiceException eese){
				String errori = getTestoErrori(eese);
				log.debug(methodName, "Saltato predocumento " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione() + ": impossibile aggiornare il predocumento:  " + errori);
				res.addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il predocumento " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione() + " non e' aggiornabile: " + errori));
			}
		}
	}

	/**
	 * Ottiene una stringa di errori a partire dall'eccezione del servizio
	 * @param eese l'eccezione da cui ottenere gli errori
	 * @return gli errori
	 */
	private String getTestoErrori(ExecuteExternalServiceException eese) {
		StringBuilder sb = new StringBuilder();
		for(Errore errore: eese.getErrori()){
			sb.append(errore.getTesto()).append(" ");
		}
		return sb.toString();
	}

	/**
	 * Gets the predocumenti entrata.
	 *
	 * @return the predocumenti entrata
	 */
	private List<PreDocumentoEntrata> getPredocumentiEntrata() {
		if(req.getRicercaSinteticaPreDocumentoEntrata() != null){
			return ricercaSinteticaPreDocumentoEntrata();
		}

		return req.getPreDocumentiEntrata();
	}

	/**
	 * Ricerca sintetica pre documento entrata.
	 *
	 * @return the list
	 */
	private List<PreDocumentoEntrata> ricercaSinteticaPreDocumentoEntrata() {
		RicercaSinteticaPreDocumentoEntrata reqSintetica = req.getRicercaSinteticaPreDocumentoEntrata();
		List<PreDocumentoEntrata> result = new ArrayList<PreDocumentoEntrata>();
		ListaPaginata<PreDocumentoEntrata> listaPaginata = null;
		
		int numeroPagina = 0;
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(100);
		
		do {
			parametriPaginazione.setNumeroPagina(numeroPagina);
			listaPaginata = preDocumentoEntrataDad.ricercaSinteticaPreDocumentoEntrataModelDetail(reqSintetica.getPreDocumentoEntrata(),
					reqSintetica.getTipoCausale(), reqSintetica.getDataCompetenzaDa(), reqSintetica.getDataCompetenzaA(), reqSintetica.getDataTrasmissioneDa(), reqSintetica.getDataTrasmissioneA(),
					reqSintetica.getCausaleEntrataMancante(), reqSintetica.getSoggettoMancante(), reqSintetica.getProvvedimentoMancante(), reqSintetica.getContoCorrenteMancante(),
					reqSintetica.getNonAnnullati(), reqSintetica.getOrdinativoIncasso(), reqSintetica.getOrdinamentoPreDocumentoEntrata(), parametriPaginazione);
			result.addAll(listaPaginata);
		} while(++numeroPagina < listaPaginata.getTotalePagine());

		return result;
	}

	/**
	 * Ribalta le informazioni della causale sul preDocumento.
	 *
	 * @param preDoc the pre doc
	 */
	private void associaImputazioniContabili(PreDocumentoEntrata preDoc) {
		if(entitaConUid(capitoloEntrataGestione)) {
			preDoc.setCapitoloEntrataGestione(capitoloEntrataGestione);
		}
		if(entitaConUid(accertamento) || entitaConUid(subAccertamento)) {
			preDoc.setAccertamento(accertamento);
			preDoc.setSubAccertamento(subAccertamento);
		}
		if(entitaConUid(soggetto)) {
			preDoc.setSoggetto(soggetto);
		}
		if(entitaConUid(attoAmministrativo)) {
			preDoc.setAttoAmministrativo(attoAmministrativo);
		}
		//SIAC-7423
		if(entitaConUid(provvisorioCassa)) {
			preDoc.setProvvisorioDiCassa(provvisorioCassa);
		}
	}

	/**
	 * Aggiorna pre documento di entrata.
	 *
	 * @param preDoc the pre doc
	 */
	private AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabiliResponse aggiornaPreDocumentoDiEntrata(PreDocumentoEntrata preDoc) {

		AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabili reqAPDDEPVIC = new AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabili();
		reqAPDDEPVIC.setRichiedente(req.getRichiedente());
		reqAPDDEPVIC.setBilancio(req.getBilancio());
		reqAPDDEPVIC.setPreDocumentoEntrata(preDoc);
		reqAPDDEPVIC.setGestisciModificaImportoAccertamento(req.isGestisciModificaImportoAccertamento());
		
		return serviceExecutor.executeServiceSuccessTxRequiresNew(AggiornaPreDocumentoDiEntrataPerVariazioneImputazioniContabiliService.class, reqAPDDEPVIC);
	}

	/**
	 * <ol>
	 *     <li>deve essere aggiornabile, quindi non deve essere in uno dei seguenti stati operativi: ANNULLATO,DEFINITO</li>
	 *     <li>non deve essere gi&agrave; in stato operativo COMPLETO</li>
	 * </ol>
	 *
	 * Se non conforme esce con errore:
	 * <pre>COR_ERR_0028 - Operazione incompatibile con stato dell'entit&agrave;, keyPredisposizione, statoOp.</pre>
	 *
	 * @param preDoc the pre doc
	 * @return true, if is aggiornabile
	 */
	private boolean isAggiornabile(PreDocumentoEntrata preDoc) {
		boolean hasSubAccertamento = preDoc.getAccertamento() != null && preDoc.getAccertamento().getUid() != 0;
		boolean hasAccertamento= preDoc.getAccertamento() != null && preDoc.getAccertamento().getUid() != 0;
		boolean isAzioneConsentita = isAzioneConsentita(AzioneConsentitaEnum.PREDOCUMENTO_ENTRATA_COMPLETA_SENZA_ACCERTAMENTO.getNomeAzione());
		boolean hasSoggetto = preDoc.getSoggetto() != null && preDoc.getSoggetto().getUid() != 0;
		return StatoOperativoPreDocumento.INCOMPLETO.equals(preDoc.getStatoOperativoPreDocumento()) || (isAzioneConsentita && hasSoggetto) || (!isAzioneConsentita && (hasSubAccertamento || hasAccertamento) && hasSoggetto);
	}


	/**
	 * Carica il dettaglio del predocumento.
	 *
	 * @param preDoc the pre doc
	 * @return the dettaglio pre documento entrata
	 */
	protected PreDocumentoEntrata getDettaglioPreDocumentoEntrata(PreDocumentoEntrata preDoc) {
		return preDocumentoEntrataDad.findPreDocumentoById(preDoc.getUid());
	}
	
	// Check di coerenza dei dati: fatti una volta sola all'inizio
	
	/**
	 * Controllo di coerenza del soggetto.
	 * <br/>
	 * Il soggetto deve essere VALIDO e non BLOCCATO.
	 */
	private void checkSoggetto() {
		// Il soggetto e' facoltativo
		if(!entitaConUid(soggetto)){
			soggetto = null;
			return;
		}
		StatoOperativoAnagrafica statoOperativoAnagrafica = soggettoDad.findStatoOperativoAnagraficaSoggetto(soggetto);
		checkBusinessCondition(StatoOperativoAnagrafica.VALIDO.equals(statoOperativoAnagrafica), it.csi.siac.siacfinser.model.errore.ErroreFin.SOGGETTO_NON_VALIDO.getErrore());
		// Non dovrebbe mai verificarsi
		checkBusinessCondition(!StatoOperativoAnagrafica.BLOCCATO.equals(statoOperativoAnagrafica), ErroreFin.SOGGETTO_BLOCCATO.getErrore());
	}
	
	/**
	 * Check capitolo.
	 */
	private void checkCapitolo() {
		// Il capitolo e' facoltativo
		if(!entitaConUid(capitoloEntrataGestione)){
			capitoloEntrataGestione = null;
			return;
		}
		
		CapitoloEntrataGestione ceg = capitoloEntrataGestioneDad.ricercaDettaglioModulareCapitoloEntrataGestione(capitoloEntrataGestione, CapitoloEntrataGestioneModelDetail.Stato);
		checkBusinessCondition(ceg != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("capitolo di entrata gestione", "uid " + capitoloEntrataGestione.getUid()));
		// Il controllo di disponibilita' viene demandato sotto
		checkBusinessCondition(StatoOperativoElementoDiBilancio.VALIDO.equals(ceg.getStatoOperativoElementoDiBilancio()),
				ErroreFin.CAPITOLO_NON_VALIDO_PER_OPERAZIONE.getErrore(ceg.getStatoOperativoElementoDiBilancio(), "di entrata", "aggiornamento", "predisposizione di incasso"));
		capitoloEntrataGestione = ceg;
	}
	
	/**
	 * Check provvedimento.
	 */
	private void checkProvvedimento(){
		// Il provvedimento non e' obbligatorio
		if(!entitaConUid(attoAmministrativo)){
			attoAmministrativo = null;
			return;
		}
		AttoAmministrativo aa = attoAmministrativoDad.findProvvedimentoByIdAndModelDetail(attoAmministrativo.getUid(), AttoAmministrativoModelDetail.StatoOperativo);
		
		checkBusinessCondition(aa != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("provvedimetno", "uid " + attoAmministrativo.getUid()));
		checkBusinessCondition(!StatoOperativoAtti.ANNULLATO.equals(aa.getStatoOperativoAtti()), ErroreAtt.PROVVEDIMENTO_ANNULLATO.getErrore());
		checkBusinessCondition(StatoOperativoAtti.DEFINITIVO.equals(aa.getStatoOperativoAtti()),
				it.csi.siac.siacfinser.model.errore.ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("aggiornamento predisposizione di entrata", "definitivo"));
		attoAmministrativo = aa;
	}
	
	/**
	 * Caricamento accertamento.
	 */
	private void caricaAccertamento(){
		if(!entitaConUid(accertamento)){
			accertamento = null;
			subAccertamento = null;
			return;
		}
		//Chiave capitolo richiesta dall'inserimento della modifica, PDC richiesto dall'inserimento dell'eventuale registrazione
		Accertamento a = accertamentoBilDad.findAccertamentoByUid(accertamento.getUid(),
				AccertamentoModelDetail.Stato, AccertamentoModelDetail.AttoAmministrativo, AccertamentoModelDetail.CapitoloMinimal, AccertamentoModelDetail.PianoDeiConti);
		checkBusinessCondition(a != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("accertamento", "uid " + accertamento.getUid()));
		
		accertamento = a;
	}
	
	private void caricaSubAccertamento() {
		if(!entitaConUid(subAccertamento)) {
			subAccertamento = null;
			return;
		}
		
		SubAccertamento sa = accertamentoBilDad.findSubAccertamentoByUid(subAccertamento.getUid(),
				SubAccertamentoModelDetail.Stato, SubAccertamentoModelDetail.AttoAmministrativo, SubAccertamentoModelDetail.CapitoloMinimal, SubAccertamentoModelDetail.PianoDeiConti);
		checkBusinessCondition(sa != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("subaccertamento", "uid " + subAccertamento.getUid()));
		subAccertamento = sa;
	}
	
	/**
	 * Check congruenza soggetto incasso.
	 */
	private void checkCongruenzaSoggettoIncasso() {
		// il controllo viene effettuato solo se sono presenti sia soggetto sia accertamento
		if (soggetto == null || accertamento == null){
			return;
		}
		Soggetto soggettoAccertamento = entitaConUid(subAccertamento)
				? soggettoDad.findSoggettoByIdSubMovimentoGestione(subAccertamento.getUid())
				: soggettoDad.findSoggettoByIdMovimentoGestione(accertamento.getUid());
		
		checkBusinessCondition(soggettoAccertamento == null || soggettoAccertamento.getUid() == soggetto.getUid(),
				ErroreFin.SOGGETTO_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto incasso","accertamento"));
		if(soggettoAccertamento == null) {
			checkValiditaClassiSoggetto();
		}
	}
	
	/**
	 * Controllo di coerenza delle classi soggetto
	 */
	private void checkValiditaClassiSoggetto() {
		final String methodName = "checkValiditaClassiSoggetto";
		log.debug(methodName, "Controllo coerenza classi soggetto");
		ClasseSoggetto classeSoggettoMovimentoGestione = entitaConUid(subAccertamento)
				? soggettoDad.findClasseSoggettoByMovgestTs(subAccertamento.getUid())
				: soggettoDad.findClasseSoggettoByMovgest(accertamento.getUid());
		
		if(classeSoggettoMovimentoGestione == null) {
			log.debug(methodName, "Il movimento di gestione non e' associato ad una classe");
			// Va tutto bene?
			return;
		}
		
		List<ClasseSoggetto> classiSoggetto = soggettoDad.findClasseSoggetto(soggetto.getUid());
		if(classiSoggetto == null) {
			classiSoggetto = new ArrayList<ClasseSoggetto>();
		}
		boolean presenteClasse = false;
		for(Iterator<ClasseSoggetto> it = classiSoggetto.iterator(); it.hasNext() && !presenteClasse;) {
			ClasseSoggetto cs = it.next();
			if(cs != null && cs.getUid() == classeSoggettoMovimentoGestione.getUid()) {
				log.debug(methodName, "Classe soggetto " + cs.getUid() + " coerente tra soggetto e movimento di gestione");
				presenteClasse = true;
			}
		}
		if(!presenteClasse) {
			log.warn(methodName, "Il soggetto non appartiene alla classificazione. Non e' un errore ma deve essere segnalato");
//			throw new BusinessException(ErroreFin.SOGGETTO_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto incasso",
//				"accertamento: il soggetto di incasso deve appartenere alla classificazione"), Esito.FALLIMENTO);
		}
	}
	
	/**
	 * Controllo di coerenza dell'accertamento
	 */
	protected void checkAccertamento() {
		if(!entitaConUid(accertamento) || entitaConUid(subAccertamento)){
			return;
		}
		// Ho l'accertamento e NON il sub
		
		// Controllo validita' accertamento
		checkBusinessCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equalsIgnoreCase(accertamento.getStatoOperativoMovimentoGestioneEntrata()),
				ErroreFin.MOVIMENTO_GESTIONE_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE.getErrore("L'accertamento", "definitivo", "Non puo' essere imputato ad una predisposizione di incasso"));
		
		// Controllo congruenza tra anno accertamento e anno bilancio
		checkBusinessCondition(accertamento.getAnnoMovimento() <= bilancio.getAnno(),
				ErroreFin.MOVIMENTO_GESTIONE_PLURIENNALE_NON_AMMESSO_PER_OPERAZIONE.getErrore("accertamento", "aggiornamento predisposizione di incasso"));
	}
	
	/**
	 * Controllo di coerenza del subaccertamento
	 */
	protected void checkSubAccertamento() {
		if(!entitaConUid(accertamento) || !entitaConUid(subAccertamento)){
			return;
		}
		

		// Controllo congruenza tra anno subimpegno e anno bilancio
		checkBusinessCondition(subAccertamento.getAnnoMovimento() <= bilancio.getAnno(),
				ErroreFin.MOVIMENTO_GESTIONE_PLURIENNALE_NON_AMMESSO_PER_OPERAZIONE.getErrore("subaccertamento", "aggiornamento predisposizione di incasso"));
		
		//controllo congruenza con capitolo
		checkBusinessCondition(!entitaConUid(capitoloEntrataGestione)
				|| !entitaConUid(subAccertamento.getCapitoloEntrataGestione())
				|| capitoloEntrataGestione.getUid() == subAccertamento.getCapitoloEntrataGestione().getUid(),
			ErroreFin.ACCERTAMENTO_NON_PERTINENTE_AL_CAPITOLO_UPB.getErrore());
		
		//controllo validita' subaccertamento
		checkBusinessCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equalsIgnoreCase(subAccertamento.getStatoOperativoMovimentoGestioneEntrata()),
				ErroreFin.MOVIMENTO_GESTIONE_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE.getErrore("Il subaccertamento", "definitivo", "Non puo' essere imputato ad una predisposizione di incasso"));
		
	}
}

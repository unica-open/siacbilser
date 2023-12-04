/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

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
import it.csi.siac.siacbilser.business.service.predocumentoentrata.AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabiliService;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.AttoAmministrativoDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabili;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabiliResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.CapitoloUscitaGestioneModelDetail;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.SubImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * The Class AssociaImputazioniContabiliVariatePreDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AssociaImputazioniContabiliVariatePreDocumentoSpesaService extends CheckedAccountBaseService<AssociaImputazioniContabiliVariatePreDocumentoSpesa, AssociaImputazioniContabiliVariatePreDocumentoSpesaResponse> {

	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	@Autowired
	private PreDocumentoSpesaDad preDocumentoSpesaDad;
	@Autowired
	private AttoAmministrativoDad attoAmministrativoDad;
	@Autowired
	private SoggettoDad soggettoDad;

	@Autowired
	private ServiceExecutor serviceExecutor;
	
	private CapitoloUscitaGestione capitoloUscitaGestione;
	private Impegno impegno;
	private SubImpegno subImpegno;
	private Soggetto soggetto;
	private SedeSecondariaSoggetto sedeSecondariaSoggetto;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	private AttoAmministrativo attoAmministrativo;
	private Bilancio bilancio;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		capitoloUscitaGestione = req.getCapitoloUscitaGestione();
		impegno = req.getImpegno();
		subImpegno = req.getSubImpegno();
		soggetto = req.getSoggetto();
		sedeSecondariaSoggetto = req.getSedeSecondariaSoggetto();
		modalitaPagamentoSoggetto = req.getModalitaPagamentoSoggetto();
		attoAmministrativo = req.getAttoAmministrativo();
		bilancio = req.getBilancio();

		checkEntita(bilancio, "bilancio", false);
		
		checkCondition(entitaConUid(capitoloUscitaGestione)
				|| entitaConUid(impegno)
				|| entitaConUid(subImpegno)
				|| entitaConUid(soggetto)
				|| entitaConUid(attoAmministrativo),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("imputazione contabile"), false);
		
		checkNotNull(req.getPreDocumentiSpesa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumenti spesa"));

		if(req.getRicercaSinteticaPreDocumentoSpesa() == null){
			checkCondition(!req.getPreDocumentiSpesa().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista predocumenti spesa"), false);
			for(PreDocumentoSpesa preDoc : req.getPreDocumentiSpesa()){
				checkEntita(preDoc, "predocumento", true);
			}
		} else {
			checkNotNull(req.getRicercaSinteticaPreDocumentoSpesa().getPreDocumentoSpesa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento ricerca sintetica"));
			checkEntita(req.getRicercaSinteticaPreDocumentoSpesa().getPreDocumentoSpesa().getEnte(), "ente predocumento ricerca sintetica", false);
		}
	}

	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public AssociaImputazioniContabiliVariatePreDocumentoSpesaResponse executeService(AssociaImputazioniContabiliVariatePreDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public AssociaImputazioniContabiliVariatePreDocumentoSpesaResponse executeServiceTxRequiresNew(AssociaImputazioniContabiliVariatePreDocumentoSpesa serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		
		// Check operativi
		checkSoggetto();
		checkCapitolo();
		checkProvvedimento();
		caricaImpegno();
		caricaSubImpegno();
		
		checkCongruenzaSoggettoPagamento();
		checkImpegno();
		checkSubImpegno();
		
		// TODO: altro?
		
		List<PreDocumentoSpesa> preDocumentiSpesa = getPredocumentiSpesa();

		for (PreDocumentoSpesa preDocInList : preDocumentiSpesa) {
			PreDocumentoSpesa preDoc = getDettaglioPreDocumentoSpesa(preDocInList);
			
			if(preDoc == null) {
				res.getPreDocumentiSpesaSaltati().add(preDocInList);
				log.debug(methodName, "Saltato predocumento [" + preDocInList.getUid() + "]: non presente su base dati.");
				res.addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il predocumento [" + preDocInList.getUid() + "] non e' presente su base dati."));
				continue;
			}

			if(!isAggiornabile(preDoc)) {
				res.getPreDocumentiSpesaSaltati().add(preDoc);
				log.debug(methodName, "Saltato predocumento " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione() + ": non aggiornabile.");
				res.addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("il predocumento " + preDoc.getNumero() + " [" + preDoc.getUid() + "] - " + preDoc.getDescrizione() + " non e' aggiornabile."));
				continue;
			}

			associaImputazioniContabili(preDoc);

			try{
				aggiornaPreDocumentoDiSpesa(preDoc);
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
	 * Gets the predocumenti spesa.
	 *
	 * @return the predocumenti spesa
	 */
	private List<PreDocumentoSpesa> getPredocumentiSpesa() {
		if(req.getRicercaSinteticaPreDocumentoSpesa() != null){
			return ricercaSinteticaPreDocumentoSpesa();
		}

		return req.getPreDocumentiSpesa();
	}

	/**
	 * Ricerca sintetica pre documento spesa.
	 *
	 * @return the list
	 */
	private List<PreDocumentoSpesa> ricercaSinteticaPreDocumentoSpesa() {
		RicercaSinteticaPreDocumentoSpesa reqSintetica = req.getRicercaSinteticaPreDocumentoSpesa();
		List<PreDocumentoSpesa> result = new ArrayList<PreDocumentoSpesa>();
		ListaPaginata<PreDocumentoSpesa> listaPaginata = null;
		
		int numeroPagina = 0;
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		parametriPaginazione.setElementiPerPagina(100);
		
		do {
			parametriPaginazione.setNumeroPagina(numeroPagina);
			listaPaginata = preDocumentoSpesaDad.ricercaSinteticaPreDocumentoModelDetail(reqSintetica.getPreDocumentoSpesa(),
					reqSintetica.getTipoCausale(), reqSintetica.getDataCompetenzaDa(), reqSintetica.getDataCompetenzaA(), reqSintetica.getCausaleSpesaMancante(),
					reqSintetica.getContoTesoreriaMancante(), reqSintetica.getSoggettoMancante(), reqSintetica.getProvvedimentoMancante(), reqSintetica.getNonAnnullati(), 
					reqSintetica.getOrdinativoPagamento(), parametriPaginazione);
			result.addAll(listaPaginata);
		} while(++numeroPagina < listaPaginata.getTotalePagine());

		return result;
	}

	/**
	 * Ribalta le informazioni della causale sul preDocumento.
	 *
	 * @param preDoc the pre doc
	 */
	private void associaImputazioniContabili(PreDocumentoSpesa preDoc) {
		if(entitaConUid(req.getCapitoloUscitaGestione())) {
			preDoc.setCapitoloUscitaGestione(capitoloUscitaGestione);
		}
		if(entitaConUid(req.getImpegno())){
			preDoc.setImpegno(impegno);
		} 
		if(entitaConUid(req.getSubImpegno())) {
			preDoc.setSubImpegno(subImpegno);
		}
		if(entitaConUid(req.getSoggetto())) {
			preDoc.setSoggetto(soggetto);
		}
		if(entitaConUid(req.getAttoAmministrativo())){
			preDoc.setAttoAmministrativo(attoAmministrativo);
		}
		if(entitaConUid(req.getSedeSecondariaSoggetto())) {
			preDoc.setSedeSecondariaSoggetto(sedeSecondariaSoggetto);
		}
		if(entitaConUid(req.getModalitaPagamentoSoggetto())) {
			preDoc.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto);
		}
	}
	
	/**
	 * Aggiorna pre documento di spesa.
	 *
	 * @param preDoc the pre doc
	 */
	private AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabiliResponse aggiornaPreDocumentoDiSpesa(PreDocumentoSpesa preDoc) {

		AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabili reqAPDDE = new AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabili();
		reqAPDDE.setRichiedente(req.getRichiedente());
		reqAPDDE.setBilancio(req.getBilancio());
		reqAPDDE.setPreDocumentoSpesa(preDoc);
		
		return serviceExecutor.executeServiceSuccessTxRequiresNew(AggiornaPreDocumentoDiSpesaPerVariazioneImputazioniContabiliService.class, reqAPDDE);
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
	private boolean isAggiornabile(PreDocumentoSpesa preDoc) {
		return StatoOperativoPreDocumento.INCOMPLETO.equals(preDoc.getStatoOperativoPreDocumento());
	}


	/**
	 * Carica il dettaglio del predocumento.
	 *
	 * @param preDoc the pre doc
	 * @return the dettaglio pre documento spesa
	 */
	protected PreDocumentoSpesa getDettaglioPreDocumentoSpesa(PreDocumentoSpesa preDoc) {
		return preDocumentoSpesaDad.findPreDocumentoById(preDoc.getUid());
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
		if(!entitaConUid(capitoloUscitaGestione)){
			capitoloUscitaGestione = null;
			return;
		}
		
		CapitoloUscitaGestione cug = capitoloUscitaGestioneDad.ricercaDettaglioModulareCapitoloUscitaGestione(capitoloUscitaGestione, CapitoloUscitaGestioneModelDetail.Stato);
		checkBusinessCondition(cug != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("capitolo di uscita gestione", "uid " + capitoloUscitaGestione.getUid()));
		// Il controllo di disponibilita' viene demandato sotto
		checkBusinessCondition(StatoOperativoElementoDiBilancio.VALIDO.equals(cug.getStatoOperativoElementoDiBilancio()),
				ErroreFin.CAPITOLO_NON_VALIDO_PER_OPERAZIONE.getErrore(cug.getStatoOperativoElementoDiBilancio(), "di spesa", "aggiornamento", "predisposizione di pagamento"));
		capitoloUscitaGestione = cug;
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
				it.csi.siac.siacfinser.model.errore.ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("aggiornamento predisposizione di pagamento", "definitivo"));
		attoAmministrativo = aa;
	}
	
	/**
	 * Caricamento impegno.
	 */
	private void caricaImpegno(){
		if(!entitaConUid(impegno)){
			impegno = null;
			subImpegno = null;
			return;
		}
		// PDC richiesto dall'inserimento dell'eventuale registrazione
		Impegno i = impegnoBilDad.findImpegnoByUid(impegno.getUid(),
				ImpegnoModelDetail.Stato, ImpegnoModelDetail.AttoAmministrativo, ImpegnoModelDetail.CapitoloMinimal, ImpegnoModelDetail.PianoDeiConti);
		checkBusinessCondition(i != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("impegno", "uid " + impegno.getUid()));
		
		impegno = i;
	}
	
	private void caricaSubImpegno() {
		if(!entitaConUid(subImpegno)) {
			subImpegno= null;
			return;
		}
		
		SubImpegno si = impegnoBilDad.findSubImpegnoByUid(subImpegno.getUid(),
				SubImpegnoModelDetail.Stato, SubImpegnoModelDetail.AttoAmministrativo, SubImpegnoModelDetail.CapitoloMinimal, SubImpegnoModelDetail.PianoDeiConti);
		checkBusinessCondition(si != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("subimpegno", "uid " + subImpegno.getUid()));
		subImpegno = si;
	}
	
	/**
	 * Check congruenza soggetto pagamento.
	 */
	private void checkCongruenzaSoggettoPagamento() {
		// il controllo viene effettuato solo se sono presenti sia soggetto sia accertamento
		if (soggetto == null || impegno == null){
			return;
		}
		Soggetto soggettoImpegno = entitaConUid(subImpegno)
				? soggettoDad.findSoggettoByIdSubMovimentoGestione(subImpegno.getUid())
				: soggettoDad.findSoggettoByIdMovimentoGestione(impegno.getUid());
		
		checkBusinessCondition(soggettoImpegno == null || soggettoImpegno.getUid() == soggetto.getUid(),
				ErroreFin.SOGGETTO_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto pagamento", "impegno"));
		if(soggettoImpegno == null) {
			checkValiditaClassiSoggetto();
		}
	}
	
	/**
	 * Controllo di coerenza delle classi soggetto
	 */
	private void checkValiditaClassiSoggetto() {
		final String methodName = "checkValiditaClassiSoggetto";
		log.debug(methodName, "Controllo coerenza classi soggetto");
		ClasseSoggetto classeSoggettoMovimentoGestione = entitaConUid(subImpegno)
				? soggettoDad.findClasseSoggettoByMovgestTs(subImpegno.getUid())
				: soggettoDad.findClasseSoggettoByMovgest(impegno.getUid());
		
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
//			throw new BusinessException(ErroreFin.SOGGETTO_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto pagamento",
//				"impegno: il soggetto di pagamento deve appartenere alla classificazione"), Esito.FALLIMENTO);
		}
	}
	
	/**
	 * Controllo di coerenza dell-impegno
	 */
	protected void checkImpegno() {
		if(!entitaConUid(impegno) || entitaConUid(subImpegno)){
			return;
		}
		// Ho l'accertamento e NON il sub
		
		// Controllo validita' accertamento
		checkBusinessCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equalsIgnoreCase(impegno.getStatoOperativoMovimentoGestioneSpesa()),
				ErroreFin.MOVIMENTO_GESTIONE_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE.getErrore("L'impegno", "definitivo", "Non puo' essere imputato ad una predisposizione di pagamento"));
		
		// Controllo congruenza tra anno accertamento e anno bilancio
		checkBusinessCondition(impegno.getAnnoMovimento() <= bilancio.getAnno(),
				ErroreFin.MOVIMENTO_GESTIONE_PLURIENNALE_NON_AMMESSO_PER_OPERAZIONE.getErrore("impegno", "aggiornamento predisposizione di pagamento"));
	}
	
	/**
	 * Controllo di coerenza del subimpegno
	 */
	protected void checkSubImpegno() {
		if(!entitaConUid(impegno) || !entitaConUid(subImpegno)){
			return;
		}
		

		// Controllo congruenza tra anno subimpegno e anno bilancio
		checkBusinessCondition(subImpegno.getAnnoMovimento() <= bilancio.getAnno(),
				ErroreFin.MOVIMENTO_GESTIONE_PLURIENNALE_NON_AMMESSO_PER_OPERAZIONE.getErrore("subimpegno", "aggiornamento predisposizione di pagamento"));
		
		//controllo congruenza con capitolo
		checkBusinessCondition(!entitaConUid(capitoloUscitaGestione)
				|| !entitaConUid(subImpegno.getCapitoloUscitaGestione())
				|| capitoloUscitaGestione.getUid() == subImpegno.getCapitoloUscitaGestione().getUid(),
			ErroreFin.IMPEGNO_NON_PERTINENTE_AL_CAPITOLO_UPB.getErrore());
		
		//controllo validita' subaccertamento
		checkBusinessCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equalsIgnoreCase(subImpegno.getStatoOperativoMovimentoGestioneSpesa()),
				ErroreFin.MOVIMENTO_GESTIONE_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE.getErrore("Il subimpegno", "definitivo", "Non puo' essere imputato ad una predisposizione di pagamento"));
		
	}
}

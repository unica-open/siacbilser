/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.appjwebsrv.business.Esito;
import it.csi.appjwebsrv.business.EsitoServizio;
import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.LiquidazioneServiceHelper;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

/**
 * Completa l'{@link AllegatoAtto} con tutti i suoi {@link ElencoDocumentiAllegato}
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompletaAllegatoAttoService extends AllegatoAttoBaseService<CompletaAllegatoAtto,CompletaAllegatoAttoResponse> {

	//Services..
	@Autowired
	private AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaService;
	@Autowired
	protected LiquidazioneService liquidazioneService;
	
	//DADs..
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private SoggettoDad soggettoDad;

	//Components
	@Autowired
	private LiquidazioneServiceHelper liquidazioneServiceHelper;
	
	//Fields..
	private Bilancio bilancio;
	
	
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		checkCondition(req.getAllegatoAtto().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"), false);
		allegatoAtto = req.getAllegatoAtto();
		res.setAllegatoAtto(allegatoAtto);
		
		//l'ente viene caricato dalla classe base CheckedAccountBaseService
//		checkNotNull(req.getAllegatoAtto().getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente allegato atto"));
//		checkCondition(req.getAllegatoAtto().getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente allegato atto"), false);
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"), false);
	}
	
	@Override
	protected void init() {
		super.init();
		
		allegatoAttoDad.setEnte(ente);
		allegatoAttoDad.setLoginOperazione(loginOperazione);
		soggettoDad.setEnte(ente);
	}
	
	// SIAC-5089: timeout impostato a 3 ore
	// RM: 13/10/2017; su richiesta di Silvia impostato a 6
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT * 6)
	public CompletaAllegatoAttoResponse executeServiceTxRequiresNew(CompletaAllegatoAtto serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional
	public CompletaAllegatoAttoResponse executeService(CompletaAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		caricaBilancio();
		
		caricaAllegatoAtto();
		stampaDettaglioOperazione();
		checkStatoOperativoAllegatoAttoDaCompletare();
		checkDatiDurc();
		
		checkQuoteSpesaRilevantiIVA();
		checkQuoteEntrataCollegateAMovimento();
		checkQuoteSpesaNonCollegateALiquidazioneCollegateAMovimento();
		checkQuoteSospese();
		
		inserisciLiquidazioniEAggiornaStatoDocumenti();
		log.info("execute", "completo l'atto allegato "+ (res.getSubdocumentiScartati()==null || res.getSubdocumentiScartati().isEmpty()));
		if (res.getSubdocumentiScartati()==null || res.getSubdocumentiScartati().isEmpty()){
			aggiornaDataCompletamentoAllegatoAtto();		
			aggiornaStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.COMPLETATO);
		}
		aggiornaStatoElenchiCollegati(StatoOperativoElencoDocumenti.COMPLETATO);
	}
	
	private void caricaBilancio(){
		this.bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
	}
	
	/**
	 * Controllo lo stato operativo dell'allegato atto.
	 * <br>
	 * L'allegato deve essere in stato DA COMPLETARE, in caso contrario si invia il messaggio &lt;FIN_ERR_0226, Stato Allegato atto incongruente&gt;.
	 * <br>
	 * Verifica se &eacute; possibile annullare l'Allegato Atto controllando il diagramma degli stati dell'atto,
	 * se non &eacute; possibile segnala il messaggio &lt;FIN_ERR_0226, Stato Allegato Atto incongruente&gt;.
	 */
	protected void checkStatoOperativoAllegatoAttoDaCompletare() {
		final String methodName = "checkStatoOperativoAllegatoAttoDaCompletare";
		// Nota bene: la seconda condizione e' equivalente alla prima. Cfr. StateMachine dello StatoOperativoAllegatoAtto
		if(!StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(allegatoAtto.getStatoOperativoAllegatoAtto())) {
			log.debug(methodName, "Stato non valido: " + allegatoAtto.getStatoOperativoAllegatoAtto()
					+ ". Atteso " + StatoOperativoAllegatoAtto.DA_COMPLETARE);
			throw new BusinessException(ErroreFin.STATO_ATTO_DA_ALLEGATO_INCONGRUENTE.getErrore());
		}
	}
	
	/**
	 * non potrà essere inserita la liquidazione di soggetto con DURC scaduto 
	 * “DURC scaduto soggetto XXXX, operazione di completa Atto bloccata” oppure “DURC scaduto soggetto XXXX ricevente incasso del soggetto YYYYY, operazione di completa Atto bloccata” 
	 * In questo caso il completamento non va a buon fine.
	 */
	private void checkDatiDurc() {
		List<Integer> uidsSubdocConConfermaDurc = allegatoAttoDad.getUidsSubdocWithImpegnoConfermaDurc(allegatoAtto);
		if(uidsSubdocConConfermaDurc == null || uidsSubdocConConfermaDurc.isEmpty()) {
			return;
		}
		Map<String, Date> mappaSoggettoData = subdocumentoSpesaDad.getDataFineValiditaDurcAndSoggettoCodePiuRecenteBySubdocIds(uidsSubdocConConfermaDurc);
		Date now = new Date();
		
		//SIAC-7143
		String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(now);
		String dateFineDurc = null;
		
		for (String soggettoCode : mappaSoggettoData.keySet()) {
			Date dataFineValiditaDurc = mappaSoggettoData.get(soggettoCode);
			//SIAC-7143 e SIAC-7239
			dateFineDurc = dataFineValiditaDurc!= null? new SimpleDateFormat("yyyy-MM-dd").format(dataFineValiditaDurc) : null;			
			if(dateFineDurc== null  || dateNow.compareTo(dateFineDurc) > 0){
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il soggetto " + soggettoCode + " presenta dati Durc non validi."));
			}
		}
	}
	
	/**
	 * Le quote documento rilevanti ai fini IVA (flagRilevanteIVA = true) non ancora collegate a liquidazione devono avere valorizzato l'attributo
	 * nRegistrazioneIVA altrimenti non &egrave; possibile procedere con il completamento dell'AllegatoAtto segnalando l'errore
	 * &lt;COR_ERR_0031 - Aggiornamento non possibile (&lt;entit&agrave;&gt;: Quota documento +'identificativo doc e quota,
	 * &lt;operazione&gt;: mancante di numero registrazione IVA)&gt;.
	 */
	private void checkQuoteSpesaRilevantiIVA() {
		List<SubdocumentoSpesa> subdocs = allegatoAttoDad.findSubdocumentiSpesaRilevantiIvaNonCollegatiALiquidazioneSenzaNregIvaByAllegatoAtto(allegatoAtto);
		
		for(SubdocumentoSpesa subdoc : subdocs) {
			res.addErrore(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("Quota " + subdoc.getNumero()
					+ (subdoc.getDocumento() != null ? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "")
					+ " [uid:" + subdoc.getUid() + "]",
				"mancante di numero registrazione IVA"));
		}
		
		checkErrori(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getCodice());
	}
	
	/**
	 * Le quote di entrata devono avere collegato un movimento e il movimento deve appartenere allo stesso bilancio su cui si sta operando.
	 * &lt;COR_ERR_0031 - Aggiornamento non possibile (&lt;entit&agrave;&gt;: Quota documento +‘identificativo doc e quota,
	 * &lt;operazione&gt;: mancante di accertamento)&gt;.
	 */
	private void checkQuoteEntrataCollegateAMovimento() {
		List<SubdocumentoEntrata> subdocs = allegatoAttoDad.findSubdocumentiEntrataSenzaMovimentoDelloStessoBilancio(allegatoAtto, bilancio);
		
		for(SubdocumentoEntrata subdoc : subdocs) {
			res.addErrore(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("Quota " + subdoc.getNumero()
					+ (subdoc.getDocumento() != null? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "")
					+ " [uid:" + subdoc.getUid() + "]",
				"mancante di accertamento"));
		}
		
		checkErrori(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getCodice());
	}
	
	/**
	 * Le quote di spesa non collegate a liquidazione deve avere collegato un movimento e il movimento deve appartenere allo stesso bilancio su cui si sta operando. 
	 * &lt; COR_ERR_0031 Aggiornamento non possibile (&lt;entit&agrave;&gt;>: Quota documento + 'identificativo doc e quota'
	 * &lt;operazione&gt;: mancante di impegno)&gt;.
	 */
	private void checkQuoteSpesaNonCollegateALiquidazioneCollegateAMovimento() {
		List<SubdocumentoSpesa> subdocs = allegatoAttoDad.findSubdocumentiSpesaSenzaLiquidazioneESenzaMovimentoDelloStessoBilancio(allegatoAtto, bilancio);
		
		for(SubdocumentoSpesa subdoc : subdocs) {
			res.addErrore(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("Quota " + subdoc.getNumero()
					+ (subdoc.getDocumento() != null ? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : "")
					+ " [uid:" + subdoc.getUid() + "]",
				"mancante di impegno"));
		}
		
		checkErrori(ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getCodice());
	}
	
	
	private void checkQuoteSospese() {
		List<SubdocumentoSpesa> subdocs = allegatoAttoDad.findSubdocumentiSpesaSospesiByAllegatoAtto(allegatoAtto);
		if(subdocs == null || subdocs.isEmpty()){
			log.debug("checkQuoteSospese", "non ci sono subdocumenti sospesi");
		}
		res.setSubdocumentiScartati(subdocs);
	}
	
	
	/**
	 * Aggiorna lo sato di tutti gli elenchi collegati all'allegato atto;.
	 *
	 * @param stato the stato
	 */
	private void aggiornaStatoElenchiCollegati(StatoOperativoElencoDocumenti stato) {
		for(ElencoDocumentiAllegato elenco: allegatoAtto.getElenchiDocumentiAllegato()){
			
			log.info("aggiornaStatoElenchiCollegati","elenchi "+elenco.getUid());

			//SIAC-5572
			if(!elencoDocumentiAllegatoDad.xstQuoteSenzaLiquidazioneByElencoId(elenco.getUid())){	
				log.info("aggiornaStatoElenchiCollegati","AGGIORNATO");							
				aggiornaStatoOperativoElencoDocumentiAllegato(elenco, stato);
			}else{
				log.info("aggiornaStatoElenchiCollegati","NON AGGIORNATO");
			}
		}
	}

	/**
	 * Per ogni quota di documento di spesa associata all'allegato, 
	 * non ancora collegata a liquidazione e con  importoDaPagare  > 0 (si escludono con questa seconda condizione le quote collegate a note di credito) 
	 * &egrave; necessario inserire una liquidazione con questi attributi:
	 * <ul>
	 * <li>	in stato 'PROVVISORIO', 
	 * <li>con la descrizione uguale al campo causale 'ordinativo di pagamento' di ogni singola quota di spesa </li>
	 * <li>l'importo pari all'importoDaPagare della quota stessa . </li>
	 * <li>il Soggetto e  l'eventuale sede e la  modalit&agrave; di pagamento della liquidazione sono ricavati come segue:
	 * SE ModalitaPagamentoSoggetto.tipoAccredito = CSC (cessione di credito)
	 * ricercare la Modalit&agrave; di Pagamento e il Soggetto collegati attraverso la relazione "Cessione Credito ad altro Soggetto": 
	 * questi sono i dati da trasmettere come input per l'inserimento della liquidazione
	 * ALTRIMENTI utilizzare Modalit&agrave; di pagamento e soggetto della quota documento</li>
	 * <li>Gli altri dati della liquidazione (ad es. l'impegno o cup e cig) sono ricavati dalla quota documento di spesa a cui &egrave; legata.</li>
	 * </ul>
	 * 
	 * Per ogni documento di spesa per cui si &egrave; inserita una liquidazione 
	 * legata ad almeno una delle sue quote al punto precedente, &egrave; necessario richiamare l'operazione 
	 * che consente di aggiornarne lo stato (AggiornaStatoDocumentoDiSpesa)
	 * 
	 */
	
	private void inserisciLiquidazioniEAggiornaStatoDocumenti() {
		String methodName = "inserisciLiquidazioni";
		liquidazioneServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), bilancio, true);

		//SIAC-5572 controllo che non ci siano subdocumenti sospesi 
		//Integer num = allegatoAttoDad.countSubdocumentiSpesaRilevantiIvaNonCollegatiALiquidazioneConImportoDaPagareMaggioreDiZero(allegatoAtto);

		List<SubdocumentoSpesa> subdocs = allegatoAttoDad.findSubdocumentiSpesaRilevantiIvaNonCollegatiALiquidazioneConImportoDaPagareMaggioreDiZeroNonSospesi(allegatoAtto);
		log.info(methodName,"Trovati "+subdocs.size()+ " subdocumenti da cui vanno generate le liquidazioni.");

		
		Map<Integer,DocumentoSpesa> documentiDaAggiornare = new HashMap<Integer,DocumentoSpesa>();
		
		
		//Inserisce una liquidazione per ogni quota NON collegata ad una liquidazione
		for(SubdocumentoSpesa subdoc : subdocs) {
			
			log.info(methodName,"Inserimento liquidazione per Quota " + subdoc.getNumero()  +(subdoc.getDocumento()!=null? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc() : ""));
			
			checkModalitaPagamentoSoggetto(subdoc);
			
			Impegno impegno = subdoc.getImpegno();
			SubImpegno subImpegno = subdoc.getSubImpegno();
			impegnoBilDad.aggiungiCodiciClassificatoriAImpegnoEOSubImpegno(impegno,subImpegno);
			
			checkOrdiniEvasi(subdoc);
			
			
			Liquidazione l = liquidazioneServiceHelper.popolaLiquidazione(subdoc);
			l = liquidazioneServiceHelper.inserisciLiquidazione(l);
			
			subdocumentoSpesaDad.associaLiquidazione(subdoc, l);
			DocumentoSpesa documentoSpesa = subdoc.getDocumento();
			documentiDaAggiornare.put(documentoSpesa.getUid(), documentoSpesa);
			
		}
		
		//Aggiorna lo statoOperativoDocumento per ogni documento di spesa per cui si e' inserita una liquidazione
		for(DocumentoSpesa documentoSpesa : documentiDaAggiornare.values()){			
			log.info(methodName,"Aggiornamento Stato operativo Documento " + documentoSpesa.getDescAnnoNumeroTipoDoc());			
			aggiornaStatoOperativoDocumento(documentoSpesa);
		}

	
		
	}

	private void checkOrdiniEvasi(SubdocumentoSpesa subdoc) {
		final String methodName = "checkOrdiniEvasi";
		//se ci sono degli ordini e la configurazione dell'ente prevede che siano da verificare, controllo che siano evasi.
		boolean gestioneConVerifica = "CON_VERIFICA".equals(ente.getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_EVASIONE_ORDINI));
		
		if(gestioneConVerifica && liquidazioneServiceHelper.isOrdinePresente(subdoc.getDocumento())){
			EsitoServizio esitoServizio = liquidazioneServiceHelper.checkOrdiniEvasi(subdoc);
			log.logXmlTypeObject(esitoServizio, "ESITO");
			if(esitoServizio.getErrori() != null && !esitoServizio.getErrori().isEmpty()){
				StringBuilder sb = new StringBuilder();
				sb.append("impossibile inserire la liquidazione per la quota ");
				sb.append(subdoc.getNumero());
				sb.append("del documento");
				sb.append(subdoc.getDocumento().getNumero());
				sb.append(", la verifica di evasione degli ordini ha riscontrato i seguenti errori: ");
				
				for(String s  : esitoServizio.getErrori()){
					sb.append(s);
					sb.append(" ");
				}
				String msg = sb.toString();
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
			}
			//nel caso il servizio restituisse KO ma senza errori
			if(Esito.KO.equals(esitoServizio.getEsito())){
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile inserire la liquidazione per la quota "+ subdoc.getNumero() +" del documento "+ subdoc.getDocumento().getNumero() +" la verifica di evasione ha restituito esito negativo."));
			}
			log.debug(methodName, "gli ordini sono evasi, posso proseguire con l'inserimento della liquidazione.");
		}
	}

	private void checkModalitaPagamentoSoggetto(SubdocumentoSpesa subdoc) {
		if(subdoc.getModalitaPagamentoSoggetto()==null || subdoc.getModalitaPagamentoSoggetto().getUid()==0){
			throw new BusinessException(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("Quota " + subdoc.getNumero()  +
					(subdoc.getDocumento()!=null? " del documento " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc(): "")+" senza modalita' di pagamento. Impossibile inserire la liquidazione."));
		}
		
	}

//	/**
//	 * Popola una Liquidazione partire da un SubdocumentoSpesa.
//	 *
//	 * @param subdoc the subdoc
//	 * @return the liquidazione
//	 */
//	private Liquidazione popolaLiquidazione(SubdocumentoSpesa subdoc) {
//		Liquidazione l = new Liquidazione();
//		l.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.PROVVISORIO);
//		l.setDescrizioneLiquidazione(StringUtils.isNotBlank(subdoc.getCausaleOrdinativo())?subdoc.getCausaleOrdinativo():"");
//		l.setImportoAttualeLiquidazione(subdoc.getImportoDaPagare());
//		//TODO controllare
//		l.setImportoLiquidazione(subdoc.getImportoDaPagare());
//		l.setAttoAmministrativoLiquidazione(allegatoAtto.getAttoAmministrativo());
//		
//		//TODO SE ModalitaPagamentoSoggetto.tipoAccredito = CSC (cessione di credito) ricercare la Modalita' 
//		//di Pagamento e il Soggetto collegati attraverso la relazione "Cessione Credito ad altro Soggetto"
//		//ALTRIMENTi si ricopia dal subdoc
//		Soggetto soggettoLiquidazione = creaSoggettoLiquidazione(subdoc);
//		l.setSoggettoLiquidazione(soggettoLiquidazione);
//		gestisciCessioneDelCredito(l);
//		
//		//TODO controllare
//		Impegno impegno = subdoc.getImpegno();
//		SubImpegno subImpegno = subdoc.getSubImpegno();
//		impegnoBilDad.aggiungiCodiciClasssificatoriAImpegnoEOSubImpegno(impegno,subImpegno);
//		
//		if(subImpegno != null){
//			impostaCodiciNellaLiquidazione(subImpegno, l);
//		}else{
//			impostaCodiciNellaLiquidazione(impegno, l);
//		}
//		
//		l.setImpegno(impegno);
//		l.setSubImpegno(subImpegno);
//		l.setCup(subdoc.getCup());
//		l.setCig(subdoc.getCig());
//		
//		if(subdoc.getContoTesoreria() != null){
//			ContoTesoreria ct = new ContoTesoreria();
//			ct.setUid(subdoc.getContoTesoreria().getUid());
//			ct.setCodice(subdoc.getContoTesoreria().getCodice());
//			ct.setDescrizione(subdoc.getContoTesoreria().getDescrizione());
//			l.setContoTesoreria(ct);
//		}
//		
//		l.setForza(true);
//		
//		if(subdoc.getVoceMutuo() != null  && StringUtils.isNotBlank(subdoc.getVoceMutuo().getNumeroMutuo())) {
//			l.setNumeroMutuo(integerize(subdoc.getVoceMutuo().getNumeroMutuo()));
//		}
//		
//		//popolo la quota con dati minimi per il documento padre
//		SubdocumentoSpesa quota = new SubdocumentoSpesa();
//		quota.setUid(subdoc.getUid());
//		DocumentoSpesa doc = new DocumentoSpesa();
//		doc.setUid(subdoc.getDocumento().getUid());
//		doc.setTipoDocumento(subdoc.getDocumento().getTipoDocumento());
//		doc.setContabilizzaGenPcc(subdoc.getDocumento().getContabilizzaGenPcc());
//		quota.setDocumento(doc);
//		l.setSubdocumentoSpesa(quota);
//		
//		return l;
//	}
	
//	private Soggetto creaSoggettoLiquidazione(SubdocumentoSpesa subdoc) {
//		
//		Soggetto soggettoLiquidazione = new Soggetto();
//		
//		RicercaSoggettoPerChiaveResponse resRSPC = ricercaSoggettoPerChiave(subdoc.getDocumento().getSoggetto().getCodiceSoggetto());
//		Soggetto soggettoDocumento = resRSPC.getSoggetto();
//		if(soggettoDocumento == null || soggettoDocumento.getUid() == 0){
//			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", "codice " + subdoc.getDocumento().getSoggetto().getCodiceSoggetto()));
//		}
//		
//		soggettoLiquidazione.setUid(soggettoDocumento.getUid());
//		soggettoLiquidazione.setCodiceSoggetto(soggettoDocumento.getCodiceSoggetto());
//		soggettoLiquidazione.setStatoOperativo(soggettoDocumento.getStatoOperativo());
//		
//		List<ModalitaPagamentoSoggetto> modalitaPagamentoList = new ArrayList<ModalitaPagamentoSoggetto>();
//		if(resRSPC.getListaModalitaPagamentoSoggetto() == null) {
//			// TODO
//			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("ListaModalitaPagamentoSoggetto", "Soggetto codice " + subdoc.getDocumento().getSoggetto().getCodiceSoggetto()));
//		}
//		int uidSubdoc = subdoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2() != null && subdoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2().getUid() != 0
//				? subdoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2().getUid()
//				: subdoc.getModalitaPagamentoSoggetto().getUid();
//		for(ModalitaPagamentoSoggetto mps : resRSPC.getListaModalitaPagamentoSoggetto()){
//			int uid = mps.getModalitaPagamentoSoggettoCessione2() != null && mps.getModalitaPagamentoSoggettoCessione2().getUid() != 0
//					? mps.getModalitaPagamentoSoggettoCessione2().getUid()
//					: mps.getUid();
//			if(uid == uidSubdoc){
//				mps = ricercaModalitaPagamento(mps, soggettoDocumento);
//				modalitaPagamentoList.add(mps);
//				break;
//			}
//		}
//		
//		if(modalitaPagamentoList.isEmpty()){
//			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("ModalitaPagamentoSoggetto", "uid "+subdoc.getModalitaPagamentoSoggetto().getUid() +" modalita pagamento per Soggetto codice " + subdoc.getDocumento().getSoggetto().getCodiceSoggetto()));
//		}
//		soggettoLiquidazione.setModalitaPagamentoList(modalitaPagamentoList);
//		
//		if(subdoc.getSedeSecondariaSoggetto() != null){
//			List<SedeSecondariaSoggetto> sediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
//			SedeSecondariaSoggetto sedeSecondariaSoggetto = new SedeSecondariaSoggetto();
//			sedeSecondariaSoggetto.setUid(subdoc.getSedeSecondariaSoggetto().getUid());
//			sediSecondarie.add(sedeSecondariaSoggetto);
//			soggettoLiquidazione.setSediSecondarie(sediSecondarie);
//		}
//		
//		return soggettoLiquidazione;
//	}
//	
//	/**
//	 * Nel caso in cui la quota da liquidare è associata ad una modalità di pagamento 
//	 * di tipo cessione del credito (Tipo.Accredito=CSC) oltre alle verifiche ai punti precedenti,  
//	 * si inserisce la liquidazione da abbinare alla quota tramite il metodo Inserisce liquidazione  
//	 * del servizio Gestione liquidazione passando in input il 
//	 * 
//	 * soggetto e 
//	 * 
//	 * modalità di pagamento (con eventuale sede) 
//	 * 
//	 * che sono collegati alla modalità di pagamento della quota tramite la relazione 'Cessione del Credito ad altro soggetto'.
//	 */
//	private void gestisciCessioneDelCredito(Liquidazione liquidazione) {
//		
//		Soggetto soggettoLiquidazione = liquidazione.getSoggettoLiquidazione();
//		ModalitaPagamentoSoggetto mps = null;
//		if(soggettoLiquidazione.getModalitaPagamentoList()!=null && !soggettoLiquidazione.getModalitaPagamentoList().isEmpty()){
//			//riutilizzo ModalitaPagamentoSoggetto caricata dal servizio RicercaModalitaPagamentoPerChiave in precedenza (nel metodo #creaSoggettoLiquidazione)
//			mps = soggettoLiquidazione.getModalitaPagamentoList().get(0); 
//		}
//		if(mps == null) {
//			return;
//		}
//		
//		String codiceGruppoAccredito = getCodiceGruppoAccredito(mps);
//		
//		// TODO: configurazione per FA e PI
//		if(isTipoAccreditoCSCLike(codiceGruppoAccredito)) {
//			Soggetto soggettoCessione = ottieniSoggettoCessione(mps);
//			ModalitaPagamentoSoggetto mpsCessione = mps.getModalitaPagamentoSoggettoCessione2();
//			// ModalitaPagamentoSoggetto mpsCessione = ricercaModalitaPagamento(mps.getModalitaPagamentoSoggettoCessione2(), soggettoCessione);
//			popolaModalitaPagamentoCessione(liquidazione, mpsCessione, soggettoCessione);
//			return;
//		}
//		
//		//jira 2793
////		if(isTipoAccreditoCSILike(codiceGruppoAccredito)) {
////			Soggetto soggettoCessione = soggettoLiquidazione;
////			ModalitaPagamentoSoggetto mpsCessione = mps.getModalitaPagamentoSoggettoCessione2();
////			//ModalitaPagamentoSoggetto mpsCessione = ricercaModalitaPagamento(mps.getModalitaPagamentoSoggettoCessione2(), soggettoCessione);
////			popolaModalitaPagamentoCessione(liquidazione, mpsCessione, soggettoCessione);
////			return;
////		}
//	}
//	
//	private String getCodiceGruppoAccredito(ModalitaPagamentoSoggetto mps) {
//		return soggettoDad.ottieniCodiceGruppoAccreditoByTipoAccredito(mps.getTipoAccredito());
//	}
//
//	private boolean isTipoAccreditoCSCLike(String codiceGruppoAccredito) {
//		return TipoAccredito.CSC.name().equalsIgnoreCase(codiceGruppoAccredito);
//	}
//	
////	private boolean isTipoAccreditoCSILike(String codiceGruppoAccredito) {
////		return TipoAccredito.CSI.name().equalsIgnoreCase(codiceGruppoAccredito);
////	}
//	
//	private void popolaModalitaPagamentoCessione(Liquidazione liquidazione, ModalitaPagamentoSoggetto mpsCessione, Soggetto soggetto) {
//		List<ModalitaPagamentoSoggetto> modalitaPagamentoList = new ArrayList<ModalitaPagamentoSoggetto>();
//		modalitaPagamentoList.add(mpsCessione);
//		soggetto.setModalitaPagamentoList(modalitaPagamentoList);
//		liquidazione.setSoggettoLiquidazione(soggetto);
//		liquidazione.setModalitaPagamentoSoggetto(mpsCessione);
//	}
//
//	private Soggetto ottieniSoggettoCessione(ModalitaPagamentoSoggetto mps) {
//		RicercaSoggettoPerChiaveResponse resRSPC = ricercaSoggettoPerChiave(mps.getCessioneCodSoggetto());
//		Soggetto soggettoCessione = resRSPC.getSoggetto();
//		if(soggettoCessione == null || soggettoCessione.getUid() == 0){
//			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", "codice " + mps.getCessioneCodSoggetto()));
//		}
//		return soggettoCessione;
//	}
//	
//	private ModalitaPagamentoSoggetto ricercaModalitaPagamento(ModalitaPagamentoSoggetto mps, Soggetto soggetto) {
//		RicercaModalitaPagamentoPerChiave reqRMPC = new RicercaModalitaPagamentoPerChiave();
//		reqRMPC.setEnte(ente);
//		reqRMPC.setRichiedente(req.getRichiedente());
//		reqRMPC.setModalitaPagamentoSoggetto(mps);
//		reqRMPC.setSoggetto(soggetto);
//		RicercaModalitaPagamentoPerChiaveResponse resRMPS = soggettoService.ricercaModalitaPagamentoPerChiave(reqRMPC);
//		log.logXmlTypeObject(resRMPS, "response ricerca modalità pagamento per chiave: ");
//		return resRMPS.getModalitaPagamentoSoggetto();
//	}
//	
//	/**
//	 * Integerizzazione di una stringa.
//	 * 
//	 * @param str la stringa di partenza
//	 * @return l'Integer corrispondente, se valido; <code>null</code> altrimenti
//	 */
//	private Integer integerize(String str) {
//		final String methodName = "integerize";
//		try {
//			return Integer.valueOf(str);
//		} catch(NumberFormatException nfe) {
//			log.debug(methodName, "NumberFormatException per la stringa " + str + ": " + (nfe != null ? nfe.getMessage() : ""));
//		} catch(NullPointerException npe) {
//			log.debug(methodName, "NullPointerException per la stringa " + str + ": " + (npe != null ? npe.getMessage() : ""));
//		}
//		log.debug(methodName, "Fallimento dell'integerizzazione della stringa " + str + ". Restituisco null");
//		return null;
//	}
//	
//	
//	private void impostaCodiciNellaLiquidazione(Impegno impegno, Liquidazione l) {
//		l.setCodPdc(impegno.getCodPdc() != null ? impegno.getCodPdc() : "");
//		l.setCodTransazioneEuropeaSpesa(impegno.getCodTransazioneEuropeaSpesa() != null ? impegno.getCodTransazioneEuropeaSpesa() : "");
//		l.setCodRicorrenteSpesa(impegno.getCodRicorrenteSpesa() != null ? impegno.getCodRicorrenteSpesa() : "");
//		l.setCodContoEconomico(impegno.getCodContoEconomico() != null ? impegno.getCodContoEconomico() : "" );
//		l.setCodSiope(impegno.getCodSiope() != null ? impegno.getCodSiope() : "" );
//		l.setCodCofog(impegno.getCodCofog() != null ? impegno.getCodCofog() : "");
//		
//		// SIAC-3655
//		l.setCodCapitoloSanitarioSpesa(impegno.getCodCapitoloSanitarioSpesa() != null ? impegno.getCodCapitoloSanitarioSpesa() : "");
//		l.setCodPrgPolReg(impegno.getCodPrgPolReg() != null ? impegno.getCodPrgPolReg() : "");
//	}
//	
//	/**
//	 * Inserisci liquidazione.
//	 * 
//	 * @param liquidazione la liquidazione da inserire
//	 * @return 
//	 */
//	protected Liquidazione inserisciLiquidazione(Liquidazione liquidazione) {
//		InserisceLiquidazione reqIL = new InserisceLiquidazione();
//		reqIL.setRichiedente(req.getRichiedente());
//		reqIL.setBilancio(bilancio);
//		reqIL.setDataOra(now);
//		reqIL.setEnte(ente);
//		reqIL.setAnnoEsercizio("" + bilancio.getAnno());
//		reqIL.setLiquidazione(liquidazione);
//		
//		log.logXmlTypeObject(reqIL, "Request per InserisceLiquidazione.");
//		InserisceLiquidazioneResponse resIL = liquidazioneService.inserisceLiquidazione(reqIL);
//		log.logXmlTypeObject(resIL, "Risposta ottenuta dal servizio InserisceLiquidazione.");
//		checkServiceResponseFallimento(resIL);
//		return resIL.getLiquidazione();
//		
//		
//	}
	
	protected DocumentoSpesa aggiornaStatoOperativoDocumento(DocumentoSpesa documentoSpesa) {
		AggiornaStatoDocumentoDiSpesa reqAs = new AggiornaStatoDocumentoDiSpesa();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setDocumentoSpesa(documentoSpesa);
		reqAs.setBilancio(bilancio);
		AggiornaStatoDocumentoDiSpesaResponse resAs = executeExternalService(aggiornaStatoDocumentoDiSpesaService, reqAs);
		return resAs.getDocumentoSpesa();
	}

//	private RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiave(String codiceSoggetto) {
//		RicercaSoggettoPerChiave reqRSPC = new RicercaSoggettoPerChiave();
//		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
//		parametroSoggettoK.setCodice(codiceSoggetto /*documentoAssociato.getSoggetto().getCodiceSoggetto()*/);
//		reqRSPC.setParametroSoggettoK(parametroSoggettoK);
//		reqRSPC.setRichiedente(req.getRichiedente());
//		reqRSPC.setEnte(ente /*documentoAssociato.getEnte()*/);
//		RicercaSoggettoPerChiaveResponse resRSPC = soggettoService.ricercaSoggettoPerChiave(reqRSPC);
//		log.logXmlTypeObject(resRSPC, "response ricerca soggetto per chiave");
//		return resRSPC;
//	}

	/**
	 * La data di completamento &egrave; valorizzata con il valore passato in input se presente oppure con la data di sistema.

	 */
	private void aggiornaDataCompletamentoAllegatoAtto() {
		// La data di completamento allegato atto non esiste su DB. 
		// Potrebbe essere gestito come un campo derivato dall'ultimo cambio di stato in COMPLETATO.
		// In tal caso qui non bisogna fare niente. Sara' poi il converter a valorizzare il campo derivato.
		
	}

	// stampa nei log i dettaglio (info) dell'allegato atto
	private void stampaDettaglioOperazione() {
		String codiceElaborazione = "COMPLETA_ATTO";
		StringBuilder sb = new StringBuilder();
		sb.append("Elaborazione Completa per ");
		sb.append("Atto ");
		sb.append((allegatoAtto.getAttoAmministrativo() != null && allegatoAtto.getAttoAmministrativo().getAnno() != 0) ? allegatoAtto.getAttoAmministrativo().getAnno() : " ");
		sb.append("/");
		sb.append((allegatoAtto.getAttoAmministrativo() != null && allegatoAtto.getAttoAmministrativo().getNumero() != 0) ? allegatoAtto.getAttoAmministrativo().getNumero() : " ");
		sb.append("-");
		sb.append(allegatoAtto.getVersioneInvioFirmaNotNull());
		log.debug("stampaDettaglioOperazione", sb.toString());
		Messaggio m = new Messaggio();
		m.setCodice(codiceElaborazione);
		m.setDescrizione(sb.toString());
		res.addMessaggio(m);
	}
}

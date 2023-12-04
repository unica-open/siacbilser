/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.InserisciRegistroComunicazioniPCCService;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.model.CausalePCC;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.StatoDebito;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * Aggiorna lo stato di un documento Spesa.
 * 
 * <!--img alt="macchina a stati del documento" src="/docs/doc-files/statiDocumento.png" /-->
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStatoDocumentoDiSpesaServiceOld extends CheckedAccountBaseService<AggiornaStatoDocumentoDiSpesa, AggiornaStatoDocumentoDiSpesaResponse> {


	private DocumentoSpesa doc;
	private BigDecimal totaleQuoteENoteCreditoMenoImportiDaDedurre;
	private Boolean sommaCongruente = Boolean.FALSE;
	private StatoOperativoDocumento statoAttuale;
	private StatoOperativoDocumento statoNew;

	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	@Autowired
	private InserisciRegistroComunicazioniPCCService inserisciRegistroComunicazioniPCCService;
	@Autowired
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoSpesa();

		checkEntita(doc, "documento");

	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		
		capitoloUscitaGestioneDad.setEnte(ente);		
		capitoloUscitaGestioneDad.setLoginOperazione(loginOperazione);
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaStatoDocumentoDiSpesaResponse executeService(AggiornaStatoDocumentoDiSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		String methodName = "execute";

		caricaDettaglioDocumentoSpesaEQuote();
		initTotaleQuoteENoteCreditoMenoImportiDaDedurre();
		
		BigDecimal importo = doc.getImporto();
		
		sommaCongruente = ( importo.compareTo(totaleQuoteENoteCreditoMenoImportiDaDedurre) == 0);

		statoAttuale = doc.getStatoOperativoDocumento();
		statoNew = determinaStatoOperativoDocumento();
		
		log.debug(methodName, "statoAttuale: " + statoAttuale + " statoNew: " + statoNew + " sommaCongruente: " + sommaCongruente);

		if (!statoNew.equals(statoAttuale)) {			
			documentoSpesaDad.aggiornaStatoDocumentoSpesa(doc.getUid(), statoNew);
			doc.setStatoOperativoDocumento(statoNew);
			doc.setDataInizioValiditaStato(new Date());
			log.info(methodName, "stato aggiornato da: " + statoAttuale + " a: " + statoNew);
		}else{
			log.info(methodName, "lo stato non è cambiato, non è necessario aggiornarlo.");
		}
		
		res.setDocumentoSpesa(doc);
		res.setTotaleQuoteENoteCreditoMenoImportiDaDedurre(totaleQuoteENoteCreditoMenoImportiDaDedurre);
		res.setSommaCongruente(sommaCongruente);
		res.setStatoOperativoDocumentoPrecedente(statoAttuale);
		res.setStatoOperativoDocumentoNuovo(statoNew);
		res.setTutteLeQuoteSonoAssociateAImpegnoOSubImpegno(tutteLeQuoteSonoAssociateAImpegnoOSubImpegno());
		
		gestisciRegistrazioneGEN();
		
		gestisciPrimaRegistrazionePCC();
	}




	/**
	 * Determina lo stato attuale del documento analizzando lo stato dei suoi
	 * importi.
	 *
	 * @return the stato operativo documento
	 */
	private StatoOperativoDocumento determinaStatoOperativoDocumento() {
		final String methodName = "determinaStatoOperativoDocumento";
		
		//Modifica LottoK (20150617): Nel caso in cui la Nota collegata venisse Annullata oppure ridotta nel suo importo lo stato può tornare VALIDO 
		//oppure INCOMPLETO a seconda della situazione presente nelle quote. -> Quindi dallo stato STORNATO non rimane più bloccato!
		
//		if(StatoOperativoDocumento.STORNATO.equals(statoAttuale)){
//			log.debug(methodName, "lo stato è già STORNATO, non viene modificato");
//		    return StatoOperativoDocumento.STORNATO;  
//		}
		
		if(StatoOperativoDocumento.ANNULLATO.equals(statoAttuale)){
			log.debug(methodName, "lo stato è già ANNULLATO, non viene modificato");
			 return StatoOperativoDocumento.ANNULLATO;		
		}
		
		if(isEmesso()){
			return StatoOperativoDocumento.EMESSO;
		}
		if(isParzialmenteEmesso()){
			return StatoOperativoDocumento.PARZIALMENTE_EMESSO;
		}
		if(isLiquidato()){
			return StatoOperativoDocumento.LIQUIDATO;
		}
		if(isParzialmenteLiquidato()){
			return StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO;
		}
		if(isStornato()){
			return StatoOperativoDocumento.STORNATO;
		}
		if(isValido()){
			return StatoOperativoDocumento.VALIDO;
		}
		return StatoOperativoDocumento.INCOMPLETO;
		
	}


	/**
	 * Calcola la somma tra l'importo delle quote.
	 * 
	 * nota 2 : arrotondamento documento + importo totale quote documento -
	 * somma degli importi da dedurre di tutte le quote + importo totale note
	 * credito collegate al documento;
	 */
	private void initTotaleQuoteENoteCreditoMenoImportiDaDedurre() {
		final String methodName = "initTotaleQuoteENoteCreditoMenoImportiDaDedurre";
		
		totaleQuoteENoteCreditoMenoImportiDaDedurre = BigDecimal.ZERO;
		log.debug(methodName , "valore iniziale: " + totaleQuoteENoteCreditoMenoImportiDaDedurre);
		// TODO: JIRA SIAC-1103
		totaleQuoteENoteCreditoMenoImportiDaDedurre = totaleQuoteENoteCreditoMenoImportiDaDedurre.add(doc.getArrotondamento().abs());
		log.debug(methodName , "valore precedente + arrotondamento: " + totaleQuoteENoteCreditoMenoImportiDaDedurre);
		totaleQuoteENoteCreditoMenoImportiDaDedurre = totaleQuoteENoteCreditoMenoImportiDaDedurre.add(doc.calcolaImportoTotaleSubdoumenti()).subtract(doc.calcolaTotaleOneriRC());
		log.debug(methodName, "doc.calcolaTotaleOneriRC(): "+doc.calcolaTotaleOneriRC());
		log.debug(methodName , "valore precedente + totale subdocumenti - totale imponibile importo degli oneri con TipoIvaSplitReverse=RC : " + totaleQuoteENoteCreditoMenoImportiDaDedurre);
		totaleQuoteENoteCreditoMenoImportiDaDedurre = totaleQuoteENoteCreditoMenoImportiDaDedurre.subtract(doc.calcolaImportoTotaleDaDedurreSobdocumenti());
		log.debug(methodName , "valore precedente - importo da dedurre: " + totaleQuoteENoteCreditoMenoImportiDaDedurre);
		totaleQuoteENoteCreditoMenoImportiDaDedurre = totaleQuoteENoteCreditoMenoImportiDaDedurre.add(doc.calcolaImportoTotaleNoteCollegateSpesaNonAnnullate());
		log.debug(methodName , "valore precedente + note credito: " + totaleQuoteENoteCreditoMenoImportiDaDedurre);
		
	}

	/**
	 * Carica dettaglio documento spesa e quote.
	 */
	private void caricaDettaglioDocumentoSpesaEQuote() {
		doc = documentoSpesaDad.findDocumentoSpesaByIdAfterFlushAndClear(doc.getUid());
		List<SubdocumentoSpesa> result = subdocumentoSpesaDad.findSubdocumentiSpesaByIdDocumentoAfterFlushAndClear(doc.getUid());
		doc.setListaSubdocumenti(result);
		
		log.logXmlTypeObject(doc, "doc dopo ricerca dettaglio:");
		

	}

	/**
	 * STORNATO: se il suo importo è uguale all’importo dell’eventuale Nota di
	 * credito collegata.
	 * 
	 * Nel caso in cui la Nota collegata venisse annullata oppure ridotta nel suo importo 
	 * lo stato può tornare VALIDO oppure INCOMPLETO a seconda della situazione presente nelle quote.
	 *
	 * @return true, if is stornato
	 */
	private boolean isStornato() {
		final String methodName = "isStornato";
		BigDecimal importo = doc.getImporto();
		BigDecimal totaleNoteCredito = doc.calcolaImportoTotaleNoteCollegateSpesaNonAnnullate();
		boolean isStornato = importo.compareTo(totaleNoteCredito) == 0;
		log.debug(methodName, "Importo documento: " + importo + "; totale note collegate: " + totaleNoteCredito + ". Stornato? " + isStornato);
		return isStornato;
	}

	/**
	 * VALIDO: se tutte le sue quote ‘valide’ (nota1) hanno un impegno o
	 * subimpegno associato e l’importo del documento è uguale alla somma nota2.
	 *
	 * @return true, if is valido
	 */
	private boolean isValido() {
		String methodName = "isValido";
		
		boolean result = Boolean.TRUE.equals(tutteLeQuoteSonoAssociateAImpegnoOSubImpegno()) && sommaCongruente.booleanValue();

		log.debug(methodName, "returning: " + result);
		return result;
	}

	
	
	private Boolean tutteLeQuoteSonoAssociateAImpegnoOSubImpegno(){
		String methodName = "tutteLeQuoteSonoAssociateAImpegnoOSubImpegno";
		
		for (SubdocumentoSpesa quota : doc.getListaSubdocumenti()) {
			if (isQuotaAZero(quota)) {
				continue; // da non considerare
			}
			if (quota.getImpegno() == null && quota.getSubImpegno() == null) {
				log.debug(methodName, "Ho trovato almeno una quota senza impegno. Returning false.");
				return Boolean.FALSE;
			}
		}
		log.debug(methodName, "Tutte le quote hanno impegno o subImpegno. Returning true.");
		return Boolean.TRUE;
	}
	

	/**
	 * Checks if is parzialmente liquidato.
	 *
	 * @return true, if is parzialmente liquidato
	 */
	private boolean isParzialmenteLiquidato() {
		
		String methodName = "isParzialmenteLiquidato";
		
		for (SubdocumentoSpesa quota : doc.getListaSubdocumenti()) {
			if (isQuotaAZero(quota)) {
				continue; // da non considerare
			}
			if (quota.getLiquidazione() != null && quota.getLiquidazione().getUid() != 0) {
				log.debug(methodName, "Ho trovato almeno una quota con liquidazione, lo stato è PARZIALMENTE LIQUIDATO");
				return true;
			}
		}
		log.debug(methodName, "Non ho trovato nessuna quota con liquidazione, lo stato NON è PARZIALMENTE LIQUIDATO");
		return false;
		
	}

	/**
	 * Checks if is liquidato.
	 *
	 * @return true, if is liquidato
	 */
	private boolean isLiquidato() {
		String methodName = "isLiquidato";
		
		for (SubdocumentoSpesa quota : doc.getListaSubdocumenti()) {
			if (isQuotaAZero(quota)) {
				continue; // da non considerare
			}
			if (quota.getLiquidazione() == null || quota.getLiquidazione().getUid() == 0) {
				log.debug(methodName, "Ho trovato almeno una quota senza liquidazione, lo stato non è liquidato");
				return false;
				
			}
		}
		
		log.debug(methodName, "è liquidato? " + sommaCongruente);
		return sommaCongruente.booleanValue();
	}

	/**
	 * Checks if is parzialmente emesso.
	 *
	 * @return true, if is parzialmente emesso
	 */
	private boolean isParzialmenteEmesso() {
		String methodName = "isParzialmenteEmesso";
		
		for (SubdocumentoSpesa quota : doc.getListaSubdocumenti()) {
			if (isQuotaAZero(quota)) {
				continue; // da non considerare
			}
			if (quota.getOrdinativo() != null && quota.getOrdinativo().getUid() != 0) {
				log.debug(methodName, "Ho trovato almeno una quota con ordinativo, lo stato è PARZIALMENTE EMESSO");
				return true;
			}
		}
		
		log.debug(methodName, "Non ho trovato nessuna quota con ordinativo, lo stato NON è PARZIALMENTE EMESSO");
		return false;
	}

	/**
	 * Checks if is emesso.
	 *
	 * @return true, if is emesso
	 */
	private boolean isEmesso() {
		String methodName = "isEmesso";
		
		for (SubdocumentoSpesa quota : doc.getListaSubdocumenti()) {
			if (isQuotaAZero(quota)) {
				continue; // da non considerare
			}
			if (quota.getOrdinativo() == null || quota.getOrdinativo().getUid() == 0) {
				log.debug(methodName, "Ho trovato almeno una quota senza ordinativo, lo stato non può essere emesso");
				return false;
				
			}
		}
		
		log.debug(methodName, "è emesso? " + sommaCongruente);
		return sommaCongruente.booleanValue();
	}

	/**
	 * nota 1 : nel calcolo dello stato non bisogna considerare le quote a
	 * ‘zero’, ossia quelle che sono eventualmente collegate ad una nota credito
	 * per un importo da dedurre per quella quota=importo della quota stessa.
	 *
	 * @param quota the quota
	 * @return true, if is quota a zero
	 */
	private boolean isQuotaAZero(SubdocumentoSpesa quota) {
		return quota.getImporto().compareTo(quota.getImportoDaDedurre()) == 0;
	}
	
	
	
	
	
	
	
	
	
	
	/* ############################################ Attivazione GEN ############################################ */
	
	
	/**
	 * Nel caso in cui il Tipo Documento del documento 'padre' della quota abbia
	 * la gestione della Contabilità Generale (entità Tipo documento
	 * flagAttivaGEN = TRUE) è necessario effettuare queste operazioni:
	 * <ul>
	 * <li>Se esiste una richiesta di registrazione prima nota nel registro
	 * (entità RegistrazioneMovFin) in stato diverso da 'ANNULLATO' richiamare
	 * il servizio Annulla Prima Nota del modulo GEN, altrimenti proseguire
	 * senza richiamare questo servizio;</li>
	 * <li>Registrare sul registro (entità RegistrazioneMovFin) le richieste di
	 * prima nota necessarie sia per il documento che per le eventuali Note di
	 * credito ad esso associate in base alle indicazioni e alle condizioni
	 * descritte nel documento 'BIL-MULT-SIAC-REQ-009- Raccordi FinGen
	 * Configurazione'-foglio 'Evento GEN DB'.</li>
	 * </ul>
	 */
	private void gestisciRegistrazioneGEN() {
		String methodName = "gestisciRegistrazioneGEN";
		
		Boolean flagAttivaGEN = doc.getTipoDocumento().getFlagAttivaGEN();
		log.debug(methodName, "flagAttivaGEN: " + flagAttivaGEN + " tipoDocumento: " + doc.getTipoDocumento().getCodice());
		
		if(!Boolean.TRUE.equals(flagAttivaGEN)){
			log.debug(methodName, "Documento [uid:"+doc.getUid()+"] senza Flag Attiva GEN");
			return;
		}
		
		if(!Boolean.TRUE.equals(doc.getContabilizzaGenPcc())){
			log.debug(methodName, "Documento [uid:"+doc.getUid()+"] senza Flag Contabilizza GEN PCC");
			return;
		}
		
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, doc.getAnno());
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.SUBDOCUMENTO_SPESA, doc); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin); //se la registrazione esisteva devo annullare le eventuali primeNote associate
		
		if(!isCondizioneDiAttivazioneGENSoddisfatta()) {
			log.debug(methodName, "condizione di attivazione non soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
			return;
		}
		
		Evento evento = registrazioneGENServiceHelper.determinaEvento(TipoCollegamento.SUBDOCUMENTO_SPESA, registrazioniMovFin!=null && !registrazioniMovFin.isEmpty(), almenoUnaQuotaRilevanteIVA(doc),false,false,false);
		Evento eventoCEC = registrazioneGENServiceHelper.determinaEventoCassaEconomaleENotaCredito(TipoCollegamento.SUBDOCUMENTO_SPESA, registrazioniMovFin!=null && !registrazioniMovFin.isEmpty(), true);
		
		List<RegistrazioneMovFin> registrazioniMovFinInserite = new ArrayList<RegistrazioneMovFin>();
		for(SubdocumentoSpesa subdoc : doc.getListaSubdocumenti()) {
			//le quote "AZero" (isQuotaAZero==true) NON hanno per forza l'impegno o subImpegno quindi non posso ricavare l'elementoPianoDeiConti!
			ElementoPianoDeiConti elementoPianoDeiConti = null;
			if(subdoc.getImpegno()!=null && subdoc.getImpegno().getCapitoloUscitaGestione()!=null) { 
				log.debug(methodName, "Uid capitolo da cui ricavare l'elementoPianoDeiConti: " + subdoc.getImpegno().getCapitoloUscitaGestione());
				elementoPianoDeiConti = capitoloUscitaGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(subdoc.getImpegno().getCapitoloUscitaGestione());
				log.debug(methodName, "trovato elementoPianoDeiConti: "+(elementoPianoDeiConti!=null?elementoPianoDeiConti.getUid():"null"));
			} else {
				log.debug(methodName, "Impossibile reperire l'elementoPianoDeiConti in quanto non ho un capitolo associato all'impegno del documento della quota con uid: "+subdoc.getUid());
			}
			RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subdoc , elementoPianoDeiConti);
			registrazioniMovFinInserite.add(registrazioneMovFin);
			
			if(Boolean.TRUE.equals(doc.getCollegatoCEC())){
				log.debug(methodName, "Inserisco anche la registrazione per la Cassa Economale relativa alla quota con uid: "+subdoc.getUid());
				
//				Evento eventoCEC = registrazioneGENServiceHelper.determinaEventoCassaEconomale(subdoc, registrazioniMovFin!=null && !registrazioniMovFin.isEmpty());
			    RegistrazioneMovFin registrazioneMovFinCEC = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(eventoCEC, subdoc , elementoPianoDeiConti);
			    registrazioniMovFinInserite.add(registrazioneMovFinCEC);
			}
			
		}
		
		res.setRegistrazioniMovFinInserite(registrazioniMovFinInserite);
		
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInserite);
		
	}
	



	/**
	 * Condizione generale fatture (1)
	 * inserire il dato sul registro quando il tipo documento prevede l'attivazione di gen e quando:
	 * -> tutte le quote finanziarie hanno i movimenti gestione (impegni/accertamenti) associati
	 * -> il totale degli importi delle quote = importo netto ossia IMPORTO DOCUMENTO + ARROTONDAMENTO + IMPORTO ONERE DI TIPO REVERSE CHARGE COMPLESSIVO ASSOCIATO AL DOCUMENTO (Prima era: il totale degli importi delle quote inserite + arrotondamento e' uguale al totale documento)
	 * -> se esiste almeno una quota rilevante iva tutti i dati fiscali delle quote rilevanti iva devono essere stati inseriti (quindi deve essere presente il numero registrazione iva per tutte le quote rilevanti iva)
	 * -> tutti i movimenti gestione (impegni/accertamenti) associati alle quote non hanno gia' un record sul registro dell'entita' 'registrazionemovfin' in stato diverso da 'annullato', se così fosse visualizzare il messaggio informativo <gen_inf_0010, movimenti collegati alle quote con prime note già collegate.> e proseguire nell'operazione.
	 * 
	 * questo controllo deve essere effettuato ad ogni salvataggio effettuato sia nel mondo finanziario che nel mondo fiscale dei documenti iva.
	 * nota: la condizione con o senza iva nella colonna 'evento descrizione' è discriminata dalla presenza o meno del dettaglio iva per il documento selezionato.
	 * 
	 * 
	 * 
	 * 
	 * Condizione generale note credito (2)
	 * inserire il dato sulregistro quando la nota di credito e' associata ad un documento e non e' un documento a se' stante (vedasi entità 'tipo associazione') e quando:
	 * ->  il totale degli importi da dedurre sulle quote del documento abbinato = totale della somma degli importi delle note di credito associate al documento stesso
	 * -> se uno degli importi da dedurre e' legato ad una o + quote rilevanti iva deve esistere anche la nota di credito iva abbinata
	 * 
	 * questo controllo deve essere effettuato ad ogni salvataggio effettuato sia nel mondo finanziario che nel mondo fiscale dei documenti iva.
	 * nota: la condizione con o senza iva nella colonna 'evento descrizione' è discriminata dalla presenza o meno del dettaglio iva per il documento selezionato.
	 * 
	 * 
	 * @return true se la condizione di attivazione è soddisfatta.
	 */
	private boolean isCondizioneDiAttivazioneGENSoddisfatta() {
		//subdoc.getDocumento().getStatoOperativoDocumento() arriva dall'aggiornaStato eseguito precedentemente.
		final String methodName = "isCondizioneDiAttivazioneSoddisfatta";
		
		Boolean flagAttivaGEN = doc.getTipoDocumento().getFlagAttivaGEN();
		log.debug(methodName, "flagAttivaGEN: " + flagAttivaGEN + " tipoDocumento: " + doc.getTipoDocumento().getCodice());
		
		Boolean tutteLeQuoteSonoAssociateAImpegnoOSubImpegno = res.getTutteLeQuoteSonoAssociateAImpegnoOSubImpegno(); 
		log.debug(methodName, "tutteLeQuoteSonoAssociateAImpegnoOSubImpegno: "+tutteLeQuoteSonoAssociateAImpegnoOSubImpegno);
		
		Boolean isSommaCongruente = res.getSommaCongruente();
		log.debug(methodName, "sommaCongruente: " + isSommaCongruente);
		
		Boolean quoteRilevantiIVAENumeroRegistrazioneCongruenti = isQuoteRilevantiIVAENumeroRegistrazioneCongruenti(doc);
		log.debug(methodName, "quoteRilevantiIVAENumeroRegistrazioneCongruenti: "+quoteRilevantiIVAENumeroRegistrazioneCongruenti);
		
		
		return (flagAttivaGEN!=null && flagAttivaGEN.booleanValue()) &&
				tutteLeQuoteSonoAssociateAImpegnoOSubImpegno.booleanValue() 
				&& isSommaCongruente.booleanValue() 
				&& quoteRilevantiIVAENumeroRegistrazioneCongruenti.booleanValue();
	}

	/**
	 * se esiste almeno una quota rilevante iva tutti i dati fiscali delle quote rilevanti iva 
	 * devono essere stati inseriti (quindi deve essere presente il numero registrazione iva per tutte le quote rilevanti iva)
	 * @return
	 */
	private Boolean isQuoteRilevantiIVAENumeroRegistrazioneCongruenti(DocumentoSpesa ds) {
		String methodName = "isQuoteRilevantiIVAENumeroRegistrazioneCongruenti";
		
		Boolean almenoUnaQuotaRilevanteIva = almenoUnaQuotaRilevanteIVA(ds);
		
		if(almenoUnaQuotaRilevanteIva.booleanValue()){
			for(SubdocumentoSpesa subdoc : ds.getListaSubdocumenti()) {
				Boolean flagRilevanteIva = subdoc.getFlagRilevanteIVA();
				if(Boolean.TRUE.equals(flagRilevanteIva) && StringUtils.isBlank(subdoc.getNumeroRegistrazioneIVA())) {
					log.debug(methodName,"la quota "+ subdoc.getUid() + " rilevanteIva non ha il numero di registrazione valorizzato. Returning false.");
					return Boolean.FALSE;
				}
			}
		}
		
		return Boolean.TRUE;
		
		
	}

	/**
	 * Almeno una quota rilevante iva.
	 *
	 * @param ds the documentoSpesa
	 * @return true se presente almeno una quota rilevante IVA
	 */
	private Boolean almenoUnaQuotaRilevanteIVA(DocumentoSpesa ds) {
		for(SubdocumentoSpesa subdoc : ds.getListaSubdocumenti()) {
			Boolean flagRilevanteIva = subdoc.getFlagRilevanteIVA();
			if(Boolean.TRUE.equals(flagRilevanteIva)){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	/* ############################################ Attivazione PCC ############################################ */
	
	/**
	 * La prima comunicazione di contabilizzazione vs PCC deve essere registrata quando tutte le quote vengono associate ai relativi impegni
	 * (Tipo Operazione: <code>CONTABILIZZAZIONE</code>, Satto del debito:<code>SOSP</code>, Causale: <code>ATTILIQ</code>).
	 */
	private void gestisciPrimaRegistrazionePCC() {
		final String methodName = "gestisciPrimaRegistrazionePCC";
		
		if(!Boolean.TRUE.equals(doc.getContabilizzaGenPcc())  || !Boolean.TRUE.equals(doc.getTipoDocumento().getFlagComunicaPCC())){
			log.debug(methodName, "documento " + doc.getUid() + " senza flag contabilizzaGenPcc o ComunicaPcc, non inserisco la registrazione.");
			return;
		}
		
		// Inizializzazione DAD
		registroComunicazioniPCCDad.setEnte(ente);
		registroComunicazioniPCCDad.setLoginOperazione(loginOperazione);
		
		boolean registrazioneAttivabile = true;
		for(int i = 0; registrazioneAttivabile && i < doc.getListaSubdocumenti().size(); i++) {
			SubdocumentoSpesa ss = doc.getListaSubdocumenti().get(i);
			if(ss.getImpegnoOSubImpegno() == null || ss.getImpegnoOSubImpegno().getUid() == 0) {
				log.debug(methodName, "Subdocumento " + ss.getUid() + " senza impegno o subimpegno associato");
				registrazioneAttivabile = false;
			} else if(hasAlreadyAPCC(ss)) {
				log.debug(methodName, "Subdocumento " + ss.getUid() + " con una contabilizzazione PCC gia' associata");
				registrazioneAttivabile = false;
			}
		}
		
		if(!registrazioneAttivabile) {
			log.debug(methodName, "Registrazione PCC non attivabile");
			return;
		}
		
		// Attivo per ogni quota la registrazione PCC
		TipoOperazionePCC tipoOperazionePCC = registroComunicazioniPCCDad.findTipoOperazionePCCByValue(TipoOperazionePCC.Value.Contabilizzazione /*"CO"*/);
		StatoDebito statoDebitoSosp = registroComunicazioniPCCDad.findStatoDebitoByValue(StatoDebito.Value.EsigibilitaImportoSospesa /*"SOSP"*/);
		StatoDebito statoDebitoLiq = registroComunicazioniPCCDad.findStatoDebitoByValue(StatoDebito.Value.ImportoLiquidato); //Codice("LIQ");
		CausalePCC causalePCCAttiliq = registroComunicazioniPCCDad.findCausalePCCByValue(CausalePCC.Value.AttesaLiquidazione);//Codice("ATTLIQ");
		for(SubdocumentoSpesa ss : doc.getListaSubdocumenti()) {
			StatoDebito statoDebito = ss.getLiquidazione() != null && ss.getLiquidazione().getUid() != 0 ? statoDebitoLiq : statoDebitoSosp;
			CausalePCC causalePCC = ss.getLiquidazione() != null && ss.getLiquidazione().getUid() != 0 ? null : causalePCCAttiliq;
			creaRegistrazionePCCPerSubdocumento(ss, tipoOperazionePCC, statoDebito, causalePCC);
		}
	}

	private boolean hasAlreadyAPCC(SubdocumentoSpesa ss) {
		Long operazioni = registroComunicazioniPCCDad.countOperazioniPccBySubdocumentoAndCodiceTipoOperazione(ss.getUid(), TipoOperazionePCC.Value.Contabilizzazione);
		return operazioni != null && operazioni.longValue() > 0L;
	}
	
	private void creaRegistrazionePCCPerSubdocumento(SubdocumentoSpesa ss, TipoOperazionePCC tipoOperazionePCC, StatoDebito statoDebito, CausalePCC causalePCC) {
		final String methodName = "creaRegistrazionePCCPerSubdocumento";
		InserisciRegistroComunicazioniPCC request = new InserisciRegistroComunicazioniPCC();
		
		request.setDataOra(new Date());
		request.setRichiedente(req.getRichiedente());
		
		RegistroComunicazioniPCC registroComunicazioniPCC = new RegistroComunicazioniPCC();
		registroComunicazioniPCC.setCausalePCC(causalePCC);
		registroComunicazioniPCC.setTipoOperazionePCC(tipoOperazionePCC);
		registroComunicazioniPCC.setEnte(doc.getEnte());
		registroComunicazioniPCC.setStatoDebito(statoDebito);
		request.setRegistroComunicazioniPCC(registroComunicazioniPCC);
		
		// Imposto il minimo dei dati
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(doc.getUid());
		registroComunicazioniPCC.setDocumentoSpesa(documentoSpesa);
		
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(ss.getUid());
		subdocumentoSpesa.setDataScadenza(ss.getDataScadenza());
		registroComunicazioniPCC.setSubdocumentoSpesa(subdocumentoSpesa);
		
		InserisciRegistroComunicazioniPCCResponse response = serviceExecutor.executeServiceSuccess(InserisciRegistroComunicazioniPCCService.class, request);
		log.debug(methodName, "Inserita comunicazione PCC con uid " + response.getRegistroComunicazioniPCC().getUid() + " per il subdocumento " + ss.getUid() + " e il documento " + doc.getUid()
				+ " (tipo operazione " + tipoOperazionePCC.getUid() + "-" + tipoOperazionePCC.getCodice() + ")"
				+ " (stato debito " + statoDebito.getUid() + "-" + statoDebito.getCodice() + ")"
				+ " (causale pcc " + (causalePCC != null ? causalePCC.getUid() + "-" + causalePCC.getCodice() : "null") + ")");
	}
}

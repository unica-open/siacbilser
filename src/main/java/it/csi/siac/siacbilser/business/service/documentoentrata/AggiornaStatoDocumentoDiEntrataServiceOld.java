/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

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
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAmmTipoEnum;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

// TODO: Auto-generated Javadoc
/**
 * The Class AggiornaStatoDocumentoDiEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStatoDocumentoDiEntrataServiceOld extends CheckedAccountBaseService<AggiornaStatoDocumentoDiEntrata, AggiornaStatoDocumentoDiEntrataResponse> {

	private DocumentoEntrata doc;
	private BigDecimal totaleQuoteENoteCreditoMenoImportiDaDedurre;
	private Boolean sommaCongruente = Boolean.FALSE;
	private StatoOperativoDocumento statoAttuale;
	private StatoOperativoDocumento statoNew;

	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
	@Autowired
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	
	@Autowired
	private RicercaDettaglioDocumentoEntrataService ricercaDettaglioDocumentoEntrataService;
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoEntrata();
		
		checkEntita(doc, "documento di entrata");
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoEntrataDad.setLoginOperazione(loginOperazione);
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
		
		capitoloEntrataGestioneDad.setEnte(ente);		
		capitoloEntrataGestioneDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaStatoDocumentoDiEntrataResponse executeService(AggiornaStatoDocumentoDiEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		String methodName = "execute";

		caricaDettaglioDocumentoEntrataEQuote();
		initTotaleQuoteENoteCreditoMenoImportiDaDedurre();
		
		BigDecimal importo = doc.getImporto();
		sommaCongruente = ( importo.compareTo(totaleQuoteENoteCreditoMenoImportiDaDedurre) == 0);

		statoAttuale = doc.getStatoOperativoDocumento(); 
		statoNew = determinaStatoOperativoDocumento();

		if (!statoNew.equals(doc.getStatoOperativoDocumento())) {			
			log.info(methodName, "Aggiornamento dello stato da "+doc.getStatoOperativoDocumento()+ " a "+statoNew);
			documentoEntrataDad.aggiornaStatoDocumentoEntrata(doc.getUid(), statoNew);
			doc.setStatoOperativoDocumento(statoNew);
			doc.setDataInizioValiditaStato(new Date());
		}
		
		res.setDocumentoEntrata(doc);
		res.setTotaleQuoteENoteCreditoMenoImportiDaDedurre(totaleQuoteENoteCreditoMenoImportiDaDedurre);
		res.setSommaCongruente(sommaCongruente);
		res.setStatoOperativoDocumentoPrecedente(statoAttuale);
		res.setStatoOperativoDocumentoNuovo(statoNew);
		res.setTutteLeQuoteSonoAssociateAdAccertamentoOSubAccertamento(tutteLeQuoteSonoAssociateAdAccertamentoOSubAccertamento());
		
		gestisciRegistrazioneGEN();

	}
	

	

	/**
	 * Determina lo stato attuale del documento analizzando lo stato dei suoi
	 * importi.
	 *
	 * @return the stato operativo documento
	 */
	private StatoOperativoDocumento determinaStatoOperativoDocumento() {
		
		//Modifica LottoK (20150617): Nel caso in cui la Nota collegata venisse Annullata oppure ridotta nel suo importo lo stato può tornare VALIDO
		// oppure INCOMPLETO a seconda della situazione presente nelle quote. -> Quindi dallo stato STORNATO non rimane più bloccato!
		
//		if(StatoOperativoDocumento.STORNATO.equals(statoAttuale)){
//			return StatoOperativoDocumento.STORNATO;
//		}
		if(StatoOperativoDocumento.ANNULLATO.equals(statoAttuale)){
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
		
		totaleQuoteENoteCreditoMenoImportiDaDedurre = BigDecimal.ZERO;
		// TODO: JIRA SIAC-1103
		totaleQuoteENoteCreditoMenoImportiDaDedurre = totaleQuoteENoteCreditoMenoImportiDaDedurre.add(doc.getArrotondamento().abs());
		totaleQuoteENoteCreditoMenoImportiDaDedurre = totaleQuoteENoteCreditoMenoImportiDaDedurre.add(doc.calcolaImportoTotaleSubdoumenti()).subtract(doc.calcolaImportoTotaleDaDedurreSobdocumenti());
		totaleQuoteENoteCreditoMenoImportiDaDedurre = totaleQuoteENoteCreditoMenoImportiDaDedurre.add(doc.calcolaImportoTotaleNoteCollegateEntrataNonAnnullate());

	}

	/**
	 * Carica dettaglio documento entrata e quote.
	 */
	private void caricaDettaglioDocumentoEntrataEQuote() {
		
		doc = documentoEntrataDad.findDocumentoEntrataByIdAfterFlushAndClear(doc.getUid());
		List<SubdocumentoEntrata> result = subdocumentoEntrataDad.findSubdocumentiEntrataByIdDocumentoAfterFlushAndClear(doc.getUid());
		doc.setListaSubdocumenti(result);
		
		log.logXmlTypeObject(doc, "doc dopo ricerca dettaglio:");
		

	}

	
	/**
	 * STORNATO: se il suo importo è uguale all’importo dell’eventuale Nota di
	 * credito collegata.
	 *
	 * @return true, if is stornato
	 */
	private boolean isStornato() {
		log.debug("isStornato", "importo documento: "+ doc.getImporto());
		log.debug("isStornato", "importo totale note: "+ doc.calcolaImportoTotaleNoteCollegateEntrataNonAnnullate());
		return doc.getImporto().compareTo(doc.calcolaImportoTotaleNoteCollegateEntrataNonAnnullate()) == 0;
	}

	/**
	 * VALIDO: se tutte le sue quote ‘valide’ (nota1) hanno un impegno o
	 * subimpegno associato e l’importo del documento è uguale alla somma nota2.
	 *
	 * @return true, if is valido
	 */
	private boolean isValido() {
		String methodName = "isValido";
		
		boolean result = tutteLeQuoteSonoAssociateAdAccertamentoOSubAccertamento().booleanValue() && sommaCongruente.booleanValue();

		log.debug(methodName, "returning: " + result);
		return result;
	}

	
	private Boolean tutteLeQuoteSonoAssociateAdAccertamentoOSubAccertamento(){
		String methodName = "tutteLeQuoteSonoAssociateAdAccertamentoOSubAccertamento";
		
		for (SubdocumentoEntrata quota : doc.getListaSubdocumenti()) {
			if (isQuotaAZero(quota)) {
				continue; // da non considerare
			}
			if (quota.getAccertamento() == null && quota.getSubAccertamento() == null) {
				log.debug(methodName, "Trovato una quota con accertamento nullo. Returning false.");
				return Boolean.FALSE;
			}
		}
		log.debug(methodName, "Tutte le quote hanno accertamento o subAccertamento. Returning true.");
		return Boolean.TRUE;
	}
	
	
	
	
	/*
	 * PARZIALMENTE LIQUIDATO: se non tutte le sue quote ‘valide’ hanno una
	 * liquidazione associata oppure l’importo del documento non è uguale alla
	 * somma nota2
	 */
	/**
	 * Checks if is parzialmente liquidato.
	 *
	 * @return true, if is parzialmente liquidato
	 */
	private boolean isParzialmenteLiquidato() {
		String methodName = "isParzialmenteLiquidato";
		
		for (SubdocumentoEntrata quota : doc.getListaSubdocumenti()) {
			if (isQuotaAZero(quota)) {
				continue; // da non considerare
			}
			if (quota.getAttoAmministrativo() != null 
					&& quota.getAttoAmministrativo().getUid() != 0 
					&& quota.getAttoAmministrativo().getTipoAtto() != null
					&& SiacDAttoAmmTipoEnum.DeterminaDiIncasso.getCodice().equals(quota.getAttoAmministrativo().getTipoAtto().getCodice())) {
				log.debug(methodName, "Ho trovato almeno una quota con determina di incasso, lo stato è PARZIALMENTE LIQUIDATO");
				return true;
			}
		}
		
		log.debug(methodName, "Non ho trovato nessuna quota con determina di incasso, lo stato NON è PARZIALMENTE LIQUIDATO");
		return false;
	}

	/*
	 * LIQUIDATO: se tutte le sue quote hanno una liquidazione associata e
	 * l’importo del documento è uguale alla somma nota2
	 */
	/**
	 * Checks if is liquidato.
	 *
	 * @return true, if is liquidato
	 */
	private boolean isLiquidato() {
		String methodName = "isLiquidato";
		for (SubdocumentoEntrata quota : doc.getListaSubdocumenti()) {
			if (isQuotaAZero(quota)) {
				continue; // da non considerare
			}
			if (quota.getAttoAmministrativo() == null 
					|| quota.getAttoAmministrativo().getUid() == 0 
					|| (quota.getAttoAmministrativo().getTipoAtto()!=null && !(SiacDAttoAmmTipoEnum.DeterminaDiIncasso.getCodice()).equals(quota.getAttoAmministrativo().getTipoAtto().getCodice()))) {
				log.debug(methodName, "Ho trovato almeno una quota senza determina di incasso");
				return false;
				
			}
		}
		return sommaCongruente.booleanValue();
	}

	/**
	 * Checks if is parzialmente emesso.
	 *
	 * @return true, if is parzialmente emesso
	 */
	private boolean isParzialmenteEmesso() {
		String methodName = "isParzialmenteEmesso";
		
		for (SubdocumentoEntrata quota : doc.getListaSubdocumenti()) {
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
		
		for (SubdocumentoEntrata quota : doc.getListaSubdocumenti()) {
			if (isQuotaAZero(quota)) {
				continue; // da non considerare
			}
			if (quota.getOrdinativo() == null || quota.getOrdinativo().getUid() == 0) {
				log.debug(methodName, "Ho trovato almeno una quota senza ordinativo");
				return false;
				
			}
		}
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
	private boolean isQuotaAZero(SubdocumentoEntrata quota) {
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
		
		List<RegistrazioneMovFin> registrazioniMovFinInserite = new ArrayList<RegistrazioneMovFin>();
		for(SubdocumentoEntrata subdoc : doc.getListaSubdocumenti()) {
			//le quote "AZero" (isQuotaAZero==true) NON hanno per forza l'impegno o subImpegno quindi non posso ricavare l'elementoPianoDeiConti!
			ElementoPianoDeiConti elementoPianoDeiConti = null;
			if(subdoc.getAccertamento()!=null && subdoc.getAccertamento().getCapitoloEntrataGestione()!=null) { 
				log.debug(methodName, "Uid capitolo da cui ricavare l'elementoPianoDeiConti: " + subdoc.getAccertamento().getCapitoloEntrataGestione());
				elementoPianoDeiConti = capitoloEntrataGestioneDad.ricercaClassificatoreElementoPianoDeiContiCapitolo(subdoc.getAccertamento().getCapitoloEntrataGestione());
				log.debug(methodName, "trovato elementoPianoDeiConti: "+(elementoPianoDeiConti!=null?elementoPianoDeiConti.getUid():"null"));
			} else {
				log.debug(methodName, "Impossibile reperire l'elementoPianoDeiConti in quanto non ho un capitolo associato all'impegno del documento della quota con uid: "+subdoc.getUid());
			}
			
			RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subdoc , elementoPianoDeiConti);
			registrazioniMovFinInserite.add(registrazioneMovFin);
		}
		
		res.setRegistrazioniMovFinInserite(registrazioniMovFinInserite);
		
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFinInserite);
		
	}
	

	/**
	 * Condizione generale fatture (1)
	 * inserire il dato sul registro quando il tipo documento prevede l'attivazione di gen e quando:
	 * -> tutte le quote finanziarie hanno i movimenti gestione (impegni/accertamenti) associati
	 * -> il totale degli importi delle quote inserite + arrotondamento e' uguale al totale documento
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
		
		Boolean tutteLeQuoteSonoAssociateAImpegnoOSubImpegno = res.getTutteLeQuoteSonoAssociateAdAccertamentoOSubAccertamento(); 
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
	private Boolean isQuoteRilevantiIVAENumeroRegistrazioneCongruenti(DocumentoEntrata ds) {
		String methodName = "isQuoteRilevantiIVAENumeroRegistrazioneCongruenti";
		
		Boolean almenoUnaQuotaRilevanteIva = almenoUnaQuotaRilevanteIVA(ds);
		
		if(almenoUnaQuotaRilevanteIva.booleanValue()){
			for(SubdocumentoEntrata subdoc : ds.getListaSubdocumenti()) {
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
	 * @param ds the documentoEntrata
	 * @return true se presente almeno una quota rilevante IVA
	 */
	private Boolean almenoUnaQuotaRilevanteIVA(DocumentoEntrata ds) {
		for(SubdocumentoEntrata subdoc : ds.getListaSubdocumenti()) {
			Boolean flagRilevanteIva = subdoc.getFlagRilevanteIVA();
			if(Boolean.TRUE.equals(flagRilevanteIva)){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	
	


}

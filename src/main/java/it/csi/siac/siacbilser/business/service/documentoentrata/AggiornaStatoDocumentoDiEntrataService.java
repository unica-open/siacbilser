/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.integration.dad.AccertamentoBilDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;


/**
 * Aggiorna lo stato di un documento entrata.
 * 
 * @version 2.0 (riscritto per migliorare performance) 
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStatoDocumentoDiEntrataService extends CheckedAccountBaseService<AggiornaStatoDocumentoDiEntrata, AggiornaStatoDocumentoDiEntrataResponse> {

	//DADs
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private AccertamentoBilDad accertamentoBilDad;
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired
	private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	@Autowired
	private DocumentoDad documentoDad;
	@Autowired 
	private ImpegnoBilDad impegnoBilDad;
	
	//Services and Components
	@Autowired
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	
	//Fields
	private DocumentoEntrata doc;
	private Bilancio bilancio; 
	private Boolean sommaCongruente = Boolean.FALSE;
	private StatoOperativoDocumento statoAttuale;
	private StatoOperativoDocumento statoNew;
	private Long numeroQuoteSenzaOrdinativo = null;
	private Long numeroQuoteDelDocumento = null;
	private Long numeroQuoteSenzaDeterminaDiIncasso = null;
	private Long numeroQuoteSenzaAccertamentoOSubAccertamento = null;
	private Long numeroQuoteRilevantiIva = null; 
	private Long numeroQuoteRilevantiIvaSenzaNumeroRegistrazione = null;
	private Long numeroDocumentiPadreCollegati = null;
	boolean subdocIvaDocumentoCaricato = false;
	
	private BigDecimal importoTotaleNoteCollegateEntrataNonAnnullate = null;
	
	private List<SubdocumentoEntrata> listaSubdocumenti = null;
	private List<DocumentoEntrata> noteCreditoEntrata = null;
	private List<DocumentoEntrata> noteCreditoEntrataOld = null;
	private List<RegistrazioneMovFin> registrazioniMovFinPrecedentiNcd;
	private Map<Integer, Boolean> cacheNecessarioAggiornamentoRegistrazioneSubdocumento = new HashMap<Integer, Boolean>();
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoEntrata();
		checkEntita(doc, "documento");
		
		bilancio = req.getBilancio();
		
//		salto il check fino a che i servizi di fin no saranno allineati. se non mi viene passato per ora utilizzo il vecchio metodo
//		checkEntita(bilancio, "bilancio");
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoEntrataDad.setLoginOperazione(loginOperazione);
		documentoEntrataDad.flushAndClear();
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
		subdocumentoEntrataDad.flushAndClear(); //in realtà basta già il primo flushAndClear 2 righe sopra!
		
		capitoloEntrataGestioneDad.setEnte(ente);		
		capitoloEntrataGestioneDad.setLoginOperazione(loginOperazione);
		
		sommaCongruente = null;
		statoAttuale = null;
		statoNew = null;
		
		numeroQuoteSenzaOrdinativo = null;
		numeroQuoteDelDocumento = null;
		numeroQuoteSenzaDeterminaDiIncasso = null;
		numeroQuoteSenzaAccertamentoOSubAccertamento = null; 
		numeroQuoteRilevantiIva = null;
		numeroQuoteRilevantiIvaSenzaNumeroRegistrazione = null;
		numeroDocumentiPadreCollegati = null;
		
		importoTotaleNoteCollegateEntrataNonAnnullate = null;
		
		listaSubdocumenti = null;
		noteCreditoEntrata = null;
		noteCreditoEntrataOld = null;
		registrazioniMovFinPrecedentiNcd = null;
		
		subdocIvaDocumentoCaricato = false;
		
		elaborazioniManager.init(ente, loginOperazione);
		
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

		caricaDocumentoEntrata();
		BigDecimal totaleQuoteENoteCreditoMenoImportiDaDedurre = calcolaTotaleQuoteENoteCreditoMenoImportiDaDedurre();
		
		BigDecimal importo = doc.getImporto();
		sommaCongruente = ( importo.compareTo(totaleQuoteENoteCreditoMenoImportiDaDedurre) == 0);

		statoAttuale = doc.getStatoOperativoDocumento(); 
		statoNew = determinaStatoOperativoDocumento();
		
		log.debug(methodName, "statoAttuale: " + statoAttuale + " statoNew: " + statoNew + " sommaCongruente: " + sommaCongruente);

		if (!statoNew.equals(doc.getStatoOperativoDocumento())) {
			documentoEntrataDad.aggiornaStatoDocumentoEntrata(doc.getUid(), statoNew);
			doc.setStatoOperativoDocumento(statoNew);
			doc.setDataInizioValiditaStato(new Date());
			log.info(methodName, "stato aggiornato da: "+doc.getStatoOperativoDocumento()+ " a "+statoNew);
		} else {
			log.info(methodName, "lo stato non è cambiato, non è necessario aggiornarlo.");
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
		
		if(StatoOperativoDocumento.ANNULLATO.equals(statoAttuale)){
			 return StatoOperativoDocumento.ANNULLATO;		
		}
		if(isNotaCreditoAssociataAdUnSoloDocumento()){
			return StatoOperativoDocumento.VALIDO;
		}
		if(isStornato()){
			return StatoOperativoDocumento.STORNATO;
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
	private BigDecimal  calcolaTotaleQuoteENoteCreditoMenoImportiDaDedurre() {
		final String methodName = "calcolaTotaleQuoteENoteCreditoMenoImportiDaDedurre";
		
//		BigDecimal arrotondamento = doc.getArrotondamento().abs();
		BigDecimal arrotondamento = doc.getArrotondamento();
		log.debug(methodName , "arrotondamento: " + arrotondamento);

		BigDecimal importoTotaleSubdoumentiMenoImportoDaDedurre = calcolaImportoTotaleSubdoumentiMenoImportoDaDedurre();
		log.debug(methodName , "importoTotaleSubdoumentiMenoImportoDaDedurre: " + importoTotaleSubdoumentiMenoImportoDaDedurre);
		
		calcolaImportoTotaleNoteCollegateEntrataNonAnnullate();
		log.debug(methodName , "importoTotaleNoteCollegateEntrataNonAnnullate: " + importoTotaleNoteCollegateEntrataNonAnnullate);
		
		
		BigDecimal result = BigDecimal.ZERO;
		log.debug(methodName , "valore iniziale: " + result);
		// TODO: JIRA SIAC-1103
//		result = result.add(arrotondamento);
		result = result.subtract(arrotondamento);
		log.debug(methodName , "valore precedente - arrotondamento: " + result);
		
		result = result.add(importoTotaleSubdoumentiMenoImportoDaDedurre);
		log.debug(methodName , "valore precedente + totale subdocumenti - importo da dedurre: " + result);
		
		result = result.add(importoTotaleNoteCollegateEntrataNonAnnullate);
		log.debug(methodName , "valore precedente + note credito: " + result);
		
		log.debug(methodName, "returning: "+ result);
		return result;

	}
	
	private BigDecimal calcolaImportoTotaleSubdoumentiMenoImportoDaDedurre() {
		return documentoEntrataDad.calcolaImportoTotaleMenoImportoDaDedurreSubdocumenti(doc);
//		return doc.calcolaImportoTotaleSubdoumenti().subtract(doc.calcolaImportoTotaleDaDedurreSobdocumenti()
	}
	
	private BigDecimal calcolaImportoTotaleNoteCollegateEntrataNonAnnullate() {
		if (importoTotaleNoteCollegateEntrataNonAnnullate == null){
			importoTotaleNoteCollegateEntrataNonAnnullate = documentoEntrataDad.calcolaImportoTotaleDaDedurreSuFatturaNoteCollegateEntrataNonAnnullate(doc);
		}
		return importoTotaleNoteCollegateEntrataNonAnnullate;
	}
	
	
	

	/**
	 * Carica dettaglio documento entrata e quote.
	 */
	private void caricaDocumentoEntrata() {
		
		doc = documentoEntrataDad.findDocumentoEntrataById(doc.getUid(), DocumentoEntrataModelDetail.Attr); //Degli attributi mi serve l'arrotondamento!
		
	}
	
	
	

	
	/**
	 * STORNATO: se il suo importo è uguale all’importo dell’eventuale Nota di
	 * credito collegata.
	 *
	 * @return true, if is stornato
	 */
	private boolean isStornato() {
		final String methodName = "isStornato";
		BigDecimal importoNetto = doc.getImportoNetto();
		calcolaImportoTotaleNoteCollegateEntrataNonAnnullate();
		log.debug(methodName, "importo netto documento: "+ importoNetto + " importo totale note: "+importoTotaleNoteCollegateEntrataNonAnnullate);
		return importoNetto.compareTo(importoTotaleNoteCollegateEntrataNonAnnullate) == 0;
	}

	/**
	 * VALIDO: se tutte le sue quote ‘valide’ (nota1) hanno un impegno o
	 * subimpegno associato e l’importo del documento è uguale alla somma nota2.
	 *
	 * @return true, if is valido
	 */
	private boolean isValido() {
		String methodName = "isValido";
		
		boolean result = sommaCongruente.booleanValue() && tutteLeQuoteSonoAssociateAdAccertamentoOSubAccertamento().booleanValue();

		log.debug(methodName, "returning: " + result);
		return result;
	}

	
	private Boolean tutteLeQuoteSonoAssociateAdAccertamentoOSubAccertamento(){
		String methodName = "tutteLeQuoteSonoAssociateAImpegnoOSubImpegno";
		
		countQuoteSenzaAccertamentoOSubAccertamento();
		
		boolean result = Long.valueOf(0).equals(numeroQuoteSenzaAccertamentoOSubAccertamento);
		log.debug(methodName, "returning: " + result);
		return result;
		
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
		
		countQuoteSenzaDeterminaDiIncasso();
		countQuoteEscludendoQuotaAZero();
		
		boolean result =  numeroQuoteSenzaDeterminaDiIncasso.compareTo(Long.valueOf(0))>=0 && 
				numeroQuoteSenzaDeterminaDiIncasso.compareTo(numeroQuoteDelDocumento)<0;
		
		// 1 >= 0 && 1 < 2 -> true  (è parzialmente Liquidato)
		// 0 >= 0 && 0 < 2 -> false (è parzialmente Liquidato. NB. Fosse Liquidato non sarebbe arrivato qui! arriva qui se ha la somma non congruente!)
		// 2 >= 0 && 2 < 2 -> false (è senza neanche una Liquidazione)
		
		log.debug(methodName, "returning: " + result);
		return result;
		
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
		final String methodName = "isLiquidato";

		boolean result =  sommaCongruente.booleanValue() && Long.valueOf(0).equals(countQuoteSenzaDeterminaDiIncasso());
		log.debug(methodName, "returning: " + result);
		return result;
		
	}

	/**
	 * Checks if is parzialmente emesso.
	 *
	 * @return true, if is parzialmente emesso
	 */
	private boolean isParzialmenteEmesso() {
		final String methodName = "isParzialmenteEmesso";
		
		countQuoteSenzaOrdinativo();
		countQuoteEscludendoQuotaAZero();
		
		boolean result =  numeroQuoteSenzaOrdinativo.compareTo(Long.valueOf(0))>=0 && ////0 incluso perchè possono essere emesse anche tutte le quote! ma se la somma è incongruente allora è parzialmente emesso
				numeroQuoteSenzaOrdinativo.compareTo(numeroQuoteDelDocumento)<0; 
		//2>=2 && 2<2
		//0>=2 && 0<2
		log.debug(methodName, "returning: " + result);
		return result;
		
	}

	/**
	 * Checks if is emesso.
	 *
	 * @return true, if is emesso
	 */
	private boolean isEmesso() {
		final String methodName = "isEmesso";
		
		boolean result = sommaCongruente.booleanValue() && Long.valueOf(0).equals(countQuoteSenzaOrdinativo());
		log.debug(methodName, "returning: " + result);
		return result;
		
	}
	
	
	/**
	 * Verifica se la nota di credito e' associata ad uno e uno solo documento e non e' un documento a se' stante
	 * @return true se la nota di credito e' associata ad uno e uno solo documento e non e' un documento a se' stante 
	 */
	private boolean isNotaCreditoAssociataAdUnSoloDocumento() {
		final String methodName = "isNotaCreditoAssociataAdUnSoloDocumento";
		
		boolean result = false;
		if(doc.getTipoDocumento().isNotaCredito()) {
			countDocumentiPadreCollegati();
			log.debug(methodName, "numeroDocumentiPadreCollegati: " + numeroDocumentiPadreCollegati);
			result = Long.valueOf(1).compareTo(numeroDocumentiPadreCollegati) == 0; //true solo se "esattamente" 1.
		}
		log.debug(methodName, "returning: " + result);
		return result;
	}

	/*
	 * nota 1 : nel calcolo dello stato non bisogna considerare le quote a
	 * ‘zero’, ossia quelle che sono eventualmente collegate ad una nota credito
	 * per un importo da dedurre per quella quota=importo della quota stessa.
	 *
	 * @param quota the quota
	 * @return true, if is quota a zero
	 */
	/*private boolean isQuotaAZero(SubdocumentoEntrata quota) {
		return quota.getImporto().compareTo(quota.getImportoDaDedurre()) == 0;
	}*/
	
	
	
	
	
	
	private Long countQuoteSenzaOrdinativo() {
		if(numeroQuoteSenzaOrdinativo==null){
			numeroQuoteSenzaOrdinativo = documentoEntrataDad.countQuoteSenzaOrdinativo(doc);
		}
		return numeroQuoteSenzaOrdinativo;
	}
	
	private Long countQuoteEscludendoQuotaAZero() {
		if(numeroQuoteDelDocumento==null){
			numeroQuoteDelDocumento = documentoEntrataDad.countQuoteEscludendoQuotaAZero(doc);
		}
		return numeroQuoteDelDocumento;
	}
	
	private Long countQuoteSenzaDeterminaDiIncasso() { //speculare al countQuoteSenzaLiquidazione per le spese.
		if(numeroQuoteSenzaDeterminaDiIncasso==null) {
			numeroQuoteSenzaDeterminaDiIncasso = documentoEntrataDad.countQuoteSenzaDeterminaDiIncasso(doc);
		}
		return numeroQuoteSenzaDeterminaDiIncasso;
	}
	
	private Long countQuoteSenzaAccertamentoOSubAccertamento() {
		if(numeroQuoteSenzaAccertamentoOSubAccertamento==null){
			numeroQuoteSenzaAccertamentoOSubAccertamento = documentoEntrataDad.countQuoteSenzaAccertamentoOSubAccertamento(doc);
		}
		return numeroQuoteSenzaAccertamentoOSubAccertamento;
	}
	
	private Long countQuoteRilevantiIva() {
		if(numeroQuoteRilevantiIva==null) {
			numeroQuoteRilevantiIva = documentoEntrataDad.countQuoteRilevantiIva(doc);
		}
		return numeroQuoteRilevantiIva;
	}
	
	private Long countQuoteRilevantiIvaSenzaNumeroRegistrazione() {
		if(numeroQuoteRilevantiIvaSenzaNumeroRegistrazione==null) {
			numeroQuoteRilevantiIvaSenzaNumeroRegistrazione = documentoEntrataDad.countQuoteRilevantiIvaSenzaNumeroRegistrazione(doc);
		}
		return numeroQuoteRilevantiIvaSenzaNumeroRegistrazione;
	}
	
	private Long countDocumentiPadreCollegati() {
		if(numeroDocumentiPadreCollegati==null) {
			numeroDocumentiPadreCollegati = documentoEntrataDad.countDocumentiPadreCollegati(doc);
		}
		return numeroDocumentiPadreCollegati;
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
		
		if(!Boolean.TRUE.equals(flagAttivaGEN)) {
			log.debug(methodName, "Documento [uid:"+doc.getUid()+"] senza Flag Attiva GEN");
			return;
		}
		
		if(!Boolean.TRUE.equals(doc.getContabilizzaGenPcc())) {
			log.debug(methodName, "Documento [uid:"+doc.getUid()+"] senza Flag Contabilizza GEN PCC");
			return;
		}
		
		if(isNotaCreditoAssociataAdUnSoloDocumento()){
			log.debug(methodName, "Il documento che si sta aggiornando e' una nota credito associata ad uno e un solo documento. La registrazione, "
					+ "in questo caso, viene gestita quando viene invocato l'AggiornaStatoDocumento per il documento padre di appartenenza.");
			//Questo implica che non venendo mai attivato GEN non verra' nemmeno attivato PCC 
			//(che e' attivabile solo se e' attivato GEN [CondizioneDiAttivazioneGENSoddisfatta==TRUE])
			return;
		}
		if(bilancio == null || bilancio.getUid() == 0){
			bilancio = documentoEntrataDad.findBilancioAssociatoAlDocumento(doc.getUid());
		}
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, bilancio /*doc.getAnno()*/);
		
		
		List<RegistrazioneMovFin> registrazioniPrecedenti = ricercaRegistrazioniMovFinAssociataAlMovimento();
		startElaborazioneRegistrazioni(registrazioniPrecedenti);
		try {
			//Annullo le Registrazioni relative solo ai subdocumenti che sono stati modificati e che quindi non sono piu' valide.
			//a prescindere da fatto che le condizioni di attivazione GEN siano di nuovo soddisfatte o meno.
			//boolean esistonoRegistrazioniPrecedenti = gestisciAnnullamentoRegistrazioniGENEPrimeNote();
			gestisciAnnullamentoRegistrazioniGENEPrimeNote(registrazioniPrecedenti);
			
			
			boolean isCondizioneDiAttivazioneGENSoddisfatta = isCondizioneDiAttivazioneGENSoddisfatta();
			if(!isCondizioneDiAttivazioneGENSoddisfatta) {
				log.debug(methodName, "condizione di attivazione non soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
				res.setCondizioneDiAttivazioneGENSoddisfatta(Boolean.FALSE);
				return;
			}
			res.setCondizioneDiAttivazioneGENSoddisfatta(Boolean.TRUE);
			log.info(methodName, "Condizione di attivazione soddisfatta per il Documento [uid:"+doc.getUid()+"]. Vengono inserite/aggiornate le Registrazioni.");
			
			inserisciRegistrazioniGEN(registrazioniPrecedenti);
			
			inserisciPrimeNoteAutomaticheAsync();
		} finally {
			//in realta' questa endElaborazioneRegistrazioni sarebbe meglio eseguirla al commit del servizio!
			endElaborazioneRegistrazioni(registrazioniPrecedenti);
		}
	}

	private void inserisciPrimeNoteAutomaticheAsync() {
		
		final String methodName= "inserisciPrimeNoteAutomaticheAsync";
		boolean isAbilitatoInsPrimaNotaDaFinanziaria = "TRUE".equals(ente.getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_PNOTA_DA_FIN));
		log.debug(methodName, "utente abilitato a gestione della prima nota da finanziaria? " + isAbilitatoInsPrimaNotaDaFinanziaria);
		if(Boolean.TRUE.equals(req.getSaltaInserimentoPrimaNota()) && isAbilitatoInsPrimaNotaDaFinanziaria) {
			//salto l'inserimento della prima nota
			log.debug(methodName, "salto l'inserimento automatico della prima nota: dovra' essere poi gestito manualmente.");
			//TODO: valutare se impostare qualcosa nella response
			return;
		}
		
		String msg = "L'elaborazione di alcune prime note afferenti al documento "+doc.getDescAnnoNumeroUidTipoDocUidSoggettoStato()+" e' ancora in corso. Attendere il termine dell'elaborazione";
		Errore errore = ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg);
		//creo le chiavi per l'ambito FIN
		ElaborazioniAttiveKeyHandler eakhFIN = new ElaborazioniAttiveKeyHandler(doc.getUid(),AggiornaStatoDocumentoDiSpesaService.class,doc.getClass(), Ambito.AMBITO_FIN.name());
		String elabServiceFIN    = eakhFIN.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA);
		String elabKeyFIN        = eakhFIN.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA);
		String elabServiceFINNCD = eakhFIN.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA_NCD);
		String elabKeyFINNCD     = eakhFIN.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA_NCD);
		
		//creo le chiavi per l'ambito GSA
		ElaborazioniAttiveKeyHandler eakhGSA = new ElaborazioniAttiveKeyHandler(doc.getUid(),AggiornaStatoDocumentoDiSpesaService.class,doc.getClass(), Ambito.AMBITO_GSA.name());
		String elabServiceGSA    = eakhGSA.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA);
		String elabKeyGSA        = eakhGSA.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA);
		String elabServiceGSANCD = eakhGSA.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA_NCD);
		String elabKeyGSANCD     = eakhGSA.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA_NCD);

		
		//String group = "elabPrimaNota.movId:"+doc.getUid()+",type:"+DocumentoEntrata.class.getSimpleName();
		
		
		//NB: l'avvio di queste operazioni Async e' differito di 30 sec.
		//Bisogna avere ragionevole certezza che la tx corrente termini (con commit o anche rollback) entro 10 sec a partire da quando sono invocate.
		//Ragion per cui sono da mantenere più in "fondo" possibile del metodo execute() di questo servizio. Qui dovrebe andar bene.
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(res.getRegistrazioniMovFinPrimaNota(), elabServiceFIN, errore, elabKeyFIN);
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(res.getRegistrazioniMovGSAPrimaNota(), elabServiceGSA, errore, elabKeyGSA);
		
		//Inserisce una primaNota per le quote con importo da dedurre>0 del DocumentoEntrata
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(res.getRegistrazioniMovFinNNcdInserite(), elabServiceFINNCD, errore, elabKeyFINNCD);
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(res.getRegistrazioniMovFinNcdGSAInserite(), elabServiceGSANCD, errore, elabKeyGSANCD);

	}

	
	private List<RegistrazioneMovFin> gestisciAnnullamentoRegistrazioniGENEPrimeNote(List<RegistrazioneMovFin> registrazioniMovFinPrecedenti) {
		final String methodName = "gestisciAnnullamentoRegistrazioniGENEPrimeNote";
		
		StatoOperativoPrimaNota statoAttualePrimaNota = findStatoAttualePrimaNota(registrazioniMovFinPrecedenti);
		res.setPrimaNotaPrecedenteAnnullata(StatoOperativoPrimaNota.ANNULLATO.equals(statoAttualePrimaNota));
		
		annullaRegisrazioniPrecedenti(registrazioniMovFinPrecedenti);
		
		log.debug(methodName, "esistonoRegistrazioniPrecedenti: "+!registrazioniMovFinPrecedenti.isEmpty());
		return registrazioniMovFinPrecedenti;
	}

	private List<RegistrazioneMovFin> ricercaRegistrazioniMovFinAssociataAlMovimento() {
		//NB questo trova anche le registrazioni GSA, CEC, ecc...
		List<RegistrazioneMovFin> registrazioniMovFinPrecedenti = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.SUBDOCUMENTO_ENTRATA, doc); //se presenti ne troverà una per ogni quota, altrimenti 0.
		return registrazioniMovFinPrecedenti;
	}
	
	private StatoOperativoPrimaNota findStatoAttualePrimaNota(List<RegistrazioneMovFin> registrazioniMovFinPrecedenti) {
		for(RegistrazioneMovFin registrazioneMovFinPrecedente: registrazioniMovFinPrecedenti){
			for(MovimentoEP movimentoEP : registrazioneMovFinPrecedente.getListaMovimentiEP()){
				//Ne avrà solo uno di MovimentoEP.
				PrimaNota primaNota = movimentoEP.getPrimaNota();
				return primaNota.getStatoOperativoPrimaNota();
			}
		}
		return null;
	}
	
	
	private void annullaRegisrazioniPrecedenti(List<RegistrazioneMovFin> registrazioniMovFinPrecedenti) {
		final String methodName = "annullaRegisrazioniPrecedenti";
		
		for(RegistrazioneMovFin registrazionePrecedente : registrazioniMovFinPrecedenti) {
			if(!(registrazionePrecedente.getMovimento() instanceof SubdocumentoEntrata)){
				log.warn(methodName, "La registrazione [uid:"+registrazionePrecedente.getMovimento().getUid()+"] ha tipoCollegamento "
						+ "errato per il  SubdocumentoEntrata [uid:"+registrazionePrecedente.getMovimento().getUid()+"]. Verranno saltate. Occorre bonificare DB. ");
				continue;//se succede è perchè ci sono delle registrazioni con tipoCollegamento errate associate a questo documento.
			}
			SubdocumentoEntrata subdoc = (SubdocumentoEntrata)registrazionePrecedente.getMovimento() /*SiacTSubdoc_SubdocumentoEntrata_Base*/;
			if(isNecessarioAggiornamentoRegistrazioneSubdocumento(subdoc) /*&& isQuotaNonPagata(subdoc)*/) {
				
				log.info(methodName, "Il Subdocumento [uid:"+registrazionePrecedente.getMovimento().getUid()+"] presenta modifiche che rendono necessario "
						+ "l'aggiornamento della registrazione, pertanto annullo la registrazione e la prima nota se presente. "
						+ "Se le condizioni di attivazione saranno nuovamente soddisfatte "
						+ "verra' reinserita la registrazione con l'eventuale prima nota. ");
				
				registrazioneGENServiceHelper.annullaRegistrazioneMovFinEPrimaNota(registrazionePrecedente, registrazioniMovFinPrecedenti); //se la registrazione esisteva devo annullare le eventuali primeNote associate
				
				gestisciAnnullamentoRegistrazioneNotaCreditoAssociata(subdoc);
			}
		}
		
	}
	
	
	private void gestisciAnnullamentoRegistrazioneNotaCreditoAssociata(SubdocumentoEntrata subdoc) {
		final String methodName = "gestisciAnnullamentoRegistrazioneNotaCreditoAssociata";
		
		BigDecimal importoDaDedurreOld = subdocumentoEntrataDad.findImportoDaDedurreSubdocumentoEntrataByIdTxRequiresNew(subdoc.getUid());
		
		if(importoDaDedurreOld == null || BigDecimal.ZERO.compareTo(importoDaDedurreOld)==0){
			log.debug(methodName, "La quota [uid:"+subdoc.getUid()+"] non aveva un importo da dedurre e di conseguenza non aveva registrazioni per la nota credito. Esco.");
			return;
		}
		
		log.debug(methodName, "La quota [uid:"+subdoc.getUid()+"] aveva un importo da dedurre pari a "+importoDaDedurreOld+".. cerco di annullare la relativa registrazione della nota credito.");
		
		List<DocumentoEntrata> noteCreditoPrecedenti = getListaNoteCreditoEntrataCollegateEsclusivamenteAlDocumentoOld();
		
		if(noteCreditoPrecedenti.isEmpty()){
			log.warn(methodName, "Il documento [uid:"+doc.getUid()+"] non aveva note credito entrata precedenti da annullare pur avendo un "
					+ "importo da dedurre diverso da zero. Verificare coerenza dati su DB.");
			return;//Me ne aspetto una soltatnto.
		}
		
		if(noteCreditoPrecedenti.size()!=1){
			log.warn(methodName, "Il documento [uid:"+doc.getUid()+"] ha piu' di una nota credito collegata. Non lo gestiamo.");
			return;//Me ne aspetto una soltatnto.
		}
		
		log.debug(methodName, "Il documento [uid:"+doc.getUid()+"] ha esattamente una e una sola nota credito. Ricerco le registrazioni associate.");
		//Il documento ha esattamente una e una sola nota credito.
		DocumentoEntrata notaCreditoPrecedente = noteCreditoPrecedenti.get(0);
		
		//ricerco le regisrazioni della nota credito precedente per trovare quella con importo pari all'importo da dedurre della quota che sto analizzando.
		ricercaRegistrazioniMovFinAssociataAllaNotaCreditoPrecedente(notaCreditoPrecedente); //se presenti ne troverà una per ogni quota, altrimenti 0.
		for(RegistrazioneMovFin registrazioneMovFinPrecedenteNcd : registrazioniMovFinPrecedentiNcd){
			
			if(isRegistrazioneMovFinNotaCreditoRelativaASubdoc(registrazioneMovFinPrecedenteNcd, subdoc)) {
				
				log.debug(methodName, "Trovata la registrazione [uid:"+registrazioneMovFinPrecedenteNcd.getUid()+"] "
						+ " associata al mio subdocumento [uid:"+subdoc.getUid()+"]; la annullo.");
				
				registrazioneGENServiceHelper.annullaRegistrazioneMovFinEPrimaNota(registrazioneMovFinPrecedenteNcd, new ArrayList<RegistrazioneMovFin>());
				break; //annullo la prima registrazione nota credito precedente che ha lo stesso importoDaDedurre che era presente sul subdocumento.
			}
		}
			
			
			
	}
	
	/**
	 * Valuta se la registrazioneMovFinPrecedenteNcd della nota credito appartiene al subdoc passato come parametro
	 * 
	 * @param notaCreditoRegistrazionePrecedente
	 * @param subdoc
	 * @return
	 */
	private boolean isRegistrazioneMovFinNotaCreditoRelativaASubdoc(RegistrazioneMovFin registrazioneMovFinPrecedenteNcd, SubdocumentoEntrata subdoc) {
		final String methodName = "isRegistrazioneMovFinNotaCreditoRelativaASubdoc";
		//TODO devo trovare un modo di filtrare la registrazione nota credito relativa alla mia quota!! 
		
		SubdocumentoEntrata subdocCollegatoAllaRegistrazione = (SubdocumentoEntrata)registrazioneMovFinPrecedenteNcd.getMovimentoCollegato();
		boolean result = subdocCollegatoAllaRegistrazione!=null && subdocCollegatoAllaRegistrazione.getUid() == subdoc.getUid();
		
		log.debug(methodName, "returning: "+result);
		return result;
	}
	
	private List<RegistrazioneMovFin> ricercaRegistrazioniMovFinAssociataAllaNotaCreditoPrecedente(DocumentoEntrata notaCreditoPrecedente) {
		if(registrazioniMovFinPrecedentiNcd==null){
			registrazioniMovFinPrecedentiNcd = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimentoTxRequiresNew(TipoCollegamento.SUBDOCUMENTO_ENTRATA, notaCreditoPrecedente);
		}
		return registrazioniMovFinPrecedentiNcd;
	}
	
	
	private void inserisciRegistrazioniGEN(List<RegistrazioneMovFin> registrazioniPrecedenti) {
		final String methodName = "inserisciRegistrazioniGEN";
		
		boolean esistonoRegistrazioniPrecedenti =  !registrazioniPrecedenti.isEmpty();
		
		List<RegistrazioneMovFin> registrazioniMovFINInserite = new ArrayList<RegistrazioneMovFin>();
		List<RegistrazioneMovFin> registrazioniMovGSAInserite = new ArrayList<RegistrazioneMovFin>();
		
		List<RegistrazioneMovFin> registrazioniMovFINPrecedenti = new ArrayList<RegistrazioneMovFin>();
		List<RegistrazioneMovFin> registrazioniMovGSAPrecedenti = new ArrayList<RegistrazioneMovFin>();
		
		for(SubdocumentoEntrata subdoc : getListaSubdocumenti()) {
			
			//SIAC-3988 (punto 2)
			//Imposta il flagConvalidaManuale al default se non e' gia' valorizzato.
			if(subdoc.getFlagConvalidaManuale()==null) { 
				//workaround per subdoc che sono arrivati senza il flagConvalidaManuale impostato.
				subdoc.setFlagConvalidaManuale(getDefaultFlagConvalidaManuale());
				subdocumentoEntrataDad.aggiornaFlagConvalidaManuale(subdoc);
			}
			
			if(isQuotaPagata(subdoc)){
				/*
				 * L'inserimento di una registrazione e l'annullamento di una registrazione 
				 * di una quota nel registro deve avvenire solamente se la quota 
				 * NON ha un ordinativo associato NON ANNULLATO e se NON ha 
				 * il flagPagatoCEC=TRUE (sostanzialmente NON e' pagata), altrimenti si prosegue con la 
				 * verifica delle altre quote;
				 * CR SIAC-2847,SIAC-3006
				 */
				log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " risulta essere gia' pagato. "
						+ "La registrazione verra' saltata. ");
				continue;
			}
			
			//SIAC-4504 - Richiesta di eliminare il controllo inserito con la CR SIAC-2847
//			if(Boolean.TRUE.equals(doc.getFlagDisabilitaRegistrazioneResidui()) && isQuotaConMovimentoResiduo(subdoc)){
//				/* La registrazione di quote afferenti ad accertamenti con anno precedente all'anno di bilancio del documento
//				 * vengono saltate se flag disabilita registrazione residui e' true.
//				 * CR SIAC-2847
//				 */
//				log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " e' afferente ad un movimento residuo ed "
//						+ "il flagDisabilitaRegistrazioneResidui e' abilitato. La registrazione verra' saltata. ");
//				continue;
//			}
			
			RegistrazioneMovFin registrazionePrecedente = cercaRegistrazionePrecedenteNonAnnullata(subdoc, registrazioniPrecedenti, Ambito.AMBITO_FIN);
			if(registrazionePrecedente!=null){
				log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " ha gia' una registrazione associata NON annullata. "
						+ "Non verra' pertanto creata una ulteriore registrazione.");
				registrazioniMovFINPrecedenti.add(registrazionePrecedente);
				//a questo punto esistera' anche una registrazione per la notaCredito se presente. Avra' gia' la sua prima nota che non modifico.
				
				//Se esiste la registrazione FIN potrebbe esistere quella GSA
				RegistrazioneMovFin registrazionePrecedenteGSA = cercaRegistrazionePrecedenteNonAnnullata(subdoc, registrazioniPrecedenti, Ambito.AMBITO_GSA);
				if(registrazionePrecedenteGSA!=null){
					log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " ha gia' una registrazione associata NON annullata per GSA. "
							+ "Non verra' pertanto creata una ulteriore registrazione per GSA.");
					registrazioniMovGSAPrecedenti.add(registrazionePrecedenteGSA);
				}
				
				continue;
			}
				
			if(esisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota(subdoc)){
				log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " ha gia' un Accertamento/SubAccertamento associato ad una registrazione non annullata. "
						+ "Non verra' pertanto creata una ulteriore registrazione.");
				continue;
			}
			
			
			if(esisteUnaRegistrazioneNonAnnullataPerQuotaAnnoPrecedente(subdoc)){
				log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " ha gia' una registrazione nell'anno precedente non annullata. "
						+ "Non verra' pertanto creata una ulteriore registrazione.");
				continue;//questo evita di fare inserire anche le registrazioni per GSA e CEC.
			}
			
			
			
			ElementoPianoDeiConti elementoPianoDeiConti = determinaElementoPianoDeiConti(subdoc);
			
			//Carico i dati Iva necessari a determinare l'evento. SIAC-2847
			boolean isConIva = Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA()); //isConIva(subdoc.getFlagRilevanteIVA(), m); /*almenoUnaQuotaRilevanteIVA(doc)*/
			boolean isTipoIvaPromisqua = false; //isTipoIvaPromisqua(m);
			
			
			//L'evento e' determinato a livello di quota. Vedi SIAC-2422.
			Evento evento = registrazioneGENServiceHelper.determinaEvento(TipoCollegamento.SUBDOCUMENTO_ENTRATA, 
																			esistonoRegistrazioniPrecedenti, 
																			isConIva,//Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA()), /*almenoUnaQuotaRilevanteIVA(doc)*/
																			isTipoIvaPromisqua,
																			doc.getTipoDocumento().isNotaCredito(), false);
		
			
			//########### FIN ###########
			RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subdoc , elementoPianoDeiConti, Ambito.AMBITO_FIN);
			registrazioniMovFINInserite.add(registrazioneMovFin);
			
			gestisciRegistrazioniGENNoteCreditoEntrataCollegate(esistonoRegistrazioniPrecedenti, subdoc, elementoPianoDeiConti, isConIva, isTipoIvaPromisqua);
			
			//########### GSA ###########
			if(isDaRegistrareInGSA(subdoc)){
				log.debug(methodName, "Inserisco anche la registrazione per GSA relativa alla quota con uid: "+subdoc.getUid());
				
				RegistrazioneMovFin registrazioneMovGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subdoc , elementoPianoDeiConti, Ambito.AMBITO_GSA);
				registrazioniMovGSAInserite.add(registrazioneMovGSA);
			}
			
			
		}
		
		//Se avviassi qui l'inserimento delle primeNote rischio che partano prima che la transazione abbia committato! e quindi non troverebbero la registrazione.
		
		res.setRegistrazioniMovFinInserite(registrazioniMovFINInserite);
		res.setRegistrazioniMovGSAInserite(registrazioniMovGSAInserite);
		
		res.setRegistrazioniMovFinPrecedenti(registrazioniMovFINPrecedenti);
		res.setRegistrazioniMovGSAPrecedenti(registrazioniMovGSAPrecedenti);
	}

	
	private boolean esisteUnaRegistrazioneNonAnnullataPerQuotaAnnoPrecedente(SubdocumentoEntrata subdoc) { 
		return doc.getAnno() < bilancio.getAnno() && ! CollectionUtils.isEmpty(
				registrazioneGENServiceHelper.ricercaRegistrazioniMovFinBilancioPrecendenteAssociateAlMovimento(TipoCollegamento.SUBDOCUMENTO_ENTRATA, doc));
	}


	
	private RegistrazioneMovFin cercaRegistrazionePrecedenteNonAnnullata(SubdocumentoEntrata subdoc, List<RegistrazioneMovFin> registrazioniPrecedenti, Ambito ambito) {
		String methodName = "cercaRegistrazionePrecedenteNonAnnullata";
		for(RegistrazioneMovFin reg : registrazioniPrecedenti){
			if(!reg.getStatoOperativoRegistrazioneMovFin().equals(StatoOperativoRegistrazioneMovFin.ANNULLATO) //Stato non annullato
					&& reg.getMovimento().getUid() == subdoc.getUid() //E stesso uid del subdoc.
					&& ambito.equals(reg.getAmbito())
					){
				log.debug(methodName, "Trovata registrazione precedente per subdoc [uid:" + subdoc.getUid() + "] con stato: "
								+ reg.getStatoOperativoRegistrazioneMovFin() + " Returning true.");
				//return true;
				return reg;
			}
		}
		log.debug(methodName, "Nessura registrazione precedente per subdoc [uid:"+subdoc.getUid()+"] con stato diverso da ANNULLATO. Returning false.");
		//return false;
		return null;
	}
	
	private void gestisciRegistrazioniGENNoteCreditoEntrataCollegate(boolean esistonoRegistrazioniPrecedenti, SubdocumentoEntrata subdoc, ElementoPianoDeiConti elementoPianoDeiConti, boolean isConIva, boolean isTipoIvaPromisqua) {
		final String methodName = "gestisciRegistrazioniGENNoteCreditoEntrataCollegate";
		
		if(BigDecimal.ZERO.compareTo(subdoc.getImportoDaDedurreNotNull()) == 0) {
			log.info(methodName, "Il Subdocumento [uid:"+subdoc.getUid()+"] NON presenta importi da dedurre. Non verra inserita nessuna Registrazione NotaCredito.");
			return;
		}
		
		log.info(methodName, "Il Subdocumento [uid:"+subdoc.getUid()+"] presenta importi da dedurre. Gestisco la registrazione della notaCredito.");
		
		getListaNoteCreditoEntrataCollegateEsclusivamenteAlDocumento();
		
		if(noteCreditoEntrata.isEmpty()){
			log.info(methodName, "Il Documento [uid:"+doc.getUid()+"] non ha nessuna nota credito o ha note di credito che sono collegate anche ad altri documenti. In questo caso la gestione delle Registrazioni delle note viene saltata. ");
			return;
		}
		
		if(noteCreditoEntrata.size()>1) {
			log.info(methodName, "Il Documento [uid:"+doc.getUid()+"] presenta piu' di una nota credito collegata. In questo caso la gestione delle Registrazioni delle note viene saltata. ");
			return;
		}
		
		//Arrivato qui ho solo ed esattamente una nota credito.
		DocumentoEntrata notaCreditoEntrata = noteCreditoEntrata.get(0);
		documentoDad.aggiornaFlagContabilizzaGenPcc(notaCreditoEntrata, Boolean.TRUE);
		caricaSubdocumentiDellaNotaCredito(notaCreditoEntrata);
		
		if(notaCreditoEntrata.getListaSubdocumenti().size()!=1){
			log.info(methodName, "Il Documento [uid:"+doc.getUid()+"] presenta una nota credito collegata con piu' di una quota. In questo caso la gestione delle Registrazioni delle note viene saltata. ");
			return;
		}
		
		SubdocumentoEntrata subdocumentoEntrataNotaCredito = notaCreditoEntrata.getListaSubdocumenti().get(0); //e' sempre uno solo! checked da #caricaSubdocumentoDellaNotaCredito
		
		//L'aggiornametno e' per forza necessario perchè segue contestualmente quello del subdoc associato.
		
		if(!esisteNotaCreditoIvaSeAbbinatoAQuotaRilevanteIva(subdocumentoEntrataNotaCredito, subdoc)){
			log.info(methodName, "Il Subdocumento NotaCredito [uid:"+subdocumentoEntrataNotaCredito.getUid()+"] NON ha una corrispondente NotaCreditoIva. "
					+ "Le Registrazione NON verra' inserita. ");
			return;
		}
		
		Evento evento = registrazioneGENServiceHelper.determinaEventoNotaCredito(TipoCollegamento.SUBDOCUMENTO_ENTRATA, esistonoRegistrazioniPrecedenti, isConIva, isTipoIvaPromisqua, false /*gli eventi sulle entrate non sono differenziati per residuo*/ );
		
		RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subdocumentoEntrataNotaCredito, subdoc, elementoPianoDeiConti, Ambito.AMBITO_FIN);
		log.info(methodName, "Inserita registrazione per il Subdocumento NotaCredito [uid:"+subdocumentoEntrataNotaCredito.getUid()+"]. ");
		res.getRegistrazioniMovFinNNcdInserite().add(registrazioneMovFin);
		
		//########### GSA ########### //SIAC-4298
		if(isDaRegistrareInGSA(subdoc)){
			log.debug(methodName, "Inserisco anche la registrazione per GSA per la NCD relativa  alla nota di credito [uid:"+subdocumentoEntrataNotaCredito.getUid()+"] relativa alla quota con uid: "+subdoc.getUid() + " con evento: "+evento.getCodice());
			RegistrazioneMovFin registrazioneMovFinNcdGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subdocumentoEntrataNotaCredito, subdoc, elementoPianoDeiConti, Ambito.AMBITO_GSA);
			res.getRegistrazioniMovFinNcdGSAInserite().add(registrazioneMovFinNcdGSA);
		}
	}

	/**
	 * Controlla che se uno degli importi da dedurre e' legato ad una o + quote rilevanti iva deve esistere anche la nota di credito iva abbinata.
	 * 
	 * @param subdocNotaCreditoEntrata
	 * @param subdoc 
	 * @return
	 */
	private boolean esisteNotaCreditoIvaSeAbbinatoAQuotaRilevanteIva(SubdocumentoEntrata subdocNotaCreditoEntrata, SubdocumentoEntrata subdoc) {
		if(!Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA())){
			return true; //NON e' rilevante IVA: Non controllo esistenza NotaCreditoIva.
		}
		
		//TODO controllare che non sia gia' implicitamente soddisfatta.
		SubdocumentoIvaEntrata notaCreditoIva = subdocumentoEntrataDad.findNotaCreditoIvaAssociataUid(subdoc);
		if(notaCreditoIva != null) {
			return true;
		}

		// SIAC-3559: controllo anche se ho dei dati inva collegati direttamente alla nota di credito
		Long subdocIvaLegatiNCD = subdocumentoEntrataDad.countSubdocIvaCollegati(subdocNotaCreditoEntrata);
		return subdocIvaLegatiNCD != null && subdocIvaLegatiNCD.longValue() > 0L;
//		return true;
	}

	private void caricaSubdocumentiDellaNotaCredito(DocumentoEntrata notaCreditoEntrata) {
		
		if(!notaCreditoEntrata.getListaSubdocumenti().isEmpty()){
			//subdocumento (e' uno solo) gia' caricato.
			return;
		}
		
		//Subdocumento della notaCredito non caricato (e'uno solo). Lo carico da DB.
		List<SubdocumentoEntrata> listaSubdocumentiNcd = subdocumentoEntrataDad.findSubdocumentiEntrataByIdDocumento(notaCreditoEntrata.getUid(), 
				SubdocumentoEntrataModelDetail.AccertamentoSubaccertamento,	//Serve sia alla registrazione GEN che PCC
				SubdocumentoEntrataModelDetail.Attr//,				//Serve sia alla registrazione GEN che PCC
				//SubdocumentoEntrataModelDetail.Liquidazione 		//Serve solo alla registrazione PCC
				);
		notaCreditoEntrata.setListaSubdocumenti(listaSubdocumentiNcd);
		
	}
	
	/**
	 * Ottiene la lista delle  note di credito di entrata appartenenti esclusivamente al mio doc.
	 * 
	 * @return la lista
	 */
	private List<DocumentoEntrata> getListaNoteCreditoEntrataCollegateEsclusivamenteAlDocumento() {
		if(noteCreditoEntrata == null){
			noteCreditoEntrata = documentoEntrataDad.ricercaNoteCreditoEntrataCollegateEsclusivamenteAlDocumento(doc.getUid()); //TODO Specificare i ModelDetail che servono!
		}
		return noteCreditoEntrata;
	}
	
	/**
	 *  Ottiene la lista delle  note di credito di entrata appartenenti esclusivamente al mio doc.
	 *  
	 * @return la lista
	 */
	private List<DocumentoEntrata> getListaNoteCreditoEntrataCollegateEsclusivamenteAlDocumentoOld() {
		if(noteCreditoEntrataOld == null) {
			noteCreditoEntrataOld = documentoEntrataDad.ricercaNoteCreditoEntrataCollegateEsclusivamenteAlDocumentoTxRequiresNew(doc.getUid()); //TODO Specificare i ModelDetail che servono!
		}
		return noteCreditoEntrataOld;
	}
	
	/**
	 * Le Registrazioni sono da aggiornare solo se si verifica la modifica ad uno dei seguenti dati:
	 * <ul>
	 *  <li>Importo della quota;</li>
	 *  <li>Importo da dedurre della quota;</li>
	 *  <li>Movimento associato alla quota (accertamento-subaccertamento);</li>
	 *  <li>Flag rilevante Iva.</li>
	 * </ul>
	 *
	 * @param subdoc the subdoc
	 * @return se la registrazione necessita di aggiornamento.
	 */
	private boolean isNecessarioAggiornamentoRegistrazioneSubdocumento(SubdocumentoEntrata subdoc) {
		if(!cacheNecessarioAggiornamentoRegistrazioneSubdocumento.containsKey(subdoc.getUid())) {
			
			//Leggo il subdocumento in una nuova transazione. Soluzione NON ideale in caso di concorrenza! Ma accetto il side effect di un doppio aggiornamento 
			//nel caso in cui ho due thread sovrapposti.
			String keySubdocPrecedente = subdocumentoEntrataDad.computeKeySubdocImportoAccertamentoFlagRilevanteIvaTxRequiresNew(subdoc.getUid()); //in new tx
			String keySubdocAttuale = subdocumentoEntrataDad.computeKeySubdocImportoAccertamentoFlagRilevanteIva(subdoc.getUid()); //in this tx
			
			
			boolean necessarioAggiornamentoRegistrazione = !keySubdocAttuale.equals(keySubdocPrecedente);
			cacheNecessarioAggiornamentoRegistrazioneSubdocumento.put(subdoc.getUid(), necessarioAggiornamentoRegistrazione);
			return necessarioAggiornamentoRegistrazione;
		}
		
		return cacheNecessarioAggiornamentoRegistrazioneSubdocumento.get(subdoc.getUid());
		
		
		
	}
	
	private boolean isQuotaPagata(SubdocumentoEntrata subdoc){
		return !isQuotaNonPagata(subdoc);
	}
	
	private boolean isQuotaNonPagata(SubdocumentoEntrata subdoc){
		boolean isQuotaNonPagata = subdocumentoEntrataDad.isQuotaNonPagata(subdoc);
		return isQuotaNonPagata;
	}

	private ElementoPianoDeiConti determinaElementoPianoDeiConti(SubdocumentoEntrata subdoc) {
		final String methodName = "determinaElementoPianoDeiConti";
		
		//le quote "AZero" (isQuotaAZero==true) NON hanno per forza l'impegno o subImpegno quindi non posso ricavare l'elementoPianoDeiConti!
		Accertamento accertamentoOSubAccertamento = subdoc.getAccertamentoOSubAccertamento();
		log.debug(methodName, "Uid "+(accertamentoOSubAccertamento!=null?"Accertamento/SubAccertamento da cui ricavare l'elementoPianoDeiConti: " + accertamentoOSubAccertamento.getUid():"")
				+" subdoc.uid: "+subdoc.getUid());
		ElementoPianoDeiConti elementoPianoDeiConti = impegnoBilDad.findPianoDeiContiAssociatoAMovimentoGestione(subdoc);
		if(elementoPianoDeiConti==null || elementoPianoDeiConti.getUid() == 0) { 
			log.error(methodName, "Impossibile reperire l'elementoPianoDeiConti in quanto non l'Accertamento/SubAccertamento della quota con uid: "+subdoc.getUid() 
				+". Corretto solo se e' una quota a zero.");
			return null;
		}
		
		
		log.debug(methodName, "trovato elementoPianoDeiConti: "+elementoPianoDeiConti.getUid());
		return elementoPianoDeiConti;
	}
	
	/**
	 * Esiste una registrazione non annullata per l'impegno associato alla quota.
	 *
	 * @param subdoc the subdoc
	 * @return true, se la registrazione esiste
	 */
	private boolean esisteUnaRegistrazioneNonAnnullataPerLAccertamentoAssociatoAllaQuota(SubdocumentoEntrata subdoc) {
		final String methodName = "esisteUnaRegistrazioneNonAnnullataPerLImpegnoAssociatoAllaQuota";
		Accertamento accertamentoOSubAccertamento = subdoc.getAccertamentoOSubAccertamento();
		if(accertamentoOSubAccertamento==null){
			log.debug(methodName, "returning false. Accertamento/SubAccertamento non presente per la quota con uid: "+subdoc.getUid()); 
			return false;
		}
		TipoCollegamento tipoCollegamento = SiacDCollegamentoTipoEnum.byModelClass(accertamentoOSubAccertamento.getClass()).getTipoCollegamento();
		
		boolean result = ! CollectionUtils.isEmpty(
				registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, accertamentoOSubAccertamento, 
				// SIAC-5996 
				Ambito.AMBITO_FIN));
		
		// SIAC-5813		
		if (!result && accertamentoOSubAccertamento.getAnnoMovimento() < bilancio.getAnno()) {
			Bilancio bilancioAnnoPrecedente = bilancioDad.getBilancioAnnoPrecedente(bilancio);
			
			Integer uidMovgest = accertamentoOSubAccertamento instanceof SubAccertamento ?
					findUidMovgestByAnnoNumeroBilancio(subdoc.getAccertamento(), (SubAccertamento)accertamentoOSubAccertamento, bilancioAnnoPrecedente) :					
					findUidMovgestByAnnoNumeroBilancio(accertamentoOSubAccertamento, bilancioAnnoPrecedente);
						
			result = uidMovgest != null && 
					! CollectionUtils.isEmpty(
						registrazioneGENServiceHelper.ricercaRegistrazioniMovFinBilancioPrecendenteAssociateAlMovimento(
								tipoCollegamento, 
								uidMovgest,
								bilancioAnnoPrecedente,
								Ambito.AMBITO_FIN));
		}

		log.debug(methodName, "returning " + result + ". tipoCollegamento: " + tipoCollegamento);
		
		return result; 
	}

	private Integer findUidMovgestByAnnoNumeroBilancio(Accertamento accertamento, Bilancio bilancioAnnoPrecedente) {
		return accertamentoBilDad.findUidMovgestByAnnoNumeroBilancio(
				accertamento.getAnnoMovimento(), 
				accertamento.getNumero(),
				Accertamento.class, 
				bilancioAnnoPrecedente.getUid());
	}

	private Integer findUidMovgestByAnnoNumeroBilancio(Accertamento accertamento, SubAccertamento subAccertamento, Bilancio bilancioAnnoPrecedente) {
		return accertamentoBilDad.findUidMovgestTsByAnnoNumeroBilancio(
				accertamento.getAnnoMovimento(), 
				accertamento.getNumero(), 
				String.valueOf(subAccertamento.getNumero()),
				Accertamento.class,
				bilancioAnnoPrecedente.getUid());
	}

	
	private boolean isDaRegistrareInGSA(SubdocumentoEntrata subdoc) {
		final String methodName = "isDaRegistrareInGSA";
		if(subdoc.getAccertamento()!=null && subdoc.getAccertamento().getUid()!= 0 ) {
			Boolean flagAttivaGsa = impegnoBilDad.getFlagAttivaGsa(subdoc.getAccertamento().getUid()); 
			subdoc.getAccertamento().setFlagAttivaGsa(flagAttivaGsa);
			log.debug(methodName, "flagAttivaGSA associato all'Impegno [uid:"+subdoc.getAccertamento().getUid()+"]: " + subdoc.getAccertamento().isFlagAttivaGsa());
			return subdoc.getAccertamento().isFlagAttivaGsa();
		} 
		
		log.debug(methodName, "Impossibile reperire l'impegno associato alla quota con uid: "+subdoc.getUid()+ ". Returning false.");
		return true;
	}
	
	
	private List<SubdocumentoEntrata> getListaSubdocumenti() {
		if(listaSubdocumenti == null){
			listaSubdocumenti = subdocumentoEntrataDad.findSubdocumentiEntrataByIdDocumento(doc.getUid(), 
					SubdocumentoEntrataModelDetail.AccertamentoSubaccertamento,	//Serve sia alla registrazione GEN che PCC
					SubdocumentoEntrataModelDetail.Attr,				//Serve sia alla registrazione GEN che PCC
					//SubdocumentoEntrataModelDetail.Liquidazione 		//Serve solo alla registrazione PCC
					SubdocumentoEntrataModelDetail.SubdocumentoIva    //Serve per determinare l'evento della registrazione GEN
					);
		}
		return listaSubdocumenti;
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
		
		Boolean quoteRilevantiIVAENumeroRegistrazioneCongruenti = isQuoteRilevantiIVAENumeroRegistrazioneCongruenti();
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
	private Boolean isQuoteRilevantiIVAENumeroRegistrazioneCongruenti() {
		String methodName = "isQuoteRilevantiIVAENumeroRegistrazioneCongruenti";
		
		countQuoteRilevantiIvaSenzaNumeroRegistrazione();
		boolean result =  Long.valueOf(0).equals(numeroQuoteRilevantiIvaSenzaNumeroRegistrazione);
		log.debug(methodName,"returning: "+result + ". numeroQuoteRilevantiIvaSenzaNumeroRegistrazione: "+ numeroQuoteRilevantiIvaSenzaNumeroRegistrazione);
		
		return result;
		
	}

	/**
	 * Almeno una quota rilevante iva.
	 *
	 * @param ds the documentoEntrata
	 * @return true se presente almeno una quota rilevante IVA
	 */
	@SuppressWarnings("unused")
	private Boolean almenoUnaQuotaRilevanteIVA() {
		countQuoteRilevantiIva();
		return numeroQuoteRilevantiIva.compareTo(Long.valueOf(0)) > 0;
		
	}
	
	/**
	 * Se a livello di ente &egrave; prevista l'emissione automatica dell'ordinativo 
	 * (GESTIONE_CONVALIDA_AUTOMATICA) il default &egrave; Automatico altrimenti &egrave; Manuale.
	 * 
	 * @return
	 */
	protected Boolean getDefaultFlagConvalidaManuale() {
		final String methodName = "getDefaultFlagConvalidaManuale";
		String gca = ente.getGestioneLivelli().get(TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA);
		if("CONVALIDA_AUTOMATICA".equals(gca)){
			log.debug(methodName, TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA.name()+ " impostata a CONVALIDA_AUTOMATICA");
			return Boolean.FALSE;
		} else if("CONVALIDA_MANUALE".equals(gca)){
			log.debug(methodName, TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA.name()+ " impostata a CONVALIDA_MANUALE");
			return Boolean.TRUE;
		}
		
		return Boolean.TRUE;
	}
	/**
	 * Gets the mappa chiavi elaborazione.
	 *
	 * @param registrazioni the registrazioni
	 * @return the mappa chiavi elaborazione
	 */
	private Map<String, String[]> getMappaChiaviElaborazione(List<? extends RegistrazioneMovFin> registrazioni) {
		Class<?> chiamanteClazz = this.getClass();
		ElabKeys elabKey = ElabKeys.REGISTRAZIONE_MOV_FIN;
		Map<String, List<String>> tmp = new HashMap<String, List<String>>();
		
		for (RegistrazioneMovFin registrazione : registrazioni) {
			ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(registrazione.getUid(), chiamanteClazz);
			String ek = eakh.creaElabKeyFromPattern(elabKey);
			String es = eakh.creaElabServiceFromPattern(elabKey);
			
			if(!tmp.containsKey(es)) {
				tmp.put(es, new ArrayList<String>());
			}
			tmp.get(es).add(ek);
		}
		Map<String, String[]> res = new HashMap<String, String[]>();
		for(Map.Entry<String, List<String>> entry : tmp.entrySet()) {
			res.put(entry.getKey(), entry.getValue().toArray(new String[entry.getValue().size()]));
		}
		
		return res;
	}
	

	protected void startElaborazioneRegistrazioni(List<? extends RegistrazioneMovFin> registrazioni) {
		String methodName = "startElaborazioneRegistrazioni";
		//SIAC-4906
		Map<String, String[]> mappaChiaviElaborazione = getMappaChiaviElaborazione(registrazioni);
		for(Entry<String, String[]> entry : mappaChiaviElaborazione.entrySet()) {
		//FORMALMENTE, posso avere piu' elab services. mappaChiaviElaborazione non dovrebbe accadere nel caso dell'emissione, ma mi mantengo generale
			try {
				elaborazioniManager.startElaborazioni(entry.getKey(), entry.getValue());
			} catch (ElaborazioneAttivaException eae){
				String msg = "L'elaborazione di alcune registrazioni contabili afferenti al documento "+doc.getDescAnnoNumeroUidTipoDocUidSoggettoStato()+" e' ancora in corso. Attendere il termine dell'elaborazione. ["+eae.getMessage()+" "+Arrays.toString(entry.getValue())+"]";
				log.error(methodName, msg, eae);
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
			}
		}
	}

	protected void endElaborazioneRegistrazioni(List<? extends RegistrazioneMovFin> registrazioni) {
		Map<String, String[]> mappaChiaviElaborazione = getMappaChiaviElaborazione(registrazioni);
		for(Entry<String, String[]> entry : mappaChiaviElaborazione.entrySet()) {
			//FORMALMENTE, posso avere piu' elab services. non dovrebbe accadere nel caso in questione, ma mi mantengo generale
			elaborazioniManager.endElaborazioni(entry.getKey(), entry.getValue());
		}
	}

	@Deprecated
	private String[] getElabKeys(List<? extends RegistrazioneMovFin> registrazioni) {
		List<String> elabKeys = new ArrayList<String>();
		for(RegistrazioneMovFin reg : registrazioni){
			elabKeys.add("RegistrazioneMovFin.uid:"+reg.getUid());
		}
		return elabKeys.toArray(new String[elabKeys.size()]);
	}

}

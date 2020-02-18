/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.InserisciRegistroComunicazioniPCCService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CapitoloUscitaGestioneDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.RegistroComunicazioniPCCDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.model.AliquotaIvaTipo;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.CausalePCC;
import it.csi.siac.siacfin2ser.model.DatiFatturaPagataIncassata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.StatoDebito;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * Aggiorna lo stato di un documento Spesa.
 * 
 * <!--img alt="macchina a stati del documento" src="/docs/doc-files/statiDocumento.png" /-->
 * 
 * @version 2.0.0 (riscritto per migliorare performance) 
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaStatoDocumentoDiSpesaService extends CheckedAccountBaseService<AggiornaStatoDocumentoDiSpesa, AggiornaStatoDocumentoDiSpesaResponse> {

	//DADs
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private CapitoloUscitaGestioneDad capitoloUscitaGestioneDad;
	@Autowired
	private RegistroComunicazioniPCCDad registroComunicazioniPCCDad;
	@Autowired
	private DocumentoDad documentoDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired 
	private ImpegnoBilDad impegnoBilDad;
	
	//Services and Components
	@Autowired
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	
	//Fields
	private DocumentoSpesa doc;
	private Bilancio bilancio;
	private Boolean sommaCongruente;
	private StatoOperativoDocumento statoAttuale;
	private StatoOperativoDocumento statoNew;
	private Long numeroQuoteSenzaOrdinativo = null;
	private Long numeroQuoteDelDocumento = null;
	private Long numeroQuoteSenzaLiquidazione = null;
	private Long numeroQuoteSenzaImpegnoOSubImpegno = null;
	private Long numeroQuoteRilevantiIva = null; 
	private Long numeroQuoteRilevantiIvaSenzaNumeroRegistrazione = null;
	private Long numeroDocumentiPadreCollegati = null;
	
	private BigDecimal importoTotaleNoteCollegateSpesaNonAnnullate = null;
	private boolean isCondizioneDiAttivazioneGENSoddisfatta;
	boolean subdocIvaDocumentoCaricato = false;
	
	private List<SubdocumentoSpesa> listaSubdocumenti = null;
	private List<DocumentoSpesa> noteCreditoSpesa = null;
	private List<DocumentoSpesa> noteCreditoSpesaOld = null;
	private List<RegistrazioneMovFin> registrazioniMovFinPrecedentiNcd;
	private Map<Integer, Boolean> cacheNecessarioAggiornamentoRegistrazioneSubdocumento = new HashMap<Integer, Boolean>();
	
	//SIAC-6290
	private boolean isNecessarioAggiornamentoRegistrazioneSubdocumento;
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoSpesa();
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
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		documentoSpesaDad.flushAndClear();
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoSpesaDad.flushAndClear(); //in realtà basta già il primo flushAndClear 2 righe sopra!
		
		capitoloUscitaGestioneDad.setEnte(ente);		
		capitoloUscitaGestioneDad.setLoginOperazione(loginOperazione);
		
		sommaCongruente = null;
		statoAttuale = null;
		statoNew = null;
		
		numeroQuoteSenzaOrdinativo = null;
		numeroQuoteDelDocumento = null;
		numeroQuoteSenzaLiquidazione = null;
		numeroQuoteSenzaImpegnoOSubImpegno = null; 
		numeroQuoteRilevantiIva = null;
		numeroQuoteRilevantiIvaSenzaNumeroRegistrazione = null;
		numeroDocumentiPadreCollegati = null;
		
		importoTotaleNoteCollegateSpesaNonAnnullate = null;
		isCondizioneDiAttivazioneGENSoddisfatta = false;
		
		listaSubdocumenti = null;
		noteCreditoSpesa = null;
		noteCreditoSpesaOld = null;
		registrazioniMovFinPrecedentiNcd = null;
		//noteCreditoEntrata = null;
		
		subdocIvaDocumentoCaricato = false;
		
		elaborazioniManager.init(ente, loginOperazione);
		
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

		caricaDocumentoSpesa();
		BigDecimal totaleQuoteENoteCreditoMenoImportiDaDedurre = calcolaTotaleQuoteENoteCreditoMenoImportiDaDedurre();
		
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
		} else {
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
	private BigDecimal calcolaTotaleQuoteENoteCreditoMenoImportiDaDedurre() {
		final String methodName = "calcolaTotaleQuoteENoteCreditoMenoImportiDaDedurre";
		
		
//		BigDecimal arrotondamento = doc.getArrotondamento().abs();
		BigDecimal arrotondamento = doc.getArrotondamento();
		log.debug(methodName , "arrotondamento: " + arrotondamento);

		BigDecimal importoTotaleSubdoumentiMenoImportoDaDedurre = calcolaImportoTotaleSubdoumentiMenoImportoDaDedurre();
		log.debug(methodName , "importoTotaleSubdoumentiMenoImportoDaDedurre: " + importoTotaleSubdoumentiMenoImportoDaDedurre);
		
		BigDecimal importoTotaleOneriConTipoIvaSplitReverseChange = calcolaImportoTotaleOneriConTipoIvaSplitReverseChange();
		log.debug(methodName , "importoTotaleOneriConTipoIvaSplitReverseChange: " + importoTotaleOneriConTipoIvaSplitReverseChange);
		
		calcolaImportoTotaleNoteCollegateSpesaNonAnnullate();
		log.debug(methodName , "importoTotaleNoteCollegateSpesaNonAnnullate: " + importoTotaleNoteCollegateSpesaNonAnnullate);
		
		
		
		BigDecimal result = BigDecimal.ZERO;
		log.debug(methodName , "valore iniziale: " + result);

//		result = result.add(arrotondamento);
		result = result.subtract(arrotondamento);
		log.debug(methodName , "valore precedente - arrotondamento: " + result);
		
		result = result.add(importoTotaleSubdoumentiMenoImportoDaDedurre);
		log.debug(methodName , "valore precedente + totale subdocumenti - importo da dedurre: " + result);
		
		result = result.subtract(importoTotaleOneriConTipoIvaSplitReverseChange);
		log.debug(methodName , "valore precedente - totale imponibile importo degli oneri con TipoIvaSplitReverse.REVERSE_CHANGE: " + result);
		
		result = result.add(importoTotaleNoteCollegateSpesaNonAnnullate);
		log.debug(methodName , "valore precedente + note credito: " + result);
		
		log.debug(methodName, "returning: "+ result);
		return result;
		
	}
	
	private BigDecimal calcolaImportoTotaleSubdoumentiMenoImportoDaDedurre() {
		return documentoSpesaDad.calcolaImportoTotaleMenoImportoDaDedurreSubdocumenti(doc);
//		return doc.calcolaImportoTotaleSubdoumenti().subtract(doc.calcolaImportoTotaleDaDedurreSobdocumenti());
	}
	
	private BigDecimal calcolaImportoTotaleOneriConTipoIvaSplitReverseChange() {
		return documentoSpesaDad.calcolaImportoImponibileTotaleOneriPerTipoIvaSplitReverse(doc, TipoIvaSplitReverse.REVERSE_CHANGE);
	}
	
	private BigDecimal calcolaImportoTotaleNoteCollegateSpesaNonAnnullate() {
		if(importoTotaleNoteCollegateSpesaNonAnnullate==null){
//			return documentoSpesaDad.calcolaImportoTotaleNoteCollegateSpesaNonAnnullate(doc);
			importoTotaleNoteCollegateSpesaNonAnnullate = documentoSpesaDad.calcolaImportoTotaleDaDedurreSuFatturaNoteCollegateSpesaNonAnnullate(doc);
		}
		return importoTotaleNoteCollegateSpesaNonAnnullate;
	}
	

	/**
	 * Carica dettaglio documento spesa e quote.
	 */
	private void caricaDocumentoSpesa() {
		doc = documentoSpesaDad.findDocumentoSpesaById(doc.getUid(), DocumentoSpesaModelDetail.Attr); //Degli attributi mi serve l'arrotondamento!
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
		BigDecimal importoNetto = doc.getImportoNetto();
		BigDecimal totaleNoteCredito = calcolaImportoTotaleNoteCollegateSpesaNonAnnullate();
		boolean isStornato = importoNetto.compareTo(totaleNoteCredito) == 0;
		log.debug(methodName, "Importo netto documento: " + importoNetto + "; totale note collegate: " + totaleNoteCredito + ". Stornato? " + isStornato);
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
		
		boolean result = sommaCongruente.booleanValue() && Boolean.TRUE.equals(tutteLeQuoteSonoAssociateAImpegnoOSubImpegno());

		log.debug(methodName, "returning: " + result);
		return result;
	}

	
	
	private Boolean tutteLeQuoteSonoAssociateAImpegnoOSubImpegno() {
		String methodName = "tutteLeQuoteSonoAssociateAImpegnoOSubImpegno";
		
		countQuoteSenzaImpegnoOSubImpegno();
		
		boolean result = Long.valueOf(0).equals(numeroQuoteSenzaImpegnoOSubImpegno);
		log.debug(methodName, "returning: " + result);
		return result;
	}


	

	/**
	 * Checks if is parzialmente liquidato.
	 *
	 * @return true, if is parzialmente liquidato
	 */
	private boolean isParzialmenteLiquidato() {
		
		String methodName = "isParzialmenteLiquidato";
		
		countQuoteSenzaLiquidazione();
		countQuoteEscludendoQuotaAZero();
		
		boolean result =  numeroQuoteSenzaLiquidazione.compareTo(Long.valueOf(0))>=0 && 
				numeroQuoteSenzaLiquidazione.compareTo(numeroQuoteDelDocumento)<0;
		
		// 1 >= 0 && 1 < 2 -> true  (è parzialmente Liquidato)
		// 0 >= 0 && 0 < 2 -> false (è parzialmente Liquidato. NB. Fosse Liquidato non sarebbe arrivato qui! arriva qui se ha la somma non congruente!)
		// 2 >= 0 && 2 < 2 -> false (è senza neanche una Liquidazione)
		
		log.debug(methodName, "returning: " + result);
		return result;
	}

	/**
	 * Checks if is liquidato.
	 *
	 * @return true, if is liquidato
	 */
	private boolean isLiquidato() {
		String methodName = "isLiquidato";

		boolean result =  sommaCongruente.booleanValue() && Long.valueOf(0).equals(countQuoteSenzaLiquidazione());
		log.debug(methodName, "returning: " + result);
		return result;
	}



	/**
	 * Checks if is parzialmente emesso.
	 *
	 * @return true, if is parzialmente emesso
	 */
	private boolean isParzialmenteEmesso() {
		String methodName = "isParzialmenteEmesso";
		
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
		
		// SIAC-4749: se sono presenti i dati della fattura pagata/incassata, il documento e' automaticamente emesso
		boolean result = 
				isDatiPagamentoValorizzati(doc.getDatiFatturaPagataIncassata())
				|| (sommaCongruente.booleanValue() && Long.valueOf(0).equals(countQuoteSenzaOrdinativo()));
		log.debug(methodName, "returning: " + result);
		return result;
	}
	
	/**
	 * Controlla se i dati di pagamento del documento siano valorizzati
	 * @param datiFatturaPagataIncassata i dati di pagamento/incasso della fattura
	 * @return true se i dati sono valorizzati
	 */
	private boolean isDatiPagamentoValorizzati(DatiFatturaPagataIncassata datiFatturaPagataIncassata) {
		return datiFatturaPagataIncassata != null
				&& Boolean.TRUE.equals(datiFatturaPagataIncassata.getFlagPagataIncassata())
				&& StringUtils.isNotBlank(datiFatturaPagataIncassata.getNotePagamentoIncasso())
				&& datiFatturaPagataIncassata.getDataOperazione() != null;
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


//	/**
//	 * nota 1 : nel calcolo dello stato non bisogna considerare le quote a
//	 * ‘zero’, ossia quelle che sono eventualmente collegate ad una nota credito
//	 * per un importo da dedurre per quella quota=importo della quota stessa.
//	 *
//	 * @param quota the quota
//	 * @return true, if is quota a zero
//	 */
//	private boolean isQuotaAZero(SubdocumentoSpesa quota) {
//		return quota.getImporto().compareTo(quota.getImportoDaDedurre()) == 0;
//	}
	
	
	private Long countQuoteSenzaOrdinativo() {
		if(numeroQuoteSenzaOrdinativo==null){
			numeroQuoteSenzaOrdinativo = documentoSpesaDad.countQuoteSenzaOrdinativo(doc);
		}
		return numeroQuoteSenzaOrdinativo;
	}
	
	private Long countQuoteEscludendoQuotaAZero() {
		if(numeroQuoteDelDocumento==null){
			numeroQuoteDelDocumento = documentoSpesaDad.countQuoteEscludendoQuotaAZero(doc);
		}
		return numeroQuoteDelDocumento;
	}
	
	private Long countQuoteSenzaLiquidazione() {
		if(numeroQuoteSenzaLiquidazione==null) {
			numeroQuoteSenzaLiquidazione = documentoSpesaDad.countQuoteSenzaLiquidazione(doc);
		}
		return numeroQuoteSenzaLiquidazione;
	}
	
	private Long countQuoteSenzaImpegnoOSubImpegno() {
		if(numeroQuoteSenzaImpegnoOSubImpegno==null){
			numeroQuoteSenzaImpegnoOSubImpegno = documentoSpesaDad.countQuoteSenzaImpegnoOSubImpegno(doc);
		}
		return numeroQuoteSenzaImpegnoOSubImpegno;
	}
	
	@SuppressWarnings("unused")
	private Long countQuoteRilevantiIva() {
		if(numeroQuoteRilevantiIva==null) {
			numeroQuoteRilevantiIva = documentoSpesaDad.countQuoteRilevantiIva(doc);
		}
		return numeroQuoteRilevantiIva;
	}
	
	private Long countQuoteRilevantiIvaSenzaNumeroRegistrazione() {
		if(numeroQuoteRilevantiIvaSenzaNumeroRegistrazione==null) {
			numeroQuoteRilevantiIvaSenzaNumeroRegistrazione = documentoSpesaDad.countQuoteRilevantiIvaSenzaNumeroRegistrazione(doc);
		}
		return numeroQuoteRilevantiIvaSenzaNumeroRegistrazione;
	}
	
	private Long countDocumentiPadreCollegati() {
		if(numeroDocumentiPadreCollegati==null) {
			numeroDocumentiPadreCollegati = documentoSpesaDad.countDocumentiPadreCollegati(doc);
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
			bilancio = documentoSpesaDad.findBilancioAssociatoAlDocumento(doc.getUid());
		}
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, bilancio);
		
		
		List<RegistrazioneMovFin> registrazioniPrecedenti = ricercaRegistrazioniMovFinAssociataAlMovimento();
		startElaborazioneRegistrazioni(registrazioniPrecedenti);
		try {
			//Annullo le Registrazioni relative solo ai subdocumenti che sono stati modificati e che quindi non sono piu' valide.
			//a prescindere da fatto che le condizioni di attivazione GEN siano di nuovo soddisfatte o meno.
			//boolean esistonoRegistrazioniPrecedenti = gestisciAnnullamentoRegistrazioniGENEPrimeNote();
			gestisciAnnullamentoRegistrazioniGENEPrimeNote(registrazioniPrecedenti);
		
		
			isCondizioneDiAttivazioneGENSoddisfatta = isCondizioneDiAttivazioneGENSoddisfatta();
			if(!isCondizioneDiAttivazioneGENSoddisfatta) {
				log.info(methodName, "Condizione di attivazione NON soddisfatta. Non viene inserita/aggiornata la RegistrazioneMovFin.");
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
			//valutare se impostare qualcosa nella response
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
		
		//creo le chiavi per l'ambito CEC
		ElaborazioniAttiveKeyHandler eakhCEC = new ElaborazioniAttiveKeyHandler(doc.getUid(),AggiornaStatoDocumentoDiSpesaService.class,DocumentoSpesa.class,Ambito.AMBITO_CEC.name());
		String elabServiceCEC    = eakhCEC.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA);
		String elabKeyCEC        = eakhCEC.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA);
		String elabServiceCECNCD = eakhCEC.creaElabServiceFromPattern(ElabKeys.PRIMA_NOTA_NCD);
		String elabKeyCECNCD     = eakhCEC.creaElabKeyFromPattern(ElabKeys.PRIMA_NOTA_NCD);
		
		//String group = "elabPrimaNota.movId:"+doc.getUid()+",type:"+DocumentoSpesa.class.getSimpleName();
		
		//NB: l'avvio di queste operazioni Async e' differito di 30 sec.
		//Bisogna avere ragionevole certezza che la tx corrente termini (con commit o anche rollback) entro 10 sec a partire da quando sono invocate.
		//Ragion per cui sono da mantenere più in "fondo" possibile del metodo execute() di questo servizio. Qui dovrebe andar bene. Non spostarle da qui.
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(res.getRegistrazioniMovFinPrimaNota(), elabServiceFIN, errore, elabKeyFIN);
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(res.getRegistrazioniMovCECPrimaNota(), elabServiceCEC, errore, elabKeyCEC);
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(res.getRegistrazioniMovGSAPrimaNota(), elabServiceGSA, errore, elabKeyGSA);
		
		//Inserisce una primaNota per le quote con importo da dedurre>0 del DocumentoSpesa
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(res.getRegistrazioniMovFinNNcdInserite(),   elabServiceFINNCD, errore, elabKeyFINNCD);
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(res.getRegistrazioniMovFinNcdCECInserite(), elabServiceCECNCD, errore, elabKeyCECNCD);
		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(res.getRegistrazioniMovFinNcdGSAInserite(), elabServiceGSANCD, errore,elabKeyGSANCD);
	}

	/**
	 * Annulla, solo sotto certe condizioni, le registrazioni (e la relativa primaNota) che richiedono di essere modificate.
	 * 
	 * @return registrazioni precedenti (che sono state o meno annullate)
	 */
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
		List<RegistrazioneMovFin> registrazioniMovFinPrecedenti = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.SUBDOCUMENTO_SPESA, doc); //se presenti ne troverà una per ogni quota, altrimenti 0.
		//boolean esistonoRegistrazioniPrecedenti = !registrazioniMovFinPrecedenti.isEmpty(); //true se si stratta di un aggiornamento, false se si tratta di un primo inserimento
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
			if(!(registrazionePrecedente.getMovimento() instanceof SubdocumentoSpesa)){
				log.warn(methodName, "La registrazione [uid:"+registrazionePrecedente.getMovimento().getUid()+"] ha tipoCollegamento "
						+ "errato per il  SubdocumentoSpesa [uid:"+registrazionePrecedente.getMovimento().getUid()+"]. Verranno saltate. Occorre bonificare DB. ");
				continue;//se succede è perchè ci sono delle registrazioni con tipoCollegamento errate associate a questo documento.
			}
			SubdocumentoSpesa subdoc = (SubdocumentoSpesa)registrazionePrecedente.getMovimento() /*SiacTSubdoc_SubdocumentoSpesa_Base*/;
			isNecessarioAggiornamentoRegistrazioneSubdocumento = isNecessarioAggiornamentoRegistrazioneSubdocumento(subdoc);
			if(isNecessarioAggiornamentoRegistrazioneSubdocumento) {
				
				log.info(methodName, "Il Subdocumento [uid:"+registrazionePrecedente.getMovimento().getUid()+"] presenta modifiche che rendono necessario "
						+ "l'aggiornamento della registrazione, pertanto annullo la registrazione e la prima nota se presente. "
						+ "Se le condizioni di attivazione saranno nuovamente soddisfatte "
						+ "verra' reinserita la registrazione con l'eventuale prima nota. ");
				
				registrazioneGENServiceHelper.annullaRegistrazioneMovFinEPrimaNota(registrazionePrecedente, registrazioniMovFinPrecedenti); //se la registrazione esisteva devo annullare le eventuali primeNote associate
				
				gestisciAnnullamentoRegistrazioneNotaCreditoAssociata(subdoc);
			}
		}
	}

	private void gestisciAnnullamentoRegistrazioneNotaCreditoAssociata(SubdocumentoSpesa subdoc) {
		final String methodName = "gestisciAnnullamentoRegistrazioneNotaCreditoAssociata";
		
		BigDecimal importoDaDedurreOld = subdocumentoSpesaDad.findImportoDaDedurreSubdocumentoSpesaByIdTxRequiresNew(subdoc.getUid());
		
		if(importoDaDedurreOld == null || BigDecimal.ZERO.compareTo(importoDaDedurreOld)==0){
			log.debug(methodName, "La quota [uid:"+subdoc.getUid()+"] non aveva un importo da dedurre e di conseguenza non aveva registrazioni per la nota credito. Esco.");
			return;
		}
		
		log.debug(methodName, "La quota [uid:"+subdoc.getUid()+"] aveva un importo da dedurre pari a "+importoDaDedurreOld+".. cerco di annullare la relativa registrazione della nota credito.");
		
		List<DocumentoSpesa> noteCreditoPrecedenti = getListaNoteCreditoSpesaCollegateEsclusivamenteAlDocumentoOld();
		
		if(noteCreditoPrecedenti.isEmpty()){
			log.warn(methodName, "Il documento [uid:"+doc.getUid()+"] non aveva note credito spesa precedenti da annullare pur avendo un "
					+ "importo da dedurre diverso da zero. Verificare coerenza dati su DB.");
			return;//Me ne aspetto una soltatnto.
		}
		
		if(noteCreditoPrecedenti.size()!=1){
			log.warn(methodName, "Il documento [uid:"+doc.getUid()+"] ha piu' di una nota credito collegata. Non lo gestiamo.");
			return;//Me ne aspetto una soltatnto.
		}
		
		log.debug(methodName, "Il documento [uid:"+doc.getUid()+"] ha esattamente una e una sola nota credito. Ricerco le registrazioni associate.");
		//Il documento ha esattamente una e una sola nota credito.
		DocumentoSpesa notaCreditoPrecedente = noteCreditoPrecedenti.get(0);
		
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
	private boolean isRegistrazioneMovFinNotaCreditoRelativaASubdoc(RegistrazioneMovFin registrazioneMovFinPrecedenteNcd, SubdocumentoSpesa subdoc) {
		final String methodName = "isRegistrazioneMovFinNotaCreditoRelativaASubdoc";
		//TODO devo trovare un modo di filtrare la registrazione nota credito relativa alla mia quota!! 
		
		//notaCreditoRegistrazionePrecedente.getImporto().compareTo(importoDaDedurreOld) == 0 //Per il solo importoDaDedurre non va bene!!
		
		
		SubdocumentoSpesa subdocCollegatoAllaRegistrazione = (SubdocumentoSpesa)registrazioneMovFinPrecedenteNcd.getMovimentoCollegato();
		boolean result = subdocCollegatoAllaRegistrazione!=null && subdocCollegatoAllaRegistrazione.getUid() == subdoc.getUid();
		
		log.debug(methodName, "returning: "+result);
		return result;

	}

	private List<RegistrazioneMovFin> ricercaRegistrazioniMovFinAssociataAllaNotaCreditoPrecedente(DocumentoSpesa notaCreditoPrecedente) {
		if(registrazioniMovFinPrecedentiNcd==null){
			registrazioniMovFinPrecedentiNcd = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimentoTxRequiresNew(TipoCollegamento.SUBDOCUMENTO_SPESA, notaCreditoPrecedente);
		}
		return registrazioniMovFinPrecedentiNcd;
	}
	
	

	private void inserisciRegistrazioniGEN(List<RegistrazioneMovFin> registrazioniPrecedenti) {
		final String methodName = "inserisciRegistrazioniGEN";
		
		boolean esistonoRegistrazioniPrecedenti =  !registrazioniPrecedenti.isEmpty();
		boolean isNotaCredito = doc.getTipoDocumento().isNotaCredito();
		
		Evento eventoCEC = registrazioneGENServiceHelper.determinaEventoCassaEconomaleENotaCredito(TipoCollegamento.SUBDOCUMENTO_SPESA, esistonoRegistrazioniPrecedenti, isNotaCredito);
		
		List<RegistrazioneMovFin> registrazioniMovFINInserite = new ArrayList<RegistrazioneMovFin>();
		List<RegistrazioneMovFin> registrazioniMovCECInserite = new ArrayList<RegistrazioneMovFin>();
		List<RegistrazioneMovFin> registrazioniMovGSAInserite = new ArrayList<RegistrazioneMovFin>();
		
		List<RegistrazioneMovFin> registrazioniMovFINPrecedenti = new ArrayList<RegistrazioneMovFin>();
		List<RegistrazioneMovFin> registrazioniMovCECPrecedenti = new ArrayList<RegistrazioneMovFin>();
		List<RegistrazioneMovFin> registrazioniMovGSAPrecedenti = new ArrayList<RegistrazioneMovFin>();
		
		for(SubdocumentoSpesa subdoc : getListaSubdocumenti()) {
			
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
//			boolean isQuotaConMovimentoResiduo = isQuotaConMovimentoResiduo(subdoc);
//			if(Boolean.TRUE.equals(doc.getFlagDisabilitaRegistrazioneResidui()) && isQuotaConMovimentoResiduo){
//				/* La registrazione di quote afferenti a impegni con anno precedente all'anno di bilancio del documento
//				 * vengono saltate se flag disabilita registrazione residui e' true.
//				 * CR SIAC-2847
//				 */
//				log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " e' afferente ad un movimento residuo ed "
//						+ "il flagDisabilitaRegistrazioneResidui e' abilitato. La registrazione verra' saltata. ");
//				continue;
//			}
			
			RegistrazioneMovFin registrazionePrecedenteFIN = cercaRegistrazionePrecedenteNonAnnullata(subdoc, registrazioniPrecedenti, Ambito.AMBITO_FIN);
			if(registrazionePrecedenteFIN!=null){
				log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " ha gia' una registrazione associata NON annullata. "
						+ "Non verra' pertanto creata una ulteriore registrazione.");
				registrazioniMovFINPrecedenti.add(registrazionePrecedenteFIN);
				//a questo punto esistera' anche una registrazione per la notaCredito se presente. Avra' gia' la sua prima nota che non modifico.
				//gestisciRegistrazioniGENNoteCreditoSpesaCollegate(esistonoRegistrazioniPrecedenti, subdoc, determinaElementoPianoDeiConti(subdoc));
				//continue;
				
				//Se esiste la registrazione FIN potrebbe esistere quella GSA
				RegistrazioneMovFin registrazionePrecedenteGSA = cercaRegistrazionePrecedenteNonAnnullata(subdoc, registrazioniPrecedenti, Ambito.AMBITO_GSA);
				if(registrazionePrecedenteGSA!=null){
					log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " ha gia' una registrazione associata NON annullata per GSA. "
							+ "Non verra' pertanto creata una ulteriore registrazione per GSA.");
					registrazioniMovGSAPrecedenti.add(registrazionePrecedenteGSA);
				}
				
				//Se esiste la registrazione FIN potrebbe esistere quella CEC
				RegistrazioneMovFin registrazionePrecedenteCEC = cercaRegistrazionePrecedenteNonAnnullata(subdoc, registrazioniPrecedenti, Ambito.AMBITO_CEC);
				if(registrazionePrecedenteCEC!=null){
					log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " ha gia' una registrazione associata NON annullata per CEC. "
							+ "Non verra' pertanto creata una ulteriore registrazione per CEC.");
					registrazioniMovCECPrecedenti.add(registrazionePrecedenteCEC);
				}
				
				continue;
			} 
				
			if(subdoc.getLiquidazione() != null && subdoc.getLiquidazione().getUid() != 0 && !isNecessarioAggiornamentoRegistrazioneSubdocumento){
				log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " ha una liquidazione associata. Non verra' pertanto creata una registrazione.");
				continue;//questo evita di fare inserire anche le registrazioni per GSA e CEC.
			}
			
			if(esisteUnaRegistrazioneNonAnnullataPerLImpegnoAssociatoAllaQuota(subdoc)){
				log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " ha gia' un Impegno/SubImpegno associato ad una registrazione non annullata. "
						+ "Non verra' pertanto creata una ulteriore registrazione.");
				continue;//questo evita di fare inserire anche le registrazioni per GSA e CEC.
			}
			
			if(esisteUnaRegistrazioneNonAnnullataPerQuotaAnnoPrecedente(subdoc)){
				log.info(methodName, "Il subdocumento con uid "+ subdoc.getUid() + " ha gia' una registrazione nell'anno precedente non annullata. "
						+ "Non verra' pertanto creata una ulteriore registrazione.");
				continue;//questo evita di fare inserire anche le registrazioni per GSA e CEC.
			}
			
			
			ElementoPianoDeiConti elementoPianoDeiConti = determinaElementoPianoDeiConti(subdoc);
			
			//Carico i dati Iva necessari a determinare l'evento. SIAC-2847
			List<SubdocumentoIvaSpesa> listaSubdocumentiIva = getSubdocumentiIva(subdoc);
			
			//########## prima della CR SIAC-4537
//			Map<AliquotaIvaTipo, Integer> m = countAliquotaIvaTipo(listaSubdocumentiIva);
//			boolean isConIva = isConIva(subdoc.getFlagRilevanteIVA(), m); /*almenoUnaQuotaRilevanteIVA(doc)*/
//			boolean isTipoIvaPromisqua = isTipoIvaPromisqua(m);
			
			//########## dopo SIAC-4537
			BigDecimal totaleImpostaDetraibileMovimentiIva = getTotaleDetraibileMovimentiIva(listaSubdocumentiIva); 
			BigDecimal totaleImpostaIndetraibileMovimentiIva= getTotaleIndetraibileMovimentiIva(listaSubdocumentiIva);
			boolean isConIva = isConIva(subdoc.getFlagRilevanteIVA(), totaleImpostaDetraibileMovimentiIva);
			boolean isTipoIvaPromisqua = isTipoIvaPromisqua(totaleImpostaDetraibileMovimentiIva, totaleImpostaIndetraibileMovimentiIva);
			//########## 
			
			// SIAC-4504
			//boolean isResiduo = isQuotaConMovimentoResiduo && isTitolo01(elementoPianoDeiConti);
			boolean isQuotaConMovimentoResiduo = isQuotaConMovimentoResiduo(subdoc);
			boolean isResiduo = isQuotaConMovimentoResiduo;
			
			//L'evento e' determinato a livello di quota. Vedi SIAC-2422.
			Evento evento = registrazioneGENServiceHelper.determinaEvento(TipoCollegamento.SUBDOCUMENTO_SPESA, 
																			esistonoRegistrazioniPrecedenti, 
																			isConIva,
																			isTipoIvaPromisqua,
																			doc.getTipoDocumento().isNotaCredito(),
																			isResiduo);
			
			//########### FIN ###########
			RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subdoc , elementoPianoDeiConti, Ambito.AMBITO_FIN);
			log.debug(methodName, "Inserita la registrazione per la quota: "+subdoc.getUid() + " con evento "+ evento.getCodice());
			registrazioniMovFINInserite.add(registrazioneMovFin);
			gestisciRegistrazioniGENNoteCreditoSpesaCollegate(esistonoRegistrazioniPrecedenti, subdoc, elementoPianoDeiConti, isConIva, isTipoIvaPromisqua, isResiduo);
			
			//########### GSA ###########
			if(isDaRegistrareInGSA(subdoc)){
				log.debug(methodName, "Inserisco anche la registrazione per GSA relativa alla quota con uid: "+subdoc.getUid());
				RegistrazioneMovFin registrazioneMovGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subdoc , elementoPianoDeiConti, Ambito.AMBITO_GSA);
				registrazioniMovGSAInserite.add(registrazioneMovGSA);
			}
			
			//########### CEC ###########
			if(isDaRegistrareInCEC()){
				log.debug(methodName, "Inserisco anche la registrazione per la Cassa Economale relativa alla quota con uid: "+subdoc.getUid());
			    RegistrazioneMovFin registrazioneMovFinCEC = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(eventoCEC, subdoc , elementoPianoDeiConti, Ambito.AMBITO_FIN);  //no AMBITO_CEC!!
			    registrazioniMovCECInserite.add(registrazioneMovFinCEC);
			}
			
		}
		
		//Se avviassi qui l'inserimento delle primeNote rischio che partano prima che la transazione abbia committato! e quindi non troverebbero la registrazione.
//		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovFINInserite);
//		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovCECInserite);
//		registrazioneGENServiceHelper.inserisciPrimaNotaAutomaticaAsync(registrazioniMovGSAInserite);
		
		res.setRegistrazioniMovFinInserite(registrazioniMovFINInserite);
		res.setRegistrazioniMovCECInserite(registrazioniMovCECInserite);
		res.setRegistrazioniMovGSAInserite(registrazioniMovGSAInserite);
		
		res.setRegistrazioniMovFinPrecedenti(registrazioniMovFINPrecedenti);
		res.setRegistrazioniMovCECPrecedenti(registrazioniMovCECPrecedenti);
		res.setRegistrazioniMovGSAPrecedenti(registrazioniMovGSAPrecedenti);
	}

	
	


	private boolean isQuotaConMovimentoResiduo(SubdocumentoSpesa subdoc) {
		final String methodName = "isQuotaConMovimentoResiduo";
		if(subdoc.getImpegno()==null || subdoc.getImpegno().getUid()==0){
			return false; //le quote a Zero non hanno per forza un impegno.
		}
		//fino a qui basta l'uid, adesso carico il bilancio perche' ho bisogno dell'anno
		bilancio = bilancioDad.getBilancioByUid(bilancio.getUid());
		boolean result = subdoc.getImpegno().getAnnoMovimento() < bilancio.getAnno();
		log.debug(methodName, "returning: " + result + ". Anno bilancio: "+bilancio.getAnno() + " anno movimento: "+ subdoc.getImpegno().getAnnoMovimento());
		return result;
	}


	
	// SIAC-4504
//	private boolean isTitolo01(ElementoPianoDeiConti elementoPianoDeiConti) {
//		final String methodName = "isTitolo01";
//		boolean result = elementoPianoDeiConti!=null && elementoPianoDeiConti.getCodice()!=null && elementoPianoDeiConti.getCodice().startsWith("U.1.");
//		log.debug(methodName, "returning: " + result + ". ");
//		return result;
//		
//		
////		//le quote "AZero" (isQuotaAZero==true) NON hanno per forza l'impegno o subImpegno quindi non posso ricavare l'elementoPianoDeiConti!
////		TitoloSpesa titoloSpesa = null;
////		if(subdoc.getImpegno()!=null && subdoc.getImpegno().getUid()!= 0 ) { //.getCapitoloUscitaGestione()!=null) { 
////			
////			log.debug(methodName, "Uid impegno da cui ricavare l'elementoPianoDeiConti: " + subdoc.getImpegno().getUid());
////			titoloSpesa = impegnoBilDad.findTitoloAssociatoAMovimentoGestione(subdoc.getImpegno());
////			
////			log.debug(methodName, "trovato elementoPianoDeiConti: "+(titoloSpesa!=null?titoloSpesa.getUid():"null"));
////		} else {
////			log.debug(methodName, "Impossibile reperire l'elementoPianoDeiConti in quanto non ho l'impegno della quota con uid: "+subdoc.getUid());
////		}
////		return titoloSpesa!=null && titoloSpesa.getCodice()!=null && titoloSpesa.getCodice().equals("01");
//	}

//	/**
//	 * Estrae una mappa con il conteggio delle occorrenze per ogni {@link AliquotaIvaTipo} possibile.
//	 * 
//	 * @param s
//	 * @return mappa con chiave AliquotaIvaTipo e valore il conteggio delle occorrenze.
//	 */
//	private Map<AliquotaIvaTipo, Integer> countAliquotaIvaTipo(SubdocumentoIvaSpesa s) {
//		List<SubdocumentoIvaSpesa> l = new ArrayList<SubdocumentoIvaSpesa>();
//		l.add(s);
//		return countAliquotaIvaTipo(l);
//	}
	
	/**
	 * Estrae una mappa con il conteggio delle occorrenze per ogni {@link AliquotaIvaTipo} possibile.
	 * 
	 * @param s
	 * @return mappa con chiave AliquotaIvaTipo e valore il conteggio delle occorrenze.
	 */
	private Map<AliquotaIvaTipo, Integer> countAliquotaIvaTipo(List<SubdocumentoIvaSpesa> ss) {
		final String methodName = "countAliquotaIvaTipo";
		Map<AliquotaIvaTipo, Integer> m = new HashMap<AliquotaIvaTipo, Integer>();
		
		//Imposto a zero il contatore per ogni AliquotaIvaTipo.
		for(AliquotaIvaTipo ati: AliquotaIvaTipo.values()){
			m.put(ati, 0);
		}
		
		for(SubdocumentoIvaSpesa s: ss) {
			for(AliquotaSubdocumentoIva asi :s.getListaAliquotaSubdocumentoIva()){
				AliquotaIvaTipo ati = asi.getAliquotaIva().getAliquotaIvaTipo();
				if(ati==null){
					throw new IllegalStateException("Impossibile stabilire l'AliquotaIvaTipo per l'aliquota iva con uid: "+ asi.getAliquotaIva().getUid() +". Controllare la configurazione delle aliquote iva.");
				}
				
				if(AliquotaIvaTipo.COMMERCIALE.equals(ati) && 
					new BigDecimal("100").compareTo(asi.getAliquotaIva().getPercentualeIndetraibilita()) == 0){
					
					log.debug(methodName, "AliquotaIva di tipo COMMERCIALE con uid "+asi.getUid()+" esclusa dal conteggio perche' con percentuale indetraibilita' = 100.");
					continue;
				}
				
				Integer count = m.get(ati) + 1;
				m.put(ati, count);
			}
		}
		log.info(methodName, "Conteggio ottenuto: "+m);
		return m;
	}
	
	private boolean isConIva(Boolean flagRilevanteIVA, Map<AliquotaIvaTipo, Integer> m) {
		
		if(!Boolean.TRUE.equals(flagRilevanteIVA)){
			return false;
		}
		
		//La quota ha il flagRilevanteIVA = TRUE
		
//		int countPromisque = m.get(AliquotaIvaTipo.PROMISQUA);
//		int countIstituzionale = m.get(AliquotaIvaTipo.ISTITUZIONALE);
		int countCommerciale = m.get(AliquotaIvaTipo.COMMERCIALE);
		
		//se ho AliquotaIvaTipo.COMMERCIALE return true....altrimenti false.
		
		return countCommerciale>0;
	}

	

	private boolean isConIva(Boolean flagRilevanteIVA, BigDecimal totaleImpostaDetraibileMovimentiIva) {
		
		if(!Boolean.TRUE.equals(flagRilevanteIVA)){
			return false;
		}
		
		return totaleImpostaDetraibileMovimentiIva.signum() == 1;
	}
	
	private boolean isTipoIvaPromisqua(Map<AliquotaIvaTipo, Integer> m) {
		//subdoc.getSubdocumentoIva().getListaAliquotaSubdocumentoIva().get(0).getAliquotaIva().getAliquotaIvaTipo().equals(AliquotaIvaTipo.PROMISQUA);
		
		int countPromisque = m.get(AliquotaIvaTipo.PROMISQUA);
		int countIstituzionale = m.get(AliquotaIvaTipo.ISTITUZIONALE);
		int countCommerciale = m.get(AliquotaIvaTipo.COMMERCIALE);
		
		return countPromisque>0 || (countPromisque == 0 && (countIstituzionale>0 && countCommerciale>0));
	}
	

	private boolean isTipoIvaPromisqua(BigDecimal totaleImpostaDetraibileMovimentiIva, BigDecimal totaleImpostaIndetraibileMovimentiIva) {
		return totaleImpostaDetraibileMovimentiIva.signum()>0 && totaleImpostaIndetraibileMovimentiIva.signum()>0;
	}
	
	
	
	private BigDecimal getTotaleDetraibileMovimentiIva(List<SubdocumentoIvaSpesa> subdocumentiIva) {
		BigDecimal result= BigDecimal.ZERO;
		
		if(subdocumentiIva!=null){
			for(SubdocumentoIvaSpesa si : subdocumentiIva){
				result = result.add(si.getTotaleImpostaDetraibileMovimentiIva());
			}
		}
		
		return result;
	}
	
	private BigDecimal getTotaleIndetraibileMovimentiIva(List<SubdocumentoIvaSpesa> subdocumentiIva) {
		BigDecimal result= BigDecimal.ZERO;
		
		if(subdocumentiIva!=null){
			for(SubdocumentoIvaSpesa si : subdocumentiIva){
				result = result.add(si.getTotaleImpostaIndetraibileMovimentiIva());
			}
		}
		
		return result;
	}

	/**
	 * Restituisce il subocumentoIva associato alla quota se presente, altrimenti quello associato al suo documento se presente.
	 * 
	 * @param subdoc
	 * @return il subdocumentoIva
	 */
	private List<SubdocumentoIvaSpesa> getSubdocumentiIva(SubdocumentoSpesa subdoc) {
		List<SubdocumentoIvaSpesa> result = new ArrayList<SubdocumentoIvaSpesa>();
		
		//Se ho il subdoc iva legato alla quota (subdoc) lo restituisco.
		if(subdoc.getSubdocumentoIva()!=null){
			result.add(subdoc.getSubdocumentoIva());
			return result;
		} 
		
		//Se non ho il subdocIva sulla quota (subdoc) allora carico quella del doc.
		caricaSubdocumentoIvaDelDocumento();
		
		return doc.getListaSubdocumentoIva();
		
	}

	private void caricaSubdocumentoIvaDelDocumento() {
		if(!subdocIvaDocumentoCaricato){
			//carico il subdocumentoIva dal db
			documentoSpesaDad.caricaModelDetails(doc, DocumentoSpesaModelDetail.SubdocumentoIva);
			subdocIvaDocumentoCaricato = true;
		}
	}
	

	private RegistrazioneMovFin cercaRegistrazionePrecedenteNonAnnullata(SubdocumentoSpesa subdoc, List<RegistrazioneMovFin> registrazioniPrecedenti, Ambito ambito) {
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
		log.debug(methodName, "Nessura registrazione precedente per subdoc [uid:"+subdoc.getUid()+"] con stato diverso da ANNULLATO per l'ambito "+ambito+". Returning false.");
		//return false;
		return null;
	}

	private void gestisciRegistrazioniGENNoteCreditoSpesaCollegate(boolean esistonoRegistrazioniPrecedenti, SubdocumentoSpesa subdoc, ElementoPianoDeiConti elementoPianoDeiConti, boolean isConIva, boolean isTipoIvaPromisqua, boolean isResiduo) {
		final String methodName = "gestisciRegistrazioniGENNoteCreditoSpesaCollegate";
		
		if(BigDecimal.ZERO.compareTo(subdoc.getImportoDaDedurreNotNull()) == 0) {
			log.info(methodName, "Il Subdocumento [uid:"+subdoc.getUid()+"] NON presenta importi da dedurre. Non verra inserita nessuna Registrazione NotaCredito.");
			return;
		}
		
		log.info(methodName, "Il Subdocumento [uid:"+subdoc.getUid()+"] presenta importi da dedurre. Gestisco la registrazione della notaCredito.");
		
		getListaNoteCreditoSpesaCollegateEsclusivamenteAlDocumento();
		
		if(noteCreditoSpesa.isEmpty()){
			log.info(methodName, "Il Documento [uid:"+doc.getUid()+"] non ha nessuna nota credito o ha note di credito che sono collegate anche ad altri documenti. In questo caso la gestione delle Registrazioni delle note viene saltata. ");
			return;
		}
		
		if(noteCreditoSpesa.size()>1) {
			log.info(methodName, "Il Documento [uid:"+doc.getUid()+"] presenta piu' di una nota credito collegata. In questo caso la gestione delle Registrazioni delle note viene saltata. ");
			return;
		}
		
		//Arrivato qui ho solo ed esattamente una nota credito.
		DocumentoSpesa notaCreditoSpesa = noteCreditoSpesa.get(0);
		documentoDad.aggiornaFlagContabilizzaGenPcc(notaCreditoSpesa, Boolean.TRUE);
		caricaSubdocumentiDellaNotaCredito(notaCreditoSpesa);
		if(notaCreditoSpesa.getListaSubdocumenti().size()!=1){
			log.info(methodName, "Il Documento [uid:"+doc.getUid()+"] presenta una nota credito collegata con piu' di una quota. In questo caso la gestione delle Registrazioni delle note viene saltata. ");
			return;
		}
		
		
		SubdocumentoSpesa subdocumentoSpesaNotaCredito = notaCreditoSpesa.getListaSubdocumenti().get(0); //e' sempre uno solo! checked da #caricaSubdocumentoDellaNotaCredito
		
		//L'aggiornametno e' per forza necessario perchè segue contestualmente quello del subdoc associato.
//		if(esistonoRegistrazioniPrecedenti && !isNecessarioAggiornamentoRegistrazioneSubdocumento(subdocumentoSpesaNotaCredito)){ //Se l'agg non fosse necessario non sarei arrivato qui.
//			log.info(methodName, "Il Subdocumento NotaCredito [uid:"+subdocumentoSpesaNotaCredito.getUid()+"] NON presenta modifiche che rendono necessario l'aggiornamento della Registrazione. "
//					+ "Le Registrazione NON verra' inserita. ");
//			return;
//		}
		
		if(!esisteNotaCreditoIvaSeAbbinatoAQuotaRilevanteIva(subdocumentoSpesaNotaCredito, subdoc)){
			log.info(methodName, "Il Subdocumento NotaCredito [uid:"+subdocumentoSpesaNotaCredito.getUid()+"] NON ha una corrispondente NotaCreditoIva. "
					+ "Le Registrazione NON verra' inserita. ");
			return;
		}
		
		Evento evento = registrazioneGENServiceHelper.determinaEventoNotaCredito(TipoCollegamento.SUBDOCUMENTO_SPESA, esistonoRegistrazioniPrecedenti, isConIva, isTipoIvaPromisqua, isResiduo);
		Evento eventoCEC = registrazioneGENServiceHelper.determinaEventoCassaEconomaleENotaCredito(TipoCollegamento.SUBDOCUMENTO_SPESA, esistonoRegistrazioniPrecedenti, true);
		
		RegistrazioneMovFin registrazioneMovFin = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subdocumentoSpesaNotaCredito, subdoc, elementoPianoDeiConti, Ambito.AMBITO_FIN);
		log.info(methodName, "Inserita registrazione per il Subdocumento NotaCredito [uid:"+subdocumentoSpesaNotaCredito.getUid()+"] relativa alla quota con uid: "+subdoc.getUid() + " con evento: "+evento.getCodice());
		res.getRegistrazioniMovFinNNcdInserite().add(registrazioneMovFin);
		
		//########### GSA ########### //SIAC-4298
		if(isDaRegistrareInGSA(subdoc)){
			log.debug(methodName, "Inserisco anche la registrazione per GSA per la NCD relativa  alla nota di credito [uid:"+subdocumentoSpesaNotaCredito.getUid()+"] relativa alla quota con uid: "+subdoc.getUid() + " con evento: "+evento.getCodice());
			RegistrazioneMovFin registrazioneMovFinNcdGSA = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(evento, subdocumentoSpesaNotaCredito, subdoc, elementoPianoDeiConti, Ambito.AMBITO_GSA);
			res.getRegistrazioniMovFinNcdGSAInserite().add(registrazioneMovFinNcdGSA);
		}
		
		//########### CEC ########### //SIAC-3251 //TODO in realta' deve essere l'evento della CR 3297
		if(isDaRegistrareInCEC()){
			log.debug(methodName, "Inserisco anche la registrazione per la Cassa Economale relativa alla nota di credito [uid:"+subdocumentoSpesaNotaCredito.getUid()+"]");
		    RegistrazioneMovFin registrazioneMovFinNcdCEC = registrazioneGENServiceHelper.inserisciRegistrazioneMovFin(eventoCEC, subdocumentoSpesaNotaCredito, subdoc , elementoPianoDeiConti, Ambito.AMBITO_FIN);  //no AMBITO_CEC!!
		    res.getRegistrazioniMovFinNcdCECInserite().add(registrazioneMovFinNcdCEC);
		}
		
		
		
		
	}

	/**
	 * Controlla che se uno degli importi da dedurre e' legato ad una o + quote rilevanti iva deve esistere anche la nota di credito iva abbinata.
	 * 
	 * @param notaCreditoSpesa
	 * @return
	 */
	private boolean esisteNotaCreditoIvaSeAbbinatoAQuotaRilevanteIva(SubdocumentoSpesa subdocNotaCreditoSpesa, SubdocumentoSpesa subdoc) {
		if(!Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA())){
			return true; //NON e' rilevante IVA: Non controllo esistenza NotaCreditoIva.
		}
		
		//Controlla la presenza della notaCreditoIva.
		
				//Il subdocIva e' collegato alla notaCreditoIva.
				//Il subdocIve e' collegato a subdoc xor doc.
				//quindi:
				//	(subdoc -> subdocIva - NcdIva)
				// 	xor
				//	(subdoc -> doc -> subdocIva -NcdIva)
		
		SubdocumentoIvaSpesa notaCreditoIva = subdocumentoSpesaDad.findNotaCreditoIvaAssociataUid(subdoc);
		if(notaCreditoIva != null) {
			return true;
		}
		
		// SIAC-3559: controllo anche se ho dei dati inva collegati direttamente alla nota di credito
		Long subdocIvaLegatiNCD = subdocumentoSpesaDad.countSubdocIvaCollegati(subdocNotaCreditoSpesa);
		return subdocIvaLegatiNCD != null && subdocIvaLegatiNCD.longValue() > 0L;
	}

	private void caricaSubdocumentiDellaNotaCredito(DocumentoSpesa notaCreditoSpesa) {
		
		if(!notaCreditoSpesa.getListaSubdocumenti().isEmpty()){
			//subdocumento (e' uno solo) gia' caricato.
			return;
		}
		
		//Subdocumento della notaCredito non caricato (e'uno solo). Lo carico da DB.
		List<SubdocumentoSpesa> listaSubdocumentiNcd = subdocumentoSpesaDad.findSubdocumentiSpesaByIdDocumento(notaCreditoSpesa.getUid(), 
				SubdocumentoSpesaModelDetail.ImpegnoSubimpegno,	//Serve sia alla registrazione GEN che PCC
				SubdocumentoSpesaModelDetail.Attr//,				//Serve sia alla registrazione GEN che PCC
				//SubdocumentoSpesaModelDetail.Liquidazione 		//Serve solo alla registrazione PCC
				);
		notaCreditoSpesa.setListaSubdocumenti(listaSubdocumentiNcd);
		
	}

	/**
	 *  Ottiene la lista delle  note di credito di entrata appartenenti esclusivamente al mio doc.
	 *  
	 * @return la lista
	 */
	private List<DocumentoSpesa> getListaNoteCreditoSpesaCollegateEsclusivamenteAlDocumento() {
		if(noteCreditoSpesa == null) {
			noteCreditoSpesa = documentoSpesaDad.ricercaNoteCreditoSpesaCollegateEsclusivamenteAlDocumento(doc.getUid()); //TODO Specificare i ModelDetail che servono!
		}
		return noteCreditoSpesa;
	}
	
	/**
	 *  Ottiene la lista delle  note di credito di entrata appartenenti esclusivamente al mio doc.
	 *  
	 * @return la lista
	 */
	private List<DocumentoSpesa> getListaNoteCreditoSpesaCollegateEsclusivamenteAlDocumentoOld() {
		if(noteCreditoSpesaOld == null) {
			noteCreditoSpesaOld = documentoSpesaDad.ricercaNoteCreditoSpesaCollegateEsclusivamenteAlDocumentoTxRequiresNew(doc.getUid()); //TODO Specificare i ModelDetail che servono!
		}
		return noteCreditoSpesaOld;
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
	private boolean isNecessarioAggiornamentoRegistrazioneSubdocumento(SubdocumentoSpesa subdoc) {
		if(subdoc.getDataCancellazione()!=null){
			//La quota e' stata cancellata, pertanto e' necessario aggiornare la registrazione
			//(che in questo caso verra' annullata)
			return true;
		}
		
		if(!cacheNecessarioAggiornamentoRegistrazioneSubdocumento.containsKey(subdoc.getUid())) {
			
			//Leggo il subdocumento in una nuova transazione. Soluzione NON ideale in caso di concorrenza! Ma accetto il side effect di un doppio aggiornamento 
			//nel caso in cui ho due thread sovrapposti.
			String keySubdocPrecedente = subdocumentoSpesaDad.computeKeySubdocImportoImpegnoFlagRilevanteIvaTxRequiresNew(subdoc.getUid()); //in new tx
			String keySubdocAttuale = subdocumentoSpesaDad.computeKeySubdocImportoImpegnoFlagRilevanteIva(subdoc.getUid()); //in this tx
			
			
			boolean necessarioAggiornamentoRegistrazione = !keySubdocAttuale.equals(keySubdocPrecedente);
			cacheNecessarioAggiornamentoRegistrazioneSubdocumento.put(subdoc.getUid(), necessarioAggiornamentoRegistrazione);
			return necessarioAggiornamentoRegistrazione;
		}
		
		return cacheNecessarioAggiornamentoRegistrazioneSubdocumento.get(subdoc.getUid());
		
		
		
	}
	
	
	private boolean isQuotaPagata(SubdocumentoSpesa subdoc){
		return !isQuotaNonPagata(subdoc);
	}
	
	private boolean isQuotaNonPagata(SubdocumentoSpesa subdoc){
		boolean isQuotaNonPagata = subdocumentoSpesaDad.isQuotaNonPagata(subdoc);
		return isQuotaNonPagata;
	}


	private ElementoPianoDeiConti determinaElementoPianoDeiConti(SubdocumentoSpesa subdoc) {
		final String methodName = "determinaElementoPianoDeiConti";
		
		//le quote "AZero" (isQuotaAZero==true) NON hanno per forza l'impegno o subImpegno quindi non posso ricavare l'elementoPianoDeiConti!
		Impegno impegnoOSubImpegno = subdoc.getImpegnoOSubImpegno();
		log.debug(methodName, "Uid "+(impegnoOSubImpegno!=null?"Impegno/SubImpegno da cui ricavare l'elementoPianoDeiConti: " + impegnoOSubImpegno.getUid():"") 
				+" subdoc.uid: "+subdoc.getUid());
		ElementoPianoDeiConti elementoPianoDeiConti = impegnoBilDad.findPianoDeiContiAssociatoAMovimentoGestione(subdoc);
		if(elementoPianoDeiConti==null || elementoPianoDeiConti.getUid() == 0) {
			log.error(methodName, "Impossibile reperire l'elementoPianoDeiConti in quanto non ho l'Impegno/SubImpegno della quota con uid: "+subdoc.getUid()
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
	private boolean esisteUnaRegistrazioneNonAnnullataPerLImpegnoAssociatoAllaQuota(SubdocumentoSpesa subdoc) {
		final String methodName = "esisteUnaRegistrazioneNonAnnullataPerLImpegnoAssociatoAllaQuota";
		Impegno impegnoOSubImpegno = subdoc.getImpegnoOSubImpegno();
		if(impegnoOSubImpegno==null){
			log.debug(methodName, "returning false. Impegno/SubImpegno non presente per la quota con uid: "+subdoc.getUid()); 
			return false;
		}
		TipoCollegamento tipoCollegamento = SiacDCollegamentoTipoEnum.byModelClass(impegnoOSubImpegno.getClass()).getTipoCollegamento();

		boolean result = ! CollectionUtils.isEmpty(
				registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(tipoCollegamento, impegnoOSubImpegno, 
				// SIAC-5996 
				Ambito.AMBITO_FIN));
		
		// SIAC-5813		
		if (!result && impegnoOSubImpegno.getAnnoMovimento() < bilancio.getAnno()) {
			Bilancio bilancioAnnoPrecedente = bilancioDad.getBilancioAnnoPrecedente(bilancio);
			
			Integer uidMovgest = impegnoOSubImpegno instanceof SubImpegno ?
					findUidMovgestByAnnoNumeroBilancio(subdoc.getImpegno(), (SubImpegno)impegnoOSubImpegno, bilancioAnnoPrecedente) :					
					findUidMovgestByAnnoNumeroBilancio(impegnoOSubImpegno, bilancioAnnoPrecedente);
			
			result =  uidMovgest != null && 
					! CollectionUtils.isEmpty(
							registrazioneGENServiceHelper.ricercaRegistrazioniMovFinBilancioPrecendenteAssociateAlMovimento(
								tipoCollegamento, 
								uidMovgest,
								bilancioAnnoPrecedente,
								Ambito.AMBITO_FIN));
		}
		
		log.debug(methodName, "returning "+result + ". tipoCollegamento: "+tipoCollegamento);

		return result; 
	}
	
	
	private boolean esisteUnaRegistrazioneNonAnnullataPerQuotaAnnoPrecedente(SubdocumentoSpesa subdoc) {
		return doc.getAnno() < bilancio.getAnno() && ! CollectionUtils.isEmpty(
				registrazioneGENServiceHelper.ricercaRegistrazioniMovFinBilancioPrecendenteAssociateAlMovimento(TipoCollegamento.SUBDOCUMENTO_SPESA, doc));
	}

	private Integer findUidMovgestByAnnoNumeroBilancio(Impegno impegno, Bilancio bilancioAnnoPrecedente) {
		return impegnoBilDad.findUidMovgestByAnnoNumeroBilancio(
				impegno.getAnnoMovimento(), 
				impegno.getNumero(),
				Impegno.class,
				bilancioAnnoPrecedente.getUid());
	}

	private Integer findUidMovgestByAnnoNumeroBilancio(Impegno impegno, SubImpegno subImpegno, Bilancio bilancioAnnoPrecedente) {
		return impegnoBilDad.findUidMovgestTsByAnnoNumeroBilancio(
				impegno.getAnnoMovimento(), 
				impegno.getNumero(), 
				String.valueOf(subImpegno.getNumero()),
				Impegno.class,
				bilancioAnnoPrecedente.getUid());
	}

	private boolean isDaRegistrareInGSA(SubdocumentoSpesa subdoc) {
		final String methodName = "isDaRegistrareInGSA";
		if(subdoc.getImpegno()!=null && subdoc.getImpegno().getUid()!= 0 ) {
			Boolean flagAttivaGsa = impegnoBilDad.getFlagAttivaGsa(subdoc.getImpegno().getUid()); 
			subdoc.getImpegno().setFlagAttivaGsa(flagAttivaGsa);
			log.debug(methodName, "flagAttivaGSA associato all'Impegno [uid:"+subdoc.getImpegno().getUid()+"]: " + subdoc.getImpegno().isFlagAttivaGsa());
			return subdoc.getImpegno().isFlagAttivaGsa();
		} 
		
		log.debug(methodName, "Impossibile reperire l'impegno associato alla quota con uid: "+subdoc.getUid()+ ". Returning false.");
		return true;
	}
	
	private boolean isDaRegistrareInCEC() {
		return Boolean.TRUE.equals(doc.getCollegatoCEC());
	}
	

	private List<SubdocumentoSpesa> getListaSubdocumenti() {
		if(listaSubdocumenti == null){
			listaSubdocumenti = subdocumentoSpesaDad.findSubdocumentiSpesaByIdDocumento(doc.getUid(), 
					SubdocumentoSpesaModelDetail.ImpegnoSubimpegno,	//Serve sia alla registrazione GEN che PCC
					SubdocumentoSpesaModelDetail.Attr,				//Serve sia alla registrazione GEN che PCC
					SubdocumentoSpesaModelDetail.Liquidazione, 		//Serve solo alla registrazione PCC
					SubdocumentoSpesaModelDetail.SubdocumentoIva    //Serve per determinare l'evento della registrazione GEN
					);
			//doc.setListaSubdocumenti(subdocumenti); //Attenzione! NON modificare l'oggetto in Request (ovvero "doc") per evitare retrocompatibilità con gli utilizzatori di questo servizio!
		}
		return listaSubdocumenti;
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


//	/**
//	 * Almeno una quota rilevante iva.
//	 *
//	 * @param ds the documentoSpesa
//	 * @return true se presente almeno una quota rilevante IVA
//	 */
//	private Boolean almenoUnaQuotaRilevanteIVA(DocumentoSpesa ds) {
//		Long numeroQuoteRilevantiIva = countQuoteRilevantiIva(ds);
//		return numeroQuoteRilevantiIva.compareTo(Long.valueOf(0)) > 0;
//	}

	
	/* ############################################ Attivazione PCC ############################################ */
	
	/**
	 * La prima comunicazione di contabilizzazione vs PCC deve essere registrata quando tutte le quote vengono associate ai relativi impegni
	 * (Tipo Operazione: <code>CONTABILIZZAZIONE</code>, Satto del debito:<code>SOSP</code>, Causale: <code>ATTILIQ</code>).
	 */
	private void gestisciPrimaRegistrazionePCC() {
		final String methodName = "gestisciPrimaRegistrazionePCC";
		
		if(BigDecimal.ZERO.compareTo(doc.getImporto()) == 0){
			log.info(methodName, "Documento [uid:" + doc.getUid() + "] con importo a zero, non inserisco la registrazione.");
			return;
		}
		
		if(!Boolean.TRUE.equals(doc.getContabilizzaGenPcc())){
			log.info(methodName, "Documento [uid:" + doc.getUid() + "] con flag ContabilizzaGenPcc = false, non inserisco la registrazione.");
			return;
		}
		
		if(!Boolean.TRUE.equals(doc.getTipoDocumento().getFlagComunicaPCC())){
			log.info(methodName, "Documento [uid:" + doc.getUid() + "] di tipo con FlagComunicaPCC = false, non inserisco la registrazione.");
			return;
		}
		
		if(Boolean.TRUE.equals(doc.getTipoDocumento().getFlagAttivaGEN()) && !isCondizioneDiAttivazioneGENSoddisfatta){
			log.info(methodName, "Documento [uid:" + doc.getUid() + "] ha anche il FlagAttivaGEN a true, non inserisco le registrazioni PCC se non sono state inserite le registrazioni GEN");
			res.setCondizioneDiAttivazionePCCSoddisfatta(Boolean.FALSE);
			return;
		}
		
		// Inizializzazione DAD
		registroComunicazioniPCCDad.setEnte(ente);
		registroComunicazioniPCCDad.setLoginOperazione(loginOperazione);
		
		if(!Boolean.TRUE.equals(res.getTutteLeQuoteSonoAssociateAImpegnoOSubImpegno())){
			log.info(methodName, "Registrazione PCC non attivabile: Non tutte le quote hanno un impegno o subimpegno associato.");
			return;
		}
		
		res.setCondizioneDiAttivazionePCCSoddisfatta(Boolean.TRUE);
		
		if(hasAlreadyAPCC(doc)){
			log.info(methodName, "Registrazione PCC saltata: Documento con una contabilizzazione PCC gia' associata");
			return;
		}
		
		// Attivo per ogni quota la registrazione PCC
		TipoOperazionePCC tipoOperazionePCC = registroComunicazioniPCCDad.findTipoOperazionePCCByValue(TipoOperazionePCC.Value.Contabilizzazione /*"CO"*/);
		StatoDebito statoDebitoSosp = registroComunicazioniPCCDad.findStatoDebitoByValue(StatoDebito.Value.EsigibilitaImportoSospesa /*"SOSP"*/);
		StatoDebito statoDebitoLiq = registroComunicazioniPCCDad.findStatoDebitoByValue(StatoDebito.Value.ImportoLiquidato); //Codice("LIQ");
		StatoDebito statoDebitoNoLiq = registroComunicazioniPCCDad.findStatoDebitoByValue(StatoDebito.Value.ImportoValutatoNonLiquidabile); //Codice("NOLIQ");
		CausalePCC causalePCCAttiliq = registroComunicazioniPCCDad.findCausalePCCByValue(CausalePCC.Value.AttesaLiquidazione);//Codice("ATTLIQ");
		CausalePCC causalePCCNcd = registroComunicazioniPCCDad.findCausalePCCByValue(CausalePCC.Value.NotaCredito);//Codice("ATTLIQ");
		boolean notaCredito = doc.getTipoDocumento().isNotaCredito();
		
		for(SubdocumentoSpesa ss : getListaSubdocumenti()) {
			boolean subdocConLiquidazione = ss.getLiquidazione() != null && ss.getLiquidazione().getUid() != 0;
			StatoDebito statoDebito = notaCredito ? statoDebitoNoLiq : (subdocConLiquidazione ? statoDebitoLiq : statoDebitoSosp);
			CausalePCC causalePCC = notaCredito ? causalePCCNcd : (subdocConLiquidazione ? null : causalePCCAttiliq);
			
			InserisciRegistroComunicazioniPCCResponse resIRCPCC = inserisciRegistroComunicazioniPCCService(ss, tipoOperazionePCC, statoDebito, causalePCC);
			res.addRegistrazioneComunicazioniPCC(resIRCPCC.getRegistroComunicazioniPCC());
		}
		
	}
	

//	private boolean hasAlreadyAPCC(SubdocumentoSpesa ss) {
//		Long operazioni = registroComunicazioniPCCDad.countOperazioniPccBySubdocumentoAndCodiceTipoOperazione(ss.getUid(), "CO");
//		return operazioni != null && operazioni.longValue() > 0L;
//	}
	
	private boolean hasAlreadyAPCC(DocumentoSpesa ds) {
		Long operazioni = registroComunicazioniPCCDad.countOperazioniPccByDocumentoAndCodiceTipoOperazione(ds.getUid(), TipoOperazionePCC.Value.Contabilizzazione /*"CO"*/);
		return operazioni != null && operazioni.longValue() > 0L;
	}
	
	private InserisciRegistroComunicazioniPCCResponse inserisciRegistroComunicazioniPCCService(SubdocumentoSpesa ss, TipoOperazionePCC tipoOperazionePCC, StatoDebito statoDebito, CausalePCC causalePCC) {
		final String methodName = "inserisciRegistrazionePCCPerSubdocumento";
		InserisciRegistroComunicazioniPCC reqIRCPCC = new InserisciRegistroComunicazioniPCC();
		
		reqIRCPCC.setDataOra(new Date());
		reqIRCPCC.setRichiedente(req.getRichiedente());
		
		RegistroComunicazioniPCC registroComunicazioniPCC = new RegistroComunicazioniPCC();
		registroComunicazioniPCC.setCausalePCC(causalePCC);
		registroComunicazioniPCC.setTipoOperazionePCC(tipoOperazionePCC);
		registroComunicazioniPCC.setEnte(doc.getEnte());
		registroComunicazioniPCC.setStatoDebito(statoDebito);
		reqIRCPCC.setRegistroComunicazioniPCC(registroComunicazioniPCC);
		
		// Imposto il minimo dei dati
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(doc.getUid());
		registroComunicazioniPCC.setDocumentoSpesa(documentoSpesa);
		
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(ss.getUid());
		subdocumentoSpesa.setDataScadenza(ss.getDataScadenza());
		registroComunicazioniPCC.setSubdocumentoSpesa(subdocumentoSpesa);
		
		InserisciRegistroComunicazioniPCCResponse resIRCPCC = serviceExecutor.executeServiceSuccess(InserisciRegistroComunicazioniPCCService.class, reqIRCPCC);
		log.debug(methodName, "Inserita comunicazione PCC con uid " + resIRCPCC.getRegistroComunicazioniPCC().getUid() + " per il subdocumento " + ss.getUid() + " e il documento " + doc.getUid()
				+ " (tipo operazione " + tipoOperazionePCC.getUid() + "-" + tipoOperazionePCC.getCodice() + ")"
				+ " (stato debito " + statoDebito.getUid() + "-" + statoDebito.getCodice() + ")"
				+ " (causale pcc " + (causalePCC != null ? causalePCC.getUid() + "-" + causalePCC.getCodice() : "null") + ")");
		
		
		return resIRCPCC;
	}
	
		
	
	
	protected void startElaborazioneRegistrazioni(List<? extends RegistrazioneMovFin> registrazioni) {
		String methodName = "startElaborazioneRegistrazioni";
		Map<String, String[]> mappaChiaviElaborazione = getMappaChiaviElaborazione(registrazioni);
		//SIAC-4906
		for(Entry<String, String[]> entry : mappaChiaviElaborazione.entrySet()) {
			try {
				elaborazioniManager.startElaborazioni(entry.getKey(), entry.getValue());
			} catch (ElaborazioneAttivaException eae){
				String msg = "L'elaborazione di alcune registrazioni contabili afferenti al documento "+doc.getDescAnnoNumeroUidTipoDocUidSoggettoStato()+" e' ancora in corso. Attendere il termine dell'elaborazione. ["+eae.getMessage()+" "+Arrays.toString(entry.getValue())+"]";
				log.error(methodName, msg, eae);
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
			}
		}
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
	
	/**
	 * End elaborazione registrazioni.
	 *
	 * @param registrazioni the registrazioni
	 */
	protected void endElaborazioneRegistrazioni(List<? extends RegistrazioneMovFin> registrazioni) {
		String methodName = "startElaborazioneRegistrazioni";
		Map<String, String[]> mappaChiaviElaborazione = getMappaChiaviElaborazione(registrazioni);
		//SIAC-4906
		for(Entry<String, String[]> entry : mappaChiaviElaborazione.entrySet()) {
			elaborazioniManager.endElaborazioni(entry.getKey(), entry.getValue());
		}
	}

	private String[] getElabKeys(List<? extends RegistrazioneMovFin> registrazioni) {
		List<String> elabKeys = new ArrayList<String>();
		for(RegistrazioneMovFin reg : registrazioni){
			elabKeys.add("RegistrazioneMovFin.uid:"+reg.getUid());
		}
		return elabKeys.toArray(new String[elabKeys.size()]);
	}
	
	
}

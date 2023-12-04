/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.ordinativi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.AggiornaStatoSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.AggiornaStatoSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.utility.ElaborazioniAttiveKeyHandler;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CodiceBolloDad;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.ContoCorrenteDad;
import it.csi.siac.siacbilser.integration.dad.ContoTesoreriaDad;
import it.csi.siac.siacbilser.integration.dad.DistintaBilDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.LiquidazioneBilDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCommissioneTipoEnum;
import it.csi.siac.siacbilser.integration.exception.ElaborazioneAttivaException;
import it.csi.siac.siacbilser.integration.utility.ElaborazioniManager;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siacbilser.model.ElabKeys;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.exception.AlreadyElaboratedException;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.messaggio.MessaggioCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SospensioneSubdocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.business.service.movgest.RicercaAccertamentoPerChiaveOttimizzatoService;
import it.csi.siac.siacfinser.business.service.ordinativo.InserisceOrdinativoIncassoService;
import it.csi.siac.siacfinser.business.service.ordinativo.InserisceOrdinativoPagamentoService;
import it.csi.siac.siacfinser.cig.exception.CigException;
import it.csi.siac.siacfinser.cig.helper.CigHelper;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaDisponibilitaALiquidare;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaDisponibilitaALiquidareResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.oil.AccreditoTipoOilIsPagoPADad;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.TransazioneElementare;
import it.csi.siac.siacfinser.model.codifiche.CodiceBollo;
import it.csi.siac.siacfinser.model.codifiche.CommissioneDocumento;
import it.csi.siac.siacfinser.model.codifiche.NoteTesoriere;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione.StatoOperativoLiquidazione;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.StatoOperativoOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo.TipoAssociazioneEmissione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ordinativo.RegolarizzazioneProvvisorio;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativo;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaLiquidazioneK;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;


/**
 * Service base per l'emissione ordinativi.
 *
 * @author Domenico
 * @author Marchino Alessandro
 * @author Valentina   
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class EmetteOrdinativiDaElencoBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {

	/** La lunghezza massima per il campo descrizione come impostato su db. */
	protected static final int MAX_DESCRIZIONE_CHARS = 500;
	
	/** MathContext per il troncamento alla seconda cifra decimale */
	protected static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.DOWN);
	
	//Services...
	@Autowired
	protected LiquidazioneService liquidazioneService;
	@Autowired
	protected MovimentoGestioneService movimentoGestioneService;
	@Autowired
	protected SoggettoService soggettoService;
	@Autowired
	protected AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaService;
	@Autowired
	protected AggiornaStatoSubdocumentoIvaSpesaService aggiornaStatoSubdocumentoIvaSpesaService;
	@Autowired
	protected AggiornaStatoDocumentoDiEntrataService aggiornaStatoDocumentoDiEntrataService;
	@Autowired
	protected AggiornaStatoSubdocumentoIvaEntrataService aggiornaStatoSubdocumentoIvaEntrataService;
	@Autowired
	protected InserisceQuotaDocumentoEntrataService inserisceQuotaDocumentoEntrataService;
	@Autowired
	protected InserisceQuotaDocumentoSpesaService inserisceQuotaDocumentoSpesaService;
	
	//DADs...
	@Autowired
	protected SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	protected SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired
	private ContoTesoreriaDad contoTesoreriaDad;
	@Autowired
	protected BilancioDad bilancioDad;
	@Autowired
	protected SubdocumentoDad subdocumentoDad;
	@Autowired
	protected DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	protected DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	protected ContoCorrenteDad contoCorrenteDad;
	@Autowired
	protected CodiceBolloDad codiceBolloDad;
	@Autowired
	protected DistintaBilDad distintaBilDad;
	@Autowired
	private ElaborazioniManager elaborazioniManager;
	@Autowired
	private CodificaDad codificaDad;
	@Autowired
	private LiquidazioneBilDad liquidazioneBilDad;
	
	//SIAC-8853
	@Autowired
	private AccreditoTipoOilIsPagoPADad AccreditoTipoOilIsPagoPADad;
	
	//Fields...
	protected Bilancio bilancio;
	protected it.csi.siac.siacfin2ser.model.ContoTesoreria contoTesoreria;
	protected Distinta distinta;
	protected String note;
	protected CommissioniDocumento commissioniDocumento;
	protected it.csi.siac.siacfin2ser.model.CodiceBollo codiceBollo;
	protected Date dataScadenza;
	protected Boolean flagNoDataScadenza;

	protected Map<Integer,Liquidazione> liquidazioniCache = new HashMap<Integer,Liquidazione>();
	protected Map<Integer,Accertamento> accertamentiCache = new HashMap<Integer,Accertamento>();
	protected Map<Integer,DocumentoSpesa> documentiSpesaCache = new HashMap<Integer,DocumentoSpesa>();
	protected Map<Integer,DocumentoEntrata> documentiEntrataCache = new HashMap<Integer,DocumentoEntrata>();
	protected Map<Integer,Soggetto> soggettiCache = new HashMap<Integer,Soggetto>();
	
	//SIAC-5937
	protected boolean bilancioInDoppiaGestione = false;
	protected Bilancio bilancioAnnoSuccessivo;
	//SIAC-6175
	protected Boolean flagDaTrasmettere;
	//SIAC-6206
	protected ClassificatoreStipendi classificatoreStipendi;
	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*--------------------------------------- RICERCHE E FILTRI PRELIMINARI -------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	@Override
	protected void init() {
		super.init();
		
		documentoSpesaDad.setEnte(ente);
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoSpesaDad.setEnte(ente);
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		documentoEntrataDad.setEnte(ente);
		documentoEntrataDad.setLoginOperazione(loginOperazione);
		subdocumentoEntrataDad.setEnte(ente);
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
		contoCorrenteDad.setEnte(ente);
		elaborazioniManager.init(ente, loginOperazione);
		distintaBilDad.setEnte(ente);
	}
	
	/**
	 * Carica i dati del bilancio.
	 */
	protected void caricaBilancio(){
		this.bilancio = bilancioDad.getBilancioByUid(bilancio.getUid());
		//SIAC-5937		
		if(this.bilancio.getFaseEStatoAttualeBilancio() != null && FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(this.bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio())) {
			//lo stato del bilancio e' predisposizione consuntivo, una delle precondizioni della doppia gestione: carico il bilancio dell'anno successivo per vedere se anche le altre condizioni sono soddisfatte
			this.bilancioAnnoSuccessivo = bilancioDad.getBilancioAnnoSuccessivo(this.bilancio);
			this.bilancioInDoppiaGestione = isBilancioInDoppiaGestione();
		}
		
	}
	
	/**
	 * Carica il codice del bollo, dalla request arriva solo l'uid
	 */
	protected void caricaCodiceBollo() {
		if(codiceBollo == null){
			return;
		}
		codiceBollo = codiceBolloDad.findCodiceBolloByUid(codiceBollo.getUid());
	}
	
	/**
	 * Carica il codice della distinta, dalla request arriva solo l'uid
	 */
	protected void caricaDistinta() {
		if(distinta == null){
			return;
		}
		distinta = distintaBilDad.findDistintaByUid(distinta.getUid());
	}
	
	/**
	 * Carica il codice del conto del tesoriere, dalla request arriva solo l'uid
	 */
	protected void caricaContoTesoreria() {
		if(contoTesoreria == null){
			return;
		}
		ContoTesoreria conto = contoTesoreriaDad.findByUid(contoTesoreria.getUid());
		contoTesoreria = contoTesoreriaBilFromContoTesoreriaFin(conto);
	}
	
	/**
	 *  Carica i dettagli della liquidazione di una quota
	 * @param subdocumentoSpesa
	 * @return liquidazione la liquidazione trovata
	 */
	protected Liquidazione caricaLiquidazione(SubdocumentoSpesa subdocumentoSpesa) {
		if(subdocumentoSpesa.getLiquidazione() == null || subdocumentoSpesa.getLiquidazione().getUid() == 0){
			return null;
		}
		Liquidazione liquidazione = liquidazioniCache.get(subdocumentoSpesa.getLiquidazione().getUid());
		if(liquidazione == null){
			liquidazione = ricercaLiquidazionePerChiave(subdocumentoSpesa.getLiquidazione());
			liquidazioniCache.put(subdocumentoSpesa.getLiquidazione().getUid(),liquidazione);
		}
		return liquidazione;
	}
	
	/**
	 * Carica i dettagli dell'accertamento di una quota
	 * 
	 * @param subdocumentoEntrata
	 * @return accertamento l'accertamento trovato
	 */
	protected Accertamento caricaAccertamento(SubdocumentoEntrata subdocumentoEntrata) {
		if(subdocumentoEntrata.getAccertamento() == null || subdocumentoEntrata.getAccertamento().getUid() == 0){
			return null;
		}
		Accertamento accertamento = accertamentiCache.get(subdocumentoEntrata.getAccertamento().getUid());
		if(accertamento == null){
			accertamento = ricercaAccertamentoPerChiave(subdocumentoEntrata.getAccertamento());
			accertamentiCache.put(subdocumentoEntrata.getAccertamento().getUid(),accertamento);
		}
		return accertamento;
	}

	/**
	 * Filtra i subdocumenti che hanno il flagOrdinativoSingolo con lo stesso valore passato come parametro.
	 * <br/>
	 * Nota: i documenti con subordinati o con ritenute vengono gestiti in modo da forzare questo flag a TRUE.
	 * 
	 * @param subdocumenti          i subdocumenti
	 * @param flagOrdinativoSingolo il lag tramite cui filtrare
	 * 
	 * @return i subdocumenti filtrati per flag
	 */
	protected <S extends Subdocumento<D, ?>, D extends Documento<S, ?>> List<S> filtraQuoteConFlagOrdinativoSingolo(List<S> subdocumenti, Boolean flagOrdinativoSingolo) {
		final String methodName = "filtraQuoteConFlagOrdinativoSingolo";
		log.debug(methodName, "Ricerca quote con flag ordinativo singolo pari a " + flagOrdinativoSingolo);
		List<S> result = new ArrayList<S>();
		for(S subdoc : subdocumenti){
			if(conSubordinatiORitenute(subdoc.getDocumento()) || conImpegnoGSA(subdoc.getUid())) {
				log.debug(methodName, "Forzo il flag a TRUE per il subdocumento " + subdoc.getUid());
				//forza il flag a TRUE per i documenti con subordinati o ritenute.
				subdoc.setFlagOrdinativoSingolo(Boolean.TRUE);
			}
			
			if(flagOrdinativoSingolo.equals(subdoc.getFlagOrdinativoSingolo())) {
				log.debug(methodName, "Subdocumento " + subdoc.getUid() + " con flagOrdinativoSingolo = " + flagOrdinativoSingolo + " come desiderato");
				result.add(subdoc);
			}
		}
		return result;
	}

	private boolean conImpegnoGSA(Integer uidSubdoc) {
		Boolean flagAttivaGsaImpegnoCollegato = subdocumentoSpesaDad.getFlagAttivaGsaImpegnoCollegato(uidSubdoc);
		return Boolean.TRUE.equals(flagAttivaGsaImpegnoCollegato);
	}

	/**
	 * Restituisce true sei il documento passato come parametro ha subordinati o ritenute.
	 * 
	 * @param documento il documento da controllare
	 * @return <code>true</code> se il documento ha subordinati o ritenute; <code>false</code> in caso contrario
	 */
	private <D extends Documento<?, ?>> boolean conSubordinatiORitenute(D documento) {
		final String methodName = "conSubordinatiORitenute";
		log.debug(methodName, "Controllo se il documento con id " + documento.getUid() + " abbia ritenute o documenti subordinati. ");
		return hasRitenute(documento) || hasSubordinati(documento);
	}
	
	/**
	 * Controlla se il documento abbia documenti subordinati.
	 *
	 * @param <D> the generic type
	 * @param documento the documento
	 * @return true,se il documento ha documenti collegati, false altrimenti
	 */
	protected <D extends Documento<?, ?>> boolean hasSubordinati(D documento) {
		return !documento.getListaDocumentiSpesaFiglio().isEmpty() || !documento.getListaDocumentiEntrataFiglio().isEmpty();
	}
	
	/**
	 * Controlla se il documento abbia le ritenute.
	 * 
	 * @param documento il documento
	 * 
	 * @return se le ritenute ci siano o meno
	 */
	protected <D extends Documento<?, ?>> boolean hasRitenute(D documento) {
		final String methodName = "hasRitenute";
		log.debug(methodName, "Documento con uid: " + documento.getUid() + ". Ha ritenute? false");
		return false;
	}
	
	/**
	 * Find sub ordinativo associato alla quota unica.
	 *
	 * @param <SO> the generic type
	 * @param elencoSubOrdinativi the elenco sub ordinativi
	 * @param subdoc the subdoc
	 * @return the so
	 */
	protected <SO extends SubOrdinativo> SO findSubOrdinativoAssociatoAllaQuotaUnica(List<SO> elencoSubOrdinativi,
			Subdocumento<?,?> subdoc) {
		
		for (SO subOrdinativo : elencoSubOrdinativi) {
			
			if(subOrdinativo instanceof SubOrdinativoPagamento){
				//((SubOrdinativoPagamento)subOrdinativo).getLiquidazione().setSubdocumentoSpesa((SubdocumentoSpesa)SerializationUtils.clone(subdoc)); //infinite deep!
				((SubOrdinativoPagamento)subOrdinativo).getLiquidazione().setSubdocumentoSpesa((SubdocumentoSpesa)SerializationUtils.clone(subdoc)); //infinite deep!
			}
			//Dovrebbe esserci un solo subOrdinativo
			return subOrdinativo;
		}
		throw new IllegalStateException("Impossibile trovare il subOrdinativo legato al subDocumento [uid:"+ subdoc.getUid()+"] ");
	}
	
	/**
	 * Verifica che sussistano le condizioni per l'emissione dell'ordinativo. 
	 * Quindi per reperire i dati si effettua una ricerca attraverso l'operazione 'ricercaLiquidazionePerChiave'
	 * del servizio SPES005 Servizio Gestione Liquidazione del modulo FIN.
	 * <br/>
	 * Vengono scartate tutte le quote lette o passate in input per le quali si verifica una delle condizioni che seguono:
	 * <ul>
	 *     <li>statoOperativoLiquidazione <> VALIDO</li>
	 *     <li>TipoConvalida <> da quello passato in input</li>
	 *     <li>AttoAmministrativo collegato alla liquidazione con StatoOperativo <> D-DEFINITIVO</li>
	 *     <li>disponibilitaPagare =0</li>
	 *     </li>il Subdocumento collegato alla liquidazione appartiene a un Documento di spesa che risulta avere
	 *         un'associazione padre-figlio attiva in cui ricopre il ruolo di figlio (vedi TipoAssociazione)
	 *         e il cui padre è un Documento di entrata. Queste quote verranno emesse a partire dal documento padre,
	 *         ovvero verranno elaborate dall'emettitore di Ordinativi di incasso. Si cita a titolo esemplificativo il
	 *         documento di tipo AGGIO che ha normalmente una relazione in cui la Spesa è subordinata all'Entrata</li>
	 *     <li> CR 3606 Se il Subdocumento è SOSPESO (pagamentoSospeso = TRUE) deve essere scartato con il la motivazione Pagamento documento sospeso</li>
	 *     <li> SIAC-6261 on possono essere pagate liquidazioni di soggetto con DURC scaduto </li>    
	 * </ul>
	 *
	 * @param subdoc the subdocumento
	 */
	protected void checkOrdinativoEmettibile(SubdocumentoSpesa subdoc, Boolean flagConvalidaManuale) {
		
		Liquidazione liq = subdoc.getLiquidazione();
		//aggiungo un controllo, anche se non è in analisi: la liquidazione deve esistere e deve avere il capitolo!
		if(liq == null){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdoc.getNumero() + " [uid: "+subdoc.getUid()+"] del documento: "+subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " non collegato a liquidazione."));
		}
		if(liq.getCapitoloUscitaGestione() == null){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdoc.getNumero() + " [uid: "+subdoc.getUid()+"] del documento: "+subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " non collegato ad un capitolo."));
		}

		// statoOperativoLiquidazione <> VALIDO
		if (!StatoOperativoLiquidazione.VALIDO.equals(liq.getStatoOperativoLiquidazione())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Liquidazione [uid: " + liq.getUid() + "]",
					liq.getStatoOperativoLiquidazione()));
		}

		// TipoConvalida <> da quello passato in inpu
		if (flagConvalidaManuale != null && subdoc.getFlagConvalidaManuale() != null && !flagConvalidaManuale.equals(subdoc.getFlagConvalidaManuale())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("flag convalida manuale incongruente con subdocumento " + subdoc.getNumero()
					+ " [uid: " + subdoc.getUid() + "] del documento: " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc()));
		}

		AttoAmministrativo aa = subdoc.getAttoAmministrativo();
		// TODO: Se l'atto amministrativo non esiste?
		if (aa == null) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento [uid: " + subdoc.getUid() + "]", "privo di provvedimento"));
		}
		// AttoAmministrativo collegato alla liquidazione con StatoOperativo <> D-DEFINITIVO
		if (!StatoOperativoAtti.DEFINITIVO.equals(aa.getStatoOperativoAtti())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("stato operativo atto +" + aa.getAnno() + "/" + aa.getNumero() + " non " + StatoOperativoAtti.DEFINITIVO.getDescrizione()
					+ ". Subdocumento " + subdoc.getNumero() + " [uid: " + subdoc.getUid() + "] del documento: " + subdoc.getDocumento().getDescAnnoNumeroTipoDoc()));
		}
		// disponibilitaPagare =0
		if (BigDecimal.ZERO.compareTo(liq.getDisponibilitaPagare()) == 0) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("disponibilita pagare 0 per la liquidazione: " + liq.getAnnoLiquidazione() + "/" + liq.getNumeroLiquidazione()
					+ " [uid: " + liq.getUid() + "] del subdocumento " + subdoc.getNumero() + " [uid: " + subdoc.getUid() + "] del documento: "
					+ subdoc.getDocumento().getDescAnnoNumeroTipoDoc()));
		}
		// il Subdocumento collegato alla liquidazione appartiene a un Documento di spesa [...] e il cui padre è un Documento di entrata
		long numeroDocumentiEntrataPadre = contaNumeroDocumentiEntrataPadre(subdoc);
		if (numeroDocumentiEntrataPadre > 0L) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento [uid: " + subdoc.getUid() + "] " + subdoc.getNumero() + " [uid: " + subdoc.getUid() + "] del documento: "
					+ subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " collegato ad un documento spesa figlio di un documento entrata."));
		}
		//CR 3606
		//	Se il Subdocumento è SOSPESO (pagamentoSospeso = TRUE) deve essere scartato con il la motivazione Pagamento documento sospeso
		if(controllaPagamentoSospeso(subdoc)){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento [uid: " + subdoc.getUid() + "] " + subdoc.getNumero() + " [uid: " + subdoc.getUid() + "] del documento: "
					+ subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " con pagamento documento sospeso."));
		}
		
		if(subdoc.getLiquidazione().getModalitaPagamentoSoggetto()==null){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento [uid: " + subdoc.getUid() + "] " + subdoc.getNumero() + " [uid: " + subdoc.getUid() + "] del documento: "
					+ subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " con modalita' di pagamento non valorizzata."));
		}
		
		//Controllo ABI e CAB
		if(!isIbanValido(subdoc.getLiquidazione().getModalitaPagamentoSoggetto().getIban())){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento [uid: " + subdoc.getUid() + "] " + subdoc.getNumero() + " [uid: " + subdoc.getUid() + "] del documento: "
					+ subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " con ABI/CAB inesistenti o non validi."));
		}
		// SIAC-6080: controllo validita' modalita' di pagamento
		if(!isModalitaPagamentoValida(subdoc.getLiquidazione().getModalitaPagamentoSoggetto())) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento [uid: " + subdoc.getUid() + "] " + subdoc.getNumero() + " [uid: " + subdoc.getUid() + "] del documento: "
					+ subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " modalita' di pagamento non VALIDA."));
		}
		
		
		if(!datiIvaValidi(subdoc)){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento [uid: " + subdoc.getUid() + "] " + subdoc.getNumero() + " [uid: " + subdoc.getUid() + "] del documento: "
					+ subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " ha dati iva associati ma non e' rilevante iva"));
		}
		
		checkDatiDurc(subdoc.getLiquidazione().getImpegno(), subdoc.getLiquidazione().getSoggettoLiquidazione(),subdoc.getLiquidazione().getModalitaPagamentoSoggetto().getSoggettoCessione());
		
		//SIAC-5311
		checkDatiSiope(liq);
		
		//SIAC-6840
		//controllo che il metodo di pagamento sia "AVVISO PAGOPA" ed il campo "Codice avviso PagoPA" sia valorizzato
		//SIAC-8853
		checkBusinessCondition(!(StringUtils.isBlank(subdoc.getDocumento().getCodAvvisoPagoPA()) && isModalitaPagamentoPagoPa(subdoc)), 
				ErroreFin.COD_AVVISO_PAGO_PA_ASSENTE.getErrore("subdocumento " + subdoc.getNumero() + " [uid: "+subdoc.getUid()+"] del documento: "+subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + "."));
		
	}
	
	//SIAC-8853
	private boolean isModalitaPagamentoPagoPa(SubdocumentoSpesa subdoc) {
		String tipoAccreditoNuovoSubdocumento = subdoc.getModalitaPagamentoSoggetto().getModalitaAccreditoSoggetto().getCodice();
	    return tipoAccreditoNuovoSubdocumento != null && AccreditoTipoOilIsPagoPADad.accreditoTipoOilIsPagoPA(subdoc.getEnte().getUid(), tipoAccreditoNuovoSubdocumento);
	}

	private void checkDatiDurc(Impegno impegno, Soggetto soggettoLiquidazione, Soggetto soggettoCessione) {
		final String methodName ="checkDatiDurc";
		if(impegno == null || !impegno.isFlagSoggettoDurc()) {
			log.debug(methodName, "l'impegno non richiede controlli sul DURC. ");
			return;
		}
		//TODO: controllare caricamento soggetto
		boolean soggettoCessioneValorizzato = soggettoCessione != null && StringUtils.isNotBlank(soggettoCessione.getCodiceSoggetto()); 
		Date dataFineValiditaDurc = soggettoCessioneValorizzato ? soggettoCessione.getDataFineValiditaDurc() : soggettoLiquidazione.getDataFineValiditaDurc();
		Date now = new Date();
		
		//SIAC-7143
		String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(now);
		String dateFineDurc = dataFineValiditaDurc!= null?  new SimpleDateFormat("yyyy-MM-dd").format(dataFineValiditaDurc) : null;
		
		if(dateFineDurc != null && dateNow.compareTo(dateFineDurc) > 0) {
			StringBuilder descrizioneSoggettoCessione = new StringBuilder();
			if(soggettoCessioneValorizzato) {
				descrizioneSoggettoCessione.append(" soggetto ")
					.append(StringUtils.defaultIfBlank(soggettoCessione.getCodiceSoggetto(), "null"))
					.append(" ricevente del soggetto ");
			}
			String messaggioErrore = new StringBuilder()
					.append("Durc scaduto soggetto ")
					.append(descrizioneSoggettoCessione.toString())
					.append(StringUtils.defaultIfBlank(soggettoLiquidazione.getCodiceSoggetto(), "null"))
					.append(", pagamento liquidazione non effettuato.")
					.toString();
			log.debug(methodName, "Controllo non superato: " + messaggioErrore);
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(messaggioErrore));
		}
	}

	/**
	 *  Controlla i dati siope + della liquidazione. Affinch&grave; una qupta possa essere emessa, devo valere le seguenti condizioni sulla liquidazione ad essa associata.
	 *  <ul>
	 *  <li>Cig e motivazione assenza cig non possosno coesistere</li>
	 *  <li>Se tipoDebitoSiope == COMMERCIALE il Cig o , in alternativa, motivazione assenza cig sono obbligatori</li>
	 *  <li>tipo debito siope deve essere valorizzato.</li>
	 *  </ul>
	 *
	 * @param liq la liquidazione su cui effettuare i controlli
	 */
	private void checkDatiSiope(Liquidazione liq) {
		final String methodName = "";
		SiopeTipoDebito siopeTipoDebito = ottieniSiopeTipoDebito(liq);
		if(siopeTipoDebito == null || siopeTipoDebito.getUid() == 0) {
			log.debug(methodName, "siopeTipoDebito null");
			//tipo debito siope obbligatorio
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("Liquidazione", "dati SIOPE+ incongruenti"));
		}
		boolean isCigValorizzato = StringUtils.isNotEmpty(liq.getCig());
		boolean isMotivoAssenzaCigPresente = ottieniSiopeAssenzaMotivazione(liq) != null && ottieniSiopeAssenzaMotivazione(liq).getUid() != 0;
		
		//cig e motivazione assenza cig non possono coesistere
		if(isCigValorizzato && isMotivoAssenzaCigPresente) {
			log.debug(methodName, "cig e motivo assenza cig entrambi valorizzati");
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("Liquidazione", "dati SIOPE+ incongruenti"));
		}
		//TODO: valutare se mettere un enum al posto della stringa schiantata
		
		// se siope commercialecig o, in alternativa assenza cig sono obbligatori
		if("CO".equals(siopeTipoDebito.getCodice()) && !(isCigValorizzato || isMotivoAssenzaCigPresente)) {
			log.debug(methodName, "siope commerciale e cig o motivo assenza cig non valorizzati");
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("Liquidazione", "dati SIOPE+ incongruenti"));
		}
		
		//SIAC-8208 controlli formali sul CIG
		controlliFormaliSulCig(liq);
	}

	//SIAC-8208
	private void controlliFormaliSulCig(Liquidazione liq) {
		try {
			CigHelper.controlloCIGSuLiquidazione(liq, true);
		} catch (CigException ce) {
			log.debug("controlliFormaliSulCig", ce.getErrore().getDescrizione());
			throw new BusinessException(ce.getErrore());
		}
	}
	
	private boolean datiIvaValidi(SubdocumentoSpesa subdoc) {
		if(subdoc.getSubdocumentoIva() != null && !Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA())){
			return false;
		}
		return subdoc.getDocumento().getListaSubdocumentoIva() == null
				|| subdoc.getDocumento().getListaSubdocumentoIva().isEmpty()
				|| esisteQuotaRilevanteIva(subdoc.getDocumento());
	}
	
	private boolean esisteQuotaRilevanteIva(DocumentoSpesa documento) {
		Long quoteRilevantiIva = documentoSpesaDad.countQuoteRilevantiIva(documento);
		return quoteRilevantiIva.compareTo(Long.valueOf(0)) != 0;
	}
	
	/**
	 * Indica se il soggetto è stato sospeso per il pagamento:
	 * vale SI se il documento di spesa 'padre' ha i dati di sospensione valorizzati e la data di riattivazione=NULL,
     * oppure se il soggetto è stato sospeso a livello di attoAllegato abbinato alla quota, quindi anche in questo caso ha i dati di sospensione valorizzati e la data di riattivazione=NULL.
	 * Inoltre con l'introduzione dei dati di sospensione sulla singola quota, 
     * il pagamento della stessa dovrà essere sospeso anche se i suoi dati di sospensione sono valorizzati e la sua data di riattivazione=NULL
	 *
	 */
	private boolean controllaPagamentoSospeso(SubdocumentoSpesa subdoc) {
		//TODO: questo volendo si puo' leggermente ottimizzare considerando che c'e' una or al fondo e facendo la chiamata al dad solo quando le altre due sono false
		final String methodName = "controllaPagamentoSospeso";
		//se il documento di spesa 'padre' ha i dati di sospensione valorizzati e la data di riattivazione=NULL,
		boolean subdocSospeso = false;
		List<SospensioneSubdocumento> sospensioni = subdocumentoSpesaDad.findSospensioni(subdoc);
		for(Iterator<SospensioneSubdocumento> it = sospensioni.iterator(); it.hasNext() && !subdocSospeso;) {
			SospensioneSubdocumento ss = it.next();
			subdocSospeso =  ss.getDataSospensione() !=null && ss.getCausaleSospensione() !=null && ss.getDataRiattivazione() == null;
		}
		
		boolean isSoggettoAllegatoSospeso = false;
	
		//se il soggetto è stato sospeso a livello di attoAllegato abbinato alla quota, quindi anche in questo caso ha i dati di sospensione valorizzati e la data di riattivazione=NULL.
		isSoggettoAllegatoSospeso = subdocumentoSpesaDad.isSoggettoAttoSubdocSospeso(subdoc, subdoc.getDocumento().getSoggetto().getUid());
		
		log.debug(methodName, "subdocSospeso : "+subdocSospeso);
		log.debug(methodName, "isSoggettoAllegatoSospeso : "+isSoggettoAllegatoSospeso);

		return subdocSospeso ||  isSoggettoAllegatoSospeso;
	}

	private boolean isIbanValido(String iban) {
		String methodName = "isIbanValido";
		log.debug(methodName, "iban trovato: " + iban);
		if(StringUtils.isBlank(iban)){
			//se non ho l'iba non ho nulla da verificare
			log.debug(methodName, "l'iban non e' presente per la modalita' di pagamento selezionata, non devo verificarlo");
			return true;
		}
		if(iban.length()<2){
			//l'iban c'e' ma non posso determinatre se sia taliano o meno
			log.debug(methodName, "l'iban per la modalita' di pagamento selezionata e' presente ma ha un formato non accettabile");
			return false;
		}
		if(!"IT".equals(iban.substring(0, 2))){
			//l'iban c'e' ma non e' italiano, non devo verificare nulla
			log.debug(methodName, "l'iban per la modalita' di pagamento selezionata non e' italiano, non devo verificarlo");
			return true;
		}
		if(iban.length()<15){
			log.debug(methodName, "l'iban per la modalita' di pagamento selezionata e' italiano non valido ma non contiene tutti i caratteri necessari per la verifica"); 
			return false;
		}
		String abi = iban.substring(5,10);
		String cab = iban.substring(10, 15);
		log.debug(methodName, "abi trovato: " + abi);
		log.debug(methodName, "cab trovato: " + cab);
		boolean abiValido = contoCorrenteDad.verificaValiditaAbi(abi);
		boolean cabValido = contoCorrenteDad.verificaValiditaCab(abi, cab);
		return abiValido && cabValido;
	}
	
	/**
	 * <strong>SIAC-6080</strong>
	 * <p>
	 * Si richiede che fase di emissione di un ordinativo di pagamento l'emettitore controlli che la modalit&agrave; di pagamento del soggetto sia valida (non annullata, sospesa, bloccata).
	 * </p>
	 * <p>
	 * La funzionalit&agrave; di inserimento diretto degli ordinativi di pagamento ha un controllo sulla app che non consente l'inserimento dell'ordinativo in caso di mdp non valida.
	 * </p>
	 * @param modalitaPagamentoSoggetto la modalit&agrave; di pagamento da controllare
	 * @return se la modalit&agrave; sia VALIDA
	 */
	private boolean isModalitaPagamentoValida(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		final String codiceValido = "VALIDO";
		if(modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2() != null && modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2().getUid() != 0) {
			// Il codice della relazione e' nella descrizione dello stato
			return codiceValido.equalsIgnoreCase(modalitaPagamentoSoggetto.getDescrizioneStatoModalitaPagamento())
					// Nel caso della modalita' a cui e' effettuata la cessione, il codice e' nel codice
					&& codiceValido.equalsIgnoreCase(modalitaPagamentoSoggetto.getModalitaPagamentoSoggettoCessione2().getCodiceStatoModalitaPagamento());
		}
		
		// SIAC-6489
		ModalitaPagamentoSoggetto mdp = liquidazioneBilDad.findModalitaPagamentoById(modalitaPagamentoSoggetto.getUid());
		boolean mdpCancellata = mdp.getDataCancellazione() != null;
		
		return codiceValido.equalsIgnoreCase(modalitaPagamentoSoggetto.getCodiceStatoModalitaPagamento()) && ! mdpCancellata;
	}
	

	/**
	 * Verifica che sussistano le condizioni per l'emissione dell'ordinativo. 
	 * Quindi si effettua una ricerca attraverso l'operazione 'Ricerca dettaglio Documento Entrata '
	 * del servizio ENTS004 - Servizio Gestione Documento di Entrata.
	 * <br/>
	 * Vengono scartate le quote  lette o passate in input  per le quali si verifica una delle condizioni che seguono:
	 * <ul>
	 *     <li>StatoOperativoDocumento  A – ANNULLATO o S-STORNATO o E - EMESSO</li>
	 *     <li>TipoConvalida <> da quello passato in input</li>
	 *     <li>AttoAmministrativo collegato alla quota con StatoOperativo <> D-DEFINITIVO</li>
	 *     <li>importoDaIncassare = 0</li>
	 *     </li>il Subdocumento  appartiene  a un Documento di entrata che risulta avere un’associazione padre-figlio attiva in cui
 	 *		ricopre il ruolo di figlio (vedi TipoAssociazione) e il cui padre è un Documento di Spesa. 
 	 *		Queste quote verranno emesse a partire dal documento padre, ovvero verranno elaborate dall’emettitore di Ordinativi di pagamento. 
 	 *		Si cita a titolo esemplificativo il documento di tipo PENALE che ha normalmente una relazione in cui l’Entrata è subordinata alla Spesa.</li>
	 * </ul>
	 *
	 * @param subdoc the subdocumento
	 */
    protected void checkOrdinativoEmettibile(SubdocumentoEntrata subdoc, Boolean flagConvalidaManuale) {
    	DocumentoEntrata doc = subdoc.getDocumento();
    	//StatoOperativoDocumento  A – ANNULLATO o S-STORNATO o E - EMESSOstatoOperativoLiquidazione <> VALIDO
    	if(StatoOperativoDocumento.ANNULLATO.equals(doc.getStatoOperativoDocumento()) || 
    	   StatoOperativoDocumento.STORNATO.equals(doc.getStatoOperativoDocumento()) ||
    	   StatoOperativoDocumento.EMESSO.equals(doc.getStatoOperativoDocumento())){
    		throw new BusinessException(ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Documento [uid: "+doc.getUid()+"]", doc.getStatoOperativoDocumento().getDescrizione()));
    	}
    	
    	// TipoConvalida <> da quello passato in inpu
    	if(flagConvalidaManuale != null && subdoc.getFlagConvalidaManuale() != null
    			&& !flagConvalidaManuale.equals(subdoc.getFlagConvalidaManuale())){
    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("flag convalida manuale incongruente con subdocumento "+ subdoc.getNumero() + " [uid: "+subdoc.getUid()+"] del documento: "+subdoc.getDocumento().getDescAnnoNumeroTipoDoc()));
    	}
    	
    	AttoAmministrativo aa = subdoc.getAttoAmministrativo();
    	// TODO: Se l'atto amministrativo non esiste?
    	if(aa == null) {
    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento [uid: " + subdoc.getUid() + "]", "privo di provvedimento"));
    	}
    	// AttoAmministrativo collegato alla quota con StatoOperativo <> D-DEFINITIVO
    	if(!StatoOperativoAtti.DEFINITIVO.equals(aa.getStatoOperativoAtti())){
    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("stato operativo atto +"+aa.getAnno()+"/"+ aa.getNumero()+ " non "+StatoOperativoAtti.DEFINITIVO.getDescrizione()+". Subdocumento "+ subdoc.getNumero() + " [uid: "+subdoc.getUid()+"] del documento: "+subdoc.getDocumento().getDescAnnoNumeroTipoDoc()));
    	}
    	// ImportoDaIncassare =0
    	if(BigDecimal.ZERO.equals(subdoc.getImportoDaIncassare())){
    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("disponibilita pagare 0 per la quota: "+ subdoc.getNumero() + " [uid: "+subdoc.getUid()+"] del documento: "+subdoc.getDocumento().getDescAnnoNumeroTipoDoc()));
    	}

    	long numeroDocumentiEntrataPadre = contaNumeroDocumentiSpesaPadre(subdoc);
    	if(numeroDocumentiEntrataPadre > 0L){
    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento "
    		+ subdoc.getNumero() + " [uid: "+subdoc.getUid()+"] del documento: "+subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " collegato ad un documento spesa figlio di un documento spesa."));
    	}
    	
    	//aggiungo un controllo, anche se non è in analisi: l'accertamento deve esistere e deve avere il capitolo!
    	if(subdoc.getAccertamento() == null){
    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento "
    	    	+ subdoc.getNumero() + " [uid: "+subdoc.getUid()+"] del documento: "+subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " non collegato ad accertamento."));
    	}
    	if(subdoc.getAccertamento().getCapitoloEntrataGestione() == null){
    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento "
    	    	+ subdoc.getNumero() + " [uid: "+subdoc.getUid()+"] del documento: "+subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " non collegato ad un capitolo."));
    	}
    	
    	if(!datiIvaValidi(subdoc)){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento [uid: " + subdoc.getUid() + "] " + subdoc.getNumero() + " [uid: " + subdoc.getUid() + "] del documento: "
					+ subdoc.getDocumento().getDescAnnoNumeroTipoDoc() + " ha dati iva associati ma non e' rilevante iva"));
		}
		
	}
    

	/**
	 * Controlla se il subdocumento di entrata presenta un collegamento con un ordinativo valido. Lancia una AlreadyElaboratedException se questo accade
	 *
	 * @param subdocumentoEntrata the subdocumento entrata
	 */
	protected void checkOrdinativoGiaEmesso(SubdocumentoEntrata subdocumentoEntrata) {
		final String methodName ="checkOrdinativoGiaEmesso";
		Long countOrdinativiAssociatiAQuota = subdocumentoEntrataDad.countOrdinativiAssociatiAQuota(subdocumentoEntrata);
		log.debug(methodName, "countOrdinativiAssociatiAQuota: " + countOrdinativiAssociatiAQuota);
		if(countOrdinativiAssociatiAQuota >0) {
			Messaggio msg = MessaggioCore.MESSAGGIO_DI_SISTEMA.getMessaggio("subdocumento [uid: " + subdocumentoEntrata.getUid() + "]");
			Errore erroreElaborazione = new Errore(msg.getCodice(), msg.getDescrizione());
    		throw new AlreadyElaboratedException(erroreElaborazione);
    	}
	}

    private boolean datiIvaValidi(SubdocumentoEntrata subdoc) {
		if(subdoc.getSubdocumentoIva() != null && !Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA())){
			return false;
		}
		return subdoc.getDocumento().getListaSubdocumentoIva() == null
				|| subdoc.getDocumento().getListaSubdocumentoIva().isEmpty()
				|| esisteQuotaRilevanteIva(subdoc.getDocumento());
	}
	
	private boolean esisteQuotaRilevanteIva(DocumentoEntrata documento) {
		Long quoteRilevantiIva = documentoEntrataDad.countQuoteRilevantiIva(documento);
		return quoteRilevantiIva.compareTo(Long.valueOf(0)) != 0;
	}
	
	/**
	 * Conteggio del numero dei documenti di entrata padre per il subdocumento di spesa fornito.
	 * 
	 * @param subdoc il subdocumento
	 * @return il numero dei documenti di entrata padri
	 */
	private long contaNumeroDocumentiEntrataPadre(SubdocumentoSpesa subdoc) {
		Long numDocPadre = subdocumentoSpesaDad.contaDocumentiEntrataPadre(subdoc);
		return numDocPadre == null ? 0L : numDocPadre.longValue();
	}
	
	/**
     * Conteggio del numero dei documenti di entrata padre per il subdocumento di spesa fornito.
     * 
     * @param subdoc il subdocumento
     * @return il numero dei documenti di entrata padri
     */
    private long contaNumeroDocumentiSpesaPadre(SubdocumentoEntrata subdoc) {
    	Long numDocPadre = subdocumentoEntrataDad.contaDocumentiSpesaPadre(subdoc);
		return numDocPadre == null ? 0L : numDocPadre.longValue();
	}
    
    
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*-------------------------------------------- CREAZIONE ORDINATIVI -----------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/

	/**
	 * Creazione dell'ordinativo di pagamento.
	 * 
	 * @param subdoc il subdocumento da cui creare l'ordinativo
	 * @return l'ordinativo creato
	 */
	protected OrdinativoPagamento creaOrdinativoPagamento(SubdocumentoSpesa subdoc) {
		List<SubdocumentoSpesa> subdocs = new ArrayList<SubdocumentoSpesa>();
		subdocs.add(subdoc);
		return creaOrdinativoPagamento(subdocs);
	}
	
	/**
	 * Creazione dell'ordinativo di pagamento.
	 * 
	 * @param quote le quote da cui creare l'ordinativo
	 * @return l'ordinativo creato
	 */
	protected OrdinativoPagamento creaOrdinativoPagamento(List<SubdocumentoSpesa> quote) {
		final String methodName = "creaOrdinativoPagamento";
		if(quote == null || quote.isEmpty()) {
			// Cosa fare in questo caso?
			log.debug(methodName, "Quote non esistenti - non creo l'ordinativo");
			return null;
		}
		SubdocumentoSpesa quota = quote.get(0);
		Liquidazione liquidazione = quota.getLiquidazione();
		CapitoloUscitaGestione capitolo = liquidazione.getCapitoloUscitaGestione();
		
		OrdinativoPagamento ordinativo = new OrdinativoPagamento();
		
		creaOrdinativo(quote, capitolo, ordinativo);
		
		ordinativo.setFlagBeneficiMultiplo(Boolean.TRUE.equals(quota.getDocumento().getFlagBeneficiarioMultiplo()));
		if(capitolo.getImportiCapitolo() != null){
			ordinativo.setCastellettoEmessi(capitolo.getImportiCapitolo().getTotalePagato());
		}else{
			ordinativo.setCastellettoEmessi(BigDecimal.ZERO);
		}
		ordinativo.setCapitoloUscitaGestione(capitolo);
		
		// Classificazione finanziaria
		impostaClassificazioneFinanziariaDiGestione(ordinativo, liquidazione);
		
		impostaDatiSiopePlus(ordinativo, liquidazione);
				
		ContoTesoreria conto = contoTesoreria != null ? contoTesoreriaFinFromContoTesoreriaBil(contoTesoreria): liquidazione.getContoTesoreria();
		ordinativo.setContoTesoreria(conto);
		
		ordinativo.setDistinta(distinta != null ? distinta : liquidazione.getDistinta());
		
		ordinativo.setAttoAmministrativo(liquidazione.getAttoAmministrativoLiquidazione());
		
		// Il soggetto deve avere modalita pagamento e sede secondaria
		if(liquidazione.getSoggettoLiquidazione() != null){
			ordinativo.setSoggetto(liquidazione.getSoggettoLiquidazione());
			ordinativo.getSoggetto().setElencoModalitaPagamento(new ArrayList<ModalitaPagamentoSoggetto>());
			ordinativo.getSoggetto().getElencoModalitaPagamento().add(liquidazione.getModalitaPagamentoSoggetto());
		}
		
		if(liquidazione.getSedeSecondariaSoggetto() != null ){
			SedeSecondariaSoggetto sede = new SedeSecondariaSoggetto();
			sede.setUid(liquidazione.getSedeSecondariaSoggetto());
			List<SedeSecondariaSoggetto> sediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
			sediSecondarie.add(sede);
			ordinativo.getSoggetto().setSediSecondarie(sediSecondarie);
		}
		//-------------------------------
		// CREO I SUBORDINATIVI
		//--------------------------------
		List<SubOrdinativoPagamento> subOrdinativi = creaSubOrdinativiPagamento(quote);
		ordinativo.setElencoSubOrdinativiDiPagamento(subOrdinativi);
		
		//commissioni qui perche' sono solo per spese.
		SiacDCommissioneTipoEnum commissioneTipoEnum = SiacDCommissioneTipoEnum.byCommissioniDocumentoEvenNull(commissioniDocumento != null ? commissioniDocumento : quota.getCommissioniDocumento());
		if(commissioneTipoEnum != null){
			log.debug(methodName, "commissioneTipo: "+ commissioneTipoEnum.name() + " (codice: "+commissioneTipoEnum.getCodice()+")");
			CommissioneDocumento commissioneDocumento = new CommissioneDocumento();
			commissioneDocumento.setCodice(commissioneTipoEnum.getCodice());
			ordinativo.setCommissioneDocumento(commissioneDocumento);
		}
		//SIAC-6175
		ordinativo.setFlagDaTrasmettere(flagDaTrasmettere);
		//SIAC-6206
		ordinativo.setClassificatoreStipendi(classificatoreStipendi);
		
		log.debug(methodName, "Ordinativo di pagamento creato per liquidazione " + liquidazione.getUid() + " e capitolo " + capitolo.getUid());
		
		return ordinativo;
	}

	/**
	 * Imposta dati siope plus a partire dalla liquidazione.
	 *
	 * @param ordinativo l' ordinativo da popolare
	 * @param liquidazione la liquidazione da cui prendere i dati
	 */
	private void impostaDatiSiopePlus(OrdinativoPagamento ordinativo, Liquidazione liquidazione) {
		ordinativo.setSiopeTipoDebito(ottieniSiopeTipoDebito(liquidazione));
		ordinativo.setCig(liquidazione.getCig());
		ordinativo.setSiopeAssenzaMotivazione(ottieniSiopeAssenzaMotivazione(liquidazione));
		
	}

	/**
	 * Ottiene il valore uid-codice per la siope assenza motivazione.
	 *
	 * @param liquidazione the liquidazione
	 * @return the siope assenza motivazione
	 */
	private SiopeAssenzaMotivazione ottieniSiopeAssenzaMotivazione(Liquidazione liquidazione) {
		return popolaCodificaByUid(liquidazione.getSiopeAssenzaMotivazione(),SiopeAssenzaMotivazione.class);
	}

	/**
	 * Ottieni il valore uid-codice per la siope tipo debito.
	 *
	 * @param liquidazione the liquidazione
	 * @return the siope tipo debito
	 */
	private SiopeTipoDebito ottieniSiopeTipoDebito(Liquidazione liquidazione) {
		return popolaCodificaByUid(liquidazione.getSiopeTipoDebito(), SiopeTipoDebito.class);
	}
	
	
	
	/**
	 * Estrai siope assenza motivazione.
	 *
	 * @param subdoc the subdoc
	 * @return the siope assenza motivazione
	 */
	private <C extends Codifica> C popolaCodificaByUid(C codifica, Class<C> codificaClass) {
		if(codifica == null || codifica.getUid() == 0) {
			return null;
		}
		if(StringUtils.isEmpty(codifica.getCodice())) {
			codifica = codificaDad.ricercaCodifica(codificaClass, codifica.getUid());
		}
		return codifica;
	}

	/**
	 * Crea un ordinativo di incasso.
	 * 
	 * @param subdoc il subdocumento tramite cui creare l'ordinativo
	 * 
	 * @return l'ordinativo creato
	 */
	protected OrdinativoIncasso creaOrdinativoIncasso(SubdocumentoEntrata subdoc) {
		List<SubdocumentoEntrata> subdocs = new ArrayList<SubdocumentoEntrata>();
		subdocs.add(subdoc);
		return creaOrdinativoIncasso(subdocs);
	}
	
	/**
	 * Crea un ordinativo di incasso.
	 * 
	 * @param quote i subdocumenti tramite cui creare l'ordinativo
	 * 
	 * @return l'ordinativo creato
	 */
	protected OrdinativoIncasso creaOrdinativoIncasso(List<SubdocumentoEntrata> quote) {
		final String methodName = "creaOrdinativoIncasso";
		if(quote == null || quote.isEmpty()) {
			log.debug(methodName, "Quote non esistenti - non creo l'ordinativo");
			// Cosa fare in questo caso?
			return null;
		}
		
		SubdocumentoEntrata quota = quote.get(0);
		Accertamento accertamento = quota.getAccertamento();
		CapitoloEntrataGestione capitolo = accertamento.getCapitoloEntrataGestione();
		OrdinativoIncasso ordinativo = new OrdinativoIncasso();
		
		// Impostazione dei dati standard
		creaOrdinativo(quote, capitolo, ordinativo);
		
		ordinativo.setFlagBeneficiMultiplo(Boolean.TRUE.equals(quota.getDocumento().getFlagDebitoreMultiplo()));
		
		if(capitolo.getImportiCapitolo() != null){
			ordinativo.setCastellettoEmessi(capitolo.getImportiCapitolo().getTotaleIncassato());
		}else{
			ordinativo.setCastellettoEmessi(BigDecimal.ZERO);
		}
		ordinativo.setCapitoloEntrataGestione(capitolo);
		
		// Classificazione finanziaria
		impostaClassificazioneFinanziariaDiGestione(ordinativo, accertamento);
		
		ContoTesoreria conto = contoTesoreria != null ? contoTesoreriaFinFromContoTesoreriaBil(contoTesoreria) : contoTesoreriaFinFromContoTesoreriaBil(quota.getContoTesoreria());
		ordinativo.setContoTesoreria(conto);
		
		// Da parametro, se valorizzato, altrimenti SubdocumentoEntrata
		Distinta distintaFin = distinta != null ? distinta : quota.getDistinta();
		ordinativo.setDistinta(distintaFin);
		
		// Il soggetto deve avere modalita pagamento e sede secondaria
		ordinativo.setSoggetto(quota.getDocumento().getSoggetto());
		ordinativo.setAttoAmministrativo(quota.getAttoAmministrativo());
		if(quota.getSedeSecondariaSoggetto() != null){
			List<SedeSecondariaSoggetto> sediSecondarie = new ArrayList<SedeSecondariaSoggetto>();
			sediSecondarie.add(quota.getSedeSecondariaSoggetto());
			ordinativo.getSoggetto().setSediSecondarie(sediSecondarie);
		}
		
		List<SubOrdinativoIncasso> subOrdinativi = creaSubOrdinativiIncasso(quote);
		ordinativo.setElencoSubOrdinativiDiIncasso(subOrdinativi);
		
		ordinativo.setForza(true);
		
		//SIAC-6175
		ordinativo.setFlagDaTrasmettere(flagDaTrasmettere);
		//SIAC-6206
		ordinativo.setClassificatoreStipendi(classificatoreStipendi);
		
		log.debug(methodName, "Ordinativo di incasso creato per accertamento " + accertamento.getUid() + " e capitolo " + capitolo.getUid());
		
		return ordinativo;
	}
	
	/**
	 * ClassificazioneFinanziariaGestione ovvero vengono mappate le stesse relazioni valide con i classificatori elencati:
	 * <ul>
	 *     <li>Elemento del Piano dei Conti</li>
	 *     <li>Piano dei Conti Economico</li>
	 *     <li>Spesa Ricorrente</li>
	 *     <li>Perimetro Sanitario</li>
	 *     <li>Transazione UE</li>
	 *     <li>Politiche RegionaliUnitarie</li>
	 *     <li>Elemento SIOPE</li>
	 *     <li>Classificazione COFOG</li>
	 * </ul>
	 * 
	 * @param ordinativo            l'ordinativo da popolare
	 * @param transazioneElementare la transazione da cui ottenere i dati
	 */
	protected void impostaClassificazioneFinanziariaDiGestione(Ordinativo ordinativo, TransazioneElementare transazioneElementare) {
		ordinativo.setCodPdc(transazioneElementare.getCodPdc());
		ordinativo.setCodContoEconomico(transazioneElementare.getCodContoEconomico());
		ordinativo.setCodRicorrenteSpesa(transazioneElementare.getCodRicorrenteSpesa());
		ordinativo.setCodCapitoloSanitarioSpesa(transazioneElementare.getCodCapitoloSanitarioSpesa());
		ordinativo.setCodTransazioneEuropeaSpesa(transazioneElementare.getCodTransazioneEuropeaSpesa());
		ordinativo.setCodPrgPolReg(transazioneElementare.getCodPrgPolReg());
		ordinativo.setCodSiope(transazioneElementare.getCodSiope());
		ordinativo.setCodCofog(transazioneElementare.getCodCofog());
	}

	/**
	 * Creazione dei dati base dell'ordinativo e gestione degli ordinativi collegati.
	 * 
	 * @param quote      le quote da cui ottenere i dati
	 * @param capitolo   il capitolo da cui ottenere i dati
	 * @param ordinativo l'ordinativo da popolare
	 */
	protected <O extends Ordinativo, S extends Subdocumento<D, ?>, D extends Documento<S, ?>, C extends Capitolo<?, ?>> void creaOrdinativo(List<S> quote, C capitolo, O ordinativo) {
		final String methodName = "creaOrdinativo";
		S quota = quote.get(0);
		String descrizione = getDescrizioniDistinte(quote);
		ordinativo.setDescrizione(descrizione);
		
		String noteOrdinativo = (note != null && StringUtils.isNotBlank(note)) ? note : quota.getNote();
		ordinativo.setNote(noteOrdinativo);
		NoteTesoriere noteTesoriere = noteTesoriereFinFromNoteTesoriereBil(quota.getNoteTesoriere());
		ordinativo.setNoteTesoriere(noteTesoriere);
		
		//TODO dov'è il flagAvviso?
		//ordinativo.setflagAvviso(quota1.getFlagAvviso());
		
		if(capitolo.getImportiCapitolo() != null){
			ordinativo.setCastellettoCompentenza(capitolo.getImportiCapitolo().getStanziamento());
			ordinativo.setCastellettoCassa(capitolo.getImportiCapitolo().getStanziamentoCassa());
		}else{
			ordinativo.setCastellettoCompentenza(BigDecimal.ZERO);
			ordinativo.setCastellettoCassa(BigDecimal.ZERO);
		}
		
		Calendar gc = GregorianCalendar.getInstance();
		int currentYear = gc.get(GregorianCalendar.YEAR);
		int annoBilancio = bilancio.getAnno();
		if(currentYear > annoBilancio) {
			log.debug(methodName, "Anno corrente (" + currentYear + ") > anno del bilancio (" + annoBilancio + "): impostazione della data a 31/12/" + annoBilancio);
			// Se anno corrente > anno di bilancio la data è 31/12/anno di bilancio
			gc.set(annoBilancio, Calendar.DECEMBER, 31, 0, 0);
			ordinativo.setDataEmissione(gc.getTime()); 
		} else {
			// Se anno corrente = anno bilancio: la data e' quella di sistema
			log.debug(methodName, "Anno corrente (" + currentYear + ") = anno del bilancio (" + annoBilancio + "): impostazione della data corrente");
			ordinativo.setDataEmissione(new Date()); 
		}
		ordinativo.setLoginModifica(loginOperazione);
		
		CodiceBollo bollo = codiceBolloFinFromCodiceBolloBil(codiceBollo != null ? codiceBollo :quota.getDocumento().getCodiceBollo());
		ordinativo.setCodiceBollo(bollo);
		
		ordinativo.setFlagCopertura(quota.getFlagACopertura());
		
		List<RegolarizzazioneProvvisorio> elencoRegolarizzazioneProvvisori = creaGruppiRegolarizzazioneProvvisorio(quote);
		log.debug(methodName, "ho creato " +elencoRegolarizzazioneProvvisori.size()+ " regolarizzazioni");
		ordinativo.setElencoRegolarizzazioneProvvisori(elencoRegolarizzazioneProvvisori);
		
		//crea e inserisce eventuali ordinativi subordinati
		List<Ordinativo> listaOrdinativiCollegati = new ArrayList<Ordinativo>();
		
		listaOrdinativiCollegati.addAll(gestisciOrdinativiCollegatiIncasso(quota));
		listaOrdinativiCollegati.addAll(gestisciOrdinativiCollegatiPagamento(quota));
		
		log.debug(methodName, "Ordinativi collegati creati: " + listaOrdinativiCollegati.size());
		ordinativo.setElencoOrdinativiCollegati(listaOrdinativiCollegati);
		
		// TODO: lo stato non e' esplicitato nell'analisi, ma obbligatorio su DB (per quanto non su servizio). Imposto uno stato a piacere
		ordinativo.setStatoOperativoOrdinativo(StatoOperativoOrdinativo.INSERITO);
	}
	
	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*-------------------------------------------- CREAZIONE SUBORDINATIVI --------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Creazione dei subordinativi di pagamento.
	 * 
	 * @param quote le quote da cui creare i subordinativi
	 * 
	 * @return i subordinativi
	 */
	private List<SubOrdinativoPagamento> creaSubOrdinativiPagamento(List<SubdocumentoSpesa> quote) {
		final String methodName = "creaSubOrdinativiPagamento";
		List<SubOrdinativoPagamento> subOrdinativi = new ArrayList<SubOrdinativoPagamento>();
		for(SubdocumentoSpesa s : quote) {
			SubOrdinativoPagamento subOrdinativo = new SubOrdinativoPagamento();
			subOrdinativo.setLiquidazione(s.getLiquidazione());
			subOrdinativo.setDescrizione(s.getCausaleOrdinativo());
			subOrdinativo.setImportoAttuale(s.getImportoDaPagare());
			subOrdinativo.setImportoIniziale(s.getImportoDaPagare());
			
			if (Boolean.TRUE.equals(flagNoDataScadenza)){
				subOrdinativo.setDataEsecuzionePagamento(null);					
			}else{
				subOrdinativo.setDataEsecuzionePagamento(dataScadenza != null ? dataScadenza : (s.getDataScadenzaDopoSospensione() != null ? s.getDataScadenzaDopoSospensione() : s.getDataScadenza()));				
			}

			subOrdinativo.setSubDocumentoSpesa(s);
			
			log.debug(methodName, "Creato subordinativo per quota " + s.getUid());
			subOrdinativi.add(subOrdinativo);
		}
		log.debug(methodName, "Creati " + subOrdinativi.size() + " subordinativi di pagamento");
		return subOrdinativi;
	}
	
	/**
	 * Creazione dei subordinativi di incasso.
	 * 
	 * @param quote le quote da cui creare i subordinativi
	 * 
	 * @return i subordinativi
	 */
	private List<SubOrdinativoIncasso> creaSubOrdinativiIncasso(List<SubdocumentoEntrata> quote) {
		final String methodName = "creaSubOrdinativiIncasso";
		List<SubOrdinativoIncasso> subOrdinativi = new ArrayList<SubOrdinativoIncasso>();
		for(SubdocumentoEntrata s : quote) {
			SubOrdinativoIncasso subOrdinativo = new SubOrdinativoIncasso();
			subOrdinativo.setAccertamento(s.getAccertamentoOSubAccertamento());// TODO: Subaccertamento
			
			if(subOrdinativo.getAccertamento() instanceof SubAccertamento){
				((SubAccertamento)subOrdinativo.getAccertamento()).setNumeroAccertamentoPadre(s.getAccertamento().getNumeroBigDecimal());
				((SubAccertamento)subOrdinativo.getAccertamento()).setAnnoAccertamentoPadre(s.getAccertamento().getAnnoMovimento());
				((SubAccertamento)subOrdinativo.getAccertamento()).setAnnoMovimento(s.getAccertamento().getAnnoMovimento()); 
			}
			
			subOrdinativo.setDescrizione(s.getDocumento().getDescrizione());// + "[subdocUid:"+s.getUid()+"]");
			subOrdinativo.setImportoAttuale(s.getImportoDaIncassare());
			subOrdinativo.setImportoIniziale(s.getImportoDaIncassare());
			 
			if (Boolean.TRUE.equals(flagNoDataScadenza)){
				// Inutile, ma sempre meglio essere prudenti...
				subOrdinativo.setDataScadenza(null);
			} else {
				subOrdinativo.setDataScadenza(dataScadenza != null ? dataScadenza : s.getDataScadenza());
			}
			
			subOrdinativo.setSubDocumentoEntrata(s);
			
			log.debug(methodName, "Creato subordinativo per quota " + s.getUid());
			subOrdinativi.add(subOrdinativo);
		}
		log.debug(methodName, "Creati " + subOrdinativi.size() + " subordinativi di incasso");
		return subOrdinativi;
	}

	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*---------------------------------------- CREAZIONE ORDINATIVI SUBORDINATI----------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	private List<OrdinativoIncasso> gestisciOrdinativiCollegatiIncasso(Subdocumento<?,?> quota) {
		String methodName = "gestisciOrdinativiCollegatiIncasso";
		List<OrdinativoIncasso> ordinativiIncasso = new ArrayList<OrdinativoIncasso>();
		SubdocumentoEntrata quotaDaAggiungere = new SubdocumentoEntrata();
		
		//se ho dei documenti collegati di entrata di tipo subordinato:
		for(DocumentoEntrata docEntr : quota.getDocumento().getListaDocumentiEntrataFiglio()){
			log.debug(methodName, "documento entrata collegato: " + docEntr.getUid());
			if(TipoRelazione.SUBORDINATO.equals(docEntr.getTipoRelazione())){
				log.debug(methodName, "documento entrata collegato: " + docEntr.getUid() +  "di tipo subordinato.");
				
				BigDecimal importoResiduo = quota.getImporto();
				//carico i dettagli delle quote del doc subordinato
				List<SubdocumentoEntrata> quoteEntrata = subdocumentoEntrataDad.findSubdocumentiEntrataByIdDocumento(docEntr.getUid());
				docEntr.setListaSubdocumenti(quoteEntrata);
				
				for(SubdocumentoEntrata subdocEntr : docEntr.getListaSubdocumenti()) {
					Accertamento accertamento = caricaAccertamento(subdocEntr);
    				subdocEntr.setAccertamento(accertamento);
					if(checkSubdocEntrataFiglio(subdocEntr, quota.getAttoAmministrativo())){
						log.debug(methodName, "subdocumento entrata: " + subdocEntr.getUid() +  "ha passato il check.");
						//se l'importo della quota collegata è minore del mio importo residuo posso emettere un ordinativo per tutta la quota
						if(subdocEntr.getImporto().compareTo(importoResiduo) <= 0){
							log.debug(methodName, "importo residuo ok!");
							OrdinativoIncasso ordIncasso = creaOrdinativoIncasso(subdocEntr);
							//in questo caso non va bene quella eventuale della request, perche' e' di spesa. Per sicurezza sovrascrivo con quella della quota. 
							ordIncasso.setDistinta(subdocEntr.getDistinta());
							
							//SIAC-5937: devo assolutamente farlo prima di chiamare il servizio di inserimento, perche' in base a questo va a cambiare la disponibilita' ad incassare del movimento di gestione
							if(this.bilancioInDoppiaGestione) {
								//SIAC-5937: bvoglio l'aggiornamento della relazione il piu' possibile vicino a quello di inserimento dell'ordinativo di incasso
								Date now = new Date();
								collegaScollegaMovimentiCollegatiEntrata(subdocEntr, now);
							}
							
							ordIncasso = inserisceOrdinativoIncasso(ordIncasso);
							ordIncasso.setTipoAssociazioneEmissione(TipoAssociazioneEmissione.SUB_ORD);
							ordinativiIncasso.add(ordIncasso);
							
							aggiornaStatoOperativoDocumentoEntrata(subdocEntr.getDocumento());
							if(Boolean.TRUE.equals(subdocEntr.getFlagRilevanteIVA()) && StringUtils.isNotBlank(subdocEntr.getNumeroRegistrazioneIVA())){
								subdocEntr.setSubdocumentoIva(aggiornaStatoOperativoDocumentoIvaEntrata(subdocEntr));
							}
							
							importoResiduo = importoResiduo.subtract(subdocEntr.getImporto());
						}else{ 
							// se l'importo della quota subordinata è maggiore del mio importo residuo devo spezzarla in due quote ed emettere solo l'importo che ho a disposizione
							log.debug(methodName, "importo residuo KO!");
							//l'importo da dedurre deve essere al massimo pari all'importo della quota. Se risulta maggiore, spezzo su due quote anche l'importo da dedurre.
							BigDecimal importoDaDedurre = subdocEntr.getImportoDaDedurreNotNull();
							if(importoResiduo.compareTo(importoDaDedurre)<0){
								importoDaDedurre = importoResiduo;
							}
							
							aggiornaImportoQuotaEntrata(subdocEntr, importoResiduo, importoDaDedurre);
							quotaDaAggiungere = inserisciNuovaQuotaEntrata(subdocEntr, importoResiduo, importoDaDedurre);
							OrdinativoIncasso ordIncasso = creaOrdinativoIncasso(subdocEntr);
							//in questo caso non va bene quella eventuale della request, perche' e' di spesa. Per sicurezza sovrascrivo con quella della quota. O con niente?
							ordIncasso.setDistinta(subdocEntr.getDistinta());
							
							//SIAC-5937
							if(this.bilancioInDoppiaGestione) {
								gestisciDoppiaGestioneSubdocumentoEntrataSeNecessario(subdocEntr);
							}
							
							ordIncasso = inserisceOrdinativoIncasso(ordIncasso);
							ordIncasso.setTipoAssociazioneEmissione(TipoAssociazioneEmissione.SUB_ORD);
							ordinativiIncasso.add(ordIncasso);
							
							aggiornaStatoOperativoDocumentoEntrata(subdocEntr.getDocumento());
							if(Boolean.TRUE.equals(subdocEntr.getFlagRilevanteIVA()) && StringUtils.isNotBlank(subdocEntr.getNumeroRegistrazioneIVA())){
								subdocEntr.setSubdocumentoIva(aggiornaStatoOperativoDocumentoIvaEntrata(subdocEntr));
							}
							break;
						}
						//ricarico l'accertamento in questione, potrebbe essere cambiata diponibilità e devo tenerne conto per le quote successive
						Accertamento acc = ricercaAccertamentoPerChiave(subdocEntr.getAccertamento());
						accertamentiCache.put(subdocEntr.getAccertamento().getUid(), acc);
						subdocEntr.setAccertamento(acc);
						
					}else{
						log.debug(methodName, "subdocumento entrata: " + subdocEntr.getUid() +  "NON ha passato il check.");
					}
				}
			}else{
				log.debug(methodName, "documento entrata collegato: " + docEntr.getUid() +  "non di tipo subordinato.");
			}
			docEntr.getListaSubdocumenti().add(quotaDaAggiungere);
		}
		return ordinativiIncasso;
	}
	
	private List<OrdinativoPagamento> gestisciOrdinativiCollegatiPagamento(Subdocumento<?,?> quota) {
		String methodName = "gestisciOrdinativiCollegatiIncasso";
		List<OrdinativoPagamento> ordinativiPagamento = new ArrayList<OrdinativoPagamento>();
		SubdocumentoSpesa quotaDaAggiungere = new SubdocumentoSpesa();
		for(DocumentoSpesa docSpesa : quota.getDocumento().getListaDocumentiSpesaFiglio()){
			log.debug(methodName, "documento spesa collegato: " + docSpesa.getUid());
			if(TipoRelazione.SUBORDINATO.equals(docSpesa.getTipoRelazione())){
				log.debug(methodName, "documento spesa collegato: " + docSpesa.getUid() +  "di tipo subordinato.");
				
				BigDecimal importoResiduo = quota.getImporto();
				//carico i dettagli delle quote del doc subordinato
				List<SubdocumentoSpesa> quoteSpesa = subdocumentoSpesaDad.findSubdocumentiSpesaByIdDocumento(docSpesa.getUid());
				docSpesa.setListaSubdocumenti(quoteSpesa);
				
				for(SubdocumentoSpesa subdocSpesa : docSpesa.getListaSubdocumenti()) {
					Liquidazione liquidazione = caricaLiquidazione(subdocSpesa);
					subdocSpesa.setLiquidazione(liquidazione);
					if(checkSubdocSpesaFiglio(subdocSpesa, quota.getAttoAmministrativo())){
						log.debug(methodName, "subdocumento spesa: " + subdocSpesa.getUid() +  "ha passato il check.");
						//se l'importo della quota collegata è minore del mio importo residuo posso emettere un ordinativo per tutta la quota
						if(subdocSpesa.getImporto().compareTo(importoResiduo) <= 0){
							log.debug(methodName, "importo residuo ok!");
							OrdinativoPagamento ordPagamento = creaOrdinativoPagamento(subdocSpesa);
							//in questo caso non va bene quella eventuale della request, perche' e' di entrata. Per sicurezza sovrascrivo con quella della quota. O con niente?
							ordPagamento.setDistinta(liquidazione.getDistinta());
							
							//SIAC-5937: necessario richiamare il metodo PRIMA del servizio di inserimento ordinativo, perche' il subdocumento modifica la disponibilita' a liquidare dell'impegno nell'anno di bilancio successivo.
							if(this.bilancioInDoppiaGestione) {
								collegaScollegaMovimentiCollegatiSpesa(subdocSpesa, new Date());
							}
							
							ordPagamento = inserisceOrdinativoPagamento(ordPagamento);
							
							ordPagamento.setTipoAssociazioneEmissione(TipoAssociazioneEmissione.SUB_ORD);
							ordinativiPagamento.add(ordPagamento);
							
							aggiornaStatoOperativoDocumentoSpesa(subdocSpesa.getDocumento());
							if(Boolean.TRUE.equals(subdocSpesa.getFlagRilevanteIVA()) && StringUtils.isNotBlank(subdocSpesa.getNumeroRegistrazioneIVA())){
								subdocSpesa.setSubdocumentoIva(aggiornaStatoOperativoDocumentoIvaSpesa(subdocSpesa));
							}
							importoResiduo = importoResiduo.subtract(subdocSpesa.getImporto());
						}else{ 
							// se l'importo della quota subordinata è maggiore del mio importo residuo devo spezzarla in due quote ed emettere solo l'importo che ho a disposizione
							log.debug(methodName, "importo residuo KO!");
							//l'importo da dedurre deve essere al massimo pari all'importo della quota. Se risulta maggiore, spezzo su due quote anche l'importo da dedurre.
							BigDecimal importoDaDedurre = subdocSpesa.getImportoDaDedurreNotNull();
							if(importoResiduo.compareTo(importoDaDedurre)<0){
								importoDaDedurre = importoResiduo;
							}
							aggiornaImportoQuotaSpesa(subdocSpesa, importoResiduo, importoDaDedurre);
							quotaDaAggiungere = inserisciNuovaQuotaSpesa(subdocSpesa, importoResiduo, importoDaDedurre);
							
							//SIAC-5937: necessario richiamare il metodo PRIMA del servizio di inserimento ordinativo, perche' il subdocumento modifica la disponibilita' a liquidare dell'impegno nell'anno di bilancio successivo.
							if(this.bilancioInDoppiaGestione) {
								collegaScollegaMovimentiCollegatiSpesa(subdocSpesa, new Date());
							}
							
							OrdinativoPagamento ordPagamento = creaOrdinativoPagamento(subdocSpesa);
							//in questo caso non va bene quella eventuale della request, perche' e' di entrata. Per sicurezza sovrascrivo con quella della quota. O con niente?
							ordPagamento.setDistinta(liquidazione.getDistinta());
							ordPagamento = inserisceOrdinativoPagamento(ordPagamento);
							ordPagamento.setTipoAssociazioneEmissione(TipoAssociazioneEmissione.SUB_ORD);
							ordinativiPagamento.add(ordPagamento);

							aggiornaStatoOperativoDocumentoSpesa(subdocSpesa.getDocumento());
							if(Boolean.TRUE.equals(subdocSpesa.getFlagRilevanteIVA()) && StringUtils.isNotBlank(subdocSpesa.getNumeroRegistrazioneIVA())){
								subdocSpesa.setSubdocumentoIva(aggiornaStatoOperativoDocumentoIvaSpesa(subdocSpesa));
							}
							
							break;
						}
						
						//ricarico la liquidazione in questione, potrebbe essere cambiata diponibilità a liquidare e devo tenerne conto per le quote successive
						//Liquidazione liq = ricercaLiquidazionePerChiave(subdocSpesa.getLiquidazione());
						// RM 28/03/17 Richiamo il nuovo servizio di calcola disponibilita a liquidare, proviamo..............
						Liquidazione liq = leggiDisponibilitaALiquidare(subdocSpesa.getLiquidazione());
						liquidazioniCache.put(subdocSpesa.getLiquidazione().getUid(), liq);
						subdocSpesa.setLiquidazione(liq);
					}else{
						log.debug(methodName, "subdocumento spesa: " + subdocSpesa.getUid() +  "NON ha passato il check.");
					}
				}
			}else{
				log.debug(methodName, "documento spesa collegato: " + docSpesa.getUid() +  "non di tipo subordinato.");
			}
			docSpesa.getListaSubdocumenti().add(quotaDaAggiungere);
		}
		return ordinativiPagamento;
	}
	
	/**
	 * Aggiorna l'importo di un documento di entrata
	 * 
	 * @param subdocEntr il subdocumento da aggiornare
	 * @param importoResiduo il nuovo importo
	 */
	private void aggiornaImportoQuotaEntrata(SubdocumentoEntrata subdocEntr, BigDecimal importoResiduo, BigDecimal importoDaDedurre) {
		subdocumentoEntrataDad.aggiornaImportoSubdocumentoEntrata(subdocEntr.getUid(), importoResiduo, importoDaDedurre);
	}

	/**
	 * Inserisce una nuova quota a partire da una gia' esistente, con importo pari all'importo che resta da emettere
	 * 
	 * @param subdocEntr quota da clonare
	 * @param importoResiduo importo da sottrarre all'importo originale della quota
	 * @return
	 */
	private SubdocumentoEntrata inserisciNuovaQuotaEntrata(SubdocumentoEntrata subdocEntr, BigDecimal importoResiduo, BigDecimal importoDaDedurre) {
		SubdocumentoEntrata nuovaQuota = subdocumentoEntrataDad.findSubdocumentoEntrataById(subdocEntr.getUid());
		nuovaQuota.setUid(0);
		nuovaQuota.setImporto(subdocEntr.getImporto().subtract(importoResiduo));
		nuovaQuota.setImportoDaDedurre(subdocEntr.getImportoDaDedurre().subtract(importoDaDedurre));
		return inserisceQuotaEntrata(nuovaQuota);
	}
	
	/**
	 * Aggiorna l'importo di un documento di spesa
	 * @param subdocSpesa il subdocumento da aggiornare
	 * @param importoResiduo il nuovo importo
	 */
	private void aggiornaImportoQuotaSpesa(SubdocumentoSpesa subdocSpesa, BigDecimal importoResiduo, BigDecimal importoDaDedurre) {
		subdocumentoSpesaDad.aggiornaImportoSubdocumentoSpesa(subdocSpesa.getUid(), importoResiduo, importoDaDedurre);
	}

	/**
	 * Inserisce una nuova quota a partire da una gia' esistente, con importo pari all'importo che resta da emettere
	 * 
	 * @param subdocSpesa quota da clonare
	 * @param importoResiduo importo da sottrarre all'importo originale della quota
	 * @return
	 */
	private SubdocumentoSpesa inserisciNuovaQuotaSpesa(SubdocumentoSpesa subdocSpesa, BigDecimal importoResiduo, BigDecimal importoDaDedurre) {
		SubdocumentoSpesa nuovaQuota = subdocumentoSpesaDad.findSubdocumentoSpesaById(subdocSpesa.getUid());
		nuovaQuota.setUid(0);
		nuovaQuota.setImporto(subdocSpesa.getImporto().subtract(importoResiduo));
		nuovaQuota.setImportoDaDedurre(subdocSpesa.getImportoDaDedurre().subtract(importoDaDedurre));
		return inserisceQuotaSpesa(nuovaQuota);
	}

	/**
	 * Verifica che sussistano le condizioni per l'emissione dell'ordinativo di entrata subordinato. 
	 * <br/>
	 * @param attoAmministrativo 
	 * @param subdoc the subdocumento
	 * 
	 * @return <code>true</code> se la quota rispetta una delle seguenti condizioni:
	 * 				<ul>
	 *     				<li>StatoOperativoDocumento  AV-valido,  L-Liquidato ,  PL –parzialmente liquidato, PE – Parzialmente emesso </li>
	 *     				<li>AttoAmministrativo uguale all'AttoAmministrativo del documento padre</li>
	 *     				<li>importoDaIncassare > 0</li>
	 *     				</li>tipoConvalida non nullo</li>
	 * 				</ul>
	 * 			<code>false</code> in caso contrario
	 *
	 */
	protected boolean checkSubdocEntrataFiglio(SubdocumentoEntrata subdocEntrata, AttoAmministrativo attoAmministrativo) {
		final String methodName = "checkSubdocEntrataFiglio";
		final String pattern = "Scarto il subdoc con uid " + subdocEntrata.getUid() + ": %s";
		
		if(subdocEntrata.getOrdinativo() != null && subdocEntrata.getOrdinativo().getUid() != 0){
			log.debug(methodName, String.format(pattern, "la quota ha già ordinativo associato"));
			return false;
		}
		
		DocumentoEntrata doc = subdocEntrata.getDocumento();
    	if(!StatoOperativoDocumento.VALIDO.equals(doc.getStatoOperativoDocumento()) && 
    	   !StatoOperativoDocumento.LIQUIDATO.equals(doc.getStatoOperativoDocumento()) &&
    	   !StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO.equals(doc.getStatoOperativoDocumento()) &&
    	   !StatoOperativoDocumento.PARZIALMENTE_EMESSO.equals(doc.getStatoOperativoDocumento())){
    			log.debug(methodName, String.format(pattern, "lo stato del documento non è accettabile."));
    			return false;
//    			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdocEntrata.getNumero() + " [uid: "+subdocEntrata.getUid()+"] del documento: "+subdocEntrata.getDocumento().getDescAnnoNumeroTipoDoc() + " con stato non accettabile."));
    	}
    	
    	if(subdocEntrata.getFlagConvalidaManuale() == null){
    		log.debug(methodName, String.format(pattern, "il tipo convalida è null."));
    		return false;
//    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdocEntrata.getNumero() + " [uid: "+subdocEntrata.getUid()+"] del documento: "+subdocEntrata.getDocumento().getDescAnnoNumeroTipoDoc() + " con tipo convalida non valorizzato."));
    	}
    	
    	if(subdocEntrata.getAttoAmministrativo() == null || subdocEntrata.getAttoAmministrativo().getUid() != attoAmministrativo.getUid()) {
    		log.debug(methodName, String.format(pattern, "l'atto amministrativo è null"));
    		return false;
//    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdocEntrata.getNumero() + " [uid: "+subdocEntrata.getUid()+"] del documento: "+subdocEntrata.getDocumento().getDescAnnoNumeroTipoDoc() + " non associato ad un atto amministrativo."));
    	}
    	
    	if(subdocEntrata.getImportoDaIncassare().signum()<0){
    		log.debug(methodName, String.format(pattern, "l'importo da incassare è negativo."));
    		return false;
//    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdocEntrata.getNumero() + " [uid: "+subdocEntrata.getUid()+"] del documento: "+subdocEntrata.getDocumento().getDescAnnoNumeroTipoDoc() + " con importo da incassare negativo."));
    	}
    	
    	//aggiungo un controllo, anche se non è in analisi: l'accertamento deve esistere e deve avere il capitolo!
    	if(subdocEntrata.getAccertamento() == null){
    		log.debug(methodName, String.format(pattern, "l'accertamento è null."));
    		return false;
//    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdocEntrata.getNumero() + " [uid: "+subdocEntrata.getUid()+"] del documento: "+subdocEntrata.getDocumento().getDescAnnoNumeroTipoDoc() + " non associato ad un accertamento."));
    	}
    	if(subdocEntrata.getAccertamento().getCapitoloEntrataGestione() == null){
    		log.debug(methodName, String.format(pattern, "il capitolo dell'accertamento è null."));
    		return false;
//    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("accertamento del subdocumento " + subdocEntrata.getNumero() + " [uid: "+subdocEntrata.getUid()+"] del documento: "+subdocEntrata.getDocumento().getDescAnnoNumeroTipoDoc() + " non associato ad un capitolo."));
    	}
    	return true;
	}
	
	/**
	 * Verifica che sussistano le condizioni per l'emissione dell'ordinativo di spesa subordinato. 
	 * <br/>
	 * @param attoAmministrativo 
	 * @param subdoc the subdocumento
	 * 
	 * @return <code>true</code> se la quota rispetta una delle seguenti condizioni:
	 * 				<ul>
	 *     				<li>StatoOperativoDocumento  AV-valido,  L-Liquidato ,  PL –parzialmente liquidato, PE – Parzialmente emesso </li>
	 *     				<li>AttoAmministrativo uguale all'AttoAmministrativo del documento padre</li>
	 *     				<li>importoDaPagare > 0</li>
	 *     				</li>tipoConvalida non nullo</li>
	 * 				</ul>
	 * 			<code>false</code> in caso contrario
	 *
	 */
	protected boolean checkSubdocSpesaFiglio(SubdocumentoSpesa subdocSpesa, AttoAmministrativo attoAmministrativo) {
		final String methodName = "checkSubdocSpesaFiglio";
		final String pattern = "Scarto il subdoc con uid " + subdocSpesa.getUid() + ": %s";
		
		if(subdocSpesa.getOrdinativo() != null && subdocSpesa.getOrdinativo().getUid() != 0){
			log.debug(methodName, String.format(pattern, "la quota ha già ordinativo associato"));
			return false;
		}
		
		DocumentoSpesa doc = subdocSpesa.getDocumento();
    	if(!StatoOperativoDocumento.VALIDO.equals(doc.getStatoOperativoDocumento()) && 
    	   !StatoOperativoDocumento.LIQUIDATO.equals(doc.getStatoOperativoDocumento()) &&
    	   !StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO.equals(doc.getStatoOperativoDocumento()) &&
    	   !StatoOperativoDocumento.PARZIALMENTE_EMESSO.equals(doc.getStatoOperativoDocumento())){
    		log.debug(methodName, String.format(pattern, "lo stato del documento non è accettabile."));
    		return false;
//    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdocSpesa.getNumero() + " [uid: "+subdocSpesa.getUid()+"] del documento: "+subdocSpesa.getDocumento().getDescAnnoNumeroTipoDoc() + " con stato non accettabile."));
    	}
    	
    	if(subdocSpesa.getFlagConvalidaManuale() == null){
    		log.debug(methodName, String.format(pattern, "il tipo convalida è null."));
    		return false;
//    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdocSpesa.getNumero() + " [uid: "+subdocSpesa.getUid()+"] del documento: "+subdocSpesa.getDocumento().getDescAnnoNumeroTipoDoc() + " con tipo convalida non valorizzato."));
    	}
    	
    	if(subdocSpesa.getAttoAmministrativo() == null || subdocSpesa.getAttoAmministrativo().getUid() != attoAmministrativo.getUid()) {
    		log.debug(methodName, String.format(pattern, "l'atto amministrativo è null"));
    		return false;
//    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdocSpesa.getNumero() + " [uid: "+subdocSpesa.getUid()+"] del documento: "+subdocSpesa.getDocumento().getDescAnnoNumeroTipoDoc() + " non associato ad un atto amministrativo."));
    	}
    	
    	if(subdocSpesa.getImportoDaPagare().signum()<0){
    		log.debug(methodName, String.format(pattern, "l'importo da pagare è negativo."));
    		return false;
//    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdocSpesa.getNumero() + " [uid: "+subdocSpesa.getUid()+"] del documento: "+subdocSpesa.getDocumento().getDescAnnoNumeroTipoDoc() + " con importo da pagare negativo."));
    	}
    	
    	//aggiungo un controllo, anche se non è in analisi: la liquidazione deve esistere e deve avere il capitolo!
    	if(subdocSpesa.getLiquidazione() == null){
    		log.debug(methodName, String.format(pattern, "la liquidazione è null."));
    		return false;
//    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("subdocumento " + subdocSpesa.getNumero() + " [uid: "+subdocSpesa.getUid()+"] del documento: "+subdocSpesa.getDocumento().getDescAnnoNumeroTipoDoc() + " non associato ad una liquidazione."));
    	}
    	if(subdocSpesa.getLiquidazione().getCapitoloUscitaGestione() == null){
    		log.debug(methodName, String.format(pattern, "il capitolo della liquidazione è null."));
    		return false;
//    		throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("liquidazione del subdocumento " + subdocSpesa.getNumero() + " [uid: "+subdocSpesa.getUid()+"] del documento: "+subdocSpesa.getDocumento().getDescAnnoNumeroTipoDoc() + " non associata ad un capitolo."));
    	}
    	return true;
	}
	
	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*-------------------------------------------- RICHIAMI A SERVIZI ESTERNI -----------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Inserisce l'ordinativo di pagamento.
	 * 
	 * @param ordinativoPagamento l'ordiantivo da inserire
	 * 
	 * @return l'ordinativo inserito
	 */
	protected OrdinativoPagamento inserisceOrdinativoPagamento(OrdinativoPagamento ordinativoPagamento) {
		InserisceOrdinativoPagamento reqIO = new InserisceOrdinativoPagamento();
		reqIO.setRichiedente(req.getRichiedente());
		reqIO.setEnte(ente);
		reqIO.setBilancio(bilancio);
		reqIO.setOrdinativoPagamento(ordinativoPagamento);
		InserisceOrdinativoPagamentoResponse resIO = serviceExecutor.executeServiceSuccess(InserisceOrdinativoPagamentoService.class, reqIO);
		checkServiceResponseFallimento(resIO);
		//SIAC-8017-CMTO
		impostaMessaggiInResponse(resIO.getMessaggi());
		return resIO.getOrdinativoPagamentoInserito();
		
	}
	
	/**
	 * Inserisce l'ordinativo di incasso.
	 * 
	 * @param ordinativoIncasso l'ordiantivo da inserire
	 * 
	 * @return l'ordinativo inserito
	 */
	protected OrdinativoIncasso inserisceOrdinativoIncasso(OrdinativoIncasso ordinativoIncasso) {
		InserisceOrdinativoIncasso reqIO = new InserisceOrdinativoIncasso();
		reqIO.setRichiedente(req.getRichiedente());
		reqIO.setEnte(ente);
		reqIO.setBilancio(bilancio);
		reqIO.setOrdinativoIncasso(ordinativoIncasso);
		// ATTENZIONE! Purtroppo qui parte una transazione separata!!!! e questo e' un grave problema di architettura!!!
		// Cfr. punto 4 della mail di Domenico Lisi (ovvero io!) inviata al CSI il 6/11/2014
//		InserisceOrdinativoIncassoResponse resIO = ordinativoService.inserisceOrdinativoIncasso(reqIO);
		InserisceOrdinativoIncassoResponse resIO = serviceExecutor.executeServiceSuccess(InserisceOrdinativoIncassoService.class, reqIO);
		checkServiceResponseFallimento(resIO);
		
		impostaMessaggiInResponse(resIO.getMessaggi());
		
		return resIO.getOrdinativoIncassoInserito();
	}
	
	protected abstract void impostaMessaggiInResponse(List<Messaggio> messaggi);

	/**
	 * Popola i campi comuni alla ricerca di accertamento e subaccertamento della request per il servizio {@link RicercaAccertamentoPerChiaveOttimizzatoService}
	 *
	 * @return la request creata
	 * */
	private RicercaAccertamentoPerChiaveOttimizzato popolaCampiComuniRequestRicercaAccertamentoPerChiaveOttimizzato(Accertamento accertamento) {
		
		RicercaAccertamentoPerChiaveOttimizzato reqRAPCO = new RicercaAccertamentoPerChiaveOttimizzato();
		reqRAPCO.setRichiedente(req.getRichiedente());
		reqRAPCO.setEnte(ente);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		parametriElencoIds.setEscludiAnnullati(true);
		//SIAC-5712
		parametriElencoIds.setCaricaFlagAttivaGsa(true);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		pRicercaAccertamentoK.setAnnoEsercizio(bilancio.getAnno());
		pRicercaAccertamentoK.setAnnoAccertamento(accertamento.getAnnoMovimento());
		pRicercaAccertamentoK.setNumeroAccertamento(accertamento.getNumeroBigDecimal());
		
		reqRAPCO.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		reqRAPCO.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
		reqRAPCO.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		reqRAPCO.setEscludiSubAnnullati(true);
		return reqRAPCO;
	}
	/***
	 *  Ottiene l'accertamento chiamando il servizio {@link RicercaAccertamentoPerChiaveOttimizzato}
	 *  
	 *  @param key la chiave dell'accertamento cercato
	 *  @param reqRAPCO la request da utilizzare per il servizio
	 *  
	 *  @return l'accertamento ottenuto
	 * **/
	private Accertamento ottieniAccertamentoDaServizio(String key, RicercaAccertamentoPerChiaveOttimizzato reqRAPCO) {
		final String methodName="ottieniAccertamentoDaServizio";
		RicercaAccertamentoPerChiaveOttimizzatoResponse resRA = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(reqRAPCO);
		if(!resRA.isFallimento() && resRA.hasErrori()) {
			log.debug(methodName, "Errori nella response senza impostare il FALLIMENTO per la chiave " + key + ": impostazione del dato a mano");
			resRA.setEsito(Esito.FALLIMENTO);
		}
		//SIAC-8220 si rende parlante l'errore, non controllo per esito della response
		if(resRA.getAccertamento() == null) {
			log.debug(methodName, "Nessun dato trovato per la chiave " + key + ". Aggiungo l'errore nella response");
			resRA.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", key));
			resRA.setEsito(Esito.FALLIMENTO);
		}
		checkServiceResponseFallimento(resRA);
		Accertamento accertamentoRes = resRA.getAccertamento();
		log.debug(methodName, "Data la chiave " + key + " trovata accertamento " + accertamentoRes.getUid());
		//SIAC-5712
		if(!reqRAPCO.isCaricaSub() && resRA.getElencoSubAccertamentiTuttiConSoloGliIds() != null) {
			//non ho richiesto di caricare i subma ho comunque dei dati minimi in piu'
			accertamentoRes.setSubAccertamenti(resRA.getElencoSubAccertamentiTuttiConSoloGliIds());
		}
		
		return accertamentoRes;
	}
	
	/**
	 * Ricerca l'accertamento per chiave.
	 * 
	 * @param accertamento l'accertamento da cercare
	 * 
	 * @return l'accertamento trovata
	 */
	protected Accertamento ricercaAccertamentoPerChiave(Accertamento accertamento, SubAccertamento subaccertamento) {
		final String methodName = "ricercaAccertamentoPerChiave";
		if(accertamento == null) {
			log.debug(methodName, "Accertamento null");
			return null;
		}
		final String key = accertamento.getAnnoMovimento() + "/" + accertamento.getNumeroBigDecimal();
		RicercaAccertamentoPerChiaveOttimizzato reqRAPCO = popolaCampiComuniRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento);
		if(subaccertamento != null){
			log.debug(methodName, "subaccertamento null. Carico tutti i sub");
			reqRAPCO.getpRicercaAccertamentoK().setNumeroSubDaCercare(subaccertamento.getNumeroBigDecimal());
			reqRAPCO.setCaricaSub(true);
		}
		
		return ottieniAccertamentoDaServizio(key, reqRAPCO);
	}

	
	
	/**
	 * Ricerca l'accertamento per chiave.
	 * 
	 * @param accertamento l'accertamento da cercare
	 * 
	 * @return l'accertamento trovata
	 */
	protected Accertamento ricercaAccertamentoPerChiave(Accertamento accertamento) {
		final String methodName = "ricercaAccertamentoPerChiave";
		if(accertamento == null) {
			log.debug(methodName, "Accertamento null");
			return null;
		}
		final String key = accertamento.getAnnoMovimento() + "/" + accertamento.getNumeroBigDecimal();
		RicercaAccertamentoPerChiaveOttimizzato reqRAPCO = popolaCampiComuniRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento);
		reqRAPCO.setCaricaSub(false);
		
		return ottieniAccertamentoDaServizio(key, reqRAPCO);
		
	}

	private RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(Accertamento accertamento) {
		RicercaAttributiMovimentoGestioneOttimizzato parametri = new RicercaAttributiMovimentoGestioneOttimizzato();
		parametri.setEscludiSubAnnullati(true);
		parametri.setCaricaSub(false);
				
		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		parametriElencoIds.setEscludiAnnullati(true);
		parametri.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		pRicercaAccertamentoK.setAnnoEsercizio(bilancio.getAnno());
		pRicercaAccertamentoK.setAnnoAccertamento(accertamento.getAnnoMovimento());
		pRicercaAccertamentoK.setNumeroAccertamento(accertamento.getNumeroBigDecimal());
		
		RicercaAccertamentoPerChiaveOttimizzato reqRAPCO = new RicercaAccertamentoPerChiaveOttimizzato();
		reqRAPCO.setRichiedente(req.getRichiedente());
		reqRAPCO.setEnte(ente);
		
		reqRAPCO.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		reqRAPCO.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametri.getDatiOpzionaliElencoSubTuttiConSoloGliIds());
		reqRAPCO.setCaricaSub(false);
		reqRAPCO.setEscludiSubAnnullati(true);
		return reqRAPCO;
	}
	
	/**
	 * Ricerca il soggetto per chiave.
	 * 
	 * @param soggetto il soggetto da cercare
	 * 
	 * @return il soggetto trovato
	 */
	protected Soggetto ricercaSoggettoPerChiave(Soggetto soggetto) {
		final String methodName = "ricercaSoggettoPerChiave";
		if(soggetto == null) {
			log.debug(methodName, "Soggetto null");
			return null;
		}
		final String key = soggetto.getCodiceSoggetto();
		
		RicercaSoggettoPerChiave reqRS = new RicercaSoggettoPerChiave();
		reqRS.setDataOra(new Date());
		reqRS.setEnte(ente);
		reqRS.setRichiedente(req.getRichiedente());
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(soggetto.getCodiceSoggetto());
		reqRS.setParametroSoggettoK(parametroSoggettoK);
		
		RicercaSoggettoPerChiaveResponse resRS = soggettoService.ricercaSoggettoPerChiave(reqRS);
		// Controllo dell'esito ed eventuale forzatura
		if(!resRS.isFallimento() && resRS.hasErrori()) {
			log.debug(methodName, "Errori nella response senza impostare il FALLIMENTO per la chiave " + key + ": impostazione del dato a mano");
			resRS.setEsito(Esito.FALLIMENTO);
		}
		if(!resRS.isFallimento() && resRS.getSoggetto() == null) {
			log.debug(methodName, "Nessun dato trovato per la chiave " + key + ". Aggiungo l'errore nella response");
			resRS.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", key));
			resRS.setEsito(Esito.FALLIMENTO);
		}
		checkServiceResponseFallimento(resRS);
		Soggetto soggettoDaServizio = resRS.getSoggetto();
		// Il servizio imposta le modalita' di pagamento nel posto sbagliato. Le imposto in quello corretto
		soggettoDaServizio.setElencoModalitaPagamento(resRS.getListaModalitaPagamentoSoggetto());
		
		log.debug(methodName, "Data la chiave " + key + " trovato soggetto " + soggettoDaServizio.getUid());
		return soggettoDaServizio;
	}
	
	/**
	 * Ricerca la liquidazione per chiave.
	 * 
	 * //[LiquidazioneServiceImpl.ricercaLiquidazionePerChiave] - END SERVICE - 65879 ms.. !!!!!!!!!!!!!!!!!!!!!!
	 * 
	 * @param liquidazione la liquidazione da cercare
	 * 
	 * @return la liquidazione trovata
	 */
	protected Liquidazione ricercaLiquidazionePerChiave(Liquidazione liquidazione) {
		final String methodName = "ricercaLiquidazionePerChiave";
		if(liquidazione == null) {
			log.debug(methodName, "Liquidazione null");
			return null;
		}
		final String key = liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione();
		RicercaLiquidazionePerChiave reqRL = new RicercaLiquidazionePerChiave();
		reqRL.setRichiedente(req.getRichiedente());
		reqRL.setEnte(ente);
		reqRL.setDataOra(new Date());
		
		RicercaLiquidazioneK ricercaLiquidazioneK = new RicercaLiquidazioneK();
		ricercaLiquidazioneK.setBilancio(bilancio);
		ricercaLiquidazioneK.setAnnoEsercizio(bilancio.getAnno());
		ricercaLiquidazioneK.setAnnoLiquidazione(liquidazione.getAnnoLiquidazione());
		ricercaLiquidazioneK.setNumeroLiquidazione(liquidazione.getNumeroLiquidazione());
		ricercaLiquidazioneK.setLiquidazione(liquidazione);
		ricercaLiquidazioneK.setTipoRicerca(CostantiFin.TIPO_RICERCA_DA_EMISSIONE_ORDINATIVO);
		
		reqRL.setpRicercaLiquidazioneK(ricercaLiquidazioneK);
		RicercaLiquidazionePerChiaveResponse resRL = liquidazioneService.ricercaLiquidazionePerChiave(reqRL);
		if(!resRL.isFallimento() && resRL.hasErrori()) {
			log.debug(methodName, "Errori nella response senza impostare il FALLIMENTO per la chiave " + key + ": impostazione del dato a mano");
			resRL.setEsito(Esito.FALLIMENTO);
		}
		if(!resRL.isFallimento() && resRL.getLiquidazione() == null) {
			log.debug(methodName, "Nessun dato trovato per la chiave " + key + ". Aggiungo l'errore nella response");
			resRL.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Liquidazione", key));
			resRL.setEsito(Esito.FALLIMENTO);
		}
		checkServiceResponseFallimento(resRL);
		
		Liquidazione liquidazioneDaServizio = resRL.getLiquidazione();
		log.debug(methodName, "Data la chiave " + key + " trovata liquidazione " + liquidazioneDaServizio.getUid());
		return liquidazioneDaServizio;
	}
	
	
	protected Liquidazione leggiDisponibilitaALiquidare(Liquidazione liquidazione) {
		final String methodName = "leggiDisponibilitaALiquidare";
		if(liquidazione == null) {
			log.debug(methodName, "Liquidazione null");
			return null;
		}
		final String key = liquidazione.getAnnoLiquidazione() + "/" + liquidazione.getNumeroLiquidazione();
		CalcolaDisponibilitaALiquidare reqDispLiquidare = new CalcolaDisponibilitaALiquidare();
		reqDispLiquidare.setRichiedente(req.getRichiedente());
		reqDispLiquidare.setEnte(ente);
		reqDispLiquidare.setBilancio(bilancio);
		reqDispLiquidare.setLiquidazione(liquidazione);
		
		CalcolaDisponibilitaALiquidareResponse resDispLiquidare = liquidazioneService.calcolaDisponibilitaALiquidare(reqDispLiquidare);
		
		if(!resDispLiquidare.isFallimento() && resDispLiquidare.hasErrori()) {
			log.debug(methodName, "Errori nella response senza impostare il FALLIMENTO per la chiave " + key + ": impostazione del dato a mano");
			resDispLiquidare.setEsito(Esito.FALLIMENTO);
		}
		
		checkServiceResponseFallimento(resDispLiquidare);
		
		log.debug(methodName, "Data la chiave " + key + " trovata disponibilitaALiquidare =  " +resDispLiquidare.getDisponibilitaALiquidare());
		
		//FIXME: Controllare con Lisi
		liquidazione.setDisponibilitaPagare(resDispLiquidare.getDisponibilitaALiquidare());
		
		return liquidazione;
	}
	
	private SubdocumentoEntrata inserisceQuotaEntrata(SubdocumentoEntrata nuovaQuota) {
		InserisceQuotaDocumentoEntrata reqIQ = new InserisceQuotaDocumentoEntrata();
		reqIQ.setSubdocumentoEntrata(nuovaQuota);
		reqIQ.setBilancio(bilancio);
		reqIQ.setRichiedente(req.getRichiedente());
		InserisceQuotaDocumentoEntrataResponse resIQ = executeExternalServiceSuccess(inserisceQuotaDocumentoEntrataService, reqIQ);
		return resIQ.getSubdocumentoEntrata();
	}
	
	private SubdocumentoSpesa inserisceQuotaSpesa(SubdocumentoSpesa nuovaQuota) {
		InserisceQuotaDocumentoSpesa reqIQ = new InserisceQuotaDocumentoSpesa();
		reqIQ.setSubdocumentoSpesa(nuovaQuota);
		reqIQ.setBilancio(bilancio);
		reqIQ.setRichiedente(req.getRichiedente());
		InserisceQuotaDocumentoSpesaResponse resIQ = executeExternalServiceSuccess(inserisceQuotaDocumentoSpesaService, reqIQ);
		return resIQ.getSubdocumentoSpesa();
	}
	
	/**
	 * Aggiorna stato operativo documento di spesa.
	 *
	 * @param documentoSpesa the documento spesa
	 */
	protected void aggiornaStatoOperativoDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		final String methodName = "aggiornaStatoOperativoDocumentoSpesa";
		AggiornaStatoDocumentoDiSpesa reqAs = new AggiornaStatoDocumentoDiSpesa();
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(documentoSpesa.getUid());
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setDocumentoSpesa(doc);
		reqAs.setBilancio(bilancio);
		AggiornaStatoDocumentoDiSpesaResponse resAs = executeExternalServiceSuccess(aggiornaStatoDocumentoDiSpesaService, reqAs);
		log.debug(methodName, "Aggiornato lo stato operativo per il documento " + documentoSpesa.getUid());
		documentoSpesa.setStatoOperativoDocumento(resAs.getStatoOperativoDocumentoNuovo());
	}
	
	/**
	 * Aggiorna stato operativo documento di entrata.
	 *
	 * @param documentoEntrata the documento entrata
	 */
	protected void aggiornaStatoOperativoDocumentoEntrata(DocumentoEntrata documentoEntrata) {
		final String methodName = "aggiornaStatoOperativoDocumentoEntrata";
		AggiornaStatoDocumentoDiEntrata reqAs = new AggiornaStatoDocumentoDiEntrata();
		DocumentoEntrata doc = new DocumentoEntrata();
		doc.setUid(documentoEntrata.getUid());
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setDocumentoEntrata(doc);
		reqAs.setBilancio(bilancio);
		AggiornaStatoDocumentoDiEntrataResponse resAs = executeExternalServiceSuccess(aggiornaStatoDocumentoDiEntrataService, reqAs);
		log.debug(methodName, "Aggiornato lo stato operativo per il documento " + documentoEntrata.getUid());
		documentoEntrata.setStatoOperativoDocumento(resAs.getStatoOperativoDocumentoNuovo());
	}
	
	/**
	 * Aggiorna stato operativo documento iva.
	 *
	 * @param subdocumentoEntrata the subdocumento entrata
	 * @return the stato operativo documento
	 */
	protected SubdocumentoIvaEntrata aggiornaStatoOperativoDocumentoIvaEntrata(SubdocumentoEntrata subdocumentoEntrata) {
		final String methodName = "aggiornaStatoOperativoDocumentoIvaEntrata";
		AggiornaStatoSubdocumentoIvaEntrata reqAs = new AggiornaStatoSubdocumentoIvaEntrata();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setBilancio(bilancio);
		reqAs.setDataOra(new Date());
		reqAs.setSubdocumentoEntrata(subdocumentoEntrata);
		AggiornaStatoSubdocumentoIvaEntrataResponse resAs = executeExternalServiceSuccess(aggiornaStatoSubdocumentoIvaEntrataService, reqAs);
		log.debug(methodName, "Aggiornato lo stato operativo iva per il documento " + subdocumentoEntrata.getUid());
		return resAs.getSubdocumentoIvaEntrata();
	}
	
	/**
	 * Aggiorna stato operativo documento iva.
	 *
	 * @param subdocumentoSpesa the subdocumento spesa
	 * @return the stato operativo documento
	 */
	protected SubdocumentoIvaSpesa aggiornaStatoOperativoDocumentoIvaSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		final String methodName = "aggiornaStatoOperativoDocumentoIva";
		AggiornaStatoSubdocumentoIvaSpesa reqAs = new AggiornaStatoSubdocumentoIvaSpesa();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setBilancio(bilancio);
		reqAs.setDataOra(new Date());
		reqAs.setSubdocumentoSpesa(subdocumentoSpesa);
		AggiornaStatoSubdocumentoIvaSpesaResponse resAs = executeExternalServiceSuccess(aggiornaStatoSubdocumentoIvaSpesaService, reqAs);
		log.debug(methodName, "Aggiornato lo stato operativo iva per il documento " + subdocumentoSpesa.getUid());
		return resAs.getSubdocumentoIvaSpesa();
	}
	
	
	/**
	 *  Carica i dettagli del soggetto a partire dalla liquidazione
	 * @param liquidazione
	 * @return soggetto
	 */
	protected Soggetto caricaSoggetto(Liquidazione liquidazione) {
		if(liquidazione.getSoggettoLiquidazione() == null || liquidazione.getSoggettoLiquidazione().getUid() == 0){
			return null;
		}
		Soggetto soggetto = soggettiCache.get(liquidazione.getSoggettoLiquidazione().getUid());
		if(soggetto == null){
			soggetto = ricercaSoggettoPerChiave(liquidazione.getSoggettoLiquidazione());
			soggettiCache.put(liquidazione.getSoggettoLiquidazione().getUid(), soggetto);
		}
		return soggetto;
	}
	
	
	/**
	 * Carica i dettagli del documento padre di una quota 
	 * @param subdocumentoSpesa la quota
	 * @return documento il documento padre
	 */
	protected DocumentoSpesa caricaDocumento(SubdocumentoSpesa subdocumentoSpesa) {
		DocumentoSpesa documento = documentiSpesaCache.get(subdocumentoSpesa.getDocumento().getUid());
		if(documento == null){
			//FIXME: valutare se non va comuinque bene caricare solo le ritenute
			documento = documentoSpesaDad.findDocumentoSpesaById(subdocumentoSpesa.getDocumento().getUid());
			documentiSpesaCache.put(subdocumentoSpesa.getDocumento().getUid(), documento);
		}
		return documento;
	}
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*-------------------------------- CALCOLI STRINGHE PER LE CHIAVI DI RAGGRUPPAMENTO -------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Calcolo della stringa dell'atto amministrativo.
	 * 
	 * @param attoAmministrativo l'atto di cui calcolare la chiave
	 * 
	 * @return la chiave dell'atto
	 */
	protected String computeKeyAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		final StringBuilder sb = new StringBuilder();
		// Atto Amministrativo  => anno, numero, Tipo, Struttura Amministrativa
		
		sb.append("attoAmm")
			.append("<");
		if(attoAmministrativo == null) {
			sb.append("null");
		} else {
			sb.append(attoAmministrativo.getAnno())
				.append("/")
				.append(attoAmministrativo.getNumero())
				.append("/")
				.append(attoAmministrativo.getTipoAtto() == null ? "null" : attoAmministrativo.getTipoAtto().getUid())
				.append("/")
				.append(attoAmministrativo.getStrutturaAmmContabile() == null ? "null" : attoAmministrativo.getStrutturaAmmContabile().getUid());
		}
		sb.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave del capitolo.
	 * 
	 * @param capitolo il capitolo di cui calcolare la chiave
	 * @return la chiave del capitolo
	 */
	protected String computeKeyCapitolo(Capitolo<?, ?> capitolo) {
		final StringBuilder sb = new StringBuilder();
		// Capitolo => numeroCapitolo, numeroArticolo, Tipo Finanziamento (derivato dal MovimentoGestione)
		
		sb.append("capitolo")
			.append("<");
		if(capitolo == null) {
			sb.append("null");
		} else {
			sb.append(capitolo.getNumeroCapitolo())
				.append("/")
				.append(capitolo.getNumeroArticolo())
				.append("/")
				.append(capitolo.getTipoFinanziamento() == null ? "null" : capitolo.getTipoFinanziamento().getUid());
		}
		sb.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave del movimento di gestione.
	 * 
	 * @param movimentoGestione il movimento di cui calcolare la chiave
	 * @return la chiave del movimento
	 */
	protected String computeKeyMovimentoGestione(MovimentoGestione movimentoGestione) {
		final StringBuilder sb = new StringBuilder();
		// MovimentoGestione => annoMovimento
		
		sb.append("movGest")
			.append("<")
			.append(movimentoGestione == null ? "null" : movimentoGestione.getAnnoMovimento())
			.append(">");
		return sb.toString();
	}
	
	protected String computeKeyPianoDeiContiFinanziario(MovimentoGestione movimentoGestione) {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("codPdC")
			.append("<")
			.append(movimentoGestione == null ? "null" : movimentoGestione.getCodPdc())
			.append(">");
		return sb.toString();
 	}
	
	/**
	 * Calcola la chiave del soggetto.
	 * 
	 * @param soggetto il soggetto di cui calcolare la chiave
	 * @return la chiave del soggetto
	 */
	protected String computeKeySoggetto(Soggetto soggetto) {
		final StringBuilder sb = new StringBuilder();
		// Soggetto
		
		sb.append("sog")
			.append("<")
			.append(soggetto == null ? "null" : soggetto.getCodiceSoggetto())
			.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave della modalita di pagamento.
	 * 
	 * @param modalitaPagamentoSoggetto la modalita di cui calcolare la chiave
	 * @return la chiave della modalita
	 */
	protected String computeKeyModalitaPagamento(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		final StringBuilder sb = new StringBuilder();
		// ModalitaPagamentoSoggetto (da cui discende in modo indiretto anche la rottura per SedeSecondaria)
		
		sb.append("modPagSog")
			.append("<")
			.append(modalitaPagamentoSoggetto == null ? "null" : modalitaPagamentoSoggetto.getUid())
			.append(">");
		return sb.toString();
	}
	
	/**
	 * Calcola la chiave della sede secondaria
	 * 
	 * @param sedeSecondaria la sede di cui calcolare la chiave
	 * @return la chiave della sede
	 */
	protected String computeKeySedeSecondaria(SedeSecondariaSoggetto sedeSecondaria) {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("sedeSecSog")
			.append("<")
			.append(sedeSecondaria == null ? "null" : sedeSecondaria.getUid())
			.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave del conto tesoreria.
	 * 
	 * @param ct il conto di cui calcolare la chiave
	 * @return la chiave del conto
	 */
	protected String computeKeyContoTesoreria(it.csi.siac.siacfin2ser.model.ContoTesoreria ct) {
		final StringBuilder sb = new StringBuilder();
		// Conto di Tesoreria => se impostato il corrispondente parametro dell'operazione non e' significativo 
		sb.append("contoTes")
			.append("<");
		if(contoTesoreria != null) {
			sb.append(contoTesoreria.getUid());
		} else {
			sb.append(ct == null ? "null" : ct.getUid());
		}
		sb.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave della distinta.
	 * 
	 * @param d la distinta di cui calcolare la chiave
	 * @return la chiave della distinta
	 */
	protected Object computeKeyDistinta(Distinta d) {
		final StringBuilder sb = new StringBuilder();
		// Conto di Tesoreria => se impostato il corrispondente parametro dell'operazione non e' significativo 
		sb.append("distinta")
			.append("<");
		if(distinta != null) {
			sb.append(distinta.getUid());
		} else {
			sb.append(d == null ? "null" : d.getUid());
		}
		sb.append(">");
		return sb.toString();
	}
	
	/**
	 * Compute key tipo debito siope.
	 *
	 * @param std the std
	 * @return the string
	 */
	//SIOPE+
	protected String computeKeyTipoDebitoSiope(SiopeTipoDebito std) {
		final StringBuilder sb = new StringBuilder();
		sb.append("sioTipDeb")
			.append("<")
			.append(std !=null? std.getUid() : "null")
			.append(">");
		return sb.toString();
	}
	
	/**
	 * Compute key cig motivo assenza.
	 *
	 * @param cig the cig
	 * @return the string
	 */
	protected String computeKeyCig(String cig) {
		final StringBuilder sb = new StringBuilder();
		sb.append("cig")
			.append("<")
			.append(StringUtils.isNotEmpty(cig)? cig : "null")
			.append(">");
		return sb.toString();
	}
	
	/**
	 * Compute key siope assenza motivazione.
	 *
	 * @param sam the sam
	 * @return the string
	 */
	protected String computeKeySiopeAssenzaMotivazione(SiopeAssenzaMotivazione sam) {
		final StringBuilder sb = new StringBuilder();
		sb.append("sioAssMot")
			.append("<")
			.append(sam!= null? sam.getUid() : "null")
			.append(">");
		return sb.toString();
	}
	
	/**
	 * Calcola la chiave del codice bollo.
	 * 
	 * @param cb il codice di cui calcolare la chiave
	 * @return la chiave del codice
	 */
	protected String computeKeyCodiceBollo(it.csi.siac.siacfin2ser.model.CodiceBollo cb) {
		final StringBuilder sb = new StringBuilder();
		// Codice Bollo
		sb.append("codBollo")
			.append("<")
			.append(cb == null ? "null" : cb.getUid())
			.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave del flag avviso.
	 * 
	 * @param flag il flag di cui calcolare la chiave
	 * @return la chiave del flag
	 */
	protected String computeKeyFlagAvviso(Boolean flag) {
		final StringBuilder sb = new StringBuilder();
		// Codice Bollo
		sb.append("flagAvviso")
			.append("<")
			.append(flag)
			.append(">");
		return sb.toString();
	}
	
	/**
	 * Calcola la chiave della nota tesoriere.
	 * 
	 * @param noteTesoriere la nota di cui calcolare la chiave
	 * @return la chiave della nota
	 */
	protected String computeKeyNote(String note) {
		final StringBuilder sb = new StringBuilder();
		// Codice Bollo
		sb.append("noteTes")
			.append("<")
			.append(note == null ? "null" : note)
			.append(">");
		return sb.toString();
	}

	/**
	 * Calcola la chiave del flag a copertura.
	 * 
	 * @param flag il flag di cui calcolare la chiave
	 * @return la chiave del flag
	 */
	protected String computeKeyFlagACopertura(Boolean flag) {
		final StringBuilder sb = new StringBuilder();
		// Codice Bollo
		sb.append("flagACopertura")
			.append("<")
			.append(flag)
			.append(">");
		return sb.toString();
	}
	
	/**
	 * Calcola la chiave del data scadenza.
	 * 
	 * @param flag il flag di cui calcolare la chiave
	 * @return la chiave del flag
	 */
	protected String computeKeyDataScadenza(Date dataScadenza) {
		final StringBuilder sb = new StringBuilder();
		// Codice Bollo
		sb.append("dataScadenza")
			.append("<")
			.append(dataScadenza)
			.append(">");
		return sb.toString();
	}
	
	
	
	/*-----------------------------------------------------------------------------------------------------------------------*/
	/*------------------------------------------------- ALTRI METODI --------------------------------------------------------*/
	/*-----------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Creazione delle note tesoriere del modulo FIN a partire dalle note tesoriere del modulo BIL.
	 * 
	 * @param noteTesoriere le note BIL
	 * @return le note FIN
	 */
	private NoteTesoriere noteTesoriereFinFromNoteTesoriereBil(it.csi.siac.siacfin2ser.model.NoteTesoriere noteTesoriere) {
		if(noteTesoriere == null) {
			return null;
		}
		NoteTesoriere nt = new NoteTesoriere();
		codificaFinFromCodificaBil(nt, noteTesoriere);
		return nt;
	}
	
	/**
	 * Creazione del codice bollo del modulo FIN a partire dal codice bollo del modulo BIL.
	 * 
	 * @param codiceBollo il codice BIL
	 * @return il codice FIN
	 */
	private CodiceBollo codiceBolloFinFromCodiceBolloBil(it.csi.siac.siacfin2ser.model.CodiceBollo codiceBollo) {
		if(codiceBollo == null) {
			return null;
		}
		CodiceBollo cb = new CodiceBollo();
		codificaFinFromCodificaBil(cb, codiceBollo);
		return cb;
	}

	/**
	 * Creazione del conto tesoreria del modulo FIN a partire dal conto tesoreria del modulo BIL.
	 * 
	 * @param contoTesoreria il conto BIL
	 * @return il conto FIN
	 */
	protected ContoTesoreria contoTesoreriaFinFromContoTesoreriaBil(it.csi.siac.siacfin2ser.model.ContoTesoreria contoTesoreria) {
		if(contoTesoreria == null) {
			return null;
		}
		ContoTesoreria ct = new ContoTesoreria();
		ct.setUid(contoTesoreria.getUid());
		ct.setCodice(contoTesoreria.getCodice());
		ct.setDescrizione(contoTesoreria.getDescrizione());
		return ct;
	}
	
	/**
	 * Creazione del conto tesoreria del modulo FIN a partire dal conto tesoreria del modulo BIL.
	 * 
	 * @param contoTesoreria il conto BIL
	 * @return il conto FIN
	 */
	protected it.csi.siac.siacfin2ser.model.ContoTesoreria contoTesoreriaBilFromContoTesoreriaFin(ContoTesoreria contoTesoreria) {
		if(contoTesoreria == null) {
			return null;
		}
		it.csi.siac.siacfin2ser.model.ContoTesoreria ct = new it.csi.siac.siacfin2ser.model.ContoTesoreria();
		ct.setUid(contoTesoreria.getUid());
		ct.setCodice(contoTesoreria.getCodice());
		ct.setDescrizione(contoTesoreria.getDescrizione());
		return ct;
	}
	
	/**
	 * Trasposta i dati dalla codifica BIL alla codifica FIN.
	 * 
	 * @param codificaFin la codifica FIN
	 * @param codificaBil la codifica BIL
	 */
	private void codificaFinFromCodificaBil(Codifica codificaFin, Codifica codificaBil) {
		codificaFin.setUid(codificaBil.getUid());
		codificaFin.setCodice(codificaBil.getCodice());
		codificaFin.setDescrizione(codificaBil.getDescrizione());
	}
	
	/**
	 * Raggruppa i SubDocumenti per ProvvisorioDiCassa e per ciascuno inserire un entit&agrave; RegolarizzazioneProvvisorio di importo = alla somma SubDocumento.importo.
	 * 
	 * @param quote le quote da cui creare le regolarizzazioni
	 */
	private <S extends Subdocumento<?, ?>> List<RegolarizzazioneProvvisorio> creaGruppiRegolarizzazioneProvvisorio(List<S> quote) {
		final String methodName = "creaGruppiRegolarizzazioneProvvisorio";
		
		Map<String, RegolarizzazioneProvvisorio> group = new HashMap<String, RegolarizzazioneProvvisorio>();
		for(S s : quote){
			if(s.getProvvisorioCassa() == null) {
				log.debug(methodName, "Quota " + s.getUid() + " senza provvisorio di cassa");
				// Se non ho il provvisorio, continuo
				continue;
			}
			String key = "idProvCassa" + s.getProvvisorioCassa().getIdProvvisorioDiCassa() + "/" + s.getProvvisorioCassa().getUid();
			
			if(!group.containsKey(key)){
				RegolarizzazioneProvvisorio rpnew = new RegolarizzazioneProvvisorio();
				//SIAC-7127
				rpnew.setImporto(s.getImportoNotNull().subtract(s.getImportoDaDedurre()));
				rpnew.setProvvisorioDiCassa(s.getProvvisorioCassa());
				group.put(key, rpnew);
			}else{
				log.debug(methodName, "la key è già contenuta nella mappa, aggiorno solamente l'importo");
				RegolarizzazioneProvvisorio rp = group.get(key);
				//SIAC-7127
				BigDecimal importo = rp.getImporto().add(s.getImportoNotNull().subtract(s.getImportoDaDedurre()));
				rp.setImporto(importo);
			}
			
		}
		Collection<RegolarizzazioneProvvisorio> values = group.values();
		
		// Nel caso dell'HashMap il controllo e' inutile. Non (necessariamente) cosi' per le altre implementazioni di java.util.Map
//		if(values == null) {
//			log.debug(methodName, "Inizializzazione della lista delle regolarizzazioni");
//			values = new ArrayList<RegolarizzazioneProvvisorio>();
//		}
		
		return new ArrayList<RegolarizzazioneProvvisorio>(values);
	}

	/**
	 * Concatenazione di tutte le descrizioni dei subdocumenti evitando i doppioni.
	 * 
	 * @param quote le quote da cui ottenere le descrizioni
	 * 
	 * @return le descrizioni distinte, concatenate
	 */
	private <S extends Subdocumento<?, ?>> String getDescrizioniDistinte(List<S> quote) {
		final String methodName = "getDescrizioniDistinte";
		Map<String, String> mapDescrizioni = new HashMap<String,String>();
		for(S ss : quote){
			mapDescrizioni.put(ss.getDescrizione(), "");
		}
		StringBuilder sb = new StringBuilder();
		for(String desc : mapDescrizioni.keySet()){
			sb.append("; ").append(desc);
		}
		
		String descrizione = sb.toString();
		descrizione = descrizione.replaceFirst("; ", "");
		// Troncare se supera il limite massimo previsto dal campo del DB
		if(descrizione.length() > MAX_DESCRIZIONE_CHARS) {
			log.debug(methodName, "Descrizione: " + descrizione + "\n" + descrizione.length() + " caratteri: troppo lunga per il db. Troncamento a " + MAX_DESCRIZIONE_CHARS + " caratteri");
			descrizione = descrizione.substring(0, MAX_DESCRIZIONE_CHARS);
		}
		return descrizione;
	}

	protected void startElaborazioneQuote(List<? extends Subdocumento<?, ?>> quoteDaEmettere) {
		String methodName = "startElaborazioneQuote";
		//SIAC-4906
		Map<String, String[]> mappaChiaviElaborazione = getMappaChiaviElaborazione(quoteDaEmettere);
		for(Entry<String, String[]> entry : mappaChiaviElaborazione.entrySet()) {
			//FORMALMENTE, posso avere piu' elab services. mappaChiaviElaborazione non dovrebbe accadere nel caso dell'emissione, ma mi mantengo generale
			try {
				elaborazioniManager.startElaborazioni(entry.getKey(), entry.getValue());
			} catch (ElaborazioneAttivaException eae){
				String msg = "L'elaborazione di alcune quote e' gia' in corso. Attendere il termine della prima elaborazione. ["+eae.getMessage()+"]";
				log.error(methodName, msg, eae);
				throw new BusinessException(ErroreBil.ELABORAZIONE_ATTIVA.getErrore(msg));
			}
		}	
	}
//	
	private Map<String, String[]> getMappaChiaviElaborazione(List<? extends Subdocumento<?, ?>> quoteDaEmettere) {
		Class<?> chiamanteClazz = this.getClass();
		ElabKeys elabKey = ElabKeys.EMISSIONE_ORDINATIVI_PAGAMENTO;
		Map<String, List<String>> tmp = new HashMap<String, List<String>>();
		
		for (Subdocumento<?, ?> subdocumento : quoteDaEmettere) {
			ElaborazioniAttiveKeyHandler eakh = new ElaborazioniAttiveKeyHandler(subdocumento.getUid(), chiamanteClazz);
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

	protected void endElaborazioneQuote(List<? extends Subdocumento<?, ?>> quoteDaEmettere) {
		//SIAC-4906
		Map<String, String[]> mappaChiaviElaborazione = getMappaChiaviElaborazione(quoteDaEmettere);
		for(Entry<String, String[]> entry : mappaChiaviElaborazione.entrySet()) {
			//FORMALMENTE, posso avere piu' elab services. mappaChiaviElaborazione non dovrebbe accadere nel caso dell'emissione, ma mi mantengo generale
			elaborazioniManager.endElaborazioni(entry.getKey(), entry.getValue());

		}
	}
	
	protected SubAccertamento cercaSubAccertamento(Accertamento accertamento, SubAccertamento subAccertamento) {
		if(subAccertamento == null) {
			return null;
		}
		for(SubAccertamento sub : accertamento.getElencoSubAccertamenti()){
			if(sub.getUid() == subAccertamento.getUid()){
				return sub;
			}
		}
		return null;
	}
	//SIAC-5937
	
	/**
	 * Controlla se il bilancio attuale e quello dell'anno successivo (qualora presente) soddisfino o meno le condizioni per la doppia gestione.
	 *
	 * @return <code>true</code> se sono soddisfatte le condizioni per la doppia gestione, <code>false</code> altrimenti
	 */
	private boolean isBilancioInDoppiaGestione() {
		final String methodName = "isBilancioInDoppiaGestione";		
		boolean  faseAttualeCongruente = this.bilancio.getFaseEStatoAttualeBilancio() != null && FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(this.bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio());
		boolean bilancioSuccessivoPresente =  this.bilancioAnnoSuccessivo != null && this.bilancioAnnoSuccessivo.getUid() != 0;
		
		log.debug(methodName, "fase attuale compatibile con la doppia gestione?" + faseAttualeCongruente + " . Bilancio successivo presente? " + bilancioSuccessivoPresente);
		
		if(!(faseAttualeCongruente && bilancioSuccessivoPresente)) {
			//la se del bilancio corrente o il bilancio successivo non sono presenti o non sono compatibili. Esco.
			log.debug(methodName, "Condizioni sulla doppia gestione del bilancio non soddisfatte. Return false.");
			return false;
		}

		boolean faseBilanciosuccessivoCongruente = this.bilancioAnnoSuccessivo.getFaseEStatoAttualeBilancio() != null &&  FaseBilancio.ESERCIZIO_PROVVISORIO.equals(this.bilancioAnnoSuccessivo.getFaseEStatoAttualeBilancio().getFaseBilancio());
		if(!faseBilanciosuccessivoCongruente) {
			//La fase del bilancio successivo risulta essere diversa da quella necessaria per la doppia gestione
			log.debug(methodName, "La fase del bilancio successivo risulta essere diversa da quella necessaria per la doppia gestione ( " + FaseBilancio.ESERCIZIO_PROVVISORIO.name() + "). Return false.");
			return false;
		}		
		
		//se sono arrivata fino a qui, il bilancio e' di doppia gestione
		log.debug(methodName, "Il bilancio attuale risulta essere un bilancio di doppia gestione. Fase bilancio attuale : predisposizione consuntivo, fase bilancio anno successivo: esercizio provvisorio.");
		return true;
	}

	/**
	 * Se necessario, effettuale le operazioni necessarie alla doppia gestione.
	 *
	 * @param quota the quota per la quale eventualmente effettuare tali operazioni
	 */
	protected void gestisciDoppiaGestioneSubDocumentoSpesaSeNecessario(SubdocumentoSpesa quota) {
		List<SubdocumentoSpesa> quoteValide = new ArrayList<SubdocumentoSpesa>();
		quoteValide.add(quota);
		//centralizzo e chiamo il servizio per piu' quote
		gestisciDoppiaGestioneSubDocumentoSpesaSeNecessario(quoteValide);
	}
	

	/**
	 * Se necessario, effettua le le operazioni necessarie alla doppia gestione.
	 *
	 * @param quoteValide le quota per le quale eventualmente effettuare tali operazioni
	 */
	protected void gestisciDoppiaGestioneSubDocumentoSpesaSeNecessario(List<SubdocumentoSpesa> quoteValide) {
		if(!this.bilancioInDoppiaGestione) {
			//non sono in doppia gestione: esco. 
			return;
		}
		Date now = new Date();
		for (SubdocumentoSpesa ssValido : quoteValide) {
			collegaScollegaMovimentiCollegatiSpesa(ssValido, now);			
		}
		
	}

	/**
	 * Questo passo è da effettuarsi solo nel caso in cui ci si trovi in un anno di bilancio in PREDISPOSIZIONE CONSUNTIVO ed il bilancio successivo in fase di ESERCIZIO PROVVISORIO.<br/>
	 * Per ogni quota dell’ordinativo inserito:
	 * <ul>
	 * 	<li> Se la liquidazione che si è pagata è passata a residuo nell’anno di bilancio + 1 (attenzione, dopo l’inserimento dell’ordinativo sarà stata cancellata logicamente) e se la suddetta liquidazione residua ha una relazione valida con la quota che si sta pagando è necessario:
	 * 		<ul>
	 * 			<li>Cancellare la relazione con la liquidazione residua dell’anno di bilancio + 1 </li>
	 * 			<li>Riportare a  'sempre valida'  la relazione con la liquidazione dell’anno in corso (data fine validità nulla) </li>
	 * 		</ul>
	 * 	</li>
	 * <li> Se l’impegno/sub che si è pagato è passato a residuo nell’anno di bilancio + 1 (attenzione, dopo l’inserimento dell’ordinativo potrebbe essere stato cancellato logicamente) e se il suddetto impegno residuo ha una relazione valida con la quota che si sta pagando è necessario:
	 * 		<ul>
	 * 			<li>Cancellare la relazione con l’impegno/sub residuo dell’anno di bilancio + 1 </li>
	 * 			<li>Riportare a 'sempre valida' la relazione con l’impegno/sub dell’anno in corso (data fine validità nulla) </li>
	 * 		</ul>
	 * 	</li>
	 * 
	 * </ul>
	 *
	 * @param ssValido il subdoc di spesa su cui effettuare le operazioni di cui sopra
	 * @param now  il now su cui impostare le relazioni
	 */
	private void collegaScollegaMovimentiCollegatiSpesa(SubdocumentoSpesa ssValido, Date now) {
		subdocumentoSpesaDad.collegaScollegaMovimentoGestionePerDoppiaGestione(ssValido, this.bilancio, this.bilancioAnnoSuccessivo, now);
		subdocumentoSpesaDad.collegaScollegaLiquidazionePerDoppiaGestione(ssValido, this.bilancio, this.bilancioAnnoSuccessivo, now);
	}
	
	/**
	 * Se necessario, effettua le le operazioni necessarie alla doppia gestione.
	 *
	 * @param quota la quota per le quale eventualmente effettuare tali operazioni
	 */
	protected void gestisciDoppiaGestioneSubdocumentoEntrataSeNecessario(SubdocumentoEntrata quota) {
		List<SubdocumentoEntrata> quote = new ArrayList<SubdocumentoEntrata>();
		quote.add(quota);
		gestisciDoppiaGestioneSubdocumentoEntrataSeNecessario(quote);
 	}
	
	
	/**
	 * Se necessario, effettua le le operazioni necessarie alla doppia gestione.
	 *
	 * @param quoteValide le quota per le quale eventualmente effettuare tali operazioni
	 */
	protected void gestisciDoppiaGestioneSubdocumentoEntrataSeNecessario(List<SubdocumentoEntrata> quoteValide) {
		if(!this.bilancioInDoppiaGestione) {
			return;
		}
		Date now = new Date();
		for (SubdocumentoEntrata ssValido : quoteValide) {
			collegaScollegaMovimentiCollegatiEntrata(ssValido, now);			
		}
		
	}
	
	/**
	 * Questo passo è da effettuarsi solo nel caso in cui ci si trovi in un anno di bilancio in PREDISPOSIZIONE CONSUNTIVO ed il bilancio successivo in fase di ESERCIZIO PROVVISORIO.<br/>
	 * Per ogni quota dell’ordinativo inserito:
	 * <ul>
	 * <li> Se l’accertamento/sub che si è pagato è passato a residuo nell’anno di bilancio + 1 (attenzione, dopo l’inserimento dell’ordinativo potrebbe essere stato cancellato logicamente) e se il suddetto impegno residuo ha una relazione valida con la quota che si sta pagando è necessario:
	 * 		<ul>
	 * 			<li>Cancellare la relazione tra quota e accertamento/sub residuo dell’anno di bilancio + 1 </li>
	 * 			<li>Riportare a 'sempre valida' la relazione  tra quota e accertamento/sub dell’anno in corso (data fine validità nulla) </li>
	 * 		</ul>
	 * 	</li>
	 * 
	 * </ul>
	 *
	 * @param ssValido il subdoc di entrata su cui effettuare le operazioni di cui sopra
	 * @param now  il now su cui impostare le relazioni
	 */
	private void collegaScollegaMovimentiCollegatiEntrata(SubdocumentoEntrata ssValido, Date now) {
		subdocumentoEntrataDad.collegaScollegaMovimentoGestionePerDoppiaGestione(ssValido, this.bilancio, this.bilancioAnnoSuccessivo, now);
	}	
}

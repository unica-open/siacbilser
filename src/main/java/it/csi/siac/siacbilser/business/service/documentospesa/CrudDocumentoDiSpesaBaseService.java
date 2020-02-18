/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documento.MovimentoGestioneServiceCallGroup;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.ProvvedimentoDad;
import it.csi.siac.siacbilser.integration.dad.ProvvisorioBilDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SospensioneSubdocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAttributiMovimentoGestioneOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

/**
 * Base service per il crud dei documenti di spesa.
 *
 * @author Domenico
 * @param <REQ> the generic type
 * @param <RES> the generic type
 */
public abstract class CrudDocumentoDiSpesaBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ,RES> {
	
	protected DocumentoSpesa doc;
	protected SubdocumentoSpesa subdoc;
	protected DocumentoSpesa documentoAssociato;
	protected Bilancio bilancio;
	
	//DAD
	@Autowired
	protected DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	protected SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private SoggettoDad soggettoDad;
	@Autowired
	private ProvvedimentoDad provvedimentoDad;
	@Autowired
	private ProvvisorioBilDad provvisorioBilDad;
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	@Autowired
	private ImpegnoBilDad impegnoDad;
	@Autowired
	private CodificaDad codificaDad;
	
	//Servizi interni.
	@Autowired
	private AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaService;
	
	//Servizi esterni.
	@Autowired
	protected LiquidazioneService liquidazioneService;
	@Autowired
	private ProvvisorioService provvisorioService;
	
	protected MovimentoGestioneServiceCallGroup mgscg;
	
	
	@Override
	protected void init() {
		super.init();
		
		soggettoDad.setEnte(ente);
		mgscg = new MovimentoGestioneServiceCallGroup(serviceExecutor,req.getRichiedente(),ente, bilancio);
	}
	
	/**
	 * Aggiorna stato operativo documento.
	 *
	 * @param uidDocumento the uid documento
	 * @return the stato operativo documento
	 */
	protected AggiornaStatoDocumentoDiSpesaResponse aggiornaStatoOperativoDocumento(Integer uidDocumento) {
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(uidDocumento);
		return aggiornaStatoOperativoDocumento(documentoSpesa);
	}

	/**
	 * Aggiorna stato operativo documento.
	 *
	 * @param documentoSpesa the documento spesa
	 * @return the stato operativo documento
	 */
	protected AggiornaStatoDocumentoDiSpesaResponse aggiornaStatoOperativoDocumento(DocumentoSpesa documentoSpesa) {
		AggiornaStatoDocumentoDiSpesa reqAs = new AggiornaStatoDocumentoDiSpesa();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setDocumentoSpesa(documentoSpesa);
		reqAs.setBilancio(bilancio);
//		AggiornaStatoDocumentoDiSpesaResponse resAs = executeExternalService(aggiornaStatoDocumentoDiSpesaService, reqAs);
//		AggiornaStatoDocumentoDiSpesaResponse resAs = executeExternalServiceSuccess(aggiornaStatoDocumentoDiSpesaService, reqAs);
		AggiornaStatoDocumentoDiSpesaResponse resAs = serviceExecutor.executeServiceSuccess(aggiornaStatoDocumentoDiSpesaService, reqAs);
		
		if(resAs.getDocumentoSpesa()==null){
			throw new BusinessException("Nessun Documento restituito dal servizio aggiornaStatoDocumentoDiSpesaService.");
		}
		
		return resAs;
	}
	
	/**
	 * soggetto (DocumentoSpesa.Soggetto) deve essere in StatoOperativoAnagrafica (statoOperativo) == VALIDO
	 * tabella siac_r_soggetto_stato (legame siac_t_soggetto e siac_d_soggetto_stato)
	 */
	protected void checkSoggetto() {
		StatoOperativoAnagrafica statoOperativoAnagrafica = soggettoDad.findStatoOperativoAnagraficaSoggetto(doc.getSoggetto());
		if (!StatoOperativoAnagrafica.VALIDO.equals(statoOperativoAnagrafica) && !StatoOperativoAnagrafica.SOSPESO.equals(statoOperativoAnagrafica)) {
			throw new BusinessException(ErroreFin.SOGGETTO_NON_VALIDO.getErrore(""), Esito.FALLIMENTO);
		}
	}
	
	/**
	 * anno (DocumentoSpesa.anno) deve essere non maggiore dell'anno del bilancio in cui si sta agendo (non passato)
	 */
	protected void checkAnno() {
		//INFO: al momento non viene passato l'anno di bilancio!  
	}
	
	/**
	 * dataEmissione (DocumentoSpesa.dataEmissione) deve avere campo anno uguale all'anno del documento
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkDataEmissione() throws ServiceParamError {
		//TODO Per ora saltiamo il check
		
		
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
//		Integer year = Integer.valueOf(dateFormat.format(doc.getDataEmissione()));
//		
//		checkCondition(year.compareTo(doc.getAnno()) == 0, it.csi.siac.siacfin2ser.model.errore.ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore()); // Possibile refuso di analisi?
	}

	/**
	 * importo (DocumentoSpesa.importo) deve essere positivo
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkImportoPositvo() throws ServiceParamError {
		checkCondition(doc.getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore di zero"));
	}
	
	/**
	 * importo (DocumentoSpesa.importo) deve essere non negativo
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkImporto() throws ServiceParamError {
		checkCondition(doc.getImporto().signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore o uguale a zero"));
	}
	
	/**
	 * arrotondamento (DocumentoSpesa.arrotondamento) se esiste deve essere negativo; inoltre importo + arrotondamento deve essere potitivo
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkArrotondamento() throws ServiceParamError {
//		checkCondition(doc.getArrotondamento() == null || doc.getArrotondamento().signum() <= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": deve essere minore di zero"));
		checkCondition(doc.getArrotondamento() == null || doc.getImporto().add(doc.getArrotondamento()).signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Arrotondamento", ": importo sommato ad arrotondamento deve essere maggiore o uguale a zero"));
	}
	
	/**
	 * dataScadenza (DocumentoSpesa.dataScadenza) se esiste deve essere maggiore della data di emissione
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkDataScadenza() throws ServiceParamError {
		checkCondition(doc.getDataScadenza() == null || doc.getDataScadenza().compareTo(doc.getDataEmissione()) >= 0,
			it.csi.siac.siacfin2ser.model.errore.ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());
	}
	
	/**
	 * dataSospensione (DocumentoSpesa.dataSospensione) e causaleSospensione (DocumentoSpesa.causaleSospensione) devono essere entrambi presenti o entrambi assenti
	 *
	 * @throws ServiceParamError the service param error
	 */
	protected void checkDataSospensione() throws ServiceParamError {
		checkCondition(doc.getDataScadenza() == null || doc.getDataScadenza().compareTo(doc.getDataEmissione()) >= 0, it.csi.siac.siacfin2ser.model.errore.ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());
	}
	
	//-------------------------------------------------------------
	
	
	protected void caricaDettaglioDocumentoAssociato() {
		this.documentoAssociato = documentoSpesaDad.findDocumentoSpesaById(subdoc.getDocumento().getUid());
		subdoc.setDocumento(documentoAssociato);
		
	}
	
	protected void caricaAttoAmministrativo(){
		//il provvedimento non e' obbligatorio
		log.debug("caricaAttoAmministrativo", subdoc.getUid());
		
		if (subdoc.getAttoAmministrativo()==null || subdoc.getAttoAmministrativo().getUid()==0){
			return;
		}
		AttoAmministrativo attoAmministrativo = provvedimentoDad.findProvvedimentoById(subdoc.getAttoAmministrativo().getUid());
		
		if (attoAmministrativo==null){
			throw new BusinessException(ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore(), Esito.FALLIMENTO);
		}
		
		log.debug("caricaAttoAmministrativo", "trovato atto con uid: "+ attoAmministrativo.getUid() + ", numero: " + attoAmministrativo.getNumero() +
				" e stato opertativo : " + attoAmministrativo.getStatoOperativoAtti());
		
		subdoc.setAttoAmministrativo(attoAmministrativo);
	}
	
	/**
	 * Check provvedimento.
	 */
	protected void checkAttoAmministrativo() {
		if (subdoc.getAttoAmministrativo()==null || subdoc.getAttoAmministrativo().getUid()==0){
			return;
		}
		
		AttoAmministrativo provvedimentoAttuale = cercaProvvedimentoAttuale();
		
		//se il provvedimento non è stato cambiato, salto i controlli
		if(provvedimentoAttuale != null && provvedimentoAttuale.getUid() == subdoc.getAttoAmministrativo().getUid()){
			return;
		}
		
		if (!StatoOperativoAtti.DEFINITIVO.equals(subdoc.getAttoAmministrativo().getStatoOperativoAtti())){
			throw new BusinessException(ErroreFin.STATO_PROVVEDIMENTO_NON_CONSENTITO.getErrore("Gestione quota documento di spesa","definitivo"), Esito.FALLIMENTO);
		}
		
		// controllo sul tipo eliminato in seguito alla CR del 11/03/2015
//		String codiceTipoDeterminaDiLiquidazione = provvedimentoDad.getCodiceTipoDeterminaDiLiquidazione();
//		
//		if (!codiceTipoDeterminaDiLiquidazione.equals(subdoc.getAttoAmministrativo().getTipoAtto().getCodice())){
//			throw new BusinessException(ErroreFin.TIPO_PROVVEDIMENTO_INCONGRUENTE.getErrore(""), Esito.FALLIMENTO);
//		}
		// SIAC-5043
		if(subdoc.getAttoAmministrativo().getAllegatoAtto()!= null && subdoc.getAttoAmministrativo().getAllegatoAtto().getUid() != 0 && !hasElencoStessoAllegatoAtto()){
			throw new BusinessException(ErroreFin.ATTO_GIA_ABBINATO.getErrore(), Esito.FALLIMENTO);
		}
		
		// TODO stato in convalida!
		/*
		 * Inoltre il provvedimento non deve avere uno stato in convalida uguale a CONVALIDATO, ANNULLATO o RIFIUTATO,
		 * altrimenti si visualizza il messaggio <FIN_ERR_0075, Stato Provvedimento non consentito, 'Gestione quota documento di spesa',
		 * 'NON CONVALIDATO-NON RIFIUTATO-NON ANNULLATO'>.
		 */
	
	}

	/**
	 * Controllo se vi sia un elenco dello stesso allegato atto associato alla quota
	 * @return
	 */
	private boolean hasElencoStessoAllegatoAtto() {
		if(subdoc.getElencoDocumenti() == null || subdoc.getElencoDocumenti().getUid() == 0) {
			return false;
		}
		
		AllegatoAtto allegatoAtto = allegatoAttoDad.findAllegatoAttoByElencoDocumentiAllegato(subdoc.getElencoDocumenti());
		return allegatoAtto != null && allegatoAtto.getUid() != 0 && allegatoAtto.getUid() == subdoc.getAttoAmministrativo().getAllegatoAtto().getUid();
	}

	private AttoAmministrativo cercaProvvedimentoAttuale() {
		return subdocumentoSpesaDad.findProvvedimentoLegatoAllaQuota(subdoc);
	}

	protected void caricaBilancio(){
		bilancio = bilancioDad.getBilancioByUid(bilancio.getUid());
	}
	
	
	
	/**
	 * 
	 * Controlla la disponiblità a liquidare dell'impegno (o del subimpegno).
	 *  <br>
	 * [14:53:19] Silvia: allora il disponibile a liquidare è dato dall'importo dell'impegno (o sub) <br>
	 * [14:53:19] Silvia: meno le liquidazioni <br>
	 * [14:53:32] Silvia: meno le quote doc non ancora liquidate <br>
	 * [14:53:44] Silvia: meno i predocumenti non ancora diventatidocumenti <br>
	 * [14:54:03] Silvia: meno le carte contabilinon ancora fineite in documenti (tipo CCN) <br>
	 *  <br>
	 * [15:05:23] Domenico Lisi: quindi il vincolo è: <br>
	 * [15:05:23] Domenico Lisi: disponibilità a liquidare + quotaVecchia.importo >= quotaNuova.importo <br>
	 * [15:05:35] Domenico Lisi: (e non più quotaNuova.importoDaPagare) <br>
	 * [15:05:53] Domenico Lisi: dove in inseriemento "quotaVecchia.importo" è 0 <br>
	 * [15:05:59] Domenico Lisi: giusto? <br>
	 *  <br>
	 * [15:06:46] Silvia: yesss <br>
	 * [15:06:46] Silvia: proviamo con i numeri che così sonio + sicura <br>
	 * [15:06:46] Silvia: impegno 100 <br>
	 * [15:06:50] Silvia: vecchio importo 70 <br>
	 * [15:06:55] Silvia: vecchio disp = 30 <br>
	 * [15:07:07] Silvia: se nuovo importo è 80 <br>
	 * [15:07:23] Silvia: 30+70 >= 80 YESSS <br>
	 * [15:07:30] Silvia: vedo in riunione <br>
	 * [15:07:32] Silvia: GRAZIE <br>
	 * @param ricalcolaDisponibilitaLiquidare 
	 * 
	 * 
	 */
	protected void checkImpegnoSubImpegno(boolean ricalcolaDisponibilitaLiquidare) {
		final String methodName = "checkImpegnoSubImpegno";
		if(documentoAssociato.getTipoDocumento().isCartaContabile() || documentoAssociato.getTipoDocumento().isDisposizioneDiPagamento()) {
			//Se il tipo del documento e' CCN e DSP il controllo sull'impegno o subImpegno viene saltato
			log.debug(methodName, "il tipo del documento è " + documentoAssociato.getTipoDocumento().getCodice() + ", non effettuo i controlli");
			return;
		}
		
		Impegno impegnoOSubImpegno = subdoc.getImpegnoOSubImpegno();
		
		if(impegnoOSubImpegno==null){
			log.debug(methodName, "impegnoOSubImpegno è null, esco");
			return;
		}
		checkStatoOperativoDefinitivo(impegnoOSubImpegno);
		
		//SIAC-3961
		if(documentoAssociato.getTipoDocumento().isNotaCredito()){
			log.info(methodName, "Quota associata ad un documento di tipo nota credito. Salto il controllo sulla disponibilita' del movimento gestione.");
			return;
		}
		
		BigDecimal importoPrecedente = BigDecimal.ZERO;
		Impegno impegno = subdoc.getImpegno();
		SubImpegno subImpegno = subdoc.getSubImpegno();
		
		boolean voceMutuoValorizzato = subdoc.getVoceMutuo() !=null && subdoc.getVoceMutuo().getNumeroMutuo() !=null && StringUtils.isNotBlank(subdoc.getVoceMutuo().getNumeroMutuo());
		if(voceMutuoValorizzato) {
			// I check li faccio solo qua
			caricaVoceMutuo();
			controllaDisponibilitaVoceMutuo();
			return;
		}
		
		if(subImpegno != null && subImpegno.getUid() != 0){
			Integer uidSubImpegnoPrecedente = caricaUidSubimpegnoPrecedente();
			log.debug(methodName, "uidSubImpegnoPrecedente: " + uidSubImpegnoPrecedente);
			if(uidSubImpegnoPrecedente != null && uidSubImpegnoPrecedente == subImpegno.getUid()){
				importoPrecedente = caricaImportoDaPagarePrecedenteQuota();
			}
		}else if(impegno != null && impegno.getUid() != 0){
			Integer uidImpegnoPrecedente = caricaUidImpegnoPrecedente();
			log.debug(methodName, "uidImpegnoPrecedente: " + uidImpegnoPrecedente);
			if(uidImpegnoPrecedente != null && uidImpegnoPrecedente == impegno.getUid()){
				importoPrecedente = caricaImportoDaPagarePrecedenteQuota();
			}
		}
		
		log.debug(methodName, "importoPrecedente" + importoPrecedente);
		checkDisponibilitaImpegnoSubimpegno(importoPrecedente, ricalcolaDisponibilitaLiquidare);
	}

	/**
	 * Restituisce l'uid del subimpegno della quota attualmente presenete su DB.
	 * 
	 * @return
	 */
	private Integer caricaUidSubimpegnoPrecedente() {
		if(subdoc.getUid() == 0){
			return null;
		}
		return subdocumentoSpesaDad.findUidSubImpegnoSubdocumentoSpesaById(subdoc.getUid());
	}

	/**
	 * Restituisce l'uid dell'impegno quota attualmente presenete su DB.
	 * 
	 * @return
	 */
	private Integer caricaUidImpegnoPrecedente() {
		if(subdoc.getUid() == 0){
			return null;
		}
		return subdocumentoSpesaDad.findUidImpegnoSubdocumentoSpesaById(subdoc.getUid());
	}

	
	

	/**
	 * Restituisce l'importo della quota attualmente presenete su DB.
	 * 
	 * @return
	 */
	protected BigDecimal caricaImportoPrecedenteQuota() {
		return subdocumentoSpesaDad.findImportoSubdocumentoSpesaById(subdoc.getUid());
	}
	
	/**
	 * Restituisce l'importo della quota attualmente presenete su DB.
	 * 
	 * @return
	 */
	protected BigDecimal caricaImportoDaPagarePrecedenteQuota() {
		return subdocumentoSpesaDad.findImportoSubdocumentoSpesaById(subdoc.getUid());
	}

	private void checkStatoOperativoDefinitivo(Impegno impegno) {
		if(!StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa())){
			ErroreFin errore = impegno instanceof SubImpegno ? ErroreFin.SUBIMPEGNO_NON_IN_STATO_DEFINITIVO : ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO;
			throw new BusinessException(errore.getErrore(""), Esito.FALLIMENTO);
		}
	}

	/**
	 * Controlla se è soddisfatatta la seguente condizione:
	 * disponibilità a liquidare + quotaVecchia.importo >= quotaNuova.importo
	 * 
	 * @param impegno
	 * @param importoNuovo
	 * @param importoPrecedente
	 */
	protected void checkDisponibilitaImpegnoSubimpegno(BigDecimal importoPrecedente, boolean ricalcolaDisponibilitaLiquidare) {
		final String methodName = "checkDisponibilitaImpegnoSubimpegno";
		
		Impegno impegno = subdoc.getImpegnoOSubImpegno();
		BigDecimal importoNuovo = subdoc.getImportoDaPagare();
		
		DisponibilitaMovimentoGestioneContainer disponibilitaLiquidareContainer = ricalcolaDisponibilitaLiquidare
				? impegnoDad.ottieniDisponibilitaLiquidare(impegno, subdoc.getSubImpegno(), req.getAnnoBilancio()) 
				: impegno.getDisponibilitaLiquidare() != null
					? new DisponibilitaMovimentoGestioneContainer(impegno.getDisponibilitaLiquidare(), StringUtils.isNotBlank(impegno.getMotivazioneDisponibilitaLiquidare()) ? impegno.getMotivazioneDisponibilitaLiquidare() : "Disponibilita ottenuta dall'impegno")
					: new DisponibilitaMovimentoGestioneContainer(BigDecimal.ZERO, "Disponibilita a liquidare sull'impegno NULL");
		
		importoNuovo = importoNuovo!=null? importoNuovo : BigDecimal.ZERO;
		importoPrecedente = importoPrecedente!=null? importoPrecedente : BigDecimal.ZERO;
		
		boolean isDisponibilitaSufficiente = disponibilitaLiquidareContainer.getDisponibilita().add(importoPrecedente).compareTo(importoNuovo)>=0;
		
		log.info(methodName, "Disponibilita a liquidare: " + disponibilitaLiquidareContainer.getDisponibilita() + ", motivazione: " + disponibilitaLiquidareContainer.getMotivazione());
		log.info(methodName, "disponibilitaLiquidare: " + disponibilitaLiquidareContainer.getDisponibilita() + " + importoPrecedente: " + importoPrecedente 
				+ " >= importoNuovo: " + importoNuovo + " -> isDisponibilitaSufficiente: " + isDisponibilitaSufficiente);
		
		if(!isDisponibilitaSufficiente){
			String key = calcolaChiaveImpegno(subdoc.getImpegno(), subdoc.getSubImpegno());
			throw new BusinessException(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore("Inserimento quota documento spesa", impegno.getClass().getSimpleName() + " " + key), Esito.FALLIMENTO);
		}
	}
	
	protected void checkSiopeAssenzaMotivazione() {
		if(subdoc.getImpegnoOSubImpegno() == null || subdoc.getImpegnoOSubImpegno().getUid() == 0 || subdoc.getSiopeAssenzaMotivazione() == null || subdoc.getSiopeAssenzaMotivazione().getUid() == 0) {
			return;
		}
		
		SiopeAssenzaMotivazione siopeAssenza = codificaDad.ricercaCodifica(SiopeAssenzaMotivazione.class, subdoc.getSiopeAssenzaMotivazione().getUid());
		
		if(siopeAssenza != null && ("CL".equals(siopeAssenza.getCodice()) || ("ID".equals(siopeAssenza.getCodice())))) {
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("motivo assenza cig","selezionare una motivazione valida"), Esito.FALLIMENTO);
		}
	}

	/**
	 * Calcolo della chiave dell'impegno e subimpegno per log.
	 * <br/>
	 * La chiave &eacute; della forma <code>&lt;ANNO&gt;/&lt;NUMERO_IMPEGNO&gt;(-&lt;NUMERO_SUB&gt;)</code>
	 * @param impegno l'impegno
	 * @param subImpegno il subimpegno
	 * @return la chiave
	 */
	protected String calcolaChiaveImpegno(Impegno impegno, SubImpegno subImpegno) {
		if(impegno == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(impegno.getAnnoMovimento());
		if(impegno.getNumero() != null) {
			sb.append("/");
			sb.append(impegno.getNumero().toPlainString());
		}
		
		if(subImpegno != null && subImpegno.getNumero() != null) {
			sb.append("-");
			sb.append(subImpegno.getNumero().toPlainString());
		}
		return sb.toString();
	}

	protected void caricaImpegnoESubimpegno() {
		if(subdoc.getImpegno()==null || subdoc.getImpegno().getNumero() == null || subdoc.getImpegno().getAnnoMovimento() == 0){
			return;
		}
		
		Impegno impegno = ricercaImpegnoPerChiaveOttimizzato();
		//importante che se la lista elenco subimpegni e' null (ho cercato un sub che non esiste) venga sostituita con lista vuota
		impegno.setElencoSubImpegni(filtraSubImpegniDefinitivi(impegno.getElencoSubImpegni()));
		log.debug("caricaImpegnoESubimpegno", "trovato impegno con uid: " + impegno.getUid() + " e numero: " + impegno.getNumero());
		subdoc.setImpegno(impegno);

		if(subdoc.getSubImpegno()!= null && subdoc.getSubImpegno().getUid()!=0){
			if(impegno.getElencoSubImpegni() == null){
				throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subimpegno", subdoc.getSubImpegno().getNumero()+""), Esito.FALLIMENTO);
			}
			
			SubImpegno subImpegno = null;
			for(SubImpegno subImp: impegno.getElencoSubImpegni()){
				if(subImp.getUid()==subdoc.getSubImpegno().getUid()){
					subImpegno = subImp;
					log.debug("caricaImpegnoESubimpegno", "trovato subimpegno con uid: " + subImpegno.getUid() + " e numero: " + subImpegno.getNumero());
				}
			}
			if(subImpegno == null){
				throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subimpegno", subdoc.getSubImpegno().getNumero()+""), Esito.FALLIMENTO);
			}
			
			subdoc.setSubImpegno(subImpegno);
			
		}
	}	
	
	
	private List<SubImpegno> filtraSubImpegniDefinitivi(List<SubImpegno> elencoSubImpegni) {
		if(elencoSubImpegni == null || elencoSubImpegni.isEmpty()){
			return new ArrayList<SubImpegno>();
		}
		List<SubImpegno> result = new ArrayList<SubImpegno>();
		for(SubImpegno s : elencoSubImpegni){
			if(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(s.getStatoOperativoMovimentoGestioneSpesa())){
				result.add(s);
			}
		}
		return result;
	}

	//metodo che utilizza il vecchio servizio "RicercaImpegnoPerChiaveService"
	//lasciato qui anche se non utilizzato fino a quando il servizio non sara' del tutto abbandonato per poter eventualmente tornare ad utilizzarlo.
//	@Deprecated
//	private Impegno ricercaImpegnoPerChiave() {		
//		
//		RicercaImpegnoPerChiaveResponse resRIPC = mgscg.ricercaImpegnoPerChiaveCached(subdoc.getImpegno());
//		log.logXmlTypeObject(resRIPC, "Risposta ottenuta dal servizio RicercaImpegnoPerChiave.");
//		
////		//carico l'impegno
////		RicercaImpegnoPerChiave reqRIPC = new RicercaImpegnoPerChiave();
////		reqRIPC.setRichiedente(req.getRichiedente());
////		reqRIPC.setEnte(subdoc.getEnte());
////		
////		RicercaImpegnoK pRicercaImpegnoK = new RicercaImpegnoK();
////		pRicercaImpegnoK.setAnnoEsercizio(bilancio.getAnno());
////		pRicercaImpegnoK.setAnnoImpegno(subdoc.getImpegno().getAnnoMovimento());
////		pRicercaImpegnoK.setNumeroImpegno(subdoc.getImpegno().getNumero());
////		
////		reqRIPC.setpRicercaImpegnoK(pRicercaImpegnoK);
////		log.logXmlTypeObject(reqRIPC, "Request del servizio RicercaImpegnoPerChiave.");
////		RicercaImpegnoPerChiaveResponse resRIPC = movimentoGestioneService.ricercaImpegnoPerChiave(reqRIPC);
////		log.logXmlTypeObject(resRIPC, "Risposta ottenuta dal servizio RicercaImpegnoPerChiave.");
//////		checkServiceResponseFallimento(resRIPC);
//		
//		Impegno impegno = resRIPC.getImpegno();
//		if(impegno==null) {
//			res.addErrori(resRIPC.getErrori());
//			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", subdoc.getImpegno().getNumero()+"/"+ subdoc.getImpegno().getAnnoMovimento()+""), Esito.FALLIMENTO);
//		}
//		
//		return impegno;
//	}

	private Impegno ricercaImpegnoPerChiaveOttimizzato() {
	
		RicercaAttributiMovimentoGestioneOttimizzato parametri = new RicercaAttributiMovimentoGestioneOttimizzato();
		parametri.setEscludiSubAnnullati(true);
		parametri.setCaricaSub(subdoc.getSubImpegno()!=null && subdoc.getSubImpegno().getNumero() != null);
	
		DatiOpzionaliElencoSubTuttiConSoloGliIds parametriElencoIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		parametriElencoIds.setEscludiAnnullati(true);
		parametri.setDatiOpzionaliElencoSubTuttiConSoloGliIds(parametriElencoIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class)); //Non richiedo NESSUN importo derivato.
	
		RicercaImpegnoPerChiaveOttimizzatoResponse resRIPC = mgscg.ricercaImpegnoPerChiaveOttimizzatoCached(subdoc.getImpegno(), parametri, datiOpzionaliCapitoli, subdoc.getSubImpegno());
		log.logXmlTypeObject(resRIPC, "Risposta ottenuta dal servizio RicercaImpegnoPerChiaveOttimizzato.");
	
		Impegno impegno = resRIPC.getImpegno();
		if(impegno==null) {
			res.addErrori(resRIPC.getErrori());
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", subdoc.getImpegno().getNumero()+"/"+ subdoc.getImpegno().getAnnoMovimento()+""), Esito.FALLIMENTO);
		}
	
		return impegno;
	}

	
	/*
	 * DA NON SVILUPPARE IN V1 - Se è stato passato come parametro di input il provvisorio di cassa 
	 * e/o la quietanza è necessario verificare che non siano regolarizzati (importo da regolarizzare >= importodaPagare)
	 *  e che siano presenti in archivio, altrimenti viene segnalato il messaggio
	 *   <FIN_ERR_0147, Dati provvisorio/quietanza errati.>.

	 */
	protected void checkProvvisorio() {
		String methodName = "checkProvvisorio";
		
		if(subdoc.getProvvisorioCassa() == null || subdoc.getProvvisorioCassa().getUid() == 0){
			log.debug(methodName, "non ho provvisorio di cassa da controllare, esco");
			return;
		}
		
		//SIAC-6048
		if(subdoc.getTipoIvaSplitReverse() != null && subdoc.getImportoSplitReverse() != null) {
			//ho per forza un provvisorio, altrimenti sarei uscita prima
			log.debug(methodName, "non posso inserire una quota che abbia sia split che provvisorio");
			throw new BusinessException(ErroreFin.QUOTA_A_COPERTURA_CON_SPLIT.getErrore());			
		}
		
		BigDecimal importoDaRegolarizzare = subdoc.getProvvisorioCassa().getImportoDaRegolarizzare() != null
				? subdoc.getProvvisorioCassa().getImportoDaRegolarizzare()
				: provvisorioBilDad.calcolaImportoDaRegolarizzareProvvisorio(subdoc.getProvvisorioCassa());
		log.debug(methodName, "importto da regolarizzare: " + importoDaRegolarizzare);
		
		BigDecimal importoDaVerificare = BigDecimal.ZERO;
		ProvvisorioDiCassa provvisorioAttuale = null;
		
		if(subdoc.getUid() != 0){
			//solo se sono in aggiornamento, altrimenti il provvisorio e' sicuramente aggiunto in questo momento
			provvisorioAttuale = provvisorioBilDad.findProvvisorioBySubdocId(subdoc.getUid());
		}
		//se il provvisorio non e' cambiato controllo solo l'eventuale differenza
		if(provvisorioAttuale != null && provvisorioAttuale.getUid() == subdoc.getProvvisorioCassa().getUid()){
			BigDecimal importoDaPagareAttuale = subdocumentoSpesaDad.findImportoDaPagareSubdocumentoSpesaById(subdoc.getUid());
			//se l'importo da incassare e' diminuito o e' rimasto invariato, non devo controllare niente, ho gia' copertura
			if(importoDaPagareAttuale.compareTo(subdoc.getImportoDaPagare()) >= 0){
				log.debug(methodName, "l'importo da pagare non e' aumentato, non devo verificare la copertura del provvisorio");
				return;
			}
			
			//se l'importo da incassare e' aumentoto, verifico di avere copertura per la differenza
			log.debug(methodName, "l'importo da pagare e' aumentato, verificare la copertura del provvisorio sulla differenza");
			importoDaVerificare = subdoc.getImportoDaPagare().subtract(importoDaPagareAttuale);
			log.debug(methodName, "importo da verificare(differenza): " + importoDaVerificare);
		}else{
			//se il provvisorio e' stato inserito in questo momento devo controllare tutto l'importo da pagare
			importoDaVerificare = subdoc.getImportoDaPagare();
			log.debug(methodName, "importo da verificare(tutto l'importo da pagare): " + importoDaVerificare);
		}
		
		if(importoDaRegolarizzare.subtract(importoDaVerificare).signum() < 0){
			throw new BusinessException(ErroreFin.DATI_PROVVISORIO_QUIETANZA_ERRATI.getErrore(""), Esito.FALLIMENTO);
		}
		
		subdoc.setFlagACopertura(Boolean.TRUE);
		
	}

	protected void caricaProvvisorioDiCassa() {
		if(subdoc.getProvvisorioCassa() == null || subdoc.getProvvisorioCassa().getNumero()== null || subdoc.getProvvisorioCassa().getAnno() == null){
			return;
		}
		ProvvisorioDiCassa provvisorioDiCassa = ricercaProvvisorioDiCassa();
		if(provvisorioDiCassa==null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", subdoc.getProvvisorioCassa().getAnno() 
					 +  "/" + subdoc.getProvvisorioCassa().getNumero()), Esito.FALLIMENTO);
		}else{
			log.debug("caricaProvvisorioDiCassa", "trovato provvisorio con uid: " + provvisorioDiCassa.getUid() + " e numero " + provvisorioDiCassa.getNumero());
		}
		subdoc.setProvvisorioCassa(provvisorioDiCassa);
		log.logXmlTypeObject(subdoc.getProvvisorioCassa(), "PROVVISORIO>>>>>>>>>>>>>>>>>>");
	}

	private ProvvisorioDiCassa ricercaProvvisorioDiCassa() {
		RicercaProvvisorioDiCassaPerChiave reqRPK = new RicercaProvvisorioDiCassaPerChiave();
		
		reqRPK.setBilancio(bilancio);
		reqRPK.setDataOra(req.getDataOra());
		reqRPK.setEnte(subdoc.getEnte());
		reqRPK.setRichiedente(req.getRichiedente());
		RicercaProvvisorioDiCassaK pRicercaProvvisorioK = new RicercaProvvisorioDiCassaK();
		pRicercaProvvisorioK.setAnnoProvvisorioDiCassa(subdoc.getProvvisorioCassa().getAnno());
		pRicercaProvvisorioK.setNumeroProvvisorioDiCassa(subdoc.getProvvisorioCassa().getNumero());
		pRicercaProvvisorioK.setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S);
		reqRPK.setpRicercaProvvisorioK(pRicercaProvvisorioK);
		
		RicercaProvvisorioDiCassaPerChiaveResponse resRPK = provvisorioService.ricercaProvvisorioDiCassaPerChiave(reqRPK);
		log.logXmlTypeObject(resRPK, "Risposta ottenuta dal servizio RicercaProvvisorioPerChiave.");
		checkServiceResponseFallimento(resRPK);
		
		ProvvisorioDiCassa provvisorioDiCassa = resRPK.getProvvisorioDiCassa();
		return provvisorioDiCassa;
	}

	
	protected boolean isLiquidazioneDaInserire() {
		final String methodName= "isLiquidazioneDaInserire";
		
		
		boolean isAttoAmministrativoValorizzato = subdoc.getAttoAmministrativo() != null 
				&& subdoc.getAttoAmministrativo().getUid() != 0;
		
		boolean isImpegnoOSubimpegnoValorizzato = (subdoc.getImpegno() != null && subdoc.getImpegno().getUid() != 0) 
				|| 
				(subdoc.getSubImpegno() != null && subdoc.getSubImpegno().getUid() != 0) ;
		
		boolean isFaseBilancioPredisposizioneConsuntivo = FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio());
		
		List<SospensioneSubdocumento> sospensioni = subdocumentoSpesaDad.findSospensioni(subdoc);
		boolean nonSospeso = true;
		for(Iterator<SospensioneSubdocumento> it = sospensioni.iterator(); it.hasNext() && nonSospeso;) {
			SospensioneSubdocumento ss = it.next();
			nonSospeso = ss.getDataSospensione() == null
					|| (ss.getDataSospensione() != null && ss.getDataRiattivazione()!=null && ss.getDataRiattivazione().compareTo(ss.getDataSospensione())>=0);
		}
		
		boolean isCollegatoCEC = Boolean.TRUE.equals(subdoc.getDocumento().getCollegatoCEC());
		
		boolean isNotContabilizzatoGenPcc = Boolean.FALSE.equals(subdoc.getDocumento().getContabilizzaGenPcc());
		
		boolean flagGENPCCNonPresenti = (subdoc.getDocumento().getTipoDocumento().getFlagAttivaGEN() == null || Boolean.FALSE.equals(subdoc.getDocumento().getTipoDocumento().getFlagAttivaGEN()))
				&& (subdoc.getDocumento().getTipoDocumento().getFlagComunicaPCC() == null || Boolean.FALSE.equals(subdoc.getDocumento().getTipoDocumento().getFlagComunicaPCC()));
		
		log.debug(methodName, "isAttoAmministrativoValorizzato:"+isAttoAmministrativoValorizzato 
				+ " isImpegnoOSubimpegnoValorizzato: "+ isImpegnoOSubimpegnoValorizzato
				+ " !isFaseBilancioPredisposizioneConsuntivo: " + !isFaseBilancioPredisposizioneConsuntivo
				+ " nonSospeso: "+nonSospeso
				+ " !isCollegatoCEC: " + !isCollegatoCEC
				+ " !isNotContabilizzatoGenPcc: " + !isNotContabilizzatoGenPcc
				+ " flagGENPCCNonPresenti: " + flagGENPCCNonPresenti);
		
		if( isAttoAmministrativoValorizzato
			&& isImpegnoOSubimpegnoValorizzato
			&& !isFaseBilancioPredisposizioneConsuntivo
			&& nonSospeso
			&& !isCollegatoCEC
			&& (!isNotContabilizzatoGenPcc || flagGENPCCNonPresenti)
			) {
			
			log.info(methodName, "Returning true. La liquidazione è da inserire. ");
			return true;
		}
		
		log.info(methodName, "Returning false. La liquidazione NON è da inserire");
		return false;
	}
	
	
	protected Liquidazione inserisciLiquidazione(Liquidazione liquidazione) {
		InserisceLiquidazione reqIL = new InserisceLiquidazione();
		reqIL.setRichiedente(req.getRichiedente());
		reqIL.setBilancio(bilancio);
		reqIL.setDataOra(req.getDataOra());
		reqIL.setEnte(subdoc.getEnte());
		reqIL.setAnnoEsercizio(String.valueOf(bilancio.getAnno()));
		reqIL.setLiquidazione(liquidazione);
		
		InserisceLiquidazioneResponse resIL = liquidazioneService.inserisceLiquidazione(reqIL);
		log.logXmlTypeObject(resIL, "Risposta ottenuta dal servizio InserisceLiquidazione.");
		checkServiceResponseFallimento(resIL);
		return resIL.getLiquidazione();
	}

	
	protected void checkNumeroRegistrazioneIva() {
		String methodName = "checkNumeroRegistrazioneIva";
		
//		Impegno impegnoOSubImpegno = subdoc.getImpegnoOSubImpegno();
//		CapitoloUscitaGestione capitolo = impegnoOSubImpegno.getCapitoloUscitaGestione(); 
//		Boolean flagRilevanteIva = capitolo != null ? capitolo.getFlagRilevanteIva() : null; //NON deve prenderlo dal capitolo associato all'impegno associato alla quota!
		
		Boolean flagRilevanteIva = subdoc.getFlagRilevanteIVA();
		
		log.debug(methodName, "flagRilevanteIva sulla quota: " + flagRilevanteIva +" numero registrazione: " + subdoc.getNumeroRegistrazioneIVA());
		
		if(Boolean.TRUE.equals(flagRilevanteIva) && StringUtils.isBlank(subdoc.getNumeroRegistrazioneIVA())){
			throw new BusinessException(ErroreFin.INSERIMENTO_LIQUIDAZIONE_NON_POSSIBILE_MANCA_IL_NUMERO_REGISTRAZIONE_IVA.getErrore(""), Esito.FALLIMENTO);
		}
	}

//	protected Liquidazione popolaLiquidazione() {
//		
//		Liquidazione liquidazione = new Liquidazione();
//		
//		//se sono in aggiornamento della liquidazione -> carico quella presente sul db e sovrascrivo dopo i dati che potrebbero essere cambiati
//		if(subdoc.getLiquidazione() != null && subdoc.getLiquidazione().getUid() != 0){		
//			RicercaLiquidazionePerChiaveResponse resRLPC= ricercaLiquidazionePerChiave(subdoc.getLiquidazione(), bilancio);
//			liquidazione = resRLPC.getLiquidazione();
//			if(liquidazione == null){
//				throw new BusinessException("Errore aggioranmento Liquidazione "+subdoc.getLiquidazione().getAnnoLiquidazione()+"/"+subdoc.getLiquidazione().getNumeroLiquidazione()
//				+": Impossibile trovare la liquidazione con uid: "+subdoc.getLiquidazione().getUid());
//			}
//		}
//		
////		TransazioneElementare te = subdoc.getImpegnoOSubImpegno(); //firstNotNull(subdoc.getSubImpegno(), subdoc.getImpegno());
////		copiaDatiDaTransazioneElementareALiquidazione(te,liquidazione);
//		
//		Impegno impegno = subdoc.getImpegno();
//		SubImpegno subImpegno = subdoc.getSubImpegno();
//		//impegnoBilDad.aggiungiCodiciClasssificatoriAImpegnoEOSubImpegno(impegno,subImpegno);
//		
//		if(subImpegno != null){
//			impostaCodiciNellaLiquidazione(subImpegno, liquidazione);
//		}else{
//			impostaCodiciNellaLiquidazione(impegno, liquidazione);
//		}
//		
//		liquidazione.setImportoLiquidazione(subdoc.getImportoDaPagare());
//		liquidazione.setDescrizioneLiquidazione(subdoc.getCausaleOrdinativo());
//		
//		liquidazione.setImpegno(subdoc.getImpegno());
//		if(subdoc.getSubImpegno() != null){
//			liquidazione.setSubImpegno(subdoc.getSubImpegno());
//		}
//		liquidazione.setAttoAmministrativoLiquidazione(subdoc.getAttoAmministrativo());
//		liquidazione.setCup(subdoc.getCup());
//		liquidazione.setCig(subdoc.getCig());
//		
//		// Il numero del mutuo e' il numero della voceMutuo
//		if(subdoc.getVoceMutuo() != null  && StringUtils.isNotBlank(subdoc.getVoceMutuo().getNumeroMutuo())) {
//			liquidazione.setNumeroMutuo(integerize(subdoc.getVoceMutuo().getNumeroMutuo()));
//		}
//		
//		if(subdoc.getContoTesoreria() != null){
//			// Ricerco i dati: il servizio delle liquidazioni richiede il codice per l'inserimento
//			ContoTesoreria contoTesoreria = contoTesoreriaDad.findByUid(subdoc.getContoTesoreria().getUid());
//			liquidazione.setContoTesoreria(contoTesoreria);
//		}
//		
//		//e l'operatore ha l'azione associata con quietanza ('OP-Spe-insDocSpeQuietanza' o 'OP-Spe-aggDocSpeQuietanza').
//		if(Boolean.TRUE.equals(subdoc.getFlagACopertura()) || (subdoc.getAttoAmministrativo().getAllegatoAtto()!= null && subdoc.getAttoAmministrativo().getAllegatoAtto().getUid() != 0)){
//			liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.PROVVISORIO);
//		}else{
//			liquidazione.setStatoOperativoLiquidazione(StatoOperativoLiquidazione.VALIDO);
//		}
//		
//		//gestione soggetto e modalita' pagamento
//		Soggetto soggettoLiquidazione = creaSoggettoLiquidazione();
//		liquidazione.setSoggettoLiquidazione(soggettoLiquidazione);
//		
//		//se il TipoAccredito e' CSC o CSI la gestione della modalita' pagamento e' differente
//		gestisciCessioneDelCredito(liquidazione);
//		
//		SubdocumentoSpesa quota = new SubdocumentoSpesa();
//		quota.setUid(subdoc.getUid());
//		DocumentoSpesa doc = new DocumentoSpesa();
//		doc.setUid(documentoAssociato.getUid());
//		doc.setTipoDocumento(documentoAssociato.getTipoDocumento());
//		quota.setDocumento(doc);
//		
//		if(subdoc.getAttoAmministrativo()!=null) {
//			quota.setAttoAmministrativo(subdoc.getAttoAmministrativo());
//		}
//			
//		liquidazione.setSubdocumentoSpesa(quota);
//		liquidazione.setForza(true);
//		
//		return liquidazione;
//	}

//	private RicercaLiquidazionePerChiaveResponse ricercaLiquidazionePerChiave(Liquidazione liquidazione, Bilancio bilancio2) {
//		RicercaLiquidazionePerChiave reqRLPC = new RicercaLiquidazionePerChiave();
//		reqRLPC.setRichiedente(req.getRichiedente());
//		reqRLPC.setEnte(ente);
//		reqRLPC.setDataOra(req.getDataOra());
//		RicercaLiquidazioneK k = new RicercaLiquidazioneK();
//		k.setAnnoEsercizio(bilancio.getAnno());
//		k.setAnnoLiquidazione(liquidazione.getAnnoLiquidazione());
//		k.setBilancio(bilancio);
//		k.setLiquidazione(liquidazione);
//		k.setNumeroLiquidazione(liquidazione.getNumeroLiquidazione());
//		//k.setTipoRicerca(""); //Lascio il default
//		reqRLPC.setpRicercaLiquidazioneK(k);
//		RicercaLiquidazionePerChiaveResponse resRLPC = liquidazioneService.ricercaLiquidazionePerChiave(reqRLPC);
//		checkServiceResponseFallimento(resRLPC);
//		
//		return resRLPC;
//	}
//	
//	private void impostaCodiciNellaLiquidazione(Impegno impegno, Liquidazione l) {
//		l.setCodPdc(impegno.getCodPdc() != null ? impegno.getCodPdc() : "");
//		l.setCodTransazioneEuropeaSpesa(impegno.getCodTransazioneEuropeaSpesa() != null ? impegno.getCodTransazioneEuropeaSpesa() : "");
//		l.setCodRicorrenteSpesa(impegno.getCodRicorrenteSpesa() != null ? impegno.getCodRicorrenteSpesa() : "");
//		l.setCodContoEconomico(impegno.getCodContoEconomico() != null ? impegno.getCodContoEconomico() : "" );
//		l.setCodSiope(impegno.getCodSiope() != null ? impegno.getCodSiope() : "" );
//		l.setCodCofog(impegno.getCodCofog() != null ? impegno.getCodCofog() : "");
//		// SIAC-3655
//		l.setCodCapitoloSanitarioSpesa(impegno.getCodCapitoloSanitarioSpesa() != null ? impegno.getCodCapitoloSanitarioSpesa() : "");
//		l.setCodPrgPolReg(impegno.getCodPrgPolReg() != null ? impegno.getCodPrgPolReg() : "");
//	}

	/**
	 * Nel caso in cui la quota da liquidare è associata ad una modalità di pagamento 
	 * di tipo cessione del credito (Tipo.Accredito=CSC) oltre alle verifiche ai punti precedenti,  
	 * si inserisce la liquidazione da abbinare alla quota tramite il metodo Inserisce liquidazione  
	 * del servizio Gestione liquidazione passando in input il 
	 * 
	 * soggetto e 
	 * 
	 * modalità di pagamento (con eventuale sede) 
	 * 
	 * che sono collegati alla modalità di pagamento della quota tramite la relazione 'Cessione del Credito ad altro soggetto'.
	 */
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
//		//jira 2794
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

	/**
	 * Integerizzazione di una stringa.
	 * 
	 * @param str la stringa di partenza
	 * @return l'Integer corrispondente, se valido; <code>null</code> altrimenti
	 */
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

//	private void copiaDatiDaTransazioneElementareALiquidazione(TransazioneElementare te, Liquidazione liquidazione) {
//		if(te instanceof SubImpegno){
//			DummyMapper.mapNotNullNotEmpty((SubImpegno)te, liquidazione);
//		}else if(te instanceof Impegno){
//			DummyMapper.mapNotNullNotEmpty((Impegno)te, liquidazione);
//		}
//	}
	
	

	
//	private Soggetto creaSoggettoLiquidazione() {
//		
//		Soggetto soggettoLiquidazione = new Soggetto();
//		
//		RicercaSoggettoPerChiaveResponse resRSPC = ricercaSoggettoPerChiave(documentoAssociato.getSoggetto().getCodiceSoggetto());
//		Soggetto soggettoDocumento = resRSPC.getSoggetto();
//		if(soggettoDocumento == null || soggettoDocumento.getUid() == 0){
//			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", "codice " + documentoAssociato.getSoggetto().getCodiceSoggetto()));
//		}
//		
//		soggettoLiquidazione.setUid(soggettoDocumento.getUid());
//		soggettoLiquidazione.setCodiceSoggetto(soggettoDocumento.getCodiceSoggetto());
//		soggettoLiquidazione.setStatoOperativo(soggettoDocumento.getStatoOperativo());
//		
//		List<ModalitaPagamentoSoggetto> modalitaPagamentoList = new ArrayList<ModalitaPagamentoSoggetto>();
//		if(resRSPC.getListaModalitaPagamentoSoggetto() == null) {
//			// TODO
//			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("ListaModalitaPagamentoSoggetto", "Soggetto codice " + documentoAssociato.getSoggetto().getCodiceSoggetto()));
//		}
//		if(subdoc.getModalitaPagamentoSoggetto() != null) {
//			for(ModalitaPagamentoSoggetto mps : resRSPC.getListaModalitaPagamentoSoggetto()){
//				int uid = mps.getModalitaPagamentoSoggettoCessione2() != null && mps.getModalitaPagamentoSoggettoCessione2().getUid() != 0
//						? mps.getModalitaPagamentoSoggettoCessione2().getUid()
//						: mps.getUid();
//				if(uid == subdoc.getModalitaPagamentoSoggetto().getUid()){
//					mps = ricercaModalitaPagamento(mps, soggettoDocumento);
//					modalitaPagamentoList.add(mps);
//					break;
//				}
//			}
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
//
//	
//	private ModalitaPagamentoSoggetto ricercaModalitaPagamento(ModalitaPagamentoSoggetto mps, Soggetto soggetto) {
//		RicercaModalitaPagamentoPerChiave reqRMPC = new RicercaModalitaPagamentoPerChiave();
//		reqRMPC.setEnte(subdoc.getEnte());
//		reqRMPC.setRichiedente(req.getRichiedente());
//		reqRMPC.setModalitaPagamentoSoggetto(mps);
//		reqRMPC.setSoggetto(soggetto);
//		RicercaModalitaPagamentoPerChiaveResponse resRMPS = soggettoService.ricercaModalitaPagamentoPerChiave(reqRMPC);
//		log.logXmlTypeObject(resRMPS, "response ricerca modalità pagamento per chiave: ");
//		return resRMPS.getModalitaPagamentoSoggetto();
//	}

	
	/*
	 * Impostare flagOrdinativoSingolo = 'S'  (true) se vero almeno uno dei punti descritti di seguito.
	Se il documento a cui si riferisce la quota è collegato ad almeno 1 onere valido (dettaglioOnere con dataFineValidità non valorizzata)
	Se il documento a cui si riferisce la quota è collegato ad almeno un documento non annullato con una relazione 
	di TipoAssociazione = SUB - Documento Subordinato
	La regola di aggiornamento vale per le quote di entrambi i documenti coinvolti dalla relazione.
	 */
	protected void impostaFlagOrdinativo() {
		final String methodName = "impostaFlagOrdinativo";
		log.debug(methodName, "inizio del metodo impostaFlagOrdinativo");
		Boolean flag = null;
		log.debug(methodName, "flag iniziale: " + flag);
		//uso sempre documentoSpesaDad, il metodo è indipendente da entrata/spesa
		if(documentoAssociato.getRitenuteDocumento() != null &&  !documentoAssociato.getRitenuteDocumento().getListaOnere().isEmpty()){
			flag = Boolean.TRUE;
		}
		log.debug(methodName, "flag dopo il primo if (ritenute)" + flag);
		
		for(DocumentoSpesa docSpesa : documentoAssociato.getListaDocumentiSpesaFiglio()){
			if(TipoRelazione.SUBORDINATO.equals(docSpesa.getTipoRelazione())){
				log.debug(methodName, "ho trovato un documento subordinato di spesa. Uid:" + docSpesa.getUid());
				flag = Boolean.TRUE;
				documentoSpesaDad.impostaFlagSulleQuote(docSpesa.getUid(), TipologiaAttributo.FLAG_ORDINATIVO_SINGOLO, flag);				
			}
		}
		log.debug(methodName, "flag dopo il primo for (doc spesa figli) " + flag);
		
		for(DocumentoEntrata docEntrata : documentoAssociato.getListaDocumentiEntrataFiglio()){
			if(TipoRelazione.SUBORDINATO.equals(docEntrata.getTipoRelazione())){
				flag = Boolean.TRUE;
				log.debug(methodName, "ho trovato un documento subordinato di entrata. Uid: " + docEntrata.getUid());
				documentoSpesaDad.impostaFlagSulleQuote(docEntrata.getUid(), TipologiaAttributo.FLAG_ORDINATIVO_SINGOLO, flag);
			}
		}
		log.debug(methodName, "flag dopo il secondo for (doc entrata figli) " + flag);
		
		//SIAC-6294
		if(subdoc.getImpegno() != null && (flag == null || Boolean.FALSE.equals(flag))){
			flag = subdoc.getImpegno().isFlagAttivaGsa();
		}

		
		if(Boolean.TRUE.equals(flag)) {	
			documentoSpesaDad.impostaFlagSulleQuote(documentoAssociato.getUid(), TipologiaAttributo.FLAG_ORDINATIVO_SINGOLO, flag);			
		}
		
		if(flag != null){
			subdoc.setFlagOrdinativoSingolo(flag);
		}
		
	}
	
	/**
	 * carica voce mutuo 
	 * cerca la voce mutuo tra la lista vociMutuo dell'impegno o del subimpegno
	 */
	private void caricaVoceMutuo() {
		String methodName = "caricaVoceMutuo";
		boolean subImpegnoValorizzato = subdoc.getSubImpegno() != null && subdoc.getSubImpegno().getUid() != 0;
		boolean impegnoValorizzato = subdoc.getImpegno() != null && subdoc.getImpegno().getUid() != 0;

		if (subImpegnoValorizzato) {
			log.debug(methodName, "subImpegno valorizzato");
			VoceMutuo voceMutuo = null;
			List<VoceMutuo> listaVociMutuo = subdoc.getSubImpegno().getListaVociMutuo();

			if (listaVociMutuo != null && !listaVociMutuo.isEmpty()) {
				for (VoceMutuo vm : listaVociMutuo) {
					if (vm.getNumeroMutuo().equals(subdoc.getVoceMutuo().getNumeroMutuo())) {
						voceMutuo = vm;
						break;
					}
				}
			}
			// voce mutuo e' valida solo se il suo numero di mutuo e' uguale al
			// numero di mutuo passato dall'utente
			if (voceMutuo != null && voceMutuo.getUid() > 0) {
				subdoc.setVoceMutuo(voceMutuo);
				return;
			}
		}
		if (impegnoValorizzato) {
			log.debug(methodName, "Impegno valorizzato");
			VoceMutuo voceMutuo = null;
			List<VoceMutuo> listaVociMutuo = subdoc.getImpegno().getListaVociMutuo();

			if (listaVociMutuo != null && !listaVociMutuo.isEmpty()) {
				for (VoceMutuo vm : listaVociMutuo) {
					if (vm.getNumeroMutuo().equals(subdoc.getVoceMutuo().getNumeroMutuo())) {
						voceMutuo = vm;
						break;
					}
				}
			}
			// voce mutuo e' valida solo se il suo numero di mutuo e' uguale al
			// numero di mutuo passato dall'utente
			if (voceMutuo != null && voceMutuo.getUid() > 0) {
				subdoc.setVoceMutuo(voceMutuo);
				return;
			}
		}
		// Non ho i dati dell'impegno ne' del subimpegno: errore perche' in tal
		// caso e' obbligatorio
		// FIXME: trovare errore corretto
		throw new BusinessException("mutuo non presente nell'elenco dei collegati al movimento di gestione");
	}

	/**
	 * controlla la disponibilita a liquidare della voce mutuo
	 * disponibilitaLiquidare + importo vecchio >= documento.importo
	 */
	protected void controllaDisponibilitaVoceMutuo() {
		String methodName = "controllaDisponibilitaVoceMutuo";

		BigDecimal importoPrecedente = BigDecimal.ZERO;
		BigDecimal importoVecchioDisponiblitaLiquidare = BigDecimal.ZERO;

		Integer uidVoceMutuoPrecedente = caricaUidVoceMutuoPrecedente();
		log.debug(methodName, "uidVoceMutuoPrecedente: " + uidVoceMutuoPrecedente);
		if (uidVoceMutuoPrecedente != null && uidVoceMutuoPrecedente == subdoc.getVoceMutuo().getUid()) {
			importoPrecedente = caricaImportoDaPagarePrecedenteQuota();
		}
		importoVecchioDisponiblitaLiquidare = importoVecchioDisponiblitaLiquidare.add(importoPrecedente).add(subdoc.getVoceMutuo().getImportoDisponibileLiquidareVoceMutuo());
		log.debug(methodName, "Importo vecchio + disponibilita a liquidare :" + importoVecchioDisponiblitaLiquidare);
		log.debug(methodName, "Importo attuale documento " + subdoc.getImporto());
		if (importoVecchioDisponiblitaLiquidare.compareTo(subdoc.getImporto()) < 0) {
			log.debug(methodName, "Disponibilita insufficiente ");
			throw new BusinessException(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore("Aggiornamento quota documento spesa", "Voce Mutuo"));
		}
	}

	protected void gestisciNumeroRegistrazioneIva() {
		if(!Boolean.TRUE.equals(subdoc.getFlagRilevanteIVA())){
			//la quota non e' rilevante iva, non devo fare nulla
			return;
		}
		if(documentoAssociato == null || documentoAssociato.getListaSubdocumentoIva() == null || documentoAssociato.getListaSubdocumentoIva().isEmpty()){
			//non esiste subdoc iva collegato all'intero documento, non devo fare null
			return;
		}
		SubdocumentoIvaSpesa subdocIva = documentoAssociato.getListaSubdocumentoIva().get(0);
		subdoc.setNumeroRegistrazioneIVA(subdocIva.getNumeroRegistrazioneIVA());
		
	}
	
	private Integer caricaUidVoceMutuoPrecedente() {
		if (subdoc.getUid() == 0) {
			return null;
		}
		return subdocumentoSpesaDad.findUidVoceMutuoSubdocumentoSpesaById(subdoc.getUid());
	}
	
	protected void checkNumero() throws ServiceParamError {
		checkCondition(StringUtils.isNotBlank(doc.getNumero()), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		doc.setNumero(StringUtils.trimToNull(doc.getNumero()));
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
//		throw new BusinessException("Tipologia Gestione Livello "+TipologiaGestioneLivelli.GESTIONE_CONVALIDA_AUTOMATICA.name() +" non configurato per l'ente (uid: "+ente.getUid()+"). "
//				+ "Occorre modificare la configurazione dell'ente.");
	}
	
	/**
	 * Controlla se esiste gi&agrave; un documento con lo stesso numero e anno.
	 * Se esiste viene sollevata l'eccezione con il messaggio:
	 * 
	 * <COR_ERR_0008, Entit&agrave; gi&agrave; presente, 'DOCUMENTO ENTRATA', 
	 * (chiave ricerca entit&agrave; = anno documento del parametro di input + "/" + numero documento del parametro di input + "/" +
	 * tipo documento del parametro di input + "/" + soggetto del paramentro 
	 * di input + "/" + stato del documento trovato)>
	 * 
	 * <FIN_ERR_0087, Operazione non possibile, 'Inserimento Documento Entrata.'> 
	 * 
	 */
	protected void checkDocumentoGiaEsistente() {
		RicercaPuntualeDocumentoSpesa reqRP = new RicercaPuntualeDocumentoSpesa();
		reqRP.setRichiedente(req.getRichiedente());
		
		DocumentoSpesa d = new DocumentoSpesa();
		d.setAnno(doc.getAnno());
		d.setNumero(doc.getNumero());
		d.setTipoDocumento(doc.getTipoDocumento());
		d.setStatoOperativoDocumento(null);
		d.setSoggetto(doc.getSoggetto());
		d.setEnte(doc.getEnte());
		
		reqRP.setStatoOperativoDocumentoDaEscludere(StatoOperativoDocumento.ANNULLATO);
		
		reqRP.setDocumentoSpesa(d);
		RicercaPuntualeDocumentoSpesaResponse resRP = serviceExecutor.executeService(RicercaPuntualeDocumentoSpesaService.class, reqRP);
		
		if(resRP.getDocumentoSpesa() != null) {
			throw new BusinessException(ErroreCore.ENTITA_PRESENTE.getErrore("Inserimento Documento Spesa", resRP.getDocumentoSpesa().getDescAnnoNumeroTipoDocSoggettoStato()), Esito.FALLIMENTO, false);
		}
		
	}
	
	/**
	 * Gestione della sospensione.
	 * <br/>
	 * La sospensione &eacute; un elemento collegato direttamente al subdoc. Viene gestito manualmente
	 * @param gestisciSospensioni se le sospensioni siano da gestire
	 */
	protected void gestisciSospensioniSubdocumento(boolean gestisciSospensioni) {
		final String methodName = "gestisciSospensioniSubdocumento";
		if(!gestisciSospensioni) {
			log.debug(methodName, "Gestione sospensioni non richiesta");
			return;
		}
		subdocumentoSpesaDad.aggiornaSospensioni(subdoc);
	}
	
	/**
	 * Controlli di merito sui dati di sospensione
	 * @param gestisciSospensioni se le sospensioni siano da gestire
	 */
	protected void checkSospensione(boolean gestisciSospensioni) throws ServiceParamError {
		if(!gestisciSospensioni) {
			// Gestione della sospensione non richiesto: esco
			subdoc.setSospensioni(new ArrayList<SospensioneSubdocumento>());
			return;
		}
		for(SospensioneSubdocumento ss : subdoc.getSospensioni()) {
			// La data di scadenza deve essere maggiore o uguale la data di emissione
			checkCondition(ss.getDataSospensione() != null && StringUtils.isNotBlank(ss.getCausaleSospensione()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("data e causale di sospensione"));
			checkCondition(ss.getDataSospensione() == null
					|| ss.getDataRiattivazione() == null
					|| ss.getDataRiattivazione().after(ss.getDataSospensione()),
				it.csi.siac.siacfin2ser.model.errore.ErroreFin.DATA_RIATTIVAZIONE_SOSPENSIONE_NON_CORRETTA.getErrore());
		}
	}
}

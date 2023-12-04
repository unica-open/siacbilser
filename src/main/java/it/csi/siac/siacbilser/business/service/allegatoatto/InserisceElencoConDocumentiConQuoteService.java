/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AccertamentoBilDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.ProvvisorioBilDad;
import it.csi.siac.siacbilser.integration.dad.RegistroUnicoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroUnico;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.integration.dad.datacontainer.DisponibilitaMovimentoGestioneContainer;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Inserimento di un elenco e creazione dei documenti spesa/entrata con le relative quote raggruppate per soggetto.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceElencoConDocumentiConQuoteService extends InserisceElencoBaseService {
	
	/** Formattazione per il numero del documento */
	private static final String FORMAT_NUMERO_DOC = "%d/%d/%s/%s/%d/%02d";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
	
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired
	private RegistroUnicoDad registroUnicoDad;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	@Autowired
	private AccertamentoBilDad accertamentoBilDad;
	@Autowired
	private ProvvisorioBilDad provvisorioBilDad;
	
	@Autowired
	private AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaService;
	@Autowired
	private AggiornaStatoDocumentoDiEntrataService aggiornaStatoDocumentoDiEntrataService;
	

	private AllegatoAtto allegatoAtto;

	private TipoDocumento tipoDocumentoAllegatoAttoSpesa;
	private TipoDocumento tipoDocumentoAllegatoAttoEntrata;

	private CodiceBollo codiceBolloEsente;
	
	private int progressivoDocumentoSpesa = 1; //progressivo dei documenti di spesa inseriti. Parte da 1
	private int progressivoDocumentoEntrata = 1;//progressivo dei documenti di entrata inseriti. Parte da 1
	

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.elencoDocumentiAllegato = req.getElencoDocumentiAllegato();
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		this.bilancio = req.getBilancio();
		
		checkNotNull(elencoDocumentiAllegato, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti"));
		checkNotNull(elencoDocumentiAllegato.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente elenco documenti"));
		checkCondition(elencoDocumentiAllegato.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente elenco documenti"));
		checkNotNull(elencoDocumentiAllegato.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto elenco documenti"));
		checkCondition(elencoDocumentiAllegato.getAllegatoAtto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto elenco documenti"));
		this.allegatoAtto = elencoDocumentiAllegato.getAllegatoAtto();
		
		checkNotNull(elencoDocumentiAllegato.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno elenco documenti"));
		
		if (elencoDocumentiAllegato.getStatoOperativoElencoDocumenti() == null) {
			//Se non specificato lo stato operativo elenco di default è BOZZA
			elencoDocumentiAllegato.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
		}
		
		checkNotNull(elencoDocumentiAllegato.getSubdocumenti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumenti"));
		checkCondition(!elencoDocumentiAllegato.getSubdocumenti().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumenti"));
		
		for(Subdocumento<?, ?> subdoc : elencoDocumentiAllegato.getSubdocumenti()){
			checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento"));
			checkCondition(subdoc.getUid()==0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento"));
			
			
			checkNotNull(subdoc.getMovimentoGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("movimento gestione subdocumento"));
			checkCondition(subdoc.getMovimentoGestione().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid movimento gestione subdocumento"));
			
			boolean soggettoInMovimentoGestione = subdoc.getMovimentoGestione()!=null && subdoc.getMovimentoGestione().getSoggetto()!=null && subdoc.getMovimentoGestione().getSoggetto().getUid()!=0 ;
			boolean soggettoInSubMovimentoGestione = subdoc.getSubMovimentoGestione()!=null && subdoc.getSubMovimentoGestione().getSoggetto()!=null && subdoc.getSubMovimentoGestione().getSoggetto().getUid()!=0 ;
			
			checkCondition(soggettoInMovimentoGestione || soggettoInSubMovimentoGestione, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto movimento o submovimento gestione subdocumento"));
			
			if(soggettoInSubMovimentoGestione){
				subdoc.getMovimentoGestione().setSoggetto(subdoc.getSubMovimentoGestione().getSoggetto());
			}
		}
		for(SubdocumentoSpesa subdocSpesa : elencoDocumentiAllegato.getSubdocumentiSpesa()) {
			checkEntita(subdocSpesa.getModalitaPagamentoSoggetto(), "modalita pagamento soggetto subdocumento");
		}
		
	}
	
	@Override
	@Transactional(timeout=300)
	public InserisceElencoResponse executeService(InserisceElenco serviceRequest) {
		return super.executeService(serviceRequest);
	}	
	
	@Override
	protected void init() {
		super.init();
		
		documentoSpesaDad.setEnte(elencoDocumentiAllegato.getEnte());
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		documentoEntrataDad.setEnte(elencoDocumentiAllegato.getEnte());
		documentoEntrataDad.setLoginOperazione(loginOperazione);
		
		subdocumentoEntrataDad.setEnte(elencoDocumentiAllegato.getEnte());
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
		
		registroUnicoDad.setEnte(elencoDocumentiAllegato.getEnte());
		registroUnicoDad.setLoginOperazione(loginOperazione);
	}
	
	@Override
	protected void execute() {
		
		checkDisponibilita();
		this.bilancio = caricaBilancio(bilancio.getUid());
		this.allegatoAtto = caricaAllegatoAtto(allegatoAtto.getUid());
		caricaCodificheDocumento();
		
		checkSoggetto();
		checkMovimentiResidui();
		
		staccaNumeroElenco();
		
		
		checkProvvisorioCassa();
		inserisciDocumentiESubdocumenti();
		// Per la completezza dell'allegato atto
		if(StatoOperativoAllegatoAtto.COMPLETATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())
				|| StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())) {
			valutaInserimentoLiquidazioni(elencoDocumentiAllegato.getSubdocumentiSpesa(), allegatoAtto);
			elencoDocumentiAllegato.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.COMPLETATO);
		}
		
		elencoDocumentiAllegatoDad.inserisciElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		// SIAC-5300
		aggiornaStatoDocumenti();
		
		res.setElencoDocumentiAllegato(elencoDocumentiAllegato);
	}

	private void checkProvvisorioCassa() {
		Map<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
		if(elencoDocumentiAllegato.getAllegatoAtto() != null){
			
			for(Subdocumento<?, ?> subdoc : elencoDocumentiAllegato.getSubdocumenti()) {
				if(subdoc.getProvvisorioCassa() == null){
					continue;
				}
				
				if(!map.containsKey(subdoc.getProvvisorioCassa().getUid())){
					map.put(subdoc.getProvvisorioCassa().getUid(), BigDecimal.ZERO);
				}
				map.put(subdoc.getProvvisorioCassa().getUid(), map.get(subdoc.getProvvisorioCassa().getUid()).add(subdoc.getImporto()));
			}
			
			for(Map.Entry<Integer, BigDecimal> entry : map.entrySet()){
				ProvvisorioDiCassa provv = new ProvvisorioDiCassa();
				provv.setUid(entry.getKey());
				BigDecimal importoDaRegolarizzareProvvisorio = provvisorioBilDad.calcolaImportoDaRegolarizzareProvvisorio(provv);
				if(importoDaRegolarizzareProvvisorio.compareTo(map.get(entry.getKey())) < 0){
					throw new BusinessException(ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo in atto deve essere minore o uguale all'importo da regolarizzare sul provvisorio di cassa backend"));
				}
			}
		}
	}
	
	private void checkDisponibilita() {
		checkDisponibilitaLiquidare();
		checkDisponibilitaIncassare();
		
	}

	private void checkDisponibilitaIncassare() {
		String methodName = "checkDisponibilitaIncassare";
		//costruisco la mappa con key = uidTestata e value = importo totale dei dicumenti che vorrei inserire su quella testata
		Map<Integer, BigDecimal> mappaTestateImporti = new HashMap<Integer, BigDecimal>();
		Map<Integer, Integer> mappaMovimentoTestata = new HashMap<Integer, Integer>();
		for(SubdocumentoEntrata se : elencoDocumentiAllegato.getSubdocumentiEntrata()){
			
			Integer uidTestata = null;
			if(se.getSubAccertamento() != null && se.getSubAccertamento().getUid() != 0 ){
				log.debug(methodName, "il subdoc ha subaccertamento valorizzato con uid " + se.getSubAccertamento().getUid());
				uidTestata = se.getSubAccertamento().getUid();
			}else if(se.getAccertamento() != null && se.getAccertamento().getUid() != 0){
				log.debug(methodName, "il subdoc ha accertamento valorizzato con uid " + se.getAccertamento().getUid());
				uidTestata = mappaMovimentoTestata.get(se.getAccertamento().getUid());
				if(uidTestata == null){
					uidTestata = accertamentoBilDad.findUidTestataByUidMovimento(se.getAccertamento().getUid());
					mappaMovimentoTestata.put(se.getAccertamento().getUid(), uidTestata);
				}
				log.debug(methodName, "recuperata la relativa testata, con uid " + uidTestata);
			}
			
			if(uidTestata != null){
				BigDecimal importoAttualeSuTestata = mappaTestateImporti.get(uidTestata);
				importoAttualeSuTestata = importoAttualeSuTestata == null ? se.getImporto() : importoAttualeSuTestata.add(se.getImporto());
				mappaTestateImporti.put(uidTestata, importoAttualeSuTestata);
			}
		}
		//controllo la disponibilita sulla testata
		for(Entry<Integer, BigDecimal> entry : mappaTestateImporti.entrySet()) {
			Integer uidTestata = entry.getKey();
			BigDecimal importoTotaleSubdocSuTestata = entry.getValue();
			
			BigDecimal disponibilitaTestata = accertamentoBilDad.ottieniDisponibilitaIncassareDaTestata(uidTestata);
			log.debug(methodName, "diponibilita' movgestTs: " + disponibilitaTestata);
			log.debug(methodName, "totale subdoc: " + importoTotaleSubdocSuTestata);
			if(disponibilitaTestata.compareTo(importoTotaleSubdocSuTestata)<0){
				throw new BusinessException(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore("Inserimento quota documento entrata", "movimento gestione"));
			}
		}
	}

	private void checkDisponibilitaLiquidare() {
		String methodName = "checkDisponibilitaLiquidare";
		//costruisco la mappa con key = uidTestata e value = importo totale dei dicumenti che vorrei inserire su quella testata
		Map<Integer, BigDecimal> mappaTestateImporti = new HashMap<Integer, BigDecimal>();
		Map<Integer, Integer> mappaMovimentoTestata = new HashMap<Integer, Integer>();
		for(SubdocumentoSpesa ss : elencoDocumentiAllegato.getSubdocumentiSpesa()){
			Integer uidTestata = null;
			if(ss.getSubImpegno() != null && ss.getSubImpegno().getUid() != 0 ){
				log.debug(methodName, "il subdoc ha subimpegno valorizzato con uid " + ss.getSubImpegno().getUid());
				uidTestata = ss.getSubImpegno().getUid();
			}else if(ss.getImpegno() != null && ss.getImpegno().getUid() != 0){
				log.debug(methodName, "il subdoc ha impegno valorizzato con uid " + ss.getImpegno().getUid());
				uidTestata = mappaMovimentoTestata.get(ss.getImpegno().getUid());
				if(uidTestata == null){
					uidTestata = impegnoBilDad.findUidTestataByUidMovimento(ss.getImpegno().getUid());
					mappaMovimentoTestata.put(ss.getImpegno().getUid(), uidTestata);
				}
				log.debug(methodName, "recuperata la relativa testata, con uid " + uidTestata);
			}
			
			if(uidTestata != null){
				BigDecimal importoAttualeSuTestata = mappaTestateImporti.get(uidTestata);
				importoAttualeSuTestata = importoAttualeSuTestata == null ? ss.getImporto() : importoAttualeSuTestata.add(ss.getImporto());
				log.debug(methodName, "aggiungo/aggiorno nella mappa la testata: " + uidTestata + " con importo " + importoAttualeSuTestata);
				mappaTestateImporti.put(uidTestata, importoAttualeSuTestata);
			}
		}
		//controllo la disponibilita sulla testata
		for(Entry<Integer, BigDecimal> entry : mappaTestateImporti.entrySet()){
			Integer uidTestata = entry.getKey();
			BigDecimal importoTotaleSubdocSuTestata = entry.getValue();
			
			DisponibilitaMovimentoGestioneContainer disponibilitaTestata = impegnoBilDad.ottieniDisponibilitaLiquidareDaTestata(uidTestata, req.getAnnoBilancio());
			log.debug(methodName, "diponibilita' movgestTs: " + disponibilitaTestata.getDisponibilita() + ", motivazione: " + disponibilitaTestata.getMotivazione());
			log.debug(methodName, "totale subdoc: " + importoTotaleSubdocSuTestata);
			if(disponibilitaTestata == null || disponibilitaTestata.getDisponibilita() == null || disponibilitaTestata.getDisponibilita().compareTo(importoTotaleSubdocSuTestata)<0){
				throw new BusinessException(ErroreFin.DISPONIBILITA_INSUFFICIENTE_MOVIMENTO.getErrore("Inserimento quota documento spesa", "movimento gestione"));
			}
		}
	}

	
	private void caricaCodificheDocumento() {
		this.tipoDocumentoAllegatoAttoSpesa = documentoSpesaDad.findTipoDocumentoAllegatoAtto();
		this.tipoDocumentoAllegatoAttoEntrata = documentoEntrataDad.findTipoDocumentoAllegatoAtto();
		this.codiceBolloEsente = documentoSpesaDad.findCodiceBolloEsente();
		
	}

	/**
	 * 2.5.2	Controlli sul soggetto
	 * Ogni soggetto associato all'Allegato Atto deve rispettare i seguenti requisiti:
	 * 
	 * Il soggetto selezionato deve essere in stato 'VALIDO'  o 'SOSPESO' altrimenti viene inviato il messaggio <FIN_ERR_0104, Soggetto non valido>.
	 * La sede selezionata deve essere in stato 'VALIDO'  altrimenti viene inviato il messaggio < FIN_ERR_0175 Sede Secondaria Soggetto non valida>.
	 * La MODALITA' DI PAGAMENTO è anch'essa obbligatoria se viene associato all'atto per quel soggetto almeno un movimento di spesa, inoltre la modalità di pagamento deve rispettare le seguenti condizioni:
	 *  -  in stato 'VALIDO'  altrimenti viene visualizzato il messaggio < FIN_ERR_0175 Sede Secondaria Soggetto non valida>.
	 * - la dataScadenza deve essere non valorizzata oppure >= alla data corrente altrimenti viene inviato il messaggio < COR_ERR_0029	Incongruenza nei parametri (modalità di pagamento scaduta)>
	 * 
	 */
	private void checkSoggetto() {
		// So per certo che la lista non e' vuota;
		Subdocumento<?, ?> subdocumento = elencoDocumentiAllegato.getSubdocumenti().get(0);
		MovimentoGestione mg = null;
		if(subdocumento instanceof SubdocumentoSpesa) {
			mg = ((SubdocumentoSpesa)subdocumento).getImpegno();
		} else if (subdocumento instanceof SubdocumentoEntrata) {
			mg = ((SubdocumentoEntrata)subdocumento).getAccertamento();
		}
		
		if(mg == null) {
			throw new BusinessException(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("impegno"));
		}
		
		Soggetto soggetto = mg.getSoggetto();
		if(soggetto == null || soggetto.getUid() == 0) {
			throw new BusinessException(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("soggetto"));
		}
		
		//TODO Auto-generated method stub
		
	}

	/**
	 * Inserisce un nuovo Documento di Spesa/Entrata (con i relativi subdocumenti) 
	 * per ogni Soggetto dei subdocumenti Spesa/Entrata presente nell'elenco.
	 * 
	 */
	private void inserisciDocumentiESubdocumenti() {
		final String methodName = "inserisciDocumentoESubdocumentiSpesa";

		Map<Integer, List<Subdocumento<?, ?>>> subdocRaggruppatiPerSoggetto = groupBySoggetto(elencoDocumentiAllegato.getSubdocumenti());
		log.info(methodName, "Numero di Soggetti da elaborare: "+ subdocRaggruppatiPerSoggetto.size());
		
		
		
		for(Entry<Integer, List<Subdocumento<?,?>>> entry : subdocRaggruppatiPerSoggetto.entrySet()){
			Integer uidSoggetto = entry.getKey();
			List<Subdocumento<?,?>> subdocumentiSoggetto = entry.getValue();
			
			log.info(methodName, "Soggetto con uid: "+ uidSoggetto + " numero subdocumenti (Spesa e Entrata): "+subdocumentiSoggetto.size());
			
			inserisciDocumentoESubdocumentiSpesa(subdocumentiSoggetto, uidSoggetto); 
			
			inserisciDocumentoESubdocumentiEntrata(subdocumentiSoggetto, uidSoggetto); 
			
		}
		
		
		
	}

	private void inserisciDocumentoESubdocumentiSpesa(List<Subdocumento<?,?>> subdocumentiSoggetto, Integer uidSoggetto) {
		final String methodName = "inserisciDocumentoESubdocumentiSpesa";
		
		List<SubdocumentoSpesa> subdocumentiSpesaSoggetto = filtraSubdocumenti(SubdocumentoSpesa.class, subdocumentiSoggetto);
		log.info(methodName, "subdocumentiSpesaSoggetto con uid "+uidSoggetto+": "+ subdocumentiSpesaSoggetto.size());
		
		if(subdocumentiSpesaSoggetto.isEmpty()) {
			log.debug(methodName, "Non sono presenti subdocumenti spesa da inserire");
			return;
		}
		
		DocumentoSpesa documentoSpesa = creaDocumentoSpesa(uidSoggetto, subdocumentiSpesaSoggetto);		
		inserisciDocumentoSpesa(documentoSpesa);
		
		for(SubdocumentoSpesa subdoc : subdocumentiSpesaSoggetto){
			popolaSubdocumento(subdoc, documentoSpesa);
			inserisciSubdocumentoSpesa(subdoc);
		}
		
		aggiornaStatoOperativoDocumentoSpesaPadre(documentoSpesa);
	}
	
	
	private void inserisciDocumentoESubdocumentiEntrata(List<Subdocumento<?,?>> subdocumentiSoggetto, Integer uidSoggetto) {
		final String methodName = "inserisciDocumentoESubdocumentiEntrata";
		
		List<SubdocumentoEntrata> subdocumentiEntrataSoggetto = filtraSubdocumenti(SubdocumentoEntrata.class, subdocumentiSoggetto);
		log.info(methodName, "subdocumentiEntrataSoggetto con uid "+uidSoggetto+": "+ subdocumentiEntrataSoggetto.size());
		
		if(subdocumentiEntrataSoggetto.isEmpty()) {
			log.debug(methodName, "Non sono presenti subdocumenti entrata da inserire");
			return;
		}
		
		DocumentoEntrata documentoEntrata = creaDocumentoEntrata(uidSoggetto, subdocumentiEntrataSoggetto);		
		inserisciDocumentoEntrata(documentoEntrata);
		
		for(SubdocumentoEntrata subdoc : subdocumentiEntrataSoggetto){
			popolaSubdocumento(subdoc, documentoEntrata);
			inserisciSubdocumentoEntrata(subdoc);
		}
		
		aggiornaStatoOperativoDocumentoEntrataPadre(documentoEntrata);
	}


	private DocumentoSpesa aggiornaStatoOperativoDocumentoSpesaPadre(DocumentoSpesa docSpesa) {
		AggiornaStatoDocumentoDiSpesa reqAs = new AggiornaStatoDocumentoDiSpesa();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setDocumentoSpesa(docSpesa);
		reqAs.setBilancio(bilancio);
		AggiornaStatoDocumentoDiSpesaResponse resAs = executeExternalService(aggiornaStatoDocumentoDiSpesaService, reqAs);
		return resAs.getDocumentoSpesa();
	}
	
	private DocumentoEntrata aggiornaStatoOperativoDocumentoEntrataPadre(DocumentoEntrata documentoEntrata) {
		AggiornaStatoDocumentoDiEntrata reqAs = new AggiornaStatoDocumentoDiEntrata();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setBilancio(bilancio);
		reqAs.setDocumentoEntrata(documentoEntrata);
		AggiornaStatoDocumentoDiEntrataResponse resAs = executeExternalService(aggiornaStatoDocumentoDiEntrataService, reqAs);
		return resAs.getDocumentoEntrata();
	}


	@SuppressWarnings("unchecked")
	private <SD extends Subdocumento<?, ?>> List<SD> filtraSubdocumenti(Class<SD> clazz, List<Subdocumento<?, ?>> subdocumenti) {
		List<SD> result = new ArrayList<SD>();
		for(Subdocumento<?, ?> sub : subdocumenti) {
			if(clazz.isInstance(sub)) {
				result.add((SD)sub);
			}
		}
		return result;
	}

	/**
	 * Raggruppa la lista di subdocumenti per il Soggetto del MovimentoGestione 
	 * 
	 * @param suboducumenti
	 * @return
	 */
	private  Map<Integer, List<Subdocumento<?,?>>> groupBySoggetto(List<Subdocumento<?,?>> suboducumenti) {
		Map<Integer, List<Subdocumento<?,?>>> result = new HashMap<Integer, List<Subdocumento<?,?>>>();
		for(Subdocumento<?,?> s : suboducumenti) {
			Integer uidSoggetto = s.getMovimentoGestione().getSoggetto().getUid();
			if(!result.containsKey(uidSoggetto)){
				result.put(uidSoggetto, new ArrayList<Subdocumento<?,?>>());
			}
			result.get(uidSoggetto).add(s);
		}
		return result;
	}



	private void inserisciSubdocumentoSpesa(SubdocumentoSpesa subdoc) {
		Integer numeroSubdocumento = subdocumentoSpesaDad.staccaNumeroSubdocumento(subdoc.getDocumento().getUid());
		subdoc.setNumero(numeroSubdocumento);
		subdocumentoSpesaDad.inserisciAnagraficaSubdocumentoSpesa(subdoc);
	}

	private void inserisciSubdocumentoEntrata(SubdocumentoEntrata subdoc) {
		Integer numeroSubdocumento = subdocumentoEntrataDad.staccaNumeroSubdocumento(subdoc.getDocumento().getUid());
		subdoc.setNumero(numeroSubdocumento);
		subdocumentoEntrataDad.inserisciAnagraficaSubdocumentoEntrata(subdoc);
	}

	private <S extends Subdocumento<D, ?>, D extends Documento<S, ?>> void popolaSubdocumento(S subdoc, D doc) {
		subdoc.setDocumento(doc);
		subdoc.setEnte(elencoDocumentiAllegato.getEnte());
		
		subdoc.setAttoAmministrativo(allegatoAtto.getAttoAmministrativo());
	}

	private void inserisciDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		documentoSpesaDad.inserisciAnagraficaDocumentoSpesa(documentoSpesa);
	}
	
	private void inserisciDocumentoEntrata(DocumentoEntrata documentoEntrata) {
		documentoEntrataDad.inserisciAnagraficaDocumentoEntrata(documentoEntrata);
	}
	
	

	private DocumentoSpesa creaDocumentoSpesa(Integer uidSoggetto, List<SubdocumentoSpesa> subdocumentiSpesaSoggetto) {
		DocumentoSpesa d = new DocumentoSpesa();
		d.setEnte(elencoDocumentiAllegato.getEnte());
		d.setAnno(bilancio.getAnno());
		
		String numeroDocumentoSpesa = generaNumeroDocumentoSpesa();
		d.setNumero(numeroDocumentoSpesa);
		d.setNumeroRepertorio(numeroDocumentoSpesa);
		d.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		// Documento.descrizione e subdocumento.descrizione = AllegatoAtto.causale
		String descrizioneDocumento = allegatoAtto.getCausale();
		d.setDescrizione(descrizioneDocumento);
		
		// Tipo documento -> tipo documento ALG - ALLEGATO ATTO
		d.setTipoDocumento(tipoDocumentoAllegatoAttoSpesa);
		// codice bollo: ESENTE
		d.setCodiceBollo(codiceBolloEsente);
		
		Date now = Utility.truncateToStartOfDay(new Date());
		d.setDataEmissione(now);
		d.setDataRepertorio(now);
		// Data scadenza documento e quota = data scadenza Allegato Atto
		d.setDataScadenza(allegatoAtto.getDataScadenza());
		
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(uidSoggetto);
		d.setSoggetto(soggetto);
		
		BigDecimal importoDocumento = calcolaTotaleImportoQuote(subdocumentiSpesaSoggetto);
		d.setImporto(importoDocumento);
		
		String causaleOrdinativo = "DOCUMENTO N. " + numeroDocumentoSpesa + " DEL " + sdf.format(now);
		for(SubdocumentoSpesa subdoc : subdocumentiSpesaSoggetto) {
			subdoc.setDataScadenza(allegatoAtto.getDataScadenza());
			subdoc.setDescrizione(descrizioneDocumento);
			subdoc.setCausaleOrdinativo(causaleOrdinativo);
			subdoc.setAttoAmministrativo(allegatoAtto.getAttoAmministrativo());
			subdoc.setCommissioniDocumento(CommissioniDocumento.BENEFICIARIO);
			
		}
		
		// Devo staccare il numero di registro unico (se ne sarebbe occupato il servizio di inserimento)
		impostaNumeroRegistroUnico(d);
		
		return d;
	}
	
	/**
	 * Stacca il numero di {@link RegistroUnico} per il documento e lo imposta nell'oggetto {@link #doc}
	 * @param doc
	 */
	private void impostaNumeroRegistroUnico(DocumentoSpesa doc) {
		final String methodName = "impostaNumeroRegistroUnico";
		if(!Boolean.TRUE.equals(doc.getTipoDocumento().getFlagRegistroUnico())) {
			log.debug(methodName, "Il tipo documento " + doc.getTipoDocumento().getCodice() + " per l'ente " + ente.getUid() + " ha il flagRegistroUnico non a TRUE: registro unico non da impostare");
			return;
		}
		
		Integer numeroRegistroUnico = registroUnicoDad.staccaNumeroRegistroUnico(doc.getAnno());
		log.debug(methodName, "numeroRegistroUnico: "+numeroRegistroUnico + " anno: "+doc.getAnno());
		
		RegistroUnico registroUnico = new RegistroUnico();
		registroUnico.setAnno(doc.getAnno());
		registroUnico.setNumero(numeroRegistroUnico);
		registroUnico.setDateRegistro(new Date());
		doc.setRegistroUnico(registroUnico);
	}

	
	
	private DocumentoEntrata creaDocumentoEntrata(Integer uidSoggetto, List<SubdocumentoEntrata> subdocumentiEntrataSoggetto) {
		DocumentoEntrata d = new DocumentoEntrata();
		d.setEnte(elencoDocumentiAllegato.getEnte());
		d.setAnno(bilancio.getAnno());
		
		final String numeroDocumentoEntrata = generaNumeroDocumentoEntrata();
		d.setNumero(numeroDocumentoEntrata);
		d.setNumeroRepertorio(numeroDocumentoEntrata);
		d.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		String descrizioneDocumento = allegatoAtto.getCausale();
		d.setDescrizione(descrizioneDocumento);
		
		d.setTipoDocumento(tipoDocumentoAllegatoAttoEntrata);
		d.setCodiceBollo(codiceBolloEsente);
		
		Date now = Utility.truncateToStartOfDay(new Date());		
		d.setDataEmissione(now);
		d.setDataRepertorio(now);
		d.setDataScadenza(allegatoAtto.getDataScadenza());
		
		Soggetto soggetto = new Soggetto();
		soggetto.setUid(uidSoggetto);
		d.setSoggetto(soggetto);
		
		BigDecimal importoDocumento = calcolaTotaleImportoQuote(subdocumentiEntrataSoggetto);
		d.setImporto(importoDocumento);
		
		for(SubdocumentoEntrata subdoc : subdocumentiEntrataSoggetto) {
			subdoc.setDataScadenza(allegatoAtto.getDataScadenza());
			subdoc.setDescrizione(descrizioneDocumento);
			subdoc.setAttoAmministrativo(allegatoAtto.getAttoAmministrativo());
		}
		
		return d;
	}
	
	
	/**
	 * Calcola totale importo delle quote passate come parametro.
	 *
	 * @param subdocumenti the subdocumenti
	 * @return the big decimal
	 */
	public BigDecimal calcolaTotaleImportoQuote(List<? extends Subdocumento<?,?>> subdocumenti) {
		BigDecimal result = BigDecimal.ZERO;
		for (Subdocumento<?,?> se : subdocumenti) {
			result = result.add(se.getImporto());
		}
		return result;
	}
	
	private String generaNumeroDocumentoSpesa() {
		return generaNumeroDocumento(progressivoDocumentoSpesa++);
	}
	
	private String generaNumeroDocumentoEntrata() {
		return generaNumeroDocumento(progressivoDocumentoEntrata++);
	}
	
	private String generaNumeroDocumento(int progressivo) {
		AttoAmministrativo aa = allegatoAtto.getAttoAmministrativo();
		return String.format(FORMAT_NUMERO_DOC, aa.getAnno(), aa.getNumero(), aa.getTipoAtto().getCodice(),
				aa.getStrutturaAmmContabile() != null ? aa.getStrutturaAmmContabile().getCodice() : "",
				elencoDocumentiAllegato.getNumero(), progressivo);
	}
	
	

	@Override
	protected Accertamento caricaDatiAccertamento(SubdocumentoEntrata se) {
		Accertamento accertamento = accertamentoBilDad.findMiniminalAccertamentoDataByUid(se.getAccertamento().getUid());
		return accertamento;
	}

	@Override
	protected Impegno caricaDatiImpegno(SubdocumentoSpesa ss) {
		Impegno impegno = impegnoBilDad.findMiniminalImpegnoDataByUid(ss.getImpegno().getUid());
		return impegno;
	}
	
}

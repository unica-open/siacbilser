/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentoentrata;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.integration.dad.AccountDad;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;

/**
 * The Class DefiniscePreDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefiniscePreDocumentoEntrataService extends CheckedAccountBaseService<DefiniscePreDocumentoEntrata, DefiniscePreDocumentoEntrataResponse> {

	private static final String SEPARATOR = " - ";
	private static final String ND = "N.D.";

	@Autowired
	@Qualifier("inserisceDocumentoEntrataService")
	private InserisceDocumentoEntrataService inserisceDocumentoEntrataService;
	@Autowired
	private InserisceQuotaDocumentoEntrataService inserisceQuotaDocumentoEntrataService;
	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad;
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	
	@Autowired
	private CodificaDad codificaDad;
	@Autowired
	private AccountDad accountDad;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ITALY); 	
	private int n = 0;
	
	private TipoDocumento tipoDocumentoDSI;
	private CodiceBollo codiceBolloEsente;
	private Boolean flagConvalidaManuale;
	private Date now;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getPreDocumentiEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumenti spesa"));
		
		if(req.getRicercaSinteticaPreDocumentoEntrata() == null){
			checkCondition(!req.getPreDocumentiEntrata().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista predocumenti spesa"));
			for(PreDocumentoEntrata preDoc : req.getPreDocumentiEntrata()){
				checkEntita(preDoc, "predocumento");
			}
		}
		
		checkEntita(req.getBilancio(), "bilancio");
		checkCondition(req.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#init()
	 */
	@Override
	protected void init() {
		documentoEntrataDad.setEnte(ente);
		documentoEntrataDad.setLoginOperazione(loginOperazione);
		
		preDocumentoEntrataDad.setEnte(ente);
		preDocumentoEntrataDad.setLoginOperazione(loginOperazione);
		
		codificaDad.setEnte(ente);
		
		elencoDocumentiAllegatoDad.setEnte(ente);
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
		now = new Date();
	}
	
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public DefiniscePreDocumentoEntrataResponse executeService(DefiniscePreDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public DefiniscePreDocumentoEntrataResponse executeServiceTxRequiresNew(DefiniscePreDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		this.flagConvalidaManuale = ottieniDefaultFlagConvalidaManuale();
		
		// Ottengo i predoc
		List<PreDocumentoEntrata> preDocumentiEntrata = getPreDocumentiEntrata();
		
		// Reggruppamento di primo e secondo livello
		Map<String, List<PreDocumentoEntrata>> preDocumentiEntrataRaggruppatiPrimoLivello = raggruppaPrimoLivelloConStatoCompleto(preDocumentiEntrata);
		Map<String, Map<String, List<PreDocumentoEntrata>>> preDocumentiEntrataRaggruppatiPrimoESecondoLivello = raggruppaGruppiDiSecondoLivello(preDocumentiEntrataRaggruppatiPrimoLivello);
		// Inserisco i dati
		inserisciDocumenti(preDocumentiEntrataRaggruppatiPrimoESecondoLivello);
	}
	
	/**
	 * Inizializzazione del flag di convalida manuale
	 */
	private Boolean ottieniDefaultFlagConvalidaManuale() {
		final String methodName = "ottieniDefaultFlagConvalidaManuale";
		Ente ente = accountDad.findEnteAssocciatoAdAccount(req.getRichiedente().getAccount().getUid());
		
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
	 * Raggruppa gruppi di secondo livello.
	 *
	 * @param documentiSpesaRaggruppati the documenti spesa raggruppati
	 * @return the map
	 */
	private Map<String, Map<String, List<PreDocumentoEntrata>>> raggruppaGruppiDiSecondoLivello(Map<String, List<PreDocumentoEntrata>> documentiSpesaRaggruppati) {
		
		Map<String, Map<String, List<PreDocumentoEntrata>>> result = new HashMap<String, Map<String, List<PreDocumentoEntrata>>>();
		
		for(Entry<String, List<PreDocumentoEntrata>> entry : documentiSpesaRaggruppati.entrySet()){
			String key = entry.getKey();
			List<PreDocumentoEntrata> listaDoc = entry.getValue();
			
			Map<String,List<PreDocumentoEntrata>> preDocumentiSecondoLivello = raggruppaSecondoLivello(listaDoc);
			
			result.put(key, preDocumentiSecondoLivello);
		}
		return result;
	}
	
	/**
	 * Raggruppa secondo livello.
	 *
	 * @param preDocumentiSpesa the pre documenti spesa
	 * @return the map
	 */
	private Map<String, List<PreDocumentoEntrata>> raggruppaSecondoLivello(List<PreDocumentoEntrata> preDocumentiSpesa) {
		
		Map<String, List<PreDocumentoEntrata>> result = new HashMap<String, List<PreDocumentoEntrata>>();
		
		for(PreDocumentoEntrata preDoc : preDocumentiSpesa) {
			String key = getSubDocumentoKey(preDoc);
			
			if(!result.containsKey(key)) {
				result.put(key, new ArrayList<PreDocumentoEntrata>());
			}
			
			List<PreDocumentoEntrata> list = result.get(key);
			list.add(preDoc);
		}
		return result;
	}
	
	
	/**
	 * Gets the sub documento key.
	 *
	 * @param preDoc the pre doc
	 * @return the sub documento key
	 */
	private String getSubDocumentoKey(PreDocumentoEntrata preDoc) {
		return new StringBuilder()
				.append(preDoc.getCapitoloEntrataGestione() != null ? preDoc.getCapitoloEntrataGestione().getUid() : "null").append(" ")
				.append(preDoc.getAccertamento() != null ? preDoc.getAccertamento().getUid() : "null").append(" ")
				.append(preDoc.getSubAccertamento() != null ? preDoc.getSubAccertamento().getUid() : "null").append(" ")
				.append(preDoc.getProvvisorioDiCassa() != null ? preDoc.getProvvisorioDiCassa().getUid() : "null").append(" ")
				.toString();
	}
	
	/**
	 * Inserisci documenti.
	 *
	 * @param preDocumentiSpesaRaggruppatiPrimoESecondoLivello the pre documenti spesa raggruppati primo e secondo livello
	 */
	private void inserisciDocumenti(Map<String, Map<String, List<PreDocumentoEntrata>>> preDocumentiSpesaRaggruppatiPrimoESecondoLivello) {
		final String methodName = "inserisciDocumenti";
		n = 0;
		for(Map<String, List<PreDocumentoEntrata>> preDocumentiPrimoLivello: preDocumentiSpesaRaggruppatiPrimoESecondoLivello.values()){
			n++;
			DocumentoEntrata doc = popolaDocumento(preDocumentiPrimoLivello);
			
			try{
				inserisciDocumentoEntrata(doc);
				inserisciSubDocumenti(doc, preDocumentiPrimoLivello);
				
			}catch(ExecuteExternalServiceException eese) {
				log.error(methodName, "non sono riuscito a inserire il documento: " + eese.getMessage(), eese);
				res.addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile inserire il documento:" + doc.getNumero() + ": " + eese.getMessage()));
			}
		}
	}

	/**
	 * Inserisci subdocumenti.
	 *
	 * @param doc il documento inserito
	 * @param preDocumentiPrimoLivello ti predocumenti di primo livello
	 */
	private void inserisciSubDocumenti(DocumentoEntrata doc, Map<String, List<PreDocumentoEntrata>> preDocumentiPrimoLivello) {
		final String methodName = "inserisciSubDocumenti";
		for(List<PreDocumentoEntrata> preDocs : preDocumentiPrimoLivello.values()){
			
			SubdocumentoEntrata subDoc = popolaSubDocumento(preDocs, doc);
			try {
				inserisciSubdocumentoEntrata(subDoc);
				collegaAdElenco(preDocs, subDoc);
			} catch(ExecuteExternalServiceException eese) {
				log.error(methodName, "non sono riuscito a inserire la quota: " + eese.getMessage(), eese);
				res.addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile inserire subdocumento del documento uid: " + doc.getUid() + "]: " + eese.getMessage()));
			}
			
			for(PreDocumentoEntrata preDoc : preDocs){
				preDocumentoEntrataDad.definisciPreDocumento(preDoc.getUid(), subDoc.getUid());
			}
		}
	}

	/**
	 * Collegamento delle quote all'elenco del predoc
	 * @param preDocs i predoc
	 * @param subDoc il subdoc
	 */
	private void collegaAdElenco(List<PreDocumentoEntrata> preDocs, SubdocumentoEntrata subDoc) {
		final String methodName = "collegaAdElenco";
		ElencoDocumentiAllegato eda = null;
		for(PreDocumentoEntrata pde : preDocs) {
			if(pde.getElencoDocumentiAllegato() != null && pde.getElencoDocumentiAllegato().getUid() != 0) {
				eda = pde.getElencoDocumentiAllegato();
			}
		}
		// Controllo di avere l'elenco
		if(eda == null) {
			log.debug(methodName, "Subdocumento non da collegare ad un elenco");
			return;
		}
		log.info(methodName, "Collegamento subdocumento " + subDoc.getUid() + " all'elenco " + eda.getUid());
		elencoDocumentiAllegatoDad.collegaElencoSubdocumento(eda, subDoc);
	}

	/**
	 * Inserisci subdocumento entrata.
	 *
	 * @param subDoc the sub doc
	 */
	private void inserisciSubdocumentoEntrata(SubdocumentoEntrata subDoc) {
		InserisceQuotaDocumentoEntrata reqIQDS = new InserisceQuotaDocumentoEntrata();
		reqIQDS.setRichiedente(req.getRichiedente());
		reqIQDS.setSubdocumentoEntrata(subDoc);
		reqIQDS.setBilancio(req.getBilancio());
		reqIQDS.setSaltaControlloDisponibilitaAccertamento(true);
		
		InserisceQuotaDocumentoEntrataResponse resIQDE = inserisceQuotaDocumentoEntrataService.executeServiceTxRequiresNew(reqIQDS);
		checkServiceResponseFallimento(resIQDE);
		
		subDoc.setUid(resIQDE.getSubdocumentoEntrata().getUid());
	}

	/**
	 * Popola sub documento.
	 *
	 * @param preDocs the pre docs
	 * @param doc the doc
	 * @return the subdocumento entrata
	 */
	private SubdocumentoEntrata popolaSubDocumento(List<PreDocumentoEntrata> preDocs, DocumentoEntrata doc) {
		SubdocumentoEntrata subDoc = new SubdocumentoEntrata();
		
		PreDocumentoEntrata preDoc = preDocs.get(0);
		
		subDoc.setDescrizione(doc.getDescrizione());
		BigDecimal totaleImporto = calcolaTotaleImportoPreDocumenti(preDocs);
		subDoc.setImporto(totaleImporto);
		
		subDoc.setAccertamento(preDoc.getAccertamento());
		subDoc.setSubAccertamento(preDoc.getSubAccertamento());
		
//		subDoc.setCausaleOrdinativo(preDoc.getCausaleEntrata().getCodice() + " "+ preDoc.getCausaleEntrata().getDescrizione());
		
//		subDoc.setAcceeDoc.getAccertamento());
//		subDoc.setSubImpegno(preDoc.getSubImpegno());
		
//		subDoc.setModalitaPagamentoSoggetto(preDoc.getModalitaPagamentoSoggetto());
//		subDoc.setSedeSecondariaSoggetto(preDoc.getSedeSecondariaSoggetto());
		
		subDoc.setAttoAmministrativo(preDoc.getAttoAmministrativo());
		
//		subDoc.setCommissioniDocumento(CommissioniDocumento.BENEFICIARIO);
		
		subDoc.setProvvisorioCassa(preDoc.getProvvisorioDiCassa());
		subDoc.setEnte(ente);
		subDoc.setFlagConvalidaManuale(this.flagConvalidaManuale);
		
		subDoc.setDocumento(doc);
		return subDoc;
	}

	/**
	 * Inserisci documento entrata.
	 *
	 * @param doc the doc
	 */
	private void inserisciDocumentoEntrata(DocumentoEntrata doc) {
		InserisceDocumentoEntrata reqIDS = new InserisceDocumentoEntrata();
		reqIDS.setRichiedente(req.getRichiedente());
		reqIDS.setDocumentoEntrata(doc);
		reqIDS.setInserisciQuotaContestuale(false);
		reqIDS.setBilancio(req.getBilancio());
		
		InserisceDocumentoEntrataResponse resIDS = inserisceDocumentoEntrataService.executeServiceTxRequiresNew(reqIDS);
		checkServiceResponseFallimento(resIDS);
		
		Integer uidDoc = resIDS.getDocumentoEntrata().getUid();
		
		doc.setUid(uidDoc);
	}

	/**
	 * Popola documento.
	 *
	 * @param preDocumenti the pre documenti
	 * @return the documento entrata
	 */
	private DocumentoEntrata popolaDocumento(Map<String, List<PreDocumentoEntrata>> preDocumenti   /*List<PreDocumentoEntrata> preDocumenti*/) {
		
		PreDocumentoEntrata preDoc = null;
		for(List<PreDocumentoEntrata> preDocsSecondoLivello : preDocumenti.values()){
			preDoc = preDocsSecondoLivello.get(0);
			break;
		}
		
		if(preDoc == null){
			//Non dovrebbe verificarsi mai.
			throw new IllegalStateException("Non riesco a reperire il primo predocumento nel raggruppamento.");
		}
		
		DocumentoEntrata doc = new DocumentoEntrata();

		doc.setAnno(req.getBilancio().getAnno());
		doc.setNumero(calcolaNumeroDocumento(preDoc));
		doc.setDescrizione(calcolaDescrizioneDocumento(preDoc));
		doc.setDataEmissione(now);
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		doc.setEnte(ente);
		
		doc.setTipoDocumento(getTipoDocumentoDSI());
		doc.setCodiceBollo(getCodiceBolloEsente());
		doc.setSoggetto(preDoc.getSoggetto());
		
		//TODO doc.setCommissioniDocumento?????? è nella quota!! subDoc.setCommissioniDocumento(tipoCommissione);
		
		BigDecimal totaleImportoPreDocumenti = calcolaTotaleImportoPreDocumenti(preDocumenti);		
		doc.setImporto(totaleImportoPreDocumenti);
		
		return doc;
	}
	
	/**
	 * Calcolo del numero del documento
	 * @param preDoc il predoc
	 * @return il numero del documento
	 */
	private String calcolaNumeroDocumento(PreDocumentoEntrata preDoc) {
		return new StringBuilder()
				.append(preDoc.getCausaleEntrata() != null ? preDoc.getCausaleEntrata().getCodice() : ND).append(SEPARATOR)
				.append(preDoc.getContoCorrente() != null ? preDoc.getContoCorrente().getCodice() : ND).append(SEPARATOR)
				.append(preDoc.getPeriodoCompetenza() != null ? preDoc.getPeriodoCompetenza() : ND).append(SEPARATOR)
				.append(preDoc.getSoggetto() != null ? preDoc.getSoggetto().getCodiceSoggetto() : ND).append(SEPARATOR)
				.append(sdf.format(now)).append(SEPARATOR)
				.append(n)
				.toString();
	}

	/**
	 * Calcolo della descrizione del documento
	 * @param preDoc il predoc
	 * @return la descrizione del documento
	 */
	private String calcolaDescrizioneDocumento(PreDocumentoEntrata preDoc) {
		return new StringBuilder()
				.append(preDoc.getCausaleEntrata() != null ? preDoc.getCausaleEntrata().getDescrizione() : ND).append(SEPARATOR)
				.append(preDoc.getContoCorrente() != null ? preDoc.getContoCorrente().getDescrizione() : ND).append(SEPARATOR)
				.append(preDoc.getPeriodoCompetenza() != null ? preDoc.getPeriodoCompetenza() : ND).append(SEPARATOR)
				.append(preDoc.getSoggetto() != null ? preDoc.getSoggetto().getCodiceSoggetto() : ND).append(SEPARATOR)
				.append(sdf.format(now))
				.toString();
	}
	
	/**
	 * Ottiene il tipo di documento con codice DSI
	 * @return il tipo documento DSI
	 */
	private TipoDocumento getTipoDocumentoDSI() {
		if(tipoDocumentoDSI == null) {
			tipoDocumentoDSI = codificaDad.ricercaCodifica(TipoDocumento.class, "DSI");
		}
		return tipoDocumentoDSI;
	}
	
	/**
	 * Ottiene codice bollo corrispondente all'esente
	 * @return il codice bollo esente
	 */
	private CodiceBollo getCodiceBolloEsente() {
		if(codiceBolloEsente == null) {
			codiceBolloEsente = documentoEntrataDad.findCodiceBolloEsente();
		}
		return codiceBolloEsente;
	}

	/**
	 * Calcola totale importo pre documenti.
	 *
	 * @param preDocs the pre docs
	 * @return the big decimal
	 */
	private BigDecimal calcolaTotaleImportoPreDocumenti(List<PreDocumentoEntrata> preDocs) {
		BigDecimal result = BigDecimal.ZERO;
		for(PreDocumentoEntrata preDoc : preDocs){
			result = result.add(preDoc.getImportoNotNull());
		}
		return result;
	}

	/**
	 * Calcola totale importo pre documenti.
	 *
	 * @param preDocumenti the pre documenti
	 * @return the big decimal
	 */
	private BigDecimal calcolaTotaleImportoPreDocumenti(Map<String, List<PreDocumentoEntrata>> preDocumenti) {
		BigDecimal result = BigDecimal.ZERO;
		for(List<PreDocumentoEntrata> preDocsSecondoLivello : preDocumenti.values()){
			for(PreDocumentoEntrata preDoc : preDocsSecondoLivello){
				result = result.add(preDoc.getImportoNotNull());
			}
		}
		
		return result;
	}

	/**
	 * Raggruppa primo livello con stato completo.
	 *
	 * @param preDocumentiEntrata the pre documenti entrata
	 * @return the map
	 */
	private Map<String, List<PreDocumentoEntrata>> raggruppaPrimoLivelloConStatoCompleto(List<PreDocumentoEntrata> preDocumentiEntrata) {
		String methodName = "raggruppaPrimoLivelloConStatoCompleto";
		
		Map<String, List<PreDocumentoEntrata>> result = new HashMap<String, List<PreDocumentoEntrata>>();
		
		for(PreDocumentoEntrata preDoc : preDocumentiEntrata) {
			
			// SIAC-6090: se richiesto, evito di ricaricare il dettaglio del predoc
			if(!req.isSkipCaricamentoDettaglioPredocumento()) {
				preDoc = getDettaglioPreDocumentoEntrata(preDoc);
			}
			
			String key = getDocumentoKey(preDoc);
			
			if(!isCompleto(preDoc)) {
				log.info(methodName, "Saltato perche' non completo: "+key);
				res.getPredocumentiSaltati().add(preDoc);
				continue;
			}
			
			res.getPredocumentiElaborati().add(preDoc);
			
			if(!result.containsKey(key)) {
				result.put(key, new ArrayList<PreDocumentoEntrata>());
				log.debug(methodName, "Nuovo gruppo di primo livello: "+key);
			}
			
			List<PreDocumentoEntrata> list = result.get(key);
			list.add(preDoc);
			
		}
		return result;
	}

	/**
	 * Gets the documento key.
	 *
	 * @param preDoc the pre doc
	 * @return the documento key
	 */
	private String getDocumentoKey(PreDocumentoEntrata preDoc) {
		return new StringBuilder()
				.append(preDoc.getStrutturaAmministrativoContabile() != null ? preDoc.getStrutturaAmministrativoContabile().getUid() : "null").append(" ")
				.append(preDoc.getCausaleEntrata() != null ? preDoc.getCausaleEntrata().getUid() : "null").append(" ")
//				.append(preDoc.getContoTesoreria() != null ? preDoc.getContoTesoreria().getUid() : "null").append(" ")
				.append(preDoc.getPeriodoCompetenza() != null ? preDoc.getPeriodoCompetenza() : "null").append(" ")
				.append(preDoc.getSoggetto() != null ? preDoc.getSoggetto().getUid() : "null")
				.toString();
	}
	
	/**
	 * Checks if is completo.
	 *
	 * @param preDoc the pre doc
	 * @return true, if is completo
	 */
	private boolean isCompleto(PreDocumentoEntrata preDoc) {
		return StatoOperativoPreDocumento.COMPLETO.equals(preDoc.getStatoOperativoPreDocumento());
	}

	/**
	 * Gets the pre documenti entrata.
	 *
	 * @return the pre documenti entrata
	 */
	private List<PreDocumentoEntrata> getPreDocumentiEntrata() {
		if(req.getRicercaSinteticaPreDocumentoEntrata()!=null){
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
		ListaPaginata<PreDocumentoEntrata> result =  ricercaSinteticaPreDocumentoEntrata(0);
		
		for(int i = 1; i < result.getTotalePagine(); i++) {
			List<PreDocumentoEntrata> temp = ricercaSinteticaPreDocumentoEntrata(i);
			result.addAll(temp);
		}
		return result;
	}
	
	/**
	 * Ricerca sintetica pre documento entrata.
	 *
	 * @param numeroPagina the numero pagina
	 * @return i predoc di entrata
	 */
	private ListaPaginata<PreDocumentoEntrata> ricercaSinteticaPreDocumentoEntrata(int numeroPagina){
		return preDocumentoEntrataDad.ricercaSinteticaPreDocumento(
				req.getRicercaSinteticaPreDocumentoEntrata().getPreDocumentoEntrata(),
				req.getRicercaSinteticaPreDocumentoEntrata().getTipoCausale(),
				req.getRicercaSinteticaPreDocumentoEntrata().getDataCompetenzaDa(),
				req.getRicercaSinteticaPreDocumentoEntrata().getDataCompetenzaA(),
				req.getRicercaSinteticaPreDocumentoEntrata().getDataTrasmissioneDa(),
				req.getRicercaSinteticaPreDocumentoEntrata().getDataTrasmissioneA(),
				req.getRicercaSinteticaPreDocumentoEntrata().getCausaleEntrataMancante(),
				req.getRicercaSinteticaPreDocumentoEntrata().getSoggettoMancante(),
				req.getRicercaSinteticaPreDocumentoEntrata().getProvvedimentoMancante(),
				req.getRicercaSinteticaPreDocumentoEntrata().getContoCorrenteMancante(),
				req.getRicercaSinteticaPreDocumentoEntrata().getNonAnnullati(),
				req.getRicercaSinteticaPreDocumentoEntrata().getOrdinativoIncasso(),
				req.getRicercaSinteticaPreDocumentoEntrata().getOrdinamentoPreDocumentoEntrata(),
				new ParametriPaginazione(numeroPagina, 100));
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

}

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
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.integration.dad.AccountDad;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StatoOperazioneAsincronaEnum;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class DefiniscePreDocumentoDiEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Deprecated
public class DefiniscePreDocumentoDiEntrataService extends BaseServiceOneWay<DefiniscePreDocumentoDiEntrata> {
	

	/** The ricerca sintetica pre documento entrata. */
	@Autowired
	private RicercaSinteticaPreDocumentoEntrataService ricercaSinteticaPreDocumentoEntrata;
	
	/** The ricerca dettaglio pre documento entrata service. */
	@Autowired
	private RicercaDettaglioPreDocumentoEntrataService ricercaDettaglioPreDocumentoEntrataService;
	
	/** The inserisce documento entrata service. */
	@Autowired
	@Qualifier("inserisceDocumentoEntrataService")
	private InserisceDocumentoEntrataService inserisceDocumentoEntrataService;

	/** The inserisce quota documento entrata service. */
	@Autowired
	private InserisceQuotaDocumentoEntrataService inserisceQuotaDocumentoEntrataService;
	
	/** The pre documento entrata dad. */
	@Autowired
	private PreDocumentoEntrataDad preDocumentoEntrataDad;
	
	/** The documento entrata dad. */
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	
	/** The sdf. */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ITALY); 	
	
	@Autowired
	private CodificaDad codificaDad;
	@Autowired
	private AccountDad accountDad;
	

	/** The n. */
	private int n = 0;
	private Boolean flagConvalidaManuale;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getPreDocumentiEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumenti spesa"));
		
		if(req.getRicercaSinteticaPreDocumentoEntrata()==null){
			checkCondition(!req.getPreDocumentiEntrata().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista predocumenti spesa"));
			
			for(PreDocumentoEntrata preDoc : req.getPreDocumentiEntrata()){
				checkNotNull(preDoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumento"));
				checkCondition(preDoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid predocumento"));
			}
		}
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		checkCondition(req.getBilancio().getAnno()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
		
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#init()
	 */
	@Override
	protected void init() {
		documentoEntrataDad.setEnte(req.getEnte());
		documentoEntrataDad.setLoginOperazione(req.getRichiedente().getAccount().getDescrizione());
		
		preDocumentoEntrataDad.setEnte(req.getEnte());
		preDocumentoEntrataDad.setLoginOperazione(req.getRichiedente().getAccount().getDescrizione());
		
		codificaDad.setEnte(req.getEnte());
		elencoDocumentiAllegatoDad.setEnte(req.getEnte());
		elencoDocumentiAllegatoDad.setLoginOperazione(req.getRichiedente().getAccount().getDescrizione());
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#executeService(it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequest)
	 */
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public void executeService(DefiniscePreDocumentoDiEntrata serviceRequest) {
		super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#execute()
	 */
	@Override
	protected void execute() {
		
		this.flagConvalidaManuale = ottieniDefaultFlagConvalidaManuale();
		
		List<PreDocumentoEntrata> preDocumentiEntrata = getPreDocumentiEntrata();
		
		Map<String,List<PreDocumentoEntrata>> preDocumentiEntrataRaggruppatiPrimoLivello = raggruppaPrimoLivelloConStatoCompleto(preDocumentiEntrata);	
		Map<String,Map<String,List<PreDocumentoEntrata>>> preDocumentiEntrataRaggruppatiPrimoESecondoLivello = raggruppaGruppiDiSecondoLivello(preDocumentiEntrataRaggruppatiPrimoLivello);		
		
				
		inserisciDocumenti(preDocumentiEntrataRaggruppatiPrimoESecondoLivello);
		
		
		statoFinaleOperazione = StatoOperazioneAsincronaEnum.STATO_OPASINC_CONCLUSA; //Di default è già impostato a STATO_OPASINC_CONCLUSA quindi si può omettere.

		
		
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
	private Map<String,Map<String,List<PreDocumentoEntrata>>> raggruppaGruppiDiSecondoLivello(Map<String, List<PreDocumentoEntrata>> documentiSpesaRaggruppati) {
		
		Map<String,Map<String,List<PreDocumentoEntrata>>> result = new HashMap<String, Map<String,List<PreDocumentoEntrata>>>();
		
		
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
		
		return (preDoc.getCapitoloEntrataGestione()!=null?preDoc.getCapitoloEntrataGestione().getUid() :"null") + " " 
				+ (preDoc.getAccertamento()!=null?preDoc.getAccertamento().getUid():"null") + " " 
				+ (preDoc.getSubAccertamento()!=null?preDoc.getSubAccertamento().getUid():"null") + " "
				+ (preDoc.getProvvisorioDiCassa() !=null?preDoc.getProvvisorioDiCassa().getUid() :"null") + " ";
	}
	



	
	
	/**
	 * Inserisci documenti.
	 *
	 * @param preDocumentiSpesaRaggruppatiPrimoESecondoLivello the pre documenti spesa raggruppati primo e secondo livello
	 */
	private void inserisciDocumenti(Map<String, Map<String, List<PreDocumentoEntrata>>> preDocumentiSpesaRaggruppatiPrimoESecondoLivello) {
		final String methodName = "inserisciDocumenti";
		n=0;
		for(Map<String, List<PreDocumentoEntrata>> preDocumentiPrimoLivello: preDocumentiSpesaRaggruppatiPrimoESecondoLivello.values()){
			n++;	
			DocumentoEntrata doc = popolaDocumento(preDocumentiPrimoLivello);
			
			try{
				inserisciDocumentoEntrata(doc);	
				inserisciSubDocumenti(doc, preDocumentiPrimoLivello);
				
			}catch(ExecuteExternalServiceException eese) {
				log.error(methodName, "non sono riuscito a inserire il documento: " + eese.getMessage(), eese);
				inserisciDettaglioOperazioneAsinc("DOCUMENTO INSERITO", " impossibile inserire il documento:" + doc.getNumero() , Esito.FALLIMENTO, eese.getMessage());
			}
			
		}
		
		
	}




	private void inserisciSubDocumenti(DocumentoEntrata doc, Map<String, List<PreDocumentoEntrata>> preDocumentiPrimoLivello) {
		final String methodName = "inserisciSubDocumenti";
		for(List<PreDocumentoEntrata> preDocs : preDocumentiPrimoLivello.values()){						
			
			SubdocumentoEntrata subDoc = popolaSubDocumento(preDocs, doc);				
			try {
				inserisciSubdocumentoEntrata(subDoc);
				collegaAdElenco(preDocs, subDoc);
			} catch(ExecuteExternalServiceException eese) {
				log.error(methodName, "non sono riuscito a inserire la quota: " + eese.getMessage(), eese);
				inserisciDettaglioOperazioneAsinc("QUOTA NON INSERITA", "impossibile inserire subdocumento del documento uid: "+doc.getUid()+"] " , Esito.FALLIMENTO, eese.getMessage());
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
			log.debug(methodName, "Subdocumenti non da collegare ad un elenco");
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
		InserisceQuotaDocumentoEntrataResponse resIQDE = inserisceQuotaDocumentoEntrataService.executeServiceTxRequiresNew(reqIQDS);
		checkServiceResponseFallimento(resIQDE);
//		InserisceQuotaDocumentoEntrataResponse resIQDE = executeExternalServiceSuccess(inserisceQuotaDocumentoEntrataService, reqIQDS);
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
		
		subDoc.setAttoAmministrativo(preDoc.getAttoAmministrativo()	);
		
//		subDoc.setCommissioniDocumento(CommissioniDocumento.BENEFICIARIO);
		
		subDoc.setProvvisorioCassa(preDoc.getProvvisorioDiCassa());
		subDoc.setEnte(req.getEnte());
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
		
//		InserisceDocumentoEntrataResponse resIDS = executeExternalServiceSuccess(inserisceDocumentoEntrataService, reqIDS);
		
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
		
		if(preDoc==null){
			//Non dovrebbe verificarsi mai.
			throw new IllegalStateException("Non riesco a reperire il primo predocumento nel raggruppamento.");
		}
		
		
		DocumentoEntrata doc = new DocumentoEntrata();	

		doc.setAnno(req.getBilancio().getAnno());
		
		Date now = new Date();
		
		String separator = " - ";
		String nd = "N.D.";
		
		doc.setNumero((preDoc.getCausaleEntrata()!=null?preDoc.getCausaleEntrata().getCodice(): nd)  + separator
				+ (preDoc.getContoCorrente()!=null?preDoc.getContoCorrente().getCodice(): nd )+ separator
				+ (preDoc.getPeriodoCompetenza()!=null?preDoc.getPeriodoCompetenza(): nd) + separator
				+ (preDoc.getSoggetto()!=null?preDoc.getSoggetto().getCodiceSoggetto(): nd) + separator
				+ sdf.format(now) + separator
				+ n
				);
		
		
		doc.setDescrizione((preDoc.getCausaleEntrata()!=null?preDoc.getCausaleEntrata().getDescrizione() :nd) + separator
				+ (preDoc.getContoCorrente()!=null?preDoc.getContoCorrente().getDescrizione(): nd )+ separator
				+ (preDoc.getPeriodoCompetenza()!=null?preDoc.getPeriodoCompetenza(): nd) + separator
				+ (preDoc.getSoggetto()!=null?preDoc.getSoggetto().getCodiceSoggetto(): nd) + separator
				+ sdf.format(now)
				);
		
		doc.setDataEmissione(now);
		
		TipoDocumento tipoDocumento = codificaDad.ricercaCodifica(TipoDocumento.class, "DSI");
		doc.setTipoDocumento(tipoDocumento);
		
		
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		
		doc.setSoggetto(preDoc.getSoggetto());
		
		//TODO doc.setCommissioniDocumento?????? è nella quota!! subDoc.setCommissioniDocumento(tipoCommissione);
		
		BigDecimal totaleImportoPreDocumenti = calcolaTotaleImportoPreDocumenti(preDocumenti);		
		doc.setImporto(totaleImportoPreDocumenti);
		
		CodiceBollo codiceBollo  = documentoEntrataDad.findCodiceBolloEsente();
		doc.setCodiceBollo(codiceBollo);
		
		doc.setEnte(req.getEnte());
		
		return doc;
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
			
			preDoc = getDettaglioPreDocumentoEntrata(preDoc);
			
			String key = getDocumentoKey(preDoc);
						
			if(!isCompleto(preDoc)) {	
				log.info(methodName, "Saltato perche' non completo: "+key);
				inserisciDettaglioOperazioneAsinc("SALTATO", " predocumento: " + preDoc.getNumero() + " ["+preDoc.getUid()+"] - " + preDoc.getDescrizione(), Esito.FALLIMENTO, "Non completo");
				continue;
			}
			
			inserisciDettaglioOperazioneAsinc("AGGIORNATO", " predocumento: " + preDoc.getNumero() + " ["+preDoc.getUid()+"] - " + preDoc.getDescrizione(), Esito.SUCCESSO);
			
			
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
		
		return (preDoc.getStrutturaAmministrativoContabile()!=null?preDoc.getStrutturaAmministrativoContabile().getUid() :"null") + " " 
				+ (preDoc.getCausaleEntrata()!=null?preDoc.getCausaleEntrata().getUid():"null") + " " 
				//+ (preDoc.getContoTesoreria()!=null?preDoc.getContoTesoreria().getUid():"null") + " " 
				+ (preDoc.getPeriodoCompetenza()!=null?preDoc.getPeriodoCompetenza():"null") + " "
				+ (preDoc.getSoggetto()!=null?preDoc.getSoggetto().getUid():"null");
	}
	
	
//	private String getSubDocumentoKey(PreDocumentoEntrata preDoc) {
//		
//		return (preDoc.getCapitoloUscitaGestione()!=null?preDoc.getCapitoloUscitaGestione().getUid() :"null") + " " 
//				+ (preDoc.getImpegno()!=null?preDoc.getImpegno().getUid():"null") + " " 
//				+ (preDoc.getSubImpegno()!=null?preDoc.getSubImpegno().getUid():"null") + " ";
//	}

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
		RicercaSinteticaPreDocumentoEntrataResponse resRSPD = ricercaSinteticaPreDocumentoEntrata(0);		
		List<PreDocumentoEntrata> result =  resRSPD.getPreDocumenti();
		
		for(int i = 1; i < resRSPD.getTotalePagine(); i++) {			
			resRSPD = ricercaSinteticaPreDocumentoEntrata(i);
			result.addAll(resRSPD.getPreDocumenti());			
		}
		return result;
	}
	
	/**
	 * Ricerca sintetica pre documento entrata.
	 *
	 * @param numeroPagina the numero pagina
	 * @return the ricerca sintetica pre documento entrata response
	 */
	private RicercaSinteticaPreDocumentoEntrataResponse ricercaSinteticaPreDocumentoEntrata(int numeroPagina){
		RicercaSinteticaPreDocumentoEntrata reqRSPD = req.getRicercaSinteticaPreDocumentoEntrata();		
		ParametriPaginazione pp = new ParametriPaginazione(numeroPagina);
		pp.setElementiPerPagina(100);
		reqRSPD.setParametriPaginazione(pp);
		reqRSPD.setRichiedente(req.getRichiedente());		
		RicercaSinteticaPreDocumentoEntrataResponse resRSPD = executeExternalService(ricercaSinteticaPreDocumentoEntrata, reqRSPD);
		return resRSPD;
	}
	
	
	
	/**
	 * Carica il dettaglio del predocumento.
	 *
	 * @param preDoc the pre doc
	 * @return the dettaglio pre documento entrata
	 */
	protected PreDocumentoEntrata getDettaglioPreDocumentoEntrata(PreDocumentoEntrata preDoc) {
		RicercaDettaglioPreDocumentoEntrata req = new RicercaDettaglioPreDocumentoEntrata();
		req.setDataOra(new Date());
		req.setPreDocumentoEntrata(preDoc);
		req.setRichiedente(this.req.getRichiedente());
		RicercaDettaglioPreDocumentoEntrataResponse res = executeExternalServiceSuccess(ricercaDettaglioPreDocumentoEntrataService,req);
		return res.getPreDocumentoEntrata();
	}

}

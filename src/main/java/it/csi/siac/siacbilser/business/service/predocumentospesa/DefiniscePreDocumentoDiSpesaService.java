/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.predocumentospesa;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.StatoOperazioneAsincronaEnum;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;


/**
 * The Class DefiniscePreDocumentoDiSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Deprecated
public class DefiniscePreDocumentoDiSpesaService extends BaseServiceOneWay<DefiniscePreDocumentoDiSpesa> {

	/** The ricerca sintetica pre documento spesa. */
	@Autowired
	private RicercaSinteticaPreDocumentoSpesaService ricercaSinteticaPreDocumentoSpesa;
	
	/** The ricerca dettaglio pre documento spesa service. */
	@Autowired
	private RicercaDettaglioPreDocumentoSpesaService ricercaDettaglioPreDocumentoSpesaService;
	
	/** The inserisce documento spesa service. */
	@Autowired
	@Qualifier("inserisceDocumentoSpesaService")
	private InserisceDocumentoSpesaService inserisceDocumentoSpesaService;

	/** The inserisce quota documento spesa service. */
	@Autowired
	private InserisceQuotaDocumentoSpesaService inserisceQuotaDocumentoSpesaService;
	
	/** The pre documento spesa dad. */
	@Autowired
	private PreDocumentoSpesaDad preDocumentoSpesaDad;
	
	/** The documento spesa dad. */
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	
	@Autowired
	private CodificaDad codificaDad;
	
	/** The sdf. */
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ITALY); 	
	
	/** The n. */
	private int n = 0;
	private Boolean flagConvalidaManuale;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkNotNull(req.getPreDocumentiSpesa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumenti spesa"));
		
		if(req.getRicercaSinteticaPreDocumentoSpesa()==null){
			checkCondition(!req.getPreDocumentiSpesa().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista predocumenti spesa"));			
			for(PreDocumentoSpesa preDoc : req.getPreDocumentiSpesa()){
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
		documentoSpesaDad.setEnte(req.getEnte());
		documentoSpesaDad.setLoginOperazione(req.getRichiedente().getOperatore().getCodiceFiscale());
		
		preDocumentoSpesaDad.setEnte(req.getEnte());
		preDocumentoSpesaDad.setLoginOperazione(req.getRichiedente().getOperatore().getCodiceFiscale());
		
		codificaDad.setEnte(req.getEnte());
		elencoDocumentiAllegatoDad.setEnte(req.getEnte());
		elencoDocumentiAllegatoDad.setLoginOperazione(req.getRichiedente().getAccount().getDescrizione());
	}
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#executeService(it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequest)
	 */
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public void executeService(DefiniscePreDocumentoDiSpesa serviceRequest) {
		super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseServiceOneWay#execute()
	 */
	@Override
	protected void execute() {
		
		this.flagConvalidaManuale = ottieniDefaultFlagConvalidaManuale();
		
		List<PreDocumentoSpesa> preDocumentiSpesa = getPredocumentiSpesa();
		
		Map<String,List<PreDocumentoSpesa>> preDocumentiSpesaRaggruppatiPrimoLivello = raggruppaPrimoLivelloConStatoCompleto(preDocumentiSpesa);		
				
		inserisciDocumenti(preDocumentiSpesaRaggruppatiPrimoLivello);		
		
		
		statoFinaleOperazione = StatoOperazioneAsincronaEnum.STATO_OPASINC_CONCLUSA; //Di default è già impostato a STATO_OPASINC_CONCLUSA quindi si può omettere.

		
		
	}
	
	
	


	/**
	 * Inizializzazione del flag di convalida manuale
	 */
	private Boolean ottieniDefaultFlagConvalidaManuale() {
		// SIAC-4950: per le spese il flag convalida manuale non deve essere settato (restituisco null per forzare)
		return null;
	}

	/**
	 * Inserisci documenti.
	 *
	 * @param preDocumentiSpesaRaggruppatiPrimoLivello the pre documenti spesa raggruppati primo livello
	 */
	private void inserisciDocumenti(Map<String, List<PreDocumentoSpesa>> preDocumentiSpesaRaggruppatiPrimoLivello) {
		final String methodName = "inserisciDocumenti";
		n=0;
		for(List<PreDocumentoSpesa> preDocumentiPrimoLivello: preDocumentiSpesaRaggruppatiPrimoLivello.values()){
			n++;
			DocumentoSpesa doc = popolaDocumento(preDocumentiPrimoLivello);
			try {
				
				inserisciDocumentoSpesa(doc);
				inserisciSubDocumenti(doc, preDocumentiPrimoLivello);
			
			} catch(ExecuteExternalServiceException eese) {
				log.error(methodName, "non sono riuscito a inserire il documento.", eese);
				inserisciDettaglioOperazioneAsinc("DOCUMENTO INSERITO", " impossibile inserire il documento:" + doc.getNumero() , Esito.FALLIMENTO, eese.getMessage());
			}
					
		}
		
	}


	private void inserisciSubDocumenti(DocumentoSpesa doc, List<PreDocumentoSpesa> preDocumentiPrimoLivello) {
		
		final String methodName = "inserisciSubDocumenti";
		
		for(PreDocumentoSpesa preDoc : preDocumentiPrimoLivello){
			SubdocumentoSpesa subDoc = popolaSubDocumento(preDoc, doc);			
			try {
				inserisciSubDocumentoSpesa(subDoc);
				collegaAdElenco(preDoc, subDoc);
			} catch(ExecuteExternalServiceException eese) {
				log.error(methodName, "non sono riuscito a inserire la quota: ", eese);
				inserisciDettaglioOperazioneAsinc("QUOTA NON INSERITA", "impossibile inserire subdocumento del documento uid: "+doc.getUid()+"] " , Esito.FALLIMENTO, eese.getMessage());
			}
			preDocumentoSpesaDad.definisciPreDocumento(preDoc.getUid(), subDoc.getUid());
		}
	}
	
	/**
	 * Collegamento delle quote all'elenco del predoc
	 * @param preDocs i predoc
	 * @param subDoc il subdoc
	 */
	private void collegaAdElenco(PreDocumentoSpesa preDoc, SubdocumentoSpesa subDoc) {
		final String methodName = "collegaAdElenco";
		// Controllo di avere l'elenco
		if(preDoc.getElencoDocumentiAllegato() == null || preDoc.getElencoDocumentiAllegato().getUid() == 0) {
			log.debug(methodName, "Subdocumento non da collegare ad un elenco");
			return;
		}
		log.info(methodName, "Collegamento subdocumento " + subDoc.getUid() + " all'elenco " + preDoc.getElencoDocumentiAllegato().getUid());
		elencoDocumentiAllegatoDad.collegaElencoSubdocumento(preDoc.getElencoDocumentiAllegato(), subDoc);
	}

	/**
	 * Inserisci sub documento spesa.
	 *
	 * @param subDoc the sub doc
	 */
	private void inserisciSubDocumentoSpesa(SubdocumentoSpesa subDoc) {
		InserisceQuotaDocumentoSpesa reqIQDS = new InserisceQuotaDocumentoSpesa();
		reqIQDS.setRichiedente(req.getRichiedente());
		reqIQDS.setSubdocumentoSpesa(subDoc);
		reqIQDS.setBilancio(req.getBilancio());
		
//		InserisceQuotaDocumentoSpesaResponse resIQDS = executeExternalServiceSuccess(inserisceQuotaDocumentoSpesaService, reqIQDS);
		
		InserisceQuotaDocumentoSpesaResponse resIQDS = inserisceQuotaDocumentoSpesaService.executeServiceTxRequiresNew(reqIQDS);
		checkServiceResponseFallimento(resIQDS);
				
				
		subDoc.setUid(resIQDS.getSubdocumentoSpesa().getUid());
		
	}

	/**
	 * Popola sub documento.
	 *
	 * @param preDoc the pre doc
	 * @param doc the doc
	 * @return the subdocumento spesa
	 */
	private SubdocumentoSpesa popolaSubDocumento(PreDocumentoSpesa preDoc, DocumentoSpesa doc) {
		SubdocumentoSpesa subDoc = new SubdocumentoSpesa();
		
		
		subDoc.setDescrizione(doc.getDescrizione() + " - " + preDoc.getNumero() );
		subDoc.setImporto(preDoc.getImportoNotNull());
		
		subDoc.setCausaleOrdinativo(preDoc.getCausaleSpesa().getCodice() + " "+ preDoc.getCausaleSpesa().getDescrizione());
		
		subDoc.setImpegno(preDoc.getImpegno());
		subDoc.setSubImpegno(preDoc.getSubImpegno());
		
		if(preDoc.getModalitaPagamentoSoggetto() != null && preDoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2() != null 
				&& preDoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2().getUid() != 0){
			subDoc.setModalitaPagamentoSoggetto(preDoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2());
		}else{
			subDoc.setModalitaPagamentoSoggetto(preDoc.getModalitaPagamentoSoggetto());
		}
		
		subDoc.setSedeSecondariaSoggetto(preDoc.getSedeSecondariaSoggetto());
		
		//CR-4922
		//subDoc.setAttoAmministrativo(preDoc.getAttoAmministrativo()	);
		
//		subDoc.setCommissioniDocumento(CommissioniDocumento.BENEFICIARIO);
		subDoc.setProvvisorioCassa(preDoc.getProvvisorioDiCassa());
		

		subDoc.setDocumento(doc);
		
		subDoc.setEnte(req.getEnte());
		
		subDoc.setFlagConvalidaManuale(this.flagConvalidaManuale);
		// SIAC-5205
		subDoc.setCausaleOrdinativo(preDoc.getDescrizione());
		
		return subDoc;
	}

	/**
	 * Inserisci documento spesa.
	 *
	 * @param doc the doc
	 */
	private void inserisciDocumentoSpesa(DocumentoSpesa doc) {
		InserisceDocumentoSpesa reqIDS = new InserisceDocumentoSpesa();
		reqIDS.setRichiedente(req.getRichiedente());		
		reqIDS.setDocumentoSpesa(doc);
		reqIDS.setInserisciQuotaContestuale(false);
		reqIDS.setBilancio(req.getBilancio());
		
		
//		InserisceDocumentoSpesaResponse resIDS = executeExternalServiceSuccess(inserisceDocumentoSpesaService, reqIDS);
		InserisceDocumentoSpesaResponse resIDS = inserisceDocumentoSpesaService.executeServiceTxRequiresNew(reqIDS);
		checkServiceResponseFallimento(resIDS);
		
		Integer uidDoc = resIDS.getDocumentoSpesa().getUid();
		
		doc.setUid(uidDoc);
		
		
	}
	

	/**
	 * Popola documento.
	 *
	 * @param preDocumenti the pre documenti
	 * @return the documento spesa
	 */
	private DocumentoSpesa popolaDocumento(List<PreDocumentoSpesa> preDocumenti) {
		PreDocumentoSpesa preDoc = preDocumenti.get(0);
		
		DocumentoSpesa doc = new DocumentoSpesa();	

		doc.setAnno(req.getBilancio().getAnno());
		
		Date now = new Date();
		
		String separator = " - ";
		String nd = "N.D.";
		
		doc.setNumero((preDoc.getCausaleSpesa()!=null?preDoc.getCausaleSpesa().getCodice(): nd)  + separator
				+ (preDoc.getContoTesoreria()!=null?preDoc.getContoTesoreria().getCodice(): nd )+ separator
				+ (preDoc.getPeriodoCompetenza()!=null?preDoc.getPeriodoCompetenza(): nd) + separator
				+ (preDoc.getSoggetto()!=null?preDoc.getSoggetto().getCodiceSoggetto(): nd) + separator
				+ sdf.format(now) + separator
				+ n
				);
		
		
		doc.setDescrizione((preDoc.getCausaleSpesa()!=null?preDoc.getCausaleSpesa().getDescrizione() :nd) + separator
				+ (preDoc.getContoTesoreria()!=null?preDoc.getContoTesoreria().getDescrizione(): nd) + separator
				+ (preDoc.getPeriodoCompetenza()!=null?preDoc.getPeriodoCompetenza(): nd) + separator
				+ (preDoc.getSoggetto()!=null?preDoc.getSoggetto().getCodiceSoggetto(): nd) + separator
				+ sdf.format(now)
				);
		
		doc.setDataEmissione(now);
		
		TipoDocumento tipoDocumento = codificaDad.ricercaCodifica(TipoDocumento.class, "DSP");
		
		doc.setTipoDocumento(tipoDocumento);
		
		
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		
		doc.setSoggetto(preDoc.getSoggetto());
		
		//TODO doc.setCommissioniDocumento?????? è nella quota!! subDoc.setCommissioniDocumento(tipoCommissione);
		
		BigDecimal totaleImportoPreDocumenti = calcolaTotaleImportoPreDocumenti(preDocumenti);		
		doc.setImporto(totaleImportoPreDocumenti);
		
		CodiceBollo codiceBollo  = documentoSpesaDad.findCodiceBolloEsente();
		doc.setCodiceBollo(codiceBollo);
		
		// TODO: quando sara' richiesto (a posteriori della segnalazione SIAC-4680)
//		doc.setStrutturaAmministrativoContabile(preDoc.getStrutturaAmministrativoContabile());
		
		doc.setEnte(req.getEnte());
		
		return doc;
	}

	/**
	 * Calcola totale importo pre documenti.
	 *
	 * @param preDocumenti the pre documenti
	 * @return the big decimal
	 */
	private BigDecimal calcolaTotaleImportoPreDocumenti(List<PreDocumentoSpesa> preDocumenti) {
		BigDecimal result = BigDecimal.ZERO;
		for(PreDocumentoSpesa preDoc : preDocumenti){
			result = result.add(preDoc.getImportoNotNull());
		}
		return result;
	}

	/**
	 * Raggruppa primo livello con stato completo.
	 *
	 * @param preDocumentiSpesa the pre documenti spesa
	 * @return the map
	 */
	private Map<String, List<PreDocumentoSpesa>> raggruppaPrimoLivelloConStatoCompleto(List<PreDocumentoSpesa> preDocumentiSpesa) {
		String methodName = "raggruppaPrimoLivelloConStatoCompleto";
		
		Map<String, List<PreDocumentoSpesa>> result = new HashMap<String, List<PreDocumentoSpesa>>();
		
		for(PreDocumentoSpesa preDoc : preDocumentiSpesa) {
			
			preDoc = getDettaglioPreDocumentoSpesa(preDoc);
			
			String key = getDocumentoKey(preDoc);
						
			if(!isCompleto(preDoc)) {	
				log.info(methodName, "Saltato perche' non completo: "+key);
				inserisciDettaglioOperazioneAsinc("SALTATO", " predocumento:" + preDoc.getNumero() + " ["+preDoc.getUid()+"] - " + preDoc.getDescrizione(), Esito.FALLIMENTO, "Non completo");
				continue;
			}
			
			inserisciDettaglioOperazioneAsinc("AGGIORNATO", " predocumento:" + preDoc.getNumero() + " ["+preDoc.getUid()+"] - " + preDoc.getDescrizione(), Esito.SUCCESSO);
						
			
			if(!result.containsKey(key)) {
				result.put(key, new ArrayList<PreDocumentoSpesa>());				
				log.debug(methodName, "Nuovo gruppo di primo livello: "+key);
			}
			
			List<PreDocumentoSpesa> list = result.get(key);			
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
	private String getDocumentoKey(PreDocumentoSpesa preDoc) {
		
		return (preDoc.getStrutturaAmministrativoContabile()!=null?preDoc.getStrutturaAmministrativoContabile().getUid() :"null") + " " 
				+ (preDoc.getCausaleSpesa()!=null?preDoc.getCausaleSpesa().getUid():"null") + " " 
				+ (preDoc.getContoTesoreria()!=null?preDoc.getContoTesoreria().getUid():"null") + " " 
				+ (preDoc.getPeriodoCompetenza()!=null?preDoc.getPeriodoCompetenza():"null") + " "
				+ (preDoc.getSoggetto()!=null?preDoc.getSoggetto().getUid():"null");
		//+ (preDoc.getProvvisorioDiCassa() !=null?preDoc.getProvvisorioDiCassa().getUid() :"null") + " ";

	}
	
	
//	private String getSubDocumentoKey(PreDocumentoSpesa preDoc) {
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
private boolean isCompleto(PreDocumentoSpesa preDoc) {
		return StatoOperativoPreDocumento.COMPLETO.equals(preDoc.getStatoOperativoPreDocumento());
	}

	/**
	 * Gets the predocumenti spesa.
	 *
	 * @return the predocumenti spesa
	 */
	private List<PreDocumentoSpesa> getPredocumentiSpesa() {	
		if(req.getRicercaSinteticaPreDocumentoSpesa()!=null){
			return ricercaSinteticaPreDocumentoSpesa();
		}
		
		return req.getPreDocumentiSpesa();
		
		
	}

	/**
	 * Ricerca sintetica pre documento spesa.
	 *
	 * @return the list
	 */
	private List<PreDocumentoSpesa> ricercaSinteticaPreDocumentoSpesa() {
		RicercaSinteticaPreDocumentoSpesaResponse resRSPD = ricercaSinteticaPreDocumentoSpesa(0);		
		List<PreDocumentoSpesa> result =  resRSPD.getPreDocumenti();
		
		for(int i = 1; i < resRSPD.getTotalePagine(); i++) {			
			resRSPD = ricercaSinteticaPreDocumentoSpesa(i);
			result.addAll(resRSPD.getPreDocumenti());			
		}
		return result;
	}
	
	/**
	 * Ricerca sintetica pre documento spesa.
	 *
	 * @param numeroPagina the numero pagina
	 * @return the ricerca sintetica pre documento spesa response
	 */
	private RicercaSinteticaPreDocumentoSpesaResponse ricercaSinteticaPreDocumentoSpesa(int numeroPagina){
		RicercaSinteticaPreDocumentoSpesa reqRSPD = req.getRicercaSinteticaPreDocumentoSpesa();		
		ParametriPaginazione pp = new ParametriPaginazione(numeroPagina);
		pp.setElementiPerPagina(100);
		reqRSPD.setParametriPaginazione(pp);
		reqRSPD.setRichiedente(req.getRichiedente());		
		RicercaSinteticaPreDocumentoSpesaResponse resRSPD = executeExternalService(ricercaSinteticaPreDocumentoSpesa, reqRSPD);
		return resRSPD;
	}
	
	/**
	 * Carica il dettaglio del predocumento.
	 *
	 * @param preDoc the pre doc
	 * @return the dettaglio pre documento spesa
	 */
	protected PreDocumentoSpesa getDettaglioPreDocumentoSpesa(PreDocumentoSpesa preDoc) {
		RicercaDettaglioPreDocumentoSpesa req = new RicercaDettaglioPreDocumentoSpesa();
		req.setDataOra(new Date());
		req.setPreDocumentoSpesa(preDoc);
		req.setRichiedente(this.req.getRichiedente());
		RicercaDettaglioPreDocumentoSpesaResponse res = executeExternalServiceSuccess(ricercaDettaglioPreDocumentoSpesaService,req);
		return res.getPreDocumentoSpesa();
	}

}

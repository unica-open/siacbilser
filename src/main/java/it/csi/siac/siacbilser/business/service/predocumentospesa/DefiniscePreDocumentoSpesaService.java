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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.SubImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.Impegno;


/**
 * The Class DefiniscePreDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DefiniscePreDocumentoSpesaService extends CheckedAccountBaseService<DefiniscePreDocumentoSpesa, DefiniscePreDocumentoSpesaResponse> {

	private static final String SEPARATOR = " - ";
	private static final String ND = "N.D.";
	
	@Autowired
	@Qualifier("inserisceDocumentoSpesaService")
	private InserisceDocumentoSpesaService inserisceDocumentoSpesaService;
	@Autowired
	private InserisceQuotaDocumentoSpesaService inserisceQuotaDocumentoSpesaService;
	
	@Autowired
	private PreDocumentoSpesaDad preDocumentoSpesaDad;
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private CodificaDad codificaDad;
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ITALY);
	private int n = 0;
	
	private TipoDocumento tipoDocumentoDSP;
	private CodiceBollo codiceBolloEsente;
	private Boolean flagConvalidaManuale;
	private Date now;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getPreDocumentiSpesa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("predocumenti spesa"));
		
		if(req.getRicercaSinteticaPreDocumentoSpesa() == null){
			checkCondition(!req.getPreDocumentiSpesa().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista predocumenti spesa"));
			for(PreDocumentoSpesa preDoc : req.getPreDocumentiSpesa()){
				checkEntita(preDoc, "predocumento");
			}
		}
		
		checkEntita(req.getBilancio(), "bilancio");
		checkCondition(req.getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno bilancio"));
	}
	
	@Override
	protected void init() {
		documentoSpesaDad.setEnte(ente);
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		
		preDocumentoSpesaDad.setEnte(ente);
		preDocumentoSpesaDad.setLoginOperazione(loginOperazione);
		
		codificaDad.setEnte(ente);
		elencoDocumentiAllegatoDad.setEnte(ente);
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
		
		now = new Date();
	}
	
	@Override
	@Transactional(timeout=AsyncBaseService.TIMEOUT)
	public DefiniscePreDocumentoSpesaResponse executeService(DefiniscePreDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public DefiniscePreDocumentoSpesaResponse executeServiceTxRequiresNew(DefiniscePreDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		this.flagConvalidaManuale = ottieniDefaultFlagConvalidaManuale();
		
		// Ottengo i predoc
		List<PreDocumentoSpesa> preDocumentiSpesa = getPredocumentiSpesa();
		// Raggruppo
		Map<String, List<PreDocumentoSpesa>> preDocumentiSpesaRaggruppatiPrimoLivello = raggruppaPrimoLivelloConStatoCompleto(preDocumentiSpesa);
		// Inserisco i dati
		inserisciDocumenti(preDocumentiSpesaRaggruppatiPrimoLivello);
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
		n = 0;
		for(List<PreDocumentoSpesa> preDocumentiPrimoLivello: preDocumentiSpesaRaggruppatiPrimoLivello.values()){
			n++;
			DocumentoSpesa doc = popolaDocumento(preDocumentiPrimoLivello);
			try {
				
				inserisciDocumentoSpesa(doc);
				inserisciSubDocumenti(doc, preDocumentiPrimoLivello);
			
			} catch(ExecuteExternalServiceException eese) {
				log.error(methodName, "non sono riuscito a inserire il documento.", eese);
				res.addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile inserire il documento:" + doc.getNumero() + ": " + eese.getMessage()));
			}
		}
		
	}

	/**
	 * Inserisci predocumenti
	 * @param doc il documento cui legare le quote
	 * @param preDocumentiPrimoLivello i predoc
	 */
	private void inserisciSubDocumenti(DocumentoSpesa doc, List<PreDocumentoSpesa> preDocumentiPrimoLivello) {
		
		final String methodName = "inserisciSubDocumenti";
		
		for(PreDocumentoSpesa preDoc : preDocumentiPrimoLivello){
			SubdocumentoSpesa subDoc = popolaSubDocumento(preDoc, doc);
			try {
				inserisciSubDocumentoSpesa(subDoc);
				collegaAdElenco(preDoc, subDoc);
			} catch(ExecuteExternalServiceException eese) {
				log.error(methodName, "non sono riuscito a inserire la quota: ", eese);
				res.addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile inserire subdocumento del documento uid: " + doc.getUid() + "]: " + eese.getMessage()));
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
		
		subDoc.setDescrizione(doc.getDescrizione() + " - " + preDoc.getNumero());
		subDoc.setImporto(preDoc.getImportoNotNull());
		
		subDoc.setCausaleOrdinativo(preDoc.getCausaleSpesa().getCodice() + " " + preDoc.getCausaleSpesa().getDescrizione());
		
		subDoc.setImpegno(preDoc.getImpegno());
		subDoc.setSubImpegno(preDoc.getSubImpegno());
		
		if(preDoc.getModalitaPagamentoSoggetto() != null
				&& preDoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2() != null 
				&& preDoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2().getUid() != 0){
			subDoc.setModalitaPagamentoSoggetto(preDoc.getModalitaPagamentoSoggetto().getModalitaPagamentoSoggettoCessione2());
		}else{
			subDoc.setModalitaPagamentoSoggetto(preDoc.getModalitaPagamentoSoggetto());
		}
		
		subDoc.setSedeSecondariaSoggetto(preDoc.getSedeSecondariaSoggetto());
		
		//CR-4922
		//subDoc.setAttoAmministrativo(preDoc.getAttoAmministrativo());
		
//		subDoc.setCommissioniDocumento(CommissioniDocumento.BENEFICIARIO);
		subDoc.setProvvisorioCassa(preDoc.getProvvisorioDiCassa());
		subDoc.setVoceMutuo(preDoc.getVoceMutuo());
		subDoc.setDocumento(doc);
		subDoc.setEnte(ente);
		subDoc.setFlagConvalidaManuale(this.flagConvalidaManuale);
		// SIAC-5205
		subDoc.setCausaleOrdinativo(preDoc.getDescrizione());
		
		// SIAC-5311 SIOPE+
		copiaDatiSiope(preDoc, subDoc);
		
		return subDoc;
	}

	/**
	 * Tipo debito SIOPE, CIG o, in alternativa, il Motivo esclusione CIG sono da registrare sul documento
	 * in fase di definisci pre-documento derivandoli dall'impegno/subimpegno
	 * @param preDoc il predocumento originale
	 * @param subDoc il subdocumento su cui segnare i dati del SIOPE
	 */
	private void copiaDatiSiope(PreDocumentoSpesa preDoc, SubdocumentoSpesa subDoc) {
		final String methodName = "copiaDatiSiope";
		Impegno impegno = subDoc.getImpegnoOSubImpegno();
		if(impegno == null) {
			log.warn(methodName, "Il subdocumento corrispondente al predocumento [" + preDoc.getUid() + "] non ha impegno ne' subimpegno. Salto la copia dei dati del SIOPE");
			return;
		}
		
		subDoc.setCig(impegno.getCig());
		subDoc.setCup(impegno.getCup());
		subDoc.setSiopeAssenzaMotivazione(impegno.getSiopeAssenzaMotivazione());
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
		
		int uidDoc = resIDS.getDocumentoSpesa().getUid();
		
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
		doc.setNumero(calcolaNumeroDocumento(preDoc));
		doc.setDescrizione(calcolaDescrizioneDocumento(preDoc));
		doc.setDataEmissione(now);
		
		doc.setTipoDocumento(getTipoDocumentoDSP());
		doc.setCodiceBollo(getCodiceBolloEsente());
		
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		doc.setSoggetto(preDoc.getSoggetto());
		
		//TODO doc.setCommissioniDocumento?????? è nella quota!! subDoc.setCommissioniDocumento(tipoCommissione);
		
		BigDecimal totaleImportoPreDocumenti = calcolaTotaleImportoPreDocumenti(preDocumenti);
		doc.setImporto(totaleImportoPreDocumenti);
		
		// TODO: quando sara' richiesto (a posteriori della segnalazione SIAC-4680)
//		doc.setStrutturaAmministrativoContabile(preDoc.getStrutturaAmministrativoContabile());
		
		doc.setEnte(ente);
		
		return doc;
	}
	
	/**
	 * Ottiene il tipo di documento con codice DSP
	 * @return il tipo documento DSP
	 */
	private TipoDocumento getTipoDocumentoDSP() {
		if(tipoDocumentoDSP == null) {
			tipoDocumentoDSP = codificaDad.ricercaCodifica(TipoDocumento.class, "DSP");
		}
		return tipoDocumentoDSP;
	}
	
	/**
	 * Ottiene codice bollo corrispondente all'esente
	 * @return il codice bollo esente
	 */
	private CodiceBollo getCodiceBolloEsente() {
		if(codiceBolloEsente == null) {
			codiceBolloEsente = documentoSpesaDad.findCodiceBolloEsente();
		}
		return codiceBolloEsente;
	}

	/**
	 * Calcolo del numero del documento
	 * @param preDoc il predoc
	 * @return il numero del documento
	 */
	private String calcolaNumeroDocumento(PreDocumentoSpesa preDoc) {
		return new StringBuilder()
			.append(preDoc.getCausaleSpesa() != null ? preDoc.getCausaleSpesa().getCodice() : ND).append(SEPARATOR)
			.append(preDoc.getContoTesoreria() != null ? preDoc.getContoTesoreria().getCodice() : ND ).append(SEPARATOR)
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
	private String calcolaDescrizioneDocumento(PreDocumentoSpesa preDoc) {
		return new StringBuilder()
			.append(preDoc.getCausaleSpesa() != null ? preDoc.getCausaleSpesa().getDescrizione() : ND).append(SEPARATOR)
			.append(preDoc.getContoTesoreria() != null ? preDoc.getContoTesoreria().getDescrizione() : ND ).append(SEPARATOR)
			.append(preDoc.getPeriodoCompetenza() != null ? preDoc.getPeriodoCompetenza() : ND).append(SEPARATOR)
			.append(preDoc.getSoggetto() != null ? preDoc.getSoggetto().getCodiceSoggetto() : ND).append(SEPARATOR)
			.append(sdf.format(now))
			.toString();
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
			
			// SIAC-4996: se richiesto, evito di ricaricare il dettaglio del predoc
			if(!req.isSkipCaricamentoDettaglioPredocumento()) {
				preDoc = getDettaglioPreDocumentoSpesa(preDoc);
			}
			
			String key = getDocumentoKey(preDoc);
			
			if(!isCompleto(preDoc)) {
				log.info(methodName, "Saltato perche' non completo: " + key);
				res.getPredocumentiSaltati().add(preDoc);
				continue;
			}
			
			res.getPredocumentiElaborati().add(preDoc);
			
			if(!result.containsKey(key)) {
				result.put(key, new ArrayList<PreDocumentoSpesa>());
				log.debug(methodName, "Nuovo gruppo di primo livello: "+key);
			}
			
			List<PreDocumentoSpesa> list = result.get(key);
			list.add(preDoc);
		}
		Utility.MDTL.clear();
		
		return result;
	}

	/**
	 * Gets the documento key.
	 *
	 * @param preDoc the pre doc
	 * @return the documento key
	 */
	private String getDocumentoKey(PreDocumentoSpesa preDoc) {
		
		return new StringBuilder()
				.append(preDoc.getStrutturaAmministrativoContabile() != null ? preDoc.getStrutturaAmministrativoContabile().getUid() : "null").append(" ")
				.append(preDoc.getCausaleSpesa() != null ? preDoc.getCausaleSpesa().getUid() : "null").append(" ")
				.append(preDoc.getContoTesoreria() != null ? preDoc.getContoTesoreria().getUid() : "null").append(" ")
				.append(preDoc.getPeriodoCompetenza() != null ? preDoc.getPeriodoCompetenza() : "null").append(" ")
				.append(preDoc.getSoggetto() != null ? preDoc.getSoggetto().getUid() : "null")
//				.append(preDoc.getProvvisorioDiCassa() != null ? preDoc.getProvvisorioDiCassa().getUid() : "null")
				.toString();
	}
	
	
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
		if(req.getRicercaSinteticaPreDocumentoSpesa() != null){
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
		ListaPaginata<PreDocumentoSpesa> result =  ricercaSinteticaPreDocumentoSpesa(0);
		
		for(int i = 1; i < result.getTotalePagine(); i++) {
			List<PreDocumentoSpesa> temp = ricercaSinteticaPreDocumentoSpesa(i);
			result.addAll(temp);
		}
		return result;
	}
	
	/**
	 * Ricerca sintetica pre documento spesa.
	 *
	 * @param numeroPagina the numero pagina
	 * @return i predoc di spesa
	 */
	private ListaPaginata<PreDocumentoSpesa> ricercaSinteticaPreDocumentoSpesa(int numeroPagina){
		return preDocumentoSpesaDad.ricercaSinteticaPreDocumentoModelDetail(
				req.getRicercaSinteticaPreDocumentoSpesa().getPreDocumentoSpesa(),
				req.getRicercaSinteticaPreDocumentoSpesa().getTipoCausale(),
				req.getRicercaSinteticaPreDocumentoSpesa().getDataCompetenzaDa(),
				
				req.getRicercaSinteticaPreDocumentoSpesa().getDataCompetenzaA(),
				req.getRicercaSinteticaPreDocumentoSpesa().getCausaleSpesaMancante(),
				req.getRicercaSinteticaPreDocumentoSpesa().getContoTesoreriaMancante(),
				req.getRicercaSinteticaPreDocumentoSpesa().getSoggettoMancante(),
				
				req.getRicercaSinteticaPreDocumentoSpesa().getProvvedimentoMancante(),
				req.getRicercaSinteticaPreDocumentoSpesa().getNonAnnullati(),
				req.getRicercaSinteticaPreDocumentoSpesa().getOrdinativoPagamento(),
				new ParametriPaginazione(numeroPagina, 100));
	}
	
	/**
	 * Carica il dettaglio del predocumento.
	 *
	 * @param preDoc the pre doc
	 * @return the dettaglio pre documento spesa
	 */
	protected PreDocumentoSpesa getDettaglioPreDocumentoSpesa(PreDocumentoSpesa preDoc) {
		Utility.MDTL.addModelDetails(
				PreDocumentoSpesaModelDetail.AttoAmm,
				PreDocumentoSpesaModelDetail.Causale,
				PreDocumentoSpesaModelDetail.ContoTesoreria,
				PreDocumentoSpesaModelDetail.ImpegnoModelDetail,
				PreDocumentoSpesaModelDetail.ModPag,
				PreDocumentoSpesaModelDetail.ProvvisorioDiCassa,
				PreDocumentoSpesaModelDetail.Sogg,
				PreDocumentoSpesaModelDetail.Stato,
				PreDocumentoSpesaModelDetail.VoceMutuo,
				//SIAC-6784
				PreDocumentoSpesaModelDetail.ElencoDocumentiAllegato,
				
				ImpegnoModelDetail.CigCup,
				ImpegnoModelDetail.SiopeAssenzaMotivazione,
				ImpegnoModelDetail.SiopeTipoDebito,
				
				SubImpegnoModelDetail.CigCup,
				SubImpegnoModelDetail.SiopeAssenzaMotivazione,
				SubImpegnoModelDetail.SiopeTipoDebito);
		
		return preDocumentoSpesaDad.findPreDocumentoByIdModelDetail(preDoc.getUid(), Utility.MDTL.byModelDetailClass(PreDocumentoSpesaModelDetail.class));
	}

}

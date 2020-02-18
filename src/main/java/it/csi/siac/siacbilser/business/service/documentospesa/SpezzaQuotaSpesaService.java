/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.ProvvisorioBilDad;
import it.csi.siac.siacbilser.integration.dad.SoggettoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * The Class SpezzaQuotaSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SpezzaQuotaSpesaService extends CheckedAccountBaseService<SpezzaQuotaSpesa, SpezzaQuotaSpesaResponse> {
	
	/** Pattern per il CIG */
	private static final Pattern CIG_PATTERN = Pattern.compile("\\w{10}");
	/** Pattern per il Cup */
	private static final Pattern CUP_PATTERN = Pattern.compile("[A-Za-z]\\w{2}[A-Za-z]\\w{11}");
	
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	private ProvvisorioBilDad provvisorioBilDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private SoggettoDad soggettoDad;
	
	private SubdocumentoSpesa subdocumentoSpesa;
	private ProvvisorioDiCassa provvisorioDiCassa;
	private BigDecimal importoOriginale;
	private BigDecimal importoSplitReverseOriginale;
	private TipoIvaSplitReverse tipoIvaSplitReverseOriginale;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getSubdocumentoSpesa(), "subdocumento");
		checkEntita(req.getBilancio(), "bilancio", false);
		
		// Check importo
		checkNotNull(req.getSubdocumentoSpesa().getImporto(), "importo");
		checkCondition(req.getSubdocumentoSpesa().getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("importo", "non puo' essere negativo"), false);
		// Importo split-reverse controllato sotto
		
		checkEntita(req.getSubdocumentoSpesa().getModalitaPagamentoSoggetto(), "modalita pagamento soggetto", false);
		
		checkNotNull(req.getSubdocumentoSpesa().getDataScadenza(), "data scadenza", false);
		
		// CIG e CUP
		checkCondition(StringUtils.isBlank(req.getSubdocumentoSpesa().getCig()) || CIG_PATTERN.matcher(req.getSubdocumentoSpesa().getCig()).matches(), 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("cig", "deve essere composto da dieci caratteri"), false);
		checkCondition(StringUtils.isBlank(req.getSubdocumentoSpesa().getCup()) || CUP_PATTERN.matcher(req.getSubdocumentoSpesa().getCup()).matches(), 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("cup", "deve essere composto da 15 caratteri, il primo e il quarto dei quali devono essere una lettera"), false);
	}
	
	@Override
	@Transactional(timeout=120)
	public SpezzaQuotaSpesaResponse executeService(SpezzaQuotaSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		elencoDocumentiAllegatoDad.setEnte(ente);
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected void execute() {
		caricaSubdocumentoSpesaOriginale();
		checkSubdocumento();
		
		aggiornaSubdocumentoSpesaOriginale();
		creaSubdocumentoNuovo();
		aggiornaStatoDocumento();
	}
	

	/**
	 * Caricamento della quota di spesa originale
	 */
	private void caricaSubdocumentoSpesaOriginale() {
		SubdocumentoSpesa ss = subdocumentoSpesaDad.findSubdocumentoSpesaById(req.getSubdocumentoSpesa().getUid());
		if(ss == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subdocumento", "uid " + req.getSubdocumentoSpesa().getUid()));
		}
		subdocumentoSpesa = ss;
		importoOriginale = subdocumentoSpesa.getImporto();
		importoSplitReverseOriginale = subdocumentoSpesa.getImportoSplitReverse();
		tipoIvaSplitReverseOriginale = subdocumentoSpesa.getTipoIvaSplitReverse();
	}

	/**
	 * Controllo dei dati del subdocumento
	 */
	private void checkSubdocumento() {
		// NUOVO IMPORTO QUOTA: Deve essere < importo quota originale e > 0
		if(importoOriginale.compareTo(req.getSubdocumentoSpesa().getImporto()) <= 0) {
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("importo", "deve essere inferiore l'importo originale del subdocumento"));
		}
		// NUOVO IMPORTO IVA SPLIT: Deve essere < importo quota Iva originale(se presente) e > 0
		if(subdocumentoSpesa.getImportoSplitReverse() != null && subdocumentoSpesa.getImportoSplitReverse().signum() > 0) {
			// L'importo deve esistere
			if(req.getSubdocumentoSpesa().getImportoSplitReverse() == null) {
				throw new BusinessException(ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo split-reverse"));
			}
			// L'importo deve essere > 0
			if(req.getSubdocumentoSpesa().getImportoSplitReverse().signum() < 0) {
				throw new BusinessException(ErroreCore.FORMATO_NON_VALIDO.getErrore("importo split-reverse", "non puo' essere negativo"));
			}
			// L'importo non puo' superare l'importo originale
			if(subdocumentoSpesa.getImportoSplitReverse().compareTo(req.getSubdocumentoSpesa().getImportoSplitReverse()) < 0) {
				throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("importo split-reverse", "non deve essere superiore l'importo split-reverse originale del subdocumento"));
			}
		} else if(req.getSubdocumentoSpesa().getImportoSplitReverse() != null){
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("importo split-reverse", "non deve essere valorizzato in quanto non presente sulla quota"));
		}
		
		// SIAC-5468
		if(subdocumentoSpesa.getImportoDaDedurre() != null && subdocumentoSpesa.getImportoDaDedurre().signum() > 0) {
			// Non devo avere collegate note di credito
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("la quota da spezzare e' collegata a una nota di credito"));
		}
		//SIAC-7032
		checkModPagIsPagoPA();
		
		checkProvvisorioCassa();
	}
	
	/**
	 * Controllo di esistenza e capienza del provvisorio di cassa
	 */
	private void checkProvvisorioCassa() {
		final String methodName = "checkProvvisorioCassa";
		if(req.getSubdocumentoSpesa().getProvvisorioCassa() == null || req.getSubdocumentoSpesa().getProvvisorioCassa().getUid() == 0) {
			log.debug(methodName, "Provvisorio di cassa non fornito, esco");
			return;
		}
		ProvvisorioDiCassa pdc = provvisorioBilDad.findProvvisorioById(req.getSubdocumentoSpesa().getProvvisorioCassa().getUid());
		checkBusinessCondition(pdc != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", "uid " + req.getSubdocumentoSpesa().getProvvisorioCassa().getUid()));

		// TODO: fix dei check sul provvisorio di cassa
		// SIAC-6192: si richiede che l'importo della quota possa essere pari all'importo da regolarizzare del provvisorio
		// SIAC-xxxx: nel caso in cui non si chieda di modificare il provvisorio di cassa, saltare il controllo
		ProvvisorioDiCassa oldPdc = subdocumentoSpesa.getProvvisorioCassa();
		if(oldPdc != null && oldPdc.getUid() == pdc.getUid()) {
			// Ripristino una parte dell'importo da regolarizzare sul provvisorio
			BigDecimal importoDaRegolarizzare = pdc.getImportoDaRegolarizzare() == null ? BigDecimal.ZERO : pdc.getImportoDaRegolarizzare();
			importoDaRegolarizzare = importoDaRegolarizzare.add(req.getSubdocumentoSpesa().getImporto());
			pdc.setImportoDaRegolarizzare(importoDaRegolarizzare);
		} else {
			checkBusinessCondition(pdc.getImportoDaRegolarizzare() == null || pdc.getImportoDaRegolarizzare().compareTo(req.getSubdocumentoSpesa().getImporto()) >= 0,
				ErroreCore.VALORE_NON_VALIDO.getErrore("importo", "non deve essere superiore all'importo da regolarizzare del provvisorio di cassa (" + pdc.getImportoDaRegolarizzare().toPlainString() + ")"));
		}
		provvisorioDiCassa = pdc;
	}

	/**
	 * Aggiornamento del subdocumento originale
	 */
	private void aggiornaSubdocumentoSpesaOriginale() {
		final String methodName = "aggiornaSubdocumentoSpesaOriginale";
		log.debug(methodName, "Copia dei dati sulla quota");
		
		// SIAC-5979 - inizio modifiche
		
		// Aggiornamento sui dati recuperati
		subdocumentoSpesa.setImporto(importoOriginale.subtract(req.getSubdocumentoSpesa().getImporto()));
		
		if(importoSplitReverseOriginale != null) {
			subdocumentoSpesa.setImportoSplitReverse(importoSplitReverseOriginale.subtract(req.getSubdocumentoSpesa().getImportoSplitReverseNotNull()));
			subdocumentoSpesa.setTipoIvaSplitReverse(tipoIvaSplitReverseOriginale);
		}
		
		handleSplitReverseZero(subdocumentoSpesa);
		
		if (subdocumentoSpesa.getLiquidazione() != null) {
			subdocumentoSpesa.getLiquidazione().setImportoLiquidazione(subdocumentoSpesa.getImporto());
		}

		// SIAC-5979 - fine modifiche
		
		// Chiamo il servizio
		AggiornaQuotaDocumentoSpesa reqAQDS = new AggiornaQuotaDocumentoSpesa();
		
		reqAQDS.setDataOra(new Date());
		reqAQDS.setRichiedente(req.getRichiedente());
		reqAQDS.setAggiornaStatoDocumento(false);
		reqAQDS.setBilancio(req.getBilancio());
		reqAQDS.setSubdocumentoSpesa(subdocumentoSpesa);
		
		AggiornaQuotaDocumentoSpesaResponse resAQDS = serviceExecutor.executeServiceSuccess(AggiornaQuotaDocumentoSpesaService.class, reqAQDS);
		
		log.debug(methodName, "Aggiornato subdoc " + resAQDS.getSubdocumentoSpesa().getUid());
		
		res.setSubdocumentoSpesaAttuale(res.getSubdocumentoSpesaAttuale());
	}

	/**
	 * SIAC-5457
	 * <br/>
	 * Quando nello spezzare una quota lo split rimane su una sola quota, occorre eliminare i dati split dalla quota a cui &eacute; stato azzerato l'importo per poterla riutilizzare 
	 */
	private void handleSplitReverseZero(SubdocumentoSpesa subdoc) {
		final String methodName = "handleSplitReverseZero";
		if(subdoc.getImportoSplitReverse() == null || subdoc.getImportoSplitReverse().signum() != 0) {
			log.debug(methodName, "Split/reverse non valorizzato o non zero: esco");
			return;
		}
		log.debug(methodName, "Importo split/reverse valorizzato a zero: cancello il dato");
		subdoc.setImportoSplitReverse(null);
		subdoc.setTipoIvaSplitReverse(null);
	}

	/**
	 * Creazione del nuovo subdocumento
	 */
	private void creaSubdocumentoNuovo() {
		final String methodName = "creaSubdocumentoNuovo";
		
		// Clono il subdoc
		SubdocumentoSpesa ss = JAXBUtility.clone(subdocumentoSpesa);
		// Imposto i nuovi valori
		ss.setUid(0);
		ss.setLiquidazione(null);
		
		// SIAC-5979 - inizio modifiche
				
		ss.setProvvisorioCassa(provvisorioDiCassa);

		ss.setImporto(req.getSubdocumentoSpesa().getImporto());
		// SIAC-5940: Spezza quote errore modalita' pagamento
		ss.setModalitaPagamentoSoggetto(req.getSubdocumentoSpesa().getModalitaPagamentoSoggetto());

		ss.setImportoSplitReverse(req.getSubdocumentoSpesa().getImportoSplitReverseNotNull());
		ss.setTipoIvaSplitReverse(tipoIvaSplitReverseOriginale);

		ss.setDataScadenza(req.getSubdocumentoSpesa().getDataScadenza());
		
		// SIAC-5979 - fine modifiche

		handleSplitReverseZero(ss);
		
		InserisceQuotaDocumentoSpesa reqIQDS = new InserisceQuotaDocumentoSpesa();
		
		reqIQDS.setDataOra(new Date());
		reqIQDS.setRichiedente(req.getRichiedente());
		reqIQDS.setAggiornaStatoDocumento(false);
		reqIQDS.setBilancio(req.getBilancio());
		reqIQDS.setQuotaContestuale(false);
		reqIQDS.setSubdocumentoSpesa(ss);
		reqIQDS.setForzaRicalcolaDisponibilitaLiquidare(true);
		// SIAC-5393
		reqIQDS.setPreventInserimentoLiquidazione(subdocumentoSpesa.getLiquidazione() == null || subdocumentoSpesa.getLiquidazione().getUid() == 0);
		
		InserisceQuotaDocumentoSpesaResponse resIQDS = serviceExecutor.executeServiceSuccess(InserisceQuotaDocumentoSpesaService.class, reqIQDS);
		
		log.debug(methodName, "Inserito subdocumento " + resIQDS.getSubdocumentoSpesa().getUid());
		// Collega a elenco
		collegaSubdocumentoElenco(resIQDS.getSubdocumentoSpesa(), subdocumentoSpesa.getElencoDocumenti());
		
		res.setSubdocumentoSpesaNuovo(resIQDS.getSubdocumentoSpesa());
	}

	private void collegaSubdocumentoElenco(Subdocumento<?, ?> subdoc, ElencoDocumentiAllegato elencoDocumenti) {
		final String methodName = "collegaSubdocumentoElenco";
		// Collegamento della quota con l'elenco (non gestito in automatico dall'inserisci quota)
		elencoDocumentiAllegatoDad.collegaElencoSubdocumento(elencoDocumenti, subdoc);
		log.debug(methodName, "Collegati subdocumento [" + subdoc.getUid() + "] con elenco [" + elencoDocumenti.getUid() + "]");
	}

	/**
	 * Aggiornamento dello stato del subdocumento
	 * Grazie mille per l'odio
	 */
	private void aggiornaStatoDocumento() {
		final String methodName = "aggiornaStatoDocumento";
		AggiornaStatoDocumentoDiSpesa reqAS = new AggiornaStatoDocumentoDiSpesa();
		
		reqAS.setDataOra(req.getDataOra());
		reqAS.setRichiedente(req.getRichiedente());
		reqAS.setBilancio(req.getBilancio());
		reqAS.setDocumentoSpesa(subdocumentoSpesa.getDocumento());
		
		log.debug(methodName, "Aggiornamento dello stato del documento " + subdocumentoSpesa.getDocumento().getUid());
		
		AggiornaStatoDocumentoDiSpesaResponse resAS = serviceExecutor.executeServiceSuccess(AggiornaStatoDocumentoDiSpesaService.class, reqAS);
		log.debug(methodName, "Nuovo stato del documento: " + resAS.getStatoOperativoDocumentoNuovo());
		
	}
	

	//SIAC-7032 e SIAC-6840
	private void checkModPagIsPagoPA() {
		
		//SIAC-7032
		if(subdocumentoSpesa.getDocumento() == null
				||subdocumentoSpesa.getDocumento().getTipoDocumento() == null ) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("documento spesa collegato", "impossibile reperire il tipo documento" ));
		}
		//SIAC-6840 (analisi) Se la riga non e' associato ad un documento (deriva daAssocia Movimento) la modalità di pagamento non può essere Avviso PagoPA
		if (subdocumentoSpesa.getDocumento().getTipoDocumento().isAllegatoAtto() && isModalitaPagamentoPagoPa()) {
			throw new BusinessException(ErroreFin.MOD_PAGO_PA_NON_AMMESSA.getErrore());
		}
		
	}

	private boolean isModalitaPagamentoPagoPa() {
		String tipoAccreditoNuovoSubdocumento = soggettoDad.getTipoAccreditoModalitaPagamentoSoggetto(req.getSubdocumentoSpesa().getModalitaPagamentoSoggetto().getUid());
		return tipoAccreditoNuovoSubdocumento != null && "APA".equals(tipoAccreditoNuovoSubdocumento);
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.ProvvisorioBilDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * The Class SpezzaQuotaSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SpezzaQuotaEntrataService extends CheckedAccountBaseService<SpezzaQuotaEntrata, SpezzaQuotaEntrataResponse> {
	
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	private ProvvisorioBilDad provvisorioBilDad;
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	private SubdocumentoEntrata subdocumentoEntrata;
	private ProvvisorioDiCassa provvisorioDiCassa;
	private BigDecimal importoOriginale;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getSubdocumentoEntrata(), "subdocumento");
		checkEntita(req.getBilancio(), "bilancio", false);
		
		// Check importo
		checkNotNull(req.getSubdocumentoEntrata().getImporto(), "importo");
		checkCondition(req.getSubdocumentoEntrata().getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("importo", "non puo' essere negativo"), false);
		checkNotNull(req.getSubdocumentoEntrata().getDataScadenza(), "data scadenza", false);
	}
	
	@Override
	@Transactional(timeout=120)
	public SpezzaQuotaEntrataResponse executeService(SpezzaQuotaEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		elencoDocumentiAllegatoDad.setEnte(ente);
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
	}

	@Override
	protected void execute() {
		caricaSubdocumentoOriginale();
		checkSubdocumento();
		
		aggiornaSubdocumentoOriginale();
		subdocumentoEntrataDad.flushAndClear();
		creaSubdocumentoNuovo();
		aggiornaStatoDocumento();
	}

	/**
	 * Caricamento della quota di entrata originale
	 */
	private void caricaSubdocumentoOriginale() {
		SubdocumentoEntrata ss = subdocumentoEntrataDad.findSubdocumentoEntrataById(req.getSubdocumentoEntrata().getUid());
		if(ss == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("subdocumento", "uid " + req.getSubdocumentoEntrata().getUid()));
		}
		subdocumentoEntrata = ss;
		importoOriginale = subdocumentoEntrata.getImporto();
	}

	/**
	 * Controllo dei dati del subdocumento
	 */
	private void checkSubdocumento() {
		// NUOVO IMPORTO QUOTA: Deve essere < importo quota originale e > 0
		if(importoOriginale.compareTo(req.getSubdocumentoEntrata().getImporto()) <= 0) {
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("importo", "deve essere inferiore l'importo originale del subdocumento"));
		}
		// SIAC-5468
		if(subdocumentoEntrata.getImportoDaDedurre() != null && subdocumentoEntrata.getImportoDaDedurre().signum() > 0) {
			// Non devo avere collegate note di credito
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("la quota da spezzare e' collegata a una nota di credito"));
		}
		checkProvvisorioCassa();
	}
	
	/**
	 * Controllo di esistenza e capienza del provvisorio di cassa
	 */
	private void checkProvvisorioCassa() {
		final String methodName = "checkProvvisorioCassa";
		if(req.getSubdocumentoEntrata().getProvvisorioCassa() == null || req.getSubdocumentoEntrata().getProvvisorioCassa().getUid() == 0) {
			log.debug(methodName, "Provvisorio di cassa non fornito, esco");
			return;
		}
		ProvvisorioDiCassa pdc = provvisorioBilDad.findProvvisorioById(req.getSubdocumentoEntrata().getProvvisorioCassa().getUid());
		checkBusinessCondition(pdc != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("provvisorio di cassa", "uid " + req.getSubdocumentoEntrata().getProvvisorioCassa().getUid()));
		// TODO: fix dei check sul provvisorio di cassa
		// SIAC-6192: si richiede che l'importo della quota possa essere pari all'importo da regolarizzare del provvisorio
		// SIAC-xxxx: nel caso in cui non si chieda di modificare il provvisorio di cassa, saltare il controllo
		ProvvisorioDiCassa oldPdc = subdocumentoEntrata.getProvvisorioCassa();
		if(oldPdc != null && oldPdc.getUid() == pdc.getUid()) {
			// Ripristino una parte dell'importo da regolarizzare sul provvisorio
			BigDecimal importoDaRegolarizzare = pdc.getImportoDaRegolarizzare() == null ? BigDecimal.ZERO : pdc.getImportoDaRegolarizzare();
			importoDaRegolarizzare = importoDaRegolarizzare.add(req.getSubdocumentoEntrata().getImporto());
			pdc.setImportoDaRegolarizzare(importoDaRegolarizzare);
		} else {
			checkBusinessCondition(pdc.getImportoDaRegolarizzare() == null || pdc.getImportoDaRegolarizzare().compareTo(req.getSubdocumentoEntrata().getImporto()) >= 0,
				ErroreCore.VALORE_NON_VALIDO.getErrore("importo", "non deve essere superiore all'importo da regolarizzare del provvisorio di cassa (" + pdc.getImportoDaRegolarizzare().toPlainString() + ")"));
		}
		
		provvisorioDiCassa = pdc;
	}

	/**
	 * Aggiornamento del subdocumento originale
	 */
	private void aggiornaSubdocumentoOriginale() {
		final String methodName = "aggiornaSubdocumentoOriginale";
		log.debug(methodName, "Copia dei dati sulla quota");
		
		// Aggiornamento sui dati recuperati
		subdocumentoEntrata.setImporto(importoOriginale.subtract(req.getSubdocumentoEntrata().getImporto()));
		subdocumentoEntrata.setDataScadenza(req.getSubdocumentoEntrata().getDataScadenza());
		//subdocumentoEntrata.setProvvisorioCassa(provvisorioDiCassa);
		
		// Chiamo il servizio
		AggiornaQuotaDocumentoEntrata reqAQDE = new AggiornaQuotaDocumentoEntrata();
		
		reqAQDE.setDataOra(new Date());
		reqAQDE.setRichiedente(req.getRichiedente());
		reqAQDE.setAggiornaStatoDocumento(false);
		reqAQDE.setBilancio(req.getBilancio());
		reqAQDE.setSubdocumentoEntrata(subdocumentoEntrata);
		reqAQDE.setImpostaFlagConvalidaManuale(false);
		
		AggiornaQuotaDocumentoEntrataResponse resAQDS = serviceExecutor.executeServiceSuccess(AggiornaQuotaDocumentoEntrataService.class, reqAQDE);
		
		log.debug(methodName, "Aggiornato subdoc " + resAQDS.getSubdocumentoEntrata().getUid());
		res.setSubdocumentoEntrataAttuale(subdocumentoEntrata);
	}

	/**
	 * Creazione del nuovo subdocumento
	 */
	private void creaSubdocumentoNuovo() {
		final String methodName = "creaSubdocumentoNuovo";
		
		// Clono il subdoc
		SubdocumentoEntrata se = JAXBUtility.clone(subdocumentoEntrata);
		// Imposto i nuovi valori
		se.setUid(0);
		se.setImporto(req.getSubdocumentoEntrata().getImporto());
		se.setProvvisorioCassa(provvisorioDiCassa);
		
		InserisceQuotaDocumentoEntrata reqIQDS = new InserisceQuotaDocumentoEntrata();
		
		reqIQDS.setDataOra(new Date());
		reqIQDS.setRichiedente(req.getRichiedente());
		reqIQDS.setAggiornaStatoDocumento(false);
		reqIQDS.setBilancio(req.getBilancio());
		reqIQDS.setQuotaContestuale(false);
		reqIQDS.setSubdocumentoEntrata(se);
		reqIQDS.setImpostaFlagConvalidaManuale(false);
		
		InserisceQuotaDocumentoEntrataResponse resIQDS = serviceExecutor.executeServiceSuccess(InserisceQuotaDocumentoEntrataService.class, reqIQDS);
		
		log.debug(methodName, "Inserito subdocumento " + resIQDS.getSubdocumentoEntrata().getUid());
		
		// Collegamento quota/elenco
		collegaSubdocumentoElenco(resIQDS.getSubdocumentoEntrata(), subdocumentoEntrata.getElencoDocumenti());
		
		res.setSubdocumentoEntrataNuovo(resIQDS.getSubdocumentoEntrata());
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
		AggiornaStatoDocumentoDiEntrata reqAS = new AggiornaStatoDocumentoDiEntrata();
		
		reqAS.setDataOra(req.getDataOra());
		reqAS.setRichiedente(req.getRichiedente());
		reqAS.setBilancio(req.getBilancio());
		reqAS.setDocumentoEntrata(subdocumentoEntrata.getDocumento());
		
		log.debug(methodName, "Aggiornamento dello stato del documento " + subdocumentoEntrata.getDocumento().getUid());
		
		AggiornaStatoDocumentoDiEntrataResponse resAS = serviceExecutor.executeServiceSuccess(AggiornaStatoDocumentoDiEntrataService.class, reqAS);
		log.debug(methodName, "Nuovo stato del documento: " + resAS.getStatoOperativoDocumentoNuovo());
		
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AnnullaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.EliminaQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentospesa.AnnullaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.EliminaQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazioneResponse;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

public abstract class DisassociaQuotaBaseService<REQ extends ServiceRequest,RES extends ServiceResponse> extends CheckedAccountBaseService<REQ, RES> {

	// Costanti applicative
	private static final Long ONE = Long.valueOf(1L);
		
	@Autowired
	protected ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	protected SubdocumentoDad subdocumentoDad;
	@Autowired
	private AllegatoAttoDad allegatoAttoDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private DocumentoDad documentoDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	@Autowired
	private AnnullaDocumentoSpesaService annullaDocumentoSpesaService;
	@Autowired
	private AnnullaDocumentoEntrataService annullaDocumentoEntrataService;
	@Autowired
	private LiquidazioneService liquidazioneService;
	
	protected ElencoDocumentiAllegato elencoDocumentiAllegato;
	protected Subdocumento<?, ?> subdocumento;
	protected AllegatoAtto allegatoAtto;
	protected Bilancio bilancio;
	private Date now = new Date();
	

	/**
	 * Caricamento del dettaglio del bilancio.
	 * 
	 * @return il bilancio con i dati di dettaglio
	 */
	protected Bilancio caricaDettaglioBilancio(Integer uidBilancio) {
		final String methodName = "caricaDettaglioBilancio";
		log.debug(methodName, "Caricamento dettaglio bilancio con uid " +uidBilancio);
		
		return bilancioDad.getBilancioByUid(uidBilancio);
	}
	
	/**
	 * Caricamento del dettaglio dell'allegato atto
	 * 
	 * @return l'allegato atto con i dati di dettaglio
	 */
	protected AllegatoAtto caricaAllegatoAtto() {
		final String methodName = "caricaAllegatoAtto";
		log.debug(methodName, "Caricamento atto amministrativo per elenco " + elencoDocumentiAllegato.getUid());
		
		return allegatoAttoDad.findAllegatoAttoByElencoDocumentiAllegato(elencoDocumentiAllegato);
	}

	/**
	 * Controlla che la quota non sia ancora stata convalidata (ovvero l'attributo <em>TipoConvalida</em> non &eacute; valorizzato).
	 */
	protected void checkQuotaNonConvalidata() {
		Boolean flagConvalidaManuale = subdocumentoDad.findFlagConvalidaManuale(subdocumento);
		if(flagConvalidaManuale != null) {
			throw new BusinessException(ErroreCore.VALORE_NON_VALIDO.getErrore("tipo convalida", "non deve essere valorizzato"));
		}
	}
	
	/**
	 * Cancella il legame tra l'elenco e la quota.
	 */
	protected void eliminaLegameElencoQuota() {
		final String methodName = "eliminaLegameElencoQuota";
		
		TipoDocumento tipoDocumento = subdocumentoDad.findTipoDocumentoBySubdocumento(subdocumento);
		Long numeroQuote = subdocumentoDad.countSubdocumentiConStessoPadre(subdocumento);
		log.debug(methodName, "numero quote legate allo stesso docuemento: " + numeroQuote);
		boolean isUltimaQuota = ONE.compareTo(numeroQuote) == 0;
		
		if(allegatoAtto != null) {
			// Eliminazione relazione con l'allegato
			subdocumentoDad.cancellaRelazioneSubdocumentoAttoAmm(subdocumento, allegatoAtto, now);
			log.debug(methodName, "Cancellata relazione tra atto amministrativo dell'allegato " + allegatoAtto.getUid() + " e subdocumento " + subdocumento.getUid());
		}
		
		TipoFamigliaDocumento tipoFamigliaDocumento = tipoDocumento.getTipoFamigliaDocumento();
		log.debug(methodName, "Subdocumento " + subdocumento.getUid() + ", tipoDocumento " + tipoDocumento.getCodice() + ", famiglia " + tipoFamigliaDocumento);
		if(TipoFamigliaDocumento.isSpesa(tipoFamigliaDocumento)) {
			// Annullamento liquidazione
			annullaLiquidazione();
		}
		
		if(isTipoAllegatoAtto(tipoDocumento)) {
			eliminaQuotaAllegatoAtto(tipoDocumento, isUltimaQuota);
		} else {
			elencoDocumentiAllegatoDad.disassociaQuotaDaElenco(elencoDocumentiAllegato, subdocumento);
		}
		
		log.debug(methodName, "Cancellata relazione tra elenco " + elencoDocumentiAllegato.getUid() + " e subdocumento " + subdocumento.getUid());
	}
	
	/**
	 * Annullamento della liquidazione.
	 */
	private void annullaLiquidazione() {
		final String methodName = "annullaLiquidazione";
		SubdocumentoSpesa ss = new SubdocumentoSpesa();
		ss.setUid(subdocumento.getUid());
		Liquidazione liquidazione = subdocumentoSpesaDad.findLiquidazioneAssociataAlSubdocumento(ss);
		if(liquidazione == null) {
			log.debug(methodName, "Il subdocumento di spesa con uid " + subdocumento.getUid() + " non ha una liquidazione associata");
			return;
		}
		log.debug(methodName, "Annullamento della liquidazione " + liquidazione.getUid() + " del subdocumento " + subdocumento.getUid());
		
		AnnullaLiquidazione request = new AnnullaLiquidazione();
		request.setAnnoEsercizio(String.valueOf(bilancio.getAnno()));
		request.setDataOra(now);
		request.setEnte(ente);
		request.setRichiedente(req.getRichiedente());
		request.setLiquidazioneDaAnnullare(liquidazione);
		
		AnnullaLiquidazioneResponse response = liquidazioneService.annullaLiquidazione(request);
		checkServiceResponseFallimento(response);
		log.debug(methodName, "Liquidazione " + liquidazione.getUid() + " del subdocumento " + subdocumento.getUid() + " annullata correttamente");
	}
	
	/**
	 * Controlla se il tipo di documento &eacute; allegato atto
	 * 
	 * @param tipoDocumento the tipo documento
	 * @return the tipo allegato atto
	 */
	private boolean isTipoAllegatoAtto(TipoDocumento tipoDocumento) {
		final String methodName = "isTipoAllegatoAtto";
		boolean isTipoAllegato = tipoDocumento.isAllegatoAtto();
		log.debug(methodName, "Subdocumento " + subdocumento.getUid() + " relativo ad un documento di tipo allegato atto? " + isTipoAllegato);
		return isTipoAllegato;
	}

	/**
	 * Elimina la quota di tipo allegato atto.
	 * 
	 * @param tipoDocumento il tipo di documento
	 */
	private void eliminaQuotaAllegatoAtto(TipoDocumento tipoDocumento, boolean isUltimaQuota) {
		final String methodName = "eliminaQuotaAllegatoAtto";
		final TipoFamigliaDocumento tipoFamigliaDocumento = tipoDocumento.getTipoFamigliaDocumento();
		log.debug(methodName, "Subdocumento " + subdocumento.getUid() + " di famiglia " + tipoFamigliaDocumento);
		
		if(TipoFamigliaDocumento.isSpesa(tipoFamigliaDocumento)) {
			eliminazioneQuotaSpesaSePossibile();
			aggiornamentoImportoDocumento();
			if(isUltimaQuota){
				log.debug(methodName, "Il subdocumento " + subdocumento.getUid() + " e' l'ultimo del doc, annullo anche il documento");
				annullamentoDocumentoSpesa();
			}
			
		} else if (TipoFamigliaDocumento.isEntrata(tipoFamigliaDocumento)) {
			eliminazioneQuotaEntrataSePossibile();
			aggiornamentoImportoDocumento();
			if(isUltimaQuota){
				log.debug(methodName, "Il subdocumento " + subdocumento.getUid() + " e' l'ultimo del doc, annullo anche il documento");
				annullamentoDocumentoEntrata();
			}
			
		}
	}

	
	/**
	 * Se &eacute; possibile, elimina la quota di spesa
	 */
	private void eliminazioneQuotaSpesaSePossibile() {
		final String methodName = "eliminazioneQuotaSpesaSePossibile";
		subdocumentoDad.cancellaRelazioneSubdocumentoMovimentoGestione(subdocumento, now);
		log.debug(methodName, "Cancellate le relazioni tra subdocumento e movimenti di gestione collegati");
		
		EliminaQuotaDocumentoSpesa reqEQDS = new EliminaQuotaDocumentoSpesa();
		reqEQDS.setAggiornaStatoDocumento(false);
		reqEQDS.setDataOra(now);
		reqEQDS.setRichiedente(req.getRichiedente());
		reqEQDS.setBilancio(bilancio);
		reqEQDS.setRoolbackOnly(false);
		
		SubdocumentoSpesa subdocumentoSpesa = new SubdocumentoSpesa();
		subdocumentoSpesa.setUid(subdocumento.getUid());
		reqEQDS.setSubdocumentoSpesa(subdocumentoSpesa);
		
		
		EliminaQuotaDocumentoSpesaResponse resEQDS = serviceExecutor.executeServiceSuccess(EliminaQuotaDocumentoSpesaService.class, reqEQDS, ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getCodice());
		
		//EliminaQuotaDocumentoSpesaResponse resEQDS = eliminaQuotaDocumentoSpesaService.executeServiceTxRequiresNew(reqEQDS);
		checkServiceResponseFallimento(resEQDS,ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getCodice());
		if(resEQDS.verificatoErrore(ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getCodice())){
			log.debug(methodName, "non ho potuto eliminare la quota ma cancello ugualmente le relazioni");
			elencoDocumentiAllegatoDad.disassociaQuotaDaElenco(elencoDocumentiAllegato, subdocumento);
		}
		log.debug(methodName, "Eliminazione della quota richiamata. Eliminazione avvenuta? " + !resEQDS.isFallimento());
	}
	
	/**
	 * Se &eacute; possibile, elimina la quota di entrata
	 */
	private void eliminazioneQuotaEntrataSePossibile() {
		final String methodName = "eliminazioneQuotaEntrataSePossibile";
		subdocumentoDad.cancellaRelazioneSubdocumentoMovimentoGestione(subdocumento, now);
		log.debug(methodName, "Cancellate le relazioni tra subdocumento e movimenti di gestione collegati");
		
		EliminaQuotaDocumentoEntrata reqEQDE = new EliminaQuotaDocumentoEntrata();
		reqEQDE.setAggiornaStatoDocumento(false);
		reqEQDE.setDataOra(now);
		reqEQDE.setRichiedente(req.getRichiedente());
		reqEQDE.setBilancio(bilancio);
		reqEQDE.setRoolbackOnly(false);
		
		SubdocumentoEntrata subdocumentoEntrata = new SubdocumentoEntrata();
		subdocumentoEntrata.setUid(subdocumento.getUid());
		reqEQDE.setSubdocumentoEntrata(subdocumentoEntrata);
		
		
		EliminaQuotaDocumentoEntrataResponse resEQDE = serviceExecutor.executeServiceSuccess(EliminaQuotaDocumentoEntrataService.class, reqEQDE, ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getCodice());
//		EliminaQuotaDocumentoEntrataResponse response = eliminaQuotaDocumentoEntrataService.executeServiceTxRequiresNew(request);
		checkServiceResponseFallimento(resEQDE,ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getCodice());	
		if(resEQDE.verificatoErrore(ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getCodice())){
			log.debug(methodName, "non ho potuto eliminare la quota ma cancello ugualmente le relazioni");
			elencoDocumentiAllegatoDad.disassociaQuotaDaElenco(elencoDocumentiAllegato, subdocumento);
		}
		log.debug(methodName, "Eliminazione della quota richiamata. Eliminazione avvenuta? " + resEQDE.isFallimento());
	}

	/**
	 * Aggiornamento dell'importo del documento.
	 */
	private void aggiornamentoImportoDocumento() {
		final String methodName = "aggiornamentoImportoQuota";
		BigDecimal importoQuota = subdocumentoDad.getImportoSubdocumento(subdocumento);
		log.debug(methodName, "Importo della quota: " + importoQuota);
		documentoDad.updateImportoDocumento(null, importoQuota, subdocumento);
		log.debug(methodName, "Importo del documento aggiornato");
	}

	/**
	 * Annulla il documento di spesa se necessario.
	 */
	private void annullamentoDocumentoSpesa() {
		final String methodName = "annullamentoDocumentoSpesaSeNecessario";
		DocumentoSpesa documentoSpesa = subdocumentoSpesaDad.findDocumentoByIdSubdocumentoSpesa(subdocumento.getUid());
		log.debug(methodName, "Documento con uid " + documentoSpesa.getUid() + " associato al subdocumento " + subdocumento.getUid());
		
		AnnullaDocumentoSpesa request = new AnnullaDocumentoSpesa();
		request.setDataOra(now);
		request.setRichiedente(req.getRichiedente());
		request.setDocumentoSpesa(documentoSpesa);
		request.setBilancio(bilancio);
		
		AnnullaDocumentoSpesaResponse response = executeExternalServiceSuccess(annullaDocumentoSpesaService, request);
		log.debug(methodName, "Annullamento del documento di spesa " + documentoSpesa.getUid() + " avvenuto con successo? " + !response.isFallimento());
		subdocumentoDad.cancellaQuoteByUidDocumento(documentoSpesa, now);
		log.debug(methodName, "Quote del documento di spesa " + documentoSpesa.getUid() + " cancellate");
	}
	
	/**
	 * Annulla il documento di entrata se necessario.
	 */
	private void annullamentoDocumentoEntrata() {
		final String methodName = "annullamentoDocumentoEntrataSeNecessario";
		DocumentoEntrata documentoEntrata = subdocumentoEntrataDad.findDocumentoByIdSubdocumentoEntrata(subdocumento.getUid());
		log.debug(methodName, "Documento con uid " + documentoEntrata.getUid() + " associato al subdocumento " + subdocumento.getUid());
		
		AnnullaDocumentoEntrata request = new AnnullaDocumentoEntrata();
		request.setDataOra(now);
		request.setRichiedente(req.getRichiedente());
		request.setDocumentoEntrata(documentoEntrata);
		request.setBilancio(bilancio);
		
		executeExternalServiceSuccess(annullaDocumentoEntrataService, request);
		log.debug(methodName, "Annullamento del documento di entrata " + documentoEntrata.getUid() + " avvenuto con successo");
		subdocumentoDad.cancellaQuoteByUidDocumento(documentoEntrata, now);
		log.debug(methodName, "Quote del documento di entrata " + documentoEntrata.getUid() + " cancellate");
	}

}

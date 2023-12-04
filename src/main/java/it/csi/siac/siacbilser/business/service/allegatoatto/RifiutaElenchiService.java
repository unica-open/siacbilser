/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AnnullaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.EliminaQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AnnullaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.EliminaQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.integration.dad.AllegatoAttoDad;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchiResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazioneResponse;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

/**
 * Rifiuta uno o più elenchi ed eventualmente l'intero allegato.
 * 
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RifiutaElenchiService extends CheckedAccountBaseService<RifiutaElenchi,RifiutaElenchiResponse> {

	private static final Date NOW = new Date();

	//DADs..
	@Autowired
	protected AllegatoAttoDad allegatoAttoDad;
	@Autowired 
	protected ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	protected SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	protected SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired
	protected SubdocumentoDad subdocumentoDad;
	@Autowired
	protected DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	protected DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	protected DocumentoDad documentoDad;
	@Autowired
	protected BilancioDad bilancioDad;
	
	//Services..
	@Autowired
	protected AnnullaDocumentoSpesaService annullaDocumentoSpesaService;
	@Autowired
	protected AnnullaDocumentoEntrataService annullaDocumentoEntrataService;
	@Autowired
	protected EliminaQuotaDocumentoSpesaService eliminaQuotaDocumentoSpesaService;
	@Autowired
	protected EliminaQuotaDocumentoEntrataService eliminaQuotaDocumentoEntrataService;
	@Autowired
	protected LiquidazioneService liquidazioneService;
	
	//Fields..
	private AllegatoAtto allegatoAtto;
	private Bilancio bilancio;
	private StatoOperativoAllegatoAtto statoOperativoAllegatoAtto;
	private List<ElencoDocumentiAllegato> elenchi;
	private boolean elenchiValorizzati;
	private Map<Integer, TipoFamigliaDocumento> documentiDaAggiornare = new HashMap<Integer, TipoFamigliaDocumento>();
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getAllegatoAtto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		checkCondition(req.getAllegatoAtto().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"), false);
		
		checkEntita(req.getBilancio(), "Bilancio");
		
		this.elenchiValorizzati = req.getAllegatoAtto().getElenchiDocumentiAllegato() != null
				&& !req.getAllegatoAtto().getElenchiDocumentiAllegato().isEmpty();
		
		if(elenchiValorizzati) {
			for(ElencoDocumentiAllegato elenco : req.getAllegatoAtto().getElenchiDocumentiAllegato()){
				checkNotNull(elenco, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("elenco documenti allegato allegato atto"));
				checkCondition(elenco.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid elenco documenti allegato allegato atto"));
			}
		}
		
		this.statoOperativoAllegatoAtto = req.getAllegatoAtto().getStatoOperativoAllegatoAtto();
	}

	@Override
	protected void init() {
		super.init();
		
		allegatoAttoDad.setEnte(ente);
		allegatoAttoDad.setLoginOperazione(loginOperazione);
		
		elencoDocumentiAllegatoDad.setEnte(ente);
		elencoDocumentiAllegatoDad.setLoginOperazione(loginOperazione);
		
		subdocumentoEntrataDad.setEnte(ente);
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
		
		subdocumentoSpesaDad.setEnte(ente);
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		
		documentiDaAggiornare = new HashMap<Integer, TipoFamigliaDocumento>();
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT * 4)
	public RifiutaElenchiResponse executeServiceTxRequiresNew(RifiutaElenchi serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}
	
	@Override
	@Transactional
	public RifiutaElenchiResponse executeService(RifiutaElenchi serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void execute() {
		String methodName = "execute";

		this.allegatoAtto = caricaAllegatoAtto(req.getAllegatoAtto().getUid());
		this.bilancio = caricaBilancio(req.getBilancio().getUid());
		this.elenchi = elenchiValorizzati ? req.getAllegatoAtto().getElenchiDocumentiAllegato() : allegatoAtto.getElenchiDocumentiAllegato();
		
		checkStatoOperativoAllegatoAttoCompletato(allegatoAtto);
		
		caricaDatiElenchiAndCheckStatoElenchi();
			
		for(ElencoDocumentiAllegato elencoDocumentiAllegato : elenchi){
			
			if(!elenchiValorizzati){ //se sto rifiutando tutto l'allegato
				
				Date dataTrasmissione = elencoDocumentiAllegato.getDataTrasmissione();
				log.debug(methodName, "Elenco " + elencoDocumentiAllegato.getUid() + " - data trasmissione: " + dataTrasmissione);
				//Per tutti i subdocumenti in elenco...
				for(Subdocumento<?, ?> subdocReq : elencoDocumentiAllegato.getSubdocumenti()){
					Subdocumento<?, ?> subdoc = caricaSubdocumentoConDatiDiBase(subdocReq);
					Integer uidDoc = subdoc.getDocumento().getUid();
					// Sgancia quota da atto amministrativo
					subdocumentoDad.cancellaRelazioneSubdocumentoAttoAmm(subdoc, allegatoAtto, NOW);
					try {
						// Annulla Liquidazioni: ogni liquidazione legata ai documenti dell'elenco in rifiuto deve essere annullata.
						if(subdoc instanceof SubdocumentoSpesa) {
							annullaLiquidazione((SubdocumentoSpesa) subdoc);
						}
						
						if(dataTrasmissione != null || subdoc.getDocumento().getTipoDocumento().isAllegatoAtto()){
							//Se la data di trasmissione e' valorizzata oppure la quota appartiene ad un documento di tipo AllegatoAtto si elimina la quota.
							eliminaQuota(subdoc);
							eliminaDocumentoSeNecessario(subdoc, uidDoc);
						} else{
							elencoDocumentiAllegatoDad.disassociaQuotaDaElenco(elencoDocumentiAllegato, subdoc);
						}
					} catch(RuntimeException re) {
						String motivazioneScarto = "Scartata quota con uid " + subdoc.getUid() + " relativa all'elenco " + elencoDocumentiAllegato.getUid()
								+ " per causa: " + re.getMessage();
						// TODO: scartare la quota e segnalarlo
						log.debug(methodName, motivazioneScarto);
						res.getListaScarti().add(motivazioneScarto);
					}
//					aggiornaStatoDocumento(subdoc, uidDoc);
					aggiungiDocumentoDaAggiornare(uidDoc, getTipoFamigliaDocumento(subdoc));
					
				}
				aggiornaStatoOperativoElencoDocumenti(elencoDocumentiAllegato, StatoOperativoElencoDocumenti.RIFIUTATO);
				
			}else{ //se sto rifiutando solo alcuni elenchi
				
				for(Subdocumento<?, ?> subdocReq : elencoDocumentiAllegato.getSubdocumenti()){
					Subdocumento<?, ?> subdoc = caricaSubdocumentoConDatiDiBase(subdocReq);
					Integer uidDoc = subdoc.getDocumento().getUid();
					try {
						// Annulla Liquidazioni: ogni liquidazione legata ai documenti dell'elenco in rifiuto deve essere annullata.
						if(subdoc instanceof SubdocumentoSpesa) {
							annullaLiquidazione((SubdocumentoSpesa) subdoc);
						}
					} catch(RuntimeException re) {
						String motivazioneScarto = "Scartata quota con uid " + subdoc.getUid() + " relativa all'elenco " + elencoDocumentiAllegato.getUid()
								+ " per causa: " + re.getMessage();
						log.debug(methodName, motivazioneScarto);
						res.getListaScarti().add(motivazioneScarto);
					}
//					aggiornaStatoDocumento(subdoc, uidDoc);
					aggiungiDocumentoDaAggiornare(uidDoc, getTipoFamigliaDocumento(subdoc));
				}
				aggiornaStatoOperativoElencoDocumenti(elencoDocumentiAllegato, StatoOperativoElencoDocumenti.BOZZA);
			}
		}
		
		aggiornaStatoDocumentiDaAggiornare();
			
		StatoOperativoAllegatoAtto statoOperativoAggiornato = elenchiValorizzati ? StatoOperativoAllegatoAtto.DA_COMPLETARE : StatoOperativoAllegatoAtto.RIFIUTATO;
		aggiornaStatoOperativoAllegatoAtto(req.getAllegatoAtto(), statoOperativoAggiornato);
		
	}

	private void aggiornaStatoDocumentiDaAggiornare() {
		final String methodName = "aggiornaStatoDocumentiDaAggiornare";
		
		log.debug(methodName, "Aggiorno lo stato dei seguenti Documenti: "+(log.isDebugEnabled()?documentiDaAggiornare.toString():""));
		
		for(Entry<Integer, TipoFamigliaDocumento> e : documentiDaAggiornare.entrySet()){
			Integer uidDocumento = e.getKey();
			TipoFamigliaDocumento tipoFamiglia = e.getValue();
			
			if(tipoFamiglia.equals(TipoFamigliaDocumento.SPESA)){
				aggiornaStatoDocSpesa(uidDocumento);
			} else {
				aggiornaStatoDocEntrata(uidDocumento);
			}
		}
		
	}

	private void aggiungiDocumentoDaAggiornare(Integer uidDoc, TipoFamigliaDocumento tipoFamigliaDocumento) {
		documentiDaAggiornare.put(uidDoc, tipoFamigliaDocumento);
		
	}

	private TipoFamigliaDocumento getTipoFamigliaDocumento(Subdocumento<?, ?> subdoc) {
		if(subdoc instanceof SubdocumentoSpesa){
			return TipoFamigliaDocumento.SPESA;
		} else if (subdoc instanceof SubdocumentoEntrata){
			return TipoFamigliaDocumento.ENTRATA;
		} else {
			throw new IllegalArgumentException("Tipologia di Sudocumento non deducibile: "+ (subdoc!=null?subdoc.getClass():"subdoc e' null."));
		}
	}

	private void caricaDatiElenchiAndCheckStatoElenchi() {
		// Verifica dati
		ListIterator<ElencoDocumentiAllegato> iteratorEDA = elenchi.listIterator();
		while(iteratorEDA.hasNext()) {
			ElencoDocumentiAllegato eda = iteratorEDA.next();
			ElencoDocumentiAllegato elencoDocumentiAllegato = caricaElencoDocumentiAllegato(eda.getUid());
			checkStatoElenco(elencoDocumentiAllegato);
			// Sostituisco il valore con quello calcolato da db
			iteratorEDA.set(elencoDocumentiAllegato);
		}
	}
	
	private void eliminaQuota(Subdocumento<?, ?> subdoc) {
		if(subdoc instanceof SubdocumentoSpesa){
			subdocumentoSpesaDad.eliminaSubdocumentoSpesa((SubdocumentoSpesa) subdoc);
		}else if (subdoc instanceof SubdocumentoEntrata){
			subdocumentoEntrataDad.eliminaSubdocumentoEntrata((SubdocumentoEntrata) subdoc);
		}
	}
	
	private void eliminaDocumentoSeNecessario(Subdocumento<?, ?> subdoc, Integer uidDoc) {
		boolean docDaAnnullare = isDocumentoSenzaQuote(uidDoc);
		if(subdoc instanceof SubdocumentoSpesa && docDaAnnullare){
			annullaDocumentoSpesa(uidDoc);
		}else if (subdoc instanceof SubdocumentoEntrata && docDaAnnullare){
			annullaDocumentoEntrata(uidDoc);
		}
		
	}

	private DocumentoEntrata annullaDocumentoEntrata(Integer uidDoc) {
		DocumentoEntrata doc = new DocumentoEntrata();
		doc.setUid(uidDoc);
		AnnullaDocumentoEntrata reqAD = new AnnullaDocumentoEntrata();
		reqAD.setDocumentoEntrata(doc);
		reqAD.setRichiedente(req.getRichiedente());
		reqAD.setBilancio(bilancio);
		AnnullaDocumentoEntrataResponse resAD = serviceExecutor.executeServiceSuccess(annullaDocumentoEntrataService, reqAD);
		return resAD.getDocumentoEntrata();
	}

	private DocumentoSpesa annullaDocumentoSpesa(Integer uidDoc) {
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(uidDoc);
		AnnullaDocumentoSpesa reqAD = new AnnullaDocumentoSpesa();
		reqAD.setDocumentoSpesa(doc);
		reqAD.setRichiedente(req.getRichiedente());
		reqAD.setBilancio(bilancio);
		AnnullaDocumentoSpesaResponse resAD = serviceExecutor.executeServiceSuccess(annullaDocumentoSpesaService, reqAD);
		return resAD.getDocumentoSpesa();
	}
	
	private boolean isDocumentoSenzaQuote(Integer uidDoc) {
		Long numeroQuote = documentoDad.countSubdocumentiByUidDocumento(uidDoc);
		return numeroQuote.compareTo(Long.valueOf(0)) == 0;
	}

	/**
	 * Ottiene i dati dell'allegato atto il cui uid e' passato come parametro.
	 *
	 * @param uid the uid
	 * @return the allegato atto
	 */
	protected AllegatoAtto caricaAllegatoAtto(Integer uid) {
		final String methodName = "caricaAllegatoAtto";
		
		AllegatoAtto aa = allegatoAttoDad.findAllegatoAttoById(uid);
		
		if(aa == null) {
			log.error(methodName, "Nessun allegato atto con uid " + uid + " presente in archivio");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("allegato atto", "uid: "+ uid));
		}
		return aa;
	}
	
	/**
	 * Ottiene i dati dell bilancio il cui uid e' passato come parametro.
	 *
	 * @param uid the uid
	 * @return the bilancio
	 */
	protected Bilancio caricaBilancio(Integer uid) {
		final String methodName = "caricaBilancio";
		
		Bilancio bil = bilancioDad.getBilancioByUid(uid);
		
		if(bil == null) {
			log.error(methodName, "Nessun bilancio con uid " + uid + " presente in archivio");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("bilancio", "uid: "+ uid));
		}
		return bil;
	}
	
	/**
	 * SE  statoOperativoAllegatoAtto <> C – COMPLETATO o PC – PARZIALMENTE CONVALIDATO
	 * Allora  l’allegato deve essere scartato con la motivazione  <FIN_ERR_0226, Stato Allegato Atto incongruente>.
	 *
	 * @param aa the allegatoAtto
	 */
	protected void checkStatoOperativoAllegatoAttoCompletato(AllegatoAtto aa) {
		final String methodName = "checkStatoOperativoAllegatoAttoCompletato";
		
		if(!StatoOperativoAllegatoAtto.COMPLETATO.equals(aa.getStatoOperativoAllegatoAtto()) 
				&& !StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(aa.getStatoOperativoAllegatoAtto())) {
			
			log.error(methodName, "Stato non valido: " + aa.getStatoOperativoAllegatoAtto());
			throw new BusinessException(ErroreFin.STATO_ATTO_DA_ALLEGATO_INCONGRUENTE.getErrore());
		}
	}
	
	/**
	 * Stato Elenchi: lo StatoOperativoElencoDocumento di tutti gli elenchi passati in input deve essere COMPLETATO o,
	 * se a parametro &eacute; stato passato Stato Operativo Atto in Convalida RIFIUTATO, anche RIFIUTATI.
	 * <br/>
	 * Se il controllo non ha esito positivo viene segnalato l'errore bloccante: &lt;FIN_ERR_0146: Elenco con stato incongruente o assente&gt;
	 *
	 * @param elenco the elenco
	 */
	protected void checkStatoElenco(ElencoDocumentiAllegato elenco) {
		String methodName = "checkStatoElenco";
		boolean statoOperativoCompatibile = (StatoOperativoAllegatoAtto.RIFIUTATO.equals(statoOperativoAllegatoAtto)
				&& StatoOperativoElencoDocumenti.RIFIUTATO.equals(elenco.getStatoOperativoElencoDocumenti()))
				|| StatoOperativoElencoDocumenti.COMPLETATO.equals(elenco.getStatoOperativoElencoDocumenti());
		
		if(!statoOperativoCompatibile){
			String statoAtteso = StatoOperativoAllegatoAtto.RIFIUTATO.equals(statoOperativoAllegatoAtto)
					? "Attesi: " + StatoOperativoElencoDocumenti.COMPLETATO + ", " + StatoOperativoElencoDocumenti.RIFIUTATO
					: "Atteso: " + StatoOperativoElencoDocumenti.COMPLETATO;
			log.error(methodName, "Stato non valido: " + elenco.getStatoOperativoElencoDocumenti() + ". " + statoAtteso);
			throw new BusinessException(ErroreFin.ELENCO_CON_STATO_INCONGRUENTE_O_ASSENTE.getErrore());
		}
	}
	
	private DocumentoSpesa aggiornaStatoDocSpesa(Integer uidDoc) {
		AggiornaStatoDocumentoDiSpesa reqSDS = new AggiornaStatoDocumentoDiSpesa();
		reqSDS.setRichiedente(req.getRichiedente());
		DocumentoSpesa documento = new DocumentoSpesa();
		documento.setUid(uidDoc);
		reqSDS.setDocumentoSpesa(documento);
		reqSDS.setBilancio(bilancio);
		AggiornaStatoDocumentoDiSpesaResponse resASDDS = serviceExecutor.executeServiceSuccess(AggiornaStatoDocumentoDiSpesaService.class, reqSDS);
//		AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaService = appCtx.getBean(AggiornaStatoDocumentoDiSpesaService.class);
//		AggiornaStatoDocumentoDiSpesaResponse resASDDS = aggiornaStatoDocumentoDiSpesaService.executeService(reqSDS);
		return resASDDS.getDocumentoSpesa();
		
	}
	
	private DocumentoEntrata aggiornaStatoDocEntrata(Integer uidDoc) {
		AggiornaStatoDocumentoDiEntrata reqSDE = new AggiornaStatoDocumentoDiEntrata();
		reqSDE.setRichiedente(req.getRichiedente());
		DocumentoEntrata documento = new DocumentoEntrata();
		documento.setUid(uidDoc);
		reqSDE.setDocumentoEntrata(documento);
		reqSDE.setBilancio(bilancio);
		
		AggiornaStatoDocumentoDiEntrataResponse resASDDE = serviceExecutor.executeServiceSuccess(AggiornaStatoDocumentoDiEntrataService.class, reqSDE);
//		AggiornaStatoDocumentoDiEntrataService aggiornaStatoDocumentoDiEntrataService = appCtx.getBean(AggiornaStatoDocumentoDiEntrataService.class);
//		AggiornaStatoDocumentoDiEntrataResponse resASDDE = aggiornaStatoDocumentoDiEntrataService.executeService(reqSDE);
		return resASDDE.getDocumentoEntrata();
	}

	private void annullaLiquidazione(SubdocumentoSpesa subdoc) {
		String methodName = "annullaLiquidazione";
		Liquidazione liquidazione = caricaLiquidazione(subdoc);
		if(liquidazione == null){ 
			//SIAC-3807. La liquidazione può essere null nel caso dei documenti stornati.
			log.info(methodName, "Il subdoc con uid: "+subdoc.getUid() + " non presenta nessuna liquidazione da annullare. Esco.");
			return;
		}
		annullaLiquidazione(liquidazione);
		scollegaLiquidazioneSubdocumento(subdoc, liquidazione);
		subdoc.setLiquidazione(null);
	}
	
	/**
	 * Carocamento della liquidazione
	 * @param subdoc
	 * @return
	 */
	private Liquidazione caricaLiquidazione(SubdocumentoSpesa subdoc) {
		final String methodName = "caricaLiquidazione";
		Liquidazione liquidazione = subdocumentoSpesaDad.findLiquidazioneAssociataAlSubdocumento(subdoc);
		log.debug(methodName, "Caricata liquidazione " + (liquidazione == null ? "null" : liquidazione.getUid()) + " per il subdocumento " + subdoc.getUid());
		return liquidazione;
	}

	/**
	 * Annulla la liquidazione.
	 * 
	 * @param l la liquidazione da annullare
	 */
	private void annullaLiquidazione(Liquidazione l) {
		AnnullaLiquidazione requestAnnullaLiquidazione = new AnnullaLiquidazione();
		Liquidazione liq = (Liquidazione)SerializationUtils.clone(l);
		requestAnnullaLiquidazione.setDataOra(NOW);
		requestAnnullaLiquidazione.setEnte(allegatoAtto.getEnte());
		requestAnnullaLiquidazione.setRichiedente(req.getRichiedente());
		requestAnnullaLiquidazione.setLiquidazioneDaAnnullare(liq);
		requestAnnullaLiquidazione.setAnnoEsercizio(String.valueOf(bilancio.getAnno()));
		requestAnnullaLiquidazione.setAggiornaStatoDocumento(false);
		
		AnnullaLiquidazioneResponse responseAnnullaLiquidazione = liquidazioneService.annullaLiquidazione(requestAnnullaLiquidazione);
		checkServiceResponseFallimento(responseAnnullaLiquidazione);
	}
	
	/**
	 * Scollega la liquidazione dal documento di spesa.
	 * 
	 * @param subdoc il subdocumento da scollegare
	 */
	private void scollegaLiquidazioneSubdocumento(SubdocumentoSpesa subdoc, Liquidazione liquidazione) {
		subdocumentoSpesaDad.eliminaLegameLiquidazioneQuota(liquidazione, subdoc, NOW);
	}
	
	/**
	 * Aggiorna lo stato dell'elenco.
	 * 
	 * @param elencoDocumentiAllegato the elenco documenti allegato
	 * @param stato the stato
	 */
	private void aggiornaStatoOperativoElencoDocumenti(ElencoDocumentiAllegato elencoDocumentiAllegato, StatoOperativoElencoDocumenti stato) {
		String methodName = "aggiornaStatoOperativoElencoDocumenti";
		log.info(methodName, "stato ElencoDocumentiAllegato.uid = " + elencoDocumentiAllegato.getUid() + " da impostare a " + stato);
		elencoDocumentiAllegatoDad.aggiornaStatoElencoDocumentiAllegato(elencoDocumentiAllegato.getUid(), stato);
	}

	//-----------------------------------------------------------
	
	
	/**
	 * Carica il subdocumento con dati di base.
	 *
	 * @param subdocReq the subdoc req
	 * @return the subdocumento
	 */
	private Subdocumento<?, ?> caricaSubdocumentoConDatiDiBase(Subdocumento<?, ?> subdocReq) {
		Subdocumento<?, ?> subdoc = null;
		String logMsg = "";
		if(subdocReq instanceof SubdocumentoSpesa){
			subdoc = subdocumentoSpesaDad.findSubdocumentoSpesaBaseById(subdocReq.getUid());
			logMsg = "Spesa";
		}else if(subdocReq instanceof SubdocumentoEntrata){
			subdoc = subdocumentoEntrataDad.findSubdocumentoEntrataBaseById(subdocReq.getUid());
			logMsg = "Entrata";
		} else {
			throw new IllegalArgumentException("Tipo di Subdocumento non supportato.");
		}
		
		if(subdoc==null) {
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Subdocumento di "+ logMsg, "uid: "+ subdocReq.getUid()));
		}
		
		return subdoc;
		
	}
	
	/**
	 * Ottiene i dati dell'elenco.
	 *
	 * @param uid the uid
	 * @return the elenco documenti allegato
	 */
	protected ElencoDocumentiAllegato caricaElencoDocumentiAllegato(Integer uid) {
		final String methodName = "caricaElencoDocumentiAllegato";
		
		ElencoDocumentiAllegato eda = elencoDocumentiAllegatoDad.findElencoDocumentiAllegatoById(uid);
		if(eda == null) {
			log.debug(methodName, "Nessun elenco documenti con uid " + uid + " presente in archivio");
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("elenco docuementi ", "uid: "+ uid));
		}
		return eda;
	}
	

	/**
	 * Aggiorna lo StatoOperativoAllegatoAtto per l'AllegatoAtto passato come parametro.
	 *
	 * @param aa the allegatoAtto
	 * @param stato the stato
	 */
	private void aggiornaStatoOperativoAllegatoAtto(AllegatoAtto aa, StatoOperativoAllegatoAtto stato) {
		String methodName = "aggiornaStatoOperativoAllegatoAtto";
		log.info(methodName, "stato AllegatoAtto.uid = " + aa.getUid() + " da impostare a " + stato);
		allegatoAttoDad.aggiornaStatoAllegatoAtto(aa.getUid(), stato);
	}
	
}

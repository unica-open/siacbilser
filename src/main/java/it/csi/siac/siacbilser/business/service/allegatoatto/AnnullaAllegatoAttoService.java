/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.allegatoatto;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.documentoentrata.AnnullaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.EliminaQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentospesa.AnnullaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.EliminaQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazioneResponse;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaAllegatoAttoService extends AllegatoAttoBaseService<AnnullaAllegatoAtto,AnnullaAllegatoAttoResponse> {
	
	private final Date now = new Date();
	
	@Autowired
	protected LiquidazioneService liquidazioneService;
	@Autowired
	protected EliminaQuotaDocumentoSpesaService eliminaQuotaDocumentoSpesaService;
	@Autowired
	protected EliminaQuotaDocumentoEntrataService eliminaQuotaDocumentoEntrataService;
	@Autowired
	private AnnullaDocumentoSpesaService annullaDocumentoSpesaService;
	@Autowired
	private AnnullaDocumentoEntrataService annullaDocumentoEntrataService;
	
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	@Autowired
	private BilancioDad bilancioDad;
	
	private Bilancio bilancio;
	
	
	@Override
	@Transactional
	public AnnullaAllegatoAttoResponse executeService(AnnullaAllegatoAtto serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		this.allegatoAtto = req.getAllegatoAtto();
		
		
		checkNotNull(allegatoAtto, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("allegato atto"));
		checkCondition(allegatoAtto.getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid allegato atto"), false);
		
		checkNotNull(allegatoAtto.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente allegato atto"));
		checkCondition(allegatoAtto.getEnte().getUid() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente allegato atto"), false);
		
		checkEntita(req.getBilancio(), "Bilancio");
		
		this.ente = allegatoAtto.getEnte();
				
	}
	
	@Override
	protected void init() {
		super.init();
		
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoSpesaDad.setEnte(ente);
	}

	@Override
	protected void execute() {
		caricaAllegatoAtto();
		caricaBilancio();
		checkStatoOperativoAllegatoAttoDaCompletare();
		
		aggiornaStatoOperativoAllegatoAtto(StatoOperativoAllegatoAtto.ANNULLATO);
		eliminazioneDatiLiquidazioneDocumento();
		scollegamentoQuote();
	}

	private void caricaBilancio() {
		final int uidBilancio = req.getBilancio().getUid();
		bilancio = bilancioDad.getBilancioByUid(uidBilancio);
		if(bilancio == null) {
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("Bilancio", "uid: " + uidBilancio));
		}
	}

	/**
	 * Eliminazione dei dati delle liquidazioni
	 */
	private void eliminazioneDatiLiquidazioneDocumento() {
		eliminazioneDocumentiTipoNonAllegatoAtto();
		eliminazioneDocumentiTipoAllegatoAtto();
	}

	/**
	 * Scollega le quote dall'allegato.
	 */
	private void scollegamentoQuote() {
		allegatoAttoDad.scollegaElenchiDaAllegato(allegatoAtto, now);
//		non dovrei scollegare le quote dall'elenco
		for(ElencoDocumentiAllegato elenco : allegatoAtto.getElenchiDocumentiAllegato()){
			allegatoAttoDad.scollegaQuoteDaElenco(elenco, now);
		}
		
	}
	
	/**
	 * <ul>
	 *     <li>Annullamento della liquidazione abbinata se presente</li>
	 *     <li>Eliminazione della quota documento se possibile (ad es. se non &egrave; l'unica quota del documento);</li>
	 *     <li>Annullamento del documento associato alla quota solo se la quota che si sta eliminando &eacute; l'unica quota associata al documento.</li>
	 * </ul>
	 */
	private void eliminazioneDocumentiTipoAllegatoAtto() {
		// Annullamento della liquidazione abbinata se presente
		List<Liquidazione> liquidazioni = allegatoAttoDad.findLiquidazioneDiQuoteAllegatoByAllegatoAtto(allegatoAtto);
		for(Liquidazione l : liquidazioni) {
			annullaLiquidazione(l);
		}
		// Elimino il legame tra le quote e l'atto amministrativo
		allegatoAttoDad.eliminaLegameQuoteAllegato(allegatoAtto);
		
		Map<Integer, Collection<Integer>> uidDocumentiSpesa = new HashMap<Integer, Collection<Integer>>();
		Map<Integer, Collection<Integer>> uidDocumentiEntrata = new HashMap<Integer, Collection<Integer>>();
		// Eliminazione della quota documento se possibile
		List<SubdocumentoSpesa> subdocumentiSpesa = allegatoAttoDad.findSubdocumentiSpesaTipoAllegatoByAllegatoAtto(allegatoAtto);
		for(SubdocumentoSpesa ss : subdocumentiSpesa) {
			eliminaQuotaSpesa(ss);
			
			Integer uidDocumentoSpesa = ss.getDocumento().getUid();
			Collection<Integer> uidDocumento = uidDocumentiSpesa.get(uidDocumentoSpesa);
			if(uidDocumento == null) {
				uidDocumento = new HashSet<Integer>();
				uidDocumentiSpesa.put(uidDocumentoSpesa, uidDocumento);
			}
			uidDocumento.add(ss.getUid());
		}
		List<SubdocumentoEntrata> subdocumentiEntrata = allegatoAttoDad.findSubdocumentiEntrataTipoAllegatoByAllegatoAtto(allegatoAtto);
		for(SubdocumentoEntrata se : subdocumentiEntrata) {
			eliminaQuotaEntrata(se);
			
			Integer uidDocumentoEntrata = se.getDocumento().getUid();
			Collection<Integer> uidDocumento = uidDocumentiEntrata.get(uidDocumentoEntrata);
			if(uidDocumento == null) {
				uidDocumento = new HashSet<Integer>();
				uidDocumentiEntrata.put(uidDocumentoEntrata, uidDocumento);
			}
			uidDocumento.add(se.getUid());
		}
		
		// Annullamento del documento associato alla quota solo se la quota che si sta eliminando é l'unica quota associata al documento.
		for(Map.Entry<Integer, Collection<Integer>> entry : uidDocumentiSpesa.entrySet()) {
			annullaDocumentoSpesaSeApplicabile(entry.getKey(), entry.getValue());
		}
		for(Map.Entry<Integer, Collection<Integer>> entry : uidDocumentiEntrata.entrySet()) {
			annullaDocumentoEntrataSeApplicabile(entry.getKey(), entry.getValue());
		}
	}

	
	

	private void annullaDocumentoSpesaSeApplicabile(Integer uidDocumento, Collection<Integer> uidSubdocConsiderati) {
		final String methodName = "annullaSubdocumentoSpesaSeApplicabile";
		List<Integer> uids = subdocumentoSpesaDad.findUidSubdocumentiByUidDocumento(uidDocumento);
		// Se ho pie' di un uid, esco
		if(uids.isEmpty() || uids.size() > 1) {
			log.debug(methodName, "Documento " + uidDocumento + " con " + uids.size() + " subdocumenti associati. Non posso annullare");
			return;
		}
		// Ce n'e' ancora solo uno. Controllo che sia tra quelli che ho provato ad annullare
		Integer uid = uids.get(0);
		boolean uidContenuto = uidSubdocConsiderati.contains(uid);
		if(!uidContenuto) {
			log.debug(methodName, "Documento " + uidDocumento + " contiene una quota con uid " + uid + " che non ho tentato di annullare io. Non posso annullare");
			return;
		}
		annullaDocumentoSpesa(uidDocumento);
	}
	
	private void annullaDocumentoSpesa(Integer uidDocumento) {
		AnnullaDocumentoSpesa request = new AnnullaDocumentoSpesa();
		request.setDataOra(now);
		request.setRichiedente(req.getRichiedente());
		request.setBilancio(bilancio);
		
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(uidDocumento);
		request.setDocumentoSpesa(documentoSpesa);
		
		serviceExecutor.executeServiceSuccessTxRequiresNew(annullaDocumentoSpesaService, request);
		subdocumentoSpesaDad.cancellaQuoteDocAnnullato(uidDocumento);
	}

	private void annullaDocumentoEntrataSeApplicabile(Integer uidDocumento, Collection<Integer> uidSubdocConsiderati) {
		final String methodName = "annullaSubdocumentoEntrataSeApplicabile";
		List<Integer> uids = subdocumentoEntrataDad.findUidSubdocumentiByUidDocumento(uidDocumento);
		// Se ho pie' di un uid, esco
		if(uids.isEmpty() || uids.size() > 1) {
			log.debug(methodName, "Documento " + uidDocumento + " con " + uids.size() + " subdocumenti associati. Non posso annullare");
			return;
		}
		// Ce n'e' ancora solo uno. Controllo che sia tra quelli che ho provato ad annullare
		Integer uid = uids.get(0);
		boolean uidContenuto = uidSubdocConsiderati.contains(uid);
		if(!uidContenuto) {
			log.debug(methodName, "Documento " + uidDocumento + " contiene una quota con uid " + uid + " che non ho tentato di annullare io. Non posso annullare");
			return;
		}
		annullaDocumentoEntrata(uidDocumento);		
	}
	
	private void annullaDocumentoEntrata(Integer uidDocumento) {
		AnnullaDocumentoEntrata request = new AnnullaDocumentoEntrata();
		request.setDataOra(now);
		request.setRichiedente(req.getRichiedente());
		request.setBilancio(bilancio);
		
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(uidDocumento);
		request.setDocumentoEntrata(documentoEntrata);
		
		serviceExecutor.executeServiceSuccessTxRequiresNew(annullaDocumentoEntrataService, request);
		subdocumentoSpesaDad.cancellaQuoteDocAnnullato(uidDocumento);
	}

	/**
	 * <ul>
	 *     <li>Annullamento della liquidazione abbinata se presente ed eliminazione del legame con la quota ad essa abbinata;</li>
	 *     <li>Eliminazione del legame tra l'Allegato Atto e ogni quota ad esso collegata.</li>
	 * </ul>
	 */
	private void eliminazioneDocumentiTipoNonAllegatoAtto() {
		// Annullamento della liquidazione abbinata se presente ed eliminazione del legame con la quota ad essa abbinata
		List<Liquidazione> liquidazioni = allegatoAttoDad.findLiquidazioneDiQuoteNonAllegatoByAllegatoAtto(allegatoAtto);
		for(Liquidazione l : liquidazioni) {
			annullaLiquidazione(l);
			// Sono solo sul subdocumento di spesa
			subdocumentoSpesaDad.eliminaLegameLiquidazioneQuota(l, now);
		}
		
		// Eliminazione del legame tra l'Allegato Atto e ogni quota ad esso collegata
		// Cancellare il provvedimento dalla quota
		allegatoAttoDad.eliminaLegameQuoteNonAllegato(allegatoAtto);
	}
	
	

	/**
	 * Annulla la liquidazione.
	 * 
	 * @param l la liquidazione da annullare
	 */
	protected void annullaLiquidazione(Liquidazione l) {
		AnnullaLiquidazione requestAnnullaLiquidazione = new AnnullaLiquidazione();
		requestAnnullaLiquidazione.setDataOra(now);
		requestAnnullaLiquidazione.setEnte(allegatoAtto.getEnte());
		requestAnnullaLiquidazione.setRichiedente(req.getRichiedente());
		requestAnnullaLiquidazione.setLiquidazioneDaAnnullare(l);
		requestAnnullaLiquidazione.setAnnoEsercizio(String.valueOf(bilancio.getAnno()));
		
		AnnullaLiquidazioneResponse responseAnnullaLiquidazione = liquidazioneService.annullaLiquidazione(requestAnnullaLiquidazione);
		checkServiceResponseFallimento(responseAnnullaLiquidazione);
	}
	
	/**
	 * Eliminazione della quota di spesa.
	 * 
	 * @param subdocumentoSpesa
	 */
	protected void eliminaQuotaSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		EliminaQuotaDocumentoSpesa request = new EliminaQuotaDocumentoSpesa();
		request.setDataOra(now);
		request.setAggiornaStatoDocumento(false);
		request.setRichiedente(req.getRichiedente());
		request.setBilancio(bilancio);
		request.setSubdocumentoSpesa(subdocumentoSpesa);
		
		serviceExecutor.executeServiceSuccessTxRequiresNew(eliminaQuotaDocumentoSpesaService, request, ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getCodice());
	}
	
	/**
	 * Eliminazione della quota di entrata.
	 * 
	 * @param subdocumentoEntrata
	 */
	protected void eliminaQuotaEntrata(SubdocumentoEntrata subdocumentoEntrata) {
		EliminaQuotaDocumentoEntrata request = new EliminaQuotaDocumentoEntrata();
		request.setDataOra(now);
		request.setAggiornaStatoDocumento(false);
		request.setRichiedente(req.getRichiedente());
		request.setBilancio(bilancio);
		request.setSubdocumentoEntrata(subdocumentoEntrata);
		
		serviceExecutor.executeServiceSuccessTxRequiresNew(eliminaQuotaDocumentoEntrataService, request, ErroreFin.QUOTA_DOCUMENTO_CHE_NON_SI_PUO_ELIMINARE.getCodice());
	}

}

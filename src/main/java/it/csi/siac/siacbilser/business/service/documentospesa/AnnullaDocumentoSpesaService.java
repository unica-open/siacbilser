/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.BilancioDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazioneResponse;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * The Class AnnullaDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaDocumentoSpesaService extends CheckedAccountBaseService<AnnullaDocumentoSpesa, AnnullaDocumentoSpesaResponse> {
	
	/** The documento spesa dad. */
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private SubdocumentoDad subdocumentoDad;
	@Autowired
	private PreDocumentoSpesaDad predocumentoSpesaDad;
	@Autowired
	private BilancioDad bilancioDad;
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	
	/** The documento. */
	private DocumentoSpesa documento;
	
	/** The ricerca quote by documento spesa service. */
	@Autowired
	private RicercaQuoteByDocumentoSpesaService ricercaQuoteByDocumentoSpesaService;
	
	@Autowired
	private LiquidazioneService liquidazioneService;
	
//	private AnnullaLiquidazione
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		documento = req.getDocumentoSpesa();
		
		checkNotNull(documento, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento spesa"));
		checkCondition(documento.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento spesa"));
		
		checkEntita(req.getBilancio(), "bilancio");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		predocumentoSpesaDad.setLoginOperazione(loginOperazione);

//		registrazioneMovFinDad.setEnte(ente);
//		registrazioneMovFinDad.setLoginOperazione(loginOperazione);
//		
//		bilancioDad.setEnteEntity(ente);
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AnnullaDocumentoSpesaResponse executeService(AnnullaDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		caricaDocumentoESubdocumenti();
		checkDocumentoAnnullabile();
		
		annullaLiquidazioni();
		sganciaDaElenco();
		annullaPredocumentoSePresente();
		annullaRegistrazioneMovFinEPrimaNotaSePresente();
		StatoOperativoDocumento statoOperativoDocumento = StatoOperativoDocumento.ANNULLATO;
		documentoSpesaDad.aggiornaStatoDocumentoSpesa(documento.getUid(), statoOperativoDocumento);
		documento.setStatoOperativoDocumento(statoOperativoDocumento);
		res.setDocumentoSpesa(documento);

	}

	private void caricaDocumentoESubdocumenti() {
		DocumentoSpesa doc = documentoSpesaDad.findDocumentoSpesaByIdAfterFlushAndClear(documento.getUid());
		if(doc==null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("documento spesa", "id: "+ documento.getUid()));
		}
		documento = doc;
		RicercaQuoteByDocumentoSpesa reqRQ = new RicercaQuoteByDocumentoSpesa();
		reqRQ.setRichiedente(req.getRichiedente());
		reqRQ.setDocumentoSpesa(documento);

		RicercaQuoteByDocumentoSpesaResponse resRQ = executeExternalService(ricercaQuoteByDocumentoSpesaService, reqRQ);
		
		List<SubdocumentoSpesa> listaSubdocumenti = resRQ.getSubdocumentiSpesa();
		
		documento.setListaSubdocumenti(listaSubdocumenti);
	}



	private void checkDocumentoAnnullabile() {
		final String methodName = "checkDocumentoAnnullabile";
		
		List<DocumentoEntrata> listaDocEntrata = documento.getListaDocumentiEntrataFiglio();
		List<DocumentoSpesa> listaDocSpesa = documento.getListaDocumentiSpesaFiglio();
		List<DocumentoSpesa> listaNoteCredito = documento.getListaNoteCreditoSpesaFiglio();
		List<SubdocumentoSpesa> listaQuote = documento.getListaSubdocumenti();
		
		if( !StatoOperativoDocumento.INCOMPLETO.equals(documento.getStatoOperativoDocumento()) &&
				!StatoOperativoDocumento.VALIDO.equals(documento.getStatoOperativoDocumento()) &&
				!StatoOperativoDocumento.LIQUIDATO.equals(documento.getStatoOperativoDocumento()) &&
				!StatoOperativoDocumento.PARZIALMENTE_LIQUIDATO.equals(documento.getStatoOperativoDocumento())
					){
				
				log.debug(methodName, "stato operativo incongruente" + documento.getStatoOperativoDocumento() );
				throw new BusinessException(ErroreFin.DOCUMENTO_CHE_NON_SI_PUO_ANNULLARE.getErrore(""), Esito.FALLIMENTO);
		}
		
		if(listaDocEntrata != null){
			for(DocumentoEntrata de : listaDocEntrata){
				if(!StatoOperativoDocumento.ANNULLATO.equals(de.getStatoOperativoDocumento())){
					log.debug(methodName, "documento entrata collegato, uid: " + de.getUid());
					throw new BusinessException(ErroreFin.DOCUMENTO_CHE_NON_SI_PUO_ANNULLARE.getErrore(""), Esito.FALLIMENTO);
					
				}
			}
		}
		
		if(listaDocSpesa != null){
			for(DocumentoSpesa ds : listaDocSpesa){
				if(!StatoOperativoDocumento.ANNULLATO.equals(ds.getStatoOperativoDocumento())){
					log.debug(methodName, "documento spesa collegato, uid: " + ds.getUid());
					throw new BusinessException(ErroreFin.DOCUMENTO_CHE_NON_SI_PUO_ANNULLARE.getErrore(""), Esito.FALLIMENTO);
					
				}
			}
		}
		
		if(listaNoteCredito != null){
			for(DocumentoSpesa ds : listaNoteCredito){
				if(!StatoOperativoDocumento.ANNULLATO.equals(ds.getStatoOperativoDocumento())){
					log.debug(methodName, "nota credito collegata, uid: " + ds.getUid());
					throw new BusinessException(ErroreFin.DOCUMENTO_CHE_NON_SI_PUO_ANNULLARE.getErrore(""), Esito.FALLIMENTO);
					
				}
			}
		}
		
		if(listaQuote != null){
			for(SubdocumentoSpesa sds : listaQuote){
				if( sds.getNumeroRegistrazioneIVA()!= null && !sds.getNumeroRegistrazioneIVA().isEmpty()){
					log.debug(methodName, "quota con numero registrazione valorizzato, uid: " + sds.getUid());
					throw new BusinessException(ErroreFin.DOCUMENTO_CHE_NON_SI_PUO_ANNULLARE.getErrore(""), Esito.FALLIMENTO);
				}
			}
		}
		
	}
	

	private void annullaLiquidazioni() {
		if(documento.getListaSubdocumenti() != null){
			for(SubdocumentoSpesa sds : documento.getListaSubdocumenti()){
				if( sds.getLiquidazione() != null){
					annullaLiquidazione(sds.getLiquidazione());
				}
			}
		}
		
	}
	
	private void annullaLiquidazione(Liquidazione liquidazione) {
		Bilancio bilancio = bilancioDad.getBilancioByUid(req.getBilancio().getUid());
		AnnullaLiquidazione annullaLiquidazione = new AnnullaLiquidazione();
		annullaLiquidazione.setLiquidazioneDaAnnullare(liquidazione);
		annullaLiquidazione.setEnte(documento.getEnte());
		annullaLiquidazione.setRichiedente(req.getRichiedente());
		annullaLiquidazione.setDataOra(req.getDataOra());
		annullaLiquidazione.setAnnoEsercizio(String.valueOf(bilancio.getAnno()));
		
		AnnullaLiquidazioneResponse annullaLiquidazioneResponse = liquidazioneService.annullaLiquidazione(annullaLiquidazione);
		log.logXmlTypeObject(annullaLiquidazioneResponse, "Risposta ottenuta dal servizio AnnullaLiquidazione.");
		checkServiceResponseFallimento(annullaLiquidazioneResponse);
	}
	
	
	private void sganciaDaElenco() {
		if(documento.getListaSubdocumenti() != null){
			for(SubdocumentoSpesa sds : documento.getListaSubdocumenti()){
				if(sds.getElencoDocumenti() != null && sds.getElencoDocumenti().getUid() != 0){
					//sgancio la quota dall'elenco
					elencoDocumentiAllegatoDad.disassociaQuotaDaElenco(sds.getElencoDocumenti(), sds);
					if(sds.getElencoDocumenti().getAllegatoAtto() != null && sds.getElencoDocumenti().getAllegatoAtto().getUid() != 0){
						//se era collegato ad un allegato atto, cancello anche il legame con il provvedimento
						subdocumentoDad.cancellaRelazioniSubdocumentoAttoAmm(sds);
					}
					sds.setElencoDocumenti(null);
					sds.setAttoAmministrativo(null);
				}
			}
		}
	}

	
	/**
	 * Nel caso in cui il documento fosse collegato ad un predocumento  è necessario ripristinare lo 
	 * stato corretto del predocumento associato facendolo ritornare a COMPLETO, richiamando il 
	 * servizio Aggiorna PreDocumento del servizio Gestione predocumento Entrata.
	 */
	private void annullaPredocumentoSePresente() {
		for(SubdocumentoSpesa subdoc : documento.getListaSubdocumenti()){
			aggiornaStatoPredocumento(subdoc.getUid());
			eliminaLegameSubdocPredoc(subdoc.getUid());
		}
	}
	
	private void aggiornaStatoPredocumento(int uidSubdoc) {
		predocumentoSpesaDad.aggiornaStatoPreDocumentiAssociatiAlSubdocumento(uidSubdoc, StatoOperativoPreDocumento.COMPLETO);
	}

	private void eliminaLegameSubdocPredoc(int uidSubdoc) {
		subdocumentoDad.eliminaLegameSubdocPredoc(uidSubdoc);
	}

	/**
	 * Se esiste per il documento da annullare una richiesta di registrazione prima nota nel registro 
	 * (entità RegistrazioneMovFin) in stato diverso da 'ANNULLATO' richiamare il servizio Annulla Prima Nota del modulo GEN, 
	 * altrimenti proseguire senza richiamare questo servizio.
	 */
	private void annullaRegistrazioneMovFinEPrimaNotaSePresente() {
//		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, documento.getAnno());
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, req.getBilancio());
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.SUBDOCUMENTO_SPESA,documento); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin);//se la registrazione esisteva devo annullare le eventuali primeNote associate
		
	}

}

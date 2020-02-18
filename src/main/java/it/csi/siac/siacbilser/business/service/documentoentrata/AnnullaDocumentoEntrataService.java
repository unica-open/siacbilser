/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RegistrazioneGENServiceHelper;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.ElencoDocumentiAllegatoDad;
import it.csi.siac.siacbilser.integration.dad.PreDocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrataResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * The Class AnnullaDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaDocumentoEntrataService extends CheckedAccountBaseService<AnnullaDocumentoEntrata, AnnullaDocumentoEntrataResponse> {
	
	/** The documento entrata dad. */
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	private SubdocumentoDad subdocumentoDad;
	@Autowired
	private PreDocumentoEntrataDad predocumentoEntrataDad;
	@Autowired
	private ElencoDocumentiAllegatoDad elencoDocumentiAllegatoDad;
	@Autowired
	private RegistrazioneGENServiceHelper registrazioneGENServiceHelper;
	
	/** The documento. */
	private DocumentoEntrata documento;
	
	@Autowired
	private RicercaQuoteByDocumentoEntrataService ricercaQuoteByDocumentoEntrataService;
	
	@Autowired
	private MovimentoGestioneService movimentoGestioneService;
	
//	/** The aggiorna stato documento di entrata service. */
//	@Autowired
//	private AggiornaStatoDocumentoDiEntrataService aggiornaStatoDocumentoDiEntrataService;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		documento = req.getDocumentoEntrata();
		
		checkNotNull(documento, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento entrata"));
		checkCondition(documento.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento entrata"));
		
		checkEntita(req.getBilancio(), "bilancio");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoEntrataDad.setLoginOperazione(loginOperazione);
		predocumentoEntrataDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AnnullaDocumentoEntrataResponse executeService(AnnullaDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		caricaDocumentoESubdocumenti();
		checkDocumentoAnnullabile();
		
		annullaAccertamentoAutomatico();
		sganciaDaElenco();
		annullaPredocumentoSePresente();
		annullaRegistrazioneMovFinEPrimaNotaSePresente();
		
		StatoOperativoDocumento statoOperativoDocumento = StatoOperativoDocumento.ANNULLATO;
		documentoEntrataDad.aggiornaStatoDocumentoEntrata(documento.getUid(), statoOperativoDocumento);
		documento.setStatoOperativoDocumento(statoOperativoDocumento);
		res.setDocumentoEntrata(documento);

	}
	
	
	private void caricaDocumentoESubdocumenti() {
		DocumentoEntrata doc = documentoEntrataDad.findDocumentoEntrataById(documento.getUid());
		if(doc==null){
			throw new BusinessException(ErroreCore.ENTITA_NON_TROVATA.getErrore("documento entrata", "id: "+documento.getUid()));
		}
		documento = doc;
		RicercaQuoteByDocumentoEntrata reqRQ = new RicercaQuoteByDocumentoEntrata();
		reqRQ.setRichiedente(req.getRichiedente());
		reqRQ.setDocumentoEntrata(documento);

		RicercaQuoteByDocumentoEntrataResponse resRQ = executeExternalService(ricercaQuoteByDocumentoEntrataService, reqRQ);
		
		List<SubdocumentoEntrata> listaSubdocumenti = resRQ.getSubdocumentiEntrata();
		
		documento.setListaSubdocumenti(listaSubdocumenti);
	}
	
	
	private void checkDocumentoAnnullabile() {
		final String methodName = "checkDocumentoAnnullabile";
		
		List<DocumentoEntrata> listaDocEntrata = documento.getListaDocumentiEntrataFiglio();
		List<DocumentoSpesa> listaDocSpesa = documento.getListaDocumentiSpesaFiglio();
		List<DocumentoEntrata> listaNoteCredito = documento.getListaNoteCreditoEntrataFiglio();
		List<SubdocumentoEntrata> listaQuote = documento.getListaSubdocumenti();
		
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
			for(DocumentoEntrata ds : listaNoteCredito){
				if(!StatoOperativoDocumento.ANNULLATO.equals(ds.getStatoOperativoDocumento())){
					log.debug(methodName, "nota credito collegata, uid: " + ds.getUid());
					throw new BusinessException(ErroreFin.DOCUMENTO_CHE_NON_SI_PUO_ANNULLARE.getErrore(""), Esito.FALLIMENTO);
					
				}
			}
		}
		
		if(listaQuote != null){
			for(SubdocumentoEntrata sds : listaQuote){
				if( sds.getNumeroRegistrazioneIVA()!= null && !sds.getNumeroRegistrazioneIVA().isEmpty()){
					log.debug(methodName, "quota con numero registrazione valorizzato, uid: " + sds.getUid());
					throw new BusinessException(ErroreFin.DOCUMENTO_CHE_NON_SI_PUO_ANNULLARE.getErrore(""), Esito.FALLIMENTO);
				}
			}
		}
		
	}
	
	/**
	 * Inoltre se presente deve annullare ciascun accertamento ‘automatico’ associato alle sue quote di documento 
	 * richiamando il metodo Annulla accertamento del servizio Gestione accertamento. 
	 */
	private void annullaAccertamentoAutomatico() {
		for(SubdocumentoEntrata subdocEntrata : documento.getListaSubdocumenti()){
			if(subdocEntrata.getAccertamento() != null && subdocEntrata.getAccertamento().isAutomatico()){
				annullaMovimentoEntrata(subdocEntrata.getAccertamento());
			}
		}
	}
	
	private void sganciaDaElenco() {
		if(documento.getListaSubdocumenti() != null){
			for(SubdocumentoEntrata sde : documento.getListaSubdocumenti()){
				if(sde.getElencoDocumenti() != null && sde.getElencoDocumenti().getUid() != 0){
					//sgancio la quota dall'elenco
					elencoDocumentiAllegatoDad.disassociaQuotaDaElenco(sde.getElencoDocumenti(), sde);
					if(sde.getElencoDocumenti().getAllegatoAtto() != null && sde.getElencoDocumenti().getAllegatoAtto().getUid() != 0){
						//se era collegato ad un allegato atto, cancello anche il legame con il provvedimento
						subdocumentoDad.cancellaRelazioniSubdocumentoAttoAmm(sde);
					}
					sde.setElencoDocumenti(null);
					sde.setAttoAmministrativo(null);
				}
			}
		}
	}

	private void annullaMovimentoEntrata(Accertamento accertamento) {
		AnnullaMovimentoEntrata reqAME = new AnnullaMovimentoEntrata();
		reqAME.setAccertamento(accertamento);
		reqAME.setBilancio(req.getBilancio());
		reqAME.setEnte(ente);
		reqAME.setRichiedente(req.getRichiedente());
		AnnullaMovimentoEntrataResponse resAME = movimentoGestioneService.annullaMovimentoEntrata(reqAME);
		checkServiceResponseFallimento(resAME);
	}
	
	/**
	 * Nel caso in cui il documento fosse collegato ad un predocumento  è necessario ripristinare lo 
	 * stato corretto del predocumento associato facendolo ritornare a COMPLETO, richiamando il 
	 * servizio Aggiorna PreDocumento del servizio Gestione predocumento Entrata.
	 */
	private void annullaPredocumentoSePresente() {
		for(SubdocumentoEntrata subdoc : documento.getListaSubdocumenti()){
			aggiornaStatoPredocumento(subdoc.getUid());
			eliminaLegameSubdocPredoc(subdoc.getUid());
		}
	}
	
	private void aggiornaStatoPredocumento(int uidSubdoc) {
		predocumentoEntrataDad.aggiornaStatoPreDocumentiAssociatiAlSubdocumento(uidSubdoc, StatoOperativoPreDocumento.COMPLETO);
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
		registrazioneGENServiceHelper.init(serviceExecutor, ente, req.getRichiedente(), loginOperazione, req.getBilancio());
		
		List<RegistrazioneMovFin> registrazioniMovFin = registrazioneGENServiceHelper.ricercaRegistrazioniMovFinAssociateAlMovimento(TipoCollegamento.SUBDOCUMENTO_ENTRATA,documento); //se presenti ne troverà una per ogni quota, altrimenti 0.
		
		registrazioneGENServiceHelper.annullaRegistrazioniMovFinEPrimeNote(registrazioniMovFin);//se la registrazione esisteva devo annullare le eventuali primeNote associate
		
	}
	
	/**
	 * Aggiorna stato operativo documento.
	 *
	 * @return the stato operativo documento
	 */
	/*
	private StatoOperativoDocumento aggiornaStatoOperativoDocumento() {
		AggiornaStatoDocumentoDiEntrata reqAs = new AggiornaStatoDocumentoDiEntrata();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setDocumentoEntrata(documento);
		AggiornaStatoDocumentoDiEntrataResponse resAs = executeExternalService(aggiornaStatoDocumentoDiEntrataService, reqAs);
		return resAs.getDocumentoEntrata().getStatoOperativoDocumento();
	}
	*/
	
}

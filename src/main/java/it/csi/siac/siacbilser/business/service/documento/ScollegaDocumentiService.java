/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDRelazTipoEnum;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRelazioneDocumenti;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRelazioneDocumentiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.TipoRelazione;

// TODO: Auto-generated Javadoc
/**
 *  Scollega due documenti
 *
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ScollegaDocumentiService extends CheckedAccountBaseService<AggiornaRelazioneDocumenti, AggiornaRelazioneDocumentiResponse> {
	
	@Autowired
	private AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaService;
	@Autowired
	private AggiornaStatoDocumentoDiEntrataService aggiornaStatoDocumentoDiEntrataService;
	
	@Autowired
	private DocumentoDad documentoDad;
	
	private Documento<?,?> docFiglio;
	private Documento<?,?> docPadre;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getDocFiglio(), "documento figlio");
		checkEntita(req.getDocPadre(), "documento padre");
		checkEntita(req.getBilancio(), "bilancio");
		checkNotNull(req.getTipoRelazione(), "tipo relazione");
		
		docFiglio = req.getDocFiglio();
		docPadre = req.getDocPadre();
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoDad.setLoginOperazione(loginOperazione);
		documentoDad.setEnte(ente);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AggiornaRelazioneDocumentiResponse executeService(AggiornaRelazioneDocumenti serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		documentoDad.scollegaDocumenti(docFiglio, docPadre, req.getTipoRelazione());
		
		if(TipoRelazione.NOTA_CREDITO.equals(req.getTipoRelazione())){
			aggiornaStatoOperativoDocumento(docPadre);
			documentoDad.aggiornaStatoDocumento(docFiglio.getUid(), determinaNuovoStato());
		}
		
		popolaDatiPerResponse();
		res.setDocFiglio(docFiglio);
		res.setDocPadre(docPadre);
		
	}
	
	
	private StatoOperativoDocumento determinaNuovoStato() {
		String methodName = "determinaNuovoStato";
		Long documentiPadreNotaCredito = documentoDad.countDocumentiPadre(docFiglio.getUid(), SiacDRelazTipoEnum.byTipoRelazione(TipoRelazione.NOTA_CREDITO)); 
		log.debug(methodName, "documentiPadreNotaCredito: " + documentiPadreNotaCredito);
		if(Long.valueOf(0L).compareTo(documentiPadreNotaCredito) < 0) {
			log.debug(methodName, "la nota e' collegata ad altri documenti, lo stato resta valido");
			return StatoOperativoDocumento.VALIDO;
		}else{
			log.debug(methodName, "ho scollegato l'ultimo documento, lo stato torna incompleto");
			return StatoOperativoDocumento.INCOMPLETO;
		}
	}

	private void popolaDatiPerResponse() {
		if(docPadre instanceof DocumentoSpesa){
			docFiglio.getListaDocumentiSpesaPadre().remove((DocumentoSpesa)docPadre);
		}else{
			docFiglio.getListaDocumentiEntrataPadre().remove((DocumentoEntrata)docPadre);
		}
		if(docFiglio instanceof DocumentoSpesa){
			docPadre.getListaDocumentiSpesaFiglio().remove((DocumentoSpesa)docFiglio);
		}else{
			docPadre.getListaDocumentiEntrataFiglio().remove((DocumentoEntrata)docFiglio);
		}
	}
	
	private void aggiornaStatoOperativoDocumento(Documento<?, ?> documento) {
		if(documento instanceof DocumentoSpesa){
			aggiornaStatoOperativoDocumentoSpesa((DocumentoSpesa)documento);
		}else{
			aggiornaStatoOperativoDocumentoEntrata((DocumentoEntrata)documento);
		}
		
	}
	
	private void aggiornaStatoOperativoDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		DocumentoSpesa doc = new DocumentoSpesa();
		doc.setUid(documentoSpesa.getUid());
		AggiornaStatoDocumentoDiSpesa reqAs = new AggiornaStatoDocumentoDiSpesa();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setDocumentoSpesa(doc);
		reqAs.setBilancio(req.getBilancio());
		AggiornaStatoDocumentoDiSpesaResponse resAs = executeExternalService(aggiornaStatoDocumentoDiSpesaService, reqAs);
		documentoSpesa.setStatoOperativoDocumento(resAs.getStatoOperativoDocumentoNuovo());
		// SIAC-4433
		documentoSpesa.setDataInizioValiditaStato(resAs.getDocumentoSpesa().getDataInizioValiditaStato());
	}
	
	private void aggiornaStatoOperativoDocumentoEntrata(DocumentoEntrata documentoEntrata) {
		DocumentoEntrata doc = new DocumentoEntrata();
		doc.setUid(documentoEntrata.getUid());
		AggiornaStatoDocumentoDiEntrata reqAs = new AggiornaStatoDocumentoDiEntrata();
		reqAs.setRichiedente(req.getRichiedente());
		reqAs.setBilancio(req.getBilancio());
		reqAs.setDocumentoEntrata(doc);
		AggiornaStatoDocumentoDiEntrataResponse resAs = executeExternalService(aggiornaStatoDocumentoDiEntrataService, reqAs);
		documentoEntrata.setStatoOperativoDocumento(resAs.getStatoOperativoDocumentoNuovo());
		// SIAC-4433
		documentoEntrata.setDataInizioValiditaStato(resAs.getDocumentoEntrata().getDataInizioValiditaStato());
	}
	
}

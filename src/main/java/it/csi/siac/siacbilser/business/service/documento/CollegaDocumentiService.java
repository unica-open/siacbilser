/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documento;

import java.math.BigDecimal;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
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
import it.csi.siac.siacfin2ser.model.StatoSDIDocumento;
import it.csi.siac.siacfin2ser.model.TipoRelazione;

/**
 * Collega due documenti
 *
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CollegaDocumentiService extends CheckedAccountBaseService<AggiornaRelazioneDocumenti, AggiornaRelazioneDocumentiResponse> {
	
	
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
		checkCollegamentoGiaPresente();
		
		if(TipoRelazione.NOTA_CREDITO.equals(req.getTipoRelazione())){
			//SIAC-6988 Inizio  FL Controllo che lo stato non sia con stato SDI diverso da INVIATA A FEL; ACCETTATA / CONSEGNATA; IN DECORRENZA TERMINI.
			if(checkStatoSDIFEL(documentoDad.findStatoSDIDocumento(docFiglio))){
				String msg = "la nota di accredito è già stata inviata a FEL" ;
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
			}
			//SIAC-6988 Fine  FL
			impostaImportoDaDedurreSuFattura();
			
			
			Boolean flagContabilizzaGenPccDocPadre = documentoDad.findFlagContabilizzaGenPccDocumento(docFiglio);
			if(Boolean.TRUE.equals(flagContabilizzaGenPccDocPadre)){ //aggiunto per SIAC-3531
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il documento nota credito ha gia' attive le registrazioni contabili."));
			}
			
			//checkTotaleImportoDaDedurreSuFattura(this.docFiglio);
		}
		
		documentoDad.collegaDocumenti(docFiglio, docPadre, req.getTipoRelazione());
		if(TipoRelazione.NOTA_CREDITO.equals(req.getTipoRelazione())){
			aggiornaStatoOperativoDocumento(docPadre);
			documentoDad.aggiornaStatoDocumento(docFiglio.getUid(), StatoOperativoDocumento.VALIDO);
		}
		
		popolaDatiPerResponse();
		res.setDocFiglio(docFiglio);
		res.setDocPadre(docPadre);
		
	}

	//SIAC-6988 Inizio  FL
	private  boolean checkStatoSDIFEL(String statoSDI) {
		boolean flagStatoSDI= false;
		if (statoSDI != null && !statoSDI.equals("")) {
				if (StatoSDIDocumento.DECORR_TERMINI.getCodice().equalsIgnoreCase(statoSDI)  ||
					StatoSDIDocumento.ACCET_CONSEG.getCodice().equalsIgnoreCase(statoSDI)  ||
					StatoSDIDocumento.INVIATA_FEL.getCodice().equalsIgnoreCase(statoSDI)) {
					flagStatoSDI= true; 
				}
		}
		return flagStatoSDI;
	}
	//SIAC-6988 Fine  FL
	/**
	 * Imposta l'importo da dedurre su fattura con il massimo importo disponibile per la nota di credito.
	 * Ovvero: importoNotaCredito-totaleImportoDaDedurreSuFattura
	 * Dove totaleImportoDaDedurreSuFattura rappresenta il totale importo da dedurre della nota gia' collegato ad altre fatture.
	 * 
	 * @throws BusinessException OPERAZIONE_NON_CONSENTITA se la nota di credito ha più disponibilita' da dedurre.
	 */
	private void impostaImportoDaDedurreSuFattura() { //aggiunto per CR SIAC-4409
		final String methodName = "impostaImportoDaDedurreSuFattura";
		
		BigDecimal importoNotaCredito = documentoDad.findImportoDocumento(docFiglio); //Il docFiglio e' la NCD il docPadre e' il Documento Fattura a cui sto collegando la NCD.
		docFiglio.setImporto(importoNotaCredito);
		BigDecimal totaleImportoDaDedurreSuFattura = documentoDad.calcolaTotaleImportoDaDedurreSuFattura(docFiglio.getUid());
		BigDecimal importoDaDedurreSuFattura = importoNotaCredito.subtract(totaleImportoDaDedurreSuFattura);
		docFiglio.setImportoDaDedurreSuFattura(importoDaDedurreSuFattura);
		
		log.debug(methodName, MessageFormat.format("Controllo importi:"
				+ "\ntotaleImportoDaDedurreSuFattura: {0,number,###,##0.00} €. "
				+ "\nimportoDaDedurreSuFattura: {1,number,###,##0.00} €. "
				+ "\nimportoNotaDiCredito: {2,number,###,##0.00} €", 
				totaleImportoDaDedurreSuFattura, 
				importoDaDedurreSuFattura,
				importoNotaCredito));
		//SIAC-6988 - l'importo deve essere minore di 0 perchè se il saldo è uguale a 0 significa che c'è totale copertura di importi
		if(importoDaDedurreSuFattura.compareTo(BigDecimal.ZERO) < 0){
			String msg = MessageFormat.format("Il totale importo da dedurre su fattura deve essere minore o uguale dell''importo della nota di credito. "
					+ "Totale importo da dedurre su altre fatture: {0,number,###,##0.00} €. "
					+ "Importo nota credito: {1,number,###,##0.00} €.", 
					totaleImportoDaDedurreSuFattura , importoNotaCredito);
			log.error(methodName, msg + " [notaDiCredito.uid: "+ docFiglio.getUid() + "]");
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
		}
	}

	private void popolaDatiPerResponse() {
		if(docFiglio instanceof DocumentoSpesa){
			docPadre.addDocumentoSpesaFiglio((DocumentoSpesa)docFiglio);
		}else{
			docPadre.addDocumentoEntrataFiglio((DocumentoEntrata)docFiglio);
		}
	}

	private void aggiornaStatoOperativoDocumento(Documento<?, ?> documento) {
		if(documento instanceof DocumentoSpesa){
			aggiornaStatoOperativoDocumentoSpesa((DocumentoSpesa)documento);
		}else{
			aggiornaStatoOperativoDocumentoEntrata((DocumentoEntrata)documento);
		}
		
	}

	private void checkCollegamentoGiaPresente() {
		if(isCollegamentoPresente()){
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("esiste gia' una relazione di tipo " + req.getTipoRelazione().getCodice() + " tra i documenti " + docFiglio.getUid() + " e " + docPadre.getUid()));
		}
	}

	private boolean isCollegamentoPresente() {
		return documentoDad.isRelazionePresente(docFiglio, docPadre, req.getTipoRelazione());
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

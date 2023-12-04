/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.documentospesa.InserisceDocumentoSpesaService;
import it.csi.siac.siacbilser.integration.dad.FatturaFELDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.sirfelser.model.StatoAcquisizioneFEL;

/**
 * Inserimento dell'anagrafica del Documento di Entrata .
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class InserisceDocumentoEntrataService extends CrudDocumentoDiEntrataBaseService<InserisceDocumentoEntrata, InserisceDocumentoEntrataResponse> {
		
	
	/** The inserisce quota documento entrata service. */
	@Autowired
	private InserisceQuotaDocumentoEntrataService inserisceQuotaDocumentoEntrataService;
	
	/** The inserisce documento spesa service. */
	private InserisceDocumentoSpesaService inserisceDocumentoSpesaService;
	
	@Autowired 
	private FatturaFELDad fatturaFELDad;
	
	/** The app ctx. */
	@Autowired
	private ApplicationContext appCtx;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		final String methodName = "checkServiceParam";
		
		doc = req.getDocumentoEntrata();
		
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		checkCondition(req.getBilancio().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid bilancio"));
		
		checkNotNull(doc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(doc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(doc.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento"));
		//checkNotNull(doc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		checkNotNull(doc.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione documento"));
		
		checkNotNull(doc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento"));
		
		checkNotNull(doc.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione documento"));
		
		checkNotNull(doc.getTipoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento"));
		checkCondition(doc.getTipoDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid tipo documento"));
				
//		checkNotNull(doc.getCodiceBollo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice bollo documento"));
//		checkCondition(doc.getCodiceBollo().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid codice bollo documento"));
		
		try{
			checkNotNull(doc.getListaSubdocumenti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti del documento"));
			checkCondition(doc.getListaSubdocumenti().size()==1, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti (deve esserci un solo subdocumento) del documento"));
		} catch (ServiceParamError spe) { // Se il subdocumento non viene passato 
				// ne viene inizializzato uno vuoto
			log.info(methodName, "Subdocumento passato come parametro non presente o ignorato (deve esserci un solo subdocumento)! Verrà creato un subdocumento che copre l'intero importo del documento.");
			SubdocumentoEntrata subdoc = new SubdocumentoEntrata();
			List<SubdocumentoEntrata> listaSubdocumenti = new ArrayList<SubdocumentoEntrata>();
			listaSubdocumenti.add(subdoc);
			doc.setListaSubdocumenti(listaSubdocumenti);
		}
		
		checkNotNull(doc.getSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto documento"));
		checkCondition(doc.getSoggetto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto"));
		
		
		//checkNotNull(doc.getFlagBeneficiarioMultiplo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("flag beneficiario multiplo"));
//		if(doc.getFlagBeneficiarioMultiplo()==null) { //NON obbligatorio! Default a FALSE
//			doc.setFlagBeneficiarioMultiplo(Boolean.FALSE);
//		}
		
		//checkNotNull(doc.getStatoOperativoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("stato operativo documento"));
		
//		checkNotNull(doc.getSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto documento"));
//		checkNotNull(doc.getSoggetto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("soggetto"));
//		checkCondition(doc.getSoggetto().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto"));
		
		// SIAC-3191
		checkNumero();
		checkImporto();
		checkArrotondamento();
		checkDataEmissione();
		checkDataScadenza();
		
		// SIAC-6565
		TipoDocumento tipoDoc = documentoEntrataDad.findTipoDocumentoById(doc.getTipoDocumento().getUid());
		if (("FTV".equals(tipoDoc.getCodice())) || ("NCD".equals(tipoDoc.getCodice()))) {
			checkSoggettoDocumento();
		}
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoEntrataDad.setLoginOperazione(loginOperazione);
		documentoEntrataDad.setEnte(doc.getEnte());
		fatturaFELDad.setLoginOperazione(loginOperazione);
		fatturaFELDad.setEnte(ente);
		
		inserisceDocumentoSpesaService = appCtx.getBean("inserisceDocumentoSpesaService", InserisceDocumentoSpesaService.class);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public InserisceDocumentoEntrataResponse executeService(InserisceDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		checkDocumentoGiaEsistente();
		checkSoggetto();
		checkAnno(); // TODO: controllare se possa essere spostato sopra
		
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.INCOMPLETO);
		
		documentoEntrataDad.inserisciAnagraficaDocumentoEntrata(doc);
		
		
		
		if(req.isInserisciQuotaContestuale()){
			//Ad esempio il servizio DefiniscePreDocumentoDiSpesaService salta l'inserimento contestuale della quota.			
			
			SubdocumentoEntrata subdocumentoEntrata = doc.getListaSubdocumenti().get(0);
			
			//Imposto lo stesso importo del documento anche sulla singola quota della nota credito.
			subdocumentoEntrata.setImporto(doc.getImportoNetto());
			subdocumentoEntrata.setDescrizione(doc.getDescrizione());
			subdocumentoEntrata.setDataScadenza(doc.getDataScadenza());
			
			subdocumentoEntrata = inserisceQuotaDocumentoEntrata(subdocumentoEntrata);
			
			doc.getListaSubdocumenti().set(0,subdocumentoEntrata);
		}
		
		gestisciFatturaFEL();
		
		caricaTipoDocumento();
		gestisciFlagRegolarizzazione();
		
		res.setDocumentoEntrata(doc);	
		
		
	}

	/**
	 * Gestisce, se necessario, l'inserimento automatico del documento di regolarizzazione.
	 * 
	 */
	protected void gestisciFlagRegolarizzazione() {		
		if(req.isInserisciDocumentoRegolarizzazione() && Boolean.TRUE.equals(doc.getTipoDocumento().getFlagRegolarizzazione())) {
			
			InserisceDocumentoSpesa reqIDS = new InserisceDocumentoSpesa();
			reqIDS.setRichiedente(req.getRichiedente());
			reqIDS.setInserisciDocumentoRegolarizzazione(false);	
			reqIDS.setBilancio(req.getBilancio());
			
			DocumentoSpesa documentoSpesa = creaDocumentoSpesaEquivalente();
			
			reqIDS.setDocumentoSpesa(documentoSpesa);
			executeExternalServiceSuccess(inserisceDocumentoSpesaService, reqIDS);
		}
	}
	
	
	private void gestisciFatturaFEL() {
		final String methodName = "gestisciFatturaFEL";
		
		if(doc.getFatturaFEL() != null && doc.getFatturaFEL().getIdFattura() != null){
			log.debug(methodName, "Inserisco il legame tra il documento[uid:"+doc.getUid()+"] e la fatturaFEL[idFattura:"+doc.getFatturaFEL().getIdFattura()+"]");
			
			fatturaFELDad.inserisciRelazioneDocumentoFattura(doc, doc.getFatturaFEL());
			fatturaFELDad.aggiornaStatoFattura(doc.getFatturaFEL(), StatoAcquisizioneFEL.IMPORTATA);
			fatturaFELDad.impostaDataCaricamentoFattura(doc.getFatturaFEL(), new Date());
			//TODO corrispettivo oneri in entrata?
//			elaboraDettagliOnereDocumento();
		}
	}
	

	/**
	 * Crea documento spesa equivalente.
	 *
	 * @return the documento spesa
	 */
	private DocumentoSpesa creaDocumentoSpesaEquivalente() {
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setAnno(doc.getAnno());
		documentoSpesa.setArrotondamento(doc.getArrotondamento());
		documentoSpesa.setCodiceBollo(doc.getCodiceBollo());	
		documentoSpesa.setDataEmissione(doc.getDataEmissione());
		documentoSpesa.setDataRepertorio(doc.getDataRepertorio());
		documentoSpesa.setDataScadenza(doc.getDataScadenza());
		documentoSpesa.setDescrizione(doc.getDescrizione());
		documentoSpesa.setEnte(doc.getEnte());
		documentoSpesa.setFlagBeneficiarioMultiplo(doc.getFlagDebitoreMultiplo());
		documentoSpesa.setImporto(doc.getImporto());
		documentoSpesa.setNote(doc.getNote());
		documentoSpesa.setNumero(doc.getNumero()); 
		documentoSpesa.setNumeroRepertorio(doc.getNumeroRepertorio());
		documentoSpesa.setSoggetto(doc.getSoggetto());
		documentoSpesa.setStatoOperativoDocumento(doc.getStatoOperativoDocumento());
		documentoSpesa.setTerminePagamento(doc.getTerminePagamento());
		
		TipoDocumento tipoDocumentoSpesa = documentoEntrataDad.findTipoDocumentoByCodiceEFamiglia(doc.getTipoDocumento().getCodice(), TipoFamigliaDocumento.SPESA);			
		documentoSpesa.setTipoDocumento(tipoDocumentoSpesa);
		
		documentoSpesa.setTipoRelazione(TipoRelazione.SUBORDINATO);
		documentoSpesa.addDocumentoEntrataPadre(doc);
		return documentoSpesa;
	}

	/**
	 * Carica tipo documento.
	 */
	private void caricaTipoDocumento() {
		TipoDocumento tipoDocumento = documentoEntrataDad.findTipoDocumentoById(doc.getTipoDocumento().getUid());
		doc.setTipoDocumento(tipoDocumento);		
	}
	
	/**
	 * Richiama il servizio di inserimento del subdocumento di entrata.
	 *
	 * @param subdocumentoEntrata the subdocumento entrata
	 * @return il subdocumento inserito.
	 */
	private SubdocumentoEntrata inserisceQuotaDocumentoEntrata(SubdocumentoEntrata subdocumentoEntrata) {
		InserisceQuotaDocumentoEntrata reqInsQuota = new InserisceQuotaDocumentoEntrata();
		reqInsQuota.setRichiedente(req.getRichiedente());
		reqInsQuota.setQuotaContestuale(true);
		reqInsQuota.setBilancio(req.getBilancio());
		reqInsQuota.setAggiornaStatoDocumento(false);
		subdocumentoEntrata.setEnte(doc.getEnte());
		
		DocumentoEntrata d = new DocumentoEntrata();
		d.setUid(doc.getUid());
		subdocumentoEntrata.setDocumento(d);		
		reqInsQuota.setSubdocumentoEntrata(subdocumentoEntrata);
		
		
		InserisceQuotaDocumentoEntrataResponse resInsQuota = executeExternalServiceSuccess(inserisceQuotaDocumentoEntrataService,reqInsQuota);
		return resInsQuota.getSubdocumentoEntrata();
		
	}
	
	
}

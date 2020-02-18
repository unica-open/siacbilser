/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoRelazione;

/**
 * Inserimento dell'anagrafica della nota credito di entrata.
 * 
 * @author Domenico
 *
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceNotaCreditoEntrataService extends CrudDocumentoDiEntrataBaseService<InserisceNotaCreditoEntrata, InserisceNotaCreditoEntrataResponse> {
	
//	/** The documento spesa dad. */
//	@Autowired
//	private DocumentoEntrataDad documentoEntrataDad;
	
	/** The inserisce quota documento entrata service. */
	@Autowired
	private InserisceQuotaDocumentoEntrataService inserisceQuotaDocumentoEntrataService;
	
//	/** The doc. */
//	private DocumentoEntrata doc;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoEntrata();
		bilancio = req.getBilancio();
		
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		
		checkEntita(bilancio, "bilancio");
		
		checkNotNull(doc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
		checkCondition(doc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
		
		checkNotNull(doc.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento"));
//		checkNotNull(doc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		checkNotNull(doc.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione documento"));
		
		checkNotNull(doc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento"));
		
		checkNotNull(doc.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione documento"));
		
		checkNotNull(doc.getTipoDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("tipo documento"));
		checkCondition(doc.getTipoDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid tipo documento"));
		
		
		checkNotNull(doc.getListaSubdocumenti(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti del documento"));
		checkCondition(doc.getListaSubdocumenti().size()==1, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti (deve esserci un solo subdocumento) del documento"));
		
		for(DocumentoSpesa dsp : doc.getListaDocumentiSpesaPadre()){
			dsp.setTipoRelazione(TipoRelazione.NOTA_CREDITO);
		}
		
		for(DocumentoEntrata dsp : doc.getListaDocumentiEntrataPadre()){
			dsp.setTipoRelazione(TipoRelazione.NOTA_CREDITO);
		}
		
		// SIAC-3191
		checkNumero();
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		documentoEntrataDad.setLoginOperazione(loginOperazione);
		documentoEntrataDad.setEnte(ente);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public InserisceNotaCreditoEntrataResponse executeService(InserisceNotaCreditoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		doc.setTipoRelazione(TipoRelazione.NOTA_CREDITO);
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		if(doc.getImportoDaDedurreSuFattura() == null){
			doc.setImportoDaDedurreSuFattura(doc.getImporto());
		}
		// SIAC-5233
		checkDocumentoGiaEsistente();
		
		documentoEntrataDad.inserisciAnagraficaDocumentoEntrata(doc);
		
		SubdocumentoEntrata subdocumentoEntrata = doc.getListaSubdocumenti().get(0);
		
		//Imposto lo stesso importo del documento anche sulla singola quota della nota credito.
		subdocumentoEntrata.setImporto(doc.getImporto());
		
		subdocumentoEntrata = inserisceQuotaDocumentoEntrata(subdocumentoEntrata);
		
		doc.getListaSubdocumenti().set(0,subdocumentoEntrata);
		
		for(DocumentoEntrata padre: doc.getListaDocumentiEntrataPadre()){
			DocumentoEntrata documentoConStatoAggiornato = aggiornaStatoOperativoDocumento(padre.getUid());
			padre.setStatoOperativoDocumento(documentoConStatoAggiornato.getStatoOperativoDocumento());
			padre.setDataInizioValiditaStato(documentoConStatoAggiornato.getDataInizioValiditaStato());
			break;//dovrebbe esserci un solo padre
		}
		
		res.setDocumentoEntrata(doc);	
		
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
		subdocumentoEntrata.setEnte(doc.getEnte());
		reqInsQuota.setAggiornaStatoDocumento(false);
		reqInsQuota.setBilancio(bilancio);
		reqInsQuota.setQuotaContestuale(true);
		
		DocumentoEntrata d = new DocumentoEntrata();
		d.setUid(doc.getUid());
		subdocumentoEntrata.setDocumento(d);		
		reqInsQuota.setSubdocumentoEntrata(subdocumentoEntrata);
	
		
		InserisceQuotaDocumentoEntrataResponse resInsQuota = executeExternalServiceSuccess(inserisceQuotaDocumentoEntrataService,reqInsQuota);
		return resInsQuota.getSubdocumentoEntrata();
		
	}
	
	
}

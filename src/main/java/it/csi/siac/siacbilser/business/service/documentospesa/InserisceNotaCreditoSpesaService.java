/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoRelazione;

// TODO: Auto-generated Javadoc
/**
 * Inserimento dell'anagrafica della nota credito di spesa.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InserisceNotaCreditoSpesaService extends CrudDocumentoDiSpesaBaseService<InserisceNotaCreditoSpesa, InserisceNotaCreditoSpesaResponse> {
	
	
	
	/** The inserisce quota documento spesa service. */
	@Autowired
	private InserisceQuotaDocumentoSpesaService inserisceQuotaDocumentoSpesaService;
	
	@Autowired
	protected DocumentoDad documentoDad;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoSpesa();
		
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		
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
		
		checkNotNull(req.getBilancio(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio"));
		bilancio = req.getBilancio();
		
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
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		documentoSpesaDad.setEnte(ente);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public InserisceNotaCreditoSpesaResponse executeService(InserisceNotaCreditoSpesa serviceRequest) {
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
		
		impostaFlagCollegatoCECSePadreCollegatoCEC();
		documentoSpesaDad.inserisciAnagraficaDocumentoSpesa(doc);
		
		SubdocumentoSpesa subdocumentoSpesa = doc.getListaSubdocumenti().get(0);
		
		//Imposto lo stesso importo del documento anche sulla singola quota della nota credito.
		subdocumentoSpesa.setImporto(doc.getImporto());
		
		subdocumentoSpesa = inserisceQuotaDocumentoSpesa(subdocumentoSpesa);
		
		doc.getListaSubdocumenti().set(0,subdocumentoSpesa);
		
		for(DocumentoSpesa padre: doc.getListaDocumentiSpesaPadre()){
			DocumentoSpesa documentoConStatoAggiornato = aggiornaStatoOperativoDocumento(padre.getUid()).getDocumentoSpesa();
			padre.setStatoOperativoDocumento(documentoConStatoAggiornato.getStatoOperativoDocumento());
			padre.setDataInizioValiditaStato(documentoConStatoAggiornato.getDataInizioValiditaStato());
			break;//dovrebbe esserci un solo padre
		}
		
		res.setDocumentoSpesa(doc);
		
	}
	
	private void impostaFlagCollegatoCECSePadreCollegatoCEC(){
		String methodName = "impostaFlagCollegatoCECSePadreCollegatoCEC";
		for(DocumentoSpesa padre: doc.getListaDocumentiSpesaPadre()){
			boolean collegatoCEC = documentoDad.isCollegatoCEC(padre);
			log.debug(methodName, "Imposto flagCollegatoCEC a "+collegatoCEC);
			doc.setCollegatoCEC(collegatoCEC);
			break;//dovrebbe esserci un solo padre
		}
	}

	/**
	 * Richiama il servizio di inserimento del subdocumento di spesa.
	 *
	 * @param subdocumentoSpesa the subdocumento spesa
	 * @return il subdocumento inserito.
	 */
	private SubdocumentoSpesa inserisceQuotaDocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		InserisceQuotaDocumentoSpesa reqInsQuota = new InserisceQuotaDocumentoSpesa();
		reqInsQuota.setRichiedente(req.getRichiedente());	
		reqInsQuota.setAggiornaStatoDocumento(false);
		reqInsQuota.setBilancio(bilancio);
		reqInsQuota.setQuotaContestuale(true);
		subdocumentoSpesa.setEnte(doc.getEnte());
		
		DocumentoSpesa d = new DocumentoSpesa();
		d.setUid(doc.getUid());
		subdocumentoSpesa.setDocumento(d);		
		reqInsQuota.setSubdocumentoSpesa(subdocumentoSpesa);	
		
		InserisceQuotaDocumentoSpesaResponse resInsQuota = executeExternalServiceSuccess(inserisceQuotaDocumentoSpesaService,reqInsQuota);
		return resInsQuota.getSubdocumentoSpesa();
		
	}
	
	
}

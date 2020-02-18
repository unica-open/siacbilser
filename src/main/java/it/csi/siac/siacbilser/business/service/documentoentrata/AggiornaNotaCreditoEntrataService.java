/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoRelazione;

/**
 * Aggiorna l'anagrafica della nota credito di entrata.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaNotaCreditoEntrataService extends CrudDocumentoDiEntrataBaseService<AggiornaNotaCreditoEntrata, AggiornaNotaCreditoEntrataResponse> {
	
	
	/** The aggiorna quota documento entrata service. */
	@Autowired
	private AggiornaQuotaDocumentoEntrataService aggiornaQuotaDocumentoEntrataService;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoEntrata();
		bilancio = req.getBilancio();
		
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		checkCondition(doc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento"));
		
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
	public AggiornaNotaCreditoEntrataResponse executeService(AggiornaNotaCreditoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		doc.setStatoOperativoDocumento(StatoOperativoDocumento.VALIDO);
		if(doc.getImportoDaDedurreSuFattura() == null){
			doc.setImportoDaDedurreSuFattura(doc.getImporto());
		}
		
		checkTotaleImportoDaDedurreSuFattura(doc);
		
		documentoEntrataDad.aggiornaAnagraficaDocumentoEntrata(doc);
		
		SubdocumentoEntrata subdocumentoEntrata = doc.getListaSubdocumenti().get(0);
		
		//Imposto lo stesso importo del documento anche sulla singola quota della nota credito.
		subdocumentoEntrata.setImporto(doc.getImporto());
		
		subdocumentoEntrata = aggiornaQuotaDocumentoEntrata(subdocumentoEntrata);
		
		doc.getListaSubdocumenti().set(0,subdocumentoEntrata);
		
		for(DocumentoEntrata padre: doc.getListaDocumentiEntrataPadre()){
			DocumentoEntrata documentoConStatoAggiornato = aggiornaStatoOperativoDocumento(padre.getUid());
			padre.setStatoOperativoDocumento(documentoConStatoAggiornato.getStatoOperativoDocumento());
			padre.setDataInizioValiditaStato(documentoConStatoAggiornato.getDataInizioValiditaStato());
			//dovrebbe esserci un solo padre
			break;
		}
		
		res.setDocumentoEntrata(doc);		
		
	}
	

	/**
	 * Richiama il servizio di aggiornamento del subdocumento di entrata.
	 *
	 * @param subdocumentoEntrata the subdocumento entrata
	 * @return il subdocumento inserito.
	 */
	private SubdocumentoEntrata aggiornaQuotaDocumentoEntrata(SubdocumentoEntrata subdocumentoEntrata) {
		AggiornaQuotaDocumentoEntrata reqInsQuota = new AggiornaQuotaDocumentoEntrata();
		reqInsQuota.setRichiedente(req.getRichiedente());		
		subdocumentoEntrata.setEnte(doc.getEnte());
		reqInsQuota.setAggiornaStatoDocumento(false);
		reqInsQuota.setBilancio(bilancio);
		
		DocumentoEntrata d = new DocumentoEntrata();
		d.setUid(doc.getUid());
		subdocumentoEntrata.setDocumento(d);
		if(subdocumentoEntrata.getAttoAmministrativo() != null && subdocumentoEntrata.getAttoAmministrativo().getUid() == 0){
			subdocumentoEntrata.setAttoAmministrativo(null);
		}
		reqInsQuota.setSubdocumentoEntrata(subdocumentoEntrata);	
		
		AggiornaQuotaDocumentoEntrataResponse resInsQuota = executeExternalServiceSuccess(aggiornaQuotaDocumentoEntrataService,reqInsQuota);
		return resInsQuota.getSubdocumentoEntrata();
		
	}
	
	/**
	 * Controlla la coerenza tra il totaleImportoDaDedurreSuFattura e l'importo della nota di credito.
	 * 
	 * @param notaDiCredito
	 * @throws BusinessException OPERAZIONE_NON_CONSENTITA se totaleImportoDaDedurreSuFattura supera l'importo della nota di credito.
	 */
	private void checkTotaleImportoDaDedurreSuFattura(Documento<?, ?> notaDiCredito) {
		String methodName = "checkTotaleImportoDaDedurreSuFattura";
		
		BigDecimal totaleImportoDaDedurreSuFattura = calcolaTotaleImportoDaDedurreSuFattura();
		
		log.debug(methodName, MessageFormat.format("Controllo importi:\n"
				+ "totaleImportoDaDedurreSuFattura: {0,number,###,##0.00} €. \n"
				+ "importo nota di credito: {1,number,###,##0.00} €", 
				totaleImportoDaDedurreSuFattura, 
				notaDiCredito.getImporto()));
		
		if(totaleImportoDaDedurreSuFattura.compareTo(notaDiCredito.getImporto())>0){
			String msg = MessageFormat.format("l''importo da dedurre su fatture deve essere minore o uguale dell''importo della nota di credito. "
					+ "Totale importo da dedurre su fatture: {0,number,###,##0.00} €. "
					+ "Importo nota di credito: {1,number,###,##0.00} €.", 
					totaleImportoDaDedurreSuFattura, 
					notaDiCredito.getImporto());
			log.error(methodName, msg + " [notaDiCredito.uid: "+ notaDiCredito.getUid() + "]");
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore(msg));
		}
	}

	private BigDecimal calcolaTotaleImportoDaDedurreSuFattura() {
		final String methodName = "calcolaTotaleImportoDaDedurreSuFattura";
		
		BigDecimal totaleImportoDaDedurreSuFattura = BigDecimal.ZERO;
		for(DocumentoEntrata documentoEntrataPadre : doc.getListaDocumentiEntrataPadre()){
			totaleImportoDaDedurreSuFattura = totaleImportoDaDedurreSuFattura.add(documentoEntrataPadre.getImportoDaDedurreSuFattura());
		}
		log.debug(methodName, "result:"+totaleImportoDaDedurreSuFattura);
		return totaleImportoDaDedurreSuFattura;
	}
}

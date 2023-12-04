/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * The Class AggiornaImportiQuoteDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaImportoDaDedurreQuoteDocumentoEntrataService extends CrudDocumentoDiEntrataBaseService<AggiornaImportiQuoteDocumentoEntrata, AggiornaImportiQuoteDocumentoEntrataResponse> {
	
	
	/** The subdocs. */
	private List<SubdocumentoEntrata> subdocs;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocs = req.getSubdocumentiEntrata();
		bilancio = req.getBilancio();
		
		checkNotNull(subdocs, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti entrata"));
		checkCondition(!subdocs.isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti entrata (vuota)"));
		
		checkEntita(bilancio, "bilancio");

		for(SubdocumentoEntrata subdoc : subdocs) {
			checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento entrata"));
			checkCondition(subdoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento entrata"));
			
//			checkNotNull(subdoc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
//			checkCondition(subdoc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
			
			checkNotNull(subdoc.getDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento subdocumento entrata"));
			checkCondition(subdoc.getDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento subdocumento entrata"));
			
			//checkNotNull(subdoc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero subdocumento"));
			
			checkNotNull(subdoc.getImportoDaDedurre(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo da dedurre subdocumento"));
			checkNotNull(subdoc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo subdocumento"));
			//checkNotNull(subdoc.getImportoDaPagare(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo da pagare subdocumento"));
			
			checkCondition(subdoc.getImportoDaDedurre().compareTo(subdoc.getImporto())<=0, ErroreCore.VALORE_NON_CONSENTITO.getErrore("importo da dedurre", "Deve essere minore uguale all'importo"));
			
		}
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
		subdocumentoEntrataDad.setEnte(ente);
	}
		
	@Override
	@Transactional
	public AggiornaImportiQuoteDocumentoEntrataResponse executeService(AggiornaImportiQuoteDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		for(SubdocumentoEntrata subdoc : subdocs) {
			subdocumentoEntrataDad.aggiornaImportoDaDedurreSubdocumentoEntrata(subdoc);		
		}
		
		for(@SuppressWarnings("rawtypes") Documento doc : req.getDocumentiReferenziatiDaiSubdocumenti()){
			DocumentoEntrata statoOperativoDocumento = aggiornaStatoOperativoDocumento(doc.getUid());
			doc.setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());	
			doc.setDataInizioValiditaStato(statoOperativoDocumento.getDataInizioValiditaStato());
		}			
		
		res.setSubdocumentiEntrata(subdocs);
	}

}

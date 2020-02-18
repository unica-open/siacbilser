/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class AggiornaImportoDaDedurreQuoteDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaImportoDaDedurreQuoteDocumentoSpesaService extends CrudDocumentoDiSpesaBaseService<AggiornaImportiQuoteDocumentoSpesa, AggiornaImportiQuoteDocumentoSpesaResponse> {
	
	
	/** The subdocs. */
	private List<SubdocumentoSpesa> subdocs;
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocs = req.getSubdocumentiSpesa();
		
		checkNotNull(subdocs, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti spesa"));
		checkCondition(!subdocs.isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti spesa (vuota)"));

		for(SubdocumentoSpesa subdoc : subdocs) {
			checkNotNull(subdoc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("subdocumento spesa"));
			checkCondition(subdoc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid subdocumento spesa"));
			
//			checkNotNull(subdoc.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
//			checkCondition(subdoc.getEnte().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid ente"));
			
			checkNotNull(subdoc.getDocumento(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento subdocumento spesa"));
			checkCondition(subdoc.getDocumento().getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento subdocumento spesa"));
			
			//checkNotNull(subdoc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero subdocumento"));
			
			checkNotNull(subdoc.getImportoDaDedurre(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo da dedurre subdocumento"));
			checkNotNull(subdoc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo subdocumento"));
			//checkNotNull(subdoc.getImportoDaPagare(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo da pagare subdocumento"));
			
			checkCondition(subdoc.getImportoDaDedurre().compareTo(subdoc.getImporto())<=0, ErroreCore.VALORE_NON_VALIDO.getErrore("importo da dedurre", "Deve essere minore uguale all'importo"));
			
			
		}
		
		checkEntita(req.getBilancio(), "bilancio");
		bilancio = req.getBilancio();
		
	}	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		super.init();
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		//subdocumentoSpesaDad.setEnte(subdoc.getEnte());
	}
	
	@Override
	@Transactional
	public AggiornaImportiQuoteDocumentoSpesaResponse executeService(AggiornaImportiQuoteDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
		

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		for(SubdocumentoSpesa subdoc : subdocs) {
			subdocumentoSpesaDad.aggiornaImportoDaDedurreSubdocumentoSpesa(subdoc);		
		}
		
		for(@SuppressWarnings("rawtypes") Documento doc : req.getDocumentiReferenziatiDaiSubdocumenti()){
			DocumentoSpesa statoOperativoDocumento = aggiornaStatoOperativoDocumento(doc.getUid()).getDocumentoSpesa();
			doc.setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());
			doc.setDataInizioValiditaStato(statoOperativoDocumento.getDataInizioValiditaStato());
		}			
		
		res.setSubdocumentiSpesa(subdocs);
	}

}

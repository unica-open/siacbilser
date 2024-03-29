/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnullaDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaNotaCreditoEntrataService extends CrudDocumentoDiEntrataBaseService<AnnullaNotaCreditoEntrata, AnnullaNotaCreditoEntrataResponse> {
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoEntrata();
		bilancio = req.getBilancio();
		
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento Entrata"));
		checkCondition(doc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento Entrata"));
		checkEntita(bilancio, "bilancio");
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		documentoEntrataDad.setLoginOperazione(loginOperazione);
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AnnullaNotaCreditoEntrataResponse executeService(AnnullaNotaCreditoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		caricaDocumentoPadre();
		
		StatoOperativoDocumento statoOperativoDocumento = StatoOperativoDocumento.ANNULLATO;
		documentoEntrataDad.aggiornaStatoDocumentoEntrata(doc.getUid(), statoOperativoDocumento);
		doc.setStatoOperativoDocumento(statoOperativoDocumento);
		res.setDocumentoEntrata(doc);
		
		for(DocumentoEntrata d : doc.getListaDocumentiEntrataPadre()){
			aggiornaStatoOperativoDocumento(d);
		}
		

	}

	private void caricaDocumentoPadre() {
		DocumentoEntrata documentoEntrata = documentoEntrataDad.findDocumentiCollegatiByIdDocumento(doc.getUid());
		if(documentoEntrata.getListaDocumentiEntrataPadre().isEmpty()){
			throw new BusinessException(ErroreCore.ENTITA_INESISTENTE.getErrore("Annulla nota credito","Il documento associato alla nota credito"));
		}
		
		doc.setListaDocumentiEntrataPadre(documentoEntrata.getListaDocumentiEntrataPadre());
	}

}

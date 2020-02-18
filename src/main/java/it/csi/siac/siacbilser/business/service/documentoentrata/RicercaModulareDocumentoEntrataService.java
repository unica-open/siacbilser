/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;

/**
 * Ricerca di un Documento di Entrata.
 * Permrette di richiedere esattamente il dettaglio che si vuole ottenere del 
 * documento attraverso un elenco di {@link DocumentoEntrataModelDetail} e dei 
 * subdocumenti associati attraverso un elenco di {@link SubdocumentoEntrataModelDetail}.
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaModulareDocumentoEntrataService extends CheckedAccountBaseService<RicercaModulareDocumentoEntrata, RicercaModulareDocumentoEntrataResponse> {
	
	//DADs..
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	@Autowired
	private SubdocumentoEntrataDad subdocumentoEntrataDad;
	
	//Fields..
	private DocumentoEntrata doc;

	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoEntrata();
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		checkCondition(doc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento"));
		
		if(req.getDocumentoEntrataModelDetails()!=null && req.getSubdocumentoEntrataModelDetails()!=null){
			List<DocumentoEntrataModelDetail> list =  new ArrayList<DocumentoEntrataModelDetail>(Arrays.asList(req.getDocumentoEntrataModelDetails()));
			list.remove(DocumentoEntrataModelDetail.SubdocumentoEntrata);
			req.setDocumentoEntrataModelDetails(list.toArray(new DocumentoEntrataModelDetail[list.size()]));
		}
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaModulareDocumentoEntrataResponse executeService(RicercaModulareDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		DocumentoEntrata documentoEntrata = documentoEntrataDad.findDocumentoEntrataByIdModelDetail(doc.getUid(), req.getDocumentoEntrataModelDetails());		
		if(documentoEntrata==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("documento Entrata", "id: "+doc.getUid()));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		
		if(req.getSubdocumentoEntrataModelDetails()!=null){
			List<SubdocumentoEntrata> listaSubdocumenti = subdocumentoEntrataDad.findSubdocumentiEntrataByIdDocumento(doc.getUid(), req.getSubdocumentoEntrataModelDetails());	
			
			documentoEntrata.setListaSubdocumenti(listaSubdocumenti);
		}
		
		res.setDocumento(documentoEntrata);
		
		
	}

}

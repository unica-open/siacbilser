/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesaModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;

/**
 * Ricerca di un Documento di Spesa. 
 * Permrette di richiedere esattamente il dettaglio che si vuole ottenere del 
 * documento attraverso un elenco di {@link DocumentoSpesaModelDetail} e dei 
 * subdocumenti associati attraverso un elenco di {@link SubdocumentoSpesaModelDetail}.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaModulareDocumentoSpesaService extends CheckedAccountBaseService<RicercaModulareDocumentoSpesa, RicercaModulareDocumentoSpesaResponse> {
	
	//DADs..
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	//Fields..
	private DocumentoSpesa doc;

	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoSpesa();
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		checkCondition(doc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento"));
		
		
		if(req.getDocumentoSpesaModelDetails()!=null && req.getSubdocumentoSpesaModelDetails()!=null){
			List<DocumentoSpesaModelDetail> list =  new ArrayList<DocumentoSpesaModelDetail>(Arrays.asList(req.getDocumentoSpesaModelDetails()));
			list.remove(DocumentoSpesaModelDetail.SubdocumentoSpesa);
			req.setDocumentoSpesaModelDetails(list.toArray(new DocumentoSpesaModelDetail[list.size()]));
		}
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaModulareDocumentoSpesaResponse executeService(RicercaModulareDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void init() {
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		DocumentoSpesa documentoSpesa = documentoSpesaDad.findDocumentoSpesaByIdModelDetail(doc.getUid(), req.getDocumentoSpesaModelDetails());
		if(documentoSpesa==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("documento spesa", "id: "+doc.getUid()));
			res.setEsito(Esito.FALLIMENTO);
			return;
		}
		
		if(req.getSubdocumentoSpesaModelDetails() != null){
			List<SubdocumentoSpesa> listaSubdocumenti = subdocumentoSpesaDad.findSubdocumentiSpesaByIdDocumento(doc.getUid(), req.getSubdocumentoSpesaModelDetails());
			documentoSpesa.setListaSubdocumenti(listaSubdocumenti);
		}
		
		res.setDocumento(documentoSpesa);
		
		
	}



}

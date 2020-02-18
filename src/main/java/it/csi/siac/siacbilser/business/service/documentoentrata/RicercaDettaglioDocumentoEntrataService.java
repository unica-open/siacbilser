/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoEntrataDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

// TODO: Auto-generated Javadoc
/**
 * Ricerca di dettaglio di un Documento di Entrata.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioDocumentoEntrataService extends CheckedAccountBaseService<RicercaDettaglioDocumentoEntrata, RicercaDettaglioDocumentoEntrataResponse> {
	
	/** The documento entrata dad. */
	@Autowired
	private DocumentoEntrataDad documentoEntrataDad;
	
	/** The ricerca quote by documento entrata service. */
	@Autowired
	private RicercaQuoteByDocumentoEntrataService ricercaQuoteByDocumentoEntrataService;
	
	/** The doc. */
	private DocumentoEntrata doc;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoEntrata();
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		checkCondition(doc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioDocumentoEntrataResponse executeService(RicercaDettaglioDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		DocumentoEntrata documentoEntrata = documentoEntrataDad.findDocumentoEntrataById(doc.getUid());		
		if(documentoEntrata==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("documento Entrata", "id: "+doc.getUid()));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		
		
		List<SubdocumentoEntrata> listaSubdocumenti = ricercaSubdocumentiEntrata(documentoEntrata);
		
		documentoEntrata.setListaSubdocumenti(listaSubdocumenti);
		
		res.setDocumento(documentoEntrata);
		
		
	}

	/**
	 * Ricerca le quote legate al documento di Entrata.
	 *
	 * @param documentoEntrata the documento entrata
	 * @return the list
	 */
	private List<SubdocumentoEntrata> ricercaSubdocumentiEntrata(DocumentoEntrata documentoEntrata) {
		RicercaQuoteByDocumentoEntrata reqRQ = new RicercaQuoteByDocumentoEntrata();
		reqRQ.setRichiedente(req.getRichiedente());
		reqRQ.setDocumentoEntrata(documentoEntrata);

		RicercaQuoteByDocumentoEntrataResponse resRQ = executeExternalService(ricercaQuoteByDocumentoEntrataService, reqRQ);
		return resRQ.getSubdocumentiEntrata();
	}

}

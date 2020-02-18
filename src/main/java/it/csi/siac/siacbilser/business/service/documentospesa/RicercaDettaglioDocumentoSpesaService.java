/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

// TODO: Auto-generated Javadoc
/**
 * Ricerca di dettaglio di un Documento di Spesa.
 *
 * @author Domenico
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioDocumentoSpesaService extends CheckedAccountBaseService<RicercaDettaglioDocumentoSpesa, RicercaDettaglioDocumentoSpesaResponse> {
	
	/** The documento spesa dad. */
	@Autowired
	private DocumentoSpesaDad documentoSpesaDad;
	
	/** The ricerca quote by documento spesa service. */
	@Autowired
	private RicercaQuoteByDocumentoSpesaService ricercaQuoteByDocumentoSpesaService;
	
	/** The doc. */
	private DocumentoSpesa doc;
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		doc = req.getDocumentoSpesa();
		checkNotNull(doc, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		checkCondition(doc.getUid()!=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid documento"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional(readOnly=true)
	public RicercaDettaglioDocumentoSpesaResponse executeService(RicercaDettaglioDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		DocumentoSpesa documentoSpesa = documentoSpesaDad.findDocumentoSpesaById(doc.getUid());		
		if(documentoSpesa==null){
			res.addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("documento spesa", "id: "+doc.getUid()));
			res.setEsito(Esito.FALLIMENTO);			
			return;
		}
		
		
		List<SubdocumentoSpesa> listaSubdocumenti = ricercaSubdocumentiSpesa(documentoSpesa);
		documentoSpesa.setListaSubdocumenti(listaSubdocumenti);
		
		res.setDocumento(documentoSpesa);
		
		
	}

	/**
	 * Ricerca le quote legate al documento di spesa.
	 *
	 * @param documentoSpesa the documento spesa
	 * @return the list
	 */
	private List<SubdocumentoSpesa> ricercaSubdocumentiSpesa(DocumentoSpesa documentoSpesa) {
		RicercaQuoteByDocumentoSpesa reqRQ = new RicercaQuoteByDocumentoSpesa();
		reqRQ.setRichiedente(req.getRichiedente());
		reqRQ.setDocumentoSpesa(documentoSpesa);

		RicercaQuoteByDocumentoSpesaResponse resRQ = executeExternalService(ricercaQuoteByDocumentoSpesaService, reqRQ);
		return resRQ.getSubdocumentiSpesa();
	}

}

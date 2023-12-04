/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.documento.DocumentoServiceCallGroup;
import it.csi.siac.siacbilser.integration.dad.ProvvisorioBilDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * Inserimento dell'anagrafica del Documento di Entrata .
 *
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class InserisceDocumentoPerProvvisoriEntrataService extends CrudDocumentoDiEntrataBaseService<InserisceDocumentoPerProvvisoriEntrata, InserisceDocumentoPerProvvisoriEntrataResponse> {
		
	
	private DocumentoServiceCallGroup dscg;
	
	@Autowired
	private ProvvisorioBilDad provvisorioBilDad;
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getBilancio(), "bilancio");
		
		checkNotNull(req.getDocumentoEntrata(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		doc = req.getDocumentoEntrata();
		doc.setEnte(ente);
		
		checkNotNull(doc.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento"));
		checkNotNull(doc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		checkNotNull(doc.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione documento"));
		checkNotNull(doc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento"));
		checkNotNull(doc.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione documento"));
		
		checkEntita(doc.getTipoDocumento(), "tipo documento");
		checkEntita(doc.getSoggetto(), "soggetto documento");
				
		checkNotNull(req.getListaQuote(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti del documento"));
		for(SubdocumentoEntrata subdocEntrata : doc.getListaSubdocumenti()){
			checkEntita(subdocEntrata.getProvvisorioCassa(), "provvisorio di cassa");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		this.dscg = new DocumentoServiceCallGroup(super.serviceExecutor, req.getRichiedente(), ente, req.getBilancio());
		
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public InserisceDocumentoPerProvvisoriEntrataResponse executeService(InserisceDocumentoPerProvvisoriEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		inserisciDocumenotEntrata();
		inserisciQuoteConProvvisori();
		res.setDocumentoEntrata(doc);	
		
	}
	
	/**
	 * Richiamo al servizio InserisceDocumentoEntrataService
	 */
	private void inserisciDocumenotEntrata() {
		InserisceDocumentoEntrataResponse resIDE = dscg.inserisceDocumentoEntrata(doc, false);
		doc = resIDE.getDocumentoEntrata();
	}

	/**
	 * Inserimento delle quote con i relativi provvisori di cassa
	 */
	private void inserisciQuoteConProvvisori() {
		List<SubdocumentoEntrata> listaQuoteInserite = new ArrayList<SubdocumentoEntrata>();
		DocumentoEntrata documentoEntrata = new DocumentoEntrata();
		documentoEntrata.setUid(doc.getUid());
		for(SubdocumentoEntrata subdocEntrata : req.getListaQuote()){
			popolaQuota(subdocEntrata,documentoEntrata);
			subdocEntrata = inserisciQuota(subdocEntrata);
			listaQuoteInserite.add(subdocEntrata);
		}
		doc.setListaSubdocumenti(listaQuoteInserite);
		
	}
	
	/**
	 * Richiamo al servizio InserisceQuotaDocumentoEntrataService
	 */
	private SubdocumentoEntrata inserisciQuota(SubdocumentoEntrata subdocEntrata) {
		return dscg.inserisceQuotaDocumentoEntrata(subdocEntrata, false);
	}

	/**
	 * Popola la quota da inserire con importo pari all'importo da regolarizzare del provvisorio di cassa
	 * @param subdocEntrata
	 * @param documentoEntrata
	 */
	private void popolaQuota(SubdocumentoEntrata subdocEntrata, DocumentoEntrata documentoEntrata) {
		BigDecimal importoDaRegolarizzare = provvisorioBilDad.calcolaImportoDaRegolarizzareProvvisorio(subdocEntrata.getProvvisorioCassa());
		String causaleProvvisorio = provvisorioBilDad.ottieniDescrizioneCausaleProvvisorio(subdocEntrata.getProvvisorioCassa());
		
		subdocEntrata.getProvvisorioCassa().setImportoDaRegolarizzare(importoDaRegolarizzare);
		subdocEntrata.getProvvisorioCassa().setCausale(causaleProvvisorio);
		subdocEntrata.setImporto(importoDaRegolarizzare);
		subdocEntrata.setDocumento(documentoEntrata);
		subdocEntrata.setEnte(ente);
		subdocEntrata.setDescrizione(causaleProvvisorio);
	}

	
	
}

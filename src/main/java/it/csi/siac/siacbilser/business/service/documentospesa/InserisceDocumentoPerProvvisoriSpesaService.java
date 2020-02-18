/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Inserimento dell'anagrafica del Documento di Spesa .
 *
 * @author Valentina
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class InserisceDocumentoPerProvvisoriSpesaService extends CrudDocumentoDiSpesaBaseService<InserisceDocumentoPerProvvisoriSpesa, InserisceDocumentoPerProvvisoriSpesaResponse> {
		
	
	private DocumentoServiceCallGroup dscg;
	
	@Autowired
	private ProvvisorioBilDad provvisorioBilDad;
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		checkEntita(req.getBilancio(), "bilancio");
		
		checkNotNull(req.getDocumentoSpesa(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("documento"));
		doc = req.getDocumentoSpesa();
		doc.setEnte(ente);
		
		checkNotNull(doc.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno documento"));
		checkNotNull(doc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero documento"));
		checkNotNull(doc.getDescrizione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("descrizione documento"));
		checkNotNull(doc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo documento"));
		checkNotNull(doc.getDataEmissione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("data emissione documento"));
		
		checkEntita(doc.getTipoDocumento(), "tipo documento");
		checkEntita(doc.getSoggetto(), "soggetto documento");
				
		checkNotNull(req.getListaQuote(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti del documento"));
		for(SubdocumentoSpesa subdocSpesa : doc.getListaSubdocumenti()){
			checkEntita(subdocSpesa.getProvvisorioCassa(), "provvisorio di cassa");
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
	public InserisceDocumentoPerProvvisoriSpesaResponse executeService(InserisceDocumentoPerProvvisoriSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		
		inserisciDocumentoSpesa();
		inserisciQuoteConProvvisori();
		res.setDocumentoSpesa(doc);	
		
	}
	
	/**
	 * Richiamo al servizio InserisceDocumentoEntrataService
	 */
	private void inserisciDocumentoSpesa() {
		InserisceDocumentoSpesaResponse resIDE = dscg.inserisceDocumentoSpesa(doc, false);
		doc = resIDE.getDocumentoSpesa();
	}

	/**
	 * Inserimento delle quote con i relativi provvisori di cassa
	 */
	private void inserisciQuoteConProvvisori() {
		List<SubdocumentoSpesa> listaQuoteInserite = new ArrayList<SubdocumentoSpesa>();
		DocumentoSpesa documentoSpesa = new DocumentoSpesa();
		documentoSpesa.setUid(doc.getUid());
		for(SubdocumentoSpesa subdocSpesa : req.getListaQuote()){
			popolaQuota(subdocSpesa, documentoSpesa);
			subdocSpesa = inserisciQuota(subdocSpesa);
			listaQuoteInserite.add(subdocSpesa);
		}
		doc.setListaSubdocumenti(listaQuoteInserite);
		
	}
	
	/**
	 * Richiamo al servizio InserisceQuotaDocumentoEntrataService
	 */
	private SubdocumentoSpesa inserisciQuota(SubdocumentoSpesa subdocSpesa) {
		return dscg.inserisceQuotaDocumentoSpesa(subdocSpesa, false, false, true);
	}

	/**
	 * Popola la quota da inserire con importo pari all'importo da regolarizzare del provvisorio di cassa
	 * @param subdocEntrata
	 * @param documentoEntrata
	 */
	private void popolaQuota(SubdocumentoSpesa subdocSpesa, DocumentoSpesa documentoSpesa) {
		BigDecimal importoDaRegolarizzare = provvisorioBilDad.calcolaImportoDaRegolarizzareProvvisorio(subdocSpesa.getProvvisorioCassa());
		String causaleProvvisorio = provvisorioBilDad.ottieniDescrizioneCausaleProvvisorio(subdocSpesa.getProvvisorioCassa());
		
		subdocSpesa.getProvvisorioCassa().setImportoDaRegolarizzare(importoDaRegolarizzare);
		subdocSpesa.getProvvisorioCassa().setCausale(causaleProvvisorio);
		subdocSpesa.setImporto(importoDaRegolarizzare);
		subdocSpesa.setDocumento(documentoSpesa);
		subdocSpesa.setEnte(ente);
		subdocSpesa.setDescrizione(causaleProvvisorio);
	}

	
	
}

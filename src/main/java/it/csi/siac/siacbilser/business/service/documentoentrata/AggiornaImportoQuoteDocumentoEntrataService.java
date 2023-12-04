/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentoentrata;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.documento.DocumentoServiceCallGroup;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * The Class AggiornaImportiQuoteDocumentoEntrataService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaImportoQuoteDocumentoEntrataService extends CrudDocumentoDiEntrataBaseService<AggiornaImportiQuoteDocumentoEntrata, AggiornaImportiQuoteDocumentoEntrataResponse> {
	
	
	/** The subdocs. */
	private List<SubdocumentoEntrata> subdocs;
	
	@Autowired
	private DocumentoDad documentoDad;
	
	private DocumentoServiceCallGroup dscg;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocs = req.getSubdocumentiEntrata();
		bilancio = req.getBilancio();
		
		checkNotNull(subdocs, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti entrata"));
		checkCondition(!subdocs.isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti entrata (vuota)"));
		
		checkEntita(bilancio, "bilancio");

		for(SubdocumentoEntrata subdoc : subdocs) {
			checkEntita(subdoc, "subdocumento entrata");
//			checkEntita(subdoc.getEnte(), "ente");
			checkEntita(subdoc.getDocumento(), "documento subdocumento entrata");
			
			//checkNotNull(subdoc.getNumero(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("numero subdocumento"));
			
			checkNotNull(subdoc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo subdocumento"));
			//checkNotNull(subdoc.getImportoDaPagare(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo da pagare subdocumento"));
		}
	}
	
	@Override
	protected void init() {
		subdocumentoEntrataDad.setLoginOperazione(loginOperazione);
		subdocumentoEntrataDad.setEnte(ente);
		documentoEntrataDad.setEnte(ente);
		documentoEntrataDad.setLoginOperazione(loginOperazione);
		
		this.dscg = new DocumentoServiceCallGroup(super.serviceExecutor, req.getRichiedente(), ente, req.getBilancio());
	}
	
	@Override
	@Transactional
	public AggiornaImportiQuoteDocumentoEntrataResponse executeService(AggiornaImportiQuoteDocumentoEntrata serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		
		for(SubdocumentoEntrata subdoc : subdocs) {
			log.debug(methodName, "Elaborazione subdocumento [" + subdoc.getUid() + "]");
			
			//######### Nuova versione.
			checkImportoAggiornabile(subdoc);
			SubdocumentoEntrata subdocAttuale = subdocumentoEntrataDad.findSubdocumentoEntrataById(subdoc.getUid());
			BigDecimal importoPrecedente =  subdocAttuale.getImporto();
			aggiornaImportoDocPadre(subdoc, importoPrecedente);
			
			//Imposto il nuovo importo
			subdocAttuale.setImporto(subdoc.getImporto());
			
			// SIAC-5043: Imposto provvisorio cassa
			if(entitaConUid(subdoc.getProvvisorioCassa())) {
				log.debug(methodName, "Override del provvisorio di cassa per il subdoc " + subdoc.getUid() + ", nuovo valore: " + subdoc.getProvvisorioCassa().getUid());
				subdocAttuale.setProvvisorioCassa(subdoc.getProvvisorioCassa());
			}
			
			dscg.aggiornaQuotaDocumentoEntrata(subdocAttuale,false);
		}
		
		for(DocumentoEntrata doc : req.getDocumentiReferenziatiDaiSubdocumenti()){
			log.debug(methodName, "Elaborazione documento [" + doc.getUid() + "]");
			DocumentoEntrata statoOperativoDocumento = aggiornaStatoOperativoDocumento(doc.getUid());
			doc.setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());	
			doc.setDataInizioValiditaStato(statoOperativoDocumento.getDataInizioValiditaStato());
		}			
		
		res.setSubdocumentiEntrata(subdocs);
	}

	private void aggiornaImportoDocPadre(SubdocumentoEntrata subdoc, BigDecimal importoPrecedente) {
		documentoDad.updateImportoDocumento(subdoc.getImporto(), importoPrecedente, subdoc.getDocumento());
		
	}

	private void checkImportoAggiornabile(SubdocumentoEntrata subdoc) {
		final String methodName = "checkImportoAggiornabile";
		TipoDocumento tipo = documentoEntrataDad.findTipoDocumentoByIdDocumento(subdoc.getDocumento().getUid());
		TipoDocumento tipoALG = documentoEntrataDad.findTipoDocumentoAllegatoAtto();
		if(tipo == null || tipoALG.getUid() != tipo.getUid()){
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento importo", "il documento non e' di tipo ALG", Esito.FALLIMENTO));
		}
		log.debug(methodName, "Tipo del documento " + tipo.getCodice() + " e' aggiornabile");
	}

}

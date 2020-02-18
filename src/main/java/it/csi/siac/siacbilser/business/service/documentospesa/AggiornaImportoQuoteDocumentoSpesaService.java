/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.documentospesa;

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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * The Class AggiornaImportoDaDedurreQuoteDocumentoSpesaService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaImportoQuoteDocumentoSpesaService extends CrudDocumentoDiSpesaBaseService<AggiornaImportiQuoteDocumentoSpesa, AggiornaImportiQuoteDocumentoSpesaResponse> {
	
	@Autowired
	private DocumentoDad documentoDad;
	
	/** The subdocs. */
	private List<SubdocumentoSpesa> subdocs;
	
	private DocumentoServiceCallGroup dscg;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		
		subdocs = req.getSubdocumentiSpesa();
		
		checkNotNull(subdocs, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti spesa"));
		checkCondition(!subdocs.isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("lista subdocumenti spesa (vuota)"));

		for(SubdocumentoSpesa subdoc : subdocs) {
			checkEntita(subdoc, "subdocumento spesa");
//			checkEntita(subdoc.getEnte(), "ente subdocumento spesa");
			checkEntita(subdoc.getDocumento(), "documento subdocumento spesa");
			
			checkNotNull(subdoc.getImporto(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo subdocumento"));
			//checkNotNull(subdoc.getImportoDaPagare(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("importo da pagare subdocumento"));
		}
		
		checkEntita(req.getBilancio(), "bilancio");
		bilancio = req.getBilancio();
	}
	
	@Override
	protected void init() {
		super.init();
		subdocumentoSpesaDad.setLoginOperazione(loginOperazione);
		subdocumentoSpesaDad.setEnte(ente);
		documentoSpesaDad.setLoginOperazione(loginOperazione);
		documentoSpesaDad.setEnte(ente);
		
		this.dscg = new DocumentoServiceCallGroup(super.serviceExecutor, req.getRichiedente(), ente, req.getBilancio());
	}
	
	@Override
	@Transactional
	public AggiornaImportiQuoteDocumentoSpesaResponse executeService(AggiornaImportiQuoteDocumentoSpesa serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		final String methodName = "execute";
		
		for(SubdocumentoSpesa subdoc : subdocs) {
			log.debug(methodName, "Elaborazione subdocumento [" + subdoc.getUid() + "]");
			
			//######### Nuova versione.
			checkImportoAggiornabile(subdoc);
			SubdocumentoSpesa subdocAttuale = subdocumentoSpesaDad.findSubdocumentoSpesaById(subdoc.getUid());
			BigDecimal importoPrecedente =  subdocAttuale.getImporto();
			aggiornaImportoDocPadre(subdoc, importoPrecedente);
			
			//Imposto il nuovo importo
			subdocAttuale.setImporto(subdoc.getImporto());
			// SIAC-5012: Imposto modalita' pagamento soggetto
			if(entitaConUid(subdoc.getModalitaPagamentoSoggetto())) {
				log.debug(methodName, "Override della modalita' pagamento soggetto per il subdoc " + subdoc.getUid() + ", nuovo valore: " + subdoc.getModalitaPagamentoSoggetto().getUid());
				subdocAttuale.setModalitaPagamentoSoggetto(subdoc.getModalitaPagamentoSoggetto());
			}
			// SIAC-5043: Imposto provvisorio cassa
			if(entitaConUid(subdoc.getProvvisorioCassa())) {
				log.debug(methodName, "Override del provvisorio di cassa per il subdoc " + subdoc.getUid() + ", nuovo valore: " + subdoc.getProvvisorioCassa().getUid());
				subdocAttuale.setProvvisorioCassa(subdoc.getProvvisorioCassa());
			} else {
				// SIAC-5842
				log.debug(methodName, "Override del provvisorio di cassa per il subdoc " + subdoc.getUid() + ", nuovo valore: NULL");
				subdocAttuale.setProvvisorioCassa(null);
			}
			
			dscg.aggiornaQuotaDocumentoSpesa(subdocAttuale,false);
		}
		
		for(DocumentoSpesa doc : req.getDocumentiReferenziatiDaiSubdocumenti()){
			log.debug(methodName, "Elaborazione documento AggiornaQuotaDocumentoSpesaService[" + doc.getUid() + "]");
			DocumentoSpesa statoOperativoDocumento = aggiornaStatoOperativoDocumento(doc.getUid()).getDocumentoSpesa();
			doc.setStatoOperativoDocumento(statoOperativoDocumento.getStatoOperativoDocumento());
			doc.setDataInizioValiditaStato(statoOperativoDocumento.getDataInizioValiditaStato());
		}
		
		res.setSubdocumentiSpesa(subdocs);
	}

	private void aggiornaImportoDocPadre(SubdocumentoSpesa subdoc, BigDecimal importoPrecedente) {
		documentoDad.updateImportoDocumento(subdoc.getImporto(), importoPrecedente, subdoc.getDocumento());
		
	}

	private void checkImportoAggiornabile(SubdocumentoSpesa subdoc) {
		final String methodName = "checkImportoAggiornabile";
		TipoDocumento tipo = documentoSpesaDad.findTipoDocumentoByIdDocumento(subdoc.getDocumento().getUid());
		TipoDocumento tipoALG = documentoSpesaDad.findTipoDocumentoAllegatoAtto();
		if(tipo == null || tipoALG.getUid() != tipo.getUid()){
			throw new BusinessException(ErroreFin.OPERAZIONE_NON_COMPATIBILE.getErrore("Aggiornamento importo", "il documento non e' di tipo ALG", Esito.FALLIMENTO));
		}
		log.debug(methodName, "Tipo del documento " + tipo.getCodice() + " e' aggiornabile");
	}

}

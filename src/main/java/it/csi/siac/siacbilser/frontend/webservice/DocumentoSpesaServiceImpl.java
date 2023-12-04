/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaAttributiQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaImportoDaDedurreQuoteDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaImportoQuoteDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaNotaCreditoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaOnereSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaTestataDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AnnullaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AnnullaNotaCreditoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AssociaProvvisorioQuoteSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AttivaRegistrazioniContabiliSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.EliminaOnereSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.EliminaQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceDocumentoPerProvvisoriSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceNotaCreditoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceQuotaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceTestataDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisciOnereSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.ProporzionaImportiSplitReverseService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDettaglioDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDettaglioQuotaSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDocumentiCollegatiByDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaModulareDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaOnereByDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaPuntualeDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuotaSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuoteByDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuoteDaEmettereSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaQuotePerProvvisorioSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaSinteticaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaSinteticaModulareDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaSinteticaModulareQuoteByDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaSinteticaModulareQuoteSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaSinteticaTestataDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.SpezzaQuotaSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAttributiQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAttributiQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOnereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOnereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciOnereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciOnereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ProporzionaImportiSplitReverse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ProporzionaImportiSplitReverseResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOnereByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaSpesaResponse;

/**
 * Implementazione del web service DocumentoSpesaService.
 *
 * @author Domenico
 */
@WebService(serviceName = "DocumentoSpesaService", portName = "DocumentoSpesaServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService")
public class DocumentoSpesaServiceImpl implements DocumentoSpesaService {
	
	/** The app ctx. */
	@Autowired
	private ApplicationContext appCtx;
	
	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public RicercaPuntualeDocumentoSpesaResponse ricercaPuntualeDocumentoSpesa(RicercaPuntualeDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeDocumentoSpesaService.class, parameters);
	}

	@Override
	public RicercaSinteticaDocumentoSpesaResponse ricercaSinteticaDocumentoSpesa(RicercaSinteticaDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaDocumentoSpesaService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaDocumentoSpesaResponse ricercaSinteticaTestataDocumentoSpesa(RicercaSinteticaDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaTestataDocumentoSpesaService.class, parameters);
	}

	@Override
	public RicercaDettaglioDocumentoSpesaResponse ricercaDettaglioDocumentoSpesa(RicercaDettaglioDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioDocumentoSpesaService.class, parameters);
	}

	@Override
	public RicercaModulareDocumentoSpesaResponse ricercaModulareDocumentoSpesa(RicercaModulareDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaModulareDocumentoSpesaService.class, parameters);
	}

	@Override
	public InserisceDocumentoSpesaResponse inserisceDocumentoSpesa(InserisceDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceDocumentoSpesaService.class, parameters);
	}
	
	@Override
	public InserisceDocumentoSpesaResponse inserisceTestataDocumentoSpesa(InserisceDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceTestataDocumentoSpesaService.class, parameters);
	}

	@Override
	public InserisceQuotaDocumentoSpesaResponse inserisceQuotaDocumentoSpesa(InserisceQuotaDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceQuotaDocumentoSpesaService.class, parameters);
	}

	@Override
	public AggiornaQuotaDocumentoSpesaResponse aggiornaQuotaDocumentoSpesa(AggiornaQuotaDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaQuotaDocumentoSpesaService.class, parameters);
	}

	@Override
	public AggiornaDocumentoDiSpesaResponse aggiornaDocumentoDiSpesa(AggiornaDocumentoDiSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaDocumentoDiSpesaService.class, parameters);
	}
	
	@Override
	public AggiornaDocumentoDiSpesaResponse aggiornaTestataDocumentoDiSpesa(AggiornaDocumentoDiSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaTestataDocumentoDiSpesaService.class, parameters);
	}	
	
	@Override
	public AggiornaImportiQuoteDocumentoSpesaResponse aggiornaImportoQuoteDocumentoSpesa(AggiornaImportiQuoteDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaImportoQuoteDocumentoSpesaService.class, parameters);
	}
	
	@Override
	public AggiornaImportiQuoteDocumentoSpesaResponse aggiornaImportoDaDedurreQuoteDocumentoSpesa(AggiornaImportiQuoteDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaImportoDaDedurreQuoteDocumentoSpesaService.class, parameters);
	}	

	@Override
	public AggiornaStatoDocumentoDiSpesaResponse aggiornaStatoDocumentoDiSpesa(AggiornaStatoDocumentoDiSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaStatoDocumentoDiSpesaService.class, parameters);
	}

	@Override
	public AnnullaDocumentoSpesaResponse annullaDocumentoSpesa(AnnullaDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaDocumentoSpesaService.class, parameters);
	}
	
	@Override
	public AnnullaNotaCreditoSpesaResponse annullaNotaCreditoSpesa(AnnullaNotaCreditoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaNotaCreditoSpesaService.class, parameters);
	}

	@Override
	public EliminaQuotaDocumentoSpesaResponse eliminaQuotaDocumentoSpesa(EliminaQuotaDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaQuotaDocumentoSpesaService.class, parameters);
	}

	@Override
	public RicercaQuotaSpesaResponse ricercaQuotaSpesa(RicercaQuotaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaQuotaSpesaService.class, parameters);
	}
	
	@Override
	public RicercaQuoteDaEmettereSpesaResponse ricercaQuoteDaEmettereSpesa(RicercaQuoteDaEmettereSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaQuoteDaEmettereSpesaService.class, parameters);
	}

	@Override
	public RicercaQuoteByDocumentoSpesaResponse ricercaQuoteByDocumentoSpesa(RicercaQuoteByDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaQuoteByDocumentoSpesaService.class, parameters);
	}

	@Override
	public RicercaDettaglioQuotaSpesaResponse ricercaDettaglioQuotaSpesa(RicercaDettaglioQuotaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioQuotaSpesaService.class, parameters);
	}

	@Override
	public InserisciOnereSpesaResponse inserisciOnereSpesa(InserisciOnereSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciOnereSpesaService.class, parameters);
	}

	@Override
	public AggiornaOnereSpesaResponse aggiornaOnereSpesa(AggiornaOnereSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaOnereSpesaService.class, parameters);
	}

	@Override
	public EliminaOnereSpesaResponse eliminaOnereSpesa(EliminaOnereSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaOnereSpesaService.class, parameters);
	}

	@Override
	public RicercaOnereByDocumentoSpesaResponse ricercaOnereByDocumentoSpesa(RicercaOnereByDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaOnereByDocumentoSpesaService.class, parameters);
	}

	@Override
	public InserisceNotaCreditoSpesaResponse inserisceNotaCreditoSpesa(InserisceNotaCreditoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceNotaCreditoSpesaService.class, parameters);
	}
	
	@Override
	public AggiornaNotaCreditoSpesaResponse aggiornaNotaCreditoSpesa(AggiornaNotaCreditoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaNotaCreditoSpesaService.class, parameters);
	}

	@Override
	public RicercaDocumentiCollegatiByDocumentoSpesaResponse ricercaDocumentiCollegatiByDocumentoSpesa(RicercaDocumentiCollegatiByDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDocumentiCollegatiByDocumentoSpesaService.class, parameters);
	}

	@Override
	public AggiornaAttributiQuotaDocumentoSpesaResponse aggiornaAttributiQuotaDocumentoSpesa(AggiornaAttributiQuotaDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaAttributiQuotaDocumentoSpesaService.class, parameters);
	}

	@Override
	public AttivaRegistrazioniContabiliSpesaResponse attivaRegistrazioniContabiliSpesa(AttivaRegistrazioniContabiliSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AttivaRegistrazioniContabiliSpesaService.class, parameters);
	}

	@Override
	public ProporzionaImportiSplitReverseResponse proporzionaImportiSplitReverse(ProporzionaImportiSplitReverse parameters) {
		return BaseServiceExecutor.execute(appCtx, ProporzionaImportiSplitReverseService.class, parameters);
	}
	
	@Override
	public RicercaQuotePerProvvisorioSpesaResponse ricercaQuotePerProvvisorioSpesa(RicercaQuotePerProvvisorioSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaQuotePerProvvisorioSpesaService.class, parameters);
	}

	@Override
	public InserisceDocumentoPerProvvisoriSpesaResponse inserisceDocumentoPerProvvisoriSpesa(InserisceDocumentoPerProvvisoriSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceDocumentoPerProvvisoriSpesaService.class, parameters);
	}

	@Override
	public AssociaProvvisorioQuoteSpesaResponse associaProvvisorioQuoteSpesa(AssociaProvvisorioQuoteSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AssociaProvvisorioQuoteSpesaService.class, parameters);
	}

	@Override
	public RicercaSinteticaModulareQuoteByDocumentoSpesaResponse ricercaSinteticaModulareQuoteByDocumentoSpesa(RicercaSinteticaModulareQuoteByDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaModulareQuoteByDocumentoSpesaService.class, parameters);
	}

	@Override
	public RicercaSinteticaModulareDocumentoSpesaResponse ricercaSinteticaModulareDocumentoSpesa(RicercaSinteticaModulareDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaModulareDocumentoSpesaService.class, parameters);
	}

	@Override
	public SpezzaQuotaSpesaResponse spezzaQuotaSpesa(SpezzaQuotaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, SpezzaQuotaSpesaService.class, parameters);
	}

	@Override
	public RicercaSinteticaModulareQuoteSpesaResponse ricercaSinteticaModulareQuoteSpesa(RicercaSinteticaModulareQuoteSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaModulareQuoteSpesaService.class, parameters);
	}

}

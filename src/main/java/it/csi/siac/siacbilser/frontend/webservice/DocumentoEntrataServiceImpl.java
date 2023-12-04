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
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaImportoDaDedurreQuoteDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaImportoQuoteDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaNotaCreditoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaTestataDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AnnullaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AnnullaNotaCreditoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AssociaProvvisorioQuoteEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AttivaRegistrazioniContabiliEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.EliminaQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.EmettiFatturaFelEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceDocumentoPerProvvisoriEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceNotaCreditoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceQuotaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.InserisceTestataDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaDettaglioDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaDettaglioQuotaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaModulareDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaPuntualeDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaQuotaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaQuoteByDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaQuoteDaEmettereEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaQuotePerProvvisorioEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaSinteticaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaSinteticaModulareDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaSinteticaModulareQuoteByDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaSinteticaModulareQuoteEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.RicercaSinteticaTestataDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.SpezzaQuotaEntrataService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaDocumentiCollegatiByDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AttivaRegistrazioniContabiliEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmettiFatturaFelEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmettiFatturaFelEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoPerProvvisoriEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaModulareDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteByDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareQuoteEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaEntrataResponse;


/**
 * The Class DocumentoEntrataServiceImpl.
 */
@WebService(serviceName = "DocumentoEntrataService", portName = "DocumentoEntrataServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService")
public class DocumentoEntrataServiceImpl implements DocumentoEntrataService {

	
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
	public RicercaPuntualeDocumentoEntrataResponse ricercaPuntualeDocumentoEntrata(RicercaPuntualeDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeDocumentoEntrataService.class, parameters);
	}

	@Override
	public RicercaSinteticaDocumentoEntrataResponse ricercaSinteticaDocumentoEntrata(RicercaSinteticaDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaDocumentoEntrataService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaDocumentoEntrataResponse ricercaSinteticaTestataDocumentoEntrata(RicercaSinteticaDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaTestataDocumentoEntrataService.class, parameters);
	}

	@Override
	public RicercaDettaglioDocumentoEntrataResponse ricercaDettaglioDocumentoEntrata(RicercaDettaglioDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioDocumentoEntrataService.class, parameters);
	}

	@Override
	public InserisceDocumentoEntrataResponse inserisceDocumentoEntrata(InserisceDocumentoEntrata parameters) {
		return appCtx.getBean("inserisceDocumentoEntrataService", InserisceDocumentoEntrataService.class).executeService(parameters);
	}
	
	@Override
	public InserisceDocumentoPerProvvisoriEntrataResponse inserisceDocumentoPerProvvisoriEntrata(InserisceDocumentoPerProvvisoriEntrata parameters) {
		return appCtx.getBean("inserisceDocumentoPerProvvisoriEntrataService", InserisceDocumentoPerProvvisoriEntrataService.class).executeService(parameters);
	}
	
	@Override
	public InserisceDocumentoEntrataResponse inserisceTestataDocumentoEntrata(InserisceDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceTestataDocumentoEntrataService.class, parameters);
	}

	@Override
	public InserisceQuotaDocumentoEntrataResponse inserisceQuotaDocumentoEntrata(InserisceQuotaDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceQuotaDocumentoEntrataService.class, parameters);
	}

	@Override
	public AggiornaQuotaDocumentoEntrataResponse aggiornaQuotaDocumentoEntrata(AggiornaQuotaDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaQuotaDocumentoEntrataService.class, parameters);
	}

	@Override
	public AggiornaDocumentoDiEntrataResponse aggiornaDocumentoDiEntrata(AggiornaDocumentoDiEntrata parameters) {
		return appCtx.getBean("aggiornaDocumentoDiEntrataService", AggiornaDocumentoDiEntrataService.class).executeService(parameters);
	}
	
	@Override
	public AggiornaDocumentoDiEntrataResponse aggiornaTestataDocumentoDiEntrata(AggiornaDocumentoDiEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaTestataDocumentoDiEntrataService.class, parameters);
	}

	@Override
	public AggiornaImportiQuoteDocumentoEntrataResponse aggiornaImportoQuoteDocumentoEntrata(AggiornaImportiQuoteDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaImportoQuoteDocumentoEntrataService.class, parameters);
	}
	
	@Override
	public AggiornaImportiQuoteDocumentoEntrataResponse aggiornaImportoDaDedurreQuoteDocumentoEntrata(AggiornaImportiQuoteDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaImportoDaDedurreQuoteDocumentoEntrataService.class, parameters);
	}

	@Override
	public AggiornaStatoDocumentoDiEntrataResponse aggiornaStatoDocumentoDiEntrata(AggiornaStatoDocumentoDiEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaStatoDocumentoDiEntrataService.class, parameters);
	}

	@Override
	public AnnullaDocumentoEntrataResponse annullaDocumentoEntrata(AnnullaDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaDocumentoEntrataService.class, parameters);
	}
	
	@Override
	public AnnullaNotaCreditoEntrataResponse annullaNotaCreditoEntrata(AnnullaNotaCreditoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaNotaCreditoEntrataService.class, parameters);
	}

	@Override
	public EliminaQuotaDocumentoEntrataResponse eliminaQuotaDocumentoEntrata(EliminaQuotaDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaQuotaDocumentoEntrataService.class, parameters);
	}

	@Override
	public RicercaQuotaEntrataResponse ricercaQuotaEntrata(RicercaQuotaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaQuotaEntrataService.class, parameters);
	}
	
	@Override
	public RicercaQuoteDaEmettereEntrataResponse ricercaQuoteDaEmettereEntrata(RicercaQuoteDaEmettereEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaQuoteDaEmettereEntrataService.class, parameters);
	}
	
	@Override
	public RicercaDettaglioQuotaEntrataResponse ricercaDettaglioQuotaEntrata(RicercaDettaglioQuotaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioQuotaEntrataService.class, parameters);
	}

	@Override
	public RicercaQuoteByDocumentoEntrataResponse ricercaQuoteByDocumentoEntrata(RicercaQuoteByDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaQuoteByDocumentoEntrataService.class, parameters);
	}

	@Override
	public InserisceNotaCreditoEntrataResponse inserisceNotaCreditoEntrata(InserisceNotaCreditoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceNotaCreditoEntrataService.class, parameters);
	}
	
	@Override
	public AggiornaNotaCreditoEntrataResponse aggiornaNotaCreditoEntrata(AggiornaNotaCreditoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaNotaCreditoEntrataService.class, parameters);
	}

	@Override
	public RicercaDocumentiCollegatiByDocumentoEntrataResponse ricercaDocumentiCollegatiByDocumentoEntrata(RicercaDocumentiCollegatiByDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDocumentiCollegatiByDocumentoEntrataService.class, parameters);
	}

	@Override
	public AttivaRegistrazioniContabiliEntrataResponse attivaRegistrazioniContabiliEntrata(AttivaRegistrazioniContabiliEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AttivaRegistrazioniContabiliEntrataService.class, parameters);
	}

	@Override
	public RicercaQuotePerProvvisorioEntrataResponse ricercaQuotePerProvvisorioEntrata(RicercaQuotePerProvvisorioEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaQuotePerProvvisorioEntrataService.class, parameters);
	}

	@Override
	public AssociaProvvisorioQuoteEntrataResponse associaProvvisorioQuoteEntrata(AssociaProvvisorioQuoteEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AssociaProvvisorioQuoteEntrataService.class, parameters);
	}

	@Override
	public RicercaSinteticaModulareQuoteByDocumentoEntrataResponse ricercaSinteticaModulareQuoteByDocumentoEntrata(RicercaSinteticaModulareQuoteByDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaModulareQuoteByDocumentoEntrataService.class, parameters);
	}

	@Override
	public RicercaModulareDocumentoEntrataResponse ricercaModulareDocumentoEntrata(RicercaModulareDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaModulareDocumentoEntrataService.class, parameters);
	}

	@Override
	public RicercaSinteticaModulareDocumentoEntrataResponse ricercaSinteticaModulareDocumentoEntrata(RicercaSinteticaModulareDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaModulareDocumentoEntrataService.class, parameters);
	}

	@Override
	public SpezzaQuotaEntrataResponse spezzaQuotaEntrata(SpezzaQuotaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, SpezzaQuotaEntrataService.class, parameters);
	}

	@Override
	public RicercaSinteticaModulareQuoteEntrataResponse ricercaSinteticaModulareQuoteEntrata(RicercaSinteticaModulareQuoteEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaModulareQuoteEntrataService.class, parameters);
	}

	@Override
	public EmettiFatturaFelEntrataResponse emettiFatturaFelEntrata(EmettiFatturaFelEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, EmettiFatturaFelEntrataService.class, parameters);	
	}

}

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
import it.csi.siac.siacbilser.business.service.documentoivaentrata.AggiornaControregistrazioneService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.AggiornaNotaCreditoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.AggiornaQuotaIvaDifferitaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.AggiornaStatoSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.AggiornaSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.ContaDatiCollegatiSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.InserisceControregistrazioneService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.InserisceNotaCreditoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.InserisceQuotaIvaDifferitaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.InserisceSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.RicercaDettaglioSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.RicercaNoteCreditoIvaDocumentoEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.RicercaPuntualeSubdocumentoIvaEntrataService;
import it.csi.siac.siacbilser.business.service.documentoivaentrata.RicercaSinteticaSubdocumentoIvaEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaControregistrazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaControregistrazioneResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaIvaDifferitaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaIvaDifferitaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceControregistrazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceControregistrazioneResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaIvaDifferitaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaIvaDifferitaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaEntrataResponse;

/**
 * The Class DocumentoIvaEntrataServiceImpl.
 */
@WebService(serviceName = "DocumentoIvaEntrataService", portName = "DocumentoIvaEntrataServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaEntrataService")
public class DocumentoIvaEntrataServiceImpl implements DocumentoIvaEntrataService {
	
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
	public RicercaPuntualeSubdocumentoIvaEntrataResponse ricercaPuntualeSubdocumentoIvaEntrata(RicercaPuntualeSubdocumentoIvaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeSubdocumentoIvaEntrataService.class, parameters);
	}

	@Override
	public RicercaSinteticaSubdocumentoIvaEntrataResponse ricercaSinteticaSubdocumentoIvaEntrata(RicercaSinteticaSubdocumentoIvaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaSubdocumentoIvaEntrataService.class, parameters);
	}

	@Override
	public RicercaDettaglioSubdocumentoIvaEntrataResponse ricercaDettaglioSubdocumentoIvaEntrata(RicercaDettaglioSubdocumentoIvaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioSubdocumentoIvaEntrataService.class, parameters);
	}

	@Override
	public InserisceSubdocumentoIvaEntrataResponse inserisceSubdocumentoIvaEntrata(InserisceSubdocumentoIvaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceSubdocumentoIvaEntrataService.class, parameters);
	}

	@Override
	public AggiornaSubdocumentoIvaEntrataResponse aggiornaSubdocumentoIvaEntrata(AggiornaSubdocumentoIvaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaSubdocumentoIvaEntrataService.class, parameters);
	}
	
	@Override
	public InserisceNotaCreditoIvaEntrataResponse inserisceNotaCreditoIvaEntrata(InserisceNotaCreditoIvaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceNotaCreditoIvaEntrataService.class, parameters);
	}

	@Override
	public AggiornaNotaCreditoIvaEntrataResponse aggiornaNotaCreditoIvaEntrata(AggiornaNotaCreditoIvaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaNotaCreditoIvaEntrataService.class, parameters);
	}
	
	@Override
	public InserisceQuotaIvaDifferitaEntrataResponse inserisceQuotaIvaDifferitaEntrata(InserisceQuotaIvaDifferitaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceQuotaIvaDifferitaEntrataService.class, parameters);
	}

	@Override
	public AggiornaQuotaIvaDifferitaEntrataResponse aggiornaQuotaIvaDifferitaEntrata(AggiornaQuotaIvaDifferitaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaQuotaIvaDifferitaEntrataService.class, parameters);
	}
	
	@Override
	public AggiornaStatoSubdocumentoIvaEntrataResponse aggiornaStatoSubdocumentoIvaEntrata(AggiornaStatoSubdocumentoIvaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaStatoSubdocumentoIvaEntrataService.class, parameters);
	}
	
	@Override
	public InserisceControregistrazioneResponse inserisceControregistrazione(InserisceControregistrazione parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceControregistrazioneService.class, parameters);
	}
	
	@Override
	public AggiornaControregistrazioneResponse aggiornaControregistrazione(AggiornaControregistrazione parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaControregistrazioneService.class, parameters);
	}
	
	@Override
	public RicercaNoteCreditoIvaDocumentoEntrataResponse ricercaNoteCreditoIvaDocumentoEntrata(RicercaNoteCreditoIvaDocumentoEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaNoteCreditoIvaDocumentoEntrataService.class, parameters);
	}

	@Override
	public ContaDatiCollegatiSubdocumentoIvaEntrataResponse contaDatiCollegatiSubdocumentoIvaEntrata(ContaDatiCollegatiSubdocumentoIvaEntrata parameters) {
		return BaseServiceExecutor.execute(appCtx, ContaDatiCollegatiSubdocumentoIvaEntrataService.class, parameters);
	}
	

}

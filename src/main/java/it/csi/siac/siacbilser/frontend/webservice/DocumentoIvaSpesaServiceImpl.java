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
import it.csi.siac.siacbilser.business.service.documentoivaspesa.AggiornaNotaCreditoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.AggiornaQuotaIvaDifferitaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.AggiornaStatoSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.AggiornaSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.ContaDatiCollegatiSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.InserisceNotaCreditoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.InserisceQuotaIvaDifferitaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.InserisceSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.RicercaDettaglioSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.RicercaNoteCreditoIvaDocumentoSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.RicercaPuntualeSubdocumentoIvaSpesaService;
import it.csi.siac.siacbilser.business.service.documentoivaspesa.RicercaSinteticaSubdocumentoIvaSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaIvaDifferitaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaIvaDifferitaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaIvaDifferitaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceQuotaIvaDifferitaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNoteCreditoIvaDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaPuntualeSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaSubdocumentoIvaSpesaResponse;

/**
 * The Class DocumentoIvaSpesaServiceImpl.
 */
@WebService(serviceName = "DocumentoIvaSpesaService", portName = "DocumentoIvaSpesaServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaSpesaService")
public class DocumentoIvaSpesaServiceImpl implements DocumentoIvaSpesaService {
	
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
	public RicercaPuntualeSubdocumentoIvaSpesaResponse ricercaPuntualeSubdocumentoIvaSpesa(RicercaPuntualeSubdocumentoIvaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeSubdocumentoIvaSpesaService.class, parameters);
	}

	@Override
	public RicercaSinteticaSubdocumentoIvaSpesaResponse ricercaSinteticaSubdocumentoIvaSpesa(RicercaSinteticaSubdocumentoIvaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaSubdocumentoIvaSpesaService.class, parameters);
	}

	@Override
	public RicercaDettaglioSubdocumentoIvaSpesaResponse ricercaDettaglioSubdocumentoIvaSpesa(RicercaDettaglioSubdocumentoIvaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioSubdocumentoIvaSpesaService.class, parameters);
	}

	@Override
	public InserisceSubdocumentoIvaSpesaResponse inserisceSubdocumentoIvaSpesa(InserisceSubdocumentoIvaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceSubdocumentoIvaSpesaService.class, parameters);
	}

	@Override
	public AggiornaSubdocumentoIvaSpesaResponse aggiornaSubdocumentoIvaSpesa(AggiornaSubdocumentoIvaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaSubdocumentoIvaSpesaService.class, parameters);
	}
	
	@Override
	public InserisceNotaCreditoIvaSpesaResponse inserisceNotaCreditoIvaSpesa(InserisceNotaCreditoIvaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceNotaCreditoIvaSpesaService.class, parameters);
	}

	@Override
	public AggiornaNotaCreditoIvaSpesaResponse aggiornaNotaCreditoIvaSpesa(AggiornaNotaCreditoIvaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaNotaCreditoIvaSpesaService.class, parameters);
	}
	
	@Override
	public InserisceQuotaIvaDifferitaSpesaResponse inserisceQuotaIvaDifferitaSpesa(InserisceQuotaIvaDifferitaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceQuotaIvaDifferitaSpesaService.class, parameters);
	}

	@Override
	public AggiornaQuotaIvaDifferitaSpesaResponse aggiornaQuotaIvaDifferitaSpesa(AggiornaQuotaIvaDifferitaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaQuotaIvaDifferitaSpesaService.class, parameters);
	}
	
	@Override
	public AggiornaStatoSubdocumentoIvaSpesaResponse aggiornaStatoSubdocumentoIvaSpesa(AggiornaStatoSubdocumentoIvaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaStatoSubdocumentoIvaSpesaService.class, parameters);
	}
	
	@Override
	public RicercaNoteCreditoIvaDocumentoSpesaResponse ricercaNoteCreditoIvaDocumentoSpesa(RicercaNoteCreditoIvaDocumentoSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaNoteCreditoIvaDocumentoSpesaService.class, parameters);
	}

	@Override
	public ContaDatiCollegatiSubdocumentoIvaSpesaResponse contaDatiCollegatiSubdocumentoIvaSpesa(ContaDatiCollegatiSubdocumentoIvaSpesa parameters) {
		return BaseServiceExecutor.execute(appCtx, ContaDatiCollegatiSubdocumentoIvaSpesaService.class, parameters);
		}

}

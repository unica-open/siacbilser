/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacfinser.business.service.carta.AggiornaCartaContabileService;
import it.csi.siac.siacfinser.business.service.carta.AnnullaCartaContabileService;
import it.csi.siac.siacfinser.business.service.carta.CollegaQuotaDocumentoARigaCartaService;
import it.csi.siac.siacfinser.business.service.carta.InserisceCartaContabileService;
import it.csi.siac.siacfinser.business.service.carta.RegolarizzaCartaContabileService;
import it.csi.siac.siacfinser.business.service.carta.RicercaCartaContabilePerChiaveService;
import it.csi.siac.siacfinser.business.service.carta.RicercaCartaContabileService;
import it.csi.siac.siacfinser.business.service.carta.RicercaDocumentiCartaService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaQuotaDocumentoARigaCarta;
import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaQuotaDocumentoARigaCartaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RegolarizzaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RegolarizzaCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabile;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabilePerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCartaContabileResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDocumentiCarta;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDocumentiCartaResponse;


@WebService(serviceName = "CartaContabileService", 
portName = "CartaContabileServicePort", 
targetNamespace = FINSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.CartaContabileService")
public class CartaContabileServiceImpl extends AbstractService implements CartaContabileService {
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	@WebMethod
	public  @WebResult InserisceCartaContabileResponse inserisceCartaContabile(@WebParam  InserisceCartaContabile request) {
		return new ServiceExecutor<InserisceCartaContabileResponse, InserisceCartaContabile>("inserisceCartaContabile") {
			@Override
			InserisceCartaContabileResponse executeService(InserisceCartaContabile request) {
				return appCtx.getBean(InserisceCartaContabileService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult AggiornaCartaContabileResponse aggiornaCartaContabile(@WebParam  AggiornaCartaContabile request) {
		return new ServiceExecutor<AggiornaCartaContabileResponse, AggiornaCartaContabile>("aggiornaCartaContabile") {
			@Override
			AggiornaCartaContabileResponse executeService(AggiornaCartaContabile request) {
				return appCtx.getBean(AggiornaCartaContabileService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaCartaContabileResponse ricercaCartaContabile(@WebParam  RicercaCartaContabile request) {
		return new ServiceExecutor<RicercaCartaContabileResponse, RicercaCartaContabile>("ricercaCartaContabile") {
			@Override
			RicercaCartaContabileResponse executeService(RicercaCartaContabile request) {
				return appCtx.getBean(RicercaCartaContabileService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult AnnullaCartaContabileResponse annullaCartaContabile(@WebParam  AnnullaCartaContabile request) {
		return new ServiceExecutor<AnnullaCartaContabileResponse, AnnullaCartaContabile>("annullaCartaContabile") {
			@Override
			AnnullaCartaContabileResponse executeService(AnnullaCartaContabile request) {
				return appCtx.getBean(AnnullaCartaContabileService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public  @WebResult RegolarizzaCartaContabileResponse regolarizzaCartaContabile(@WebParam  RegolarizzaCartaContabile request) {
		return new ServiceExecutor<RegolarizzaCartaContabileResponse, RegolarizzaCartaContabile>("regolarizzaCartaContabile") {
			@Override
			RegolarizzaCartaContabileResponse executeService(RegolarizzaCartaContabile request) {
				return appCtx.getBean(RegolarizzaCartaContabileService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaCartaContabilePerChiaveResponse ricercaCartaContabilePerChiave(@WebParam  RicercaCartaContabilePerChiave request) {
		return new ServiceExecutor<RicercaCartaContabilePerChiaveResponse, RicercaCartaContabilePerChiave>("ricercaCartaContabilePerChiave") {
			@Override
			RicercaCartaContabilePerChiaveResponse executeService(RicercaCartaContabilePerChiave request) {
				return appCtx.getBean(RicercaCartaContabilePerChiaveService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaDocumentiCartaResponse ricercaDocumentiCarta(@WebParam  RicercaDocumentiCarta request) {
		return new ServiceExecutor<RicercaDocumentiCartaResponse, RicercaDocumentiCarta>("ricercaDocumentiCarta") {
			@Override
			RicercaDocumentiCartaResponse executeService(RicercaDocumentiCarta request) {
				return appCtx.getBean(RicercaDocumentiCartaService.class).executeService(request);
			}
		}.execute(request);
	}
	
	
	@Override
	@WebMethod
	public  @WebResult CollegaQuotaDocumentoARigaCartaResponse collegaQuotaDocumentoARigaCarta(@WebParam  CollegaQuotaDocumentoARigaCarta request) {
		return new ServiceExecutor<CollegaQuotaDocumentoARigaCartaResponse, CollegaQuotaDocumentoARigaCarta>("collegaQuotaDocumentoARigaCarta") {
			@Override
			CollegaQuotaDocumentoARigaCartaResponse executeService(CollegaQuotaDocumentoARigaCarta request) {
				return appCtx.getBean(CollegaQuotaDocumentoARigaCartaService.class).executeService(request);
			}
		}.execute(request);
	}
	
	
}

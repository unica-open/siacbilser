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

import it.csi.siac.siacfinser.business.service.provvisorio.AggiornaProvvisorioDiCassaService;
import it.csi.siac.siacfinser.business.service.provvisorio.AggiornaSacProvvisoriDiCassaService;
import it.csi.siac.siacfinser.business.service.provvisorio.InserisciProvvisorioDiCassaService;
import it.csi.siac.siacfinser.business.service.provvisorio.RicercaProvvisoriDiCassaService;
import it.csi.siac.siacfinser.business.service.provvisorio.RicercaProvvisorioDiCassaPerChiaveService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaProvvisorioDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaProvvisorioDiCassaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSacProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSacProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciProvvisorioDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciProvvisorioDiCassaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiaveResponse;

@WebService(serviceName = "ProvvisorioService", 
portName = "ProvvisorioServicePort", 
targetNamespace = FINSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService")
public class ProvvisorioServiceImpl extends AbstractService implements ProvvisorioService {
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	@WebMethod
	public  @WebResult RicercaProvvisoriDiCassaResponse ricercaProvvisoriDiCassa(@WebParam  RicercaProvvisoriDiCassa request) {
		return new ServiceExecutor<RicercaProvvisoriDiCassaResponse, RicercaProvvisoriDiCassa>("ricercaProvvisoriDiCassa") {
			@Override
			RicercaProvvisoriDiCassaResponse executeService(RicercaProvvisoriDiCassa request) {
				return appCtx.getBean(RicercaProvvisoriDiCassaService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaProvvisorioDiCassaPerChiaveResponse ricercaProvvisorioDiCassaPerChiave(@WebParam  RicercaProvvisorioDiCassaPerChiave request) {
		return new ServiceExecutor<RicercaProvvisorioDiCassaPerChiaveResponse, RicercaProvvisorioDiCassaPerChiave>("ricercaProvvisorioDiCassaPerChiave") {
			@Override
			RicercaProvvisorioDiCassaPerChiaveResponse executeService(RicercaProvvisorioDiCassaPerChiave request) {
				return appCtx.getBean(RicercaProvvisorioDiCassaPerChiaveService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult InserisciProvvisorioDiCassaResponse inserisciProvvisorioDiCassa(@WebParam  InserisciProvvisorioDiCassa request) {
		return new ServiceExecutor<InserisciProvvisorioDiCassaResponse, InserisciProvvisorioDiCassa>("inserisciProvvisorioDiCassa") {
			@Override
			InserisciProvvisorioDiCassaResponse executeService(InserisciProvvisorioDiCassa request) {
				return appCtx.getBean(InserisciProvvisorioDiCassaService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult AggiornaProvvisorioDiCassaResponse aggiornaProvvisorioDiCassa(@WebParam  AggiornaProvvisorioDiCassa request) {
		return new ServiceExecutor<AggiornaProvvisorioDiCassaResponse, AggiornaProvvisorioDiCassa>("aggiornaProvvisorioDiCassa") {
			@Override
			AggiornaProvvisorioDiCassaResponse executeService(AggiornaProvvisorioDiCassa request) {
				return appCtx.getBean(AggiornaProvvisorioDiCassaService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult AggiornaSacProvvisoriDiCassaResponse aggiornaSacProvvisoriDiCassa(@WebParam  AggiornaSacProvvisoriDiCassa request) {
		return new ServiceExecutor<AggiornaSacProvvisoriDiCassaResponse, AggiornaSacProvvisoriDiCassa>("aggiornaSacProvvisoriDiCassa") {
			@Override
			AggiornaSacProvvisoriDiCassaResponse executeService(AggiornaSacProvvisoriDiCassa request) {
				return appCtx.getBean(AggiornaSacProvvisoriDiCassaService.class).executeService(request);
			}
		}.execute(request);
	}
	
}

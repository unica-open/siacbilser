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

import it.csi.siac.siacfinser.business.service.mutuo.AggiornaMutuoService;
import it.csi.siac.siacfinser.business.service.mutuo.AnnullaMutuoService;
import it.csi.siac.siacfinser.business.service.mutuo.InserisceMutuoService;
import it.csi.siac.siacfinser.business.service.mutuo.RicercaMutuoPerChiaveService;
import it.csi.siac.siacfinser.business.service.mutuo.RicercaMutuoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaMutuoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMutuoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceMutuoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaMutuoResponse;


@WebService(serviceName = "MutuoService", 
portName = "MutuoServicePort", 
targetNamespace = FINSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.MutuoService")
public class MutuoServiceImpl extends AbstractService implements MutuoService {
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	@WebMethod
	public  @WebResult InserisceMutuoResponse inserisceMutuo(@WebParam  InserisceMutuo request) {
		return new ServiceExecutor<InserisceMutuoResponse, InserisceMutuo>("inserisceMutuo", true) {
			@Override
			InserisceMutuoResponse executeService(InserisceMutuo request) {
				return appCtx.getBean(InserisceMutuoService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public  @WebResult AggiornaMutuoResponse aggiornaMutuo(@WebParam  AggiornaMutuo request) {
		return new ServiceExecutor<AggiornaMutuoResponse, AggiornaMutuo>("aggiornaMutuo", true) {
			@Override
			AggiornaMutuoResponse executeService(AggiornaMutuo request) {
				return appCtx.getBean(AggiornaMutuoService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public  @WebResult RicercaMutuoResponse ricercaMutuo(@WebParam  RicercaMutuo request) {
		return new ServiceExecutor<RicercaMutuoResponse, RicercaMutuo>("ricercaMutuo") {
			@Override
			RicercaMutuoResponse executeService(RicercaMutuo request) {
				return appCtx.getBean(RicercaMutuoService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public  @WebResult RicercaMutuoPerChiaveResponse ricercaMutuoPerChiave(@WebParam  RicercaMutuoPerChiave request) {
		return new ServiceExecutor<RicercaMutuoPerChiaveResponse, RicercaMutuoPerChiave>("ricercaMutuoPerChiave") {
			@Override
			RicercaMutuoPerChiaveResponse executeService(RicercaMutuoPerChiave request) {
				return appCtx.getBean(RicercaMutuoPerChiaveService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public  @WebResult AnnullaMutuoResponse annullaMutuo(@WebParam  AnnullaMutuo request) {
		return new ServiceExecutor<AnnullaMutuoResponse, AnnullaMutuo>("annullaMutuo") {
			@Override
			AnnullaMutuoResponse executeService(AnnullaMutuo request) {
				return appCtx.getBean(AnnullaMutuoService.class).executeService(request);
			}
		}.execute(request);
	}
}
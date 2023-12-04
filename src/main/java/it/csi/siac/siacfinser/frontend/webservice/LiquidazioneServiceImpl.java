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

import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacfinser.business.service.liquidazione.AggiornaLiquidazioneModulareService;
import it.csi.siac.siacfinser.business.service.liquidazione.AggiornaLiquidazioneService;
import it.csi.siac.siacfinser.business.service.liquidazione.AnnullaLiquidazioneService;
import it.csi.siac.siacfinser.business.service.liquidazione.CalcolaDisponibilitaALiquidareService;
import it.csi.siac.siacfinser.business.service.liquidazione.InserisceLiquidazioneService;
import it.csi.siac.siacfinser.business.service.liquidazione.RicercaLiquidazioneConAllegatoAttoService;
import it.csi.siac.siacfinser.business.service.liquidazione.RicercaLiquidazionePerChiaveService;
import it.csi.siac.siacfinser.business.service.liquidazione.RicercaLiquidazioniService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneModulare;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneModulareResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaDisponibilitaALiquidare;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaDisponibilitaALiquidareResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceLiquidazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioneConAllegatoAtto;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioneConAllegatoAttoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazionePerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaLiquidazioniResponse;

@Component("liquidazioneService")
@WebService(serviceName = "LiquidazioneService", 
portName = "LiquidazioneServicePort", 
targetNamespace = FINSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.LiquidazioneService")
public class LiquidazioneServiceImpl extends AbstractService implements LiquidazioneService {
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	@WebMethod
	public  @WebResult RicercaLiquidazioniResponse ricercaLiquidazioni(@WebParam  RicercaLiquidazioni request) {
		return new ServiceExecutor<RicercaLiquidazioniResponse, RicercaLiquidazioni>("ricercaLiquidazioni") {
			@Override
			RicercaLiquidazioniResponse executeService(RicercaLiquidazioni request) {
				return appCtx.getBean(RicercaLiquidazioniService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult InserisceLiquidazioneResponse inserisceLiquidazione(@WebParam InserisceLiquidazione request) {
		return new ServiceExecutor<InserisceLiquidazioneResponse, InserisceLiquidazione>("inserisceLiquidazione") {
			@Override
			InserisceLiquidazioneResponse executeService(InserisceLiquidazione request) {
				return appCtx.getBean(InserisceLiquidazioneService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaLiquidazionePerChiaveResponse ricercaLiquidazionePerChiave(@WebParam  RicercaLiquidazionePerChiave request) {
		return new ServiceExecutor<RicercaLiquidazionePerChiaveResponse, RicercaLiquidazionePerChiave>("ricercaLiquidazionePerChiave") {
			@Override
			RicercaLiquidazionePerChiaveResponse executeService(RicercaLiquidazionePerChiave request) {
				return appCtx.getBean(RicercaLiquidazionePerChiaveService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public  @WebResult AnnullaLiquidazioneResponse annullaLiquidazione(@WebParam  AnnullaLiquidazione request) {
		return new ServiceExecutor<AnnullaLiquidazioneResponse, AnnullaLiquidazione>("annullaLiquidazione") {
			@Override
			AnnullaLiquidazioneResponse executeService(AnnullaLiquidazione request) {
				return appCtx.getBean(AnnullaLiquidazioneService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult AggiornaLiquidazioneResponse aggiornaLiquidazione(@WebParam AggiornaLiquidazione request) {
		return new ServiceExecutor<AggiornaLiquidazioneResponse, AggiornaLiquidazione>("aggiornaLiquidazione") {
			@Override
			AggiornaLiquidazioneResponse executeService(AggiornaLiquidazione request) {
				return appCtx.getBean(AggiornaLiquidazioneService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaLiquidazioneConAllegatoAttoResponse ricercaLiquidazioneConAllegatoAtto(
			@WebParam RicercaLiquidazioneConAllegatoAtto request) {
		return appCtx.getBean(RicercaLiquidazioneConAllegatoAttoService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	AggiornaLiquidazioneModulareResponse aggiornaLiquidazioneModulare(
			@WebParam AggiornaLiquidazioneModulare request) {
		
		return appCtx.getBean(AggiornaLiquidazioneModulareService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	CalcolaDisponibilitaALiquidareResponse calcolaDisponibilitaALiquidare(
			@WebParam CalcolaDisponibilitaALiquidare request) {
		return appCtx.getBean(CalcolaDisponibilitaALiquidareService.class).executeService(request);
	}
}

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfinser.business.service.common.CercaProgettoService;
import it.csi.siac.siacfinser.business.service.common.FindComunePerNomeService;
import it.csi.siac.siacfinser.business.service.common.ListaSedimeService;
import it.csi.siac.siacfinser.business.service.common.ListeService;
import it.csi.siac.siacfinser.business.service.common.RicercaAccountPerChiaveService;
import it.csi.siac.siacfinser.business.service.common.RicercaGruppoTipoAccreditoPkService;
import it.csi.siac.siacfinser.business.service.soggetto.CaricaDatiVisibilitaSacCapitoloService;
import it.csi.siac.siacfinser.business.service.soggetto.CercaComuniService;
import it.csi.siac.siacfinser.frontend.webservice.msg.CaricaDatiVisibilitaSacCapitolo;
import it.csi.siac.siacfinser.frontend.webservice.msg.CaricaDatiVisibilitaSacCapitoloResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.EsistenzaProgettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNome;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComunePerNomeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSedime;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSedimeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccountPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccountPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaGruppoTipoAccreditoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaGruppoTipoAccreditoPerChiaveResponse;

/**
 * Implementazione della commonservice, dalla quale devono estendere tutte le altre implementazioni 
 * 
 * @author luca.romanello
 *
 */
@WebService(serviceName = "GenericService", 
portName = "GenericServicePort", 
targetNamespace = FINSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.GenericService")
public class GenericServiceImpl extends AbstractService implements GenericService {
	

	protected transient LogSrvUtil log = new LogSrvUtil(this.getClass());

	@Autowired
	protected ApplicationContext appCtx;
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	
	@Override
	@WebMethod
	@WebResult
	public ListeResponse liste(@WebParam Liste request) {
		return new ServiceExecutor<ListeResponse, Liste>("liste") {
			@Override
			ListeResponse executeService(Liste request) {
				return appCtx.getBean(ListeService.class).executeService(request);
			}
		}.execute(request);
	}
	
	
	@Override
	@WebMethod
	@WebResult
	public ListaSedimeResponse listaSedime(@WebParam ListaSedime request) {
		return new ServiceExecutor<ListaSedimeResponse, ListaSedime>("listeSedime") {
			@Override
			ListaSedimeResponse executeService(ListaSedime request) {
				return appCtx.getBean(ListaSedimeService.class).executeService(request);
			}
		}.execute(request);
	}
	
	
	@Override
	@WebMethod
	@WebResult
	public ListaComunePerNomeResponse findComunePerNome(@WebParam ListaComunePerNome request) {
		return new ServiceExecutor<ListaComunePerNomeResponse, ListaComunePerNome>("listeSedime") {
			@Override
			ListaComunePerNomeResponse executeService(ListaComunePerNome request) {
				return appCtx.getBean(FindComunePerNomeService.class).executeService(request);
			}
		}.execute(request);
	}
	
	
	@Override
	@WebMethod
	@WebResult
	public RicercaAccountPerChiaveResponse ricercaAccountPerChiave(@WebParam RicercaAccountPerChiave request) {
		return new ServiceExecutor<RicercaAccountPerChiaveResponse, RicercaAccountPerChiave>("ricercaAccountPerChiave") {
			@Override
			RicercaAccountPerChiaveResponse executeService(RicercaAccountPerChiave request) {
				return appCtx.getBean(RicercaAccountPerChiaveService.class).executeService(request);
			}
		}.execute(request);
	}
	
	
	@Override
	@WebMethod
	@WebResult
	public RicercaGruppoTipoAccreditoPerChiaveResponse findGruppoTipoAccreditoPerPk(@WebParam RicercaGruppoTipoAccreditoPerChiave request) {
		return new ServiceExecutor<RicercaGruppoTipoAccreditoPerChiaveResponse, RicercaGruppoTipoAccreditoPerChiave>("listeSedime") {
			@Override
			RicercaGruppoTipoAccreditoPerChiaveResponse executeService(RicercaGruppoTipoAccreditoPerChiave request) {
				return appCtx.getBean(RicercaGruppoTipoAccreditoPkService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	@WebResult
	public EsistenzaProgettoResponse cercaProgetto(@WebParam EsistenzaProgetto request) {
		return new ServiceExecutor<EsistenzaProgettoResponse, EsistenzaProgetto>("cercaProgetto") {
			@Override
			EsistenzaProgettoResponse executeService(EsistenzaProgetto request) {
				return appCtx.getBean(CercaProgettoService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public @WebResult
	ListaComuniResponse cercaComuni(@WebParam ListaComuni request) {
		return new ServiceExecutor<ListaComuniResponse, ListaComuni>("cercaComuni") {
			@Override
			ListaComuniResponse executeService(ListaComuni request) {
				return appCtx.getBean(CercaComuniService.class).executeService(request);
			}
		}.execute(request);
	}


	@Override
	@WebMethod
	public CaricaDatiVisibilitaSacCapitoloResponse caricaDatiVisibilitaSacCapitolo(CaricaDatiVisibilitaSacCapitolo request) {
		return new ServiceExecutor<CaricaDatiVisibilitaSacCapitoloResponse, CaricaDatiVisibilitaSacCapitolo>("caricaDatiVisibilitaSacCapitolo") {
			@Override
			CaricaDatiVisibilitaSacCapitoloResponse executeService(CaricaDatiVisibilitaSacCapitolo request) {
				return appCtx.getBean(CaricaDatiVisibilitaSacCapitoloService.class).executeService(request);
			}
		}.execute(request);
	}
	

}

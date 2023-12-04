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

import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfinser.business.service.ordinativo.AggiornaOrdinativoIncassoService;
import it.csi.siac.siacfinser.business.service.ordinativo.AggiornaOrdinativoPagamentoService;
import it.csi.siac.siacfinser.business.service.ordinativo.AnnullaOrdinativoIncassoService;
import it.csi.siac.siacfinser.business.service.ordinativo.AnnullaOrdinativoPagamentoService;
import it.csi.siac.siacfinser.business.service.ordinativo.CollegaReversaliOrdinativoPagamentoService;
import it.csi.siac.siacfinser.business.service.ordinativo.InserisceOrdinativoIncassoService;
import it.csi.siac.siacfinser.business.service.ordinativo.InserisceOrdinativoPagamentoService;
import it.csi.siac.siacfinser.business.service.ordinativo.ReintroitoOrdinativoPagamentoAsyncService;
import it.csi.siac.siacfinser.business.service.ordinativo.ReintroitoOrdinativoPagamentoService;
import it.csi.siac.siacfinser.business.service.ordinativo.RicercaOrdinativoIncassoPerChiaveService;
import it.csi.siac.siacfinser.business.service.ordinativo.RicercaOrdinativoIncassoService;
import it.csi.siac.siacfinser.business.service.ordinativo.RicercaOrdinativoPagamentoPerChiaveService;
import it.csi.siac.siacfinser.business.service.ordinativo.RicercaOrdinativoPagamentoService;
import it.csi.siac.siacfinser.business.service.ordinativo.RicercaSubOrdinativoIncassoService;
import it.csi.siac.siacfinser.business.service.ordinativo.RicercaSubOrdinativoPagamentoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaReversaliOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.CollegaReversaliOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoIncasso;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoIncassoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ReintroitoOrdinativoPagamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.ReintroitoOrdinativoPagamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoIncassoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaOrdinativoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubOrdinativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubOrdinativoResponse;


@WebService(serviceName = "OrdinativoService", 
portName = "OrdinativoServicePort", 
targetNamespace = FINSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.OrdinativoService")
public class OrdinativoServiceImpl extends AbstractService implements OrdinativoService {
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	@WebMethod
	public  @WebResult InserisceOrdinativoPagamentoResponse inserisceOrdinativoPagamento(@WebParam  InserisceOrdinativoPagamento request) {
		return new ServiceExecutor<InserisceOrdinativoPagamentoResponse, InserisceOrdinativoPagamento>("inserisceOrdinativoPagamento") {
			@Override
			InserisceOrdinativoPagamentoResponse executeService(InserisceOrdinativoPagamento request) {
				return appCtx.getBean(InserisceOrdinativoPagamentoService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult InserisceOrdinativoIncassoResponse inserisceOrdinativoIncasso(@WebParam  InserisceOrdinativoIncasso request) {
		return new ServiceExecutor<InserisceOrdinativoIncassoResponse, InserisceOrdinativoIncasso>("inserisceOrdinativoIncasso") {
			@Override
			InserisceOrdinativoIncassoResponse executeService(InserisceOrdinativoIncasso request) {
				return appCtx.getBean(InserisceOrdinativoIncassoService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaOrdinativoResponse ricercaOrdinativoPagamento(@WebParam  RicercaOrdinativo request) {
		return new ServiceExecutor<RicercaOrdinativoResponse, RicercaOrdinativo>("ricercaOrdinativoPagamento") {
			@Override
			RicercaOrdinativoResponse executeService(RicercaOrdinativo request) {
				return appCtx.getBean(RicercaOrdinativoPagamentoService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaOrdinativoResponse ricercaOrdinativoIncasso(@WebParam  RicercaOrdinativo request) {
		return new ServiceExecutor<RicercaOrdinativoResponse, RicercaOrdinativo>("ricercaOrdinativoIncasso") {
			@Override
			RicercaOrdinativoResponse executeService(RicercaOrdinativo request) {
				return appCtx.getBean(RicercaOrdinativoIncassoService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaSubOrdinativoResponse ricercaSubOrdinativoPagamento(@WebParam  RicercaSubOrdinativo request) {
		return new ServiceExecutor<RicercaSubOrdinativoResponse, RicercaSubOrdinativo>("ricercaSubOrdinativoPagamento") {
			@Override
			RicercaSubOrdinativoResponse executeService(RicercaSubOrdinativo request) {
				return appCtx.getBean(RicercaSubOrdinativoPagamentoService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaSubOrdinativoResponse ricercaSubOrdinativoIncasso(@WebParam  RicercaSubOrdinativo request) {
		return new ServiceExecutor<RicercaSubOrdinativoResponse, RicercaSubOrdinativo>("ricercaSubOrdinativoIncasso") {
			@Override
			RicercaSubOrdinativoResponse executeService(RicercaSubOrdinativo request) {
				return appCtx.getBean(RicercaSubOrdinativoIncassoService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public  @WebResult AggiornaOrdinativoPagamentoResponse aggiornaOrdinativoPagamento(@WebParam  AggiornaOrdinativoPagamento request) {
		return new ServiceExecutor<AggiornaOrdinativoPagamentoResponse, AggiornaOrdinativoPagamento>("aggiornaOrdinativoPagamento") {
			@Override
			AggiornaOrdinativoPagamentoResponse executeService(AggiornaOrdinativoPagamento request) {
				return appCtx.getBean(AggiornaOrdinativoPagamentoService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult AggiornaOrdinativoIncassoResponse aggiornaOrdinativoIncasso(@WebParam  AggiornaOrdinativoIncasso request) {
		return new ServiceExecutor<AggiornaOrdinativoIncassoResponse, AggiornaOrdinativoIncasso>("aggiornaOrdinativoIncasso") {
			@Override
			AggiornaOrdinativoIncassoResponse executeService(AggiornaOrdinativoIncasso request) {
				return appCtx.getBean(AggiornaOrdinativoIncassoService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaOrdinativoPagamentoPerChiaveResponse ricercaOrdinativoPagamentoPerChiave(@WebParam  RicercaOrdinativoPagamentoPerChiave request) {
		return new ServiceExecutor<RicercaOrdinativoPagamentoPerChiaveResponse, RicercaOrdinativoPagamentoPerChiave>("ricercaOrdinativoPagamentoPerChiave") {
			@Override
			RicercaOrdinativoPagamentoPerChiaveResponse executeService(RicercaOrdinativoPagamentoPerChiave request) {
				return appCtx.getBean(RicercaOrdinativoPagamentoPerChiaveService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaOrdinativoIncassoPerChiaveResponse ricercaOrdinativoIncassoPerChiave(@WebParam  RicercaOrdinativoIncassoPerChiave request) {
		return new ServiceExecutor<RicercaOrdinativoIncassoPerChiaveResponse, RicercaOrdinativoIncassoPerChiave>("ricercaOrdinativoIncassoPerChiave") {
			@Override
			RicercaOrdinativoIncassoPerChiaveResponse executeService(RicercaOrdinativoIncassoPerChiave request) {
				return appCtx.getBean(RicercaOrdinativoIncassoPerChiaveService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public  @WebResult AnnullaOrdinativoPagamentoResponse annullaOrdinativoPagamento(@WebParam  AnnullaOrdinativoPagamento request) {
		return new ServiceExecutor<AnnullaOrdinativoPagamentoResponse, AnnullaOrdinativoPagamento>("annullaOrdinativoPagamento") {
			@Override
			AnnullaOrdinativoPagamentoResponse executeService(AnnullaOrdinativoPagamento request) {
				return appCtx.getBean(AnnullaOrdinativoPagamentoService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public  @WebResult AnnullaOrdinativoIncassoResponse annullaOrdinativoIncasso(@WebParam  AnnullaOrdinativoIncasso request) {
		return new ServiceExecutor<AnnullaOrdinativoIncassoResponse, AnnullaOrdinativoIncasso>("annullaOrdinativoIncasso") {
			@Override
			AnnullaOrdinativoIncassoResponse executeService(AnnullaOrdinativoIncasso request) {
				return appCtx.getBean(AnnullaOrdinativoIncassoService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public ReintroitoOrdinativoPagamentoResponse reintroitoOrdinativoPagamento(ReintroitoOrdinativoPagamento request) {
		return new ServiceExecutor<ReintroitoOrdinativoPagamentoResponse, ReintroitoOrdinativoPagamento>("reintroitoOrdinativoPagamento") {
			@Override
			ReintroitoOrdinativoPagamentoResponse executeService(ReintroitoOrdinativoPagamento request) {
				return appCtx.getBean(ReintroitoOrdinativoPagamentoService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	public AsyncServiceResponse reintroitoOrdinativoPagamentoAsync(AsyncServiceRequestWrapper<ReintroitoOrdinativoPagamento> request) {
		return new ServiceExecutor<AsyncServiceResponse, AsyncServiceRequestWrapper<ReintroitoOrdinativoPagamento>>("reintroitoOrdinativoPagamentoAsync") {
			@Override
			AsyncServiceResponse executeService(AsyncServiceRequestWrapper<ReintroitoOrdinativoPagamento> request) {
				return appCtx.getBean(ReintroitoOrdinativoPagamentoAsyncService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	public CollegaReversaliOrdinativoPagamentoResponse collegaReversaliOrdinativoPagamento(
			CollegaReversaliOrdinativoPagamento request) {
		return new ServiceExecutor<CollegaReversaliOrdinativoPagamentoResponse, CollegaReversaliOrdinativoPagamento>("collegaReversaliOrdinativoPagamento") {
			@Override
			CollegaReversaliOrdinativoPagamentoResponse executeService(CollegaReversaliOrdinativoPagamento request) {
				return appCtx.getBean(CollegaReversaliOrdinativoPagamentoService.class).executeService(request);
			}
		}.execute(request);
	}

}

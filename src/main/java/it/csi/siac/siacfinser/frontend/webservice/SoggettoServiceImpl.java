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

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siaccommon.util.JAXBUtility;
import it.csi.siac.siacfinser.business.service.soggetto.AggiornaDatiDurcSoggettoService;
import it.csi.siac.siacfinser.business.service.soggetto.AggiornaLegameSoggettiService;
import it.csi.siac.siacfinser.business.service.soggetto.AggiornaSoggettoProvvisorioService;
import it.csi.siac.siacfinser.business.service.soggetto.AggiornaSoggettoService;
import it.csi.siac.siacfinser.business.service.soggetto.AggiungiSoggettoAllaClassificazioneService;
import it.csi.siac.siacfinser.business.service.soggetto.AnnullaLegameSoggettiService;
import it.csi.siac.siacfinser.business.service.soggetto.AnnullaModalitaPagamentoInModificaService;
import it.csi.siac.siacfinser.business.service.soggetto.AnnullaSedeInModificaService;
import it.csi.siac.siacfinser.business.service.soggetto.AnnullaSoggettoInModificaService;
import it.csi.siac.siacfinser.business.service.soggetto.CancellaSoggettoService;
import it.csi.siac.siacfinser.business.service.soggetto.EliminaClassiService;
import it.csi.siac.siacfinser.business.service.soggetto.InserisceClasseSoggettoService;
import it.csi.siac.siacfinser.business.service.soggetto.InserisceSoggettoProvvisiorioService;
import it.csi.siac.siacfinser.business.service.soggetto.InserisceSoggettoService;
import it.csi.siac.siacfinser.business.service.soggetto.ListaSoggettiDellaClasseService;
import it.csi.siac.siacfinser.business.service.soggetto.ListeGestioneSoggettoService;
import it.csi.siac.siacfinser.business.service.soggetto.ModificaClasseSoggettoService;
import it.csi.siac.siacfinser.business.service.soggetto.RicercaBancaService;
import it.csi.siac.siacfinser.business.service.soggetto.RicercaModalitaPagamentoPerChiaveService;
import it.csi.siac.siacfinser.business.service.soggetto.RicercaSedeSecondariaPerChiaveService;
import it.csi.siac.siacfinser.business.service.soggetto.RicercaSoggettiService;
import it.csi.siac.siacfinser.business.service.soggetto.RicercaSoggettiSiacOttimizzatoService;
import it.csi.siac.siacfinser.business.service.soggetto.RicercaSoggettoPerChiaveService;
import it.csi.siac.siacfinser.business.service.soggetto.RimuoviSoggettoDaClassificazioneService;
import it.csi.siac.siacfinser.business.service.soggetto.VerificaIbanService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaDatiDurcSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaDatiDurcSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLegameSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaLegameSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoProvvisorio;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoProvvisorioResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiungiSoggettoAllaClassificazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiungiSoggettoAllaClassificazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaClasseResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLegameSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaLegameSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaModalitaPagamentoInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaModalitaPagamentoInModificaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSedeInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSedeInModificaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSoggettoInModifica;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaSoggettoInModificaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.CancellaSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.CancellaSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceClasseResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggettoProvvisorio;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggettoProvvisorioResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSoggettiDellaClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaSoggettiDellaClasseResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaClasse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaClasseResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaBanca;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaBancaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaModalitaPagamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSedeSecondariaPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RimuoviSoggettoDaClassificazione;
import it.csi.siac.siacfinser.frontend.webservice.msg.RimuoviSoggettoDaClassificazioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaIban;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaIbanResponse;

@WebService(serviceName = "SoggettoService", 
portName = "SoggettoServicePort", 
targetNamespace = FINSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.SoggettoService")
public class SoggettoServiceImpl extends AbstractService implements SoggettoService {
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	@WebMethod
	public@WebResult
	InserisceSoggettoResponse inserisceSoggetto(
			@WebParam InserisceSoggetto request) {
		return new ServiceExecutor<InserisceSoggettoResponse, InserisceSoggetto>("inserisceSoggetto", true) {
			@Override
			InserisceSoggettoResponse executeService(InserisceSoggetto request) {
				return appCtx.getBean(InserisceSoggettoService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public @WebResult
	RicercaSoggettiResponse ricercaSoggetti(@WebParam RicercaSoggetti request) {
		return new ServiceExecutor<RicercaSoggettiResponse, RicercaSoggetti>("ricercaSoggetti") {
			@Override
			RicercaSoggettiResponse executeService(RicercaSoggetti request) {
				return appCtx.getBean(RicercaSoggettiService.class).executeService(request);
			}
		}.execute(request);   
	}
	
	@Override
	@WebMethod
	public @WebResult
	RicercaSoggettiOttimizzatoResponse ricercaSoggettiOttimizzato(@WebParam RicercaSoggettiOttimizzato request) {
		return new ServiceExecutor<RicercaSoggettiOttimizzatoResponse, RicercaSoggettiOttimizzato>("ricercaSoggetti") {
			@Override
			RicercaSoggettiOttimizzatoResponse executeService(RicercaSoggettiOttimizzato request) {
				return appCtx.getBean(RicercaSoggettiSiacOttimizzatoService.class).executeService(request);
			}
		}.execute(request);   
	}


	@Override
	@WebMethod
	public  @WebResult RicercaSoggettoPerChiaveResponse ricercaSoggettoPerChiave(
													@WebParam  RicercaSoggettoPerChiave request) {
		return new ServiceExecutor<RicercaSoggettoPerChiaveResponse, RicercaSoggettoPerChiave>("ricercaSoggettoPerChiave") {
			@Override
			RicercaSoggettoPerChiaveResponse executeService(RicercaSoggettoPerChiave request) {
				return appCtx.getBean(RicercaSoggettoPerChiaveService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public@WebResult
	AggiornaSoggettoResponse aggiornaSoggetto(@WebParam AggiornaSoggetto request) {
		return new ServiceExecutor<AggiornaSoggettoResponse, AggiornaSoggetto>("aggiornaSoggetto") {
			@Override
			AggiornaSoggettoResponse executeService(AggiornaSoggetto request) {
				return appCtx.getBean(AggiornaSoggettoService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public@WebResult
	RimuoviSoggettoDaClassificazioneResponse rimuoviSoggettoDaClassificazione(@WebParam RimuoviSoggettoDaClassificazione request) {
		return new ServiceExecutor<RimuoviSoggettoDaClassificazioneResponse, RimuoviSoggettoDaClassificazione>("rimuoviSoggettoDaClassificazione") {
			@Override
			RimuoviSoggettoDaClassificazioneResponse executeService(RimuoviSoggettoDaClassificazione request) {
				return appCtx.getBean(RimuoviSoggettoDaClassificazioneService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public@WebResult
	AggiungiSoggettoAllaClassificazioneResponse aggiungiSoggettoAllaClassificazione(@WebParam AggiungiSoggettoAllaClassificazione request) {
		return new ServiceExecutor<AggiungiSoggettoAllaClassificazioneResponse, AggiungiSoggettoAllaClassificazione>("aggiungiSoggettoAllaClassificazione") {
			@Override
			AggiungiSoggettoAllaClassificazioneResponse executeService(AggiungiSoggettoAllaClassificazione request) {
				return appCtx.getBean(AggiungiSoggettoAllaClassificazioneService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public@WebResult
	InserisceSoggettoProvvisorioResponse inserisceSoggettoProvvisorio(@WebParam InserisceSoggettoProvvisorio request) {
		return new ServiceExecutor<InserisceSoggettoProvvisorioResponse, InserisceSoggettoProvvisorio>("inserisceSoggettoProvvisorio", true) {
			@Override
			InserisceSoggettoProvvisorioResponse executeService(InserisceSoggettoProvvisorio request) {
				return appCtx.getBean(InserisceSoggettoProvvisiorioService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public  @WebResult RicercaSedeSecondariaPerChiaveResponse ricercaSedeSecondariaPerChiave(
													@WebParam  RicercaSedeSecondariaPerChiave request) {
		return new ServiceExecutor<RicercaSedeSecondariaPerChiaveResponse, RicercaSedeSecondariaPerChiave>("ricercaSedeSecondariaPerChiave") {
			@Override
			RicercaSedeSecondariaPerChiaveResponse executeService(RicercaSedeSecondariaPerChiave request) {
				return appCtx.getBean(RicercaSedeSecondariaPerChiaveService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaModalitaPagamentoPerChiaveResponse ricercaModalitaPagamentoPerChiave(
													@WebParam  RicercaModalitaPagamentoPerChiave request) {
		return new ServiceExecutor<RicercaModalitaPagamentoPerChiaveResponse, RicercaModalitaPagamentoPerChiave>("ricercaModalitaPagamentoPerChiave") {
			@Override
			RicercaModalitaPagamentoPerChiaveResponse executeService(RicercaModalitaPagamentoPerChiave request) {
				return appCtx.getBean(RicercaModalitaPagamentoPerChiaveService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult CancellaSoggettoResponse cancellaSoggetto(@WebParam  CancellaSoggetto request) {
		return new ServiceExecutor<CancellaSoggettoResponse, CancellaSoggetto>("cancellaSoggetto") {
			@Override
			CancellaSoggettoResponse executeService(CancellaSoggetto request) {
				return appCtx.getBean(CancellaSoggettoService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public  @WebResult AnnullaSoggettoInModificaResponse annullaSoggettoInModifica(@WebParam  AnnullaSoggettoInModifica request) {
		return new ServiceExecutor<AnnullaSoggettoInModificaResponse, AnnullaSoggettoInModifica>("annullaSoggettoInModifica") {
			@Override
			AnnullaSoggettoInModificaResponse executeService(AnnullaSoggettoInModifica request) {
				return appCtx.getBean(AnnullaSoggettoInModificaService.class).executeService(request);
			}
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult AnnullaSedeInModificaResponse annullaSedeInModifica(@WebParam  AnnullaSedeInModifica request) {
		return new ServiceExecutor<AnnullaSedeInModificaResponse, AnnullaSedeInModifica>("annullaSedeInModifica") {
			@Override
			AnnullaSedeInModificaResponse executeService(AnnullaSedeInModifica request) {
				return appCtx.getBean(AnnullaSedeInModificaService.class).executeService(request);
			}
		}.execute(request);
	}
	
	
	@Override
	@WebMethod
	public  @WebResult AnnullaModalitaPagamentoInModificaResponse annullaModalitaPagamentoInModifica(@WebParam  AnnullaModalitaPagamentoInModifica request) {
		return new ServiceExecutor<AnnullaModalitaPagamentoInModificaResponse, AnnullaModalitaPagamentoInModifica>("annullaModalitaPagamentoInModifica") {
			@Override
			AnnullaModalitaPagamentoInModificaResponse executeService(AnnullaModalitaPagamentoInModifica request) {
				return appCtx.getBean(AnnullaModalitaPagamentoInModificaService.class).executeService(request);
			}
		}.execute(request);
	}
	
	
	@Override
	@WebMethod
	public  @WebResult AggiornaSoggettoProvvisorioResponse aggiornaSoggettoProvvisorio(@WebParam  AggiornaSoggettoProvvisorio request) {
		return new ServiceExecutor<AggiornaSoggettoProvvisorioResponse, AggiornaSoggettoProvvisorio>("aggiornaSoggettoProvvisorio") {
			@Override
			AggiornaSoggettoProvvisorioResponse executeService(AggiornaSoggettoProvvisorio request) {
				// SIAC-6847
				log.debug("REQUEST-AGGIORNASOGGETTOPROVVISORIO", JAXBUtility.marshall(request));
				return appCtx.getBean(AggiornaSoggettoProvvisorioService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public @WebResult AggiornaLegameSoggettiResponse aggiornaLegameSoggetti(@WebParam AggiornaLegameSoggetti request) {
		return new ServiceExecutor<AggiornaLegameSoggettiResponse, AggiornaLegameSoggetti>("aggiornaLegameSoggetti") {
			@Override
			AggiornaLegameSoggettiResponse executeService(AggiornaLegameSoggetti request) {
				return appCtx.getBean(AggiornaLegameSoggettiService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public @WebResult AnnullaLegameSoggettiResponse annullaLegameSoggetti(@WebParam AnnullaLegameSoggetti request) {
		return new ServiceExecutor<AnnullaLegameSoggettiResponse, AnnullaLegameSoggetti>("annullaLegameSoggetti") {
			@Override
			AnnullaLegameSoggettiResponse executeService(AnnullaLegameSoggetti request) {
				return appCtx.getBean(AnnullaLegameSoggettiService.class).executeService(request);
			}
		}.execute(request);
	}	
	
	@Override
	@WebMethod
	public @WebResult
	ListeGestioneSoggettoResponse listeGestioneSoggetto(@WebParam ListeGestioneSoggetto request) {
		return new ServiceExecutor<ListeGestioneSoggettoResponse, ListeGestioneSoggetto>("listeGestioneSoggetto") {
			@Override
			ListeGestioneSoggettoResponse executeService(ListeGestioneSoggetto request) {
				return appCtx.getBean(ListeGestioneSoggettoService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaBancaResponse ricercaBanca(@WebParam RicercaBanca request)
	{
		return new ServiceExecutor<RicercaBancaResponse, RicercaBanca>("ricercaBanca", true) {
			@Override
			RicercaBancaResponse executeService(RicercaBanca request) {
				return appCtx.getBean(RicercaBancaService.class).executeService(request);
			}
		}.execute(request);

	}

	@Override
	@WebMethod
	public @WebResult
	VerificaIbanResponse verificaIban(@WebParam VerificaIban request)
	{
		return new ServiceExecutor<VerificaIbanResponse, VerificaIban>("verificaIban", true) {
			@Override
			VerificaIbanResponse executeService(VerificaIban request) {
				return appCtx.getBean(VerificaIbanService.class).executeService(request);
			}
		}.execute(request);
	}
	
	
	@Override
	@WebMethod
	public @WebResult
	AnnullaClasseResponse annullaClasse(@WebParam AnnullaClasse request) {
		return new ServiceExecutor<AnnullaClasseResponse, AnnullaClasse>("eliminaClasse", true) {
			
			@Override
			AnnullaClasseResponse executeService(AnnullaClasse request) {
				return appCtx.getBean(EliminaClassiService.class).executeService(request);
			}
			
		}.execute(request);
	}

	@Override
	@WebMethod
	public @WebResult
	InserisceClasseResponse inserisceClasse(@WebParam InserisceClasse request) {
		return new ServiceExecutor<InserisceClasseResponse, InserisceClasse>("inserisciClasse", true) {
			
			@Override
			InserisceClasseResponse executeService(InserisceClasse request) {
				return appCtx.getBean(InserisceClasseSoggettoService.class).executeService(request);
			}
			
		}.execute(request);
	}	

	@Override
	@WebMethod
	public @WebResult
	ModificaClasseResponse modificaClasse(@WebParam ModificaClasse request) {
		return new ServiceExecutor<ModificaClasseResponse, ModificaClasse>("modificaClasse", true) {
			
			@Override
			ModificaClasseResponse executeService(ModificaClasse request) {
				return appCtx.getBean(ModificaClasseSoggettoService.class).executeService(request);
			}
			
		}.execute(request);
	}
	
	@Override
	@WebMethod
	public @WebResult
	ListaSoggettiDellaClasseResponse listaSoggettiDellaClasse(@WebParam ListaSoggettiDellaClasse request) {
		return new ServiceExecutor<ListaSoggettiDellaClasseResponse, ListaSoggettiDellaClasse>("listaSoggettiDellaClasse", true) {
			
			@Override
			ListaSoggettiDellaClasseResponse executeService(ListaSoggettiDellaClasse request) {
				return appCtx.getBean(ListaSoggettiDellaClasseService.class).executeService(request);
			}
			
		}.execute(request);
	}

	@Override
	@WebMethod
	public @WebResult
	AggiornaDatiDurcSoggettoResponse aggiornaDatiDurcSoggetto(AggiornaDatiDurcSoggetto parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaDatiDurcSoggettoService.class, parameters);
	}
	
}

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

import it.csi.siac.siacfinser.business.service.movgest.AggiornaAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.AggiornaImpegnoService;
import it.csi.siac.siacfinser.business.service.movgest.AggiornaStoricoImpegnoAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.AnnullaMovimentoEntrataService;
import it.csi.siac.siacfinser.business.service.movgest.AnnullaMovimentoSpesaService;
import it.csi.siac.siacfinser.business.service.movgest.ConsultaDettaglioAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.ConsultaDettaglioImpegnoService;
import it.csi.siac.siacfinser.business.service.movgest.ConsultaVincoliAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.EliminaStoricoImpegnoAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.InserisceAccertamentiService;
import it.csi.siac.siacfinser.business.service.movgest.InserisceImpegniService;
import it.csi.siac.siacfinser.business.service.movgest.InserisceStoricoImpegnoAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.InserisciModificaImportoMovimentoGestioneEntrataService;
import it.csi.siac.siacfinser.business.service.movgest.LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaAccertamentiService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaAccertamentiSubAccertamentiPerOrdinativoIncassoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaAccertamentiSubAccertamentiService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaAccertamentoPerChiaveOttimizzatoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaAccertamentoPerChiaveService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaAvanzovincoloService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaDettaglioImpegnoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaImpegniService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaImpegniSubimpegniPerVociMutuoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaImpegniSubimpegniService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaImpegnoOSubPerChiaveService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaImpegnoPerChiaveOttimizzatoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaImpegnoPerChiaveService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaReversaliByAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaSinteticaAccertamentiSubAccertamentiService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaSinteticaImpegniSubimpegniService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaStoricoImpegnoAccertamentoPerChiaveService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaStoricoImpegnoAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaSubAccertamentiDiUnAccertamentoService;
import it.csi.siac.siacfinser.business.service.movgest.RicercaSubImpegniDiUnImpegnoService;
import it.csi.siac.siacfinser.business.service.movgest.VerificaLegameImpegnoLiquidazioniService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaModificaImportoMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaModificaImportoMovimentoGestioneEntrataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoEntrataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AnnullaMovimentoSpesaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaDettaglioAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaDettaglioAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaDettaglioImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaDettaglioImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaVincoliAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.ConsultaVincoliAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.EliminaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.EliminaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceImpegniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneEntrataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisciModificaImportoMovimentoGestioneSpesaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoProvvedimentoMovimentoGestione;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaImportoImpegnoSuAnniSuccessivi;
import it.csi.siac.siacfinser.frontend.webservice.msg.ModificaImportoImpegnoSuAnniSuccessiviResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAvanzovincolo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAvanzovincoloResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDettaglioImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaDettaglioImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniGlobal;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniGlobalResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubImpegniPerVociMutuo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubimpegniPerVociMutuoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoOSubPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoOSubPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaReversaliByAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaReversaliByAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamenti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaAccertamentiSubAccertamentiResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubimpegniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamentoPerChiaveResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaStoricoImpegnoAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubAccertamentiDiUnAccertamento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubAccertamentiDiUnAccertamentoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubImpegniDiUnImpegno;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSubImpegniDiUnImpegnoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaLegameImpegnoLiquidazioni;
import it.csi.siac.siacfinser.frontend.webservice.msg.VerificaLegameImpegnoLiquidazioniResponse;

@Component("movimentoGestioneService")
@WebService(serviceName = "MovimentoGestioneService", portName = "MovimentoGestioneServicePort", targetNamespace = FINSvcDictionary.NAMESPACE, 
			endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService")
public class MovimentoGestioneServiceImpl extends AbstractService implements MovimentoGestioneService {
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	@WebMethod
	public  @WebResult InserisceImpegniResponse inserisceImpegni(@WebParam  InserisceImpegni request) {
		return appCtx.getBean(InserisceImpegniService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaImpegnoPerChiaveResponse ricercaImpegnoPerChiave(@WebParam  RicercaImpegnoPerChiave request) {
		return appCtx.getBean(RicercaImpegnoPerChiaveService.class).executeService(request);
	}
	


	
	@Override
	@WebMethod
	public  @WebResult RicercaImpegnoPerChiaveOttimizzatoResponse ricercaImpegnoPerChiaveOttimizzato(@WebParam  RicercaImpegnoPerChiaveOttimizzato request) {
		return appCtx.getBean(RicercaImpegnoPerChiaveOttimizzatoService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaAccertamentoPerChiaveOttimizzatoResponse ricercaAccertamentoPerChiaveOttimizzato(@WebParam  RicercaAccertamentoPerChiaveOttimizzato request) {
		return appCtx.getBean(RicercaAccertamentoPerChiaveOttimizzatoService.class).executeService(request);
	}

	
	@Override
	@WebMethod
	public  @WebResult RicercaImpegnoOSubPerChiaveResponse ricercaImpegnoOSubPerChiave(@WebParam  RicercaImpegnoOSubPerChiave request) {
		return appCtx.getBean(RicercaImpegnoOSubPerChiaveService.class).executeService(request);
	}


	@Override
	@WebMethod
	public  @WebResult InserisceAccertamentiResponse inserisceAccertamenti(@WebParam  InserisceAccertamenti request) {
		return appCtx.getBean(InserisceAccertamentiService.class).executeService(request);
	}

	@Override
	@WebMethod
	public  @WebResult RicercaAccertamentoPerChiaveResponse ricercaAccertamentoPerChiave(@WebParam  RicercaAccertamentoPerChiave request) {
		return appCtx.getBean(RicercaAccertamentoPerChiaveService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaAccertamentiResponse ricercaAccertamenti(@WebParam  RicercaAccertamenti request) {
		return appCtx.getBean(RicercaAccertamentiService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult AnnullaMovimentoSpesaResponse annullaMovimentoSpesa(@WebParam  AnnullaMovimentoSpesa request) {
		return appCtx.getBean(AnnullaMovimentoSpesaService.class).executeService(request);
	}

	@Override
	@WebMethod
	public  @WebResult AnnullaMovimentoEntrataResponse annullaMovimentoEntrata(@WebParam  AnnullaMovimentoEntrata request) {
		return appCtx.getBean(AnnullaMovimentoEntrataService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaImpegniGlobalResponse ricercaImpegni(@WebParam  RicercaImpegniGlobal request) {
		return appCtx.getBean(RicercaImpegniService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaSubImpegniDiUnImpegnoResponse ricercaSubImpegniDiUnImpegno(@WebParam  RicercaSubImpegniDiUnImpegno request) {
		return appCtx.getBean(RicercaSubImpegniDiUnImpegnoService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaSubAccertamentiDiUnAccertamentoResponse ricercaSubAccertamentiDiUnAccertamento(@WebParam  RicercaSubAccertamentiDiUnAccertamento request) {
		return appCtx.getBean(RicercaSubAccertamentiDiUnAccertamentoService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult AggiornaImpegnoResponse aggiornaImpegno(@WebParam  AggiornaImpegno request) {
		return appCtx.getBean(AggiornaImpegnoService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult AggiornaAccertamentoResponse aggiornaAccertamento(@WebParam  AggiornaAccertamento request) {
		return appCtx.getBean(AggiornaAccertamentoService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaImpegniSubimpegniResponse ricercaImpegniSubimpegni(@WebParam  RicercaImpegniSubImpegni request) {
		return appCtx.getBean(RicercaImpegniSubimpegniService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public @WebResult
	RicercaSinteticaImpegniSubimpegniResponse ricercaSinteticaImpegniSubimpegni(
			@WebParam RicercaSinteticaImpegniSubImpegni request) {
		return appCtx.getBean(RicercaSinteticaImpegniSubimpegniService.class).executeService(request);
	}

	
	@Override
	@WebMethod
	public  @WebResult RicercaImpegniSubimpegniPerVociMutuoResponse ricercaImpegniSubimpegniPerVociMutuo(@WebParam  RicercaImpegniSubImpegniPerVociMutuo request) {
		return appCtx.getBean(RicercaImpegniSubimpegniPerVociMutuoService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaAccertamentiSubAccertamentiResponse ricercaAccertamentiSubAccertamenti(@WebParam  RicercaAccertamentiSubAccertamenti request) {
		return appCtx.getBean(RicercaAccertamentiSubAccertamentiService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaSinteticaAccertamentiSubAccertamentiResponse ricercaSinteticaAccertamentiSubAccertamenti(
			@WebParam RicercaSinteticaAccertamentiSubAccertamenti request) {
		return appCtx.getBean(RicercaSinteticaAccertamentiSubAccertamentiService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaDettaglioImpegnoResponse ricercaDettaglioImpegno(@WebParam RicercaDettaglioImpegno request) {
		return appCtx.getBean(RicercaDettaglioImpegnoService.class).executeService(request);
	}

	
	@Override
	@WebMethod
	public  @WebResult RicercaAccertamentiSubAccertamentiResponse ricercaAccertamentiSubAccertamentiPerOrdinativoIncasso(@WebParam  RicercaAccertamentiSubAccertamenti request) {
		return appCtx.getBean(RicercaAccertamentiSubAccertamentiPerOrdinativoIncassoService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneResponse leggiStoricoAggiornamentoProvvedimentoMovimentoGestione(
			@WebParam LeggiStoricoAggiornamentoProvvedimentoMovimentoGestione request) {
		return appCtx.getBean(LeggiStoricoAggiornamentoProvvedimentoMovimentoGestioneService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	RicercaReversaliByAccertamentoResponse ricercaReversaliByAccertamento(
			@WebParam RicercaReversaliByAccertamento request) {
		return appCtx.getBean(RicercaReversaliByAccertamentoService.class).executeService(request);
		
	}
	
	@Override
	@WebMethod
	public  @WebResult RicercaAvanzovincoloResponse ricercaAvanzovincolo(@WebParam  RicercaAvanzovincolo request) {
		return appCtx.getBean(RicercaAvanzovincoloService.class).executeService(request);
	}

	@Override
	public InserisciModificaImportoMovimentoGestioneEntrataResponse inserisciModificaImportoMovimentoGestioneEntrata(
			InserisciModificaImportoMovimentoGestioneEntrata request) {
		return appCtx.getBean(InserisciModificaImportoMovimentoGestioneEntrataService.class).executeService(request);
	}

	@Override
	public AggiornaModificaImportoMovimentoGestioneEntrataResponse aggiornaModificaImportoMovimentoGestioneEntrata(AggiornaModificaImportoMovimentoGestioneEntrata request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@WebMethod
	public @WebResult
	VerificaLegameImpegnoLiquidazioniResponse verificaLegameImpegnoLiquidazioni(
			@WebParam VerificaLegameImpegnoLiquidazioni request) {
		return appCtx.getBean(VerificaLegameImpegnoLiquidazioniService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult ConsultaDettaglioImpegnoResponse consultaDettaglioImpegno(@WebParam  ConsultaDettaglioImpegno request) {
		return appCtx.getBean(ConsultaDettaglioImpegnoService.class).executeService(request);
	}
	
	@Override
	@WebMethod
	public  @WebResult ConsultaDettaglioAccertamentoResponse consultaDettaglioAccertamento(@WebParam ConsultaDettaglioAccertamento request) {
		return appCtx.getBean(ConsultaDettaglioAccertamentoService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult ConsultaVincoliAccertamentoResponse consultaVincoliAccertamento(@WebParam ConsultaVincoliAccertamento request) {
		return appCtx.getBean(ConsultaVincoliAccertamentoService.class).executeService(request);
	}

	@Override
	@WebMethod
	@WebResult
	public InserisciModificaImportoMovimentoGestioneSpesaResponse inserisciModificaImportoMovimentoGestioneSpesa(
			@WebParam InserisciModificaImportoMovimentoGestioneSpesa request) {
		return null;
	}

	@Override
	@WebMethod
	@WebResult
	public InserisceStoricoImpegnoAccertamentoResponse inserisceStoricoImpegnoAccertamento(InserisceStoricoImpegnoAccertamento request) {
		return appCtx.getBean(InserisceStoricoImpegnoAccertamentoService.class).executeService(request);
	}

	@Override
	@WebMethod
	@WebResult
	public AggiornaStoricoImpegnoAccertamentoResponse aggiornaStoricoImpegnoAccertamento(AggiornaStoricoImpegnoAccertamento request) {
		return appCtx.getBean(AggiornaStoricoImpegnoAccertamentoService.class).executeService(request);
	}

	@Override
	@WebMethod
	@WebResult
	public RicercaStoricoImpegnoAccertamentoPerChiaveResponse ricercaStoricoImpegnoPerChiaveAccertamento(RicercaStoricoImpegnoAccertamentoPerChiave request) {
		// TODO Auto-generated method stub
		return appCtx.getBean(RicercaStoricoImpegnoAccertamentoPerChiaveService.class).executeService(request);
	}

	@Override
	@WebMethod
	@WebResult
	public RicercaStoricoImpegnoAccertamentoResponse ricercaStoricoImpegnoAccertamento(RicercaStoricoImpegnoAccertamento request) {
		// TODO Auto-generated method stub
		return appCtx.getBean(RicercaStoricoImpegnoAccertamentoService.class).executeService(request);
	}

	@Override
	@WebMethod
	@WebResult
	public EliminaStoricoImpegnoAccertamentoResponse eliminaStoricoImpegnoAccertamento(@WebParam EliminaStoricoImpegnoAccertamento request) {
		return appCtx.getBean(EliminaStoricoImpegnoAccertamentoService.class).executeService(request);	
	}

	@Override
	@WebMethod
	@WebResult
	public ModificaImportoImpegnoSuAnniSuccessiviResponse modificaImportoImpegnoSuAnniSuccessivi(@WebParam ModificaImportoImpegnoSuAnniSuccessivi request) {
		return appCtx.getBean(ModificaImportoImpegnoSuAnniSuccessiviService.class).executeService(request);
	}
}


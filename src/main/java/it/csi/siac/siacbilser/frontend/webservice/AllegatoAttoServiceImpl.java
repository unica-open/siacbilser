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

import it.csi.siac.siacbilser.business.service.allegatoatto.AggiornaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.AggiornaDatiSoggettoAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.AggiornaElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.AggiornaMassivaDatiSoggettoAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.AnnullaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.AssociaElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.CompletaAllegatoAttoAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.CompletaAllegatoAttoMultiploAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.CompletaAllegatoAttoMultiploService;
import it.csi.siac.siacbilser.business.service.allegatoatto.CompletaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ControlloImportiImpegniVincolatiService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ConvalidaAllegatoAttoPerElenchiAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ConvalidaAllegatoAttoPerElenchiMultiploAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ConvalidaAllegatoAttoPerElenchiMultiploService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ConvalidaAllegatoAttoPerElenchiService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ConvalidaAllegatoAttoPerProvvisorioAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.ConvalidaAllegatoAttoPerProvvisorioService;
import it.csi.siac.siacbilser.business.service.allegatoatto.DisassociaElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.EliminaQuotaDaElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceElencoConDocumentiConQuoteService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InserisceElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InviaAllegatoAttoAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.InviaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RiCompletaAllegatoAttoPerElenchiAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RiCompletaAllegatoAttoPerElenchiService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaDatiSoggettoAllegatoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaDatiSospensioneAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaDettaglioAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaDettaglioElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaElenchiPerAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaElencoDaEmettereService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaSinteticaQuoteElencoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RicercaSinteticaStampaAllegatoAttoService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RifiutaElenchiAsyncService;
import it.csi.siac.siacbilser.business.service.allegatoatto.RifiutaElenchiService;
import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.StampaAllegatoAttoAsyncService;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.StampaAllegatoAttoService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAttoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAttoResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDatiSoggettoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDatiSoggettoAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaMassivaDatiSoggettoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaMassivaDatiSoggettoAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoMultiploResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolati;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolatiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiMultiploResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerProvvisorio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerProvvisorioResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DisassociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DisassociaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RiCompletaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RiCompletaAllegatoAttoPerElenchiResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSoggettoAllegatoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenchiPerAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenchiPerAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElencoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchiResponse;

@WebService(serviceName = "AllegatoAttoService", portName = "AllegatoAttoServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService")
public class AllegatoAttoServiceImpl implements AllegatoAttoService {

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
	public InserisceAllegatoAttoResponse inserisceAllegatoAtto( InserisceAllegatoAtto parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceAllegatoAttoService.class, parameters);
	}

	@Override
	public AggiornaAllegatoAttoResponse aggiornaAllegatoAtto( AggiornaAllegatoAtto parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaAllegatoAttoService.class, parameters);
	}

	@Override
	public AnnullaAllegatoAttoResponse annullaAllegatoAtto( AnnullaAllegatoAtto parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaAllegatoAttoService.class, parameters);
	}

	@Override
	public RicercaAllegatoAttoResponse ricercaAllegatoAtto( RicercaAllegatoAtto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaAllegatoAttoService.class, parameters);
	}

	@Override
	public RicercaDettaglioAllegatoAttoResponse ricercaDettaglioAllegatoAtto( RicercaDettaglioAllegatoAtto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioAllegatoAttoService.class, parameters);
	}

	@Override
	public InserisceElencoResponse inserisceElenco( InserisceElenco parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceElencoService.class, parameters);
	}

	@Override
	public AggiornaElencoResponse aggiornaElenco( AggiornaElenco parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaElencoService.class, parameters);
	}

	@Override
	public RicercaElencoResponse ricercaElenco( RicercaElenco parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaElencoService.class, parameters);
	}
	
	@Override
	public RicercaElencoDaEmettereResponse ricercaElencoDaEmettere(RicercaElencoDaEmettere parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaElencoDaEmettereService.class, parameters);
	}	

	@Override
	public AssociaElencoResponse associaElenco( AssociaElenco parameters) {
		return BaseServiceExecutor.execute(appCtx, AssociaElencoService.class, parameters);
	}
	
	@Override
	public AggiornaDatiSoggettoAllegatoAttoResponse aggiornaDatiSoggettoAllegatoAtto( AggiornaDatiSoggettoAllegatoAtto parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaDatiSoggettoAllegatoAttoService.class, parameters);
	}
	
	@Override
	public RicercaDettaglioElencoResponse ricercaDettaglioElenco( RicercaDettaglioElenco parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioElencoService.class, parameters);
	}
	
	@Override
	public EliminaQuotaDaElencoResponse eliminaQuotaDaElenco( EliminaQuotaDaElenco parameters){
		return BaseServiceExecutor.execute(appCtx, EliminaQuotaDaElencoService.class, parameters);
	}

	@Override
	public DisassociaElencoResponse disassociaElenco(DisassociaElenco parameters) {
		return BaseServiceExecutor.execute(appCtx, DisassociaElencoService.class, parameters);
	}
	
	@Override
	public InserisceElencoResponse inserisceElencoConDocumentiConQuote(InserisceElenco parameters){
		return BaseServiceExecutor.execute(appCtx, InserisceElencoConDocumentiConQuoteService.class, parameters);
	}

	@Override
	public RicercaDatiSoggettoAllegatoResponse ricercaDatiSoggettoAllegato(RicercaDatiSoggettoAllegato parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDatiSoggettoAllegatoService.class, parameters);
	}
	
	// --- Asincroni
	
	@Override
	public CompletaAllegatoAttoResponse completaAllegatoAtto(CompletaAllegatoAtto parameters) {
		return BaseServiceExecutor.execute(appCtx, CompletaAllegatoAttoService.class, parameters);
	}
	
	@Override
	public AsyncServiceResponse completaAllegatoAttoAsync( AsyncServiceRequestWrapper<CompletaAllegatoAtto> parameters) {
		return BaseServiceExecutor.execute(appCtx, CompletaAllegatoAttoAsyncService.class, parameters);
	}
	
	@Override
	public ConvalidaAllegatoAttoPerElenchiResponse convalidaAllegatoAttoPerElenchi( ConvalidaAllegatoAttoPerElenchi parameters) {
		return BaseServiceExecutor.execute(appCtx, ConvalidaAllegatoAttoPerElenchiService.class, parameters);
	}
	
	@Override
	public RiCompletaAllegatoAttoPerElenchiResponse riCompletaAllegatoAttoPerElenchi( RiCompletaAllegatoAttoPerElenchi parameters) {
		return BaseServiceExecutor.execute(appCtx, RiCompletaAllegatoAttoPerElenchiService.class, parameters);
	}
	
	@Override
	public AsyncServiceResponse convalidaAllegatoAttoPerElenchiAsync( AsyncServiceRequestWrapper<ConvalidaAllegatoAttoPerElenchi> parameters) {
		return BaseServiceExecutor.execute(appCtx, ConvalidaAllegatoAttoPerElenchiAsyncService.class, parameters);
	}
	
	@Override
	public AsyncServiceResponse riCompletaAllegatoAttoPerElenchiAsync(AsyncServiceRequestWrapper<RiCompletaAllegatoAttoPerElenchi> parameters) {

		return BaseServiceExecutor.execute(appCtx, RiCompletaAllegatoAttoPerElenchiAsyncService.class, parameters);
	}
	
	@Override
	public RifiutaElenchiResponse rifiutaElenchi(RifiutaElenchi parameters) {
		return BaseServiceExecutor.execute(appCtx, RifiutaElenchiService.class, parameters);
	}
	
	@Override
	public AsyncServiceResponse rifiutaElenchiAsync( AsyncServiceRequestWrapper<RifiutaElenchi> parameters) {
		return BaseServiceExecutor.execute(appCtx, RifiutaElenchiAsyncService.class, parameters);
	}

	@Override
	public ConvalidaAllegatoAttoPerProvvisorioResponse convalidaAllegatoAttoPerProvvisorio( ConvalidaAllegatoAttoPerProvvisorio parameters) {
		return BaseServiceExecutor.execute(appCtx, ConvalidaAllegatoAttoPerProvvisorioService.class, parameters);
	}
	
	@Override
	public AsyncServiceResponse convalidaAllegatoAttoPerProvvisorioAsync(AsyncServiceRequestWrapper<ConvalidaAllegatoAttoPerProvvisorio> parameters) {
		return BaseServiceExecutor.execute(appCtx, ConvalidaAllegatoAttoPerProvvisorioAsyncService.class, parameters);
	}

	@Override
	public StampaAllegatoAttoResponse stampaAllegatoAtto(StampaAllegatoAtto parameters) {
		return appCtx.getBean(Utility.toDefaultBeanName(StampaAllegatoAttoService.class), StampaAllegatoAttoService.class).executeService(parameters);
	}
	
	@Override
	public StampaAllegatoAttoResponse stampaAllegatoAttoAsync(StampaAllegatoAtto parameters) {
		return appCtx.getBean(Utility.toDefaultBeanName(StampaAllegatoAttoAsyncService.class), StampaAllegatoAttoAsyncService.class).executeService(parameters);
	}

	@Override
	public InviaAllegatoAttoResponse inviaAllegatoAtto(InviaAllegatoAtto parameters) {
		return BaseServiceExecutor.execute(appCtx, InviaAllegatoAttoService.class, parameters);
	}

	@Override
	public AsyncServiceResponse inviaAllegatoAttoAsync(AsyncServiceRequestWrapper<InviaAllegatoAtto> parameters) {
		return BaseServiceExecutor.execute(appCtx, InviaAllegatoAttoAsyncService.class, parameters);
	}
	
	@Override
	public CompletaAllegatoAttoMultiploResponse completaAllegatoAttoMultiplo(CompletaAllegatoAttoMultiplo parameters) {
		return BaseServiceExecutor.execute(appCtx, CompletaAllegatoAttoMultiploService.class, parameters);
	}
	
	@Override
	public AsyncServiceResponse completaAllegatoAttoMultiploAsync( AsyncServiceRequestWrapper<CompletaAllegatoAttoMultiplo> parameters) {
		return BaseServiceExecutor.execute(appCtx, CompletaAllegatoAttoMultiploAsyncService.class, parameters);
	}

	@Override
	public ConvalidaAllegatoAttoPerElenchiMultiploResponse convalidaAllegatoAttoPerElenchiMultiplo(ConvalidaAllegatoAttoPerElenchiMultiplo parameters) {
		return BaseServiceExecutor.execute(appCtx,ConvalidaAllegatoAttoPerElenchiMultiploService.class, parameters);
	}

	@Override
	public AsyncServiceResponse convalidaAllegatoAttoPerElenchiMultiploAsync(AsyncServiceRequestWrapper<ConvalidaAllegatoAttoPerElenchiMultiplo> parameters) {
		return BaseServiceExecutor.execute(appCtx,ConvalidaAllegatoAttoPerElenchiMultiploAsyncService.class, parameters);
	}
	
	//Lotto O
	@Override
	public RicercaElenchiPerAllegatoAttoResponse ricercaElenchiPerAllegatoAtto(RicercaElenchiPerAllegatoAtto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaElenchiPerAllegatoAttoService.class, parameters);
	}
	
	//CR 2705
	@Override
	public RicercaSinteticaStampaAllegatoAttoResponse ricercaSinteticaStampaAllegatoAtto(RicercaSinteticaStampaAllegatoAtto parameters) {
	    return BaseServiceExecutor.execute(appCtx, RicercaSinteticaStampaAllegatoAttoService.class, parameters);
	}

	@Override
	public RicercaSinteticaQuoteElencoResponse ricercaSinteticaQuoteElenco(RicercaSinteticaQuoteElenco parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaQuoteElencoService.class, parameters);
	}

	// SIAC-5172
	@Override
	public AggiornaMassivaDatiSoggettoAllegatoAttoResponse aggiornaMassivaDatiSoggettoAllegatoAtto(AggiornaMassivaDatiSoggettoAllegatoAtto parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaMassivaDatiSoggettoAllegatoAttoService.class, parameters);
	}

	@Override
	public RicercaDatiSospensioneAllegatoAttoResponse ricercaDatiSospensioneAllegatoAtto(
			RicercaDatiSospensioneAllegatoAtto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDatiSospensioneAllegatoAttoService.class, parameters);
	}
	// SIAC-6688
	@Override
	public ControlloImportiImpegniVincolatiResponse controlloImportiImpegniVincolati(ControlloImportiImpegniVincolati parameters) {
		return BaseServiceExecutor.execute(appCtx, ControlloImportiImpegniVincolatiService.class, parameters);
	}
}

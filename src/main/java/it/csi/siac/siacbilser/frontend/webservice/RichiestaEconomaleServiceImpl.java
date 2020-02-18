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
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRendicontoRichiestaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomaleAnticipoPerTrasfertaDipendentiService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomaleAnticipoSpesePerMissioneService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomaleAnticipoSpeseService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomalePagamentoFattureService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomalePagamentoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomaleRimborsoSpeseService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AggiornaRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.AnnullaRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.ControllaAggiornabilitaRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRendicontoRichiestaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleAnticipoPerTrasfertaDipendentiService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleAnticipoSpesePerMissioneService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleAnticipoSpeseService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomalePagamentoFattureService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomalePagamentoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleRimborsoSpeseService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.InserisceRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaClassificatoriGenericiCassaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioRendicontoRichiestaService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioRichiestaAnticipoMissioneNonErogataService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaDettaglioRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaMezziDiTrasportoService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaRichiesteAnticipoMissioniNonErogateService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaSinteticaModulareRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaSinteticaRichiestaEconomaleService;
import it.csi.siac.siacbilser.business.service.cassaeconomale.RicercaTipoGiustificativoService;
import it.csi.siac.siaccecser.frontend.webservice.CECSvcDictionary;
import it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AggiornaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.ControllaAggiornabilitaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.ControllaAggiornabilitaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriGenericiCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriGenericiCassaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiestaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaAnticipoMissioneNonErogata;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaAnticipoMissioneNonErogataResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaMezziDiTrasporto;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaMezziDiTrasportoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaRichiesteAnticipoMissioniNonErogate;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaRichiesteAnticipoMissioniNonErogateResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaModulareRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaModulareRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativoResponse;


/**
 * The Class CassaEconomaleServiceImpl.
 */
@WebService(serviceName = "RichiestaEconomaleService", portName = "RichiestaEconomaleServicePort", 
targetNamespace = CECSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siaccecser.frontend.webservice.RichiestaEconomaleService")
public class RichiestaEconomaleServiceImpl implements RichiestaEconomaleService {

	
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
	public InserisceRichiestaEconomaleResponse inserisceRichiestaEconomale(InserisceRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, InserisceRichiestaEconomaleService.class, parameters);
	}
	
	@Override
	public InserisceRichiestaEconomaleResponse inserisceRichiestaEconomaleRimborsoSpese(InserisceRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, InserisceRichiestaEconomaleRimborsoSpeseService.class, parameters);
	}
	
	@Override
	public InserisceRichiestaEconomaleResponse inserisceRichiestaEconomalePagamentoFatture(InserisceRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, InserisceRichiestaEconomalePagamentoFattureService.class, parameters);
	}
	
	@Override
	public InserisceRichiestaEconomaleResponse inserisceRichiestaEconomaleAnticipoSpese(InserisceRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, InserisceRichiestaEconomaleAnticipoSpeseService.class, parameters);
	}
	
	@Override
	public InserisceRichiestaEconomaleResponse inserisceRichiestaEconomaleAnticipoPerTrasfertaDipendenti(InserisceRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, InserisceRichiestaEconomaleAnticipoPerTrasfertaDipendentiService.class, parameters);
	}
	
	@Override
	public InserisceRichiestaEconomaleResponse inserisceRichiestaEconomaleAnticipoSpesePerMissione(InserisceRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, InserisceRichiestaEconomaleAnticipoSpesePerMissioneService.class, parameters);
	}
	
	@Override
	public InserisceRichiestaEconomaleResponse inserisceRichiestaEconomalePagamento(InserisceRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, InserisceRichiestaEconomalePagamentoService.class, parameters);
	}
	
	@Override
	public AggiornaRichiestaEconomaleResponse aggiornaRichiestaEconomale(AggiornaRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, AggiornaRichiestaEconomaleService.class, parameters);
	}
	
	@Override
	public AggiornaRichiestaEconomaleResponse aggiornaRichiestaEconomaleRimborsoSpese(AggiornaRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, AggiornaRichiestaEconomaleRimborsoSpeseService.class, parameters);
	}
	
	@Override
	public AggiornaRichiestaEconomaleResponse aggiornaRichiestaEconomalePagamentoFatture(AggiornaRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, AggiornaRichiestaEconomalePagamentoFattureService.class, parameters);
	}
	
	@Override
	public AggiornaRichiestaEconomaleResponse aggiornaRichiestaEconomaleAnticipoSpese(AggiornaRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, AggiornaRichiestaEconomaleAnticipoSpeseService.class, parameters);
	}
	
	@Override
	public AggiornaRichiestaEconomaleResponse aggiornaRichiestaEconomaleAnticipoPerTrasfertaDipendenti(AggiornaRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, AggiornaRichiestaEconomaleAnticipoPerTrasfertaDipendentiService.class, parameters);
	}
	
	@Override
	public AggiornaRichiestaEconomaleResponse aggiornaRichiestaEconomaleAnticipoSpesePerMissione(AggiornaRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, AggiornaRichiestaEconomaleAnticipoSpesePerMissioneService.class, parameters);
	}
	
	@Override
	public AggiornaRichiestaEconomaleResponse aggiornaRichiestaEconomalePagamento(AggiornaRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, AggiornaRichiestaEconomalePagamentoService.class, parameters);
	}
	
	@Override
	public RicercaDettaglioRichiestaEconomaleResponse ricercaDettaglioRichiestaEconomale(RicercaDettaglioRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioRichiestaEconomaleService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaRichiestaEconomaleResponse ricercaSinteticaRichiestaEconomale(RicercaSinteticaRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaRichiestaEconomaleService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaModulareRichiestaEconomaleResponse ricercaSinteticaModulareRichiestaEconomale(RicercaSinteticaModulareRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaModulareRichiestaEconomaleService.class, parameters);
	}
	
	@Override
	public AnnullaRichiestaEconomaleResponse annullaRichiestaEconomale(AnnullaRichiestaEconomale parameters){
		return BaseServiceExecutor.execute(appCtx, AnnullaRichiestaEconomaleService.class, parameters);
	}
	
	@Override
	public InserisceRendicontoRichiestaResponse inserisceRendicontoRichiesta(InserisceRendicontoRichiesta parameters){
		return BaseServiceExecutor.execute(appCtx, InserisceRendicontoRichiestaService.class, parameters);
	}
	
	@Override
	public AggiornaRendicontoRichiestaResponse aggiornaRendicontoRichiesta(AggiornaRendicontoRichiesta parameters){
		return BaseServiceExecutor.execute(appCtx, AggiornaRendicontoRichiestaService.class, parameters);
	}
	
	@Override
	public RicercaDettaglioRendicontoRichiestaResponse ricercaDettaglioRendicontoRichiesta(RicercaDettaglioRendicontoRichiesta parameters){
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioRendicontoRichiestaService.class, parameters);
	}

	@Override
	public RicercaMezziDiTrasportoResponse ricercaMezziDiTrasporto(RicercaMezziDiTrasporto parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaMezziDiTrasportoService.class, parameters);
	}

	@Override
	public RicercaTipoGiustificativoResponse ricercaTipoGiustificativo(RicercaTipoGiustificativo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoGiustificativoService.class, parameters);
	}

	@Override
	public RicercaClassificatoriGenericiCassaEconomaleResponse ricercaClassificatoriGenericiCassaEconomale(RicercaClassificatoriGenericiCassaEconomale parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaClassificatoriGenericiCassaEconomaleService.class, parameters);
	}

	@Override
	public ControllaAggiornabilitaRichiestaEconomaleResponse controllaAggiornabilitaRichiestaEconomale(ControllaAggiornabilitaRichiestaEconomale parameters) {
		return BaseServiceExecutor.execute(appCtx, ControllaAggiornabilitaRichiestaEconomaleService.class, parameters);
	}

	// Lotto P
	@Override
	public RicercaRichiesteAnticipoMissioniNonErogateResponse ricercaRichiesteAnticipoMissioniNonErogate(RicercaRichiesteAnticipoMissioniNonErogate parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaRichiesteAnticipoMissioniNonErogateService.class, parameters);
	}

	@Override
	public RicercaDettaglioRichiestaAnticipoMissioneNonErogataResponse ricercaDettaglioRichiesaAnticipoMissioneNonErogata(RicercaDettaglioRichiestaAnticipoMissioneNonErogata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioRichiestaAnticipoMissioneNonErogataService.class, parameters);
	}

}

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
import it.csi.siac.siacbilser.business.service.excel.variazionidibilancio.StampaExcelVariazioneDiBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.AggiornaAnagraficaVariazioneBilancioAsyncService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.AggiornaAnagraficaVariazioneBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.AggiornaDettaglioVariazioneImportoCapitoloService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.DefinisceAnagraficaVariazioneBilancioAsyncService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.DefinisceAnagraficaVariazioneBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.EliminaDettaglioVariazioneImportoCapitoloService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.GestisciDettaglioVariazioneComponenteImportoCapitoloService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.InserisceAnagraficaVariazioneBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.InserisceDettaglioResiduoVariazioneBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.InserisciDettaglioVariazioneImportoCapitoloService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.RicercaDettagliVariazionePrimoCapitoloNellaVariazioneService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.RicercaDettaglioAnagraficaVariazioneBilancioService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.RicercaDettaglioVariazioneComponenteImportoCapitoloService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneService;
import it.csi.siac.siacbilser.business.service.variazionibilancio.RicercaVariazioneBilancioService;
import it.csi.siac.siacbilser.business.service.variazionicodifica.AggiornaVariazioneCodificheService;
import it.csi.siac.siacbilser.business.service.variazionicodifica.DefinisceVariazioneCodificheService;
import it.csi.siac.siacbilser.business.service.variazionicodifica.InserisceVariazioneCodificheService;
import it.csi.siac.siacbilser.business.service.variazionicodifica.RicercaDettaglioVariazioneCodificheService;
import it.csi.siac.siacbilser.business.service.variazionicodifica.RicercaTipoVariazioneService;
import it.csi.siac.siacbilser.business.service.variazionicodifica.RicercaVariazioneCodificheService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.DefinisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.GestisciDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.GestisciDettaglioVariazioneComponenteImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceDettaglioResiduoVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceDettaglioResiduoVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioAnagraficaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoVariazioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneCodificheResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaExcelVariazioneDiBilancio;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaExcelVariazioneDiBilancioResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.Esito;

/**
 * Implementazione del servizio VariazioneDiBilancioService.
 */
@WebService(serviceName = "VariazioneDiBilancioService", portName = "VariazioneDiBilancioServicePort", 
			targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService")
public class VariazioneDiBilancioServiceImpl implements VariazioneDiBilancioService {

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
	@Deprecated
	public it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEBResponse inserisceStornoUEBEntrata(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEB parameters) {
		return  BaseServiceExecutor.execute(appCtx, it.csi.siac.siacbilser.business.service.stornoueb.InserisceStornoUEBEntrataService.class, parameters);
	}

	@Override
	@Deprecated
	public it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEBResponse inserisceStornoUEBUscita(it.csi.siac.siacbilser.frontend.webservice.msg.InserisceStornoUEB parameters) {
		return  BaseServiceExecutor.execute(appCtx, it.csi.siac.siacbilser.business.service.stornoueb.InserisceStornoUEBUscitaService.class, parameters);
	}

	@Override
	@Deprecated
	public it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEBResponse aggiornaStornoUEBEntrata(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEB parameters) {
		return BaseServiceExecutor.execute(appCtx, it.csi.siac.siacbilser.business.service.stornoueb.AggiornaStornoUEBEntrataService.class, parameters);
	}

	@Override
	@Deprecated
	public it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEBResponse aggiornaStornoUEBUscita(it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStornoUEB parameters) {
		return BaseServiceExecutor.execute(appCtx, it.csi.siac.siacbilser.business.service.stornoueb.AggiornaStornoUEBUscitaService.class, parameters);
	}

	@Override
	@Deprecated
	public it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStornoUEBResponse ricercaStornoUEB(it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStornoUEB parameters) {
		return BaseServiceExecutor.execute(appCtx, it.csi.siac.siacbilser.business.service.stornoueb.RicercaStornoUEBService.class, parameters);
	}

	@Override
	public RicercaVariazioneBilancioResponse ricercaVariazioneBilancio(RicercaVariazioneBilancio parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaVariazioneBilancioService.class, parameters);
	}

	@Override
	public InserisceVariazioneCodificheResponse inserisceVariazioneCodifiche(InserisceVariazioneCodifiche parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceVariazioneCodificheService.class, parameters);
	}

	@Override
	public AggiornaVariazioneCodificheResponse aggiornaVariazioneCodifiche(AggiornaVariazioneCodifiche parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaVariazioneCodificheService.class, parameters);
	}

	@Override
	public DefinisceVariazioneCodificheResponse definisceVariazioneCodifiche(DefinisceVariazioneCodifiche parameters) {
		return BaseServiceExecutor.execute(appCtx, DefinisceVariazioneCodificheService.class, parameters);
	}

	@Override
	public RicercaVariazioneCodificheResponse ricercaVariazioneCodifiche(RicercaVariazioneCodifiche parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaVariazioneCodificheService.class, parameters);
	}

	@Override
	public RicercaDettaglioVariazioneCodificheResponse ricercaDettaglioVariazioneCodifiche(RicercaDettaglioVariazioneCodifiche parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioVariazioneCodificheService.class, parameters);
	}

	@Override
	public RicercaTipoVariazioneResponse ricercaTipoVariazione(RicercaTipoVariazione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoVariazioneService.class, parameters);
	}

	//######################### New 2016/05/17 ############################//

	@Override
	public InserisciDettaglioVariazioneImportoCapitoloResponse inserisciDettaglioVariazioneImportoCapitolo(InserisciDettaglioVariazioneImportoCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciDettaglioVariazioneImportoCapitoloService.class, parameters);
	}

	@Override
	public AggiornaDettaglioVariazioneImportoCapitoloResponse aggiornaDettaglioVariazioneImportoCapitolo(AggiornaDettaglioVariazioneImportoCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaDettaglioVariazioneImportoCapitoloService.class, parameters);
	}

	@Override
	public EliminaDettaglioVariazioneImportoCapitoloResponse eliminaDettaglioVariazioneImportoCapitolo(EliminaDettaglioVariazioneImportoCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaDettaglioVariazioneImportoCapitoloService.class, parameters);
	}

	@Override
	public AggiornaAnagraficaVariazioneBilancioResponse aggiornaAnagraficaVariazioneBilancio(AggiornaAnagraficaVariazioneBilancio parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaAnagraficaVariazioneBilancioService.class, parameters);
	}

	@Override
	public AsyncServiceResponse aggiornaAnagraficaVariazioneBilancioAsync(AsyncServiceRequestWrapper<AggiornaAnagraficaVariazioneBilancio> parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaAnagraficaVariazioneBilancioAsyncService.class, parameters);
	}

	@Override
	public RicercaDettaglioAnagraficaVariazioneBilancioResponse ricercaDettaglioAnagraficaVariazioneBilancio(RicercaDettaglioAnagraficaVariazioneBilancio parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioAnagraficaVariazioneBilancioService.class, parameters);
	}

	@Override
	public RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse ricercaDettagliVariazioneImportoCapitoloNellaVariazione(RicercaDettagliVariazioneImportoCapitoloNellaVariazione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettagliVariazioneImportoCapitoloNellaVariazioneService.class, parameters);
	}
	//SIAC-6884 primo capitolo inserito nella variazione
	@Override
	public RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse ricercaDettagloVariazionePrimoCapitoloNellaVariazione(RicercaDettagliVariazioneImportoCapitoloNellaVariazione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettagliVariazionePrimoCapitoloNellaVariazioneService.class, parameters);
	}
	
	@Override
	public RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneResponse ricercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione(RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazione parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSingoloDettaglioVariazioneImportoCapitoloNellaVariazioneService.class, parameters);
	}

	@Override
	public InserisceAnagraficaVariazioneBilancioResponse inserisceAnagraficaVariazioneBilancio(InserisceAnagraficaVariazioneBilancio parameters) {
		//return BaseServiceExecutor.execute(appCtx, InserisceAnagraficaVariazioneBilancioService.class, parameters);
		InserisceAnagraficaVariazioneBilancioResponse res = BaseServiceExecutor.execute(appCtx, InserisceAnagraficaVariazioneBilancioService.class, parameters);
		if(Boolean.TRUE.equals(parameters.getCaricaResidui()) && !res.isFallimento() && ! res.hasErrori()) {
			// FIXME: brutto brutto bruuuuuuuutto, e' una martellata da modificare
			InserisceDettaglioResiduoVariazioneBilancio requestIDRVB = new InserisceDettaglioResiduoVariazioneBilancio();
			requestIDRVB.setRichiedente(parameters.getRichiedente());
			requestIDRVB.setVariazioneImportoCapitolo(res.getVariazioneImportoCapitolo());
			InserisceDettaglioResiduoVariazioneBilancioResponse response = BaseServiceExecutor.execute(appCtx, InserisceDettaglioResiduoVariazioneBilancioService.class, requestIDRVB);
			
			res.addErrori(response.getErrori());
			if(response.isFallimento()) {
				res.setEsito(Esito.FALLIMENTO);
			}
		}
		
		return res;
	}

	@Override
	public DefinisceAnagraficaVariazioneBilancioResponse definisceAnagraficaVariazioneBilancio(DefinisceAnagraficaVariazioneBilancio parameters) {
		return BaseServiceExecutor.execute(appCtx, DefinisceAnagraficaVariazioneBilancioService.class, parameters);
	}

	@Override
	public AsyncServiceResponse definisceAnagraficaVariazioneBilancioAsync(AsyncServiceRequestWrapper<DefinisceAnagraficaVariazioneBilancio> parameters) {
		return BaseServiceExecutor.execute(appCtx, DefinisceAnagraficaVariazioneBilancioAsyncService.class, parameters);
	}

	@Override
	public StampaExcelVariazioneDiBilancioResponse stampaExcelVariazioneDiBilancio(StampaExcelVariazioneDiBilancio parameters) {
		return appCtx.getBean(Utility.toDefaultBeanName(StampaExcelVariazioneDiBilancioService.class), StampaExcelVariazioneDiBilancioService.class).executeService(parameters);
	}

	// SIAC-6881
	@Override
	public GestisciDettaglioVariazioneComponenteImportoCapitoloResponse gestisciDettaglioVariazioneComponenteImportoCapitolo(GestisciDettaglioVariazioneComponenteImportoCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, GestisciDettaglioVariazioneComponenteImportoCapitoloService.class, parameters);
	}

	@Override
	public RicercaDettaglioVariazioneComponenteImportoCapitoloResponse ricercaDettaglioVariazioneComponenteImportoCapitolo(RicercaDettaglioVariazioneComponenteImportoCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioVariazioneComponenteImportoCapitoloService.class, parameters);
	}


}

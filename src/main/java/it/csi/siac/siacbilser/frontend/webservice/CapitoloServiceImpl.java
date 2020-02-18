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
import it.csi.siac.siacbilser.business.service.capitolo.AggiornaStanziamentiCapitoliVariatiService;
import it.csi.siac.siacbilser.business.service.capitolo.CalcolaTotaliStanziamentiDiPrevisioneService;
import it.csi.siac.siacbilser.business.service.capitolo.CalcoloDisponibilitaDiUnCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.ContaMovimentiAssociatiACapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.ControllaAttributiModificabiliCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.ControllaClassificatoriModificabiliCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.RicercaCategoriaCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.RicercaSinteticaVariazioniSingoloCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.RicercaStoricoVariazioniCodificheCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolo.RicercaVariazioniCapitoloService;
import it.csi.siac.siacbilser.business.service.capitolouscitagestione.RicercaVariazioniCapitoloPerAggiornamentoCapitoloService;
import it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo.AggiornaRelazioneAttoDiLeggeCapitoloService;
import it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo.CancellaRelazioneAttoDiLeggeCapitoloService;
import it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo.InserisceRelazioneAttoDiLeggeCapitoloService;
import it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo.RicercaPuntualeRelazioneAttoDiLeggeCapitoloService;
import it.csi.siac.siacbilser.business.service.relazioneattodileggecapitolo.RicercaRelazioneAttoDiLeggeCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStanziamentiCapitoliVariati;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaStanziamentiCapitoliVariatiResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcolaTotaliStanziamentiDiPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.CancellaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaMovimentiAssociatiACapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ContaMovimentiAssociatiACapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCategoriaCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCategoriaCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaRelazioneAttoDiLeggeCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaRelazioneAttoDiLeggeCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaVariazioniSingoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaVariazioniSingoloCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStoricoVariazioniCodificheCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStoricoVariazioniCodificheCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloResponse;

/**
 * Implementazione del servizio CapitoloService.
 *
 * @author VM
 * @version $Id: $
 */
@WebService(serviceName = "CapitoloService", portName = "CapitoloServicePort", targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.CapitoloService")
public class CapitoloServiceImpl implements CapitoloService {

	/** The app ctx. */
	@Autowired
	private ApplicationContext appCtx;

	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	public AggiornaStanziamentiCapitoliVariatiResponse aggiornaStanziamentiCapitoliVariati(AggiornaStanziamentiCapitoliVariati arg) {
		return BaseServiceExecutor.execute(appCtx, AggiornaStanziamentiCapitoliVariatiService.class, arg);
	}

	@Override
	public CalcolaTotaliStanziamentiDiPrevisioneResponse calcolaTotaliStanziamentiDiPrevisione(CalcolaTotaliStanziamentiDiPrevisione arg) {
		return BaseServiceExecutor.execute(appCtx, CalcolaTotaliStanziamentiDiPrevisioneService.class, arg);
	}

	@Override
	public CalcoloDisponibilitaDiUnCapitoloResponse calcoloDisponibilitaDiUnCapitolo(CalcoloDisponibilitaDiUnCapitolo arg) {
		return BaseServiceExecutor.execute(appCtx, CalcoloDisponibilitaDiUnCapitoloService.class, arg);
	}
	
	@Override
	public ControllaClassificatoriModificabiliCapitoloResponse controllaClassificatoriModificabiliCapitolo(ControllaClassificatoriModificabiliCapitolo arg){
		return BaseServiceExecutor.execute(appCtx, ControllaClassificatoriModificabiliCapitoloService.class, arg);
	}
	
	@Override
	public ControllaAttributiModificabiliCapitoloResponse controllaAttributiModificabiliCapitolo(ControllaAttributiModificabiliCapitolo arg){
		return BaseServiceExecutor.execute(appCtx, ControllaAttributiModificabiliCapitoloService.class, arg);
	}
	
	@Override
	public InserisceRelazioneAttoDiLeggeCapitoloResponse inserisceRelazioneAttoDiLeggeCapitolo(InserisceRelazioneAttoDiLeggeCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceRelazioneAttoDiLeggeCapitoloService.class, parameters);
	}

	@Override
	public AggiornaRelazioneAttoDiLeggeCapitoloResponse aggiornaRelazioneAttoDiLeggeCapitolo(AggiornaRelazioneAttoDiLeggeCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaRelazioneAttoDiLeggeCapitoloService.class, parameters);
	}

	@Override
	public RicercaRelazioneAttoDiLeggeCapitoloResponse ricercaRelazioneAttoDiLeggeCapitolo(RicercaRelazioneAttoDiLeggeCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaRelazioneAttoDiLeggeCapitoloService.class, parameters);
	}

	@Override
	public CancellaRelazioneAttoDiLeggeCapitoloResponse cancellaRelazioneAttoDiLeggeCapitolo(CancellaRelazioneAttoDiLeggeCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, CancellaRelazioneAttoDiLeggeCapitoloService.class, parameters);
	}

	@Override
	public RicercaRelazioneAttoDiLeggeCapitoloResponse ricercaPuntualeRelazioneAttoDiLeggeCapitolo(RicercaRelazioneAttoDiLeggeCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaPuntualeRelazioneAttoDiLeggeCapitoloService.class, parameters);
	}
	
	@Override
	public RicercaCategoriaCapitoloResponse ricercaCategoriaCapitolo(RicercaCategoriaCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaCategoriaCapitoloService.class, parameters);
	}

	@Override
	public RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse ricercaVariazioniCapitoloPerAggiornamentoCapitolo(RicercaVariazioniCapitoloPerAggiornamentoCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaVariazioniCapitoloPerAggiornamentoCapitoloService.class, parameters);
	}
	
	@Override
	public ContaMovimentiAssociatiACapitoloResponse contaMovimentiAssociatiACapitolo(ContaMovimentiAssociatiACapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, ContaMovimentiAssociatiACapitoloService.class, parameters);
	}

	@Override
	public RicercaVariazioniCapitoloResponse ricercaVariazioniCapitolo(RicercaVariazioniCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaVariazioniCapitoloService.class, parameters);
	}

	@Override
	public RicercaSinteticaVariazioniSingoloCapitoloResponse ricercaSinteticaVariazioniSingoloCapitolo(RicercaSinteticaVariazioniSingoloCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaVariazioniSingoloCapitoloService.class, parameters);
	}

	@Override
	public RicercaStoricoVariazioniCodificheCapitoloResponse ricercaStoricoVariazioniCodificheCapitolo(RicercaStoricoVariazioniCodificheCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaStoricoVariazioniCodificheCapitoloService.class, parameters);
	}

}

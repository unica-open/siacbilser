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

import it.csi.siac.siacbilser.business.service.conciliazione.AggiornaConciliazionePerBeneficiarioService;
import it.csi.siac.siacbilser.business.service.conciliazione.AggiornaConciliazionePerCapitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.AggiornaConciliazionePerTitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.EliminaConciliazionePerBeneficiarioService;
import it.csi.siac.siacbilser.business.service.conciliazione.EliminaConciliazionePerCapitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.EliminaConciliazionePerTitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.InserisceConciliazionePerBeneficiarioService;
import it.csi.siac.siacbilser.business.service.conciliazione.InserisceConciliazionePerCapitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.InserisceConciliazionePerTitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.InserisceConciliazioniPerBeneficiarioService;
import it.csi.siac.siacbilser.business.service.conciliazione.InserisceConciliazioniPerCapitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaContiConciliazionePerClasseService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaContiConciliazionePerTitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaDettaglioConciliazionePerBeneficiarioService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaDettaglioConciliazionePerCapitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaDettaglioConciliazionePerTitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaSinteticaConciliazionePerBeneficiarioService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaSinteticaConciliazionePerCapitoloService;
import it.csi.siac.siacbilser.business.service.conciliazione.RicercaSinteticaConciliazionePerTitoloService;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacgenser.frontend.webservice.ConciliazioneService;
import it.csi.siac.siacgenser.frontend.webservice.GENSvcDictionary;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConciliazioniPerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerClasseResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaContiConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConciliazionePerTitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerBeneficiarioResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerCapitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerCapitoloResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerTitolo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConciliazionePerTitoloResponse;

/**
 * Implementatore dei servizi per la conciliazione GSA.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 26/10/2015
 *
 */
@WebService(serviceName = "ConciliazioneService", portName = "ConciliazioneServicePort", targetNamespace = GENSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacgenser.frontend.webservice.ConciliazioneService")
public class ConciliazioneServiceImpl implements ConciliazioneService {

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
	public InserisceConciliazionePerTitoloResponse inserisceConciliazionePerTitolo(InserisceConciliazionePerTitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, InserisceConciliazionePerTitoloService.class).executeService(parameters);
	}

	@Override
	public AggiornaConciliazionePerTitoloResponse aggiornaConciliazionePerTitolo(AggiornaConciliazionePerTitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, AggiornaConciliazionePerTitoloService.class).executeService(parameters);
	}

	@Override
	public EliminaConciliazionePerTitoloResponse eliminaConciliazionePerTitolo(EliminaConciliazionePerTitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, EliminaConciliazionePerTitoloService.class).executeService(parameters);
	}

	@Override
	public RicercaDettaglioConciliazionePerTitoloResponse ricercaDettaglioConciliazionePerTitolo(RicercaDettaglioConciliazionePerTitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaDettaglioConciliazionePerTitoloService.class).executeService(parameters);
	}
	
	@Override
	public RicercaSinteticaConciliazionePerTitoloResponse ricercaSinteticaConciliazionePerTitolo(RicercaSinteticaConciliazionePerTitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaSinteticaConciliazionePerTitoloService.class).executeService(parameters);
	}

	@Override
	public RicercaContiConciliazionePerTitoloResponse ricercaContiConciliazionePerTitolo(RicercaContiConciliazionePerTitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaContiConciliazionePerTitoloService.class).executeService(parameters);
	}

	@Override
	public InserisceConciliazionePerCapitoloResponse inserisceConciliazionePerCapitolo(InserisceConciliazionePerCapitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, InserisceConciliazionePerCapitoloService.class).executeService(parameters);
	}

	@Override
	public InserisceConciliazioniPerCapitoloResponse inserisceConciliazioniPerCapitolo(InserisceConciliazioniPerCapitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, InserisceConciliazioniPerCapitoloService.class).executeService(parameters);
	}

	@Override
	public AggiornaConciliazionePerCapitoloResponse aggiornaConciliazionePerCapitolo(AggiornaConciliazionePerCapitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, AggiornaConciliazionePerCapitoloService.class).executeService(parameters);
	}

	@Override
	public EliminaConciliazionePerCapitoloResponse eliminaConciliazionePerCapitolo(EliminaConciliazionePerCapitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, EliminaConciliazionePerCapitoloService.class).executeService(parameters);
	}

	@Override
	public RicercaDettaglioConciliazionePerCapitoloResponse ricercaDettaglioConciliazionePerCapitolo(RicercaDettaglioConciliazionePerCapitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaDettaglioConciliazionePerCapitoloService.class).executeService(parameters);
	}
	
	@Override
	public RicercaSinteticaConciliazionePerCapitoloResponse ricercaSinteticaConciliazionePerCapitolo(RicercaSinteticaConciliazionePerCapitolo parameters) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaSinteticaConciliazionePerCapitoloService.class).executeService(parameters);
	}

	@Override
	public InserisceConciliazionePerBeneficiarioResponse inserisceConciliazionePerBeneficiario(InserisceConciliazionePerBeneficiario parameters) {
		return Utility.getBeanViaDefaultName(appCtx, InserisceConciliazionePerBeneficiarioService.class).executeService(parameters);
	}

	@Override
	public InserisceConciliazioniPerBeneficiarioResponse inserisceConciliazioniPerBeneficiario(InserisceConciliazioniPerBeneficiario parameters) {
		return Utility.getBeanViaDefaultName(appCtx, InserisceConciliazioniPerBeneficiarioService.class).executeService(parameters);
	}

	@Override
	public AggiornaConciliazionePerBeneficiarioResponse aggiornaConciliazionePerBeneficiario(AggiornaConciliazionePerBeneficiario parameters) {
		return Utility.getBeanViaDefaultName(appCtx, AggiornaConciliazionePerBeneficiarioService.class).executeService(parameters);
	}

	@Override
	public EliminaConciliazionePerBeneficiarioResponse eliminaConciliazionePerBeneficiario(EliminaConciliazionePerBeneficiario parameters) {
		return Utility.getBeanViaDefaultName(appCtx, EliminaConciliazionePerBeneficiarioService.class).executeService(parameters);
	}

	@Override
	public RicercaDettaglioConciliazionePerBeneficiarioResponse ricercaDettaglioConciliazionePerBeneficiario(RicercaDettaglioConciliazionePerBeneficiario parameters) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaDettaglioConciliazionePerBeneficiarioService.class).executeService(parameters);
	}
	
	@Override
	public RicercaSinteticaConciliazionePerBeneficiarioResponse ricercaSinteticaConciliazionePerBeneficiario(RicercaSinteticaConciliazionePerBeneficiario parameters) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaSinteticaConciliazionePerBeneficiarioService.class).executeService(parameters);
	}
	
	//SIAC-4956 sostituito con ricercaContiConciliazionePerClasse
//	@Override
//	public RicercaContiConciliazionePerCausaleEPResponse ricercaContiConciliazionePerCausaleEP(RicercaContiConciliazionePerCausaleEP parameters) {
//		return Utility.getBeanViaDefaultName(appCtx, RicercaContiConciliazionePerCausaleEPService.class).executeService(parameters);
//	}
	
	@Override
	public RicercaContiConciliazionePerClasseResponse ricercaContiConciliazionePerClasse(RicercaContiConciliazionePerClasse parameters) {
		return Utility.getBeanViaDefaultName(appCtx, RicercaContiConciliazionePerClasseService.class).executeService(parameters);
	}
}

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
import it.csi.siac.siacbilser.business.service.primanota.AggiornaPrimaNotaGENService;
import it.csi.siac.siacbilser.business.service.primanota.AggiornaPrimaNotaIntegrataManualeService;
import it.csi.siac.siacbilser.business.service.primanota.AggiornaPrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.AnnullaPrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.CollegaPrimeNoteService;
import it.csi.siac.siacbilser.business.service.primanota.EliminaCollegamentoPrimeNoteService;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaIntegrataManualeService;
import it.csi.siac.siacbilser.business.service.primanota.InseriscePrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.RegistraMassivaPrimaNotaIntegrataAsyncService;
import it.csi.siac.siacbilser.business.service.primanota.RegistraMassivaPrimaNotaIntegrataService;
import it.csi.siac.siacbilser.business.service.primanota.RegistraPrimaNotaIntegrataService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaDettaglioPrimaNotaIntegrataService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaDettaglioPrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaPrimeNoteService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaSinteticaPrimaNotaIntegrataManualeService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaSinteticaPrimaNotaIntegrataService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaSinteticaPrimaNotaIntegrataValidabileService;
import it.csi.siac.siacbilser.business.service.primanota.RicercaSinteticaPrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.ValidaPrimaNotaService;
import it.csi.siac.siacbilser.business.service.primanota.ValidazioneMassivaPrimaNotaIntegrataAsyncService;
import it.csi.siac.siacbilser.business.service.primanota.ValidazioneMassivaPrimaNotaIntegrataService;
import it.csi.siac.siacbilser.business.service.rateirisconti.AggiornaRateoService;
import it.csi.siac.siacbilser.business.service.rateirisconti.AggiornaRiscontoService;
import it.csi.siac.siacbilser.business.service.rateirisconti.InserisciRateoService;
import it.csi.siac.siacbilser.business.service.rateirisconti.InserisciRiscontoService;
import it.csi.siac.siacbilser.business.service.rateirisconti.OttieniEntitaCollegatePrimaNotaService;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacgenser.frontend.webservice.GENSvcDictionary;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRateo;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRateoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRisconto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaRiscontoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.CollegaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.CollegaPrimeNoteResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCollegamentoPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaCollegamentoPrimeNoteResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRateo;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRateoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRisconto;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisciRiscontoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraMassivaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraMassivaPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNoteResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabile;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaIntegrataValidabileResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidazioneMassivaPrimaNotaIntegrataResponse;

@WebService(serviceName = "PrimaNotaService", portName = "PrimaNotaServicePort", 
targetNamespace = GENSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService")
public class PrimaNotaServiceImpl implements PrimaNotaService {

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
	public InseriscePrimaNotaResponse inseriscePrimaNota(InseriscePrimaNota parameters) {
		return BaseServiceExecutor.execute(appCtx, InseriscePrimaNotaService.class, parameters);
	}

	@Override
	public AggiornaPrimaNotaResponse aggiornaPrimaNota(AggiornaPrimaNota parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaPrimaNotaService.class, parameters);
	}
	
	@Override
	public AggiornaPrimaNotaResponse aggiornaPrimaNotaGEN(AggiornaPrimaNota parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaPrimaNotaGENService.class, parameters);
	}

	@Override
	public AnnullaPrimaNotaResponse annullaPrimaNota(AnnullaPrimaNota parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaPrimaNotaService.class, parameters);
	}

	@Override
	public RicercaSinteticaPrimaNotaResponse ricercaSinteticaPrimaNota(RicercaSinteticaPrimaNota parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaPrimaNotaService.class, parameters);
	}

	@Override
	public RicercaDettaglioPrimaNotaResponse ricercaDettaglioPrimaNota(RicercaDettaglioPrimaNota parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioPrimaNotaService.class, parameters);
	}


	@Override
	public ValidaPrimaNotaResponse validaPrimaNota(ValidaPrimaNota parameters) {
		return BaseServiceExecutor.execute(appCtx, ValidaPrimaNotaService.class, parameters);
	}

	@Override
	public RegistraPrimaNotaIntegrataResponse registraPrimaNotaIntegrata(RegistraPrimaNotaIntegrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RegistraPrimaNotaIntegrataService.class, parameters);
	}

	@Override
	public RicercaSinteticaPrimaNotaIntegrataResponse ricercaSinteticaPrimaNotaIntegrata(RicercaSinteticaPrimaNotaIntegrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaPrimaNotaIntegrataService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaPrimaNotaIntegrataValidabileResponse ricercaSinteticaPrimaNotaIntegrataValidabile(RicercaSinteticaPrimaNotaIntegrataValidabile parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaPrimaNotaIntegrataValidabileService.class, parameters);
	}

	@Override
	public RicercaDettaglioPrimaNotaIntegrataResponse ricercaDettaglioPrimaNotaIntegrata(RicercaDettaglioPrimaNotaIntegrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioPrimaNotaIntegrataService.class, parameters);
	}

	@Override
	public ValidazioneMassivaPrimaNotaIntegrataResponse validazioneMassivaPrimaNotaIntegrata(ValidazioneMassivaPrimaNotaIntegrata parameters) {
		return BaseServiceExecutor.execute(appCtx, ValidazioneMassivaPrimaNotaIntegrataService.class, parameters);
	}

	@Override
	public AsyncServiceResponse validazioneMassivaPrimaNotaIntegrataAsync(AsyncServiceRequestWrapper<ValidazioneMassivaPrimaNotaIntegrata> parameters) {
		return BaseServiceExecutor.execute(appCtx, ValidazioneMassivaPrimaNotaIntegrataAsyncService.class, parameters);
	}

	@Override
	public CollegaPrimeNoteResponse collegaPrimeNote(CollegaPrimeNote parameters) {
		return BaseServiceExecutor.execute(appCtx, CollegaPrimeNoteService.class, parameters);
	}
	
	@Override
	public EliminaCollegamentoPrimeNoteResponse eliminaCollegamentoPrimeNote(EliminaCollegamentoPrimeNote parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaCollegamentoPrimeNoteService.class, parameters);
	}

	@Override
	public RicercaPrimeNoteResponse ricercaPrimeNote(RicercaPrimeNote parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaPrimeNoteService.class, parameters);
	}

	@Override
	public InserisciRateoResponse inserisciRateo(InserisciRateo parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciRateoService.class, parameters);
	}

	@Override
	public AggiornaRateoResponse aggiornaRateo(AggiornaRateo parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaRateoService.class, parameters);
	}

	@Override
	public InserisciRiscontoResponse inserisciRisconto(InserisciRisconto parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciRiscontoService.class, parameters);
	}

	@Override
	public AggiornaRiscontoResponse aggiornaRisconto(AggiornaRisconto parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaRiscontoService.class, parameters);
	}

	@Override
	public OttieniEntitaCollegatePrimaNotaResponse ottieniEntitaCollegatePrimaNota(OttieniEntitaCollegatePrimaNota parameters) {
		return BaseServiceExecutor.execute(appCtx, OttieniEntitaCollegatePrimaNotaService.class, parameters);
	}

	@Override
	public RegistraMassivaPrimaNotaIntegrataResponse registraMassivaPrimaNotaIntegrata(RegistraMassivaPrimaNotaIntegrata parameters) {
		return BaseServiceExecutor.execute(appCtx, RegistraMassivaPrimaNotaIntegrataService.class, parameters);
	}

	@Override
	public AsyncServiceResponse registraMassivaPrimaNotaIntegrataAsync(AsyncServiceRequestWrapper<RegistraMassivaPrimaNotaIntegrata> parameters) {
		return BaseServiceExecutor.execute(appCtx, RegistraMassivaPrimaNotaIntegrataAsyncService.class, parameters);
	}

	@Override
	public InseriscePrimaNotaIntegrataManualeResponse inseriscePrimaNotaIntegrataManuale(InseriscePrimaNotaIntegrataManuale parameters) {
		return BaseServiceExecutor.execute(appCtx, InseriscePrimaNotaIntegrataManualeService.class, parameters);
	}

	@Override
	public RicercaSinteticaPrimaNotaIntegrataManualeResponse ricercaSinteticaPrimaNotaIntegrataManuale(RicercaSinteticaPrimaNotaIntegrataManuale parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaPrimaNotaIntegrataManualeService.class, parameters);
	}

	@Override
	public AggiornaPrimaNotaIntegrataManualeResponse aggiornaPrimaNotaIntegrataManuale(AggiornaPrimaNotaIntegrataManuale parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaPrimaNotaIntegrataManualeService.class, parameters);
	}

}

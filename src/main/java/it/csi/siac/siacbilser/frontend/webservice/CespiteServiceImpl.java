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
import it.csi.siac.siacbilser.business.service.cespiti.AggiornaAnagraficaDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.AggiornaCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.AggiornaVariazioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.CollegaCespiteDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.EliminaCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.EliminaDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.EliminaVariazioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciAmmortamentoMassivoCespiteAsyncService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciAmmortamentoMassivoCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciAnagraficaDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciAnteprimaAmmortamentoAnnuoCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciPrimeNoteAmmortamentoAnnuoCespiteAsyncService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciPrimeNoteAmmortamentoAnnuoCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciPrimeNoteDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciVariazioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaCespitePerChiaveService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaDettaglioCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaDettaglioDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaDettaglioVariazioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaScrittureInventarioByEntitaCollegataService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaDismissioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaScrittureRegistroAByCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaVariazioneCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.ScollegaCespiteDismissioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.CESPSvcDictionary;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaAnagraficaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaAnagraficaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.EliminaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAmmortamentoMassivoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnagraficaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnagraficaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnteprimaAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespitePerChiave;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespitePerChiaveResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaScrittureInventarioByEntitaCollegata;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaScrittureInventarioByEntitaCollegataResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaScrittureRegistroAByCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaScrittureRegistroAByCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteDismissioneCespiteResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;

@WebService(serviceName = "CespiteService", portName = "CespiteServicePort", 
targetNamespace = CESPSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siaccespser.frontend.webservice.CespiteService")
public class CespiteServiceImpl implements CespiteService {

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
	public InserisciCespiteResponse inserisciCespite(InserisciCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciCespiteService.class, parameters);

	}

	@Override
	public AggiornaCespiteResponse aggiornaCespite(AggiornaCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaCespiteService.class, parameters);
	}

	@Override
	public RicercaDettaglioCespiteResponse ricercaDettaglioCespite(RicercaDettaglioCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioCespiteService.class, parameters);
	}
	
	@Override
	public RicercaCespitePerChiaveResponse ricercaCespitePerChiave(RicercaCespitePerChiave parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaCespitePerChiaveService.class, parameters);
	}

	@Override
	public RicercaSinteticaCespiteResponse ricercaSinteticaCespite(RicercaSinteticaCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaCespiteService.class, parameters);
	}

	@Override
	public EliminaCespiteResponse eliminaCespite(EliminaCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaCespiteService.class, parameters);
	}

	/*********** DISMISSIONI **********************/

	@Override
	public InserisciAnagraficaDismissioneCespiteResponse inserisciAnagraficaDismissioneCespite(InserisciAnagraficaDismissioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciAnagraficaDismissioneCespiteService.class, parameters);
	}

	@Override
	public AggiornaAnagraficaDismissioneCespiteResponse aggiornaAnagraficaDismissioneCespite(AggiornaAnagraficaDismissioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaAnagraficaDismissioneCespiteService.class, parameters);
	}

	@Override
	public RicercaDettaglioDismissioneCespiteResponse ricercaDettaglioDismissioneCespite(RicercaDettaglioDismissioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioDismissioneCespiteService.class, parameters);
	}

	@Override
	public RicercaSinteticaDismissioneCespiteResponse ricercaSinteticaDismissioneCespite(RicercaSinteticaDismissioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaDismissioneCespiteService.class, parameters);
	}

	@Override
	public EliminaDismissioneCespiteResponse eliminaDismissioneCespite(EliminaDismissioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaDismissioneCespiteService.class, parameters);
	}

	@Override
	public CollegaCespiteDismissioneCespiteResponse collegaCespiteDismissioneCespite(CollegaCespiteDismissioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, CollegaCespiteDismissioneCespiteService.class, parameters);
	}

	@Override
	public ScollegaCespiteDismissioneCespiteResponse scollegaCespiteDismissioneCespite(ScollegaCespiteDismissioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, ScollegaCespiteDismissioneCespiteService.class, parameters);
	}
	
	@Override
	public InserisciPrimeNoteDismissioneCespiteResponse inserisciPrimeNoteDismissioneCespite(InserisciPrimeNoteDismissioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciPrimeNoteDismissioneCespiteService.class, parameters);
	}


	public InserisciVariazioneCespiteResponse inserisciVariazioneCespite(InserisciVariazioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciVariazioneCespiteService.class, parameters);
	}

	@Override
	public AggiornaVariazioneCespiteResponse aggiornaVariazioneCespite(AggiornaVariazioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaVariazioneCespiteService.class, parameters);
	}
	
	@Override
	public EliminaVariazioneCespiteResponse eliminaVariazioneCespite(EliminaVariazioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaVariazioneCespiteService.class, parameters);
	}

	@Override
	public RicercaDettaglioVariazioneCespiteResponse ricercaDettaglioVariazioneCespite(RicercaDettaglioVariazioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioVariazioneCespiteService.class, parameters);
	}

	@Override
	public RicercaSinteticaVariazioneCespiteResponse ricercaSinteticaVariazioneCespite(RicercaSinteticaVariazioneCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaVariazioneCespiteService.class, parameters);
	}

	@Override
	public RicercaScrittureInventarioByEntitaCollegataResponse ricercaScrittureInventarioByEntitaCollegata(RicercaScrittureInventarioByEntitaCollegata parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaScrittureInventarioByEntitaCollegataService.class, parameters);
	}

	@Override
	public InserisciAmmortamentoMassivoCespiteResponse inserisciAmmortamentoMassivoCespite(InserisciAmmortamentoMassivoCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciAmmortamentoMassivoCespiteService.class, parameters);
	}

	@Override
	public AsyncServiceResponse inserisciAmmortamentoMassivoCespiteAsync(AsyncServiceRequestWrapper<InserisciAmmortamentoMassivoCespite> parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciAmmortamentoMassivoCespiteAsyncService.class, parameters);
	}

	@Override
	public RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteResponse ricercaSinteticaDettaglioAmmortamentoAnnuoCespite(	RicercaSinteticaDettaglioAmmortamentoAnnuoCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaDettaglioAmmortamentoAnnuoCespiteService.class, parameters);
	}

	@Override
	public RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteService.class, parameters);
	}

	@Override
	public InserisciAnteprimaAmmortamentoAnnuoCespiteResponse inserisciAnteprimaAmmortamentoAnnuoCespite(InserisciAnteprimaAmmortamentoAnnuoCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciAnteprimaAmmortamentoAnnuoCespiteService.class, parameters);
	}

	@Override
	public InserisciPrimeNoteAmmortamentoAnnuoCespiteResponse inserisciPrimeNoteAmmortamentoAnnuoCespite(InserisciPrimeNoteAmmortamentoAnnuoCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciPrimeNoteAmmortamentoAnnuoCespiteService.class, parameters);
	}

	@Override
	public AsyncServiceResponse inserisciPrimeNoteAmmortamentoAnnuoCespiteAsync(AsyncServiceRequestWrapper<InserisciPrimeNoteAmmortamentoAnnuoCespite> parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciPrimeNoteAmmortamentoAnnuoCespiteAsyncService.class, parameters);
	}

	@Override
	public RicercaSinteticaRegistroACespiteResponse ricercaSinteticaRegistroACespite(RicercaSinteticaRegistroACespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaRegistroACespiteService.class, parameters);
	}

	@Override
	public RicercaSinteticaScrittureRegistroAByCespiteResponse ricercaSinteticaScrittureRegistroAByCespite(RicercaSinteticaScrittureRegistroAByCespite parameters) {
		// TODO Auto-generated method stub
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaScrittureRegistroAByCespiteService.class, parameters);
	}

}

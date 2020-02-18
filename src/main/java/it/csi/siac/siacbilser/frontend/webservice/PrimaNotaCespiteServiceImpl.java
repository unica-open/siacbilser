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
import it.csi.siac.siacbilser.business.service.cespiti.AggiornaImportoCespiteRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.AggiornaPrimaNotaSuRegistroACespiteAsyncService;
import it.csi.siac.siacbilser.business.service.cespiti.AggiornaPrimaNotaSuRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.CollegaCespiteRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.InserisciPrimaNotaSuRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaCespiteDaPrimaNotaService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaMovimentoDettaglioRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RicercaSinteticaMovimentoEPRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RifiutaPrimaNotaCespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.RifiutaPrimaNotaSuRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.ScollegaCespiteRegistroACespiteService;
import it.csi.siac.siacbilser.business.service.cespiti.ValidaPrimaNotaCespiteService;
import it.csi.siac.siacbilser.business.service.primanota.OttieniDatiPrimeNoteFatturaConNotaCreditoService;
import it.csi.siac.siaccespser.frontend.webservice.CESPSvcDictionary;
import it.csi.siac.siaccespser.frontend.webservice.PrimaNotaCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaImportoCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaImportoCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.AggiornaPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespiteDaPrimaNota;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespiteDaPrimaNotaResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoDettaglioRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoEPRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoEPRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaSuRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaSuRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ScollegaCespiteRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespiteResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniDatiPrimeNoteFatturaConNotaCredito;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniDatiPrimeNoteFatturaConNotaCreditoResponse;

@WebService(serviceName = "PrimaNotaCespiteService", portName = "PrimaNotaCespiteServicePort", 
targetNamespace = CESPSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siaccespser.frontend.webservice.PrimaNotaCespiteService")
public class PrimaNotaCespiteServiceImpl implements PrimaNotaCespiteService {

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
	public ValidaPrimaNotaCespiteResponse validaPrimaNotaCespite(ValidaPrimaNotaCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, ValidaPrimaNotaCespiteService.class, parameters);
	}

	@Override
	public RifiutaPrimaNotaCespiteResponse rifiutaPrimaNotaCespite(RifiutaPrimaNotaCespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RifiutaPrimaNotaCespiteService.class, parameters);
	}

	@Override
	public RicercaCespiteDaPrimaNotaResponse ricercaCespiteDaPrimaNota(RicercaCespiteDaPrimaNota parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaCespiteDaPrimaNotaService.class, parameters);
	}

	@Override
	public InserisciPrimaNotaSuRegistroACespiteResponse inserisciPrimaNotaSuRegistroACespite(InserisciPrimaNotaSuRegistroACespite parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciPrimaNotaSuRegistroACespiteService.class, parameters);
	}

	@Override
	public RifiutaPrimaNotaSuRegistroACespiteResponse rifiutaPrimaNotaSuRegistroACespite(RifiutaPrimaNotaSuRegistroACespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RifiutaPrimaNotaSuRegistroACespiteService.class, parameters);
	}

	@Override
	public RicercaSinteticaMovimentoEPRegistroACespiteResponse ricercaSinteticaMovimentoEPRegistroACespite(RicercaSinteticaMovimentoEPRegistroACespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaMovimentoEPRegistroACespiteService.class, parameters);
	}

	@Override
	public CollegaCespiteRegistroACespiteResponse collegaCespitePRegistroACespite(CollegaCespiteRegistroACespite parameters) {
		 return BaseServiceExecutor.execute(appCtx, CollegaCespiteRegistroACespiteService.class, parameters);	
	}

	@Override
	public ScollegaCespiteRegistroACespiteResponse scollegaCespitePRegistroACespite(ScollegaCespiteRegistroACespite parameters) {
		return BaseServiceExecutor.execute(appCtx, ScollegaCespiteRegistroACespiteService.class, parameters);
	}

	@Override
	public AggiornaPrimaNotaSuRegistroACespiteResponse aggiornaPrimaNotaSuRegistroACespite(AggiornaPrimaNotaSuRegistroACespite parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaPrimaNotaSuRegistroACespiteService.class, parameters);
	}

	@Override
	public AsyncServiceResponse aggiornaPrimaNotaSuRegistroACespiteAsync(AsyncServiceRequestWrapper<AggiornaPrimaNotaSuRegistroACespite> parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaPrimaNotaSuRegistroACespiteAsyncService.class, parameters);
	}

	@Override
	public AggiornaImportoCespiteRegistroACespiteResponse aggiornaImportoCespiteRegistroACespite(AggiornaImportoCespiteRegistroACespite parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaImportoCespiteRegistroACespiteService.class, parameters);
	}

	@Override
	public RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse ricercaSinteticaMovimentoDettaglioRegistroACespite(RicercaSinteticaMovimentoDettaglioRegistroACespite parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaMovimentoDettaglioRegistroACespiteService.class, parameters);
	}

	@Override
	public OttieniDatiPrimeNoteFatturaConNotaCreditoResponse ottieniDatiPrimeNoteFatturaConNotaCredito(OttieniDatiPrimeNoteFatturaConNotaCredito parameters) {
		return BaseServiceExecutor.execute(appCtx, OttieniDatiPrimeNoteFatturaConNotaCreditoService.class, parameters);
	}
}



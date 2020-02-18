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
import it.csi.siac.siacbilser.business.service.vincolicapitolo.AggiornaVincoloCapitoloService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.AnnullaVincoloCapitoloService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.AssociaCapitoloAlVincoloService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.InserisceAnagraficaVincoloService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.RicercaDettaglioVincoloService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.RicercaVincoloService;
import it.csi.siac.siacbilser.business.service.vincolicapitolo.ScollegaCapitoloAlVincoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVincoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaVincoloCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaVincoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaVincoloCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AssociaCapitoloAlVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AssociaCapitoloAlVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceAnagraficaVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVincoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ScollegaCapitoloAlVincolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ScollegaCapitoloAlVincoloResponse;

/**
 * Implementazione del servizio VincoloCapitoloService.
 */
@WebService(serviceName = "VincoloCapitoloService", portName = "VincoloCapitoloServicePort", targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.VincoloCapitoloService")
public class VincoloCapitoloServiceImpl implements VincoloCapitoloService {

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
	public InserisceAnagraficaVincoloResponse inserisceAnagraficaVincolo(InserisceAnagraficaVincolo arg) {
		return  BaseServiceExecutor.execute(appCtx, InserisceAnagraficaVincoloService.class, arg);
	}

	@Override
	public RicercaVincoloResponse ricercaVincolo(RicercaVincolo arg) {
		return  BaseServiceExecutor.execute(appCtx, RicercaVincoloService.class, arg);
	}
	
	@Override
	public RicercaDettaglioVincoloResponse ricercaDettaglioVincolo(RicercaDettaglioVincolo arg) {
		return  BaseServiceExecutor.execute(appCtx, RicercaDettaglioVincoloService.class, arg);
	}

	@Override
	public AnnullaVincoloCapitoloResponse annullaVincoloCapitolo(AnnullaVincoloCapitolo arg) {
		return  BaseServiceExecutor.execute(appCtx, AnnullaVincoloCapitoloService.class, arg);
	}

	@Override
	public AggiornaVincoloCapitoloResponse aggiornaVincoloCapitolo(AggiornaVincoloCapitolo arg) {
		return  BaseServiceExecutor.execute(appCtx, AggiornaVincoloCapitoloService.class, arg);
	}

	@Override
	public AssociaCapitoloAlVincoloResponse associaCapitoloAlVincolo(AssociaCapitoloAlVincolo arg) {
		return  BaseServiceExecutor.execute(appCtx, AssociaCapitoloAlVincoloService.class, arg);
	}

	@Override
	public ScollegaCapitoloAlVincoloResponse scollegaCapitoloAlVincolo(ScollegaCapitoloAlVincolo arg) {
		return  BaseServiceExecutor.execute(appCtx, ScollegaCapitoloAlVincoloService.class, arg);
	}


}

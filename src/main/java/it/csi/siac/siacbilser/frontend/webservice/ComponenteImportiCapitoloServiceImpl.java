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
import it.csi.siac.siacbilser.business.service.componenteimpcap.AggiornaComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.componenteimpcap.AggiornaImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.componenteimpcap.AnnullaComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.componenteimpcap.InserisceComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.componenteimpcap.RicercaComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;

@WebService(serviceName = "ComponenteImportiCapitoloService", portName = "ComponenteImportiCapitoloServicePort", 
targetNamespace = BILSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.ComponenteImportiCapitoloService")
public class ComponenteImportiCapitoloServiceImpl implements ComponenteImportiCapitoloService {
	
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
	public InserisceComponenteImportiCapitoloResponse inserisceComponenteImportiCapitolo(InserisceComponenteImportiCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceComponenteImportiCapitoloService.class, parameters);
	}

	@Override
	public AggiornaComponenteImportiCapitoloResponse aggiornaComponenteImportiCapitolo(AggiornaComponenteImportiCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaComponenteImportiCapitoloService.class, parameters);
	}

	@Override
	public RicercaComponenteImportiCapitoloResponse ricercaComponenteImportiCapitolo(RicercaComponenteImportiCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaComponenteImportiCapitoloService.class, parameters);
	}

	@Override
	public AnnullaComponenteImportiCapitoloResponse annullaComponenteImportiCapitolo(AnnullaComponenteImportiCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaComponenteImportiCapitoloService.class, parameters);
	}

	@Override
	public AggiornaImportiCapitoloResponse aggiornaImportiCapitolo(AggiornaImportiCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaImportiCapitoloService.class, parameters);
	}
}

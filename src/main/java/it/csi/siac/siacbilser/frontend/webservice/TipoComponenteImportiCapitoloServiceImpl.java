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
import it.csi.siac.siacbilser.business.service.tipocomponenteimpcap.AggiornaTipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.tipocomponenteimpcap.AnnullaTipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.tipocomponenteimpcap.InserisceTipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.tipocomponenteimpcap.RicercaDettaglioTipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.tipocomponenteimpcap.RicercaSinteticaTipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.tipocomponenteimpcap.RicercaTipoComponenteImportiCapitoloImpegnabiliService;
import it.csi.siac.siacbilser.business.service.tipocomponenteimpcap.RicercaTipoComponenteImportiCapitoloPerCapitoloService;
import it.csi.siac.siacbilser.business.service.tipocomponenteimpcap.RicercaTipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.business.service.tipocomponenteimpcap.RicercaTipoComponenteImportiCapitoloTotaliService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloResponse;

@WebService(serviceName = "TipoComponenteImportiCapitoloService", portName = "TipoComponenteImportiCapitoloServicePort", 
targetNamespace = BILSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService")
public class TipoComponenteImportiCapitoloServiceImpl implements TipoComponenteImportiCapitoloService {
	
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
	public InserisceTipoComponenteImportiCapitoloResponse inserisceTipoComponenteImportiCapitolo(InserisceTipoComponenteImportiCapitolo request) {
		return BaseServiceExecutor.execute(appCtx, InserisceTipoComponenteImportiCapitoloService.class, request);
	}

	@Override
	public AggiornaTipoComponenteImportiCapitoloResponse aggiornaTipoComponenteImportiCapitolo(AggiornaTipoComponenteImportiCapitolo request) {
		return BaseServiceExecutor.execute(appCtx, AggiornaTipoComponenteImportiCapitoloService.class, request);
	}

	@Override
	public RicercaTipoComponenteImportiCapitoloResponse ricercaTipoComponenteImportiCapitolo(RicercaTipoComponenteImportiCapitolo request) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoComponenteImportiCapitoloService.class, request);
	}

	@Override
	public RicercaDettaglioTipoComponenteImportiCapitoloResponse ricercaDettaglioTipoComponenteImportiCapitolo(RicercaDettaglioTipoComponenteImportiCapitolo request) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioTipoComponenteImportiCapitoloService.class, request);
	}

	@Override
	public RicercaSinteticaTipoComponenteImportiCapitoloResponse ricercaSinteticaTipoComponenteImportiCapitolo(RicercaSinteticaTipoComponenteImportiCapitolo request) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaTipoComponenteImportiCapitoloService.class, request);
	}

	@Override
	public RicercaTipoComponenteImportiCapitoloPerCapitoloResponse ricercaTipoComponenteImportiCapitoloPerCapitolo(RicercaTipoComponenteImportiCapitoloPerCapitolo request) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoComponenteImportiCapitoloPerCapitoloService.class, request);
 	}

	@Override
	public AnnullaTipoComponenteImportiCapitoloResponse annullaTipoComponenteImportiCapitolo(AnnullaTipoComponenteImportiCapitolo request) {
		return BaseServiceExecutor.execute(appCtx, AnnullaTipoComponenteImportiCapitoloService.class, request);
	}
	//SIAC-7349
	@Override
	public RicercaTipoComponenteImportiCapitoloResponse ricercaTipoComponenteImportiCapitoloTotali(RicercaTipoComponenteImportiCapitolo request) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoComponenteImportiCapitoloTotaliService.class, request);
	}
	
	
	//SIAC-7349
	@Override
	public RicercaTipoComponenteImportiCapitoloResponse ricercaTipoComponenteImportiCapitoloImpegnabili(RicercaTipoComponenteImportiCapitolo request) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoComponenteImportiCapitoloImpegnabiliService.class, request);
	}
	

}

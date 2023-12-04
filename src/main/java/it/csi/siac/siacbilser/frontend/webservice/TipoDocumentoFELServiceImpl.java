/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.tipodocumentofel.AggiornaTipoDocumentoFELService;
import it.csi.siac.siacbilser.business.service.tipodocumentofel.AnnullaTipoDocumentoFELService;
import it.csi.siac.siacbilser.business.service.tipodocumentofel.InserisceTipoDocumentoFELService;
import it.csi.siac.siacbilser.business.service.tipodocumentofel.RicercaDettaglioTipoDocumentoFELService;
import it.csi.siac.siacbilser.business.service.tipodocumentofel.RicercaPuntualeTipoDocumentoFELService;
import it.csi.siac.siacbilser.business.service.tipodocumentofel.RicercaSinteticaTipoDocumentoFELService;
import it.csi.siac.siacbilser.business.service.tipodocumentofel.RicercaTipoDocFELService;
import it.csi.siac.siacbilser.business.service.tipodocumentofel.RicercaTipoDocumentoFELService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoDocumentoFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocFELResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFELPerTipoContabilia;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFELPerTipoContabiliaResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFELResponse;



/**
 * The Class TipoDocumentoFELServiceImpl.
 */
@WebService(serviceName = "TipoDocumentoFELService", portName = "TipoDocumentoFELServicePort", 
targetNamespace = BILSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.TipoDocumentoFELService")
public class TipoDocumentoFELServiceImpl implements TipoDocumentoFELService {
	
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
	public RicercaTipoDocumentoFELResponse ricercaTipoDocumentoFEL(RicercaTipoDocumentoFEL parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoDocumentoFELService.class, parameters);
	}

	@Override
	public RicercaTipoDocFELResponse ricercaTipoDocFEL(RicercaTipoDocFEL parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoDocFELService.class, parameters);
	}

 

	@Override
	public InserisceTipoDocumentoFELResponse inserisceTipoDocumentoFEL(InserisceTipoDocumentoFEL request) {
		return BaseServiceExecutor.execute(appCtx, InserisceTipoDocumentoFELService.class, request);
	}

	@Override
	public AggiornaTipoDocumentoFELResponse aggiornaTipoDocumentoFEL(AggiornaTipoDocumentoFEL request) {
		return BaseServiceExecutor.execute(appCtx, AggiornaTipoDocumentoFELService.class, request);
	}


	@Override
	public RicercaTipoDocumentoFELResponse ricercaPuntualeTipoDocumentoFEL(RicercaTipoDocumentoFEL request) {
		return   BaseServiceExecutor.execute(appCtx, RicercaPuntualeTipoDocumentoFELService.class, request);
	}

	
	@Override
	public RicercaDettaglioTipoDocumentoFELResponse ricercaDettaglioTipoDocumentoFEL(RicercaDettaglioTipoDocumentoFEL request) {
		return  BaseServiceExecutor.execute(appCtx, RicercaDettaglioTipoDocumentoFELService.class, request);
	}

	
	@Override                                       
	public RicercaSinteticaTipoDocumentoFELResponse ricercaSinteticaTipoDocumentoFEL(RicercaSinteticaTipoDocumentoFEL request) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaTipoDocumentoFELService.class, request);
	}

	@Override
	public RicercaTipoDocumentoFELPerTipoContabiliaResponse ricercaTipoDocumentoFELPerTipoContabilia(RicercaTipoDocumentoFELPerTipoContabilia request) {
		return null;//BaseServiceExecutor.execute(appCtx, RicercaTipoDocumentoFELPerCapitoloService.class, request);
 	}

	@Override
	public AnnullaTipoDocumentoFELResponse annullaTipoDocumentoFEL(AnnullaTipoDocumentoFEL request) {
		return BaseServiceExecutor.execute(appCtx, AnnullaTipoDocumentoFELService.class, request);
	}

 

}

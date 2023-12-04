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
import it.csi.siac.siacbilser.business.service.documento.AggiornaTipoOnereService;
import it.csi.siac.siacbilser.business.service.documento.DettaglioStoricoTipoOnereService;
import it.csi.siac.siacbilser.business.service.documento.InserisceTipoOnereService;
import it.csi.siac.siacbilser.business.service.documento.RicercaDettaglioTipoOnereService;
import it.csi.siac.siacbilser.business.service.documento.RicercaSinteticaTipoOnereService;
import it.csi.siac.siacbilser.business.service.documento.RicercaTipoOnereService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.TipoOnereService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaTipoOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoOnereResponse;


/**
 * The Class TipoOnereServiceImpl.
 */
@WebService(serviceName = "TipoOnereService", portName = "TipoOnereServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.TipoOnereService")
public class TipoOnereServiceImpl implements TipoOnereService {
	
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
	public InserisceTipoOnereResponse inserisceTipoOnere(InserisceTipoOnere parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceTipoOnereService.class, parameters);
	}
	
	@Override
	public AggiornaTipoOnereResponse aggiornaTipoOnere(AggiornaTipoOnere parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaTipoOnereService.class, parameters);
	}
	
	@Override
	public RicercaTipoOnereResponse ricercaTipoOnere(RicercaTipoOnere parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoOnereService.class, parameters);
	}
	
	@Override
	public RicercaSinteticaTipoOnereResponse ricercaSinteticaTipoOnere(RicercaSinteticaTipoOnere parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaTipoOnereService.class, parameters);
	}
	
	@Override
	public RicercaDettaglioTipoOnereResponse ricercaDettaglioTipoOnere(RicercaDettaglioTipoOnere parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioTipoOnereService.class, parameters);
	}

	@Override
	public DettaglioStoricoTipoOnereResponse dettaglioStoricoTipoOnere(DettaglioStoricoTipoOnere parameters) {
		return BaseServiceExecutor.execute(appCtx, DettaglioStoricoTipoOnereService.class, parameters);
	}

}

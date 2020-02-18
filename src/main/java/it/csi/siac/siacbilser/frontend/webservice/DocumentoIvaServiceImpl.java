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
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaAliquotaIvaService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaAttivitaIvaLegateAGruppoAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaAttivitaIvaService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaTipoRegistrazioneIvaService;
import it.csi.siac.siacbilser.business.service.documentoiva.RicercaValutaService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAliquotaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaLegateAGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaLegateAGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoRegistrazioneIvaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValuta;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaValutaResponse;

/**
 * The Class DocumentoIvaServiceImpl.
 */
@WebService(serviceName = "DocumentoIvaService", portName = "DocumentoIvaServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService")
public class DocumentoIvaServiceImpl implements DocumentoIvaService {
	
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
	public RicercaTipoRegistrazioneIvaResponse ricercaTipoRegistrazioneIva(RicercaTipoRegistrazioneIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaTipoRegistrazioneIvaService.class, parameters);
	}

	@Override
	public RicercaAttivitaIvaResponse ricercaAttivitaIva(RicercaAttivitaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaAttivitaIvaService.class, parameters);
	}
	
	@Override
	public RicercaAttivitaIvaLegateAGruppoAttivitaIvaResponse ricercaAttivitaIvaLegateAGruppoAttivitaIva(RicercaAttivitaIvaLegateAGruppoAttivitaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaAttivitaIvaLegateAGruppoAttivitaIvaService.class, parameters);
	}

	@Override
	public RicercaAliquotaIvaResponse ricercaAliquotaIva(RicercaAliquotaIva parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaAliquotaIvaService.class, parameters);
	}

	@Override
	public RicercaValutaResponse ricercaValuta(RicercaValuta parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaValutaService.class, parameters);
	}
	
}

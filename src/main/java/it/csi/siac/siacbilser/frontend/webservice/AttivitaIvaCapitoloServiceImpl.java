/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.attivitaivacapitolo.EliminaRelazioneAttivitaIvaCapitoloService;
import it.csi.siac.siacbilser.business.service.attivitaivacapitolo.InserisceRelazioneAttivitaIvaCapitoloService;
import it.csi.siac.siacbilser.business.service.attivitaivacapitolo.RicercaRelazioneAttivitaIvaCapitoloService;
import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacfin2ser.frontend.webservice.AttivitaIvaCapitoloService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRelazioneAttivitaIvaCapitoloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRelazioneAttivitaIvaCapitoloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitoloResponse;

/**
 * The Class AttivitaIvaCapitoloServiceImpl.
 */
@WebService(serviceName = "AttivitaIvaCapitoloService", portName = "AttivitaIvaCapitoloServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.AttivitaIvaCapitoloService")
public class AttivitaIvaCapitoloServiceImpl implements AttivitaIvaCapitoloService {
	
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

	/* (non-Javadoc)
	 * @see it.csi.siac.siacfin2ser.frontend.webservice.AttivitaIvaCapitoloService#ricercaRelazioneAttivitaIvaCapitolo(it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitolo)
	 */
	@Override
	@WebMethod
	@WebResult
	public RicercaRelazioneAttivitaIvaCapitoloResponse ricercaRelazioneAttivitaIvaCapitolo(@WebParam RicercaRelazioneAttivitaIvaCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaRelazioneAttivitaIvaCapitoloService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacfin2ser.frontend.webservice.AttivitaIvaCapitoloService#inserisceRelazioneAttivitaIvaCapitolo(it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRelazioneAttivitaIvaCapitolo)
	 */
	@Override
	@WebMethod
	@WebResult
	public InserisceRelazioneAttivitaIvaCapitoloResponse inserisceRelazioneAttivitaIvaCapitolo(@WebParam InserisceRelazioneAttivitaIvaCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceRelazioneAttivitaIvaCapitoloService.class, parameters);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siacfin2ser.frontend.webservice.AttivitaIvaCapitoloService#eliminaRelazioneAttivitaIvaCapitolo(it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRelazioneAttivitaIvaCapitolo)
	 */
	@Override
	@WebMethod
	@WebResult
	public EliminaRelazioneAttivitaIvaCapitoloResponse eliminaRelazioneAttivitaIvaCapitolo(@WebParam EliminaRelazioneAttivitaIvaCapitolo parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaRelazioneAttivitaIvaCapitoloService.class, parameters);
	}

}

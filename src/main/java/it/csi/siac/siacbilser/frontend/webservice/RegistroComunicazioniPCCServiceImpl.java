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
import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.AggiornaRegistroComunicazioniPCCService;
import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.EliminaRegistroComunicazioniPCCService;
import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.InserisciRegistroComunicazioniPCCService;
import it.csi.siac.siacbilser.business.service.registrocomunicazionipcc.RicercaRegistriComunicazioniPCCSubdocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.RegistroComunicazioniPCCService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistriComunicazioniPCCSubdocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistriComunicazioniPCCSubdocumentoResponse;

/**
 * Implementazione del web service RegistroComunicazioniPCCService.
 *
 * @author Marchino Alessandro
 */
@WebService(serviceName = "RegistroComunicazioniPCCService", portName = "RegistroComunicazioniPCCService", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.RegistroComunicazioniPCCService")
public class RegistroComunicazioniPCCServiceImpl implements RegistroComunicazioniPCCService {
	
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
	public RicercaRegistriComunicazioniPCCSubdocumentoResponse ricercaRegistriComunicazioniPCCSubdocumento(RicercaRegistriComunicazioniPCCSubdocumento parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaRegistriComunicazioniPCCSubdocumentoService.class, parameters);
	}

	@Override
	public InserisciRegistroComunicazioniPCCResponse inserisciRegistroComunicazioniPCC(InserisciRegistroComunicazioniPCC parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciRegistroComunicazioniPCCService.class, parameters);
	}

	@Override
	public AggiornaRegistroComunicazioniPCCResponse aggiornaRegistroComunicazioniPCC(AggiornaRegistroComunicazioniPCC parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaRegistroComunicazioniPCCService.class, parameters);
	}

	@Override
	public EliminaRegistroComunicazioniPCCResponse eliminaRegistroComunicazioniPCC(EliminaRegistroComunicazioniPCC parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaRegistroComunicazioniPCCService.class, parameters);
	}

}

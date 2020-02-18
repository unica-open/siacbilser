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
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaOrdineService;
import it.csi.siac.siacbilser.business.service.documentospesa.EliminaOrdineService;
import it.csi.siac.siacbilser.business.service.documentospesa.InserisceOrdineService;
import it.csi.siac.siacbilser.business.service.documentospesa.RicercaOrdiniDocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.FIN2SvcDictionary;
import it.csi.siac.siacfin2ser.frontend.webservice.OrdineService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaOrdineResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaOrdineResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceOrdine;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceOrdineResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaOrdiniDocumentoResponse;


// TODO: Auto-generated Javadoc
/**
 * The Class OrdineServiceImpl.
 */
@WebService(serviceName = "OrdineService", portName = "OrdineServicePort", 
targetNamespace = FIN2SvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfin2ser.frontend.webservice.OrdineService")
public class OrdineServiceImpl implements OrdineService {
	
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
	public InserisceOrdineResponse inserisceOrdine(InserisceOrdine parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisceOrdineService.class, parameters);
	}

	@Override
	public AggiornaOrdineResponse aggiornaOrdine(AggiornaOrdine parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaOrdineService.class, parameters);
	}
	
	@Override
	public EliminaOrdineResponse eliminaOrdine(EliminaOrdine parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaOrdineService.class, parameters);
	}

	@Override
	public RicercaOrdiniDocumentoResponse ricercaOrdiniDocumento(RicercaOrdiniDocumento parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaOrdiniDocumentoService.class, parameters);
	}

	

}

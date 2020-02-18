/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.frontend.webservice.custom.stilo;
  

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.custom.stilo.business.service.ElaboraAttiAmministrativiAsyncStiloService;
import it.csi.siac.custom.stilo.business.service.ElaboraAttiAmministrativiStiloService;
import it.csi.siac.custom.stilo.integ.business.service.RicercaMovimentoGestioneStiloService;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.stilo.RicercaMovimentoGestioneStilo;
import it.csi.siac.siacintegser.frontend.webservice.msg.custom.stilo.RicercaMovimentoGestioneStiloResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraAttiAmministrativi;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraAttiAmministrativiAsyncResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraAttiAmministrativiResponse;

/**
 * The Class RicercaServiceImpl.
 */
@WebService(serviceName = "StiloService",
	portName = "StiloServicePort",
	targetNamespace = StiloSvcDictionary.NAMESPACE,
	endpointInterface = "it.csi.siac.siacintegser.frontend.webservice.custom.stilo.StiloService")
public class StiloServiceImpl implements StiloService {
	
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
	public RicercaMovimentoGestioneStiloResponse ricercaMovimentoGestione(RicercaMovimentoGestioneStilo request) {
		return appCtx.getBean(RicercaMovimentoGestioneStiloService.class).executeService(request);
	}
	
	
	@Override
	@WebMethod
	public @WebResult
	ElaboraAttiAmministrativiResponse elaboraAttiAmministrativi(@WebParam ElaboraAttiAmministrativi request)
	{
		return appCtx.getBean(ElaboraAttiAmministrativiStiloService.class).executeService(request);
	}
 
	@Override
	@WebMethod
	public @WebResult
	ElaboraAttiAmministrativiAsyncResponse elaboraAttiAmministrativiAsync(@WebParam ElaboraAttiAmministrativi request)
	{
		return appCtx.getBean(ElaboraAttiAmministrativiAsyncStiloService.class).executeService(request);
	}

	
	
}

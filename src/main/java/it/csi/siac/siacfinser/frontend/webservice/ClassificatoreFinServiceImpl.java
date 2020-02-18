/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.frontend.webservice;


import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacfinser.business.service.classificatorefin.LeggiClassificatoriGenericiByTipoMovimentoGestService;
import it.csi.siac.siacfinser.business.service.classificatorefin.LeggiClassificatoriGenericiByTipoOrdinativoGestService;
import it.csi.siac.siacfinser.business.service.classificatorefin.LeggiClassificatoriGerarchiciByIdPadreService;
import it.csi.siac.siacfinser.business.service.classificatorefin.LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGestService;
import it.csi.siac.siacfinser.business.service.classificatorefin.LeggiTreeContoEconomicoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoMovimentoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoMovimentoGestResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoOrdinativoGestResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGerarchiciByIdPadre;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGerarchiciByIdPadreResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGest;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGestResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiTreeContoEconomico;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiTreeContoEconomicoResponse;

/**
 * Implementazione del servizio ClassificatoreFinService, i metodi esposti sono
 * propri del modulo FIN
 * 
 * @author rmontuori
 * @version $Id: $
 */
@WebService(serviceName = "ClassificatoreFinService", portName = "ClassificatoreFinServicePort", targetNamespace = FINSvcDictionary.NAMESPACE, endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.ClassificatoreFinService")
public class ClassificatoreFinServiceImpl implements ClassificatoreFinService {

	@Autowired
	private ApplicationContext appCtx;
	
	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	@WebMethod
	public @WebResult
	LeggiClassificatoriGenericiByTipoMovimentoGestResponse leggiClassificatoriGenericiByTipoMovimentoGest(
			@WebParam LeggiClassificatoriGenericiByTipoMovimentoGest parameters) {
		return appCtx.getBean(LeggiClassificatoriGenericiByTipoMovimentoGestService.class).executeService(parameters);
	}

	@Override
	@WebMethod
	public @WebResult
	LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGestResponse leggiClassificatoriGerarchiciILivelloByTipoMovimentoGest(
			@WebParam LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGest parameters) {
		return appCtx.getBean(LeggiClassificatoriGerarchiciILivelloByTipoMovimentoGestService.class).executeService(parameters);
	}
	
	@Override
	@WebMethod
	public @WebResult
	LeggiTreeContoEconomicoResponse leggiTreeContoEconomico(
			@WebParam LeggiTreeContoEconomico parameters) {
		return appCtx.getBean(LeggiTreeContoEconomicoService.class).executeService(parameters);
	}

	@Override
	@WebMethod
	public @WebResult
	LeggiClassificatoriGerarchiciByIdPadreResponse leggiClassificatoriGerarchiciByIdPadre(@WebParam LeggiClassificatoriGerarchiciByIdPadre parameters) {
		return appCtx.getBean(LeggiClassificatoriGerarchiciByIdPadreService.class).executeService(parameters);
	}

	@Override
	@WebMethod
	public @WebResult
	LeggiClassificatoriGenericiByTipoOrdinativoGestResponse leggiClassificatoriGenericiByTipoOrdinativoGest(
			@WebParam LeggiClassificatoriGenericiByTipoOrdinativoGest parameters) {
		return appCtx.getBean(LeggiClassificatoriGenericiByTipoOrdinativoGestService.class).executeService(parameters);
	}
}

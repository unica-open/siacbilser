/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.frontend.webservice;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacfinser.business.service.oil.AccreditoTipoOilIsPagoPAService;
import it.csi.siac.siacfinser.business.service.oil.CountOrdinativiMifService;
import it.csi.siac.siacfinser.business.service.oil.CountOrdinativiMifSiopePlusService;
import it.csi.siac.siacfinser.business.service.oil.GeneraXmlOrdinativiMifService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AccreditoTipoOilIsPagoPA;
import it.csi.siac.siacfinser.frontend.webservice.msg.AccreditoTipoOilIsPagoPAResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.CountOrdinativiMif;
import it.csi.siac.siacfinser.frontend.webservice.msg.CountOrdinativiMifResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.CountOrdinativiMifSiopePlusResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.GeneraXmlOrdinativiMif;
import it.csi.siac.siacfinser.frontend.webservice.msg.GeneraXmlOrdinativiMifResponse;

@WebService(serviceName = "OilService", 
portName = "OilServicePort", targetNamespace = FINSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.OilService")
public class OilServiceImpl implements OilService
{

	@Autowired
	private ApplicationContext appCtx;

	@PostConstruct
	public void init()
	{
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public GeneraXmlOrdinativiMifResponse generaXmlOrdinativiMif(GeneraXmlOrdinativiMif req)
	{
		return BaseServiceExecutor.execute(appCtx, GeneraXmlOrdinativiMifService.class, req);
	}

	@Override
	public CountOrdinativiMifResponse countOrdinativiMif(CountOrdinativiMif req)
	{
		return BaseServiceExecutor.execute(appCtx, CountOrdinativiMifService.class, req);
	}

	@Override
	public CountOrdinativiMifSiopePlusResponse countOrdinativiMifSiopePlus(CountOrdinativiMif req)
	{
		return BaseServiceExecutor.execute(appCtx, CountOrdinativiMifSiopePlusService.class, req);
	}

	@Override
	public AccreditoTipoOilIsPagoPAResponse accreditoTipoOilIsPagoPA(AccreditoTipoOilIsPagoPA req) {
		return BaseServiceExecutor.execute(appCtx, AccreditoTipoOilIsPagoPAService.class, req);
	}
}

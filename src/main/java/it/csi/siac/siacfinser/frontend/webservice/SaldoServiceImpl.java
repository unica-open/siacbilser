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

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacfinser.business.service.saldo.AggiornaAddebitiContoCorrenteService;
import it.csi.siac.siacfinser.business.service.saldo.AggiornaSaldoInizialeContoCorrenteService;
import it.csi.siac.siacfinser.business.service.saldo.CalcolaSaldoCassaDataService;
import it.csi.siac.siacfinser.business.service.saldo.LeggiVociContoCorrenteService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAddebitiContoCorrente;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaAddebitiContoCorrenteResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSaldoInizialeContoCorrente;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSaldoInizialeContoCorrenteResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaSaldoCassaData;
import it.csi.siac.siacfinser.frontend.webservice.msg.CalcolaSaldoCassaDataResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiVociContoCorrente;
import it.csi.siac.siacfinser.frontend.webservice.msg.LeggiVociContoCorrenteResponse;

@WebService(serviceName = "SaldoService", 
portName = "SaldoServicePort", 
targetNamespace = FINSvcDictionary.NAMESPACE, 
endpointInterface = "it.csi.siac.siacfinser.frontend.webservice.SaldoService")
public class SaldoServiceImpl extends AbstractService implements SaldoService
{

	@PostConstruct
	public void init()
	{
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	@WebMethod
	public @WebResult LeggiVociContoCorrenteResponse leggiVociContoCorrente(@WebParam LeggiVociContoCorrente request)
	{
		return new ServiceExecutor<LeggiVociContoCorrenteResponse, LeggiVociContoCorrente>("leggiVociContoCorrente")
		{
			@Override
			LeggiVociContoCorrenteResponse executeService(LeggiVociContoCorrente request)
			{
				return appCtx.getBean(LeggiVociContoCorrenteService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public @WebResult AggiornaSaldoInizialeContoCorrenteResponse aggiornaSaldoInizialeContoCorrente(
			@WebParam AggiornaSaldoInizialeContoCorrente request)
	{
		return new ServiceExecutor<AggiornaSaldoInizialeContoCorrenteResponse, AggiornaSaldoInizialeContoCorrente>(
				"aggiornaSaldoInizialeContoCorrente")
		{
			@Override
			AggiornaSaldoInizialeContoCorrenteResponse executeService(AggiornaSaldoInizialeContoCorrente request)
			{
				return appCtx.getBean(AggiornaSaldoInizialeContoCorrenteService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public @WebResult AggiornaAddebitiContoCorrenteResponse aggiornaAddebitiContoCorrente(
			@WebParam AggiornaAddebitiContoCorrente request)
	{
		return new ServiceExecutor<AggiornaAddebitiContoCorrenteResponse, AggiornaAddebitiContoCorrente>(
				"aggiornaAddebitiContoCorrente")
		{
			@Override
			AggiornaAddebitiContoCorrenteResponse executeService(AggiornaAddebitiContoCorrente request)
			{
				return appCtx.getBean(AggiornaAddebitiContoCorrenteService.class).executeService(request);
			}
		}.execute(request);
	}

	@Override
	@WebMethod
	public @WebResult CalcolaSaldoCassaDataResponse calcolaSaldoCassaData(
			@WebParam CalcolaSaldoCassaData request)
	{
		return new ServiceExecutor<CalcolaSaldoCassaDataResponse, CalcolaSaldoCassaData>(
				"calcolaSaldoCassaData")
		{
			@Override
			CalcolaSaldoCassaDataResponse executeService(CalcolaSaldoCassaData request)
			{
				return appCtx.getBean(CalcolaSaldoCassaDataService.class).executeService(request);
			}
		}.execute(request);
	}

}

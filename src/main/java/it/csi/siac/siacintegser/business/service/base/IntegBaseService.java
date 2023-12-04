/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Reductor;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccommonser.util.dozer.DozerUtil;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.frontend.webservice.exception.ServiceException;
import it.csi.siac.siaccorser.frontend.webservice.util.ServiceUtils;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.helper.CoreServiceHelper;
import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.base.BaseRequest;
import it.csi.siac.siacintegser.frontend.webservice.msg.base.BaseResponse;
import it.csi.siac.siacintegser.model.base.Ambito;
import it.csi.siac.siacintegser.model.base.Errore;
import it.csi.siac.siacintegser.model.base.Esito;
import it.csi.siac.siacintegser.model.base.Messaggio;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

public abstract class IntegBaseService<IREQ extends BaseRequest, IRES extends BaseResponse>
{
	protected LogSrvUtil log = new LogSrvUtil(this.getClass());

	@Autowired protected CoreServiceHelper coreServiceHelper;
	@Autowired protected DozerUtil dozerUtil;
	@Autowired protected ApplicationContext appCtx;

	protected Richiedente richiedente;
	protected Ente ente;

	private List<Errore> erroriIres = new ArrayList<Errore>();
	private List<Messaggio> messaggiIres = new ArrayList<Messaggio>();

	public IRES executeService(IREQ ireq)
	{
		String methodName = "executeService";

		IRES ires = null;

		try
		{
			logServiceRequest(ireq);
			
			internalCheckServiceParameters(ireq);

			if (erroriIres.isEmpty())
			{
				init(ireq);
				ires = execute(ireq);
			}
		}
		catch (ServiceParamError spe)
		{
			log.error(methodName, "Check parametri del servizio terminato con errori.");
			addErrore(spe.getErrore());
		}
		catch (ServiceException se)
		{
			log.error(methodName, "Errore durante la chiamata al servizio di business.", se);
			addErrori(se.getErrori());
		}
		catch (RuntimeException re)
		{
			log.error(methodName, "Errore di runtime nell'esecuzione del servizio.", re);
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore(re.getMessage()));
		}
		finally
		{
			if (ires == null)
				ires = instantiateNewIRes();

			ires.addErrori(erroriIres);
			ires.addMessaggi(messaggiIres);

			if (ires.hasErrori())
				ires.setEsito(Esito.FALLIMENTO);
		}

		logServiceResponse(ires);

		return ires;
	}

	protected abstract IRES execute(IREQ ireq);

	protected void init(IREQ ireq)
	{
		richiedente = coreServiceHelper.findRichiedente(ireq.getCodiceFruitore(), ireq.getCodiceEnte());
		ente = richiedente.getAccount().getEnte();
	}

	protected <REQ extends ServiceRequest> REQ map(IREQ ireq, Class<REQ> reqClass, IntegMapId mapId)
	{
		REQ req = dozerUtil.map(ireq, reqClass, mapId);

		req.setRichiedente(richiedente);

		return req;
	}

	protected <RES extends ServiceResponse> IRES map(RES res, Class<IRES> iresClass, IntegMapId mapId)
	{
		checkServiceResponse(res);

		IRES ires = dozerUtil.map(res, iresClass, mapId);
		
		for (it.csi.siac.siaccorser.model.Errore e : res.getErrori())     
			addErrore(e);
		
		return ires;
	}

	protected Class<IRES> getIntegResponseClass()
	{
		return CoreUtil.getGenericTypeClass(this.getClass(), IntegBaseService.class, 1);
	}

	protected IRES instantiateNewIRes()
	{
		return CoreUtil.instantiateNewGenericType(this.getClass(), IntegBaseService.class, 1);
	}

	protected <RES extends ServiceResponse> void checkServiceResponse(RES res)
	{
		ServiceUtils.checkServiceResponse(res);
	}

	protected void logServiceRequest(IREQ ireq)
	{
		log.logXmlTypeObject(ireq, "Service Request");
	}

	protected void logServiceResponse(IRES ires)
	{
		log.logXmlTypeObject(ires, "Service Response");
	}

	private void internalCheckServiceParameters(IREQ ireq) throws ServiceParamError
	{
		checkServiceBaseParameters(ireq);

		checkServiceParameters(ireq);
	}

	protected void checkServiceBaseParameters(IREQ ireq) throws ServiceParamError
	{
		assertParamNotNull(ireq, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("request"));
		assertParamNotNull(ireq.getCodiceFruitore(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice fruitore"));
		assertParamNotNull(ireq.getCodiceEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice ente"));
	}

	protected void checkServiceParameters(IREQ ireq) throws ServiceParamError
	{
	}

	protected void checkParamNotNull(Object obj, it.csi.siac.siaccorser.model.Errore errore) throws ServiceParamError
	{
		checkParamCondition(obj != null, errore);
	}

	protected void assertParamNotNull(Object obj, it.csi.siac.siaccorser.model.Errore errore) throws ServiceParamError
	{
		assertParamCondition(obj != null, errore);
	}

	protected void assertParamCondition(boolean condition, it.csi.siac.siaccorser.model.Errore errore)
			throws ServiceParamError
	{
		if (!condition)
			throw new ServiceParamError(errore);
	}

	protected void checkParamCondition(boolean condition, it.csi.siac.siaccorser.model.Errore errore)
			throws ServiceParamError
	{
		if (!condition)
			addErrore(errore);
	}
	
	private void addErrori(List<it.csi.siac.siaccorser.model.Errore> erroriRes)
	{
		for (it.csi.siac.siaccorser.model.Errore e : erroriRes)
			addErrore(e);
	}

	protected void addErrore(it.csi.siac.siaccorser.model.Errore errore)
	{
		if (errore != null)
			erroriIres.add(mapErrore(errore));
	}

	private Errore mapErrore(it.csi.siac.siaccorser.model.Errore e)
	{
		return new Errore(translateCodice(e.getCodice()), e.getDescrizione());
	}

	protected void addMessaggio(MessaggioInteg messaggioInteg, Object... args)
	{
		Messaggio messaggio = messaggioInteg.getMessaggio(args);

		addMessaggio(messaggio.getCodice(), messaggio.getDescrizione());
	}

	protected void addMessaggio(it.csi.siac.siaccorser.model.Messaggio messaggio)
	{
		addMessaggio(messaggio.getCodice(), messaggio.getDescrizione());
	}

	protected void addMessaggio(String codice, String descrizione)
	{
		messaggiIres.add(new Messaggio(translateCodice(codice), descrizione));
	}

	private String translateCodice(String codice)
	{
		String[] tmp = codice.split("_");
		
		return String.format("%s-%s-%s",
				Ambito.byLabel(StringUtils.defaultString(StringUtils.substringBetween(this.getClass().getCanonicalName(),
						"it.csi.siac.siacintegser.business.service.", "."))),
				tmp.length < 3 ? PrefissoCodiceMessaggio.X.getCodice() : PrefissoCodiceMessaggio.getCodice(tmp[0]), 
				tmp.length < 3 ? codice : tmp[2]
		);
	}
}

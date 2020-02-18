/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import it.csi.siac.siaccommon.util.CoreUtils;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccommonser.util.dozer.DozerUtil;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetRichiedente;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetRichiedenteResponse;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.base.exception.InvokeBusinessServiceException;
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
	protected LogUtil log = new LogUtil(this.getClass());

	@Autowired
	protected DozerUtil dozerUtil;

	@Autowired
	protected ApplicationContext appCtx;

	@Autowired
	protected CoreService coreService;

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
		catch (ServiceParamError e)
		{
			log.error(methodName, "Check parametri del servizio terminato con errori.");
			addErrore(e.getErrore());
		}
		catch (InvokeBusinessServiceException ibse)
		{
			log.error(methodName, "Errore durante la chiamata al servizio di business.", ibse);
			addErrori(ibse.getErrori());
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
		GetRichiedente getRichiedente = new GetRichiedente();
		getRichiedente.setCodiceAccount(String.format("%s-%s", ireq.getCodiceFruitore(), ireq.getCodiceEnte()));

		GetRichiedenteResponse getrRichiedenteResponse = coreService.getRichiedente(getRichiedente);

		checkBusinessServiceResponse(getrRichiedenteResponse);		
		
		richiedente = getrRichiedenteResponse.getRichiedente();
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
		checkBusinessServiceResponse(res);

		IRES ires = dozerUtil.map(res, iresClass, mapId);
		
		for (it.csi.siac.siaccorser.model.Errore e : res.getErrori())     
			addErrore(e);
		
		return ires;
	}

	protected Class<IRES> getIntegResponseClass()
	{
		return CoreUtils.getGenericTypeClass(this.getClass(), IntegBaseService.class, 1);
	}

	protected IRES instantiateNewIRes()
	{
		return CoreUtils.instantiateNewGenericType(this.getClass(), IntegBaseService.class, 1);
	}

	protected <RES extends ServiceResponse> void checkBusinessServiceResponse(RES res)
	{
		if (res == null)
			throw new NullPointerException("La response del servizio di business è null");

		if (res.isFallimento())
			throw new InvokeBusinessServiceException(res.getErrori());

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
		assertNotNull(ireq, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("request"));
		assertNotNull(ireq.getCodiceFruitore(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice fruitore"));
		assertNotNull(ireq.getCodiceEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice ente"));
	}

	protected void checkServiceParameters(IREQ ireq) throws ServiceParamError
	{
	}

	protected void checkNotNull(Object obj, it.csi.siac.siaccorser.model.Errore errore) throws ServiceParamError
	{
		checkCondition(obj != null, errore);
	}

	protected void assertNotNull(Object obj, it.csi.siac.siaccorser.model.Errore errore) throws ServiceParamError
	{
		assertCondition(obj != null, errore);
	}

	protected void assertCondition(boolean condition, it.csi.siac.siaccorser.model.Errore errore)
			throws ServiceParamError
	{
		if (!condition)
			throw new ServiceParamError(errore);
	}

	protected void checkCondition(boolean condition, it.csi.siac.siaccorser.model.Errore errore)
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

		String labCod;
		String txtCod;

		if (tmp.length < 3)
		{
			labCod = "X";
			txtCod = codice;
		}
		else
		{
			labCod = PrefissoCodiceMessaggio.codiceByLabel(tmp[0]);
			txtCod = tmp[2];
		}

		return String.format("%s-%s-%s", 
				Ambito.byLabel(StringUtils.substringBetween(this.getClass().getCanonicalName(),
						"it.csi.siac.siacintegser.business.service.", ".")), 
				labCod, 
				txtCod);
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.frontend.webservice.documenti;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacintegser.business.service.documenti.ElaboraAttiAmministrativiAsyncService;
import it.csi.siac.siacintegser.business.service.documenti.ElaboraAttiAmministrativiService;
import it.csi.siac.siacintegser.business.service.documenti.ElaboraDocumentoGenericoAsyncService;
import it.csi.siac.siacintegser.business.service.documenti.ElaboraDocumentoGenericoService;
import it.csi.siac.siacintegser.business.service.documenti.LeggiStatoElaborazioneDocumentoService;
import it.csi.siac.siacintegser.business.service.test.TestService;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraAttiAmministrativi;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraAttiAmministrativiAsyncResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraAttiAmministrativiResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumento;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumentoAsyncResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.ElaboraDocumentoResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.LeggiStatoElaborazioneDocumento;
import it.csi.siac.siacintegser.frontend.webservice.msg.documenti.LeggiStatoElaborazioneDocumentoResponse;
import it.csi.siac.siacintegser.frontend.webservice.msg.test.Test;
import it.csi.siac.siacintegser.frontend.webservice.msg.test.TestResponse;

@WebService(serviceName = "DocumentiService", 
	portName = "DocumentiServicePort", 
	targetNamespace = DocumentiSvcDictionary.NAMESPACE, 
	endpointInterface = "it.csi.siac.siacintegser.frontend.webservice.documenti.DocumentiService")
public class DocumentiServiceImpl implements DocumentiService
{

	@Autowired
	private ApplicationContext appCtx;


	@PostConstruct
	public void init()
	{
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	@WebMethod
	public @WebResult TestResponse test(@WebParam Test request) {
		return appCtx.getBean(TestService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	ElaboraDocumentoResponse elaboraDocumento(@WebParam ElaboraDocumento request)
	{
		return appCtx.getBean(ElaboraDocumentoGenericoService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	ElaboraDocumentoAsyncResponse elaboraDocumentoAsync(@WebParam ElaboraDocumento request)
	{
		return appCtx.getBean(ElaboraDocumentoGenericoAsyncService.class).executeService(request);
	}

	@Override
	@WebMethod
	public @WebResult
	ElaboraAttiAmministrativiResponse elaboraAttiAmministrativi(@WebParam ElaboraAttiAmministrativi request)
	{
		return appCtx.getBean(ElaboraAttiAmministrativiService.class).executeService(request);
	}
 
	@Override
	@WebMethod
	public @WebResult
	ElaboraAttiAmministrativiAsyncResponse elaboraAttiAmministrativiAsync(@WebParam ElaboraAttiAmministrativi request)
	{
		return appCtx.getBean(ElaboraAttiAmministrativiAsyncService.class).executeService(request);
	}

	
	@Override
	@WebMethod
	public @WebResult
	LeggiStatoElaborazioneDocumentoResponse leggiStatoElaborazioneDocumento(
			@WebParam LeggiStatoElaborazioneDocumento request)
	{
		return appCtx.getBean(LeggiStatoElaborazioneDocumentoService.class).executeService(request);
	}

}

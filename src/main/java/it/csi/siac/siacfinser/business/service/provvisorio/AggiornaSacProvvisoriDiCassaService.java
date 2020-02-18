/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.provvisorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSacProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaSacProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.integration.dad.ProvvisorioDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaSacProvvisoriDiCassaService
		extends AbstractBaseService<AggiornaSacProvvisoriDiCassa, AggiornaSacProvvisoriDiCassaResponse>
{

	@Autowired
	private ProvvisorioDad provvisorioDad;

	@Override
	protected void init()
	{
		final String methodName = "AggiornaSacProvvisoriDiCassaService : init()";
		
		provvisorioDad.setEnte(req.getEnte());
		provvisorioDad.setLoginOperazione(req.getRichiedente().getOperatore().getCodiceFiscale());
		
		log.debug(methodName, " - Begin");
	}

	@Override
	@Transactional
	public AggiornaSacProvvisoriDiCassaResponse executeService(AggiornaSacProvvisoriDiCassa serviceRequest)
	{
		return super.executeService(serviceRequest);
	}

	@Override
	public void execute()
	{
		provvisorioDad.aggiornaSacProvvisoriDiCassa(req.getElencoProvvisoriDiCassa(), req.getEnte(),
				req.getRichiedente());
	}

	@Override
	protected void checkServiceParam() throws ServiceParamError
	{

		checkNotNull(req.getElencoProvvisoriDiCassa(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("sac provvisori di cassa"));
		checkNotNull(req.getEnte(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("ente"));
	}
}

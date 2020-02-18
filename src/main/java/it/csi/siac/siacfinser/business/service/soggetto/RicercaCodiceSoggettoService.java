/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCodiceSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaCodiceSoggettoResponse;
import it.csi.siac.siacfinser.integration.dad.SoggettoFinDad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCodiceSoggettoService extends
		CheckedAccountBaseService<RicercaCodiceSoggetto, RicercaCodiceSoggettoResponse>
{

	@Autowired
	private SoggettoFinDad soggettoDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError
	{
		checkNotNull(req.getUidSoggetto(),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("uid soggetto"));
	}

	@Transactional(readOnly = true)
	public RicercaCodiceSoggettoResponse executeService(RicercaCodiceSoggetto serviceRequest)
	{
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute()
	{
		String codiceSoggetto = soggettoDad.ricercaCodiceSoggetto(req.getUidSoggetto());
		
		res.setCodiceSoggetto(codiceSoggetto);
	}

}

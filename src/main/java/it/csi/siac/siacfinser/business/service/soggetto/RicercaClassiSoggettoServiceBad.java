/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.soggetto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.business.service.AbstractBaseService;
import it.csi.siac.siacfinser.business.service.soggetto.hr.RicercaSoggettiHRService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaClassiSoggettoServiceBad extends AbstractBaseService<RicercaSoggetti, RicercaSoggettiResponse>
{
	@Autowired
	private ApplicationContext appCtx;

//	@Override
//	@Transactional(readOnly=true)
//	public RicercaSoggettiResponse executeService(RicercaSoggetti serviceRequest) {
//		return super.executeService(serviceRequest);
//	}

	@Override
	protected void execute()
	{
		if (req.getSorgenteDatiSoggetto() == null)
			req.setSorgenteDatiSoggetto(SorgenteDatiSoggetto.SIAC);

		switch (req.getSorgenteDatiSoggetto())
		{
		case HR:
			res = appCtx.getBean(RicercaSoggettiHRService.class).executeService(req);
			break;
		case SIAC:
			res = appCtx.getBean(RicercaSoggettiSiacService.class).executeService(req);
			break;
		default:

			res.addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Sorgente dati soggetto non valida: "
					+ req.getSorgenteDatiSoggetto().name()));
			res.setEsito(Esito.FALLIMENTO);
			break;
		}
	}
}

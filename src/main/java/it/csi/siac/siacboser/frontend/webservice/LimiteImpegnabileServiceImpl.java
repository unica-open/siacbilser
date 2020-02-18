/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacboser.frontend.webservice;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacboser.business.service.limiteimpegnabile.AggiornaCapitoloLimiteImpegnabileService;
import it.csi.siac.siacboser.frontend.webservice.msg.AggiornaCapitoloLimiteImpegnabile;
import it.csi.siac.siacboser.frontend.webservice.msg.AggiornaCapitoloLimiteImpegnabileResponse;

public class LimiteImpegnabileServiceImpl implements LimiteImpegnabileService
{
	@Autowired
	private ApplicationContext appCtx;

	@PostConstruct
	public void init()
	{ 
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override  
	public AggiornaCapitoloLimiteImpegnabileResponse aggiornaCapitoloLimiteImpegnabile(AggiornaCapitoloLimiteImpegnabile req)
	{
		return BaseServiceExecutor.execute(appCtx, AggiornaCapitoloLimiteImpegnabileService.class, req);
	}
}

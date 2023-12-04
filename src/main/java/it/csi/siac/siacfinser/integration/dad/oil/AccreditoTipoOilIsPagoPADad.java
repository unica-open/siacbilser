/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad.oil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siacfinser.integration.dao.soggetto.SiacDAccreditoTipoOilRepository;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccreditoTipoOilIsPagoPADad extends BaseDadImpl
{
	@Autowired SiacDAccreditoTipoOilRepository siacDAccreditoTipoOilRepository;
	
	public boolean accreditoTipoOilIsPagoPA(Integer idEnte, String codiceAccreditoTipo) {
		return siacDAccreditoTipoOilRepository.countAccreditoTipoOilPagoPA(idEnte, codiceAccreditoTipo) > 0;
	}
}

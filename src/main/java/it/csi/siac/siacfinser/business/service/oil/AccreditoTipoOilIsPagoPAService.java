/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.business.service.oil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siaccorser.model.Esito;
import it.csi.siac.siacfinser.frontend.webservice.msg.AccreditoTipoOilIsPagoPA;
import it.csi.siac.siacfinser.frontend.webservice.msg.AccreditoTipoOilIsPagoPAResponse;
import it.csi.siac.siacfinser.integration.dad.oil.AccreditoTipoOilIsPagoPADad;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AccreditoTipoOilIsPagoPAService extends CheckedAccountBaseService<AccreditoTipoOilIsPagoPA, AccreditoTipoOilIsPagoPAResponse>
{
	@Autowired private AccreditoTipoOilIsPagoPADad accreditoTipoOilIsPagoPADad; 

	@Override
	public AccreditoTipoOilIsPagoPAResponse executeService(AccreditoTipoOilIsPagoPA serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		res.setAccreditoTipoOilIsPagoPA(
				accreditoTipoOilIsPagoPADad.accreditoTipoOilIsPagoPA(
						req.getRichiedente().getAccount().getEnte().getUid(), 
						req.getCodiceAccreditoTipo()));
		res.setEsito(Esito.SUCCESSO);
	}
}

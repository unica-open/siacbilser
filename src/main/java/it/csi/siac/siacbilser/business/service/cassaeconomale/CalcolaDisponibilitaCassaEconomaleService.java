/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CassaEconomaleDad;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcolaDisponibilitaCassaEconomaleService extends CheckedAccountBaseService<CalcolaDisponibilitaCassaEconomale, CalcolaDisponibilitaCassaEconomaleResponse> {

	@Autowired
	private CassaEconomaleDad cassaEconomaleDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCassaEconomale(), "Cassa economale", false);
		checkEntita(req.getBilancio(), "bilancio");
	}

	@Override
	@Transactional(readOnly = true)
	public CalcolaDisponibilitaCassaEconomaleResponse executeService(CalcolaDisponibilitaCassaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		CassaEconomale cassaEconomale = req.getCassaEconomale();
		cassaEconomaleDad.calcolaImportiDerivatiCassaEconomale(cassaEconomale, req.getBilancio(), req.getImportiDerivatiRichiesti());
		
		res.setCassaEconomale(cassaEconomale);
	}

}

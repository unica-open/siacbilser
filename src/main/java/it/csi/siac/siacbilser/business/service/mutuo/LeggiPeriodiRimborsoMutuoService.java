/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.LeggiPeriodiRimborsoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.LeggiPeriodiRimborsoMutuoResponse;
import it.csi.siac.siacbilser.integration.dad.MutuoDad;
import it.csi.siac.siacbilser.model.mutuo.PeriodoRimborsoMutuo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LeggiPeriodiRimborsoMutuoService extends CheckedAccountBaseService<LeggiPeriodiRimborsoMutuo, LeggiPeriodiRimborsoMutuoResponse> {
	
	@Autowired private MutuoDad mutuoDad;
	
	@Override
	@Transactional
	public LeggiPeriodiRimborsoMutuoResponse executeService(LeggiPeriodiRimborsoMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {

	}
	
	@Override
	protected void execute() {
		List<PeriodoRimborsoMutuo> periodoRimborsoMutuos = mutuoDad.leggiPeriodiRimborsoMutuo();
		res.setListaPeriodoRimborsoMutuo(periodoRimborsoMutuos);
	}

	@Override
	protected void init() {
		mutuoDad.setEnte(ente);
	}

}

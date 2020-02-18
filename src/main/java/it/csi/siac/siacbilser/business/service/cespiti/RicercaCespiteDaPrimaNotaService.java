/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CespiteDad;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespiteDaPrimaNota;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaCespiteDaPrimaNotaResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaCespiteDaPrimaNotaService extends CheckedAccountBaseService<RicercaCespiteDaPrimaNota, RicercaCespiteDaPrimaNotaResponse> {

	// DAD
	@Autowired
	private CespiteDad cespiteDad;

	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getPrimaNota(), "prima nota");
	}
	
	@Override
	protected void init() {
		super.init();
		cespiteDad.setEnte(ente);
	}
	
	@Transactional(readOnly = true)
	@Override
	public RicercaCespiteDaPrimaNotaResponse executeService(RicercaCespiteDaPrimaNota serviceRequest) {
		return super.executeService(serviceRequest);
	}

	@Override
	protected void execute() {
		List<Cespite> listaCespite = cespiteDad.ricercaCespiteDaPrimaNota(req.getPrimaNota(),req.getTipoEvento(),CespiteModelDetail.TipoBeneCespiteModelDetail);
		res.setListaCespite(listaCespite);
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaDettaglioMutuoService extends BaseRicercaMutuoService<RicercaDettaglioMutuo, RicercaDettaglioMutuoResponse> {
	
	@Override
	@Transactional
	public RicercaDettaglioMutuoResponse executeService(RicercaDettaglioMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkUidEntita(req.getMutuo().getUid(),"uidMutuo");
	}

	@Override
	protected void execute() {
		Mutuo mutuo = mutuoDad.ricercaDettaglioMutuo(req.getMutuo(), req.getMutuoModelDetails());
		res.setMutuo(mutuo);
	}

}

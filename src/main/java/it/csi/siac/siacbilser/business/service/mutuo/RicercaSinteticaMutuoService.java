/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaSinteticaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaSinteticaMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaMutuoService extends BaseRicercaMutuoService<RicercaSinteticaMutuo, RicercaSinteticaMutuoResponse> {
	
	@Override
	@Transactional(readOnly = true)
	public RicercaSinteticaMutuoResponse executeService(RicercaSinteticaMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkNotNull(req.getParametriPaginazione(),  ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Parametri paginazione"));
	}
	
	@Override
	protected void execute() {
		ListaPaginata<Mutuo> mutui= mutuoDad.ricercaSinteticaMutui(mutuo, req.getParametriPaginazione(), req.getMutuoModelDetails());
		res.setMutui(mutui);
		res.setCardinalitaComplessiva(mutui.size());
	}
}

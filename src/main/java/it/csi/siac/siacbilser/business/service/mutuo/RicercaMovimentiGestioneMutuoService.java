/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaMovimentiGestioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaMovimentiGestioneMutuoResponse;
import it.csi.siac.siacbilser.integration.dad.MutuoDad;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public abstract class RicercaMovimentiGestioneMutuoService<MG extends MovimentoGestione, RMGMREQ extends RicercaMovimentiGestioneMutuo, RMGMRES extends RicercaMovimentiGestioneMutuoResponse<MG>> 
	extends CheckedAccountBaseService<RMGMREQ, RMGMRES> {

	protected @Autowired MutuoDad mutuoDad;
	
	@Override
	@Transactional(readOnly = true)
	public RMGMRES executeService(RMGMREQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getMovimentoGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("movimentoGestione"));
		checkNotNull(req.getParametriPaginazione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("parametriPaginazione"));
	}
	
	@Override
	protected void init() {
		mutuoDad.setEnte(ente);
	}	
	
	@Override
	protected void execute() {}
}

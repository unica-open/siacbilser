/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaMovimentiGestioneAssociatiMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaMovimentiGestioneAssociatiMutuoResponse;
import it.csi.siac.siacbilser.integration.dad.MutuoDad;
import it.csi.siac.siacbilser.model.mutuo.MovimentoGestioneAssociatoMutuo;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public abstract class RicercaMovimentiGestioneAssociatiMutuoService<MG extends MovimentoGestione, MGA extends MovimentoGestioneAssociatoMutuo<MG>, RMGAMREQ extends RicercaMovimentiGestioneAssociatiMutuo, RMGAMRES extends RicercaMovimentiGestioneAssociatiMutuoResponse<MG, MGA>> 
	extends CheckedAccountBaseService<RMGAMREQ, RMGAMRES> {

	protected @Autowired MutuoDad mutuoDad;
	
	@Override
	@Transactional(readOnly = true)
	public RMGAMRES executeService(RMGAMREQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getMutuo(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("mutuo"));
	}
	
	@Override
	protected void init() {
		mutuoDad.setEnte(ente);
	}	
	
	@Override
	protected void execute() {}
}

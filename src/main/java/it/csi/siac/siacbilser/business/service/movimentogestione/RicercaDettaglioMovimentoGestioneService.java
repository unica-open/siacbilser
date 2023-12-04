/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.movimentogestione;

import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioMovimentoGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.movimentogestione.RicercaDettaglioMovimentoGestioneResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.model.MovimentoGestione;

public abstract class RicercaDettaglioMovimentoGestioneService<MG extends MovimentoGestione, RDMGREQ extends RicercaDettaglioMovimentoGestione<MG>, RDMGRES extends RicercaDettaglioMovimentoGestioneResponse<MG>> 
	extends CheckedAccountBaseService<RDMGREQ, RDMGRES> {

	
	@Override
	@Transactional(readOnly = true)
	public RDMGRES executeService(RDMGREQ serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkNotNull(req.getMovimentoGestione(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("movimentoGestione"));
		checkUidEntita(req.getMovimentoGestione().getUid(),"uidMovimentoGestione");
	}
}

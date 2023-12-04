/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.BaseCalcoloProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.BaseCalcoloProspettoRiassuntivoCronoprogrammaResponse;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.ProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public abstract class 
	BaseCalcoloProspettoRiassuntivoCronoprogrammaService<BCP extends BaseCalcoloProspettoRiassuntivoCronoprogramma, 
														 BCPR extends BaseCalcoloProspettoRiassuntivoCronoprogrammaResponse> 
		extends CheckedAccountBaseService<BCP, BCPR>{
	
	@Autowired
	protected ProgettoDad progettoDad;
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getProgetto(), "progetto");
		checkNotNull(req.getAnno(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno"));
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		res.setListaProspettoRiassuntivoCronoprogramma(calcoloProspettoRiassuntivoCronoprogrammaDiGestione());
	}

	protected abstract List<ProspettoRiassuntivoCronoprogramma> calcoloProspettoRiassuntivoCronoprogrammaDiGestione();

}

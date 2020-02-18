/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloProspettoRiassuntivoCronoprogrammaResponse;
import it.csi.siac.siacbilser.integration.dad.ProgettoDad;
import it.csi.siac.siacbilser.model.ProspettoRiassuntivoCronoprogramma;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class CalcoloProspettoRiassuntivoCronoprogrammaService.
 * @author Nazha
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcoloProspettoRiassuntivoCronoprogrammaService extends CheckedAccountBaseService<CalcoloProspettoRiassuntivoCronoprogramma, CalcoloProspettoRiassuntivoCronoprogrammaResponse>{
		

	
	@Autowired
	private ProgettoDad progettoDad;
	
	
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
		List<ProspettoRiassuntivoCronoprogramma> listaProspettoRiassuntivoCronoprogramma=progettoDad.calcoloProspettoRiassuntivoCronoprogrammaDiGestione(req.getProgetto(), req.getAnno());
		res.setListaProspettoRiassuntivoCronoprogramma(listaProspettoRiassuntivoCronoprogramma);
		}

}

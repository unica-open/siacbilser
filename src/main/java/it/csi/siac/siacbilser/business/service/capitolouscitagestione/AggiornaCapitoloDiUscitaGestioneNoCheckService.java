/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitolouscitagestione;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestioneResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class AggiornaCapitoloDiUscitaGestioneNoCheckService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCapitoloDiUscitaGestioneNoCheckService extends AggiornaCapitoloDiUscitaGestioneService {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitoloUscitaGestione(), "capitoloUscitaGestione");
		
		checkEntita(req.getCapitoloUscitaGestione().getEnte(), "ente", false);
		checkEntita(req.getCapitoloUscitaGestione().getBilancio(), "bilancio");
		checkCondition(req.getCapitoloUscitaGestione().getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"), false);
	}
	
	@Transactional
	public AggiornaCapitoloDiUscitaGestioneResponse executeService(AggiornaCapitoloDiUscitaGestione serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkClassificatoriModificabili() {	
		//Sovrascrivo il check con il vuoto! per saltare i controlli
		log.info("checkClassificatoriModificabili", "Controllo Saltato!");
	}
	
	@Override
	protected void checkAttributiModificabili() {
		//Sovrascrivo il check con il vuoto! per saltare i controlli
		log.info("checkAttributiModificabili", "Controllo Saltato!");
	}

}

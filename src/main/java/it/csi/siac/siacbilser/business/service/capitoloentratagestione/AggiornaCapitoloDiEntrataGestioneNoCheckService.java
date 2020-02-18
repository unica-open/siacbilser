/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentratagestione;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataGestioneResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class AggiornaCapitoloDiEntrataGestioneNoCheckService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCapitoloDiEntrataGestioneNoCheckService extends AggiornaCapitoloDiEntrataGestioneService {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitoloEntrataGestione(), "capitoloUscitaGestione");
		
		checkEntita(req.getCapitoloEntrataGestione().getEnte(), "ente", false);
		checkEntita(req.getCapitoloEntrataGestione().getBilancio(), "bilancio");
		checkCondition(req.getCapitoloEntrataGestione().getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"), false);
	}
	
	@Transactional
	public AggiornaCapitoloDiEntrataGestioneResponse executeService(AggiornaCapitoloDiEntrataGestione serviceRequest) {
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

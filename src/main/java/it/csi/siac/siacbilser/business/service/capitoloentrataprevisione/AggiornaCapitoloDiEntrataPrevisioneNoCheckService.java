/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.capitoloentrataprevisione;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiEntrataPrevisioneResponse;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class AggiornaCapitoloDiEntrataPrevisioneNoCheckService.
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaCapitoloDiEntrataPrevisioneNoCheckService extends AggiornaCapitoloDiEntrataPrevisioneService {
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCapitoloEntrataPrevisione(), "capitoloUscitaPrevisione");
		
		checkEntita(req.getCapitoloEntrataPrevisione().getEnte(), "ente", false);
		checkEntita(req.getCapitoloEntrataPrevisione().getBilancio(), "bilancio");
		checkCondition(req.getCapitoloEntrataPrevisione().getBilancio().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("bilancio anno"), false);
	}
	
	@Transactional
	public AggiornaCapitoloDiEntrataPrevisioneResponse executeService(AggiornaCapitoloDiEntrataPrevisione serviceRequest) {
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

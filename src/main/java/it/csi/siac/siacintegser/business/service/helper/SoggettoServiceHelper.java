/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.helper;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.business.service.soggetto.RicercaSoggettoPerChiaveService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SoggettoServiceHelper extends IntegServiceHelper {
	
	public Soggetto findSoggetto(String codiceSoggetto, Richiedente richiedente) {

		Soggetto soggetto = null;
		
		RicercaSoggettoPerChiave reqSoggetto = new RicercaSoggettoPerChiave();
		ParametroRicercaSoggettoK param = new ParametroRicercaSoggettoK();
		param.setCodice(codiceSoggetto);
		reqSoggetto.setParametroSoggettoK(param);
		reqSoggetto.setRichiedente(richiedente);
		RicercaSoggettoPerChiaveResponse resSoggetto = appCtx.getBean(RicercaSoggettoPerChiaveService.class).executeService(
				reqSoggetto);

		checkServiceResponse(resSoggetto);

		if (resSoggetto != null && resSoggetto.getSoggetto() != null) {
			soggetto = resSoggetto.getSoggetto();
		}

		return soggetto;
	}
}

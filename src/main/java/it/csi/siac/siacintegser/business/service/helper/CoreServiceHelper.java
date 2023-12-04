/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetParametroConfigurazioneEnte;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetParametroConfigurazioneEnteResponse;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetRichiedente;
import it.csi.siac.siaccorser.frontend.webservice.msg.GetRichiedenteResponse;
import it.csi.siac.siaccorser.model.Richiedente;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CoreServiceHelper extends IntegServiceHelper {

	@Autowired private CoreService coreService;

	public Richiedente findRichiedente(String codiceFruitore, String codiceEnte) {
		GetRichiedente getRichiedente = new GetRichiedente();
		getRichiedente.setCodiceAccount(String.format("%s-%s", codiceFruitore, codiceEnte));

		GetRichiedenteResponse getrRichiedenteResponse = coreService.getRichiedente(getRichiedente);

		checkServiceResponse(getrRichiedenteResponse);		
		
		return getrRichiedenteResponse.getRichiedente();
	}

	
	public String getParametroConfigurazioneEnte(String nomeParametro, Richiedente richiedente) {
		GetParametroConfigurazioneEnte getParametroConfigurazioneEnte = new GetParametroConfigurazioneEnte();
		getParametroConfigurazioneEnte.setNomeParametro(nomeParametro);
		getParametroConfigurazioneEnte.setRichiedente(richiedente);
		
		GetParametroConfigurazioneEnteResponse getParametroConfigurazioneEnteResponse = 
				coreService.getParametroConfigurazioneEnte(getParametroConfigurazioneEnte);
		
		checkServiceResponse(getParametroConfigurazioneEnteResponse);
		
		return getParametroConfigurazioneEnteResponse.getValoreParametro();
	}
	
}

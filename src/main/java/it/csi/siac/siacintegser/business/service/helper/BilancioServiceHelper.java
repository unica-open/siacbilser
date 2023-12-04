/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.bilancio.RicercaDettaglioBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccorser.frontend.webservice.CoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.RicercaBilancio;
import it.csi.siac.siaccorser.frontend.webservice.msg.RicercaBilancioResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BilancioServiceHelper extends IntegServiceHelper {

	@Autowired private CoreService coreService;

	public Bilancio findBilancioByAnno(Ente ente, Richiedente richiedente, Integer anno) {
		RicercaBilancio ricercaBilancio = new RicercaBilancio();

		ricercaBilancio.setAnno(anno);
		ricercaBilancio.setEnte(ente);
		ricercaBilancio.setRichiedente(richiedente);

		RicercaBilancioResponse ricercaBilancioResponse = coreService.ricercaBilancio(ricercaBilancio);

		checkServiceResponse(ricercaBilancioResponse);

		return ricercaBilancioResponse.getBilancio();
	}

	public Bilancio findDettaglioBilancioByAnno(Ente ente, Richiedente richiedente, Integer annoBilancio) {
		RicercaDettaglioBilancio ricercaDettaglioBilancio = new RicercaDettaglioBilancio();

		ricercaDettaglioBilancio.setAnnoBilancio(annoBilancio);;
		ricercaDettaglioBilancio.setRichiedente(richiedente);

		RicercaDettaglioBilancioResponse ricercaDettaglioBilancioResponse = 
				appCtx.getBean(RicercaDettaglioBilancioService.class).executeService(ricercaDettaglioBilancio);

		checkServiceResponse(ricercaDettaglioBilancioResponse);

		return ricercaDettaglioBilancioResponse.getBilancio();
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.ricerche.soggetto;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacintegser.business.service.util.converter.IntegMapId;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.soggetto.RicercaSinteticaSoggetti;
import it.csi.siac.siacintegser.frontend.webservice.msg.ricerche.soggetto.RicercaSinteticaSoggettiResponse;
import it.csi.siac.siacintegser.model.messaggio.MessaggioInteg;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RicercaSinteticaSoggettiService extends
		BaseRicercaSoggettiService<RicercaSinteticaSoggetti, RicercaSinteticaSoggettiResponse>
{
	@Override
	protected RicercaSinteticaSoggettiResponse execute(RicercaSinteticaSoggetti ireq)
	{
		RicercaSinteticaSoggettiResponse ires = map(
				ricercaSoggetti(ireq, IntegMapId.RicercaSinteticaSoggetti_RicercaSoggetti),
				RicercaSinteticaSoggettiResponse.class,
				IntegMapId.RicercaSinteticaSoggettiResponse_RicercaSoggettiResponse);

		if (ires.getSoggetti().isEmpty())
			addMessaggio(MessaggioInteg.NESSUN_RISULTATO_TROVATO, "nessun criterio di ricerca soddisfatto");
		
		return ires;
	}
}

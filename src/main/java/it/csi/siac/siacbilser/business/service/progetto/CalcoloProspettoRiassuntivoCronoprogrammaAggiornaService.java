/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaAggiorna;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaAggiornaResponse;
import it.csi.siac.siacbilser.model.ProspettoRiassuntivoCronoprogramma;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcoloProspettoRiassuntivoCronoprogrammaAggiornaService 
	extends BaseCalcoloProspettoRiassuntivoCronoprogrammaService<CalcoloProspettoRiassuntivoCronoprogrammaAggiorna, CalcoloProspettoRiassuntivoCronoprogrammaAggiornaResponse> {

	@Override
	protected List<ProspettoRiassuntivoCronoprogramma> calcoloProspettoRiassuntivoCronoprogrammaDiGestione() {
		return progettoDad.calcoloProspettoRiassuntivoCronoprogrammaDiGestioneAggiorna(req.getProgetto(), req.getAnno());
	}
}

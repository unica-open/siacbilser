/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.progetto;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaConsulta;
import it.csi.siac.siacbilser.frontend.webservice.msg.progetto.CalcoloProspettoRiassuntivoCronoprogrammaConsultaResponse;
import it.csi.siac.siacbilser.model.ProspettoRiassuntivoCronoprogramma;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalcoloProspettoRiassuntivoCronoprogrammaConsultaService 
	extends BaseCalcoloProspettoRiassuntivoCronoprogrammaService<CalcoloProspettoRiassuntivoCronoprogrammaConsulta, CalcoloProspettoRiassuntivoCronoprogrammaConsultaResponse> {

	@Override
	protected List<ProspettoRiassuntivoCronoprogramma> calcoloProspettoRiassuntivoCronoprogrammaDiGestione() {
		return progettoDad.calcoloProspettoRiassuntivoCronoprogrammaDiGestioneConsulta(req.getProgetto(), req.getAnno());
	}
}

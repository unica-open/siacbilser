/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.registroiva;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRegistroIvaOnlyCheckService extends StampaRegistroIvaService {

	@Override
	protected void startElaboration() {
		// Nessuna pre-elaborazione
	}
	
	@Override
	protected void postStartElaboration() {
		// Nessuna post-elaborazione
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.compiler;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.service.stampa.liquidazioneiva.model.StampaLiquidazioneIvaDatiERiepilogo;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaLiquidazioneIvaCorrispettiviDataCompiler extends StampaLiquidazioneIvaVenditeIvaImmediataDataCompiler {
	
	@Override
	protected void createAndSetPage() {
		final String methodName = "createAndSetPage";
		pagina = new StampaLiquidazioneIvaDatiERiepilogo();
		result.setCorrispettivi(pagina);
		
		log.debug(methodName, "Pagina creata per i Corrispettivi");
	}
	
	@Override
	protected TipoRegistroIva getTipoRegistroIvaPerFiltro() {
		return TipoRegistroIva.CORRISPETTIVI;
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamentoDett;
import it.csi.siac.siaccespser.model.AnteprimaAmmortamentoAnnuoCespite;

/**
 * The Class AmmortamentoAnnuoCespiteStatoAmmortamentoAnnuoCespiteConverter.
 * @author elisa
 * @version 1.0.0 - 30-08-2018
 */
@Component
public class AmmortamentoAnnuoCespiteDettaglioAmmortamentoAnnuoCespiteAnnoSpecificoConverter extends BaseAmmortamentoAnnuoCespiteDettaglioAmmortamentoAnnuoCespiteConverter {
	
	@Override
	protected boolean aggiungiSiacTCespitiAmmortamentoDett(SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett, AnteprimaAmmortamentoAnnuoCespite anteprimaAmmortamentoAnnuoCespite) {
		if(anteprimaAmmortamentoAnnuoCespite == null) {
			//non ho passato l'anteprima con cui filtrare. controllo solo la data_cancellazione
			return siacTCespitiAmmortamentoDett.getDataCancellazione() == null;
		}
		
		Integer annoFiltro = anteprimaAmmortamentoAnnuoCespite.getAnno();
		return siacTCespitiAmmortamentoDett.getDataCancellazione() == null && (annoFiltro == null || annoFiltro.equals(siacTCespitiAmmortamentoDett.getCesAmmDettAnno()));
	}
}

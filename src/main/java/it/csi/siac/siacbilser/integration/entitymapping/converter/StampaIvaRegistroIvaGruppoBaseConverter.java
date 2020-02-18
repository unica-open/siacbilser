/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;

/**
 * The Class StampaIvaRegistroIvaGruppoBaseConverter.
 */
@Component
public class StampaIvaRegistroIvaGruppoBaseConverter extends StampaIvaRegistroIvaConverter {
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siacbilser.integration.entitymapping.converter.StampaIvaRegistroIvaConverter#getMapIdSiacTIvaRegistro_RegistroIva()
	 */
	@Override
	protected BilMapId getMapIdRegistroIva() {
		return BilMapId.SiacTIvaRegistro_RegistroIva_GruppoBase;
	}
	
}

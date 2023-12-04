/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;

/**
 * The Class RichiestaEconomaleCassaEconomaleMinimalConverter.
 */
@Component
public class RichiestaEconomaleCassaEconomaleMinimalConverter extends RichiestaEconomaleCassaEconomaleConverter {
	
	@Override
	protected CecMapId getMapIdCassaEconomale() {
		return CecMapId.SiacTCassaEcon_CassaEconomale_Minimal;
	}
	
}

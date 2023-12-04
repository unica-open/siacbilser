/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;

/**
 * The Class CausaleEPContoMediumConverter.
 *
 * @author Domenico
 */
@Component
public class CausaleEPContoMediumConverter extends CausaleEPContoConverter {
	

	@Override
	protected GenMapId getMapIdConto() {
		return GenMapId.SiacTPdceConto_Conto_Medium;
				
	}

	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.util.dozer.MapId;

/**
 * The Class SubdocumentoSpesaLiquidazioneMinimalConverter
 */
@Component
public class SubdocumentoSpesaLiquidazioneMinimalConverter extends SubdocumentoSpesaLiquidazioneBaseConverter {

	@Override
	protected MapId getMapId() {
		return BilMapId.SiacTLiquidazione_Liquidazione_ModelDetail;
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;

/**
 * The Class RendicontoRichiestaMovimentiBaseConverter.
 */
@Component
public class RendicontoRichiestaMovimentiBaseConverter extends RendicontoRichiestaMovimentiConverter {
	

	@Override
	protected CecMapId getMapIdMovimento() {
		return CecMapId.SiacTMovimento_Movimento_Base;
	}
	

}

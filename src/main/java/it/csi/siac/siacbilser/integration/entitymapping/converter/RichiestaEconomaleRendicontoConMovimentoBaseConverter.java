/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class RichiestaEconomaleRendicontoConMovimentoBaseConverter extends RichiestaEconomaleRendicontoConverter {
	
	@Override
	protected CecMapId getMapIdRendicontoRichiesta() {
		return CecMapId.SiacTGiustificativo_RendicontoRichiesta_Base_Movimento;
	}


}

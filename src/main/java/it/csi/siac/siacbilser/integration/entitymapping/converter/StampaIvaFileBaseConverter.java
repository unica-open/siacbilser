/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;

/**
 * The Class GruppoAttivitaIvaRegistroIvaConverter.
 * @author Domenico
 */
@Component
public class StampaIvaFileBaseConverter extends StampaIvaFileConverter {
	
	@Override
	protected BilMapId getMapIdFile() {
		return BilMapId.SiacTFile_File_Base;
	}
	
}

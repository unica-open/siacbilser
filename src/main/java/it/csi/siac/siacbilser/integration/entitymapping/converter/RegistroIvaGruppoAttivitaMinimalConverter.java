/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;

/**
 * The Class RegistroIvaGruppoAttivitaConverter.
 * @author Domenico
 */
@Component
public class RegistroIvaGruppoAttivitaMinimalConverter extends RegistroIvaGruppoAttivitaConverter {
	
	@Override
	protected BilMapId getMapIdGruppoAttivitaIva() {
		return BilMapId.SiacTIvaGruppo_GruppoAttivitaIva_Minimal;
	}

	
}

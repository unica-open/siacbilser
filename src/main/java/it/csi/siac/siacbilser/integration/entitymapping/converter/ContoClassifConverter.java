/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacgenser.model.CodiceBilancio;

/**
 * The Class ContoClassifConverter.
 */
@Component
public class ContoClassifConverter extends BaseContoClassifConverter {
	
	@Override
	protected void mapToCodiceBilancio(SiacTClass siacTClass, CodiceBilancio codiceBilancio) {
		map(siacTClass,codiceBilancio,BilMapId.SiacTClass_ClassificatoreGerarchico); 
	}

}

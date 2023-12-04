/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRClassFamTree;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacgenser.model.CodiceBilancio;

/**
 * The Class ContoClassifConverter.
 */
@Component
public class ContoClassifExtendedConverter extends BaseContoClassifConverter {
	
	@Override
	protected void mapToCodiceBilancio(SiacTClass siacTClass, CodiceBilancio codiceBilancio) {
		map(siacTClass, codiceBilancio, BilMapId.SiacTClass_ClassificatoreGerarchico);

		List<SiacRClassFamTree> f = siacTClass.getSiacRClassFamTreesFiglio();

		StringBuilder codice = new StringBuilder(codiceBilancio.getCodice()); 
		
		SiacTClass siacTClassPadre = null;
		
		while (f != null && ! f.isEmpty() && f.get(0) != null && (siacTClassPadre = f.get(0).getSiacTClassPadre()) != null) {
			codice.insert(0, " - ");
			codice.insert(0, siacTClassPadre.getClassifCode());
			
			f = siacTClassPadre.getSiacRClassFamTreesFiglio();
		}

		codiceBilancio.setCodice(codice.toString());
	}

}

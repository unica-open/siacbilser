/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.Bilancio;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class CapitoloBilancioConverter extends ExtendedDozerConverter<Capitolo<?, ?>, SiacTBilElem > {

	/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	@SuppressWarnings("unchecked")
	public CapitoloBilancioConverter() {
		super((Class<Capitolo<?, ?>>)(Class<?>)Capitolo.class, SiacTBilElem.class);
	}

	@Override
	public Capitolo<?, ?> convertFrom(SiacTBilElem src, Capitolo<?, ?> dest) {
		Bilancio bilancio = map(src.getSiacTBil(), Bilancio.class, BilMapId.SiacTBil_Bilancio);
		dest.setBilancio(bilancio);
		return dest;
	}

	@Override
	public SiacTBilElem convertTo(Capitolo<?, ?> src, SiacTBilElem dest) {	
		return dest;	
	}
	
}

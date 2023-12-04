/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.Bilancio;


/**
 * The Class CapitoloEntrataGestioneBilancioConverter.
 */
@Component
public class CapitoloEntrataGestioneBilancioConverter extends ExtendedDozerConverter<CapitoloEntrataGestione, SiacTBilElem > {

	/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	public CapitoloEntrataGestioneBilancioConverter() {
		super(CapitoloEntrataGestione.class, SiacTBilElem.class);
	}


	@Override
	public CapitoloEntrataGestione convertFrom(SiacTBilElem src, CapitoloEntrataGestione dest) {
		Bilancio bilancio = map(src.getSiacTBil(), Bilancio.class, BilMapId.SiacTBil_Bilancio);
		dest.setBilancio(bilancio);
		return dest;
	}

	@Override
	public SiacTBilElem convertTo(CapitoloEntrataGestione src, SiacTBilElem dest) {	
		return dest;	
	}
	
}

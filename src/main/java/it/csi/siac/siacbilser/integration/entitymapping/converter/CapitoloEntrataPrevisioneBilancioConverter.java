/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siaccorser.model.Bilancio;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class CapitoloEntrataPrevisioneBilancioConverter extends ExtendedDozerConverter<CapitoloEntrataPrevisione, SiacTBilElem > {

	/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	public CapitoloEntrataPrevisioneBilancioConverter() {
		super(CapitoloEntrataPrevisione.class, SiacTBilElem.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CapitoloEntrataPrevisione convertFrom(SiacTBilElem src, CapitoloEntrataPrevisione dest) {
		Bilancio bilancio = map(src.getSiacTBil(), Bilancio.class, BilMapId.SiacTBil_Bilancio);
		dest.setBilancio(bilancio);
		return dest;
	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTBilElem convertTo(CapitoloEntrataPrevisione src, SiacTBilElem dest) {	
		return dest;	
	}
	
}

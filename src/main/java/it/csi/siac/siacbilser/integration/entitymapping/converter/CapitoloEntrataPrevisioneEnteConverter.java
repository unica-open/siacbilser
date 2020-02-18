/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siaccorser.model.Ente;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class CapitoloEntrataPrevisioneEnteConverter extends ExtendedDozerConverter<CapitoloEntrataPrevisione, SiacTBilElem > {
	
		/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	public CapitoloEntrataPrevisioneEnteConverter() {
		super(CapitoloEntrataPrevisione.class, SiacTBilElem.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CapitoloEntrataPrevisione convertFrom(SiacTBilElem src, CapitoloEntrataPrevisione dest) {
		Ente ente = map(src.getSiacTEnteProprietario(), Ente.class, BilMapId.SiacTEnteProprietario_Ente);
		dest.setEnte(ente);
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

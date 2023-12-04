/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siaccorser.model.Ente;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class CapitoloUscitaPrevisioneEnteConverter extends ExtendedDozerConverter<CapitoloUscitaPrevisione, SiacTBilElem > {
	
		/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	public CapitoloUscitaPrevisioneEnteConverter() {
		super(CapitoloUscitaPrevisione.class, SiacTBilElem.class);
	}

	@Override
	public CapitoloUscitaPrevisione convertFrom(SiacTBilElem src, CapitoloUscitaPrevisione dest) {
		Ente ente = map(src.getSiacTEnteProprietario(), Ente.class, BilMapId.SiacTEnteProprietario_Ente);
		dest.setEnte(ente);
		return dest;
	}

	@Override
	public SiacTBilElem convertTo(CapitoloUscitaPrevisione src, SiacTBilElem dest) {	
		return dest;	
	}
	
}

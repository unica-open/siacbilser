/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccorser.model.Ente;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class CapitoloEntrataGestioneEnteConverter extends ExtendedDozerConverter<CapitoloEntrataGestione, SiacTBilElem > {
	
		/**
	 * Instantiates a CapitoloEntrataGestioneEnteConverter
	 */
	public CapitoloEntrataGestioneEnteConverter() {
		super(CapitoloEntrataGestione.class, SiacTBilElem.class);
	}

	@Override
	public CapitoloEntrataGestione convertFrom(SiacTBilElem src, CapitoloEntrataGestione dest) {
		Ente ente = map(src.getSiacTEnteProprietario(), Ente.class, BilMapId.SiacTEnteProprietario_Ente);
		dest.setEnte(ente);
		return dest;
	}

	@Override
	public SiacTBilElem convertTo(CapitoloEntrataGestione src, SiacTBilElem dest) {	
		return dest;	
	}
	
}

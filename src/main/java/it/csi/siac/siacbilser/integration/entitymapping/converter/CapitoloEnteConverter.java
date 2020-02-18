/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.Ente;


/**
 * The Class CapitoloUscitaGestioneEnteConverter.
 */
@Component
public class CapitoloEnteConverter extends ExtendedDozerConverter<Capitolo<?, ?>, SiacTBilElem > {
	
		/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	public CapitoloEnteConverter() {
		super((Class<Capitolo<?, ?>>)(Class<?>)Capitolo.class, SiacTBilElem.class);
	}

	@Override
	public Capitolo<?, ?> convertFrom(SiacTBilElem src, Capitolo<?, ?> dest) {
		Ente ente = map(src.getSiacTEnteProprietario(), Ente.class, BilMapId.SiacTEnteProprietario_Ente);
		dest.setEnte(ente);
		return dest;
	}

	@Override
	public SiacTBilElem convertTo(Capitolo<?, ?> src, SiacTBilElem dest) {	
		return dest;	
	}
	
}

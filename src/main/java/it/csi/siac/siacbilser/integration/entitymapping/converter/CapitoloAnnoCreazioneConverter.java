/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.Bilancio;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class CapitoloAnnoCreazioneConverter extends ExtendedDozerConverter<Capitolo<?, ?>, SiacTBilElem > {

	/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	@SuppressWarnings("unchecked")
	public CapitoloAnnoCreazioneConverter() {
		super((Class<Capitolo<?, ?>>)(Class<?>)Capitolo.class, SiacTBilElem.class);
	}

	@Override
	public Capitolo<?, ?> convertFrom(SiacTBilElem src, Capitolo<?, ?> dest) {
		Date dataCreazione = src.getDataCreazione();
		Calendar cal = Calendar.getInstance();
		cal.setTime((Date) dataCreazione);
		dest.setAnnoCreazioneCapitolo(cal.get(Calendar.YEAR));
		return dest;
	}

	@Override
	public SiacTBilElem convertTo(Capitolo<?, ?> src, SiacTBilElem dest) {	
		return dest;	
	}
	
}

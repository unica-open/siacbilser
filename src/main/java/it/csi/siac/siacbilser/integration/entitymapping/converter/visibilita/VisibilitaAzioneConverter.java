/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.visibilita;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTAzione;
import it.csi.siac.siacbilser.integration.entity.SiacTVisibilita;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.visibilita.Visibilita;
import it.csi.siac.siaccorser.model.Azione;

/**
 * The Class VisibilitaAzioneConverter.
 */
@Component
public class VisibilitaAzioneConverter extends ExtendedDozerConverter<Visibilita, SiacTVisibilita> {
	
	/**
	 * Instantiates a new visibilita azione.
	 */
	public VisibilitaAzioneConverter() {
		super(Visibilita.class, SiacTVisibilita.class);
	}

	@Override
	public Visibilita convertFrom(SiacTVisibilita src, Visibilita dest) {
		if(dest == null || src == null || src.getSiacTAzione() == null) {
			return dest;
		}
		Azione azione = map(src.getSiacTAzione(), Azione.class, BilMapId.SiacTAzione_Azione);
		dest.setAzione(azione);
		return dest;
	}

	@Override
	public SiacTVisibilita convertTo(Visibilita src, SiacTVisibilita dest) {
		if(dest == null || src == null || src.getAzione() == null) {
			return dest;
		}
		SiacTAzione siacTAzione = map(src.getAzione(), SiacTAzione.class, BilMapId.SiacTAzione_Azione);
		dest.setSiacTAzione(siacTAzione);
		return dest;
	}

}

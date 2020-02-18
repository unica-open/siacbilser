/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneCapitolo;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacgenser.model.ConciliazionePerCapitolo;

/**
 * The Class ConciliazionePerCapitoloClassificatoreConverter.
 */
@Component
public class ConciliazionePerCapitoloCapitoloConverter extends ConciliazioneCapitoloConverter<ConciliazionePerCapitolo, SiacRConciliazioneCapitolo> {
	
	/**
	 * Instantiates a new conciliazione per capitolo classificatore converter.
	 */
	public ConciliazionePerCapitoloCapitoloConverter() {
		super(ConciliazionePerCapitolo.class, SiacRConciliazioneCapitolo.class);
	}

	@Override
	public ConciliazionePerCapitolo convertFrom(SiacRConciliazioneCapitolo src, ConciliazionePerCapitolo dest) {
		if(src.getSiacTBilElem() == null) {
			return dest;
		}
		
		Capitolo<?, ?> capitolo = ottieniCapitolo(src.getSiacTBilElem().getUid());
		dest.setCapitolo(capitolo);
		return dest;
	}

	@Override
	public SiacRConciliazioneCapitolo convertTo(ConciliazionePerCapitolo src, SiacRConciliazioneCapitolo dest) {
		if(src.getCapitolo() == null || src.getCapitolo().getUid() == 0) {
			return dest;
		}
		SiacTBilElem siacTBilElem = new SiacTBilElem();
		siacTBilElem.setUid(src.getCapitolo().getUid());
		dest.setSiacTBilElem(siacTBilElem);
		
		return dest;
	}

}

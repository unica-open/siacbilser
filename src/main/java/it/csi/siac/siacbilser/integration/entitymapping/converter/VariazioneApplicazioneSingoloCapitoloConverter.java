/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDVariazioneApplicazioneEnum;
import it.csi.siac.siacbilser.model.ApplicazioneVariazione;
import it.csi.siac.siacbilser.model.VariazioneImportoSingoloCapitolo;

/**
 * The Class VariazioneApplicazioneSingoloCapitoloConverter.
 */
@Component
public class VariazioneApplicazioneSingoloCapitoloConverter extends DozerConverter<VariazioneImportoSingoloCapitolo, SiacTVariazione> {
	
	
	public VariazioneApplicazioneSingoloCapitoloConverter() {
		super(VariazioneImportoSingoloCapitolo.class, SiacTVariazione.class);
	}
	
	@Override
	public VariazioneImportoSingoloCapitolo convertFrom(SiacTVariazione src, VariazioneImportoSingoloCapitolo dest) {
		ApplicazioneVariazione applicazioneVariazione = getApplicazioneVariazione(src);
		dest.setApplicazioneVariazione(applicazioneVariazione);
		return dest;
	}
	
	private ApplicazioneVariazione getApplicazioneVariazione(SiacTVariazione src) {
		if(src.getSiacDVariazioneApplicazione() == null) {
			return null;
		}
		
		String codice = src.getSiacDVariazioneApplicazione().getApplicazioneCode();
		return SiacDVariazioneApplicazioneEnum.byCodice(codice).getApplicazioneVariazione(); 
		
	}


	@Override
	public SiacTVariazione convertTo(VariazioneImportoSingoloCapitolo src, SiacTVariazione dest) {
		// Non converto
		return dest;
	}


}

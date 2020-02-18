/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTCespiti;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.Cespite;

/**
 * The Class CespitiTipoBeneCespiteConverter.
 *
 * @author Anto
 */
@Component
public class CespiteIsCollegatoAPrimeNoteConverter extends ExtendedDozerConverter<Cespite, SiacTCespiti > {
	
	@Autowired
	SiacTCespitiRepository siacTCespitiRepository;
	
	public CespiteIsCollegatoAPrimeNoteConverter() {
		super(Cespite.class, SiacTCespiti.class);
	}

	@Override
	public Cespite convertFrom(SiacTCespiti src, Cespite dest) {
		if(!Boolean.TRUE.equals(src.getFlgDonazioneRinvenimento())) {
			return dest;
		}
		Long count = siacTCespitiRepository.countSiacTPrimeNoteAssociateACespiti(src.getUid());
		dest.setIsCollegatoPrimeNote(count != null && count.longValue() != 0L);
		return dest;
	}

	@Override
	public SiacTCespiti convertTo(Cespite src, SiacTCespiti dest) {
		return dest;
	}


}

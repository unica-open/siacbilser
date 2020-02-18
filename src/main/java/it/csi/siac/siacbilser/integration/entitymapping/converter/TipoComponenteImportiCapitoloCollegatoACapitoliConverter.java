/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.componenteimporticapitolo.SiacTBilElemDetCompRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

@Component
public class TipoComponenteImportiCapitoloCollegatoACapitoliConverter extends ExtendedDozerConverter<TipoComponenteImportiCapitolo, SiacDBilElemDetCompTipo> {

	@Autowired 
	private SiacTBilElemDetCompRepository siacTBilElemDetCompRepository;

	public TipoComponenteImportiCapitoloCollegatoACapitoliConverter() {
		super(TipoComponenteImportiCapitolo.class, SiacDBilElemDetCompTipo.class);
	}

	@Override
	public TipoComponenteImportiCapitolo convertFrom(SiacDBilElemDetCompTipo src, TipoComponenteImportiCapitolo dest) {
		
		Long countByElemDetCompTipoId = siacTBilElemDetCompRepository.countByElemDetCompTipoId(src.getUid());

		dest.setCollegatoACapitoli(Boolean.valueOf(countByElemDetCompTipoId != null && countByElemDetCompTipoId.longValue() > 0L));
		
		return dest;
	}

	@Override
	public SiacDBilElemDetCompTipo convertTo(TipoComponenteImportiCapitolo src, SiacDBilElemDetCompTipo dest) {
		return dest;
	}
}

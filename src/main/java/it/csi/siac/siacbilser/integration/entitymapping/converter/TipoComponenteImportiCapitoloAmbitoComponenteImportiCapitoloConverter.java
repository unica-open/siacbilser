/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipoAmbito;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetCompTipoAmbitoEnum;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

@Component
public class TipoComponenteImportiCapitoloAmbitoComponenteImportiCapitoloConverter 
	extends ExtendedDozerConverter<TipoComponenteImportiCapitolo, SiacDBilElemDetCompTipo> {

	@Autowired
	private EnumEntityFactory eef;

	public TipoComponenteImportiCapitoloAmbitoComponenteImportiCapitoloConverter() {
		super(TipoComponenteImportiCapitolo.class, SiacDBilElemDetCompTipo.class);
	}

	@Override
	public TipoComponenteImportiCapitolo convertFrom(SiacDBilElemDetCompTipo src, TipoComponenteImportiCapitolo dest) {

		if (src.getSiacDBilElemDetCompTipoAmbito() == null) {
			return dest;
		}
		
		dest.setAmbitoComponenteImportiCapitolo(
			SiacDBilElemDetCompTipoAmbitoEnum.byCodice(
				src.getSiacDBilElemDetCompTipoAmbito()
					.getElemDetCompTipoAmbitoCode())
					.getAmbitoComponenteImportiCapitolo());
		
        return dest;
	}

	@Override
	public SiacDBilElemDetCompTipo convertTo(TipoComponenteImportiCapitolo src, SiacDBilElemDetCompTipo dest) {
		
		if(src.getAmbitoComponenteImportiCapitolo() == null) {
			return dest;
		}
		
		dest.setSiacDBilElemDetCompTipoAmbito(
			eef.getEntity(
				SiacDBilElemDetCompTipoAmbitoEnum.byAmbitoComponenteImportiCapitolo(src.getAmbitoComponenteImportiCapitolo()),
				dest.getSiacTEnteProprietario().getUid(), 
				SiacDBilElemDetCompTipoAmbito.class));

		return dest;
	}
}

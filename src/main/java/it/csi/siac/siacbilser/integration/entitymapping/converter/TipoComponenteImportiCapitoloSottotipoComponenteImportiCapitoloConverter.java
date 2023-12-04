/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompSottoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetCompSottoTipoEnum;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

@Component
public class TipoComponenteImportiCapitoloSottotipoComponenteImportiCapitoloConverter 
	extends ExtendedDozerConverter<TipoComponenteImportiCapitolo, SiacDBilElemDetCompTipo> {

	@Autowired
	private EnumEntityFactory eef;

	public TipoComponenteImportiCapitoloSottotipoComponenteImportiCapitoloConverter() {
		super(TipoComponenteImportiCapitolo.class, SiacDBilElemDetCompTipo.class);
	}

	@Override
	public TipoComponenteImportiCapitolo convertFrom(SiacDBilElemDetCompTipo src, TipoComponenteImportiCapitolo dest) {

		if (src.getSiacDBilElemDetCompSottoTipo() == null) {
			return dest;
		}
		
		dest.setSottotipoComponenteImportiCapitolo(
			SiacDBilElemDetCompSottoTipoEnum.byCodice(
				src.getSiacDBilElemDetCompSottoTipo()
					.getElemDetCompSottoTipoCode())
					.getSottotipoComponenteImportiCapitolo());
		
        return dest;
	}

	@Override
	public SiacDBilElemDetCompTipo convertTo(TipoComponenteImportiCapitolo src, SiacDBilElemDetCompTipo dest) {
		
		if(src.getSottotipoComponenteImportiCapitolo() == null) {
			return dest;
		}
		
		dest.setSiacDBilElemDetCompSottoTipo(
			eef.getEntity(
				SiacDBilElemDetCompSottoTipoEnum.bySottotipoComponenteImportiCapitolo(src.getSottotipoComponenteImportiCapitolo()),
				dest.getSiacTEnteProprietario().getUid(), 
				SiacDBilElemDetCompSottoTipo.class));

		return dest;

	}
}

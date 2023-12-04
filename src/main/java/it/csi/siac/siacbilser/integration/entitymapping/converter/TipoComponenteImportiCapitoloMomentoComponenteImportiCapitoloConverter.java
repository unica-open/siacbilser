/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipoFase;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetCompTipoFaseEnum;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

@Component
public class TipoComponenteImportiCapitoloMomentoComponenteImportiCapitoloConverter 
	extends ExtendedDozerConverter<TipoComponenteImportiCapitolo, SiacDBilElemDetCompTipo> {

	@Autowired
	private EnumEntityFactory eef;

	public TipoComponenteImportiCapitoloMomentoComponenteImportiCapitoloConverter() {
		super(TipoComponenteImportiCapitolo.class, SiacDBilElemDetCompTipo.class);
	}

	@Override
	public TipoComponenteImportiCapitolo convertFrom(SiacDBilElemDetCompTipo src, TipoComponenteImportiCapitolo dest) {

		if (src.getSiacDBilElemDetCompTipoFase() == null) {
			return dest;
		}
		
		dest.setMomentoComponenteImportiCapitolo(
			SiacDBilElemDetCompTipoFaseEnum.byCodice(
				src.getSiacDBilElemDetCompTipoFase()
					.getElemDetCompTipoFaseCode())
					.getMomentoComponenteImportiCapitolo());
		
        return dest;
	}

	@Override
	public SiacDBilElemDetCompTipo convertTo(TipoComponenteImportiCapitolo src, SiacDBilElemDetCompTipo dest) {
		
		if(src.getMomentoComponenteImportiCapitolo() == null) {
			return dest;
		}
		
		dest.setSiacDBilElemDetCompTipoFase(
			eef.getEntity(
				SiacDBilElemDetCompTipoFaseEnum.byMomentoComponenteImportiCapitolo(src.getMomentoComponenteImportiCapitolo()),
				dest.getSiacTEnteProprietario().getUid(), 
				SiacDBilElemDetCompTipoFase.class));

		return dest;
	}
}

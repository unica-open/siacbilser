/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompMacroTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetCompMacroTipoEnum;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

@Component
public class TipoComponenteImportiCapitoloMacrotipoComponenteImportiCapitoloConverter 
	extends ExtendedDozerConverter<TipoComponenteImportiCapitolo, SiacDBilElemDetCompTipo> {

	@Autowired
	private EnumEntityFactory eef;

	public TipoComponenteImportiCapitoloMacrotipoComponenteImportiCapitoloConverter() {
		super(TipoComponenteImportiCapitolo.class, SiacDBilElemDetCompTipo.class);
	}

	@Override
	public TipoComponenteImportiCapitolo convertFrom(SiacDBilElemDetCompTipo src, TipoComponenteImportiCapitolo dest) {

		if (src.getSiacDBilElemDetCompMacroTipo() == null) {
			return dest;
		}
		
		dest.setMacrotipoComponenteImportiCapitolo(
			SiacDBilElemDetCompMacroTipoEnum.byCodice(
				src.getSiacDBilElemDetCompMacroTipo()
					.getElemDetCompMacroTipoCode())
					.getMacrotipoComponenteImportiCapitolo());
		
        return dest;
	}

	@Override
	public SiacDBilElemDetCompTipo convertTo(TipoComponenteImportiCapitolo src, SiacDBilElemDetCompTipo dest) {
		
		if(src.getMacrotipoComponenteImportiCapitolo() == null) {
			return dest;
		}
		
		dest.setSiacDBilElemDetCompMacroTipo(
			eef.getEntity(
				SiacDBilElemDetCompMacroTipoEnum.byMacrotipoComponenteImportiCapitolo(src.getMacrotipoComponenteImportiCapitolo()),
				dest.getSiacTEnteProprietario().getUid(), 
				SiacDBilElemDetCompMacroTipo.class));

		return dest;
	}
}

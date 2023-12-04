/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipoFonte;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetCompTipoFonteEnum;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

@Component
public class TipoComponenteImportiCapitoloFonteFinanziariaComponenteImportiCapitoloConverter 
	extends ExtendedDozerConverter<TipoComponenteImportiCapitolo, SiacDBilElemDetCompTipo> {

	@Autowired
	private EnumEntityFactory eef;

	public TipoComponenteImportiCapitoloFonteFinanziariaComponenteImportiCapitoloConverter() {
		super(TipoComponenteImportiCapitolo.class, SiacDBilElemDetCompTipo.class);
	}

	@Override
	public TipoComponenteImportiCapitolo convertFrom(SiacDBilElemDetCompTipo src, TipoComponenteImportiCapitolo dest) {

		if (src.getSiacDBilElemDetCompTipoFonte() == null) {
			return dest;
		}
		
		dest.setFonteFinanziariaComponenteImportiCapitolo(
			SiacDBilElemDetCompTipoFonteEnum.byCodice(
				src.getSiacDBilElemDetCompTipoFonte()
					.getElemDetCompTipoFonteCode())
					.getFonteFinanziariaComponenteImportiCapitolo());
		
        return dest;
	}

	@Override
	public SiacDBilElemDetCompTipo convertTo(TipoComponenteImportiCapitolo src, SiacDBilElemDetCompTipo dest) {
		
		if(src.getFonteFinanziariaComponenteImportiCapitolo() == null) {
			return dest;
		}
		
		dest.setSiacDBilElemDetCompTipoFonte(
			eef.getEntity(
				SiacDBilElemDetCompTipoFonteEnum.byFonteFinanziariaComponenteImportiCapitolo(src.getFonteFinanziariaComponenteImportiCapitolo()),
				dest.getSiacTEnteProprietario().getUid(), 
				SiacDBilElemDetCompTipoFonte.class));

		return dest;
	}
}

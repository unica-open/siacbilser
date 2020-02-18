/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoGestioneComponenteImportiCapitolo;

@Component
public class TipoComponenteImportiCapitoloTipoGestioneComponenteImportiCapitoloConverter 
	extends ExtendedDozerConverter<TipoComponenteImportiCapitolo, SiacDBilElemDetCompTipo> {

	public TipoComponenteImportiCapitoloTipoGestioneComponenteImportiCapitoloConverter() {
		super(TipoComponenteImportiCapitolo.class, SiacDBilElemDetCompTipo.class);
	}

	@Override
	public TipoComponenteImportiCapitolo convertFrom(SiacDBilElemDetCompTipo src, TipoComponenteImportiCapitolo dest) {

		if (src.getElemDetCompTipoGestAut() == null) {
			return dest;
		}
		
		dest.setTipoGestioneComponenteImportiCapitolo(TipoGestioneComponenteImportiCapitolo.valueOf(src.getElemDetCompTipoGestAut()));
		
        return dest;
	}

	@Override
	public SiacDBilElemDetCompTipo convertTo(TipoComponenteImportiCapitolo src, SiacDBilElemDetCompTipo dest) {
		
		if(src.getTipoGestioneComponenteImportiCapitolo() == null) {
			return dest;
		}
		
		dest.setElemDetCompTipoGestAut(TipoGestioneComponenteImportiCapitolo.SOLO_AUTOMATICA.equals(src.getTipoGestioneComponenteImportiCapitolo()));

		return dest;

	}
}

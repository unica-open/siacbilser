/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetCompTipoDef;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetCompTipoDefEnum;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;

@Component
public class TipoComponenteImportiCapitoloPropostaDefaultComponenteImportiCapitoloConverter 
	extends ExtendedDozerConverter<TipoComponenteImportiCapitolo, SiacDBilElemDetCompTipo> {

	@Autowired
	private EnumEntityFactory eef;

	public TipoComponenteImportiCapitoloPropostaDefaultComponenteImportiCapitoloConverter() {
		super(TipoComponenteImportiCapitolo.class, SiacDBilElemDetCompTipo.class);
	}

	@Override
	public TipoComponenteImportiCapitolo convertFrom(SiacDBilElemDetCompTipo src, TipoComponenteImportiCapitolo dest) {

		if (src.getSiacDBilElemDetCompTipoDef() == null) {
			return dest;
		}
		
		dest.setPropostaDefaultComponenteImportiCapitolo(
			SiacDBilElemDetCompTipoDefEnum.byCodice(
				src.getSiacDBilElemDetCompTipoDef()
					.getElemDetCompTipoDefCode())
					.getPropostaDefaultComponenteImportiCapitolo());
		
        return dest;
	}

	@Override
	public SiacDBilElemDetCompTipo convertTo(TipoComponenteImportiCapitolo src, SiacDBilElemDetCompTipo dest) {
		
		if(src.getPropostaDefaultComponenteImportiCapitolo() == null) {
			return dest;
		}
		
		dest.setSiacDBilElemDetCompTipoDef(
			eef.getEntity(
				SiacDBilElemDetCompTipoDefEnum.byPropostaDefaultComponenteImportiCapitolo(src.getPropostaDefaultComponenteImportiCapitolo()),
				dest.getSiacTEnteProprietario().getUid(), 
				SiacDBilElemDetCompTipoDef.class));

		return dest;
	}
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.visibilita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDTipoCampo;
import it.csi.siac.siacbilser.integration.entity.SiacTVisibilita;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDTipoCampoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.visibilita.TipoCampo;
import it.csi.siac.siacbilser.model.visibilita.Visibilita;

/**
 * The Class VisibilitaAzioneConverter.
 */
@Component
public class VisibilitaTipoCampoConverter extends ExtendedDozerConverter<Visibilita, SiacTVisibilita> {
	
	@Autowired private EnumEntityFactory eef;
	/**
	 * Costruttore per la superclasse
	 */
	public VisibilitaTipoCampoConverter() {
		super(Visibilita.class, SiacTVisibilita.class);
	}
	
	@Override
	public Visibilita convertFrom(SiacTVisibilita src, Visibilita dest) {
		if(dest == null || src == null || src.getSiacDTipoCampo() == null) {
			return dest;
		}
		TipoCampo tipoCampo = SiacDTipoCampoEnum.byCodice(src.getSiacDTipoCampo().getTcCode()).getTipoCampo();
		dest.setTipoCampo(tipoCampo);
		return dest;
	}

	@Override
	public SiacTVisibilita convertTo(Visibilita src, SiacTVisibilita dest) {
		if(dest == null || src == null || src.getTipoCampo() == null) {
			return dest;
		}
		
		SiacDTipoCampoEnum siacDTipoCampoEnum = SiacDTipoCampoEnum.byTipoCampo(src.getTipoCampo());
		SiacDTipoCampo siacDTipoCampo = eef.getEntity(siacDTipoCampoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDTipoCampo.class); 
		dest.setSiacDTipoCampo(siacDTipoCampo);
		return dest;
	}

}

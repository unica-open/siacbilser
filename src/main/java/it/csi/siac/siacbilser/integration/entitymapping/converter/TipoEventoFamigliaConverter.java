/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacDEventoTipoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDEventoTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDEventoTipoEnum;
import it.csi.siac.siacgenser.model.TipoEvento;

@Component
public class TipoEventoFamigliaConverter extends DozerConverter<TipoEvento, SiacDEventoTipo> {
	
	
	@Autowired
	private EnumEntityFactory eef;
	@Autowired
	private SiacDEventoTipoRepository siacDEventoTipoRepository;

	
	public TipoEventoFamigliaConverter() {
		super(TipoEvento.class, SiacDEventoTipo.class);
	}


	@Override
	public TipoEvento convertFrom(SiacDEventoTipo src, TipoEvento dest) {
		SiacDEventoTipo siacDEventoTipo = siacDEventoTipoRepository.findOne(src.getUid());
		
		SiacDEventoTipoEnum siacDEventoTipoEnum = SiacDEventoTipoEnum.byEventoTipoCodeEvenNull(siacDEventoTipo.getEventoTipoCode());
		if(siacDEventoTipoEnum==null){
			return dest;
		}
		
		SiacDEventoFamTipoEnum siacDEventoFamTipoEnum = siacDEventoTipoEnum.getSiacDEventoFamTipoEnum();
		
		if(SiacDEventoFamTipoEnum.Entrata.equals(siacDEventoFamTipoEnum)){
			dest.setSpesa(Boolean.FALSE);
		}else if(SiacDEventoFamTipoEnum.Spesa.equals(siacDEventoFamTipoEnum)){
			dest.setSpesa(Boolean.TRUE);
		}
		return dest;
	}

	@Override
	public SiacDEventoTipo convertTo(TipoEvento src, SiacDEventoTipo dest) {
		
		return dest;
	}



	

}

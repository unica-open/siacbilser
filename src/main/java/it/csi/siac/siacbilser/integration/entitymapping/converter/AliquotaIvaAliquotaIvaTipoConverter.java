/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDIvaAliquotaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAliquota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaAliquotaTipoEnum;
import it.csi.siac.siacfin2ser.model.AliquotaIva;

/**
 * Converter per AliquotaIvaTipo
 * 
 * @author Domenico
 */
@Component
public class AliquotaIvaAliquotaIvaTipoConverter extends DozerConverter<AliquotaIva, SiacTIvaAliquota> {
	
	@Autowired
	private EnumEntityFactory eef;

	public AliquotaIvaAliquotaIvaTipoConverter() {
		super(AliquotaIva.class, SiacTIvaAliquota.class);
	}

	@Override
	public AliquotaIva convertFrom(SiacTIvaAliquota src, AliquotaIva dest) {
		if(src == null || src.getSiacDIvaAliquotaTipo() == null) {
			return dest;
		}
		SiacDIvaAliquotaTipoEnum siacDIvaAliquotaTipoEnum = SiacDIvaAliquotaTipoEnum.byCodice(src.getSiacDIvaAliquotaTipo().getIvaaliquotaTipoCode());
		dest.setAliquotaIvaTipo(siacDIvaAliquotaTipoEnum.getAliquotaIvaTipo());
		
		return dest;
	}

	@Override
	public SiacTIvaAliquota convertTo(AliquotaIva src, SiacTIvaAliquota dest) {
		if(src.getAliquotaIvaTipo() == null) {
			return dest;
		}
		
		SiacDIvaAliquotaTipoEnum siacDIvaAliquotaTipoEnum = SiacDIvaAliquotaTipoEnum.byAliquotaIvaTipo(src.getAliquotaIvaTipo());
		SiacDIvaAliquotaTipo siacDIvaAliquotaTipo = eef.getEntity(siacDIvaAliquotaTipoEnum, dest.getSiacTEnteProprietario().getEnteProprietarioId(),
				SiacDIvaAliquotaTipo.class);
				
		dest.setSiacDIvaAliquotaTipo(siacDIvaAliquotaTipo);
		
		return dest;
		
	}
	
}

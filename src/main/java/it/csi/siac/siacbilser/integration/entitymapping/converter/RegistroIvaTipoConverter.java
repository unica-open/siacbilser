/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDIvaRegistroTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistro;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaRegistroTipoEnum;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;



// TODO: Auto-generated Javadoc
/**
 * Converter per il tipo di esigibilita Iva.
 * 
 * 
 *
 */
@Component
public class RegistroIvaTipoConverter extends DozerConverter<RegistroIva, SiacTIvaRegistro> {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 *  Costruttore vuoto.
	 */
	public RegistroIvaTipoConverter() {
		super(RegistroIva.class, SiacTIvaRegistro.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public RegistroIva convertFrom(SiacTIvaRegistro src, RegistroIva dest) {
		
		if(src.getSiacDIvaRegistroTipo()==null || src.getSiacDIvaRegistroTipo().getDataCancellazione()!=null) {
			return dest;
		}
			
		SiacDIvaRegistroTipoEnum siacDIvaRegistroTipoEnum = SiacDIvaRegistroTipoEnum.byCodice(src.getSiacDIvaRegistroTipo().getIvaregTipoCode());
		TipoRegistroIva tipoRegistroIva = siacDIvaRegistroTipoEnum.getTipoRegistroIva();			 
		dest.setTipoRegistroIva(tipoRegistroIva);
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaRegistro convertTo(RegistroIva src, SiacTIvaRegistro dest) {
		if(src.getTipoRegistroIva()==null){
			return dest;
		}
		
		SiacDIvaRegistroTipoEnum siacDIvaRegistroTipoEnum = SiacDIvaRegistroTipoEnum.byTipoRegistroIva(src.getTipoRegistroIva());
				
		SiacDIvaRegistroTipo siacDIvaRegistroTipo = eef.getEntity(siacDIvaRegistroTipoEnum, dest.getSiacTEnteProprietario().getEnteProprietarioId(),
				SiacDIvaRegistroTipo.class);
		
		dest.setSiacDIvaRegistroTipo(siacDIvaRegistroTipo);
		return dest;
		
	}
	
}

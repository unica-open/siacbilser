/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDIvaEsigibilitaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacDIvaRegistroTipo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaEsigibilitaTipoEnum;
import it.csi.siac.siacfin2ser.model.TipoEsigibilitaIva;



// TODO: Auto-generated Javadoc
/**
 * Converter per il tipo di esigibilita Iva.
 * 
 * 
 *@deprecated 
 */
@Component
@Deprecated
public class RegistroIvaTipoEsigibilitaIvaConverter extends DozerConverter<TipoEsigibilitaIva, SiacDIvaRegistroTipo> {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 *  Costruttore vuoto.
	 */
	public RegistroIvaTipoEsigibilitaIvaConverter() {
		super(TipoEsigibilitaIva.class, SiacDIvaRegistroTipo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoEsigibilitaIva convertFrom(SiacDIvaRegistroTipo src, TipoEsigibilitaIva dest) {
		// Caso base: se non ho legata l'entity, restituisco il null sull'enum
		if(src.getSiacDIvaEsigibilitaTipo()==null) {
			return dest;
		}
			
		if(src.getSiacDIvaEsigibilitaTipo().getDataCancellazione()==null){
			dest = TipoEsigibilitaIva.fromCodice(src.getSiacDIvaEsigibilitaTipo().getIvaesTipoCode());
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDIvaRegistroTipo convertTo( TipoEsigibilitaIva src, SiacDIvaRegistroTipo dest) {
		
		SiacDIvaEsigibilitaTipoEnum siacDIvaEsigibilitaTipoEnum = SiacDIvaEsigibilitaTipoEnum.byTipoEsigibilitaIvaEvenNull(src);
		
		if(siacDIvaEsigibilitaTipoEnum == null) {
			return dest;
		}
		
		SiacDIvaEsigibilitaTipo siacDIvaEsigibilitaTipo = eef.getEntity(siacDIvaEsigibilitaTipoEnum, dest.getSiacTEnteProprietario().getEnteProprietarioId(),
				SiacDIvaEsigibilitaTipo.class);
		
		dest.setSiacDIvaEsigibilitaTipo(siacDIvaEsigibilitaTipo);
		return dest;
		
	}
	
}

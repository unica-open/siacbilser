/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDIvaGruppoTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaGruppo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaGruppoTipoEnum;
import it.csi.siac.siacfin2ser.model.TipoAttivita;



// TODO: Auto-generated Javadoc
/**
 * Converter per il tipo di attivita Iva.
 * 
 * 
 *
 */
@Component
public class GruppoAttivitaIvaTipoAttivitaConverter extends DozerConverter<TipoAttivita, SiacTIvaGruppo> {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 *  Costruttore vuoto.
	 */
	public GruppoAttivitaIvaTipoAttivitaConverter() {
		super(TipoAttivita.class, SiacTIvaGruppo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public TipoAttivita convertFrom(SiacTIvaGruppo src, TipoAttivita dest) {
		// Caso base: se non ho legata l'entity, restituisco il null sull'enum
		if(src.getSiacDIvaGruppoTipo()==null) {
			return dest;
		}
			
		if(src.getSiacDIvaGruppoTipo().getDataCancellazione()==null){
			dest = TipoAttivita.fromCodice(src.getSiacDIvaGruppoTipo().getIvagruTipoCode());
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaGruppo convertTo( TipoAttivita src, SiacTIvaGruppo dest) {
		SiacDIvaGruppoTipoEnum siacDIvaGruppoTipoEnum = SiacDIvaGruppoTipoEnum.byTipoAttivitaEvenNull(src);
		if(siacDIvaGruppoTipoEnum == null) {
			return dest;
		}
		
		SiacDIvaGruppoTipo siacDIvaGruppoTipo = eef.getEntity(siacDIvaGruppoTipoEnum, dest.getSiacTEnteProprietario().getEnteProprietarioId(),
				SiacDIvaGruppoTipo.class);
	
		dest.setSiacDIvaGruppoTipo(siacDIvaGruppoTipo);
		
		return dest;
		
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDProgrammaTipoEnum;
import it.csi.siac.siacbilser.model.Progetto;

// TODO: Auto-generated Javadoc
/**
 * Converter per gli Attributi tra Progetto e SiacTProgramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/02/2014
 *
 */
@Component
public class ProgettoTipoProgettoConverter extends ExtendedDozerConverter<Progetto, SiacTProgramma> {
	
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new progetto attr converter.
	 */
	public ProgettoTipoProgettoConverter() {
		super(Progetto.class, SiacTProgramma.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Progetto convertFrom(SiacTProgramma src, Progetto dest) {
		
		if(src.getSiacDProgrammaTipo() == null) {
			return dest;
		}		
		
		SiacDProgrammaTipoEnum siacDProgrammaStatoEnum = SiacDProgrammaTipoEnum.byCodice(src.getSiacDProgrammaTipo().getProgrammaTipoCode());
		dest.setTipoProgetto(siacDProgrammaStatoEnum.getTipoProgetto());
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTProgramma convertTo(Progetto src, SiacTProgramma dest) {	
		if(src.getTipoProgetto() == null) {
			return dest;
		}
		
		SiacDProgrammaTipoEnum siacDProgrammaTipoEnum = SiacDProgrammaTipoEnum.byTipoProgetto(src.getTipoProgetto());
		SiacDProgrammaTipo siacDProgrammaTipo = eef.getEntity(siacDProgrammaTipoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDProgrammaTipo.class);
		
		dest.setSiacDProgrammaTipo(siacDProgrammaTipo);
		return dest;
	}
	
	

}

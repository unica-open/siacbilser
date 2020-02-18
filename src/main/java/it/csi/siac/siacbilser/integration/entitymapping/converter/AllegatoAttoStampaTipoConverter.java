/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDAttoAllegatoStampaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegatoStampa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAttoAllegatoStampaTipoEnum;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;
import it.csi.siac.siacfin2ser.model.TipoStampaAllegatoAtto;
/**
 * The Class AllegatoAttoStampaTipoConverter.
 */
@Component
public class AllegatoAttoStampaTipoConverter extends ExtendedDozerConverter<AllegatoAttoStampa, SiacTAttoAllegatoStampa> {

	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Instantiates a new stampa allegato Atto Stato iva registro iva converter.
	 */
	public AllegatoAttoStampaTipoConverter() {
		super(AllegatoAttoStampa.class, SiacTAttoAllegatoStampa.class);
	}

	@Override
	public AllegatoAttoStampa convertFrom(SiacTAttoAllegatoStampa src, AllegatoAttoStampa dest) {
		if(src.getSiacDAttoAllegatoStampaTipo()!=null){
			TipoStampaAllegatoAtto tipoStampa = SiacDAttoAllegatoStampaTipoEnum.byCodice(src.getSiacDAttoAllegatoStampaTipo().getAttoalstTipoCode())
					.getTipoStampaAllegatoAtto();
			dest.setTipoStampa(tipoStampa);
		}
        return dest;
	}

	@Override
	public SiacTAttoAllegatoStampa convertTo(AllegatoAttoStampa src, SiacTAttoAllegatoStampa dest) {
		// TODO Auto-generated method stub
		if (src.getTipoStampa()==null){
			return dest;
		}
	
		SiacDAttoAllegatoStampaTipoEnum siacDAttoAllegatoStampaTipoEnum = SiacDAttoAllegatoStampaTipoEnum.byTipoStampa(src.getTipoStampa());
		
		SiacDAttoAllegatoStampaTipo siacDAttoAllegatoStampaTipo = eef.getEntity(siacDAttoAllegatoStampaTipoEnum,dest.getSiacTEnteProprietario().getUid(), SiacDAttoAllegatoStampaTipo.class);
		
		dest.setSiacDAttoAllegatoStampaTipo(siacDAttoAllegatoStampaTipo);
		return dest;	
		
	}
}

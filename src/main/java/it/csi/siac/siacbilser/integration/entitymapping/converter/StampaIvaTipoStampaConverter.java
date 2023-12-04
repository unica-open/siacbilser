/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDIvaStampaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaStampa;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaStampaTipoEnum;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;

// TODO: Auto-generated Javadoc
/**
 * The Class GruppoAttivitaIvaRegistroIvaConverter.
 */
@Component
public class StampaIvaTipoStampaConverter extends ExtendedDozerConverter<StampaIva,SiacTIvaStampa > {

	@Autowired
	private EnumEntityFactory eef;
	/**
	 * Instantiates a new gruppo attivita iva registro iva converter.
	 */
	public StampaIvaTipoStampaConverter() {
		super(StampaIva.class, SiacTIvaStampa.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public StampaIva convertFrom(SiacTIvaStampa src, StampaIva dest) {
		if(src.getSiacDIvaStampaTipo()!=null){
			TipoStampaIva tipoStampaIva = TipoStampaIva.fromCodice(src.getSiacDIvaStampaTipo().getIvastTipoCode());
			dest.setTipoStampaIva(tipoStampaIva);
		}
        return dest;

	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaStampa convertTo(StampaIva src, SiacTIvaStampa dest) {
		
	
		SiacDIvaStampaTipoEnum siacDIvaStampaTipoEnum = SiacDIvaStampaTipoEnum.byCodice(src.getTipoStampaIva().getCodice());
		SiacDIvaStampaTipo siacDIvaStampaTipo = eef.getEntity(siacDIvaStampaTipoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDIvaStampaTipo.class);
		dest.setSiacDIvaStampaTipo(siacDIvaStampaTipo);
		
		return dest;
	}



	

}

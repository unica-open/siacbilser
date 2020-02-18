/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacDModello;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDModelloEnum;
import it.csi.siac.siacfin2ser.model.Causale;
import it.csi.siac.siacfin2ser.model.ModelloCausale;

// TODO: Auto-generated Javadoc
/**
 * The Class CausaleModelloConverter.
 */
@Component
public class CausaleModelloConverter extends DozerConverter<Causale, SiacDCausale > {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/**
	 * Instantiates a new tipo causale converter.
	 */
	public CausaleModelloConverter() {
		super(Causale.class, SiacDCausale.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Causale convertFrom(SiacDCausale src, Causale dest) {
		SiacDModello siacDModello = src.getSiacDModello();
		if(siacDModello != null){
			dest.setModelloCausale(SiacDModelloEnum.byCodice(siacDModello.getModelCode()).getModello());
		}
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacDCausale convertTo(Causale src, SiacDCausale dest) {
		ModelloCausale modelloCausale = src.getModelloCausale();
		if(modelloCausale == null){
			return dest;
		}
		
		SiacDModelloEnum modelloCausaleEnum = SiacDModelloEnum.byModello(modelloCausale);
		SiacDModello siacDModello = eef.getEntity(modelloCausaleEnum, dest.getSiacTEnteProprietario().getUid(), SiacDModello.class);
		dest.setSiacDModello(siacDModello);
	
		return dest;
	}



	

}

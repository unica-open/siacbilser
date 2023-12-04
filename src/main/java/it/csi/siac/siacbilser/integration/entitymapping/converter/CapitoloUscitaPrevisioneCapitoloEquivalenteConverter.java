/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class CapitoloUscitaPrevisioneCapitoloEquivalenteConverter extends ExtendedDozerConverter<CapitoloUscitaPrevisione, SiacTBilElem > {
	
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	public CapitoloUscitaPrevisioneCapitoloEquivalenteConverter() {
		super(CapitoloUscitaPrevisione.class, SiacTBilElem.class);
	}

	@Override
	public CapitoloUscitaPrevisione convertFrom(SiacTBilElem src, CapitoloUscitaPrevisione dest) {
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findCapitoloEquivalenteByIdAndTipoCode(dest.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaGestione.getCodice());
		
		if(bilElems.isEmpty()){
			return dest;
		}
		dest.setUidCapitoloEquivalente(bilElems.get(0).getUid());
		return dest;

	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTBilElem convertTo(CapitoloUscitaPrevisione src, SiacTBilElem dest) {	
		return dest;	
	}
	
}

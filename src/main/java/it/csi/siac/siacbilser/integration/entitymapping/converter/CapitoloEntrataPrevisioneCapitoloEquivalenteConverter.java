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
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;


/**
 * The Class OnereAttrConverter.
 */
@Component
public class CapitoloEntrataPrevisioneCapitoloEquivalenteConverter extends ExtendedDozerConverter<CapitoloEntrataPrevisione, SiacTBilElem > {
	
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	public CapitoloEntrataPrevisioneCapitoloEquivalenteConverter() {
		super(CapitoloEntrataPrevisione.class, SiacTBilElem.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public CapitoloEntrataPrevisione convertFrom(SiacTBilElem src, CapitoloEntrataPrevisione dest) {
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findCapitoloEquivalenteByIdAndTipoCode(dest.getUid(),SiacDBilElemTipoEnum.CapitoloEntrataGestione.getCodice());
		
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
	public SiacTBilElem convertTo(CapitoloEntrataPrevisione src, SiacTBilElem dest) {	
		return dest;	
	}
	
}

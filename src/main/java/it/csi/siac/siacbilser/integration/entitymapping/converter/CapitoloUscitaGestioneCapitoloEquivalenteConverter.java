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
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;


/**
 * The Class CapitoloUscitaGestioneCapitoloEquivalenteConverter
 */
@Component
public class CapitoloUscitaGestioneCapitoloEquivalenteConverter extends ExtendedDozerConverter<CapitoloUscitaGestione, SiacTBilElem > {
	
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	/**
	 * Instantiates a CapitoloEntrataPrevisioneCategoriaConverter.
	 */
	public CapitoloUscitaGestioneCapitoloEquivalenteConverter() {
		super(CapitoloUscitaGestione.class, SiacTBilElem.class);
	}

	@Override
	public CapitoloUscitaGestione convertFrom(SiacTBilElem src, CapitoloUscitaGestione dest) {
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findCapitoloEquivalenteByIdAndTipoCode(dest.getUid(),SiacDBilElemTipoEnum.CapitoloUscitaPrevisione.getCodice());
		
		if(bilElems.isEmpty()){
			return dest;
		}
		dest.setUidCapitoloEquivalente(bilElems.get(0).getUid());
		return dest;

	}

	@Override
	public SiacTBilElem convertTo(CapitoloUscitaGestione src, SiacTBilElem dest) {	
		return dest;	
	}
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.componenteimporticapitolo;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemDetRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetComp;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ImportiCapitoloUPConverter;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;

/**
 * The Class ComponenteImportiCapitoloImportiCapitoloConverter.
 */
@Component
public class ComponenteImportiCapitoloImportiCapitoloConverter extends ExtendedDozerConverter<ComponenteImportiCapitolo, SiacTBilElemDetComp> {
	
	@Autowired private SiacTBilElemDetRepository siacTBilElemDetRepository;
	@Autowired private ImportiCapitoloUPConverter importiCapitoloConverter;
	
	/**
	 * Constructor
	 */
	public ComponenteImportiCapitoloImportiCapitoloConverter() {
		super(ComponenteImportiCapitolo.class, SiacTBilElemDetComp.class);
	}

	@Override
	public ComponenteImportiCapitolo convertFrom(SiacTBilElemDetComp src, ComponenteImportiCapitolo dest) {
		if(src.getSiacTBilElemDet() == null || src.getSiacTBilElemDet().getSiacTBilElem() == null || src.getSiacTBilElemDet().getSiacTPeriodo() == null) {
			return dest;
		}
		
		Class<? extends ImportiCapitolo> importiCapitoloClass = getImportiCapitoloClass(src);
		if(importiCapitoloClass == null) {
			return dest;
		}
		
		ImportiCapitolo importiCapitolo = importiCapitoloConverter.toImportiCapitolo(
				src.getSiacTBilElemDet().getSiacTBilElem().getElemId(),
				Integer.valueOf(src.getSiacTBilElemDet().getSiacTPeriodo().getAnno()),
				new HashSet<ImportiCapitoloEnum>(),
				false);
		dest.setImportiCapitolo(importiCapitolo);
		return dest;
	}

	private Class<? extends ImportiCapitolo> getImportiCapitoloClass(SiacTBilElemDetComp src) {
		return SiacDBilElemTipoEnum.byCodice(src.getSiacTBilElemDet().getSiacTBilElem().getSiacDBilElemTipo().getElemTipoCode()).getTipoCapitolo().getImportiCapitoloClass();
	}
	

	@Override
	public SiacTBilElemDetComp convertTo(ComponenteImportiCapitolo src, SiacTBilElemDetComp dest) {
		Capitolo<?, ?> capitolo = Utility.CTL.get();
		if(src.getImportiCapitolo() == null || capitolo == null) {
			return dest;
		}
		
		// FIXME [ComponenteImportiCapitolo]: solo STANZIAMENTO
		List<SiacTBilElemDet> siacTBilElemDet = siacTBilElemDetRepository.findBilElemDetsByBilElemIdAndAnnoAndTipo(
				capitolo.getUid(),
				src.getImportiCapitolo().getAnnoCompetenza().toString(),
				SiacDBilElemDetTipoEnum.Stanziamento.getCodice());
		dest.setSiacTBilElemDet(siacTBilElemDet != null && !siacTBilElemDet.isEmpty() ? siacTBilElemDet.get(0) : null);
		
		return dest;
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.componenteimporticapitolo;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.ComponenteImportiCapitoloDad;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElemDetComp;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;

/**
 * The Class ComponenteImportiCapitoloHasVariazioniConverter.
 */
@Component
public class ComponenteImportiCapitoloHasVariazioniConverter extends DozerConverter<ComponenteImportiCapitolo, SiacTBilElemDetComp> {

	@Autowired private ComponenteImportiCapitoloDad componenteImportiCapitoloDad;
	
	/**
	 * Constructor
	 */
	public ComponenteImportiCapitoloHasVariazioniConverter() {
		super(ComponenteImportiCapitolo.class, SiacTBilElemDetComp.class);
	}

	@Override
	public ComponenteImportiCapitolo convertFrom(SiacTBilElemDetComp src, ComponenteImportiCapitolo dest) {
		Long variazioniByComponente = componenteImportiCapitoloDad.countVariazioniByComponente(src.getUid());
		dest.setHasVariazioni(Boolean.valueOf(variazioniByComponente != null && variazioniByComponente.longValue() > 0L));
		return dest;
	}

	@Override
	public SiacTBilElemDetComp convertTo(ComponenteImportiCapitolo src, SiacTBilElemDetComp dest) {
		// Don't do anything
		return dest;
	}

}

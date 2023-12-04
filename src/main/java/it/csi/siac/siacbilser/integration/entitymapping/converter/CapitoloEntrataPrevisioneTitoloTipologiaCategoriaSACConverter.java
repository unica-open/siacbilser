/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataPrevisioneDad;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;

/**
 * The Class CapitoloEntrataPrevisioneTitoloTipologiaCategoriaConverter.
 */
@Component
public class CapitoloEntrataPrevisioneTitoloTipologiaCategoriaSACConverter extends ExtendedDozerConverter<CapitoloEntrataPrevisione, SiacTBilElem> {
	
	@Autowired private CapitoloEntrataPrevisioneDad capitoloEntrataPrevisioneDad;
	
	/**
	 * Instantiates a new attributi bilancio attr converter.
	 */
	public CapitoloEntrataPrevisioneTitoloTipologiaCategoriaSACConverter() {
		super(CapitoloEntrataPrevisione.class, SiacTBilElem.class);
	}

	@Override
	public CapitoloEntrataPrevisione convertFrom(SiacTBilElem src, CapitoloEntrataPrevisione dest) {
		// Verificare eventuali ottimizzazioni
		dest.setStrutturaAmministrativoContabile(capitoloEntrataPrevisioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(dest));
		dest.setCategoriaTipologiaTitolo(capitoloEntrataPrevisioneDad.ricercaClassificatoreCategoriaTipologiaTitolo(dest));
		dest.setTipologiaTitolo(capitoloEntrataPrevisioneDad.ricercaClassificatoreTipologiaTitolo(dest, dest.getCategoriaTipologiaTitolo()));
		dest.setTitoloEntrata(capitoloEntrataPrevisioneDad.ricercaClassificatoreTitoloEntrata(dest, dest.getTipologiaTitolo()));
		return dest;
	}
	
	
	@Override
	public SiacTBilElem convertTo(CapitoloEntrataPrevisione src, SiacTBilElem dest) {
		// Solo lettura?
		return dest;
	}
	
}

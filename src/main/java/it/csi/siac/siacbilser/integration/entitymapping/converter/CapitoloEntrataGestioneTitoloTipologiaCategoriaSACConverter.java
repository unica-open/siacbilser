/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.CapitoloEntrataGestioneDad;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;

/**
 * The Class CapitoloEntrataGestioneTitoloTipologiaCategoriaConverter.
 */
@Component
public class CapitoloEntrataGestioneTitoloTipologiaCategoriaSACConverter extends ExtendedDozerConverter<CapitoloEntrataGestione, SiacTBilElem> {
	
	@Autowired private CapitoloEntrataGestioneDad capitoloEntrataGestioneDad;
	
	/**
	 * Instantiates a new attributi bilancio attr converter.
	 */
	public CapitoloEntrataGestioneTitoloTipologiaCategoriaSACConverter() {
		super(CapitoloEntrataGestione.class, SiacTBilElem.class);
	}

	@Override
	public CapitoloEntrataGestione convertFrom(SiacTBilElem src, CapitoloEntrataGestione dest) {
		// Verificare eventuali ottimizzazioni
		dest.setStrutturaAmministrativoContabile(capitoloEntrataGestioneDad.ricercaClassificatoreStrutturaAmministrativaContabileCapitolo(dest));
		dest.setCategoriaTipologiaTitolo(capitoloEntrataGestioneDad.ricercaClassificatoreCategoriaTipologiaTitolo(dest));
		dest.setTipologiaTitolo(capitoloEntrataGestioneDad.ricercaClassificatoreTipologiaTitolo(dest, dest.getCategoriaTipologiaTitolo()));
		dest.setTitoloEntrata(capitoloEntrataGestioneDad.ricercaClassificatoreTitoloEntrata(dest, dest.getTipologiaTitolo()));
		return dest;
	}
	
	
	@Override
	public SiacTBilElem convertTo(CapitoloEntrataGestione src, SiacTBilElem dest) {
		// Solo lettura?
		return dest;
	}
	
}

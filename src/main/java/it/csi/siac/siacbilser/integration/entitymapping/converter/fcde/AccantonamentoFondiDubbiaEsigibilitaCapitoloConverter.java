/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.fcde;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataPrevisioneModelDetail;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaCapitoloConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaCapitoloConverter extends AccantonamentoFondiDubbiaEsigibilitaCapitoloBaseConverter<CapitoloEntrataPrevisione, AccantonamentoFondiDubbiaEsigibilita> {
	
	/**
	 * Instantiates a new accantonamento fondi dubbia esigibilita capitolo converter.
	 */
	public AccantonamentoFondiDubbiaEsigibilitaCapitoloConverter() {
		super(AccantonamentoFondiDubbiaEsigibilita.class);
	}

	@Override
	protected CapitoloEntrataPrevisione instantiateCapitolo(SiacTBilElem siacTBilElem) {
		CapitoloEntrataPrevisione capitolo = new CapitoloEntrataPrevisione();
		mapNotNull(siacTBilElem, capitolo, BilMapId.SiacTBilElem_Capitolo_Base, Converters.byModelDetails(Utility.MDTL.byModelDetailClass(CapitoloEntrataPrevisioneModelDetail.class)));
		return capitolo;
	}

	@Override
	protected void setCapitolo(AccantonamentoFondiDubbiaEsigibilita dest, CapitoloEntrataPrevisione capitolo) {
		dest.setCapitolo(capitolo);
	}

	@Override
	protected CapitoloEntrataPrevisione getCapitolo(AccantonamentoFondiDubbiaEsigibilita src) {
		return src.getCapitolo();
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.CapitoloEntrataPrevisione;

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
	protected CapitoloEntrataPrevisione instantiateCapitolo(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig) {
		return new CapitoloEntrataPrevisione();
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

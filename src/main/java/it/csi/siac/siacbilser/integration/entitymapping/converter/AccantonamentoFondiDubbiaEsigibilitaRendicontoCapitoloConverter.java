/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.model.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaRendicontoCapitoloConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaRendicontoCapitoloConverter extends AccantonamentoFondiDubbiaEsigibilitaCapitoloBaseConverter<CapitoloEntrataGestione, AccantonamentoFondiDubbiaEsigibilitaRendiconto> {
	
	/**
	 * Instantiates a new accantonamento fondi dubbia esigibilita capitolo converter.
	 */
	public AccantonamentoFondiDubbiaEsigibilitaRendicontoCapitoloConverter() {
		super(AccantonamentoFondiDubbiaEsigibilitaRendiconto.class);
	}

	@Override
	protected CapitoloEntrataGestione instantiateCapitolo(SiacRBilElemAccFondiDubbiaEsig siacRBilElemAccFondiDubbiaEsig) {
		return new CapitoloEntrataGestione();
	}

	@Override
	protected void setCapitolo(AccantonamentoFondiDubbiaEsigibilitaRendiconto dest, CapitoloEntrataGestione capitolo) {
		dest.setCapitolo(capitolo);
	}

	@Override
	protected CapitoloEntrataGestione getCapitolo(AccantonamentoFondiDubbiaEsigibilitaRendiconto src) {
		return src.getCapitolo();
	}

}

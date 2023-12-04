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
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacfin2ser.model.CapitoloEntrataGestioneModelDetail;

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
	protected CapitoloEntrataGestione instantiateCapitolo(SiacTBilElem siacTBilElem) {
		CapitoloEntrataGestione capitolo = new CapitoloEntrataGestione();
		mapNotNull(siacTBilElem, capitolo, BilMapId.SiacTBilElem_Capitolo_Base, Converters.byModelDetails(Utility.MDTL.byModelDetailClass(CapitoloEntrataGestioneModelDetail.class)));
		return capitolo;
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

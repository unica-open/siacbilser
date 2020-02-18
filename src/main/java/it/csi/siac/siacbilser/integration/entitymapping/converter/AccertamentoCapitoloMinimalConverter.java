/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMovgestBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * The Class AccertamentoCapitoloMinimalConverter.
 */
@Component
public class AccertamentoCapitoloMinimalConverter extends ExtendedDozerConverter<Accertamento, SiacTMovgest> {

	/**
	 * Instantiates a new movimento gestione (testata / sub) - capitolo minimal converter.
	 */
	public AccertamentoCapitoloMinimalConverter() {
		super(Accertamento.class, SiacTMovgest.class);
	}
	@Override
	public Accertamento convertFrom(SiacTMovgest src, Accertamento dest) {
		List<SiacRMovgestBilElem> siacRMovgestBilElems = src.getSiacRMovgestBilElems() ;
		if(siacRMovgestBilElems == null || siacRMovgestBilElems.isEmpty() ) {
			return dest;
		}
		for (SiacRMovgestBilElem siacRMovgestBilElem : siacRMovgestBilElems) {
			if(siacRMovgestBilElem.getDataCancellazione() != null){
				continue;
			}
			SiacTBilElem siacTBilElem = siacRMovgestBilElem.getSiacTBilElem();
			CapitoloEntrataGestione cap = map(siacTBilElem, CapitoloEntrataGestione.class, BilMapId.SiacTBilElem_CapitoloEntrataGestione_ModelDetail);
			dest.setChiaveCapitoloEntrataGestione(cap.getUid());
			dest.setCapitoloEntrataGestione(cap);
			return dest;
		}
		
		return dest;
	}
	
	

	@Override
	public SiacTMovgest convertTo(Accertamento src, SiacTMovgest dest) {
		return dest;
	}

}

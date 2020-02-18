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
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * The Class ImpegnoCapitoloMinimalConverter.
 */
@Component
public class ImpegnoCapitoloMinimalConverter extends ExtendedDozerConverter<Impegno, SiacTMovgest> {

	/**
	 * Instantiates a new impegno - capitolo minimal converter.
	 */
	public ImpegnoCapitoloMinimalConverter() {
		super(Impegno.class, SiacTMovgest.class);
	}
	@Override
	public Impegno convertFrom(SiacTMovgest src, Impegno dest) {
		List<SiacRMovgestBilElem> siacRMovgestBilElems = src.getSiacRMovgestBilElems() ;
		if(siacRMovgestBilElems == null || siacRMovgestBilElems.isEmpty() ) {
			return dest;
		}
		for (SiacRMovgestBilElem siacRMovgestBilElem : siacRMovgestBilElems) {
			if(siacRMovgestBilElem.getDataCancellazione() != null){
				continue;
			}
			SiacTBilElem siacTBilElem = siacRMovgestBilElem.getSiacTBilElem();
			CapitoloUscitaGestione cap = map(siacTBilElem, CapitoloUscitaGestione.class, BilMapId.SiacTBilElem_CapitoloUscitaGestione_ModelDetail);
			dest.setChiaveCapitoloUscitaGestione(cap.getUid());
			dest.setCapitoloUscitaGestione(cap);
			return dest;
		}
		
		return dest;
	}
	
	

	@Override
	public SiacTMovgest convertTo(Impegno src, SiacTMovgest dest) {
		return dest;
	}

}

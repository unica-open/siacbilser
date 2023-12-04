/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDVincoloRisorseVincolate;
import it.csi.siac.siacbilser.integration.entity.SiacRVincoloRisorseVincolate;
import it.csi.siac.siacbilser.integration.entity.SiacTVincolo;
import it.csi.siac.siacbilser.model.RisorsaVincolata;
import it.csi.siac.siacbilser.model.Vincolo;

@Component
public class VincoloRisorseVincolateConverter extends ExtendedDozerConverter<Vincolo, SiacTVincolo> {

	protected VincoloRisorseVincolateConverter() {
		super(Vincolo.class, SiacTVincolo.class);
	}

	@Override
	public Vincolo convertFrom(SiacTVincolo src, Vincolo dest) {
		if(src.getSiacRVincoloRisorseVincolates() == null) {
			return dest;
		}
		
		for(SiacRVincoloRisorseVincolate siacRVincoloRisorsaVincolata : src.getSiacRVincoloRisorseVincolates()) {
			if(siacRVincoloRisorsaVincolata.getDataCancellazione() == null) {

				RisorsaVincolata risorsaVincolata = new RisorsaVincolata();
				risorsaVincolata.setUid(siacRVincoloRisorsaVincolata.getSiacDVincoloRisorseVincolate().getUid().intValue());
				risorsaVincolata.setCodice(siacRVincoloRisorsaVincolata.getSiacDVincoloRisorseVincolate().getVincoloRisorseVincolateCode());
				risorsaVincolata.setDescrizione(siacRVincoloRisorsaVincolata.getSiacDVincoloRisorseVincolate().getVincoloRisorseVincolateDesc());
				
				dest.setRisorsaVincolata(risorsaVincolata);
				break;
			}
		}
		
		return dest;
	}

	@Override
	public SiacTVincolo convertTo(Vincolo src, SiacTVincolo dest) {
		if(src.getRisorsaVincolata() == null || src.getRisorsaVincolata().getUid() == 0) {
			return dest;
		}
		
		SiacRVincoloRisorseVincolate siacRVincoloRisorsaVincolata = new SiacRVincoloRisorseVincolate();
		siacRVincoloRisorsaVincolata.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRVincoloRisorsaVincolata.setLoginOperazione(dest.getLoginOperazione());
		siacRVincoloRisorsaVincolata.setSiacTVincolo(dest);
		
		SiacDVincoloRisorseVincolate siacDVincoloRisorseVincolate = new SiacDVincoloRisorseVincolate();
		siacDVincoloRisorseVincolate.setUid(src.getRisorsaVincolata().getUid());
		siacRVincoloRisorsaVincolata.setSiacDVincoloRisorseVincolate(siacDVincoloRisorseVincolate);
		
		List<SiacRVincoloRisorseVincolate> siacRVincoloRisorseVincolates = new ArrayList<SiacRVincoloRisorseVincolate>();
		siacRVincoloRisorseVincolates.add(siacRVincoloRisorsaVincolata);
		dest.setSiacRVincoloRisorseVincolates(siacRVincoloRisorseVincolates);
		
		return dest;
	}

}

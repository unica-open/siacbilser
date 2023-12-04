/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCronop;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.Progetto;


@Component
public class ProgettoCronoprogrammiConverter extends ExtendedDozerConverter<Progetto, SiacTProgramma> {
	
	public ProgettoCronoprogrammiConverter() {
		super(Progetto.class, SiacTProgramma.class);
	}

	@Override
	public Progetto convertFrom(SiacTProgramma src, Progetto dest) {

		dest.setCronoprogrammi(new ArrayList<Cronoprogramma>());

		for (SiacTCronop siacTCronop : src.getSiacTCronops()) {
			if (siacTCronop.getDataCancellazione() == null && siacTCronop.isDataValiditaCompresa(new Date())) {  
				dest.getCronoprogrammi()
						.add(map(siacTCronop, Cronoprogramma.class, BilMapId.SiacTCronop_Cronoprogramma));
			}
		}

		return dest;
	}
	
	@Override
	public SiacTProgramma convertTo(Progetto src, SiacTProgramma dest) {
		return dest;
	}

}

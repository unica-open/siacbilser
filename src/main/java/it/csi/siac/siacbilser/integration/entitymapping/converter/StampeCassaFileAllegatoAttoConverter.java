/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampa;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;

@Component
public class StampeCassaFileAllegatoAttoConverter extends ExtendedDozerConverter<StampeCassaFile,SiacTCassaEconStampa> {

	public StampeCassaFileAllegatoAttoConverter() {
		super(StampeCassaFile.class,SiacTCassaEconStampa.class);
	}

	@Override
	public StampeCassaFile convertFrom(SiacTCassaEconStampa src, StampeCassaFile dest) {
		if(src.getSiacTAttoAllegato() != null) {
			AllegatoAtto allegatoAtto = map(src.getSiacTAttoAllegato(), AllegatoAtto.class, BilMapId.SiacTAttoAllegato_AllegatoAtto_Base);
			dest.setAllegatoAtto(allegatoAtto);
		}
		return dest;
	}

	@Override
	public SiacTCassaEconStampa convertTo(StampeCassaFile src,	SiacTCassaEconStampa dest) {
		if(src.getAllegatoAtto() != null && src.getAllegatoAtto().getUid() != 0) {
			SiacTAttoAllegato siacTAttoAllegato = new SiacTAttoAllegato();
			siacTAttoAllegato.setUid(src.getAllegatoAtto().getUid());
			dest.setSiacTAttoAllegato(siacTAttoAllegato);
		}
		return dest;
	}

}

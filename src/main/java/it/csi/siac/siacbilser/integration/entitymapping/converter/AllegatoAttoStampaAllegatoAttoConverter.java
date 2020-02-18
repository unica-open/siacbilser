/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegatoStampa;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoStampa;

@Component
public class AllegatoAttoStampaAllegatoAttoConverter extends ExtendedDozerConverter<AllegatoAttoStampa, SiacTAttoAllegatoStampa> {

	/**
	 * Instantiates a nuw
	 * **/
	protected AllegatoAttoStampaAllegatoAttoConverter() {
		
		super(AllegatoAttoStampa.class, SiacTAttoAllegatoStampa.class);		
	}

	@Override
	public AllegatoAttoStampa convertFrom(SiacTAttoAllegatoStampa src, AllegatoAttoStampa dest) {
		if(src.getSiacTAttoAllegato()!=null && src.getSiacTAttoAllegato().getDataCancellazione()== null){
			AllegatoAtto allegatoAtto= new AllegatoAtto();
			AttoAmministrativo attoAmministrativo = new AttoAmministrativo();
			map(src.getSiacTAttoAllegato().getSiacTAttoAmm(), attoAmministrativo , BilMapId.SiacTAttoAmm_AttoAmministrativo);
			allegatoAtto.setAttoAmministrativo(attoAmministrativo);
			dest.setAllegatoAtto(allegatoAtto);			
		}
		return dest;
	}

	@Override
	public SiacTAttoAllegatoStampa convertTo(AllegatoAttoStampa src, SiacTAttoAllegatoStampa dest) {
		if(src.getAllegatoAtto()!= null){
			SiacTAttoAllegato siacTAttoAllegato = new SiacTAttoAllegato();
			siacTAttoAllegato.setUid(src.getAllegatoAtto().getUid());
			dest.setSiacTAttoAllegato(siacTAttoAllegato);
		}
		return dest;
	}

}

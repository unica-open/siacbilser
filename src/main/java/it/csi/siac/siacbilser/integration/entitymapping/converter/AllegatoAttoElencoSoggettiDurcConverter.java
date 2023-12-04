/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTSoggettoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.utility.DozerMapHelper;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

@Component
public class AllegatoAttoElencoSoggettiDurcConverter extends ExtendedDozerConverter<AllegatoAtto, SiacTAttoAllegato> {
	
	@Autowired
	private SiacTSoggettoRepository siacTSoggettoRepository;
	
	public AllegatoAttoElencoSoggettiDurcConverter() {
		super(AllegatoAtto.class, SiacTAttoAllegato.class);
	}

	@Override
	public AllegatoAtto convertFrom(SiacTAttoAllegato siacTAttoAllegato, AllegatoAtto allegatoAtto) {
		
		List<SiacTSoggetto> siacTSoggettos = siacTSoggettoRepository.findByAttoalId(siacTAttoAllegato.getUid());
		
		allegatoAtto.setElencoSoggettiDurc(DozerMapHelper.convertiLista(getMapper(), siacTSoggettos, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto_DatiDurc));
				
		return allegatoAtto;
	}

	@Override
	public SiacTAttoAllegato convertTo(AllegatoAtto src, SiacTAttoAllegato dest) {
		return dest;
	}
}

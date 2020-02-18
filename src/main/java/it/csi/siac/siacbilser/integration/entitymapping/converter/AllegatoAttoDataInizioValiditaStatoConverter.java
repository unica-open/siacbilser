/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;
import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacRAttoAllegatoStatoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;

 /**
 * The Class AllegatoAttoDataInizioValiditaStatoConverter.
 */
@Component
public class AllegatoAttoDataInizioValiditaStatoConverter extends DozerConverter<AllegatoAtto, SiacTAttoAllegato> {
	
	
	/** The siac r doc stato repository. */
    @Autowired
	private SiacRAttoAllegatoStatoRepository siacRAttoAllegatoStatoRepository;
	
	
	/**
	 * Instantiates a new documento spesa data inizio validita stato converter.
	 */
	public AllegatoAttoDataInizioValiditaStatoConverter() {
		super(AllegatoAtto.class, SiacTAttoAllegato.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AllegatoAtto convertFrom(SiacTAttoAllegato src, AllegatoAtto dest) {

		List<SiacRAttoAllegatoStato> siacRAttoAllegatoStatos = siacRAttoAllegatoStatoRepository.findAttoStatoByAttoalIdOrderedyByDataInizioValidita(src.getAttoalId());
		Date date = null;
		Integer statoId = null;
		

		for (SiacRAttoAllegatoStato r : siacRAttoAllegatoStatos) {
			Integer statoIdNew = r.getSiacDAttoAllegatoStato().getUid();
			if (!statoIdNew.equals(statoId)) {
				statoId = statoIdNew;
				date = r.getDataInizioValidita();
			}
		}

		dest.setDataInizioValiditaStato(date);

		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTAttoAllegato convertTo(AllegatoAtto src, SiacTAttoAllegato dest) {
		return dest;
	}


}

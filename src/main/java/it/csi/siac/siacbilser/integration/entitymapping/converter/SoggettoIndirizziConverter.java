/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTIndirizzoSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class SoggettoMatricolaConverter.
 */
@Component
public class SoggettoIndirizziConverter extends ExtendedDozerConverter<Soggetto, SiacTSoggetto> {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public SoggettoIndirizziConverter() {
		super(Soggetto.class, SiacTSoggetto.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Soggetto convertFrom(SiacTSoggetto src, Soggetto dest) {
		if(src.getSiacTIndirizzoSoggettos() == null){
			return dest;
		}
		List<IndirizzoSoggetto> indirizzi = new ArrayList<IndirizzoSoggetto>();
		for(SiacTIndirizzoSoggetto siacTIndirizzoSoggetto : src.getSiacTIndirizzoSoggettos()){
			if(siacTIndirizzoSoggetto.getDataCancellazione() == null && siacTIndirizzoSoggetto.getDataFineValidita() == null){
				IndirizzoSoggetto indirizzo = map(siacTIndirizzoSoggetto, IndirizzoSoggetto.class, BilMapId.SiacTIndirizzoSoggetto_IndirizzoSoggetto);
				indirizzi.add(indirizzo);
			}
		}
		dest.setIndirizzi(indirizzi);
		return dest;
	}

	@Override
	public SiacTSoggetto convertTo(Soggetto src, SiacTSoggetto dest) {
		return dest;
	}





	

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRSoggettoAttr;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class SoggettoMatricolaConverter.
 */
@Component
public class SoggettoMatricolaConverter extends ExtendedDozerConverter<Soggetto, SiacTSoggetto > {
	

	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public SoggettoMatricolaConverter() {
		super(Soggetto.class, SiacTSoggetto.class);
	}

	@Override
	public Soggetto convertFrom(SiacTSoggetto src, Soggetto dest) {
		if(src.getSiacRSoggettoAttrs() == null){
			return dest;
		}
		for(SiacRSoggettoAttr siacRSoggettoAttr : src.getSiacRSoggettoAttrs()){
			if(siacRSoggettoAttr.getDataCancellazione() == null && SiacTAttrEnum.Matricola.getCodice().equalsIgnoreCase(siacRSoggettoAttr.getSiacTAttr().getAttrCode())){
				String matricola = siacRSoggettoAttr.getTesto();
				dest.setMatricola(matricola);
			}
		}
		return dest;
	}

	@Override
	public SiacTSoggetto convertTo(Soggetto src, SiacTSoggetto dest) {
		return dest;
	}





	

}

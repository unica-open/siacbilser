/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacROrdinativoSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativo;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
/**
 * The Class OrdinativoSoggettoConverter.
 */
@Component
public class OrdinativoSoggettoConverter extends ExtendedDozerConverter<Ordinativo, SiacTOrdinativo >{

	protected OrdinativoSoggettoConverter() {
		super(Ordinativo.class, SiacTOrdinativo.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Ordinativo convertFrom(SiacTOrdinativo src, Ordinativo dest) {
		if(src.getSiacROrdinativoSoggettos() == null){
			return dest;
		}
		for(SiacROrdinativoSoggetto siacROrdinativoSoggetto : src.getSiacROrdinativoSoggettos()){
			if(siacROrdinativoSoggetto.getDataCancellazione() == null){
				Soggetto soggetto = new Soggetto();
				map(siacROrdinativoSoggetto.getSiacTSoggetto(), soggetto, BilMapId.SiacTSoggetto_Soggetto);
				dest.setSoggetto(soggetto);
			}
		}
		return dest;
	}

	@Override
	public SiacTOrdinativo convertTo(Ordinativo src, SiacTOrdinativo dest) {

		return dest;
	}

}

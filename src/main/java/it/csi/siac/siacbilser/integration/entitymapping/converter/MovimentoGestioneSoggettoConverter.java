/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsSog;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class MovimentoGestioneSoggettoConverter.
 */
@Component
public class MovimentoGestioneSoggettoConverter extends ExtendedDozerConverter<MovimentoGestione, SiacTMovgestT> {

	/**
	 * Instantiates a new movimento gestione - soggetto converter.
	 */
	public MovimentoGestioneSoggettoConverter() {
		super(MovimentoGestione.class, SiacTMovgestT.class);
	}
	@Override
	public MovimentoGestione convertFrom(SiacTMovgestT src, MovimentoGestione dest) {
		if(src.getSiacRMovgestTsSogs() == null){
			return dest;
		}
		for(SiacRMovgestTsSog siacRMovgestTsSog : src.getSiacRMovgestTsSogs()){
			if(siacRMovgestTsSog.getDataCancellazione() == null){
				Soggetto soggetto = new Soggetto();
				map(siacRMovgestTsSog.getSiacTSoggetto(), soggetto, BilMapId.SiacTSoggetto_Soggetto);
				dest.setSoggetto(soggetto);
			}
		}
		
		return dest;
	}

	@Override
	public SiacTMovgestT convertTo(MovimentoGestione src, SiacTMovgestT dest) {
		return dest;
	}

}

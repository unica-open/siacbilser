/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsSog;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class ImpegnoSoggettoConverter.
 */
@Component
public class AccertamentoSoggettoConverter extends ExtendedDozerConverter<Accertamento, SiacTMovgest > {

	/**
	 * Instantiates a new impegno - soggetto converter.
	 */
	public AccertamentoSoggettoConverter() {
		super(Accertamento.class, SiacTMovgest.class);
	}
	@Override
	public Accertamento convertFrom(SiacTMovgest src, Accertamento dest) {
		if(src.getSiacTMovgestTs() == null){
			return dest;
		}
		for(SiacTMovgestT siacTMovgestT : src.getSiacTMovgestTs()){
			if(siacTMovgestT.getDataCancellazione() == null&& "T".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) {
				if(siacTMovgestT.getSiacRMovgestTsSogs() == null){
					return dest;
				}
				for(SiacRMovgestTsSog siacRMovgestTsSog : siacTMovgestT.getSiacRMovgestTsSogs()){
					if(siacRMovgestTsSog.getDataCancellazione() == null){
						Soggetto soggetto = new Soggetto();
						map(siacRMovgestTsSog.getSiacTSoggetto(), soggetto, BilMapId.SiacTSoggetto_Soggetto);
						dest.setSoggetto(soggetto);
					}
				}
			}
		}
		
		
		return dest;
	}

	@Override
	public SiacTMovgest convertTo(Accertamento src, SiacTMovgest dest) {
		
		return dest;
	}

}

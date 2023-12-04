/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTMovgestTsDetRepository;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

@Component
public class VincoloImpegnoImportoAccertamentoConverter 
	extends BaseFinDozerConverter<VincoloImpegno, SiacRMovgestTsFin> {

	@Autowired
	SiacTMovgestTsDetRepository siacTMovgestTsDetRepository;
	
	public VincoloImpegnoImportoAccertamentoConverter() {
		super(VincoloImpegno.class, SiacRMovgestTsFin.class);
	}

	@Override
	public SiacRMovgestTsFin convertTo(VincoloImpegno source, SiacRMovgestTsFin destination) {
		if(source == null || destination == null) return null;
		return destination;
	}

	@Override
	public VincoloImpegno convertFrom(SiacRMovgestTsFin source, VincoloImpegno destination) {

		if(source != null && source.getSiacTMovgestTsA() != null
				&& source.getSiacTMovgestTsA().getMovgestTsId() != 0
				&& source.getSiacTEnteProprietario() != null
				&& source.getSiacTEnteProprietario().getEnteProprietarioId() != 0
				&& destination.getAccertamento() != null) {
			
			destination.getAccertamento().setImportoAttuale(siacTMovgestTsDetRepository
					.findImporto(source.getSiacTEnteProprietario().getEnteProprietarioId(), 
						SiacDMovgestTsDetTipoEnum.Attuale.getCodice(),
						source.getSiacTMovgestTsA().getMovgestTsId()
					)
				);
		}
		
		return destination;
	}

}

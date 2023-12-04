/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

@Component
public class VincoloImpegnoBilancioAccertamentoConverter 
	extends BaseFinDozerConverter<VincoloImpegno, SiacRMovgestTsFin> {

	public VincoloImpegnoBilancioAccertamentoConverter() {
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
				&& source.getSiacTMovgestTsA().getSiacTMovgest() != null
				&& destination.getAccertamento() != null) {
			
			mapNotNull(source, destination, FinMapId.SiacRMovgestTsFin_VincoloImpegno_Accertamento_Bilancio);
		}
		
		return destination;
	}
}

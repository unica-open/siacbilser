/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;

@Component
public class VincoloAccertamentoBilancioImpegnoConverter
	extends BaseFinDozerConverter<VincoloAccertamento, SiacRMovgestTsFin>{

	public VincoloAccertamentoBilancioImpegnoConverter() {
		super(VincoloAccertamento.class, SiacRMovgestTsFin.class);
	}

	@Override
	public SiacRMovgestTsFin convertTo(VincoloAccertamento source, SiacRMovgestTsFin destination) {
		if(source == null || destination == null) return null;
		return destination;
	}

	@Override
	public VincoloAccertamento convertFrom(SiacRMovgestTsFin source, VincoloAccertamento destination) {
		
		if(source != null && source.getSiacTMovgestTsA() != null 
				&& source.getSiacTMovgestTsA().getSiacTMovgest() != null
				&& destination.getImpegno() != null) {
			
			mapNotNull(source, destination, FinMapId.SiacRMovgestTsFin_VincoloAccertamento_Impegno_Bilancio);
		}
		
		return destination;
	}

}

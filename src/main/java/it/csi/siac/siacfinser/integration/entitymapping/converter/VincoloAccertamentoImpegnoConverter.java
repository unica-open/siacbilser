/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.model.movgest.VincoloAccertamento;

@Component
public class VincoloAccertamentoImpegnoConverter extends ExtendedDozerConverter<VincoloAccertamento, SiacRMovgestTsFin> {

	public VincoloAccertamentoImpegnoConverter() {
		super(VincoloAccertamento.class, SiacRMovgestTsFin.class);
	}

	@Override
	public SiacRMovgestTsFin convertTo(VincoloAccertamento source, SiacRMovgestTsFin destination) {
		if(source == null || destination == null) return null;
		return destination;
	}

	@Override
	public VincoloAccertamento convertFrom(SiacRMovgestTsFin source, VincoloAccertamento destination) {
		
		if(source != null && source.getSiacTMovgestTsB() != null) {
			mapNotNull(source, destination, FinMapId.SiacRMovgestTsFin_VincoloAccertamento_Impegno_Base);
		}
		
		return destination;
	}
	
}

/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsFin;
import it.csi.siac.siacfinser.model.movgest.VincoloImpegno;

@Component
public class VincoloImpegnoDiCuiPendingConverter extends BaseFinDozerConverter<VincoloImpegno, SiacRMovgestTsFin> {

	public VincoloImpegnoDiCuiPendingConverter() {
		super(VincoloImpegno.class, SiacRMovgestTsFin.class);
	}

	@Override
	public SiacRMovgestTsFin convertTo(VincoloImpegno source, SiacRMovgestTsFin destination) {
		if(source == null || destination == null) return null;
		return destination;
	}

	@Override
	public VincoloImpegno convertFrom(SiacRMovgestTsFin source, VincoloImpegno destination) {
		
		if(source != null) {
			
		}
		
		return destination;
	}

}

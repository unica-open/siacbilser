/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.model.Impegno;

@Component
public class BilancioImpegnoConverter 
	extends BaseFinDozerConverter<Impegno, SiacTMovgest> {

	public BilancioImpegnoConverter() {
		super(Impegno.class, SiacTMovgest.class);
	}

	@Override
	public SiacTMovgest convertTo(Impegno source, SiacTMovgest destination) {
		if(source == null || destination == null) return null;
		return destination;
	}

	@Override
	public Impegno convertFrom(SiacTMovgest source, Impegno destination) {

		if(source != null) {
			destination.setBilancio(mapNotNull(source.getSiacTBil(), Bilancio.class, FinMapId.SiacTBil_Bilancio));
		}
		
		return destination;
	}

}

/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.model.Accertamento;

@Component
public class BilancioAccertamentoConverter 
	extends BaseFinDozerConverter<Accertamento, SiacTMovgest> {

	public BilancioAccertamentoConverter() {
		super(Accertamento.class, SiacTMovgest.class);
	}

	@Override
	public SiacTMovgest convertTo(Accertamento source, SiacTMovgest destination) {
		if(source == null || destination == null) return null;
		return destination;
	}

	@Override
	public Accertamento convertFrom(SiacTMovgest source, Accertamento destination) {

		if(source != null) {
			destination.setBilancio(mapNotNull(source.getSiacTBil(), Bilancio.class, FinMapId.SiacTBil_Bilancio));
		}
		
		return destination;
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entity.SiacTVariazione;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * The Class VariazioneBilancioConverter.
 */
@Component
public class VariazioneBilancioConverter extends ExtendedDozerConverter<VariazioneImportoCapitolo, SiacTVariazione> {
	
	public VariazioneBilancioConverter() {
		super(VariazioneImportoCapitolo.class, SiacTVariazione.class);
	}
	
	@Override
	public VariazioneImportoCapitolo convertFrom(SiacTVariazione src, VariazioneImportoCapitolo dest) {
		dest.setBilancio(mapNotNull(src.getSiacTBil(), Bilancio.class, BilMapId.SiacTBil_Bilancio));
		return dest;
	}
	


	@Override
	public SiacTVariazione convertTo(VariazioneImportoCapitolo src, SiacTVariazione dest) {
		dest.setSiacTBil(mapNotNull(src.getBilancio(), SiacTBil.class, BilMapId.SiacTBil_Bilancio));
		return dest;
	}


}

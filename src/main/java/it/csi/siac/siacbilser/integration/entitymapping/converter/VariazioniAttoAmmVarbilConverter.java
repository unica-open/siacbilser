/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRVariazioneStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAmm;

/**
 * The Class VariazioniAttoAmmConverter.
 */
@Component
public class VariazioniAttoAmmVarbilConverter extends VariazioniAttoAmmConverter {

	
	@Override
	protected void setSiacTAttoAmm(SiacRVariazioneStato siacRVariazioneStato, SiacTAttoAmm siacTAttoAmm) {
		siacRVariazioneStato.setSiacTAttoAmmVarbil(siacTAttoAmm);
	}
	
	
	@Override
	protected SiacTAttoAmm getSiacTAttoAmm(SiacRVariazioneStato siacRVariazioneStato) {
		return siacRVariazioneStato.getSiacTAttoAmmVarbil();
	}

	
	

	

}

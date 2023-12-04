/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.fcde;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsigBil;
import it.csi.siac.siacbilser.integration.entity.SiacTBil;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siaccorser.model.Bilancio;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioBilancioConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioBilancioConverter extends ExtendedDozerConverter<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio, SiacTAccFondiDubbiaEsigBil> {

	/**
	 * Costruttore per la superclasse
	 */
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioBilancioConverter() {
		super(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class, SiacTAccFondiDubbiaEsigBil.class);
	}
	
	@Override
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio convertFrom(SiacTAccFondiDubbiaEsigBil src, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio dest) {
		if(dest == null || src == null || src.getSiacTBil() == null) {
			return dest;
		}
		Bilancio bilancio = map(src.getSiacTBil(), Bilancio.class, BilMapId.SiacTBil_Bilancio);
		dest.setBilancio(bilancio);
		return dest;
	}

	@Override
	public SiacTAccFondiDubbiaEsigBil convertTo(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio src, SiacTAccFondiDubbiaEsigBil dest) {
		if(dest == null || src == null || src.getBilancio() == null) {
			return dest;
		}
		SiacTBil siacTBil = new SiacTBil();
		siacTBil.setUid(src.getBilancio().getUid());
		dest.setSiacTBil(siacTBil);
		return dest;
	}

}

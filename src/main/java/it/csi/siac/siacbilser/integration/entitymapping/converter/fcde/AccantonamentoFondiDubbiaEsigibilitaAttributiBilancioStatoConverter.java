/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.fcde;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDAccFondiDubbiaEsigStato;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsigBil;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAccFondiDubbiaEsigStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.StatoAccantonamentoFondiDubbiaEsigibilita;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaStatoConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioStatoConverter extends ExtendedDozerConverter<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio, SiacTAccFondiDubbiaEsigBil> {

	@Autowired private EnumEntityFactory eef;
	/**
	 * Costruttore per la superclasse
	 */
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioStatoConverter() {
		super(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class, SiacTAccFondiDubbiaEsigBil.class);
	}
	
	@Override
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio convertFrom(SiacTAccFondiDubbiaEsigBil src, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio dest) {
		if(dest == null || src == null || src.getSiacDAccFondiDubbiaEsigStato() == null) {
			return dest;
		}
		
		StatoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita = SiacDAccFondiDubbiaEsigStatoEnum.byCodice(src.getSiacDAccFondiDubbiaEsigStato().getAfdeStatoCode()).getStatoAccantonamentoFondiDubbiaEsigibilita();
		dest.setStatoAccantonamentoFondiDubbiaEsigibilita(tipoAccantonamentoFondiDubbiaEsigibilita);;
		return dest;
	}

	@Override
	public SiacTAccFondiDubbiaEsigBil convertTo(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio src, SiacTAccFondiDubbiaEsigBil dest) {
		if(dest == null || src == null || src.getStatoAccantonamentoFondiDubbiaEsigibilita() == null) {
			return dest;
		}
		
		SiacDAccFondiDubbiaEsigStatoEnum siacDAccFondiDubbiaEsigStatoEnum = SiacDAccFondiDubbiaEsigStatoEnum.byStatoAccantonamentoFondiDubbiaEsigibilita(src.getStatoAccantonamentoFondiDubbiaEsigibilita());
		SiacDAccFondiDubbiaEsigStato siacDAccFondiDubbiaEsigStato = eef.getEntity(siacDAccFondiDubbiaEsigStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDAccFondiDubbiaEsigStato.class);
		dest.setSiacDAccFondiDubbiaEsigStato(siacDAccFondiDubbiaEsigStato);
		return dest;
	}

}

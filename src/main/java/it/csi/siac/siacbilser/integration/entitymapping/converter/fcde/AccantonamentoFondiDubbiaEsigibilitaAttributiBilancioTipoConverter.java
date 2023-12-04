/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.fcde;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDAccFondiDubbiaEsigTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsigBil;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAccFondiDubbiaEsigTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaTipoConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioTipoConverter extends ExtendedDozerConverter<AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio, SiacTAccFondiDubbiaEsigBil> {

	@Autowired private EnumEntityFactory eef;
	/**
	 * Costruttore per la superclasse
	 */
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioTipoConverter() {
		super(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio.class, SiacTAccFondiDubbiaEsigBil.class);
	}
	
	@Override
	public AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio convertFrom(SiacTAccFondiDubbiaEsigBil src, AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio dest) {
		if(dest == null || src == null || src.getSiacDAccFondiDubbiaEsigTipo() == null) {
			return dest;
		}
		
		TipoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita = SiacDAccFondiDubbiaEsigTipoEnum.byCodice(src.getSiacDAccFondiDubbiaEsigTipo().getAfdeTipoCode()).getTipoAccantonamentoFondiDubbiaEsigibilita();
		dest.setTipoAccantonamentoFondiDubbiaEsigibilita(tipoAccantonamentoFondiDubbiaEsigibilita);;
		return dest;
	}

	@Override
	public SiacTAccFondiDubbiaEsigBil convertTo(AccantonamentoFondiDubbiaEsigibilitaAttributiBilancio src, SiacTAccFondiDubbiaEsigBil dest) {
		if(dest == null || src == null || src.getTipoAccantonamentoFondiDubbiaEsigibilita() == null) {
			return dest;
		}
		
		SiacDAccFondiDubbiaEsigTipoEnum siacDAccFondiDubbiaEsigTipoEnum = SiacDAccFondiDubbiaEsigTipoEnum.byTipoAccantonamentoFondiDubbiaEsigibilita(src.getTipoAccantonamentoFondiDubbiaEsigibilita());
		SiacDAccFondiDubbiaEsigTipo siacDAccFondiDubbiaEsigTipo = eef.getEntity(siacDAccFondiDubbiaEsigTipoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDAccFondiDubbiaEsigTipo.class);
		dest.setSiacDAccFondiDubbiaEsigTipo(siacDAccFondiDubbiaEsigTipo);
		return dest;
	}

}

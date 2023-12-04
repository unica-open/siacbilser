/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.fcde;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDAccFondiDubbiaEsigTipoMediaConfronto;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.model.fcde.TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaGestioneTipoMediaConfrontoConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaGestioneTipoMediaConfrontoConverter extends ExtendedDozerConverter<AccantonamentoFondiDubbiaEsigibilitaGestione, SiacTAccFondiDubbiaEsig> {

	@Autowired private EnumEntityFactory eef;
	/**
	 * Costruttore per la superclasse
	 * @param clazz la classe dell'accantonamento
	 */
	public AccantonamentoFondiDubbiaEsigibilitaGestioneTipoMediaConfrontoConverter() {
		super(AccantonamentoFondiDubbiaEsigibilitaGestione.class, SiacTAccFondiDubbiaEsig.class);
	}
	
	@Override
	public AccantonamentoFondiDubbiaEsigibilitaGestione convertFrom(SiacTAccFondiDubbiaEsig src, AccantonamentoFondiDubbiaEsigibilitaGestione dest) {
		if(dest == null || src == null || src.getSiacDAccFondiDubbiaEsigTipoMediaConfronto() == null) {
			return dest;
		}
		TipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita = SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum.byCodice(src.getSiacDAccFondiDubbiaEsigTipoMediaConfronto().getAfdeTipoMediaConfCode()).getTipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita();
		
		dest.setTipoMediaConfronto(tipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita);
		return dest;
	}

	@Override
	public SiacTAccFondiDubbiaEsig convertTo(AccantonamentoFondiDubbiaEsigibilitaGestione src, SiacTAccFondiDubbiaEsig dest) {
		if(dest == null || src == null || src.getTipoMediaConfronto() == null) {
			return dest;
		}
		
		SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum siacDAccFondiDubbiaEsigTipoMediaConfrontoEnum = SiacDAccFondiDubbiaEsigTipoMediaConfrontoEnum.byTipoMediaConfrontoAccantonamentoFondiDubbiaEsigibilita(src.getTipoMediaConfronto());
		SiacDAccFondiDubbiaEsigTipoMediaConfronto siacDAccFondiDubbiaEsigTipoMediaConfronto = eef.getEntity(siacDAccFondiDubbiaEsigTipoMediaConfrontoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDAccFondiDubbiaEsigTipoMediaConfronto.class); 
		dest.setSiacDAccFondiDubbiaEsigTipoMediaConfronto(siacDAccFondiDubbiaEsigTipoMediaConfronto);
		return dest;
	}

}

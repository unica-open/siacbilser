/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.fcde;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDAccFondiDubbiaEsigTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAccFondiDubbiaEsigTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaTipoConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaTipoConverter extends ExtendedDozerConverter<AccantonamentoFondiDubbiaEsigibilitaBase<?>, SiacTAccFondiDubbiaEsig> {

	@Autowired private EnumEntityFactory eef;
	/**
	 * Costruttore per la superclasse
	 */
	@SuppressWarnings("unchecked")
	public AccantonamentoFondiDubbiaEsigibilitaTipoConverter() {
		super((Class<AccantonamentoFondiDubbiaEsigibilitaBase<?>>)(Class<?>)AccantonamentoFondiDubbiaEsigibilitaBase.class, SiacTAccFondiDubbiaEsig.class);
	}
	
	@Override
	public AccantonamentoFondiDubbiaEsigibilitaBase<?> convertFrom(SiacTAccFondiDubbiaEsig src, AccantonamentoFondiDubbiaEsigibilitaBase<?> dest) {
		// Gestito direttamente a livello modello
		return dest;
	}

	@Override
	public SiacTAccFondiDubbiaEsig convertTo(AccantonamentoFondiDubbiaEsigibilitaBase<?> src, SiacTAccFondiDubbiaEsig dest) {
		if(dest == null || src == null || src.getTipoAccantonamentoFondiDubbiaEsigiblita() == null) {
			return dest;
		}
		
		SiacDAccFondiDubbiaEsigTipoEnum siacDAccFondiDubbiaEsigTipoEnum = SiacDAccFondiDubbiaEsigTipoEnum.byTipoAccantonamentoFondiDubbiaEsigibilita(src.getTipoAccantonamentoFondiDubbiaEsigiblita());
		SiacDAccFondiDubbiaEsigTipo siacDAccFondiDubbiaEsigTipo = eef.getEntity(siacDAccFondiDubbiaEsigTipoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDAccFondiDubbiaEsigTipo.class); 
		dest.setSiacDAccFondiDubbiaEsigTipo(siacDAccFondiDubbiaEsigTipo);
		return dest;
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.fcde;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDAccFondiDubbiaEsigTipoMedia;
import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAccFondiDubbiaEsigTipoMediaEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.TipoMediaAccantonamentoFondiDubbiaEsigibilita;

/**
 * The Class AccantonamentoFondiDubbiaEsigibilitaTipoMediaConverter.
 */
@Component
public class AccantonamentoFondiDubbiaEsigibilitaTipoMediaConverter extends ExtendedDozerConverter<AccantonamentoFondiDubbiaEsigibilitaBase<?>, SiacTAccFondiDubbiaEsig> {

	@Autowired private EnumEntityFactory eef;
	/**
	 * Costruttore per la superclasse
	 * @param clazz la classe dell'accantonamento
	 */
	@SuppressWarnings("unchecked")
	public AccantonamentoFondiDubbiaEsigibilitaTipoMediaConverter() {
		super((Class<AccantonamentoFondiDubbiaEsigibilitaBase<?>>)(Class<?>)AccantonamentoFondiDubbiaEsigibilitaBase.class, SiacTAccFondiDubbiaEsig.class);
	}
	
	@Override
	public AccantonamentoFondiDubbiaEsigibilitaBase<?> convertFrom(SiacTAccFondiDubbiaEsig src, AccantonamentoFondiDubbiaEsigibilitaBase<?> dest) {
		if(dest == null || src == null || src.getSiacDAccFondiDubbiaEsigTipoMedia() == null) {
			return dest;
		}
		TipoMediaAccantonamentoFondiDubbiaEsigibilita tipoMediaAccantonamentoFondiDubbiaEsigibilita = SiacDAccFondiDubbiaEsigTipoMediaEnum.byCodice(src.getSiacDAccFondiDubbiaEsigTipoMedia().getAfdeTipoMediaCode()).getTipoMediaAccantonamentoFondiDubbiaEsigibilita();
		
		dest.setTipoMediaPrescelta(tipoMediaAccantonamentoFondiDubbiaEsigibilita);

		return dest;
	}

	@Override
	public SiacTAccFondiDubbiaEsig convertTo(AccantonamentoFondiDubbiaEsigibilitaBase<?> src, SiacTAccFondiDubbiaEsig dest) {
		if(dest == null || src == null || src.getTipoMediaPrescelta() == null) {
			return dest;
		}
		
		SiacDAccFondiDubbiaEsigTipoMediaEnum siacDAccFondiDubbiaEsigTipoMediaEnum = SiacDAccFondiDubbiaEsigTipoMediaEnum.byTipoMediaAccantonamentoFondiDubbiaEsigibilita(src.getTipoMediaPrescelta());
		SiacDAccFondiDubbiaEsigTipoMedia siacDAccFondiDubbiaEsigTipoMedia = eef.getEntity(siacDAccFondiDubbiaEsigTipoMediaEnum, dest.getSiacTEnteProprietario().getUid(), SiacDAccFondiDubbiaEsigTipoMedia.class); 
		dest.setSiacDAccFondiDubbiaEsigTipoMedia(siacDAccFondiDubbiaEsigTipoMedia);
		return dest;
	}

}

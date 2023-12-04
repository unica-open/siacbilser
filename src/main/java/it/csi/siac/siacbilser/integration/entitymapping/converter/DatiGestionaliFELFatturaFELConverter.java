/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SirfelTFatturaRepository;
import it.csi.siac.siacbilser.integration.entity.SirfelTDatiGestionali;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.integration.entity.SirfelTFatturaPK;
import it.csi.siac.siacbilser.integration.entitymapping.FelMapId;
import it.csi.siac.sirfelser.model.DatiGestionaliFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;


/**
 * The Class DatiGestionaliFELFatturaFELConverter.
 */
@Component
public class DatiGestionaliFELFatturaFELConverter extends ExtendedDozerConverter<DatiGestionaliFEL, SirfelTDatiGestionali> {
	
	@Autowired
	private SirfelTFatturaRepository sirfelTFatturaRepository;
	
	public DatiGestionaliFELFatturaFELConverter() {
		super(DatiGestionaliFEL.class, SirfelTDatiGestionali.class);
	}

	@Override
	public DatiGestionaliFEL convertFrom(SirfelTDatiGestionali src, DatiGestionaliFEL dest) {
		if(src.getSirfelTFattura() != null) {
			FatturaFEL fatturaFEL = map(src.getSirfelTFattura(), FatturaFEL.class, FelMapId.SirfelTFattura_FatturaFEL_Base);
			dest.setFattura(fatturaFEL);
		}
		
		return dest;
	}

	@Override
	public SirfelTDatiGestionali convertTo(DatiGestionaliFEL src, SirfelTDatiGestionali dest) {
		if(src.getFattura() != null) {
			SirfelTFatturaPK sirfelTFatturaPK = new SirfelTFatturaPK();
			sirfelTFatturaPK.setEnteProprietarioId(src.getEnte().getUid());
			sirfelTFatturaPK.setIdFattura(src.getFattura().getIdFattura());
			
			SirfelTFattura sirfelTFattura = sirfelTFatturaRepository.findOne(sirfelTFatturaPK);
			dest.setSirfelTFattura(sirfelTFattura);
		}
		return dest;
	}

}

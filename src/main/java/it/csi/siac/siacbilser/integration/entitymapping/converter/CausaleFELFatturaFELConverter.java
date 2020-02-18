/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SirfelTFatturaRepository;
import it.csi.siac.siacbilser.integration.entity.SirfelTCausale;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.integration.entity.SirfelTFatturaPK;
import it.csi.siac.siacbilser.integration.entitymapping.FelMapId;
import it.csi.siac.sirfelser.model.CausaleFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;


/**
 * The Class CausaleFELFatturaFELConverter.
 */
@Component
public class CausaleFELFatturaFELConverter extends ExtendedDozerConverter<CausaleFEL, SirfelTCausale> {
	
	@Autowired
	private SirfelTFatturaRepository sirfelTFatturaRepository;
	
	public CausaleFELFatturaFELConverter() {
		super(CausaleFEL.class, SirfelTCausale.class);
	}

	@Override
	public CausaleFEL convertFrom(SirfelTCausale src, CausaleFEL dest) {
		if(src.getSirfelTFattura() != null) {
			FatturaFEL fatturaFEL = map(src.getSirfelTFattura(), FatturaFEL.class, FelMapId.SirfelTFattura_FatturaFEL_Base);
			dest.setFattura(fatturaFEL);
		}
		
		return dest;
	}

	@Override
	public SirfelTCausale convertTo(CausaleFEL src, SirfelTCausale dest) {
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

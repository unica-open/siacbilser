/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SirfelTFatturaRepository;
import it.csi.siac.siacbilser.integration.entity.SirfelTCassaPrevidenziale;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.integration.entity.SirfelTFatturaPK;
import it.csi.siac.siacbilser.integration.entitymapping.FelMapId;
import it.csi.siac.sirfelser.model.CassaPrevidenzialeFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;


/**
 * The Class CassaPrevidenzialeFELFatturaFELConverter.
 */
@Component
public class CassaPrevidenzialeFELFatturaFELConverter extends ExtendedDozerConverter<CassaPrevidenzialeFEL, SirfelTCassaPrevidenziale> {
	
	@Autowired
	private SirfelTFatturaRepository sirfelTFatturaRepository;
	
	public CassaPrevidenzialeFELFatturaFELConverter() {
		super(CassaPrevidenzialeFEL.class, SirfelTCassaPrevidenziale.class);
	}

	@Override
	public CassaPrevidenzialeFEL convertFrom(SirfelTCassaPrevidenziale src, CassaPrevidenzialeFEL dest) {
		if(src.getSirfelTFattura() != null) {
			FatturaFEL fatturaFEL = map(src.getSirfelTFattura(), FatturaFEL.class, FelMapId.SirfelTFattura_FatturaFEL_Base);
			dest.setFattura(fatturaFEL);
		}
		
		return dest;
	}

	@Override
	public SirfelTCassaPrevidenziale convertTo(CassaPrevidenzialeFEL src, SirfelTCassaPrevidenziale dest) {
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

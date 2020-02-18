/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SirfelTPrestatoreRepository;
import it.csi.siac.siacbilser.integration.entity.SirfelTFattura;
import it.csi.siac.siacbilser.integration.entity.SirfelTPrestatore;
import it.csi.siac.siacbilser.integration.entity.SirfelTPrestatorePK;
import it.csi.siac.siacbilser.integration.entitymapping.FelMapId;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.PrestatoreFEL;


/**
 * The Class CassaPrevidenzialeFELFatturaFELConverter.
 */
@Component
public class FatturaFELPrestatoreFELConverter extends ExtendedDozerConverter<FatturaFEL, SirfelTFattura> {
	
	@Autowired
	private SirfelTPrestatoreRepository sirfelTPrestatoreRepository;
	
	public FatturaFELPrestatoreFELConverter() {
		super(FatturaFEL.class, SirfelTFattura.class);
	}

	@Override
	public FatturaFEL convertFrom(SirfelTFattura src, FatturaFEL dest) {
		if(src.getSirfelTPrestatore() != null) {
			PrestatoreFEL prestatoreFEL = map(src.getSirfelTPrestatore(), PrestatoreFEL.class, FelMapId.SirfelTFattura_FatturaFEL_Base);
			dest.setPrestatore(prestatoreFEL);
		}
		
		return dest;
	}

	@Override
	public SirfelTFattura convertTo(FatturaFEL src, SirfelTFattura dest) {
		if(src.getPrestatore() != null) {
			SirfelTPrestatorePK sirfelTPrestatorePK = new SirfelTPrestatorePK();
			sirfelTPrestatorePK.setEnteProprietarioId(src.getEnte().getUid());
			sirfelTPrestatorePK.setIdPrestatore(src.getPrestatore().getIdPrestatore());
			
			SirfelTPrestatore sirfelTPrestatore = sirfelTPrestatoreRepository.findOne(sirfelTPrestatorePK);
			dest.setSirfelTPrestatore(sirfelTPrestatore);
			
			dest.setIdPrestatore(src.getPrestatore().getIdPrestatore());
		}
		return dest;
	}

}

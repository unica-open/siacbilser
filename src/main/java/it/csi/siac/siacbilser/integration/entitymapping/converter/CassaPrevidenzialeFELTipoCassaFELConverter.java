/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SirfelTFatturaRepository;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoCassa;
import it.csi.siac.siacbilser.integration.entity.SirfelDTipoCassaPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTCassaPrevidenziale;
import it.csi.siac.sirfelser.model.CassaPrevidenzialeFEL;
import it.csi.siac.sirfelser.model.TipoCassaFEL;


/**
 * The Class CassaPrevidenzialeFELTipoCassaFELConverter.
 */
@Component
public class CassaPrevidenzialeFELTipoCassaFELConverter extends DozerConverter<CassaPrevidenzialeFEL, SirfelTCassaPrevidenziale> {
	
	@Autowired
	private SirfelTFatturaRepository sirfelTFatturaRepository;
	
	public CassaPrevidenzialeFELTipoCassaFELConverter() {
		super(CassaPrevidenzialeFEL.class, SirfelTCassaPrevidenziale.class);
	}

	@Override
	public CassaPrevidenzialeFEL convertFrom(SirfelTCassaPrevidenziale src, CassaPrevidenzialeFEL dest) {
		if(src.getSirfelDTipoCassa() != null) {
			TipoCassaFEL tipoCassaFEL = TipoCassaFEL.byCodice(src.getSirfelDTipoCassa().getCodice());
			dest.setTipoCassaFEL(tipoCassaFEL);
		}
		
		return dest;
	}

	@Override
	public SirfelTCassaPrevidenziale convertTo(CassaPrevidenzialeFEL src, SirfelTCassaPrevidenziale dest) {
		if(src.getTipoCassaFEL() != null) {
			SirfelDTipoCassaPK sirfelDTipoCassaPK = new SirfelDTipoCassaPK();
			sirfelDTipoCassaPK.setCodice(src.getTipoCassaFEL().getCodice());
			sirfelDTipoCassaPK.setEnteProprietarioId(src.getEnte().getUid());
			
			SirfelDTipoCassa sirfelDTipoCassa = sirfelTFatturaRepository.findSirfelDTipoCassaBySirfelDTipoCassaPK(sirfelDTipoCassaPK);
			dest.setSirfelDTipoCassa(sirfelDTipoCassa);
			
			dest.setTipoCassa(src.getTipoCassaFEL().getCodice());
		}
		return dest;
	}

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SirfelTFatturaRepository;
import it.csi.siac.siacbilser.integration.entity.SirfelDNatura;
import it.csi.siac.siacbilser.integration.entity.SirfelDNaturaPK;
import it.csi.siac.siacbilser.integration.entity.SirfelTCassaPrevidenziale;
import it.csi.siac.sirfelser.model.CassaPrevidenzialeFEL;
import it.csi.siac.sirfelser.model.NaturaFEL;


/**
 * The Class CassaPrevidenzialeFELNaturaFELConverter.
 */
@Component
public class CassaPrevidenzialeFELNaturaFELConverter extends DozerConverter<CassaPrevidenzialeFEL, SirfelTCassaPrevidenziale> {
	
	@Autowired
	private SirfelTFatturaRepository sirfelTFatturaRepository;
	
	public CassaPrevidenzialeFELNaturaFELConverter() {
		super(CassaPrevidenzialeFEL.class, SirfelTCassaPrevidenziale.class);
	}

	@Override
	public CassaPrevidenzialeFEL convertFrom(SirfelTCassaPrevidenziale src, CassaPrevidenzialeFEL dest) {
		if(src.getSirfelDNatura() != null) {
			NaturaFEL naturaFEL = NaturaFEL.byCodice(src.getSirfelDNatura().getCodice());
			dest.setNaturaFEL(naturaFEL);
		}
		
		return dest;
	}

	@Override
	public SirfelTCassaPrevidenziale convertTo(CassaPrevidenzialeFEL src, SirfelTCassaPrevidenziale dest) {
		if(src.getNaturaFEL() != null) {
			SirfelDNaturaPK sirfelDNaturaPK = new SirfelDNaturaPK();
			sirfelDNaturaPK.setCodice(src.getNaturaFEL().getCodice());
			sirfelDNaturaPK.setEnteProprietarioId(src.getEnte().getUid());
			
			SirfelDNatura sirfelDNatura = sirfelTFatturaRepository.findSirfelDNaturaBySirfelDNaturaPK(sirfelDNaturaPK);
			dest.setSirfelDNatura(sirfelDNatura);
			
			dest.setNatura(src.getNaturaFEL().getCodice());
		}
		return dest;
	}

}

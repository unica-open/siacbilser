/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SirfelTPrestatoreRepository;
import it.csi.siac.siacbilser.integration.entity.SirfelDRegimeFiscale;
import it.csi.siac.siacbilser.integration.entity.SirfelDRegimeFiscalePK;
import it.csi.siac.siacbilser.integration.entity.SirfelTPrestatore;
import it.csi.siac.sirfelser.model.PrestatoreFEL;
import it.csi.siac.sirfelser.model.RegimeFiscaleFEL;


/**
 * The Class PrestatoreFELRegimeFiscaleConverter.
 */
@Component
public class PrestatoreFELRegimeFiscaleConverter extends DozerConverter<PrestatoreFEL, SirfelTPrestatore> {
	
	@Autowired
	private SirfelTPrestatoreRepository sirfelTPrestatoreRepository;
	
	/**
	 * Instantiates a new documento entrata attr converter.
	 */
	public PrestatoreFELRegimeFiscaleConverter() {
		super(PrestatoreFEL.class, SirfelTPrestatore.class);
	}

	@Override
	public PrestatoreFEL convertFrom(SirfelTPrestatore src, PrestatoreFEL dest) {
		if(src.getSirfelDRegimeFiscale() != null) {
			RegimeFiscaleFEL regimeFiscaleFEL = RegimeFiscaleFEL.byCodice(src.getSirfelDRegimeFiscale().getCodice());
			dest.setRegimeFiscale(regimeFiscaleFEL);
		}
		
		return dest;
	}

	@Override
	public SirfelTPrestatore convertTo(PrestatoreFEL src, SirfelTPrestatore dest) {
		if(src.getRegimeFiscale() != null) {
			SirfelDRegimeFiscalePK sirfelDRegimeFiscalePK = new SirfelDRegimeFiscalePK();
			sirfelDRegimeFiscalePK.setCodice(src.getRegimeFiscale().getCodice());
			sirfelDRegimeFiscalePK.setEnteProprietarioId(src.getEnte().getUid());
			
			SirfelDRegimeFiscale sirfelDRegimeFiscale = sirfelTPrestatoreRepository.findRegimeFiscaleBySirfelDRegimeFiscalePK(sirfelDRegimeFiscalePK);
			dest.setSirfelDRegimeFiscale(sirfelDRegimeFiscale);
			dest.setRegimeFiscale(src.getRegimeFiscale().getCodice());
		}
		return dest;
	}

}

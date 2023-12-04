/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacgenser.model.Conto;

/**
 * The Class ContoCausaliEPConverter.
 */
@Component
public class ContoCausaliEPConverter extends ExtendedDozerConverter<Conto, SiacTPdceConto> {
	

	/**
	 * Instantiates a new conto causali ep converter.
	 */
	public ContoCausaliEPConverter() {
		super(Conto.class, SiacTPdceConto.class);
	}

	@Override
	public Conto convertFrom(SiacTPdceConto src, Conto dest) {
		
		
		
		return dest;
	}
	

	@Override
	public SiacTPdceConto convertTo(Conto src, SiacTPdceConto dest) {
		
		
		
		return dest;
	}



}

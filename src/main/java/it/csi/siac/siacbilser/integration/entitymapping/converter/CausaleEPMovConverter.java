/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTCausaleEp;
import it.csi.siac.siacgenser.model.CausaleEP;

@Component
public class CausaleEPMovConverter extends ExtendedDozerConverter<CausaleEP, SiacTCausaleEp > {

	public CausaleEPMovConverter() {
		super(CausaleEP.class, SiacTCausaleEp.class);
	}

	@Override
	public CausaleEP convertFrom(SiacTCausaleEp src, CausaleEP dest) {
		return dest;
	}

	@Override
	public SiacTCausaleEp convertTo(CausaleEP src, SiacTCausaleEp dest) {
		return dest;
	}



	

}

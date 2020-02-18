/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacgenser.model.CausaleEP;


/**
 * The Class CausaleEPEnteConverter.
 */
@Component
public class CausaleEPEnteConverter extends EnteBaseConverter<CausaleEP> {
	
	/**
	 * Instantiates a CausaleEPEnteConverter.
	 */
	public CausaleEPEnteConverter() {
		super(CausaleEP.class);
	}

	@Override
	protected void setEnte(CausaleEP dest, Ente ente) {
		dest.setEnte(ente);
	}

	@Override
	protected Ente getEnte(CausaleEP src) {
		return src.getEnte();
	}
}

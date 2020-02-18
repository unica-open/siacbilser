/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;

/**
 * The Class LiquidazioneSiopeAssenzaMotivazioneConverter.
 */
@Component
public class LiquidazioneSiopeAssenzaMotivazioneConverter extends ExtendedDozerConverter<Liquidazione, SiacTLiquidazione> {

	/**
	 * Instantiates a new liquidazione - siope assenza motivazione converter.
	 */
	public LiquidazioneSiopeAssenzaMotivazioneConverter() {
		super(Liquidazione.class, SiacTLiquidazione.class);
	}
	@Override
	public Liquidazione convertFrom(SiacTLiquidazione src, Liquidazione dest) {
		if(src == null || src.getSiacDSiopeAssenzaMotivazione() == null || dest == null){
			return dest;
		}
		
		SiopeAssenzaMotivazione siopeAssenzaMotivazione = map(src.getSiacDSiopeAssenzaMotivazione(), SiopeAssenzaMotivazione.class, BilMapId.SiacDSiopeAssenzaMotivazione_SiopeAssenzaMotivazione);
		dest.setSiopeAssenzaMotivazione(siopeAssenzaMotivazione);
		
		return dest;
	}
	
	@Override
	public SiacTLiquidazione convertTo(Liquidazione src, SiacTLiquidazione dest) {
		return dest;
	}

}

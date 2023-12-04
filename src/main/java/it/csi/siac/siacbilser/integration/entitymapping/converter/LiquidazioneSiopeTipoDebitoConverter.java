/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

/**
 * The Class LiquidazioneSiopeTipoDebitoConverter.
 */
@Component
public class LiquidazioneSiopeTipoDebitoConverter extends ExtendedDozerConverter<Liquidazione, SiacTLiquidazione> {

	/**
	 * Instantiates a new liquidazione - siope tipo debito converter.
	 */
	public LiquidazioneSiopeTipoDebitoConverter() {
		super(Liquidazione.class, SiacTLiquidazione.class);
	}
	@Override
	public Liquidazione convertFrom(SiacTLiquidazione src, Liquidazione dest) {
		if(src == null || src.getSiacDSiopeTipoDebito() == null || dest == null){
			return dest;
		}
		
		SiopeTipoDebito siopeTipoDebito = map(src.getSiacDSiopeTipoDebito(), SiopeTipoDebito.class, BilMapId.SiacDSiopeTipoDebito_SiopeTipoDebito);
		dest.setSiopeTipoDebito(siopeTipoDebito);
		
		return dest;
	}
	
	@Override
	public SiacTLiquidazione convertTo(Liquidazione src, SiacTLiquidazione dest) {
		return dest;
	}

}

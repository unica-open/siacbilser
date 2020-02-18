/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneSoggetto;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
/**
 * The Class LiquidazioneSoggettoConverter.
 */
@Component
public class LiquidazioneSoggettoConverter extends ExtendedDozerConverter<Liquidazione, SiacTLiquidazione >{

	protected LiquidazioneSoggettoConverter() {
		super(Liquidazione.class, SiacTLiquidazione.class);
	}

	@Override
	public Liquidazione convertFrom(SiacTLiquidazione src, Liquidazione dest) {
		if(src.getSiacRLiquidazioneSoggettos() == null){
			return dest;
		}
		for(SiacRLiquidazioneSoggetto siacRLiquidazioneSoggetto : src.getSiacRLiquidazioneSoggettos()){
			if(siacRLiquidazioneSoggetto.getDataFineValidita() == null){
				Soggetto soggetto = new Soggetto();
				map(siacRLiquidazioneSoggetto.getSiacTSoggetto(), soggetto, BilMapId.SiacTSoggetto_Soggetto);
				dest.setSoggettoLiquidazione(soggetto);
				continue;
			}
		}
		return dest;
	}

	@Override
	public SiacTLiquidazione convertTo(Liquidazione src, SiacTLiquidazione dest) {
		return dest;
	}

	
}

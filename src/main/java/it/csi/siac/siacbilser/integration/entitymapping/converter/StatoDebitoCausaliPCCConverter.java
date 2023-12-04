/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDPccCausale;
import it.csi.siac.siacbilser.integration.entity.SiacDPccDebitoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPccDebitoStatoCausale;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.CausalePCC;
import it.csi.siac.siacfin2ser.model.StatoDebito;

/**
 * The Class ImpegnoSoggettoConverter.
 */
@Component
public class StatoDebitoCausaliPCCConverter extends ExtendedDozerConverter<StatoDebito, SiacDPccDebitoStato > {

	/**
	 * Instantiates a new impegno - soggetto converter.
	 */
	public StatoDebitoCausaliPCCConverter() {
		super(StatoDebito.class, SiacDPccDebitoStato.class);
	}
	@Override
	public StatoDebito convertFrom(SiacDPccDebitoStato src, StatoDebito dest) {
		if(src.getSiacRPccDebitoStatoCausales() == null){
			return dest;
		}
		
		dest.setCausaliPCC(new ArrayList<CausalePCC>());
		
		for(SiacRPccDebitoStatoCausale siacRPccDebitoStatoCausale : src.getSiacRPccDebitoStatoCausales()){
			if(siacRPccDebitoStatoCausale.getDataCancellazione() != null){
				continue;
			}
			
			SiacDPccCausale siacDPccCausale = siacRPccDebitoStatoCausale.getSiacDPccCausale();
			
			CausalePCC causalePCC = map(siacDPccCausale, CausalePCC.class, BilMapId.SiacDPccCausale_CausalePCC);
			dest.addCausalePCC(causalePCC);
		
		}
		
		return dest;
	}

	@Override
	public SiacDPccDebitoStato convertTo(StatoDebito src, SiacDPccDebitoStato dest) {
		return dest;
	}

}

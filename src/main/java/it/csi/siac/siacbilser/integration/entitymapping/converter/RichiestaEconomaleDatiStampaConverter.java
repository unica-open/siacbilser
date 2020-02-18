/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.dozer.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTCassaEconStampaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampaValore;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siaccecser.model.RichiestaEconomale;

/**
 * The Class RichiestaEconomaleDatiStampaConverter.
 */
@Component
public class RichiestaEconomaleDatiStampaConverter extends DozerConverter<RichiestaEconomale, SiacTRichiestaEcon > {

	@Autowired
	private SiacTCassaEconStampaRepository siacTCassaEconStampaRepository;
	
	/**
	 * Instantiates a new richiesta economale dati stampa converter.
	 */
	public RichiestaEconomaleDatiStampaConverter() {
		super(RichiestaEconomale.class, SiacTRichiestaEcon.class);
	}

	@Override
	public RichiestaEconomale convertFrom(SiacTRichiestaEcon src, RichiestaEconomale dest) {
		if(src == null || src.getUid() == null) {
			return dest;
		}
		
		// SiacTCassaEconStampaValore <= SiacTCassaEconStampa <= SiacRMovimentoStampa <= SiacTMovimento <= SiacTRichiestaEcon
		List<SiacTCassaEconStampaValore> siacTCassaEconStampaValores = siacTCassaEconStampaRepository.findSiacTCassaEconStampaValoreByRiceconIdAndEnteProprietarioId(src.getUid(),
				src.getSiacTEnteProprietario().getUid());
		if(siacTCassaEconStampaValores == null || siacTCassaEconStampaValores.isEmpty()) {
			return dest;
		}
		
		SiacTCassaEconStampaValore siacTCassaEconStampaValore = siacTCassaEconStampaValores.get(0);
		
		dest.setNumeroRendicontoStampato(siacTCassaEconStampaValore.getRenNum());
		dest.setDataRendicontoStampato(siacTCassaEconStampaValore.getRenData());
		return dest;
	}
	
	@Override
	public SiacTRichiestaEcon convertTo(RichiestaEconomale src, SiacTRichiestaEcon dest) {
		return dest;
	}
	
}

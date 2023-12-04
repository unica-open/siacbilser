/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTCassaEconStampaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconStampaValore;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativo;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;

/**
 * The Class RendicontoRichiestaDatiStampaConverter.
 */
@Component
public class RendicontoRichiestaDatiStampaConverter extends GenericImpegnoSubimpegnoBaseConverter<RendicontoRichiesta, SiacTGiustificativo> {

	@Autowired
	private SiacTCassaEconStampaRepository siacTCassaEconStampaRepository;
	
	/**
	 * Instantiates a new rendiconto richiesta dati stampa converter.
	 */
	public RendicontoRichiestaDatiStampaConverter() {
		super(RendicontoRichiesta.class, SiacTGiustificativo.class);
	}

	@Override
	public RendicontoRichiesta convertFrom(SiacTGiustificativo src, RendicontoRichiesta dest) {
		if(src == null || src.getUid() == null) {
			return dest;
		}
		
		// SiacTCassaEconStampaValore <= SiacTCassaEconStampa <= SiacRMovimentoStampa <= SiacTMovimento <= SiacTRichiestaEcon
		List<SiacTCassaEconStampaValore> siacTCassaEconStampaValores = siacTCassaEconStampaRepository.findSiacTCassaEconStampaValoreByGstIdAndRiceconIdAndEnteProprietarioId(src.getUid(),
				src.getSiacTRichiestaEcon().getUid(), src.getSiacTEnteProprietario().getUid());
		if(siacTCassaEconStampaValores == null || siacTCassaEconStampaValores.isEmpty()) {
			return dest;
		}
		
		SiacTCassaEconStampaValore siacTCassaEconStampaValore = siacTCassaEconStampaValores.get(0);
		
		dest.setNumeroRendicontoStampato(siacTCassaEconStampaValore.getRenNum());
		dest.setDataRendicontoStampato(siacTCassaEconStampaValore.getRenData());
		return dest;
	}
	
	@Override
	public SiacTGiustificativo convertTo(RendicontoRichiesta src, SiacTGiustificativo dest) {
		return dest;
	}
	
}

/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsDetModFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;

@Component
public class ModificaMovimentogestioneSpesaCollegataVincoloEsplicitoConverter 
	extends BaseFinDozerConverter<ModificaMovimentoGestioneSpesaCollegata, SiacRMovgestTsDetModFin> {
	
	ModificaMovimentogestioneSpesaCollegataVincoloEsplicitoConverter() {
		super(ModificaMovimentoGestioneSpesaCollegata.class, SiacRMovgestTsDetModFin.class);
	}

	@Override
	public SiacRMovgestTsDetModFin convertTo(ModificaMovimentoGestioneSpesaCollegata source,
			SiacRMovgestTsDetModFin destination) {
		if(destination == null || source == null) {
			return null;
		}
		//TODO implementare per inserimento
		return null;
	}

	@Override
	public ModificaMovimentoGestioneSpesaCollegata convertFrom(SiacRMovgestTsDetModFin source,
			ModificaMovimentoGestioneSpesaCollegata destination) {
		
		if(source != null) destination.setVincoloEsplicito(Boolean.TRUE);
		
		return destination;
	}

}

/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.integration.dad.ModificaMovimentoGestioneSpesaCollegataDad;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.entitymapping.converter.base.ConvertersFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaFinModelDetail;

@Component
public class ModificaMovimentoGestioneSpesaCollegataImportoResiduoCollegareConverter
		extends BaseFinDozerConverter<ModificaMovimentoGestioneSpesaCollegata, SiacRMovgestTsDetModFin> {
	
	@Autowired
	private ModificaMovimentoGestioneSpesaCollegataDad  modificaMovimentoGestioneSpesaCollegataDad;
	

	public ModificaMovimentoGestioneSpesaCollegataImportoResiduoCollegareConverter() {
		super(ModificaMovimentoGestioneSpesaCollegata.class, SiacRMovgestTsDetModFin.class);
	}

	@Override
	public SiacRMovgestTsDetModFin convertTo(ModificaMovimentoGestioneSpesaCollegata source,SiacRMovgestTsDetModFin destination) {
		if(destination == null || source == null) {
			return null;
		}
		
		//TODO da implementare per l'inserimento
		return destination;
	}

	@Override
	public ModificaMovimentoGestioneSpesaCollegata convertFrom(SiacRMovgestTsDetModFin source,		ModificaMovimentoGestioneSpesaCollegata destination) {
		
		if(source != null) {
			
			SiacTModificaFin siacTModificaFin = source.getSiacTMovgestTsDetModSpesa()
					.getSiacRModificaStato().getSiacTModifica();
			
			BigDecimal importoResiduoCollegare = modificaMovimentoGestioneSpesaCollegataDad.caricaImportoResiduoCollegare(siacTModificaFin.getUid());
			if(importoResiduoCollegare != null) {
				destination.setImportoResiduoCollegare(importoResiduoCollegare);
			}
			
		}
		
		return destination;
	}
	
}
/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.entitymapping.converter.base.ConvertersFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrataFinModelDetail;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;

@Component
public class ModificaMovimentoGestioneSpesaCollegataModificaMovimentoGestioneEntrataConverter
		extends BaseFinDozerConverter<ModificaMovimentoGestioneSpesaCollegata, SiacRMovgestTsDetModFin> {
	
	public ModificaMovimentoGestioneSpesaCollegataModificaMovimentoGestioneEntrataConverter() {
		super(ModificaMovimentoGestioneSpesaCollegata.class, SiacRMovgestTsDetModFin.class);
	}

	@Override
	public SiacRMovgestTsDetModFin convertTo(ModificaMovimentoGestioneSpesaCollegata source,
			SiacRMovgestTsDetModFin destination) {
		if(destination == null || source == null) {
			return null;
		}
		
		//TODO da implementare per l'inserimento
		return destination;
	}

	@Override
	public ModificaMovimentoGestioneSpesaCollegata convertFrom(SiacRMovgestTsDetModFin source,
			ModificaMovimentoGestioneSpesaCollegata destination) {
		
		if(source != null) {
			
			SiacTModificaFin siacTModificaFin = source.getSiacTMovgestTsDetModEntrata()
					.getSiacRModificaStato().getSiacTModifica();
			//TODO l'implemntazione con i converter che sfruttano setter via entities risulta leggermente piu' lenta
			ModificaMovimentoGestioneEntrata modificaEntrata = mapNotNull(siacTModificaFin, ModificaMovimentoGestioneEntrata.class, 
					FinMapId.SiacTModifica_ModificaMovimentoGestioneEntrata, 
					ConvertersFin.byModelDetails(
						ModificaMovimentoGestioneEntrataFinModelDetail.Accertamento, 
						ModificaMovimentoGestioneEntrataFinModelDetail.DatiModificaImportoEntrata
					));
			
			destination.setModificaMovimentoGestioneEntrata(modificaEntrata);
			
		}
		
		return destination;
	}
	
}

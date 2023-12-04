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
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaFinModelDetail;

@Component
public class ModificaMovimentoGestioneSpesaCollegataModificaMovimentoGestioneSpesaConverter
		extends BaseFinDozerConverter<ModificaMovimentoGestioneSpesaCollegata, SiacRMovgestTsDetModFin> {

	public ModificaMovimentoGestioneSpesaCollegataModificaMovimentoGestioneSpesaConverter() {
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
			
			SiacTModificaFin siacTModificaFin = source.getSiacTMovgestTsDetModSpesa()
					.getSiacRModificaStato().getSiacTModifica();
			//TODO l'implemntazione con i converter che sfruttano setter via entities risulta leggermente piu' lenta
			ModificaMovimentoGestioneSpesa modificaSpesa = mapNotNull(siacTModificaFin, ModificaMovimentoGestioneSpesa.class, 
					FinMapId.SiacTModifica_ModificaMovimentoGestioneSpesa,
					ConvertersFin.byModelDetails(
						ModificaMovimentoGestioneSpesaFinModelDetail.Impegno,
						ModificaMovimentoGestioneSpesaFinModelDetail.DatiModificaImportoSpesa
					));
			
			destination.setModificaMovimentoGestioneSpesa(modificaSpesa);
			
		}
		
		return destination;
	}
	
}
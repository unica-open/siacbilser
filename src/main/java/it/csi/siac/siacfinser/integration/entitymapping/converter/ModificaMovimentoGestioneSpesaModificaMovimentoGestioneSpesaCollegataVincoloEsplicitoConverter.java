/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.integration.entitymapping.converter.base.ConvertersFin;
import it.csi.siac.siacfinser.integration.util.DatiOperazioneUtil;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaCollegataFinModelDetail;

@Component
public class ModificaMovimentoGestioneSpesaModificaMovimentoGestioneSpesaCollegataVincoloEsplicitoConverter 
	extends BaseFinDozerConverter<ModificaMovimentoGestioneSpesa, SiacTModificaFin> {

	public ModificaMovimentoGestioneSpesaModificaMovimentoGestioneSpesaCollegataVincoloEsplicitoConverter() {
		super(ModificaMovimentoGestioneSpesa.class, SiacTModificaFin.class);
	}

	@Override
	public SiacTModificaFin convertTo(ModificaMovimentoGestioneSpesa source, SiacTModificaFin destination) {
		if(destination == null || source == null) {
			return null;
		}
		
		//TODO da implementare per l'inserimento
		return destination;
	}

	@Override
	public ModificaMovimentoGestioneSpesa convertFrom(SiacTModificaFin source,
			ModificaMovimentoGestioneSpesa destination) {
		if(source != null) {
			SiacTMovgestTsDetModFin siacTMovgestTsDetModFin = null;
			for (SiacRModificaStatoFin statos : CoreUtil.checkList(source.getSiacRModificaStatos())) {
				//questo controllo dovrebbe essere superfluo ma lo teniamo
				if(statos.getDataCancellazione() == null) {
					//ad ogni modifica corrisponde un solo dettaglio
					siacTMovgestTsDetModFin = CollectionUtils.isNotEmpty(statos.getSiacTMovgestTsDetMods()) 
							? statos.getSiacTMovgestTsDetMods().get(0) : null;
				}
			}
			//inizializzo la lista delle modifiche collegate
			destination.setListaModificheMovimentoGestioneSpesaCollegata(new ArrayList<ModificaMovimentoGestioneSpesaCollegata>());
			
			if(siacTMovgestTsDetModFin != null) {
				List<SiacRMovgestTsDetModFin> modificheCollegate = DatiOperazioneUtil.soloValidi(
						siacTMovgestTsDetModFin.getSiacTMovgestTsDetModsSpesa(), 
						new Timestamp(System.currentTimeMillis()));
				
				ModificaMovimentoGestioneSpesaCollegata mod = null;
				//veniamo dalla spesa, posso avere una unica corrispondenza valida
				if(CollectionUtils.isNotEmpty(modificheCollegate)) {
					SiacRMovgestTsDetModFin siacRMovgestTsDetModFin = modificheCollegate.get(0);
					mod = mapNotNull(siacRMovgestTsDetModFin, 
							ModificaMovimentoGestioneSpesaCollegata.class, 
							FinMapId.ModificaMovimentoGestioneSpesaCollegata_SiacRMovgestTsDetModFin_Default_ModelDetail,
							ConvertersFin.byModelDetails(
								ModificaMovimentoGestioneSpesaCollegataFinModelDetail.VincoloEsplicito
							));
					mod.setImportoCollegamento(BigDecimal.ZERO);
					destination.getListaModificheMovimentoGestioneSpesaCollegata().add(mod);
				}

			}
		}
		return destination;
	}

}

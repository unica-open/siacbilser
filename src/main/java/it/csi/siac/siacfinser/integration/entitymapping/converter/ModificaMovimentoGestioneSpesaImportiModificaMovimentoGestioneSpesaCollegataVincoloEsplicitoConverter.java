/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesaFinModelDetail;

@Component
public class ModificaMovimentoGestioneSpesaImportiModificaMovimentoGestioneSpesaCollegataVincoloEsplicitoConverter
	extends BaseFinDozerConverter<ModificaMovimentoGestioneSpesaCollegata, SiacTModificaFin>{

	public ModificaMovimentoGestioneSpesaImportiModificaMovimentoGestioneSpesaCollegataVincoloEsplicitoConverter() {
		super(ModificaMovimentoGestioneSpesaCollegata.class, SiacTModificaFin.class);
	}

	@Override
	public SiacTModificaFin convertTo(ModificaMovimentoGestioneSpesaCollegata source, SiacTModificaFin destination) {
		if(destination == null || source == null) {
			return null;
		}
		
		//TODO da implementare per l'inserimento
		return destination;
	}

	@Override
	public ModificaMovimentoGestioneSpesaCollegata convertFrom(SiacTModificaFin source,
			ModificaMovimentoGestioneSpesaCollegata destination) {
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
			
			if(siacTMovgestTsDetModFin != null) {
				List<SiacRMovgestTsDetModFin> modificheCollegate = DatiOperazioneUtil.soloValidi(
						siacTMovgestTsDetModFin.getSiacTMovgestTsDetModsSpesa(), 
						new Timestamp(System.currentTimeMillis()));
				
				//veniamo dalla spesa, posso avere una unica corrispondenza valida
				if(CollectionUtils.isNotEmpty(modificheCollegate)) {
					mapNotNull(modificheCollegate.get(0),
							destination, 
							FinMapId.ModificaMovimentoGestioneSpesaCollegata_SiacRMovgestTsDetModFin_ModelDetail,
							ConvertersFin.byModelDetails(
								ModificaMovimentoGestioneSpesaCollegataFinModelDetail.ModificaMovimentoGestioneSpesa,
								ModificaMovimentoGestioneSpesaCollegataFinModelDetail.VincoloEsplicito
							));
				} else {
					//se non ho corrispondenze valide ne si fornisce una di default 
					//che contenga la reimp smarcata
					ModificaMovimentoGestioneSpesa spesa = mapNotNull(
							source, 
							ModificaMovimentoGestioneSpesa.class, 
							FinMapId.SiacTModifica_ModificaMovimentoGestioneSpesa,
							ConvertersFin.byModelDetails(
								ModificaMovimentoGestioneSpesaFinModelDetail.Impegno,
								ModificaMovimentoGestioneSpesaFinModelDetail.DatiModificaImportoSpesa
							));
					
					destination.setModificaMovimentoGestioneSpesa(spesa);
					destination.setImportoResiduoCollegare(getImportoResiduoCollgare(spesa));
					destination.setImportoMaxCollegabile(getImportoMaxCollegabile(siacTMovgestTsDetModFin, destination));
					destination.setImportoCollegamento(BigDecimal.ZERO);
				}
			}
		}
		return destination;
	}

	/**
	 * Restituisco l'importo in assenza di una corrispondenza con la SiacRMovgestTsDetModFin
	 * @param <ModificaMovimentoGestioneSpesa> modificaMovimentoGestioneSpesa
	 * @return <BigDecimal> importoResiduoCollegare
	 */
	private BigDecimal getImportoResiduoCollgare(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa) {
		//lascio questo codice come promemoria ma di fatto questo caso non si dovrebbe mai presentare
		//in quanto viene sempre passato l'importo a ZERO 
//		BigDecimal importoResiduoCollegare = BigDecimal.ZERO;
//		if (importoResiduoCollegare.abs().compareTo(BigDecimal.ZERO) >0 ) {
//			//prendo il minimo tra tutti gli importi residui
//			if (importoResiduoCollegare.abs().compareTo(siacRMovgestTsDetModFin.getMovgestTsModImpoResiduo().abs()) > 0 ) {
//				importoResiduoCollegare = siacRMovgestTsDetModFin.getMovgestTsModImpoResiduo();
//			} 
//		}else {
//			importoResiduoCollegare = siacRMovgestTsDetModFin.getMovgestTsModImpoResiduo();
//		}
		return modificaMovimentoGestioneSpesa.getImportoOld().abs();
	}

	/**
	 * Controllo che l'importo del vincolo sia maggiore del residuo associato alla modifica di spesa collegata 
	 * altrimenti assumiamo l'importo residuo come importo massimo collegabile
	 * @param <SiacTMovgestTsDetModFin> siacTMovgestTsDetModFin
	 * @param <ModificaMovimentoGestioneSpesaCollegata> modificaDiSpesaCollegata
	 * @return <BigDecimal> importoMaxCollegabile
	 */
	private BigDecimal getImportoMaxCollegabile(SiacTMovgestTsDetModFin siacTMovgestTsDetModFin, ModificaMovimentoGestioneSpesaCollegata modificaDiSpesaCollegata) {
		return modificaDiSpesaCollegata.getImportoResiduoCollegare().compareTo(siacTMovgestTsDetModFin.getMovgestTsDetImporto()) <= 0 ?
				modificaDiSpesaCollegata.getImportoResiduoCollegare() : siacTMovgestTsDetModFin.getMovgestTsDetImporto();
	}

}

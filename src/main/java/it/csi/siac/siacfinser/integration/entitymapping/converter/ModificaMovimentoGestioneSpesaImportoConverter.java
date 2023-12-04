/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione.StatoOperativoModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;

@Component
public class ModificaMovimentoGestioneSpesaImportoConverter 
	extends BaseFinDozerConverter<ModificaMovimentoGestioneSpesa, SiacTModificaFin> {

	protected ModificaMovimentoGestioneSpesaImportoConverter() {
		super(ModificaMovimentoGestioneSpesa.class, SiacTModificaFin.class);
	}

	@Override
	public SiacTModificaFin convertTo(ModificaMovimentoGestioneSpesa source,
			SiacTModificaFin destination) {
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
				if(statos.getDataCancellazione() == null) {
					siacTMovgestTsDetModFin = statos.getSiacTMovgestTsDetMods().get(0);
				}
			}
			
			if(siacTMovgestTsDetModFin != null) {
				destination.setUidDatiImportoModificaMovimentoGestione(siacTMovgestTsDetModFin.getUid());
				destination.setImportoOld(siacTMovgestTsDetModFin.getMovgestTsDetImporto());
				destination.setImportoNew(siacTMovgestTsDetModFin.getSiacTMovgestTsDet().getMovgestTsDetImporto());
				
				destination.setReimputazione(siacTMovgestTsDetModFin.getMtdmReimputazioneFlag());
				destination.setAnnoReimputazione(siacTMovgestTsDetModFin.getMtdmReimputazioneAnno());
				
				destination.setCodiceStatoOperativoModificaMovimentoGestione(siacTMovgestTsDetModFin.getSiacRModificaStato().getSiacDModificaStato().getModStatoCode());
				getStatoOperativo(destination);
			}
			
			if(source.getSiacDModificaTipo() != null) {
				destination.setTipoMovimento(source.getSiacDModificaTipo().getModTipoCode());
				destination.setTipoMovimentoDesc(source.getSiacDModificaTipo().getModTipoDesc());
				destination.setTipoModificaMovimentoGestione(destination.getTipoMovimento());
			}
			//SI PASS AD UNA GESTIONE CON ENTITES
			//importi
//			mapNotNull(source, destination, FinMapId.SiacRMovgestTsDetModFin_ImportiModificaMovimentoGestioneSpesa);
			//dati reimputazione
//			mapNotNull(source, destination, FinMapId.SiacRMovgestTsDetModFin_ReimputazioneModificaMovimentoGestioneSpesa);
			//vincolo esplicito
//			mapNotNull(source, destination, FinMapId.SiacRMovgestTsDetModFin_VincoloEsplicito);
		}
		return destination;
	}

	private void getStatoOperativo(ModificaMovimentoGestioneSpesa destination) {
		if(StringUtils.isNotBlank(destination.getCodiceStatoOperativoModificaMovimentoGestione())) {
			destination.setStatoOperativoModificaMovimentoGestione(
				"V".equals(destination.getCodiceStatoOperativoModificaMovimentoGestione()) 
					? StatoOperativoModificaMovimentoGestione.VALIDO : StatoOperativoModificaMovimentoGestione.ANNULLATO);
		}
	}

}

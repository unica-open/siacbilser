/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRModificaStato;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsSogMod;
import it.csi.siac.siacbilser.integration.entity.SiacTModifica;
import it.csi.siac.siacbilser.integration.entity.SiacTSoggetto;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * The Class ModificaMovimentoGestioneSoggettoConverter.
 */
@Component
public class ModificaMovimentoGestioneSoggettoConverter extends ExtendedDozerConverter<ModificaMovimentoGestione, SiacTModifica> {

	/**
	 * Instantiates a new modifica movimento gestione - soggetto converter.
	 */
	public ModificaMovimentoGestioneSoggettoConverter() {
		super(ModificaMovimentoGestione.class, SiacTModifica.class);
	}
	@Override
	public ModificaMovimentoGestione convertFrom(SiacTModifica src, ModificaMovimentoGestione dest) {
		SiacTSoggetto siacTSoggetto = estraiSiacTSoggetto(src);
		if(siacTSoggetto == null || siacTSoggetto.getDataCancellazione() != null){
			return dest;
		}
		
		Soggetto soggetto = convertiSoggetto(siacTSoggetto);
		dest.setSoggettoNewMovimentoGestione(soggetto);
		return dest;
	}

	private SiacTSoggetto estraiSiacTSoggetto(SiacTModifica src) {
		if(src.getSiacRModificaStatos() != null) {
			for(SiacRModificaStato srmc : src.getSiacRModificaStatos()) {
				if(srmc.getDataCancellazione() == null) {
					SiacTSoggetto sts = estraiSiacTSoggettoFromSiacRMovgestTsSogMod(srmc);
					if(sts != null) {
						return sts;
					}
				}
			}
		}
		return null;
	}
	
	private SiacTSoggetto estraiSiacTSoggettoFromSiacRMovgestTsSogMod(SiacRModificaStato srmc) {
		if(srmc.getSiacRMovgestTsSogMods() != null) {
			for(SiacRMovgestTsSogMod srmtsm : srmc.getSiacRMovgestTsSogMods()) {
				// Il 2 dovrebbe essere quello nuovo
				if(srmtsm.getDataCancellazione() == null && srmtsm.getSiacTSoggetto2() != null) {
					return srmtsm.getSiacTSoggetto2();
				}
			}
		}
		return null;
	}
	
	private Soggetto convertiSoggetto(SiacTSoggetto siacTSoggetto) {
		return mapNotNull(siacTSoggetto, Soggetto.class, BilMapId.SiacTSoggetto_Soggetto);
	}
	
	@Override
	public SiacTModifica convertTo(ModificaMovimentoGestione src, SiacTModifica dest) {
		// TODO ?
		return dest;
	}

}

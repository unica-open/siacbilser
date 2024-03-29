/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siaccommon.util.CoreUtil;
import it.csi.siac.siacfinser.integration.entity.SiacRModificaStatoFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogModFin;
import it.csi.siac.siacfinser.integration.entity.SiacRMovgestTsSogclasseModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTModificaFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsDetModFin;
import it.csi.siac.siacfinser.integration.entity.SiacTMovgestTsFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;

@Component
public class ModificaMovimentoGestioneAccertamentoConverter 
	extends BaseFinDozerConverter<ModificaMovimentoGestioneEntrata, SiacTModificaFin> {

	public ModificaMovimentoGestioneAccertamentoConverter() {
		super(ModificaMovimentoGestioneEntrata.class, SiacTModificaFin.class);
	}

	@Override
	public SiacTModificaFin convertTo(ModificaMovimentoGestioneEntrata source, SiacTModificaFin destination) {
		if(destination == null || source == null) {
			return null;
		}
		//TODO implementare per inserimento
		return null;
	}

	@Override
	public ModificaMovimentoGestioneEntrata convertFrom(SiacTModificaFin source, ModificaMovimentoGestioneEntrata destination) {
		
		if(source != null) {
			
			SiacTMovgestTsFin siacTMovgestTsFin = extractSiacTMovgestTs(source);
			
			if(siacTMovgestTsFin == null) {
				return destination;
			}
			
			Accertamento acc = mapNotNull(siacTMovgestTsFin.getSiacTMovgest(), Accertamento.class, FinMapId.SiacTMovgest_Accertamento);
			
			destination.setAccertamento(acc);
			
		}
		
		return destination;
	}

	private SiacTMovgestTsFin extractSiacTMovgestTs(SiacTModificaFin source) {
		for (SiacRModificaStatoFin siacRModificaStatoFin : CoreUtil.checkList(source.getSiacRModificaStatos())) {
			if(siacRModificaStatoFin.getDataCancellazione() != null) {
				continue;
			}
			List<SiacTMovgestTsDetModFin> siacTMovgestTsDetMods = siacRModificaStatoFin.getSiacTMovgestTsDetMods();
			List<SiacRMovgestTsSogclasseModFin> siacRMovgestTsSogclasseMods = siacRModificaStatoFin.getSiacRMovgestTsSogclasseMods();
			List<SiacRMovgestTsSogModFin> siacRMovgestTsSogMods = siacRModificaStatoFin.getSiacRMovgestTsSogMods();
			
			if(siacTMovgestTsDetMods != null) {
				
				for (SiacTMovgestTsDetModFin siacTMovgestTsDetModFin : siacTMovgestTsDetMods) {
					if(siacTMovgestTsDetModFin.getSiacTMovgestT() != null && siacTMovgestTsDetModFin.getSiacTMovgestT().getDataCancellazione() == null) {
						return siacTMovgestTsDetModFin.getSiacTMovgestT() ;
					}
					
					
				}
				
			}else if(siacRMovgestTsSogclasseMods != null) {
				//TODO: testare poi il caso di mapping di modifica di soggetto da classe a soggetto
				for (SiacRMovgestTsSogclasseModFin siacTmovgestMod : siacRMovgestTsSogclasseMods) {
					if(siacTmovgestMod.getSiacTMovgestT() != null && siacTmovgestMod.getSiacTMovgestT().getDataCancellazione() == null) {
						return siacTmovgestMod.getSiacTMovgestT() ;
					}
				}
				
			}else if(siacRMovgestTsSogMods != null) {
				for (SiacRMovgestTsSogModFin siacTmovgestMod : siacRMovgestTsSogMods) {
					if(siacTmovgestMod.getSiacTMovgestT() != null && siacTmovgestMod.getSiacTMovgestT().getDataCancellazione() == null) {
						return siacTmovgestMod.getSiacTMovgestT() ;
					}
				}
				
			}
			
		}
		return  null;
	}

}

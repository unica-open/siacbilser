/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRModificaStato;
import it.csi.siac.siacbilser.integration.entity.SiacTModifica;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestTsDet;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestTsDetMod;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;

/**
 * The Class ModificaMovimentoGestioneImportoConverter.
 */
@Component
public class ModificaMovimentoGestioneImportoConverter extends ExtendedDozerConverter<ModificaMovimentoGestione, SiacTModifica> {

	/**
	 * Instantiates a new modifica movimento gestione - importo converter.
	 */
	public ModificaMovimentoGestioneImportoConverter() {
		super(ModificaMovimentoGestione.class, SiacTModifica.class);
	}
	@Override
	public ModificaMovimentoGestione convertFrom(SiacTModifica src, ModificaMovimentoGestione dest) {
		BigDecimal importoNew = estraiImportoNew(src);
		BigDecimal importoOld = estraiImportoOld(src);
		dest.setImportoNew(importoNew);
		dest.setImportoOld(importoOld);
		return dest;
	}

	private BigDecimal estraiImportoNew(SiacTModifica src) {
		if(src.getSiacRModificaStatos() != null) {
			for(SiacRModificaStato srmc : src.getSiacRModificaStatos()) {
				if(srmc.getDataCancellazione() == null) {
					BigDecimal importo = estraiImportoNewFromSiacTMovgestTsDetMod(srmc);
					if(importo != null) {
						return importo;
					}
				}
			}
		}
		return null;
	}
	
	private BigDecimal estraiImportoNewFromSiacTMovgestTsDetMod(SiacRModificaStato srmc) {
		if(srmc.getSiacTMovgestTsDetMods() != null) {
			for(SiacTMovgestTsDetMod stmtdm : srmc.getSiacTMovgestTsDetMods()) {
				// PRendo l'importo se valorizzato
				if(stmtdm.getDataCancellazione() == null && stmtdm.getMovgestTsDetImporto() != null) {
					return stmtdm.getMovgestTsDetImporto();
				}
			}
		}
		return null;
	}
	
	private BigDecimal estraiImportoOld(SiacTModifica src) {
		if(src.getSiacRModificaStatos() != null) {
			for(SiacRModificaStato srmc : src.getSiacRModificaStatos()) {
				if(srmc.getDataCancellazione() == null) {
					BigDecimal importo = estraiImportoOldFromSiacTMovgestTsDetMod(srmc);
					if(importo != null) {
						return importo;
					}
				}
			}
		}
		return null;
	}
	
	private BigDecimal estraiImportoOldFromSiacTMovgestTsDetMod(SiacRModificaStato srmc) {
		if(srmc.getSiacTMovgestTsDetMods() != null) {
			for(SiacTMovgestTsDetMod stmtdm : srmc.getSiacTMovgestTsDetMods()) {
				if(stmtdm.getSiacTMovgestT() != null) {
					return estraiImportoOldFromSiacTMovgestTs(stmtdm.getSiacTMovgestT());
				}
			}
		}
		return null;
	}
	
	private BigDecimal estraiImportoOldFromSiacTMovgestTs(SiacTMovgestT siacTMovgestT) {
		if(siacTMovgestT.getSiacTMovgestTsDets() != null) {
			for(SiacTMovgestTsDet stmtd : siacTMovgestT.getSiacTMovgestTsDets()) {
				if(stmtd.getDataCancellazione() != null && stmtd.getSiacDMovgestTsDetTipo() != null && SiacDMovgestTsDetTipoEnum.Attuale.getCodice().equals(stmtd.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())) {
					return stmtd.getMovgestTsDetImporto();
				}
			}
		}
		return null;
	}
	@Override
	public SiacTModifica convertTo(ModificaMovimentoGestione src, SiacTModifica dest) {
		// TODO ?
		return dest;
	}

}

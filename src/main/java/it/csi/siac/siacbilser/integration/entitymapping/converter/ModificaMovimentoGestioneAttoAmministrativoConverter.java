/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.entity.SiacTModifica;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;

/**
 * The Class ModificaMovimentoGestioneAttoAmministrativoConverter.
 */
@Component
public class ModificaMovimentoGestioneAttoAmministrativoConverter extends ExtendedDozerConverter<ModificaMovimentoGestione, SiacTModifica> {

	/**
	 * Instantiates a new modifica movimento gestione - atto amministrativo converter.
	 */
	public ModificaMovimentoGestioneAttoAmministrativoConverter() {
		super(ModificaMovimentoGestione.class, SiacTModifica.class);
	}
	
	@Override
	public ModificaMovimentoGestione convertFrom(SiacTModifica src, ModificaMovimentoGestione dest) {
		if(src.getSiacTAttoAmm() == null) {
			return dest;
		}
		AttoAmministrativo attoAmministrativo = mapNotNull(src.getSiacTAttoAmm(), AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo);
		dest.setAttoAmministrativo(attoAmministrativo);
		
		return dest;
	}

	@Override
	public SiacTModifica convertTo(ModificaMovimentoGestione src, SiacTModifica dest) {
		// TODO ?
		return dest;
	}

}

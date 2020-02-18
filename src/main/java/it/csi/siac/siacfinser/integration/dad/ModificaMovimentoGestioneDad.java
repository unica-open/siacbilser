/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacfinser.Constanti;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDModificaStatoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacDModificaTipoRepository;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTModificaRepository;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestione;

@Component
@Transactional
public class ModificaMovimentoGestioneDad extends AbstractFinDad {

	@Autowired
	private SiacDModificaStatoRepository siacDModificaStatoRepository;
	
	@Autowired
	private SiacDModificaTipoRepository siacDModificaTipoRepository;
	
	@Autowired
	private SiacTModificaRepository siacTModificaRepository;
	
	/**
	 * Gets the importo attuale modifica.
	 *
	 * @param modifica the modifica
	 * @return the importo attuale modifica
	 */
	public BigDecimal getImportoAttualeModifica(ModificaMovimentoGestione modifica) {;
		return siacTModificaRepository.findMovgestTsDetImportoModByModIdAndMovgestTsDetTipoCode(modifica.getUid(), Constanti.MOVGEST_TS_DET_TIPO_ATTUALE);
	}
	
	
}
			

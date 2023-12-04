/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.fcde;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsigBil;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface AccantonamentoFondiDubbiaEsigibilitaDao.
 */
public interface AccantonamentoFondiDubbiaEsigibilitaAttributiBilancioDao extends Dao<SiacTAccFondiDubbiaEsigBil, Integer> {
	
	@Override
	SiacTAccFondiDubbiaEsigBil create(SiacTAccFondiDubbiaEsigBil tafde);

	@Override
	SiacTAccFondiDubbiaEsigBil update(SiacTAccFondiDubbiaEsigBil tafde);
	
	Page<SiacTAccFondiDubbiaEsigBil> ricerca(
			Integer bilId,
			String afdeTipoCode,
			String afdeStatoCode,
			Integer afdeBilVersione,
			Integer afdeBilVersioneNotEqual,
			Pageable pageable);
	
	/**
	 * Elimina una SiacTAccFondiDubbiaEsig.
	 *
	 * @param tafdeb la SiacTAccFondiDubbiaEsigBil da eliminare
	 * @return la SiacTAccFondiDubbiaEsigBil eliminata
	 */
	SiacTAccFondiDubbiaEsigBil logicalDelete(SiacTAccFondiDubbiaEsigBil tafdeb);
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTAccFondiDubbiaEsig;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface AccantonamentoFondiDubbiaEsigibilitaDao.
 */
public interface AccantonamentoFondiDubbiaEsigibilitaDao extends Dao<SiacTAccFondiDubbiaEsig, Integer> {
	
	/**
	 * Crea una SiacTAccFondiDubbiaEsig.
	 *
	 * @param tafde la SiacTAccFondiDubbiaEsig da inserire
	 * @return la SiacTAccFondiDubbiaEsig inserita
	 */
	SiacTAccFondiDubbiaEsig create(SiacTAccFondiDubbiaEsig tafde);

	@Override
	SiacTAccFondiDubbiaEsig update(SiacTAccFondiDubbiaEsig tafde);
	
	/**
	 * Elimina una SiacTAccFondiDubbiaEsig.
	 *
	 * @param tafde la SiacTAccFondiDubbiaEsig da eliminare
	 * @return la SiacTAccFondiDubbiaEsig eliminata
	 */
	SiacTAccFondiDubbiaEsig logicalDelete(SiacTAccFondiDubbiaEsig tafde);

	/**
	 * Effettua la ricerca sintetica paginata di SiacTAccFondiDubbiaEsig con i filtri passati come parametro.
	 * @param enteProprietarioId l'id dell'ente proprietario
	 * @param bilId l'id del bilancio
	 * @param elemTipoCode il tipo di capitolo
	 * @param pageable the pageable
	 * @return la lista paginata di SiacTAccFondiDubbiaEsig
	 */
	Page<SiacTAccFondiDubbiaEsig> ricercaSintetica(Integer enteProprietarioId, Integer bilId, String elemTipoCode, Pageable pageable);
	
}

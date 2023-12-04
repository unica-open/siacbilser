/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import it.csi.siac.siacbilser.integration.entity.SiacTOrdine;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface OrdineDao.
 */
public interface OrdineDao extends Dao<SiacTOrdine, Integer> {
	
	/**
	 * Crea una SiacTOrdine.
	 *
	 * @param o la SiacTOrdine da inserire
	 * @return la SiacTOrdine inserita
	 */
	SiacTOrdine create(SiacTOrdine o);

	/**
	 * Aggiorna una SiacTOrdine.
	 *
	 * @param o la SiacTOrdine da aggiornare
	 * @return la SiacTOrdine aggiornata
	 */
	SiacTOrdine update(SiacTOrdine o);

	
}

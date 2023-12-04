/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface DettaglioOnereDao.
 */
public interface DettaglioOnereDao extends Dao<SiacRDocOnere, Integer> {
	
	/**
	 * Crea una SiacRDocOnere.
	 *
	 * @param r la SiacRDocOnere da inserire
	 * 
	 * @return la SiacRDocOnere inserita
	 */
	SiacRDocOnere create(SiacRDocOnere r);
	
	/**
	 * Aggiorna una SiacRDocOnere.
	 *
	 * @param r la SiacRDocOnere da aggiornare
	 * 
	 * @return la SiacRDocOnere aggiornata
	 */
	SiacRDocOnere update(SiacRDocOnere r);
	
	/**
	 * Elimina una SiacRDocOnere.
	 *
	 * @param r la SiacRDocOnere da eliminare
	 * 
	 */
	void delete(SiacRDocOnere r);

}

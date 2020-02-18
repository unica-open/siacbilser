/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import it.csi.siac.siacbilser.integration.entity.SiacTFile;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface FileDao.
 */
public interface FileDao extends Dao<SiacTFile, Integer> {
	
	/**
	 * Crea una SiacTFile.
	 *
	 * @param c la SiacTFile da inserire
	 * @return la SiacTFile inserita
	 */
	SiacTFile create(SiacTFile d);

	/**
	 * Aggiorna una SiacTFile.
	 *
	 * @param c la SiacTFile da aggiornare
	 * @return la SiacTFile aggiornata
	 */
	SiacTFile update(SiacTFile d);
}

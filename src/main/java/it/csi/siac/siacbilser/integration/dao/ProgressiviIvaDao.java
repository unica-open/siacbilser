/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistroTotale;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface ProgressiviIvaDao.
 */
public interface ProgressiviIvaDao extends Dao<SiacTIvaRegistroTotale, Integer> {
	
	/**
	 * Creates the SiacTIvaRegistroTotale.
	 *
	 * @param siacTIvaRegistroTotale the SiacTIvaRegistroTotale
	 * @return the SiacTIvaRegistroTotale
	 */
	SiacTIvaRegistroTotale create(SiacTIvaRegistroTotale siacTIvaRegistroTotale);

	@Override
	SiacTIvaRegistroTotale update(SiacTIvaRegistroTotale s);

}

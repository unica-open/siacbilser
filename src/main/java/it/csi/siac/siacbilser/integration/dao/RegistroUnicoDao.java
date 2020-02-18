/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import it.csi.siac.siacbilser.integration.entity.SiacTRegistrounicoDoc;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface RegistroUnicoDao.
 * @author Domenico
 */
public interface RegistroUnicoDao extends Dao<SiacTRegistrounicoDoc, Integer> {
	
	/**
	 * Creates the RegistroUnico.
	 *
	 * @param d the d
	 * @return the siac t registrounico doc
	 */
	SiacTRegistrounicoDoc create(SiacTRegistrounicoDoc d);

	/**
	 * Update RegistroUnico.
	 *
	 * @param d the d
	 * @return the siac t registrounico doc
	 */
	SiacTRegistrounicoDoc update(SiacTRegistrounicoDoc d);

	
}

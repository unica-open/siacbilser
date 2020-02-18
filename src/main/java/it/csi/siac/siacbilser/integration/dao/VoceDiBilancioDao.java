/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import it.csi.siac.siacbilser.model.VoceDiBilancio;

// TODO: Auto-generated Javadoc
/**
 * The Interface VoceDiBilancioDao.
 */
public interface VoceDiBilancioDao {

	/**
	 * Save.
	 *
	 * @param voceDiBilancio the voce di bilancio
	 * @return the voce di bilancio
	 */
	VoceDiBilancio save(VoceDiBilancio voceDiBilancio);

	/**
	 * Find by id and lock.
	 *
	 * @param id the id
	 * @return the voce di bilancio
	 */
	VoceDiBilancio findByIdAndLock(int id);

	/**
	 * List.
	 *
	 * @return the list
	 */
	List<VoceDiBilancio> list();

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the voce di bilancio
	 */
	VoceDiBilancio findById(int id);

}

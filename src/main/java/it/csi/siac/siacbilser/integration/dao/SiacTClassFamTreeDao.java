/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import it.csi.siac.siacbilser.integration.entity.SiacTClassFamTree;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTClassFamTreeDao.
 */
public interface SiacTClassFamTreeDao {
	
	
	/**
	 * Find by id.
	 *
	 * @param uid the uid
	 * @return the siac t class fam tree
	 */
	SiacTClassFamTree findById(Integer uid);
}
/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.provvedimento;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmClass;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;



// TODO: Auto-generated Javadoc
/**
 * The Interface SiacRAttoAmmClassDao.
 */
public interface SiacRAttoAmmClassDao extends Dao<SiacRAttoAmmClass, Integer> {
	
	
	/**
	 * Creates the.
	 *
	 * @param attoLegge the atto legge
	 * @return the siac r atto amm class
	 */
	SiacRAttoAmmClass create(SiacRAttoAmmClass attoLegge);

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.Dao#update(java.lang.Object)
	 */
	SiacRAttoAmmClass update(SiacRAttoAmmClass attoDiLeggeDB);
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.Dao#findById(java.lang.Object)
	 */
	SiacRAttoAmmClass findById (Integer uid);
	
}

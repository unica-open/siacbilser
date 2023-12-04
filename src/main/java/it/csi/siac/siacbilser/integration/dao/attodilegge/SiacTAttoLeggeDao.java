/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.attodilegge;

import it.csi.siac.siacbilser.integration.entity.SiacTAttoLegge;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;



// TODO: Auto-generated Javadoc
/**
 * The Interface SiacTAttoLeggeDao.
 */
public interface SiacTAttoLeggeDao extends Dao<SiacTAttoLegge,Integer> {
	
	
	/**
	 * Creates the.
	 *
	 * @param attoLegge the atto legge
	 * @return the siac t atto legge
	 */
	SiacTAttoLegge create(SiacTAttoLegge attoLegge);

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.Dao#update(java.lang.Object)
	 */
	SiacTAttoLegge update(SiacTAttoLegge attoDiLeggeDB);
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.Dao#findById(java.lang.Object)
	 */
	SiacTAttoLegge findById (Integer uid);
	
}

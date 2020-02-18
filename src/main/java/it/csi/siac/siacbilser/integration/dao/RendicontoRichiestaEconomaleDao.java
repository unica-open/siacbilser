/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativo;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface RendicontoRichiestaEconomaleDao.
 *
 * @author Domenico
 */
public interface RendicontoRichiestaEconomaleDao extends Dao<SiacTGiustificativo, Integer> {
	
	
	/**
	 * Creates the.
	 *
	 * @param r the r
	 * @return the siac t giustificativo
	 */
	SiacTGiustificativo create(SiacTGiustificativo r);


	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.integration.dao.base.Dao#update(java.lang.Object)
	 */
	SiacTGiustificativo update(SiacTGiustificativo r);
	
}

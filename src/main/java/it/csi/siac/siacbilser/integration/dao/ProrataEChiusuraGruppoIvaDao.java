/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import it.csi.siac.siacbilser.integration.entity.SiacTIvaProrata;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface ProrataEChiusuraGruppoIvaDao.
 */
public interface ProrataEChiusuraGruppoIvaDao extends Dao<SiacTIvaProrata, Integer> {
	
	/**
	 * Crea una SiacTIvaProrata.
	 *
	 * @param siacTIvaProrata la SiacTIvaProrata da inserire
	 * 
	 * @return la SiacTIvaProrata inserita
	 */
	SiacTIvaProrata create(SiacTIvaProrata siacTIvaProrata);

	/**
	 * Aggiorna una SiacTIvaProrata.
	 *
	 * @param siacTIvaProrata la SiacTIvaProrata da aggiornare
	 * 
	 * @return la SiacTIvaProrata aggiornata
	 */
	SiacTIvaProrata update(SiacTIvaProrata siacTIvaProrata);
									
	

}

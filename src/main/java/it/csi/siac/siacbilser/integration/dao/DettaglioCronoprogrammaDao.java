/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import it.csi.siac.siacbilser.integration.entity.SiacTCronopElem;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


// TODO: Auto-generated Javadoc
/**
 * The Interface DettaglioCronoprogrammaDao.
 */
public interface DettaglioCronoprogrammaDao extends Dao<SiacTCronopElem, Integer> {

	/**
	 * Crea una SiacTCronopElem.
	 *
	 * @param c la SiacTCronopElem da inserire
	 * 
	 * @return la SiacTCronopElem inserita
	 */
	SiacTCronopElem create(SiacTCronopElem c);
	
	/**
	 * Aggiorna una SiacTCronopElem.
	 *
	 * @param c la SiacTCronopElem da aggiornare
	 * 
	 * @return la SiacTCronopElem aggiornata
	 */
	SiacTCronopElem update(SiacTCronopElem c);
	
	/**
	 * Elimina una SiacTCronopElem.
	 *
	 * @param c la SiacTCronopElem da eliminare
	 * 
	 */
	void delete(SiacTCronopElem c);

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import it.csi.siac.siacbilser.integration.entity.SiacRBilElemIvaAttivita;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface AttivitaIvaRelazioneCapitoloDao.
 */
public interface AttivitaIvaRelazioneCapitoloDao extends Dao<SiacRBilElemIvaAttivita, Integer> {
	
	/**
	 * Crea una SiacRBilElemIvaAttivita.
	 *
	 * @param siacRBilElemIvaAttivita la SiacRBilElemIvaAttivita da inserire
	 * 
	 * @return la SiacRBilElemIvaAttivita inserita
	 */
	SiacRBilElemIvaAttivita create(SiacRBilElemIvaAttivita siacRBilElemIvaAttivita);
	
	/**
	 * Elimina una SiacRBilElemIvaAttivita.
	 *
	 * @param siacRBilElemIvaAttivita la SiacRBilElemIvaAttivita da eliminare
	 */
	@Override
	void delete(SiacRBilElemIvaAttivita siacRBilElemIvaAttivita);

	
	
}

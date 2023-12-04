/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.math.BigDecimal;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEcon;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface CassaEconomaleDao.
 */
public interface CassaEconomaleDao extends Dao<SiacTCassaEcon, Integer> {
	

	/**
	 * Aggiorna una SiacTCassaEcon.
	 *
	 * @param c la SiacTCassaEcon da aggiornare
	 * 
	 * @return la SiacTCassaEcon aggiornata
	 */
	SiacTCassaEcon update(SiacTCassaEcon c);

	/**
	 * Calcola l'importo derivato.
	 * 
	 * @param cassaeconId  l'uid della cassa
	 * @param annoBilancio 
	 * @param functionName il nome della function
	 * @return l'importo della function
	 */
	BigDecimal findImportoDerivato(Integer cassaeconId, Integer annoBilancio, String functionName);
	
}

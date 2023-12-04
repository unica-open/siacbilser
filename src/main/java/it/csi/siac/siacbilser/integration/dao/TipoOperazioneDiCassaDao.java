/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacDCassaEconOperazTipo;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface CassaEconomaleDao.
 */
public interface TipoOperazioneDiCassaDao extends Dao<SiacDCassaEconOperazTipo, Integer> {
	

	/**
	 * Inserisce una SiacDCassaEconOperazTipo.
	 *
	 * @param c la SiacDCassaEconOperazTipo da inserire
	 * 
	 * @return la SiacDCassaEconOperazTipo inserita
	 */
	SiacDCassaEconOperazTipo create(SiacDCassaEconOperazTipo siacDCassaEconOperazTipo);
	
	/**
	 * Aggiorna una SiacDCassaEconOperazTipo.
	 *
	 * @param c la SiacDCassaEconOperazTipo da aggiornare
	 * 
	 * @return la SiacDCassaEconOperazTipo aggiornata
	 */
	SiacDCassaEconOperazTipo update(SiacDCassaEconOperazTipo c);
	
	
	/**
	 * Ricerca le SiacDCassaEconOperazTipo corrispondenti ai parametri passati in input.
	 *
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param cassaeconopTipoCode codice del tipo operazione
	 * @param cassaeconopTipoDesc descrizione del tipo operazione
	 * @param cassaeconId id della cassa economale
	 * @param pageable per la paginazione
	 * 
	 * @return la lista paginata di SiacDCassaEconOperazTipo trovata
	 * 
	 */
	Page<SiacDCassaEconOperazTipo> ricercaSinteticaTipoOperazioneCassa(
			Integer enteProprietarioId , 
			String cassaeconopTipoCode, 
			String cassaeconopTipoDesc, 
			Integer cassaeconId,
			Pageable pageable);


	
	
}

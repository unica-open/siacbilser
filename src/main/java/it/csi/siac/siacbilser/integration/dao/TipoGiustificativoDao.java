/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacDGiustificativo;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface TipoGiustificativoDao.
 */
public interface TipoGiustificativoDao extends Dao<SiacDGiustificativo, Integer> {
	

	/**
	 * Inserisce una SiacDGiustificativo.
	 *
	 * @param g la SiacDGiustificativo da inserire
	 * 
	 * @return la SiacDGiustificativo inserita
	 */
	SiacDGiustificativo create(SiacDGiustificativo g);
	
	/**
	 * Aggiorna una SiacDGiustificativo.
	 *
	 * @param g la SiacDGiustificativo da aggiornare
	 * 
	 * @return la SiacDGiustificativo aggiornata
	 */
	SiacDGiustificativo update(SiacDGiustificativo g);

	Page<SiacDGiustificativo> ricercaSinteticaTipoGiustificativo(
				Integer enteProprietarioId, 
				String giustCode,
				String giustDesc, 
				String giustTipoCode,
				Integer cassaeconId,
				Pageable pageable);
	
	
//	/**
//	 * Ricerca le SiacDCassaEconOperazTipo corrispondenti ai parametri passati in input.
//	 *
//	 * @param enteProprietarioId uid dell'ente proprietario
//	 * @param cassaeconopTipoCode codice del tipo operazione
//	 * @param cassaeconopTipoDesc dercizione del tipo operazione
//	 * 
//	 * @return la lista paginata di SiacDCassaEconOperazTipo trovata
//	 * 
//	 */
//	Page<SiacDCassaEconOperazTipo> ricercaSinteticaTipoOperazioneCassa(
//			Integer enteProprietarioId , 
//			String cassaeconopTipoCode, 
//			String cassaeconopTipoDesc, 
//			Pageable pageable);


	
	
}

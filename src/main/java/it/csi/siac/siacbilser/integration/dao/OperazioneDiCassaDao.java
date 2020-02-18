/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconOperaz;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;

/**
 * The Interface CassaEconomaleDao.
 */
public interface OperazioneDiCassaDao extends Dao<SiacTCassaEconOperaz, Integer> {
	

	/**
	 * Inserisce una SiacTCassaEconOperaz.
	 *
	 * @param siacTCassaEconOperaz la SiacTCassaEconOperaz da inserire
	 * 
	 * @return la SiacTCassaEconOperaz inserita
	 */
	SiacTCassaEconOperaz create(SiacTCassaEconOperaz siacTCassaEconOperaz);
	
	/**
	 * Aggiorna una SiacDCassaEconOperazTipo.
	 *
	 * @param siacTCassaEconOperaz la SiacTCassaEconOperaz da aggiornare
	 * 
	 * @return la SiacTCassaEconOperaz aggiornata
	 */
	SiacTCassaEconOperaz update(SiacTCassaEconOperaz siacTCassaEconOperaz);
	
	
	/**
	 * Ricerca le SiacTCassaEconOperaz corrispondenti ai parametri passati in input.
	 *
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param 
	 * @param 
	 * 
	 * @return la lista paginata di SiacTCassaEconOperaz trovata
	 * 
	 */
	Page<SiacTCassaEconOperaz> ricercaSinteticaOperazioneCassa(
			Integer bilId,
			Integer cassaeconId,
			Integer enteProprietarioId , 
			Date dataInizioValidita,
			Integer cassaeconopTipoId, 
			String cassaeconopStatoCode, 
			List<String> cassaeconopStatoCodesDaescludere,
			Pageable pageable);
	
	/**
	 * Ricerca le SiacTCassaEconOperaz corrispondenti ai parametri passati in input.
	 *
	 * @param enteProprietarioId uid dell'ente proprietario
	 * @param 
	 * @param 
	 * 
	 * @return la lista paginata di SiacTCassaEconOperaz trovata
	 * 
	 */
	Page<SiacTCassaEconOperaz> ricercaSinteticaOperazioneCassaPerPeriodo(
			Integer bilId,
			Integer cassaeconId,
			Integer enteProprietarioId , 
			Date dataInizioPeriodo,
			Date dataFinePeriodo,
			Integer cassaeconopTipoId, 
			String cassaeconopStatoCode,
			List<String> cassaeconopStatoCodesDaescludere,
			Pageable pageable);
	
	//SIAC-5890
	/**
	 * 
	 * @param dataPeriodoInizio
	 * @param dataPeriodoFine
	 * @param cassaEconomaleUid
	 * @param enteUid
	 * @param bilancioId
	 * @return
	 */
	 
	Long contaOperazioniDiCassa(Date dataPeriodoInizio, Date dataPeriodoFine, Integer cassaEconomaleUid, Integer enteUid,Integer bilancioId);



	
	
}

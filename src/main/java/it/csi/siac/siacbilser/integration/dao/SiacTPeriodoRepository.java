/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;



import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;

/**
 * The Interface SiacTPeriodoRepository.
 */
public interface SiacTPeriodoRepository extends JpaRepository<SiacTPeriodo, Integer> {
	
	/*
	 * 
	 * Cerca per data inizio ed ente proprietario
	 * 
	 * @param dataInizio
	 * @param ente
	 * @return
	 */
	//PeriodoDto findByDataInizioAndEnteProprietarioAndDataFineIsNotNull(Date dataInizio, EnteDto ente);
	
	/**
	 * Cerca il periodo annuale valido attualmente per l'ente selezionato.
	 *
	 * @param ente the ente
	 * @return the siac t periodo
	 */
	@Query("FROM SiacTPeriodo p " 
			+ " WHERE CURRENT_DATE BETWEEN dataInizio AND dataFine "
			+ " and siacTEnteProprietario=:ente "
			+ " and p.siacDPeriodoTipo.periodoTipoCode = 'SY' " 
			+ " and p.dataCancellazione is null " 
			+ " and p.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " and ( p.dataFineValidita is null or p.dataFineValidita > CURRENT_TIMESTAMP)")
	SiacTPeriodo findByDataAttualeAndEnteProprietario(@Param("ente")SiacTEnteProprietario ente);
	

	/**
	 * Cerca un periodo valido per l'ente a partire dalla data passata come parametro e dal tipo di periodo scelto. (annuale, trimestrale, semestrale, ecc..)
	 *
	 * @param data the data
	 * @param ente the ente
	 * @param tipo the tipo
	 * @return the siac t periodo
	 */
	@Query(" FROM SiacTPeriodo p " 
			+ " WHERE :data BETWEEN p.dataInizio AND p.dataFine "
			+ " AND p.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND p.siacDPeriodoTipo.periodoTipoCode = :tipo " 
			+ " AND p.dataCancellazione is null " 
			+ " AND p.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND ( p.dataFineValidita IS NULL OR p.dataFineValidita > CURRENT_TIMESTAMP)")
	SiacTPeriodo findByDataAndEnteProprietarioAndTipoPeriodo(@Param("data") Date data, @Param("enteProprietarioId") Integer enteProprietarioId, @Param("tipo")String tipo);

	/**
	 * Cerca un periodo annuale valido per un ente.
	 *
	 * @param anno the anno
	 * @param ente the ente
	 * @return the siac t periodo
	 */
	@Query("FROM SiacTPeriodo p " 
			+ " WHERE anno=:anno " 
			+ " and siacTEnteProprietario=:ente "
			+ " and p.siacDPeriodoTipo.periodoTipoCode = 'SY' "
			+ " and p.dataCancellazione is null " 
			+ " and p.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " and ( p.dataFineValidita is null or p.dataFineValidita > CURRENT_TIMESTAMP)")
	SiacTPeriodo findByAnnoAndEnteProprietario(@Param("anno") String anno, @Param("ente")SiacTEnteProprietario ente);
	
	/**
	 * Cerca un periodo annuale valido per un ente.
	 *
	 * @param anno the anno
	 * @param ente the ente
	 * @return the siac t periodo
	 */
	@Query("FROM SiacTPeriodo p " 
			+ " WHERE anno=:anno " 
			+ " and p.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId "
			+ " and p.siacDPeriodoTipo.periodoTipoCode = 'SY' "
			+ " and p.dataCancellazione is null " 
			+ " and p.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " and ( p.dataFineValidita is null or p.dataFineValidita > CURRENT_TIMESTAMP)")
	SiacTPeriodo findByAnnoAndEnteProprietario(@Param("anno") String anno,  @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
	/**
	 * Cerca un periodo valido per anno, tipo periodo e ente proprietario
	 * 
	 * @param anno anno 
	 * @param periodoTipoCode codice tipo periodo
	 * @param enteProprietarioId uid ente proprietario
	 * @return
	 */
	@Query("FROM SiacTPeriodo p " 
			+ " WHERE p.anno=:anno " 
			+ " and p.siacTEnteProprietario.enteProprietarioId=:enteProprietarioId "
			+ " and p.siacDPeriodoTipo.periodoTipoCode = :periodoTipoCode "
			+ " and p.dataCancellazione is null " 
			+ " and p.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " and ( p.dataFineValidita is null or p.dataFineValidita > CURRENT_TIMESTAMP)")
	SiacTPeriodo findByAnnoAndPeriodoTipoAndEnteProprietario(@Param("anno") String anno,@Param("periodoTipoCode") String periodoTipoCode,  @Param("enteProprietarioId") Integer enteProprietarioId);
	
	
}

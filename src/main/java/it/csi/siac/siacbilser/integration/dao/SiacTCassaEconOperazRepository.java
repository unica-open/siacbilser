/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconOperaz;

public interface SiacTCassaEconOperazRepository extends JpaRepository<SiacTCassaEconOperaz, Integer> {

	@Query(" SELECT COALESCE(COUNT(tceo), 0) "
			+ " FROM SiacTCassaEconOperaz tceo "
			+ " WHERE tceo.dataCancellazione IS NULL "
			+ " AND tceo.siacTBil.bilId = :bilId "
			+ " AND EXISTS ( "
			+ "     FROM tceo.siacRCassaEconOperazStatos rceos "
			+ "     WHERE rceos.dataCancellazione IS NULL "
			+ "     AND rceos.siacDCassaEconOperazStato.cassaeconopStatoCode IN (:cassaeconopStatoCodes) "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tceo.siacRCassaEconOperazTipos rceot "
			+ "     WHERE rceot.dataCancellazione IS NULL "
			+ "     AND rceot.siacDCassaEconOperazTipo.cassaeconopTipoId = :cassaeconopTipoId "
			+ " ) "
			)
	Long countBySiacDCassaEconOperazTipoAndSiacTBilAndSiacDCassaEconOperazStatoIn(@Param("cassaeconopTipoId") Integer cassaeconopTipoId, @Param("bilId") Integer bilId,
			@Param("cassaeconopStatoCodes") Collection<String> cassaeconopStatoCodes);
	
	
	@Query( " FROM SiacTCassaEconOperaz tceo "
			+ " WHERE tceo.dataCancellazione IS NULL "
			+ " AND tceo.siacTBil.bilId = :bilId "
			+ " AND tceo.dataInizioValidita = :dataInizioValidita "
			+ " AND tceo.siacTCassaEcon.cassaeconId = :cassaeconId "
			+ " AND EXISTS ( "
			+ "     FROM tceo.siacRCassaEconOperazStatos rceos "
			+ "     WHERE rceos.dataCancellazione IS NULL "
			+ "     AND rceos.siacDCassaEconOperazStato.cassaeconopStatoCode IN (:cassaeconopStatoCodes) "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tceo.siacRCassaEconOperazTipos rceot "
			+ "     WHERE rceot.dataCancellazione IS NULL "
			+ "     AND rceot.siacDCassaEconOperazTipo.cassaeconopTipoId = :cassaeconopTipoId "
			+ " ) "
			)
	List<SiacTCassaEconOperaz>  findByDataStampaGiornale( @Param("bilId") Integer bilId,@Param("cassaeconId") Integer cassaeconId, @Param("cassaeconopTipoId") Integer cassaeconopTipoId,@Param("dataInizioValidita") Date dataInizioValidita,
			@Param("cassaeconopStatoCodes") Collection<String> cassaeconopStatoCodes);

	@Query( " FROM SiacTCassaEconOperaz tceo "
			+ " WHERE tceo.dataCancellazione IS NULL "
			+ " AND tceo.siacTBil.bilId = :bilId "
			//+ " AND tceo.dataInizioValidita >= :dataInizioPeriodo  AND tceo.dataInizioValidita < :dataFinePeriodo"
			+ " AND ("
			+ "		(DATE_TRUNC('day', CAST(tceo.dataInizioValidita AS date)) >= DATE_TRUNC('day', CAST(:dataInizioPeriodo AS date)) )"
			+ "		AND  (DATE_TRUNC('day', CAST(tceo.dataInizioValidita AS date)) <= DATE_TRUNC('day', CAST(:dataFinePeriodo AS date)) )"
			+ "     )"
			+ " AND tceo.siacTCassaEcon.cassaeconId = :cassaeconId "
			+ " AND EXISTS ( "
			+ "     FROM tceo.siacRCassaEconOperazStatos rceos "
			+ "     WHERE rceos.dataCancellazione IS NULL "
			+ "     AND rceos.siacDCassaEconOperazStato.cassaeconopStatoCode IN (:cassaeconopStatoCodes) "
			+ " ) "
			+ " AND EXISTS ( "
			+ "     FROM tceo.siacRCassaEconOperazTipos rceot "
			+ "     WHERE rceot.dataCancellazione IS NULL "
			+ "     AND rceot.siacDCassaEconOperazTipo.cassaeconopTipoId = :cassaeconopTipoId "
			+ " ) "
			)
	List<SiacTCassaEconOperaz>  findByPeriodoForStampaGiornale( @Param("bilId") Integer bilId,@Param("cassaeconId") Integer cassaeconId, @Param("cassaeconopTipoId") Integer cassaeconopTipoId,
			@Param("dataInizioPeriodo") Date dataInizioPeriodo,@Param("dataFinePeriodo") Date dataFinePeriodo,
			@Param("cassaeconopStatoCodes") Collection<String> cassaeconopStatoCodes);

}

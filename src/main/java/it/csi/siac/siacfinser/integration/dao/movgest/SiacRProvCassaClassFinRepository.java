/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.movgest;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRProvCassaClassFin;

public interface SiacRProvCassaClassFinRepository extends JpaRepository<SiacRProvCassaClassFin, Integer>  {
	
	String condizione = " ( ( srpc.dataInizioValidita < :dataInput)  AND ( srpc.dataFineValidita IS NULL OR :dataInput < srpc.dataFineValidita) AND  srpc.dataCancellazione IS NULL ) ";
	
	@Query("FROM SiacRProvCassaClassFin srpc WHERE srpc.siacTProvCassaFin.provcId = :provvCassaId AND "+condizione)
	public List<SiacRProvCassaClassFin> findAllValidiByProvvCassa(@Param("provvCassaId") Integer provvCassaId,@Param("dataInput") Timestamp  dataInput);
	
	
	@Query("FROM SiacRProvCassaClassFin srpc WHERE srpc.siacTProvCassaFin.provcId = :provvCassaId ")
	public List<SiacRProvCassaClassFin> findAllByProvvCassa(@Param("provvCassaId") Integer provvCassaId);
	
	@Modifying
	@Query("UPDATE SiacRProvCassaClassFin srpc "
			+ " SET srpc.loginOperazione=:loginOperazione, "
			+ " srpc.dataFineValidita=CURRENT_TIMESTAMP, "
			+ " srpc.dataCancellazione=CURRENT_TIMESTAMP "
			+ " WHERE srpc.siacTProvCassaFin.provcId=:provvCassaId "
			+ " AND dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (dataFineValidita IS NULL OR CURRENT_TIMESTAMP < dataFineValidita) "
			+ " AND srpc.dataCancellazione IS NULL ")
	public void logicalRemove(
			@Param("provvCassaId") Integer provvCassaId,
			@Param("loginOperazione") String loginOperazione
		);
	
	
}

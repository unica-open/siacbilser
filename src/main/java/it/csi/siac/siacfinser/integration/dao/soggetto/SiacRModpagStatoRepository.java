/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dao.soggetto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacfinser.integration.entity.SiacRModpagStatoFin;

public interface SiacRModpagStatoRepository extends JpaRepository<SiacRModpagStatoFin, Integer> {

	@Query("FROM SiacRModpagStatoFin x "
			+ " WHERE x.dataCancellazione IS NULL "
			+ " AND x.dataInizioValidita < CURRENT_TIMESTAMP "
			+ " AND (x.dataFineValidita IS NULL OR x.dataFineValidita > CURRENT_TIMESTAMP) "
			+ " AND  x.siacTModpag.modpagId = :modpagId ")
	public SiacRModpagStatoFin findStatoValidoByModpagId(@Param("modpagId") Integer mdpId);
	
}

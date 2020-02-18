/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTProvCassa;

/**
 * Repository per l'entity SiacTSubdocRepository.
 *
 */
public interface SiacTProvCassaBilRepository extends JpaRepository<SiacTProvCassa, Integer> {

	@Query("SELECT pc.provcCausale " +
			" FROM SiacTProvCassa pc " +
			" WHERE pc.provcId = :provcId ")
	String findCausaleByUidProvCassa(@Param("provcId")Integer provcId);

	@Query("SELECT r.siacTProvCassa " +
			" FROM SiacRSubdocProvCassa r " +
			" WHERE r.siacTSubdoc.subdocId = :subdocId " +
			" AND r.dataCancellazione IS NULL")
	SiacTProvCassa findSiacTProvCassaBySubdocId(@Param("subdocId")Integer subdocId);
	
	
}

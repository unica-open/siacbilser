/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoElencoDoc;

/**
 * Repository per l'entity SiacRAttoAllegatoElencoDoc.
 *
 */
public interface SiacRAttoAllegatoElencoDocRepository extends JpaRepository<SiacRAttoAllegatoElencoDoc, Integer> {
	
	@Query(" FROM SiacRAttoAllegatoElencoDoc r "
			+ " WHERE r.siacTAttoAllegato.attoalId = :attoalId "
			+ " AND r.siacTAttoAllegato.dataCancellazione IS NULL "
			+ " AND r.siacTElencoDoc.eldocId = :eldocId "
			+ " AND r.siacTElencoDoc.dataCancellazione IS NULL "
			+ " AND r.dataCancellazione IS NULL ")
	List<SiacRAttoAllegatoElencoDoc> findBySiacTElencoDocAndSiacTAttoAllegato(@Param("eldocId") Integer eldocId, @Param("attoalId") Integer attoalId);
	
	@Query(" FROM SiacRAttoAllegatoElencoDoc r "
			+ " WHERE r.siacTAttoAllegato.attoalId = :attoalId "
			+ " AND r.siacTAttoAllegato.dataCancellazione IS NULL "
			+ " AND r.dataCancellazione IS NULL ")
	List<SiacRAttoAllegatoElencoDoc> findBySiacTAttoAllegato(@Param("attoalId") Integer attoalId);
	
	@Query(" FROM SiacRAttoAllegatoElencoDoc r "
			+ " WHERE r.siacTElencoDoc.eldocId = :eldocId "
			+ " AND r.siacTElencoDoc.dataCancellazione IS NULL "
			+ " AND r.dataCancellazione IS NULL ")
	List<SiacRAttoAllegatoElencoDoc> findBySiacTElencoDoc(@Param("eldocId") Integer eldocId);
	
	@Query(" SELECT COUNT(*) "
			+ " FROM SiacRAttoAllegatoElencoDoc r "
			+ " WHERE r.siacTElencoDoc.eldocId = :eldocId "
			+ " AND r.siacTElencoDoc.dataCancellazione IS NULL "
			+ " AND r.dataCancellazione IS NULL ")
	Long countBySiacTElencoDoc(@Param("eldocId") Integer eldocId);
	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocAttoAmm;

/**
 * Repository per l'entity SiacRSubdocAttoAmm.
 *
 */
public interface SiacRSubdocAttoAmmRepository extends JpaRepository<SiacRSubdocAttoAmm, Integer> {
	
	@Query(" FROM SiacRSubdocAttoAmm r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.subdocId = :subdocId "
			+ " AND EXISTS ( "
			+ "     FROM r.siacTAttoAmm.siacTAttoAllegatos aa "
			+ "     WHERE aa.dataCancellazione IS NULL "
			+ "     AND aa.attoalId = :attoalId "
			+ " ) ")
	List<SiacRSubdocAttoAmm> findBySubdocIdAndAttoalId(@Param("subdocId") Integer subdocId, @Param("attoalId") Integer attoalId);

	@Query(" FROM SiacRSubdocAttoAmm r "
			+ " WHERE r.dataCancellazione IS NULL "
			+ " AND r.siacTSubdoc.subdocId = :subdocId ")
	List<SiacRSubdocAttoAmm> findBySubdocId(@Param("subdocId") Integer subdocId);
	
}

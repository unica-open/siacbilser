/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocSubdoc;

/**
 * Repository per l'entity SiacRElencoDocSubdoc.
 *
 */
public interface SiacRElencoDocSubdocRepository extends JpaRepository<SiacRElencoDocSubdoc, Integer> {

	@Query(" FROM SiacRElencoDocSubdoc r "
			+ " WHERE r.siacTElencoDoc.eldocId = :uid "
			+ " AND r.dataCancellazione IS NULL "
			)
	List<SiacRElencoDocSubdoc> findByElenco(@Param("uid") Integer uid);
	
}

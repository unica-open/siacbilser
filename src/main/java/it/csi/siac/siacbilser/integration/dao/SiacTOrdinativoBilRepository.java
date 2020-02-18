/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativo;

/**
 * Repository per l'entity SiacTOrdinativo.
 *
 */
public interface SiacTOrdinativoBilRepository extends JpaRepository<SiacTOrdinativo, Integer> {

	@Query("SELECT r.siacTBilElem.elemId "
			+ " FROM SiacROrdinativoBilElem r "
			+ " WHERE r.dataCancellazione IS NULL "
			+"  AND r.siacTOrdinativo.ordId = :ordId ")
	int findIdCapitoloByOrdinativo(@Param("ordId") Integer ordId);
	
	
	
}

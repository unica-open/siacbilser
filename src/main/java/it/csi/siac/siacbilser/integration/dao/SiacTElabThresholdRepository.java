/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTElabThreshold;

/**
 * The Interface SiacTElabThresholdRepository.
 */
public interface SiacTElabThresholdRepository extends JpaRepository<SiacTElabThreshold, Integer> {
	
	@Query(" FROM SiacTElabThreshold tet "
			+ " WHERE tet.dataCancellazione IS NULL "
			+ " AND tet.elthresCode = :elthresCode "
			+ " ORDER BY tet.dataInizioValidita ")
	List<SiacTElabThreshold> findByElthresCode(@Param("elthresCode") String elthresCode);
	
}

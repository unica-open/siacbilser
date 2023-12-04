/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTElencoDocNum;

// TODO: Auto-generated Javadoc
/**
 * Repository per l'entity SiacTSubdocNum.
 *
 */
public interface SiacTElencoDocNumRepository extends JpaRepository<SiacTElencoDocNum, Integer> {
	
	
	
	/**
	 * Find by bil id.
	 *
	 * @param bilId the bil id
	 * @return the siac t subdoc num
	 */
	@Query("FROM SiacTElencoDocNum WHERE siacTBil.bilId = :bilId")
	SiacTElencoDocNum findByBilId(@Param("bilId") Integer bilId);
	

	
}

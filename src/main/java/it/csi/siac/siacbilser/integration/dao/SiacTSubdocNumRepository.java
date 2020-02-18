/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTSubdocNum;

// TODO: Auto-generated Javadoc
/**
 * Repository per l'entity SiacTSubdocNum.
 *
 */
public interface SiacTSubdocNumRepository extends JpaRepository<SiacTSubdocNum, Integer> {
	
	
	/**
	 * Find by doc id.
	 *
	 * @param uidDocumento the uid documento
	 * @return the siac t subdoc num
	 */
	@Query("FROM SiacTSubdocNum WHERE siacTDoc.docId = :docId")
	SiacTSubdocNum findByDocId(@Param("docId") Integer uidDocumento);
	

	
}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTOrdine;

/**
 * Repository per l'entity SiacTOrdine.
 *
 */
public interface SiacTOrdineRepository extends JpaRepository<SiacTOrdine, Integer> {
	
	
	@Query(" FROM SiacTOrdine o  "
			+ " WHERE o.dataCancellazione IS NULL "
			+ " AND EXISTS (FROM o.siacRDocOrdines rdo "
			+ "            	WHERE rdo.dataCancellazione IS NULL "
			+ "             AND rdo.siacTDoc.docId = :docId"
			+ "             )")
	List<SiacTOrdine> findOrdiniByDocumentoSpesa(@Param("docId") Integer uidDoc);
	
	@Query(" SELECT COALESCE(COUNT(o), 0)  "
			+" FROM SiacTOrdine o  "
			+ " WHERE o.dataCancellazione IS NULL "
			+ " AND EXISTS (FROM o.siacRDocOrdines rdo "
			+ "            	WHERE rdo.dataCancellazione IS NULL "
			+ "             AND rdo.siacTDoc.docId = :docId"
			+ "             )")
	Long countOrdiniByDocumentoSpesa(@Param("docId") Integer uidDoc);
	

}

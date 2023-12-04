/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRDocStato;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacRDocStatoRepository.
 */
public interface SiacRDocStatoRepository extends JpaRepository<SiacRDocStato, Integer> {
	

	/**
	 * Find doc stato by doc id orderedy by data creazione.
	 *
	 * @param docId the doc id
	 * @return the list
	 */
	@Query(" FROM SiacRDocStato r  " +	
			"WHERE r.siacTDoc.docId = :docId " +
			"ORDER BY r.dataCreazione "
			)
	List<SiacRDocStato> findDocStatoByDocIdOrderedyByDataCreazione(@Param("docId") Integer docId);
	
	
}

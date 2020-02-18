/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRPredocStato;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacRPredocStatoRepository.
 */
public interface SiacRPredocStatoRepository extends JpaRepository<SiacRPredocStato, Integer> {
	

	/**
	 * Find predoc stato by predoc id orderedy by data creazione.
	 *
	 * @param predocId the predoc id
	 * @return the list
	 */
	@Query(" FROM SiacRPredocStato r  " +	
			"WHERE r.siacTPredoc.predocId = :predocId " +
			"ORDER BY r.dataCreazione "
			)
	List<SiacRPredocStato> findPredocStatoByPredocIdOrderedyByDataCreazione(@Param("predocId") Integer predocId);
	
	
}

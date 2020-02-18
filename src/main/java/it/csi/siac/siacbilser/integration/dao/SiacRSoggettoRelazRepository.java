/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRSoggettoRelaz;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacRSoggettoRelazRepository.
 */
public interface SiacRSoggettoRelazRepository extends JpaRepository<SiacRSoggettoRelaz, Integer> {
	
	
	/**
	 * Find siac r soggetto relaz.
	 *
	 * @param soggettoId the soggetto id
	 * @return the list
	 */
	@Query(" FROM SiacRSoggettoRelaz r WHERE r.siacTSoggetto2.soggettoId = :soggettoId " +
			" AND r.siacDRelazTipo.relazTipoCode = 'SEDE_SECONDARIA'" +
			" AND r.dataCancellazione IS null "

			)
	List<SiacRSoggettoRelaz> findSiacRSoggettoRelaz(@Param("soggettoId") Integer soggettoId);
	
	
}


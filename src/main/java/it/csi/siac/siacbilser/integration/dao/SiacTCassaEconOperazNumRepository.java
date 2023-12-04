/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTCassaEconOperazNum;

// TODO: Auto-generated Javadoc
/**
 * Repository per l'entity SiacTSubdocNum.
 *
 */
public interface SiacTCassaEconOperazNumRepository extends JpaRepository<SiacTCassaEconOperazNum, Integer> {
	
	
	/**
	 * Find by cassa id.
	 *
	 * @param uidCassa the uid cassa
	 * @return the SiacTCassaEconOperazNum
	 */
	@Query("FROM SiacTCassaEconOperazNum WHERE siacTCassaEcon.cassaeconId = :cassaeconId")
	SiacTCassaEconOperazNum findByCassaId(@Param("cassaeconId") Integer uidCassa);
	

	
}

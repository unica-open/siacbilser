/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacRVincoloBilElem;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacRVincoloBilElemRepository.
 */
public interface SiacRVincoloBilElemRepository extends JpaRepository<SiacRVincoloBilElem, Integer> {

	/**
	 * Scollega capitolo al vincolo.
	 *
	 * @param vincoloId the vincolo id
	 * @param elemId the elem id
	 */
	@Query("UPDATE SiacRVincoloBilElem "
			+ " SET dataCancellazione = CURRENT_TIMESTAMP, dataFineValidita = CURRENT_TIMESTAMP "
		    + " WHERE  siacTVincolo.vincoloId = :vincoloId "
			+ " AND siacTBilElem.elemId = :elemId "
		    + " AND dataCancellazione is null " )
			//+ " AND ( dataFineValidita is null OR dataFineValidita > CURRENT_TIMESTAMP) " )
	@Modifying
	void scollegaCapitoloAlVincolo(@Param("vincoloId") Integer vincoloId, @Param("elemId") Integer elemId);
	
}

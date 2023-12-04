/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDOnereTipo;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDOnereTipoRepository.
 */
public interface SiacDOnereTipoRepository extends JpaRepository<SiacDOnereTipo, Integer> {
	
	
	/**
	 * Find nature onere by ente.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query(  "SELECT c " +
			" FROM SiacDOnereTipo c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita >= :now) " +
			" ORDER BY c.onereTipoCode ")
	List<SiacDOnereTipo> findNatureOnereByEnte(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("now") Date now);
		
}

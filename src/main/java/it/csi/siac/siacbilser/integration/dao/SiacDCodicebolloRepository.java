/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDCodicebollo;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDCodicebolloRepository.
 */
public interface SiacDCodicebolloRepository extends JpaRepository<SiacDCodicebollo, Integer> {
	
	
	/**
	 * Find codici bollo by ente.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query(  "SELECT c " +
			" FROM SiacDCodicebollo c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) " +
			" ORDER BY c.codbolloCode ")
	List<SiacDCodicebollo> findCodiciBolloByEnte(@Param("enteProprietarioId") Integer enteProprietarioId);
	
	
	/**
	 * Find codici bollo by codice.
	 *
	 * @param codice the codice
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac d codicebollo
	 */
	@Query(  "SELECT c " +
			" FROM SiacDCodicebollo c " +
			" WHERE c.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId " +
			" AND codbolloCode = :codbolloCode " +
			" AND dataCancellazione IS NULL " +
			" AND dataInizioValidita < CURRENT_TIMESTAMP "+
			" AND (dataFineValidita IS NULL OR dataFineValidita > CURRENT_TIMESTAMP) " +
			" ORDER BY c.codbolloCode ")
	SiacDCodicebollo findCodiciBolloByCodice(@Param("codbolloCode") String codice, @Param("enteProprietarioId") Integer enteProprietarioId);
		
}

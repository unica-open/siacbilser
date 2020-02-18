/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDAttoLeggeTipo;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDAttoLeggeTipoRepository.
 */
public interface SiacDAttoLeggeTipoRepository extends
		JpaRepository<SiacDAttoLeggeTipo, Integer> {
	
	
	
	/**
	 * Ricerca tipo atto amm.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param codice the codice
	 * @return the siac d atto legge tipo
	 */
	@Query("FROM SiacDAttoLeggeTipo "
			+ " WHERE attoleggeTipoCode = :codice "	
			+ " AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dataCancellazione IS NULL ")
	SiacDAttoLeggeTipo ricercaTipoAttoAmm(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("codice") String codice);
	
	
	/**
	 * Elenco tipi.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query("FROM SiacDAttoLeggeTipo "
			+ " WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dataCancellazione IS NULL ")
	List<SiacDAttoLeggeTipo> elencoTipi(@Param("enteProprietarioId") Integer enteProprietarioId);
	
}

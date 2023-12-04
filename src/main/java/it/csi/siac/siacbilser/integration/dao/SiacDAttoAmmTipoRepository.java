/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDAttoAmmTipo;

// TODO: Auto-generated Javadoc
/**
 * The Interface SiacDAttoAmmTipoRepository.
 */
public interface SiacDAttoAmmTipoRepository extends
		JpaRepository<SiacDAttoAmmTipo, Integer> {
	
	
	
	/**
	 * Ricerca tipo atto amm.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param codice the codice
	 * @return the siac d atto amm tipo
	 */
	@Query("FROM SiacDAttoAmmTipo "
			+ " WHERE attoammTipoCode = :codice "
			+ " AND siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dataCancellazione IS NULL ")
	SiacDAttoAmmTipo ricercaTipoAttoAmm(@Param("enteProprietarioId") Integer enteProprietarioId, @Param("codice") String codice);
	
	/**
	 * Ricerca tipo atto amm a partire da ente e descrizione.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @param codice the codice
	 * @return the siac d atto amm tipo
	 */
	@Query("FROM SiacDAttoAmmTipo d "
			+ " WHERE UPPER(d.attoammTipoDesc) = UPPER(:attoammTipoDesc) "
			+ " AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND d.dataCancellazione IS NULL ")
	SiacDAttoAmmTipo findSiacDAttoAmmTipoByEnteProprietarioIdAndAttoammTipoDesc(@Param("enteProprietarioId") Integer enteProprietarioId,
			@Param("attoammTipoDesc") String attoammTipoDesc);
	
	
	/**
	 * Elenco tipi.
	 *
	 * @param enteProprietarioId the ente proprietario id
	 * @return the list
	 */
	@Query("FROM SiacDAttoAmmTipo "
			+ " WHERE siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND dataCancellazione IS NULL ")
	List<SiacDAttoAmmTipo> elencoTipi(@Param("enteProprietarioId") Integer enteProprietarioId);
	
}

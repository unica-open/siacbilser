/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDAttoAmmStato;

/**
 * The Interface SiacDAttoAmmStatoRepository.
 */
public interface SiacDAttoAmmStatoRepository extends
		JpaRepository<SiacDAttoAmmStato, Integer> {

	/** The Constant ricercaStatoAttoAmm. */
	static final String RICERCA_STATO_ATTO_AMM = "FROM SiacDAttoAmmStato stato "
			+ "WHERE  stato.attoammStatoCode = :codice "
			+ "AND stato.dataCancellazione IS NULL "
			+ "AND stato.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ";

	/**
	 * Ricerca stato atto amm.
	 *
	 * @param codice the codice
	 * @param enteProprietarioId the ente proprietario id
	 * @return the siac d atto amm stato
	 */
	@Query(RICERCA_STATO_ATTO_AMM)
	SiacDAttoAmmStato ricercaStatoAttoAmm(@Param("codice") String codice, @Param("enteProprietarioId") Integer enteProprietarioId);

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacDRichiestaEconTipo;

/**
 * The Interface SiacDRichiestaEconTipoRepository.
 */
public interface SiacDRichiestaEconTipoRepository extends JpaRepository<SiacDRichiestaEconTipo, Integer> {
	

	@Query(" SELECT t.riceconTipoId "
			+ " FROM SiacDRichiestaEconTipo t "
			+ " WHERE t.dataCancellazione IS NULL "
			+ " AND t.riceconTipoCode = :riceconTipoCode"
			+ " AND t.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
	Integer findUidTipoRichiestaByCodiceEEnte(@Param("riceconTipoCode") String riceconTipoCode, @Param("enteProprietarioId") Integer enteProprietarioId);
	
}

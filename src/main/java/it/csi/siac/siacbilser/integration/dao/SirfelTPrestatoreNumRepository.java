/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SirfelTPrestatoreNum;

/**
 * The Interface SirfelTPrestatoreNumRepository.
 */
public interface SirfelTPrestatoreNumRepository extends JpaRepository<SirfelTPrestatoreNum, Integer> {

	@Query(" FROM SirfelTPrestatoreNum tpn "
			+ " WHERE tpn.dataCancellazione IS NULL "
			+ " AND tpn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
	SirfelTPrestatoreNum findByEnteProprietarioId(@Param("enteProprietarioId") Integer uid);
}

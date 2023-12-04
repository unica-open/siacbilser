/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SirfelTFatturaNum;

/**
 * The Interface SirfelTFatturaNumRepository.
 */
public interface SirfelTFatturaNumRepository extends JpaRepository<SirfelTFatturaNum, Integer> {

	@Query(" FROM SirfelTFatturaNum tfn "
			+ " WHERE tfn.dataCancellazione IS NULL "
			+ " AND tfn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId ")
	SirfelTFatturaNum findByEnteProprietarioId(@Param("enteProprietarioId") Integer uid);

}

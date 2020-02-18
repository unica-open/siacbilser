/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEconNum;

/**
 * The Interface SiacDAttoAmmStatoRepository.
 */
public interface SiacTRichiestaEconNumRepository extends JpaRepository<SiacTRichiestaEconNum, Integer> {

	
	
	@Query(" FROM SiacTRichiestaEconNum ren "
			+ " WHERE ren.siacTCassaEcon.cassaeconId = :cassaeconId "
			+ " AND ren.riceconAnno = :riceconAnno "
			+ " AND ren.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND ren.dataCancellazione IS NULL ")
	SiacTRichiestaEconNum findByCassaEconId(@Param("cassaeconId") Integer cassaeconId, @Param("riceconAnno") String riceconAnno, @Param("enteProprietarioId") Integer enteProprietarioId);

}

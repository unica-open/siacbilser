/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEconSospesaNum;

/**
 * The Interface SiacDAttoAmmStatoRepository.
 */
public interface SiacTRichiestaEconSospesaNumRepository extends
		JpaRepository<SiacTRichiestaEconSospesaNum, Integer> {

	
	
	@Query(" FROM SiacTRichiestaEconSospesaNum resn "
			+ " WHERE resn.rieconsAnno = :rieconsAnno "
			+ " AND resn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND resn.siacTCassaEcon.cassaeconId = :cassaeconId "
			+ " AND resn.dataCancellazione IS NULL ")
	SiacTRichiestaEconSospesaNum findByAnnoAndCassaId(@Param("rieconsAnno") String rieconsAnno, @Param("cassaeconId") Integer cassaeconId, @Param("enteProprietarioId") Integer enteProprietarioId);

}

/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.csi.siac.siacbilser.integration.entity.SiacTMovimentoNum;

/**
 * The Interface SiacDAttoAmmStatoRepository.
 */
public interface SiacTMovimentoNumRepository extends JpaRepository<SiacTMovimentoNum, Integer> {

	
	
	@Query(" FROM SiacTMovimentoNum mn "
			+ " WHERE mn.movtAnno = :movtAnno "
			+ " AND mn.siacTCassaEcon.cassaeconId = :cassaeconId "
			+ " AND mn.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND mn.dataCancellazione IS NULL ")
	SiacTMovimentoNum findByAnnoAndCassaUid(@Param("movtAnno") String movtAnno, @Param("cassaeconId") Integer cassaeconId, @Param("enteProprietarioId") Integer enteProprietarioId);

}
